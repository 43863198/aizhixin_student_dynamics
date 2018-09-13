package com.aizhixin.cloud.dataanalysis.dd.rollcall.manager;

import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.SchoolWeekRollcallScreenVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class RollCallManager {
    /**
     * 最新一星期逐天的考勤数据
     */
    private String SQL_DD_LASTEST_WEEK_ROLLCALL = "SELECT   DATE_FORMAT(v.CREATED_DATE, '%Y-%m-%d') AS dateday,  COUNT(*) AS total,  SUM(IF(v.`TYPE` = 1, 1, 0)) AS normal," +
            "SUM(IF(v.`TYPE` = 3, 1, 0)) AS later,  SUM(IF(v.`TYPE` = 4, 1, 0)) AS askForLeave," +
            "SUM(IF(v.`TYPE` = 5, 1, 0)) AS leaveEarly,  SUM(IF(v.`TYPE` = 2, 1, 0)) AS truant " +
            "FROM  #database#.dd_rollcall v " +
            "WHERE v.org_id = ? AND v.CREATED_DATE BETWEEN ? AND ? " +
            "GROUP BY  dateday " +
            "ORDER BY dateday ";

    private String SQL_DD_DAY_AND_DAY_ROLLCALL = "SELECT   DATE_FORMAT(v.CREATED_DATE, '%Y-%m-%d') AS dateday,  COUNT(*) AS total,  SUM(IF(v.`TYPE` = 1, 1, 0)) AS normal," +
            "SUM(IF(v.`TYPE` = 3, 1, 0)) AS later,  SUM(IF(v.`TYPE` = 4, 1, 0)) AS askForLeave," +
            "SUM(IF(v.`TYPE` = 5, 1, 0)) AS leaveEarly,  SUM(IF(v.`TYPE` = 2, 1, 0)) AS truant " +
            "FROM  #database#.dd_rollcall v " +
            "WHERE v.org_id = ? AND v.CREATED_DATE BETWEEN ? AND ? " +
            "ORDER BY dateday ";

    private String sql_teachingclass_rollcall_day = "SELECT " +
            "r.TEACHINGCLASS_ID as teachingClassId," +
            "COUNT(*) AS total, " +
            "SUM(IF(r.`TYPE` = 1, 1, 0)) AS normal," +
            "SUM(IF(r.`TYPE` = 3, 1, 0)) AS later," +
            "SUM(IF(r.`TYPE` = 4, 1, 0)) AS askForLeave," +
            "SUM(IF(r.`TYPE` = 5, 1, 0)) AS leaveEarly," +
            "SUM(IF(r.`TYPE` = 2, 1, 0)) AS truant," +
            "ROUND(SUM(IF(r.`TYPE` = 1, 1, 0))/COUNT(*),4) AS DKL " +
            "FROM dd_rollcall r " +
            "WHERE  r.org_id=? AND r.CREATED_DATE BETWEEN ? AND ? " +
            "GROUP BY r.TEACHINGCLASS_ID,DATE_FORMAT(r.`CREATED_DATE`, '%Y-%m-%d') " +
            "ORDER BY TEACHINGCLASS_ID";

    private String SQL_ORG_SET = "SELECT arithmetic FROM #database#.DD_ORGAN_SET WHERE ORGAN_ID=?";

    @Value("${dl.dd.back.dbname}")
    private String ddDatabaseName;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getOrgArithmetic(Long orgId) {
        String sql = SQL_ORG_SET.replaceAll("#database#", ddDatabaseName);
        Integer a = null;
        try {
            a = jdbcTemplate.queryForObject(sql, new Object[]{orgId}, new int[]{Types.BIGINT}, Integer.class);
        } catch (Exception e) {
        }
        if (null == a) {
            a = 10;
        }
        return a;
    }

    public List<SchoolWeekRollcallScreenVO> findRollcallStaticsDayAndDay(Long orgId, Date start, Date end) {
        String sql = SQL_DD_LASTEST_WEEK_ROLLCALL.replaceAll("#database#", ddDatabaseName);
        log.info("params: orgId:{}, start:{},end:{}; native SQL:{}", orgId, start, end, sql);
        return jdbcTemplate.query(sql,
                new Object[]{orgId, start, end},
                new int [] {Types.BIGINT, Types.TIMESTAMP, Types.TIMESTAMP},
                (ResultSet rs, int rowNum) -> new SchoolWeekRollcallScreenVO(
                        rs.getString("dateday"),
                        rs.getInt("total"), rs.getInt("normal"),
                        rs.getInt("later"), rs.getInt("askForLeave"),
                        rs.getInt("truant"), rs.getInt("leaveEarly"))
        );
    }

    public List<SchoolWeekRollcallScreenVO> findRollcallStatics(Long orgId, Date start, Date end) {
        String sql = SQL_DD_DAY_AND_DAY_ROLLCALL.replaceAll("#database#", ddDatabaseName);
        log.info("params: orgId:{}, start:{},end:{}; native SQL:{}", orgId, start, end, sql);
        return jdbcTemplate.query(sql,
                new Object[]{orgId, start, end},
                new int [] {Types.BIGINT, Types.TIMESTAMP, Types.TIMESTAMP},
                (ResultSet rs, int rowNum) -> new SchoolWeekRollcallScreenVO(
                        rs.getString("dateday"),
                        rs.getInt("total"), rs.getInt("normal"),
                        rs.getInt("later"), rs.getInt("askForLeave"),
                        rs.getInt("truant"), rs.getInt("leaveEarly"))
        );
    }

    public double attendanceAccount(int total, int normal, int later, int askForleave, int leave, int type) {
        if (type == 0) {
            type = 10;
        }
        int element = 0;
        switch (type) {
            case 10:
                element = normal;
                break;
            case 20:
                element = normal + askForleave;
                break;
            case 30:
                element = normal + later;
                break;
            case 40:
                element = normal + leave;
                break;
            case 50:
                element = normal + later + leave;
                break;
            case 60:
                element = normal + later + leave + askForleave;
                break;
        }
        return element / (total == 0 ? 1 : total);
    }


    public List<SchoolWeekRollcallScreenVO> queryLastTwoWeekRollcall(Long orgId, String start, String end) {
        String ylsj = "2018-06-04,8434,7654,43,180,1,547\n" +
                "2018-06-05,8625,7486,46,324,2,735\n" +
                "2018-06-06,8303,7492,35,269,2,505\n" +
                "2018-06-07,6172,5669,48,129,0,296\n" +
                "2018-06-08,3380,2856,22,164,2,336\n" +
                "2018-06-09,172,169,0,3,0,0\n" +
                "2018-06-11,7826,7064,55,128,1,522\n" +
                "2018-06-12,8987,8127,59,113,5,683\n" +
                "2018-06-13,8070,7390,43,165,2,470\n" +
                "2018-06-14,6639,6083,47,94,2,413\n" +
                "2018-06-15,3023,2583,7,128,0,305\n" +
                "2018-06-16,3,3,0,0,0,0";
        List<SchoolWeekRollcallScreenVO> list = new ArrayList<>();
        String[] rr = ylsj.split("\\n");
        if (rr.length > 0) {
            for (String t : rr) {
                String[] fs = t.split(",");
                if (7 == fs.length) {
                    SchoolWeekRollcallScreenVO v = new SchoolWeekRollcallScreenVO(fs[0], new Integer(fs[1]), new Integer(fs[2]), new Integer(fs[3]), new Integer(fs[4]), new Integer(fs[5]), new Integer(fs[6]));
                    list.add(v);
                }
            }
        }
        return list;
    }
}
