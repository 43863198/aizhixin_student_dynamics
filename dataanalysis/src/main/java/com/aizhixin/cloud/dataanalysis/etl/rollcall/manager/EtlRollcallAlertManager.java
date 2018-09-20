package com.aizhixin.cloud.dataanalysis.etl.rollcall.manager;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.manager.RollCallManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class EtlRollcallAlertManager {

    private final static String SQL_STUDENT_LASTEST3_ROLLCALL_ALERT =
            "INSERT INTO T_ALERT_LASTEST3_ROLLCALL (STUDENT_ID, STUDENT_NAME, STUDENT_NO, CLASSES_ID, CLASSES_NAME, PROFESSIONAL_NAME, COLLEGE_ID, COLLEGE_NAME, ORG_ID, DATE_RANGE, CAL_DATE, SHOULD_COUNT, NORMAL, DKL) " +
                    "SELECT c.studentId, u.`NAME` AS studentName, u.JOB_NUMBER AS studentNo, c.classesId, s.`NAME` AS classesName, p.`NAME` AS professionalName, u.COLLEGE_ID AS collegeId, g.`NAME` AS collegeName, u.ORG_ID AS orgId, " +
//            "'2018-09-13~2018-09-15' AS dateRange, " +
//            "'2018-09-15' AS calDate, " +
                    "#last3day#" +
                    "c.shouldCount, c.normal, c.dkl " +
                    " FROM ( " +
                    " SELECT r.CLASS_ID AS classesId," +
                    " r.student_id AS studentId," +
                    "  COUNT(*) AS shouldCount," +
                    "  SUM(IF(r.`TYPE` = 1, 1, 0)) AS normal," +
//            " ROUND(SUM(IF(r.`TYPE` = 1, 1, 0))/COUNT(*),4) AS dkl " +
                    "#dklcal#" +
                    "FROM #database#.dd_rollcall r INNER JOIN #database#.dd_schedule_rollcall sr ON r.SCHEDULE_ROLLCALL_ID = sr.ID INNER JOIN #database#.dd_schedule s ON sr.SCHEDULE_ID = s.ID " +
                    " WHERE  r.org_id = :orgId AND s.TEACH_DATE >= :start AND s.TEACH_DATE <= :end " +
                    " GROUP BY r.CLASS_ID, r.student_id " +
                    " ORDER BY r.CLASS_ID, r.student_id " +
                    ") c LEFT JOIN #orgMnagerDB#.t_user u ON c.studentId=u.ID LEFT JOIN #orgMnagerDB#.t_classes s ON u.CLASSES_ID=s.ID LEFT JOIN #orgMnagerDB#.t_professional p ON u.PROFESSIONAL_ID=p.ID LEFT JOIN #orgMnagerDB#.t_college g ON u.COLLEGE_ID=g.ID " +
                    " WHERE c.dkl<:dkl";

    private final static String SQL_STUDENT_LASTEST3_ROLLCALL_ALERT_CLEAN = "DELETE FROM T_ALERT_LASTEST3_ROLLCALL  WHERE ORG_ID=? and CAL_DATE=?";

    private final static String SQL_ROLLCALL_LAST3DAY_ORGID = "SELECT  DISTINCT r.org_id AS orgId FROM #database#.dd_rollcall r WHERE r.CREATED_DATE BETWEEN :start AND :end";

    @Value("${dl.dd.back.dbname}")
    private String ddDatabaseName;
    @Value("${dl.org.back.dbname}")
    private String orgDatabaseName;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RollCallManager rollCallManager;



    @Transactional
    public void  calStudentLastest3RollcallAlert(Long orgId, Date date, Double dkl) {
        int deleteCount = jdbcTemplate.update(SQL_STUDENT_LASTEST3_ROLLCALL_ALERT_CLEAN, new Object[] {orgId, date}, new int[]{Types.BIGINT, Types.DATE});
        log.info("Delete student lastest3day rollcall alert data count:{}", deleteCount);

        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        params.put("dkl", dkl);
        Date pre3Date = DateUtil.afterNDay(date, -2);
        pre3Date = DateUtil.getZerotime(pre3Date);//从0点开始
        params.put("start", DateUtil.formatShort(pre3Date));
        StringBuilder time = new StringBuilder("'");
        time.append(DateUtil.formatShort(pre3Date)).append("~").append(DateUtil.formatShort(date)).append("' AS dateRange,");
        time.append("'").append(DateUtil.formatShort(date)).append("' AS calDate,");
        String sql = SQL_STUDENT_LASTEST3_ROLLCALL_ALERT.replaceAll("#last3day#", time.toString());
//        date = DateUtil.afterNDay(date, 1);
        params.put("end", DateUtil.formatShort(DateUtil.getZerotime(date)));//下一天的0点

        int type = rollCallManager.getOrgArithmetic(orgId);//到课率
        String dklSql = rollCallManager.getDklCalSql(type);
        sql = sql.replaceAll("#dklcal#", dklSql);

        sql = sql.replaceAll("#database#", ddDatabaseName);
        sql = sql.replaceAll("#orgMnagerDB#", orgDatabaseName);

        log.info("Create student lastest3day rollcall alert data sql:{}", sql);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        int insertCount = template.update(sql, params);
        log.info("Create student lastest3day rollcall alert data count:{}", insertCount);
    }

    public List<Long> queryOrgIds(Date start, Date end) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sql = SQL_ROLLCALL_LAST3DAY_ORGID.replaceAll("#database#", ddDatabaseName);
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        return template.query(sql, params, (ResultSet rs, int rowNum)  -> rs.getLong("orgId"));
    }
}
