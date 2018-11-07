package com.aizhixin.cloud.dataanalysis.dd.rollcall.manager;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.dto.DayAndDayOfWeekDTO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.dto.IDNoNameDTO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.dto.OrgTeacherInfoDTO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.dto.TeacherForRollcallDTO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

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

//    private String SQL_TEACHINGCLASS_ROLLCALL_DAY = "SELECT " +
//            "r.TEACHINGCLASS_ID as teachingClassId," +
//            "COUNT(*) AS total, " +
//            "SUM(IF(r.`TYPE` = 1, 1, 0)) AS normal," +
//            "SUM(IF(r.`TYPE` = 3, 1, 0)) AS later," +
//            "SUM(IF(r.`TYPE` = 4, 1, 0)) AS askForLeave," +
//            "SUM(IF(r.`TYPE` = 5, 1, 0)) AS leaveEarly," +
//            "SUM(IF(r.`TYPE` = 2, 1, 0)) AS truant," +
//            "ROUND(SUM(IF(r.`TYPE` = 1, 1, 0))/COUNT(*),4) AS DKL " +
//            "FROM dd_rollcall r " +
//            "WHERE  r.org_id=? AND r.CREATED_DATE BETWEEN ? AND ? " +
//            "GROUP BY r.TEACHINGCLASS_ID,DATE_FORMAT(r.`CREATED_DATE`, '%Y-%m-%d') " +
//            "ORDER BY TEACHINGCLASS_ID";


    private String SQL_TEACHER_DAY_ROLLCALL_ALERT_COUNT = "SELECT " +
            "count(*) as count " +
            "FROM ( " +
            "SELECT " +
            "r.TEACHER_ID as teacherId," +
            "s.TEACH_DATE as caldate ," +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r INNER JOIN #database#.dd_schedule_rollcall sr ON r.SCHEDULE_ROLLCALL_ID = sr.ID INNER JOIN #database#.dd_schedule s ON sr.SCHEDULE_ID = s.ID " +
            "WHERE  r.org_id = :orgId AND s.TEACH_DATE >= :start AND s.TEACH_DATE < :end " +
            "#queryCondition#" +
            "GROUP BY r.TEACHER_ID, s.TEACH_DATE " +
            ") c " +
            "WHERE c.dkl < :dkl";
    private String SQL_TEACHER_DAY_ROLLCALL_ALERT = "SELECT " +
            "c.teacherId, c.caldate, c.dkl " +
            "FROM ( " +
            "SELECT " +
            "r.TEACHER_ID as teacherId," +
            "s.TEACH_DATE as caldate ," +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r INNER JOIN #database#.dd_schedule_rollcall sr ON r.SCHEDULE_ROLLCALL_ID = sr.ID INNER JOIN #database#.dd_schedule s ON sr.SCHEDULE_ID = s.ID " +
            "WHERE  r.org_id = :orgId AND s.TEACH_DATE >= :start AND s.TEACH_DATE < :end " +
            "#queryCondition#" +
            "GROUP BY r.TEACHER_ID, s.TEACH_DATE " +
            "ORDER BY s.TEACH_DATE DESC, r.TEACHER_ID " +
            ") c " +
            "WHERE c.dkl < :dkl";
    private String SQL_TEACHER_COURSE_CLASSES_ROLLCALL_ALERT = "SELECT " +
            "c.teacherId, c.caldate, c.courseId, c.courseName, c.period, c.dkl " +
            "FROM (" +
            "SELECT " +
            "r.TEACHER_ID as teacherId," +
            "s.TEACH_DATE as caldate , " +
            "r.COURSE_ID as courseId, s.COURSE_NAME as courseName," +
            " CONCAT(s.PERIOD_NO,'-',s.PERIOD_NO + s.PERIOD_NUM -1) as period," +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r , #database#.dd_schedule_rollcall sr, #database#.dd_schedule s " +
            "WHERE  r.org_id = :orgId AND s.ORGAN_ID = :orgId AND r.SCHEDULE_ROLLCALL_ID = sr.ID AND sr.SCHEDULE_ID=s.ID AND s.TEACH_DATE >= :start AND s.TEACH_DATE < :end AND r.TEACHER_ID IN (:teachers) " +
            "GROUP BY r.TEACHER_ID, s.TEACH_DATE, r.COURSE_ID, s.COURSE_NAME,s.PERIOD_NO " +
            "ORDER BY s.TEACH_DATE DESC, r.TEACHER_ID,r.COURSE_ID,s.PERIOD_NO " +
            ") c ";

    private String SQL_ORG_SET = "SELECT arithmetic FROM #database#.DD_ORGAN_SET WHERE ORGAN_ID=?";

    private String SQL_ORG_TEACHER_INFO = "SELECT " +
            "u.ID, u.`NAME` teacherName, u.JOB_NUMBER  workNo, c.`NAME` collegeName " +
            "FROM #orgMnagerDB#.t_user u LEFT JOIN #orgMnagerDB#.t_college c ON u.COLLEGE_ID=c.ID " +
            "WHERE u.ORG_ID = :orgId AND u.ID IN (:teacherIds)";

    private String SQL_CLASSES_ROLLCALL_ALERT_COUNT = "SELECT count(*) FROM ( SELECT " +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r INNER JOIN #database#.dd_schedule_rollcall sr ON r.SCHEDULE_ROLLCALL_ID = sr.ID INNER JOIN #database#.dd_schedule s ON sr.SCHEDULE_ID = s.ID " +
            " WHERE  r.org_id = :orgId AND s.TEACH_DATE >= :start AND s.TEACH_DATE < :end" +
            " #queryCondition#" +
            " GROUP BY r.CLASS_ID, s.TEACH_DATE " +
            ") c WHERE  dkl < :dkl  ";

    private String SQL_CLASSES_ROLLCALL_ALERT = "SELECT classesId, caldate, dkl FROM ( SELECT r.CLASS_ID as classesId, s.TEACH_DATE as caldate, " +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r INNER JOIN #database#.dd_schedule_rollcall sr ON r.SCHEDULE_ROLLCALL_ID = sr.ID INNER JOIN #database#.dd_schedule s ON sr.SCHEDULE_ID = s.ID " +
            " WHERE  r.org_id = :orgId AND s.TEACH_DATE >= :start AND s.TEACH_DATE < :end " +
            " #queryCondition#" +
            " GROUP BY r.CLASS_ID, s.TEACH_DATE " +
            " ORDER BY s.TEACH_DATE DESC, r.CLASS_ID ) c WHERE dkl < :dkl ";

    private String SQL_CLASSES_INFO = "SELECT " +
            "c.ID,c.`NAME` AS classesName , c.code AS classesCode, c.TEACHING_YEAR AS grade, p.`NAME` AS professionalName, g.`NAME` AS collegeName " +
            "FROM #orgMnagerDB#.t_classes c LEFT JOIN #orgMnagerDB#.t_professional p ON c.PROFESSIONAL_ID=p.ID LEFT JOIN #orgMnagerDB#.t_college g ON c.COLLEGE_ID=g.ID " +
            "WHERE c.ID in (:classesIds)";

    private String SQL_CLASSES_TEACHER = "SELECT " +
            "c.CLASSES_ID AS classesId, u.`NAME` AS teacherName " +
            "FROM #orgMnagerDB#.t_classes_teacher c LEFT JOIN #orgMnagerDB#.t_user u ON c.TEACHER_ID=u.ID " +
            "WHERE c.CLASSES_ID IN (:classesIds)";

    private String SQL_CLASSES_TEACHER_CLASSESIDS = "SELECT CLASSES_ID FROM #orgMnagerDB#.t_classes_teacher  WHERE ORG_ID = ? AND TEACHER_ID = ?";


    private String SQL_STUDENT_UNNORMAL_COURSE_ALERT_COUNT = "SELECT COUNT(*) " +
            "FROM ( " +
            "SELECT " +
            "(SUM(IF(r.`TYPE` = 3, 1, 0)) + SUM(IF(r.`TYPE` = 4, 1, 0)) + SUM(IF(r.`TYPE` = 5, 1, 0)) + SUM(IF(r.`TYPE` = 2, 1, 0)))/COUNT(*) AS undkl " +
            "FROM #database#.dd_rollcall r  " +
            "WHERE r.org_id = :orgId AND r.CREATED_DATE >= :start AND r.CREATED_DATE < :end " +
            " #queryCondition# " +
            "GROUP BY r.STUDENT_ID, r.COURSE_ID " +
            " ) c WHERE c.undkl > :undkl";

    private String SQL_STUDENT_UNNORMAL_COURSE_ALERT = "SELECT studentId, courseId, shouldCount, unNormal " +
            "FROM ( " +
            "SELECT r.STUDENT_ID AS studentId, r.COURSE_ID AS courseId,  " +
            "COUNT(*) AS shouldCount, " +
            "(SUM(IF(r.`TYPE` = 3, 1, 0)) + SUM(IF(r.`TYPE` = 4, 1, 0)) + SUM(IF(r.`TYPE` = 5, 1, 0)) + SUM(IF(r.`TYPE` = 2, 1, 0))) AS unNormal, " +
            "(SUM(IF(r.`TYPE` = 3, 1, 0)) + SUM(IF(r.`TYPE` = 4, 1, 0)) + SUM(IF(r.`TYPE` = 5, 1, 0)) + SUM(IF(r.`TYPE` = 2, 1, 0)))/COUNT(*) AS undkl " +
            "FROM #database#.dd_rollcall r  " +
            "WHERE r.org_id = :orgId AND r.CREATED_DATE >= :start AND r.CREATED_DATE < :end " +
            " #queryCondition# " +
            "GROUP BY r.STUDENT_ID, r.COURSE_ID " +
            "ORDER BY undkl DESC, r.STUDENT_NUM) c WHERE c.undkl > :undkl";

    private String SQL_ORG_COURSE = "SELECT c.ID, c.`NAME` FROM #orgMnagerDB#.t_course c WHERE c.ID IN (:courseIds)";
    private String SQL_ORG_CLASSES_SIMPLE = "SELECT c.ID, c.`NAME`, c.TEACHING_YEAR FROM #orgMnagerDB#.t_classes c WHERE c.ID IN (:classesIds)";

    private String SQL_STUDENT_ROLLCALL_ALERT_COUNT = "SELECT count(*) as count " +
            " FROM ( " +
            "  SELECT " +
            " #dklcal# " +
            "  FROM #database#.dd_rollcall r INNER JOIN #database#.dd_schedule_rollcall sr ON r.SCHEDULE_ROLLCALL_ID = sr.ID INNER JOIN #database#.dd_schedule s ON sr.SCHEDULE_ID = s.ID " +
            "  WHERE  r.org_id = :orgId AND s.TEACH_DATE >= :start AND s.TEACH_DATE < :end " +
            " #queryCondition# " +
            "  GROUP BY r.STUDENT_ID " +
            ") c " +
            " WHERE c.dkl < :dkl";

    private String SQL_STUDENT_ROLLCALL_ALERT = "SELECT  c.studentId, c.shouldCount, c.normal, c.dkl " +
            " FROM ( " +
            "  SELECT  r.STUDENT_ID AS studentId," +
            "  COUNT(*) AS shouldCount," +
            "  SUM(IF(r.`TYPE` = 1, 1, 0)) AS normal," +
            " #dklcal# " +
//            "  ROUND(SUM(IF(r.`TYPE` = 1, 1, 0))/COUNT(*),4) AS dkl " +
            " FROM #database#.dd_rollcall r INNER JOIN #database#.dd_schedule_rollcall sr ON r.SCHEDULE_ROLLCALL_ID = sr.ID INNER JOIN #database#.dd_schedule s ON sr.SCHEDULE_ID = s.ID " +
            "  WHERE  r.org_id = :orgId AND s.TEACH_DATE >= :start AND s.TEACH_DATE < :end " +
            " #queryCondition# " +
            "  GROUP BY r.STUDENT_ID " +
            "  ORDER BY ROUND(SUM(IF(r.`TYPE` = 1, 1, 0))/COUNT(*),4) DESC " +
            ") c " +
            " WHERE c.dkl < :dkl";

    private String SQL_STUDENT_BASE_INFO = "SELECT " +
            "u.ID id, u.JOB_NUMBER studentNo, u.`NAME` studentName, u.CLASSES_ID AS classesId, c.`NAME` classesName, p.`NAME` professionalName, g.`NAME` collegeName " +
            "FROM #orgMnagerDB#.t_user u LEFT JOIN #orgMnagerDB#.t_classes c ON u.CLASSES_ID=c.ID LEFT JOIN #orgMnagerDB#.t_professional p ON u.PROFESSIONAL_ID=p.ID LEFT JOIN #orgMnagerDB#.t_college g ON u.COLLEGE_ID=g.ID " +
            "WHERE u.ID IN (:ids)";

    private String SQL_UNIT_ROLLCALL_STATISTICS = "SELECT " +
            "c.unitId, c.unitName, c.total, c.normal, c.later, c.askForLeave, c.leaveEarly, c.truant, c.dkl " +
            "FROM (" +
            "SELECT " +
//            "r.college_id as unitId, r.college_name as unitName," +
            "#unitField#" +
            "COUNT(*) AS total, " +
            "SUM(IF(r.`TYPE` = 1, 1, 0)) AS normal," +
            "SUM(IF(r.`TYPE` = 3, 1, 0)) AS later," +
            "SUM(IF(r.`TYPE` = 4, 1, 0)) AS askForLeave," +
            "SUM(IF(r.`TYPE` = 5, 1, 0)) AS leaveEarly," +
            "SUM(IF(r.`TYPE` = 2, 1, 0)) AS truant," +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r , #database#.dd_schedule_rollcall sr, #database#.dd_schedule s " +
            "WHERE  r.org_id = :orgId AND s.ORGAN_ID = :orgId AND r.SCHEDULE_ROLLCALL_ID = sr.ID AND sr.SCHEDULE_ID=s.ID AND s.TEACH_DATE >= :start AND s.TEACH_DATE <= :end " +
            " #queryCondition# " +
//            "GROUP BY r.college_id, r.college_name " +
            "#groupunitField# " +
            ") c ORDER BY c.dkl DESC";


    private String SQL_COUSE_ROLLCALL_STATISTICS_COUNT = "SELECT " +
            "COUNT(*) AS count " +
            "FROM (" +
            "SELECT " +
            "COUNT(*) AS count " +
            "FROM #database#.dd_rollcall r , #database#.dd_schedule_rollcall sr, #database#.dd_schedule s " +
            "WHERE  r.org_id = :orgId AND s.ORGAN_ID = :orgId AND r.SCHEDULE_ROLLCALL_ID = sr.ID AND sr.SCHEDULE_ID=s.ID AND s.TEACH_DATE >= :start AND s.TEACH_DATE <= :end " +
            " #queryCondition# " +
            "GROUP BY s.COURSE_ID, s.COURSE_NAME, s.TEACHER_NAME " +
            ") c ";

    private String SQL_COUSE_ROLLCALL_STATISTICS = "SELECT " +
            "c.courseId, c.courseName, c.teacherName, c.total, c.normal, c.later, c.askForLeave, c.leaveEarly, c.truant, c.dkl " +
            "FROM (" +
            "SELECT " +
            "s.COURSE_ID AS courseId, s.COURSE_NAME AS courseName, s.TEACHER_NAME AS teacherName," +
            "COUNT(*) AS total, " +
            "SUM(IF(r.`TYPE` = 1, 1, 0)) AS normal," +
            "SUM(IF(r.`TYPE` = 3, 1, 0)) AS later," +
            "SUM(IF(r.`TYPE` = 4, 1, 0)) AS askForLeave," +
            "SUM(IF(r.`TYPE` = 5, 1, 0)) AS leaveEarly," +
            "SUM(IF(r.`TYPE` = 2, 1, 0)) AS truant," +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r , #database#.dd_schedule_rollcall sr, #database#.dd_schedule s " +
            "WHERE  r.org_id = :orgId AND s.ORGAN_ID = :orgId AND r.SCHEDULE_ROLLCALL_ID = sr.ID AND sr.SCHEDULE_ID=s.ID AND s.TEACH_DATE >= :start AND s.TEACH_DATE <= :end " +
            " #queryCondition# " +
            "GROUP BY s.COURSE_ID, s.COURSE_NAME, s.TEACHER_NAME " +
            ") c ORDER BY c.dkl DESC";


    private String SQL_TEACHER_ROLLCALL_STATISTICS_COUNT = "SELECT " +
            "COUNT(*) AS count " +
            "FROM (" +
            "SELECT " +
            "COUNT(*) AS count " +
            "FROM #database#.dd_rollcall r , #database#.dd_schedule_rollcall sr, #database#.dd_schedule s " +
            "WHERE  r.org_id = :orgId AND s.ORGAN_ID = :orgId AND r.SCHEDULE_ROLLCALL_ID = sr.ID AND sr.SCHEDULE_ID=s.ID AND s.TEACH_DATE >= :start AND s.TEACH_DATE <= :end " +
            " #queryCondition# " +
            "GROUP BY r.TEACHER_ID, s.TEACHER_NAME " +
            ") c ";

    private String SQL_TEACHER_ROLLCALL_STATISTICS = "SELECT " +
            "c.teacherId, c.teacherName, c.total, c.normal, c.later, c.askForLeave, c.leaveEarly, c.truant, c.dkl " +
            "FROM (" +
            "SELECT " +
            "r.TEACHER_ID AS teacherId, s.TEACHER_NAME AS teacherName," +
            "COUNT(*) AS total, " +
            "SUM(IF(r.`TYPE` = 1, 1, 0)) AS normal," +
            "SUM(IF(r.`TYPE` = 3, 1, 0)) AS later," +
            "SUM(IF(r.`TYPE` = 4, 1, 0)) AS askForLeave," +
            "SUM(IF(r.`TYPE` = 5, 1, 0)) AS leaveEarly," +
            "SUM(IF(r.`TYPE` = 2, 1, 0)) AS truant," +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r , #database#.dd_schedule_rollcall sr, #database#.dd_schedule s " +
            "WHERE  r.org_id = :orgId AND s.ORGAN_ID = :orgId AND r.SCHEDULE_ROLLCALL_ID = sr.ID AND sr.SCHEDULE_ID=s.ID AND s.TEACH_DATE >= :start AND s.TEACH_DATE <= :end " +
            " #queryCondition# " +
            "GROUP BY r.TEACHER_ID, s.TEACHER_NAME " +
            ") c ORDER BY c.dkl DESC";

    private String SQL_TEACHER_WORKNO_COLLEGE = "SELECT u.ID, u.JOB_NUMBER, c.`NAME` FROM #orgMnagerDB#.t_user u LEFT JOIN #orgMnagerDB#.t_college c ON u.COLLEGE_ID=c.ID WHERE u.ID IN (:ids)";

    //SELECT  PERIOD_NO, PERIOD_NUM FROM dd_schedule WHERE ORGAN_ID=318 AND TEACH_DATE='2018-09-25' AND  5 BETWEEN PERIOD_NO AND (PERIOD_NO + PERIOD_NUM - 1)

    //今天考勤累计统计大屏
    private String SQL_CUR_DAY_SCREEN_ROLLCALL = "SELECT COUNT(*) AS total," +
            "SUM(IF(r.`TYPE` = 1, 1, 0)) AS normal," +
            "SUM(IF(r.`TYPE` = 3, 1, 0)) AS later," +
            "SUM(IF(r.`TYPE` = 4, 1, 0)) AS askForLeave," +
            "SUM(IF(r.`TYPE` = 5, 1, 0)) AS leaveEarly," +
            "SUM(IF(r.`TYPE` = 2, 1, 0)) AS truant," +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r LEFT JOIN #database#.dd_schedule_rollcall sr ON sr.ID=r.SCHEDULE_ROLLCALL_ID LEFT JOIN #database#.dd_schedule s ON sr.SCHEDULE_ID=s.ID " +
            "WHERE s.ORGAN_ID = :orgId AND s.TEACH_DATE = :teachDate AND s.END_TIME < :endtime AND s.DELETE_FLAG=0 AND r.DELETE_FLAG=0 AND sr.DELETE_FLAG=0";

    @Value("${dl.dd.back.dbname}")
    private String ddDatabaseName;

    @Value("${dl.org.back.dbname}")
    private String orgDatabaseName;
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

    public String getDklCalSql(int type) {
        switch (type) {
            case 10:
                return "ROUND(SUM(IF(r.`TYPE` = 1, 1, 0))/COUNT(*),4) AS dkl ";
            case 20:
                return "ROUND((SUM(IF(r.`TYPE` = 1, 1, 0)) + SUM(IF(r.`TYPE` = 4, 1, 0)))/COUNT(*),4) AS dkl ";
            case 30:
                return "ROUND((SUM(IF(r.`TYPE` = 1, 1, 0)) + SUM(IF(r.`TYPE` = 3, 1, 0)))/COUNT(*),4) AS dkl ";
            case 40:
                return "ROUND((SUM(IF(r.`TYPE` = 1, 1, 0)) + SUM(IF(r.`TYPE` = 5, 1, 0)))/COUNT(*),4) AS dkl ";
            case 50:
                return "ROUND((SUM(IF(r.`TYPE` = 1, 1, 0)) + SUM(IF(r.`TYPE` = 3, 1, 0)) + SUM(IF(r.`TYPE` = 5, 1, 0)))/COUNT(*),4) AS dkl ";
            case 60:
                return "ROUND((SUM(IF(r.`TYPE` = 1, 1, 0)) + SUM(IF(r.`TYPE` = 3, 1, 0)) + SUM(IF(r.`TYPE` = 5, 1, 0)) + SUM(IF(r.`TYPE` = 4, 1, 0)))/COUNT(*),4) AS dkl ";
            default:
                return "ROUND(SUM(IF(r.`TYPE` = 1, 1, 0))/COUNT(*),4) AS dkl ";
        }
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

    public PageData<TeacherRollcallAlertVO> queryTeacherDkl(Long orgId, Long collegeId, Long teacherId, Date start, Date end, Double dkl, Integer pageIndex, Integer pageSize) {
        PageData<TeacherRollcallAlertVO> pageData = new PageData<>();
        if (null == pageIndex || pageIndex < 1) {
            pageIndex = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 20;
        }
        pageData.getPage().setPageNumber(pageIndex);
        pageData.getPage().setPageSize(pageSize);
        pageData.getPage().setTotalElements(0L);
        pageData.getPage().setTotalPages(1);

        int type = getOrgArithmetic(orgId);
        String dklSql = getDklCalSql(type);
        StringBuilder sb = new StringBuilder("");
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        if (null != collegeId && collegeId > 0) {
            sb.append(" AND r.college_id=:collegeId ");
            params.put("collegeId", collegeId);
        }
        if (null != teacherId && teacherId > 0) {
            sb.append(" AND r.TEACHER_ID=:teacherId ");
            params.put("teacherId", teacherId);
        }
        if (null == dkl || dkl <= 0) {
            dkl = 0.8;
        }
        params.put("dkl", dkl);
        Date cur = new Date();
        Date today = new Date();
        today =  DateUtil.getZerotime(today);
        if (null == start) {
            start = DateUtil.afterNDay(cur, -1);
            start =  DateUtil.getZerotime(start);
        } else {
            start = DateUtil.getZerotime(start);
        }
        if (null == end) {
            end =  DateUtil.getZerotime(cur);
        } else {
            end = DateUtil.afterNDay(end, 1);
            end = DateUtil.getZerotime(end);
        }
        if (end.after(today)) {
            end = today;
        }
        params.put("start", DateUtil.formatShort(start));
        params.put("end", DateUtil.formatShort(end));

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sql_count = SQL_TEACHER_DAY_ROLLCALL_ALERT_COUNT.replaceAll("#database#", ddDatabaseName);
        sql_count = sql_count.replaceAll("#dklcal#", dklSql);
        sql_count = sql_count.replaceAll("#queryCondition#", sb.toString());
        log.info("Count teacher rollcall alert sql:{}", sql_count);
        List<Long> counts =  template.query(sql_count, params, (ResultSet rs, int rowNum) -> rs.getLong("count"));
        Long count = 0L;
        if (null != counts && counts.size() > 0) {
            count = counts.get(0);
        }
        pageData.getPage().setTotalElements(count);
        pageData.getPage().setTotalPages(PageUtil.cacalatePagesize(count, pageSize));
        if (count > 0) {
            String sql = SQL_TEACHER_DAY_ROLLCALL_ALERT.replaceAll("#database#", ddDatabaseName);
            sql = sql.replaceAll("#dklcal#", dklSql);
            sql = sql.replaceAll("#queryCondition#", sb.toString());
            int ps = (pageIndex - 1) * pageSize;
            sql = sql +  " LIMIT " + ps + ", " + pageSize;
            log.info("Query teacher rollcall alert sql:{}", sql);
            List<TeacherRollcallAlertVO> list = template.query(sql, params, (ResultSet rs, int rowNum) -> new TeacherRollcallAlertVO(rs.getString("caldate"), rs.getLong("teacherId"), rs.getDouble("dkl")));
            Set<Long> teachers = new HashSet<>();
            Map<String, TeacherRollcallAlertVO> map = new HashMap<>();
            Map<Long, OrgTeacherInfoDTO> teacherInfoVOMap = new HashMap<>();
            if (null != list && !list.isEmpty()) {
                pageData.setData(list);
                for (TeacherRollcallAlertVO v : list) {
                    teachers.add(v.getTeacherId());
                    map.put(v.getDay() + "-" + v.getTeacherId(), v);
                }
                if (null != teacherId && teacherId > 0) {
                    teachers.add(teacherId);
                }
                Map<String, String> courseMap = new HashMap<>();
                if (!teachers.isEmpty()) {
                    params.put("teachers", teachers);
                    params.remove("dkl");

                    sql = SQL_TEACHER_COURSE_CLASSES_ROLLCALL_ALERT.replaceAll("#database#", ddDatabaseName);
                    sql = sql.replaceAll("#dklcal#", dklSql);
                    sql = sql.replaceAll("#queryCondition#", sb.toString());

                    log.info("Query teacher course period rollcall alert sql:{}", sql);
                    List<TeacherCourseRollcallAlertVO> courselist = template.query(sql, params, (ResultSet rs, int rowNum) -> new TeacherCourseRollcallAlertVO(rs.getString("caldate"), rs.getLong("teacherId"), rs.getLong("courseId"), rs.getString("courseName"), rs.getString("period"), rs.getDouble("dkl")));
                    if (null != courselist) {
                        for (TeacherCourseRollcallAlertVO vo : courselist) {
                            StringBuilder v  = new StringBuilder();
                            String tmp = courseMap.get(vo.getDay() + "-" + vo.getTeacherId());
                            if (null != tmp) {
                                v.append(tmp);
                            }
                            String dt = "" + (vo.getDkl() * 100);
                            int p = dt.indexOf(".");
                            if (p > 0 && p + 3 < dt.length()) {
                                dt = dt.substring(0, p + 3);
                            }
                            v.append("[").append(vo.getCourseName()).append(", ").append(vo.getPeriod()).append(", ").append(dt).append("%]");
                            courseMap.put(vo.getDay() + "-" + vo.getTeacherId(), v.toString());
                        }
                    }

                    params.clear();
                    params.put("orgId", orgId);
                    params.put("teacherIds", teachers);
                    sql = SQL_ORG_TEACHER_INFO.replaceAll("#orgMnagerDB#",  orgDatabaseName);
                    log.info("Query teacher info sql:{}", sql);
                    //u.ID, u.`NAME` teacherName, u.ID_NUMBER  workNo, c.`NAME` collegeName
                    List<OrgTeacherInfoDTO> teacherList = template.query(sql, params, (ResultSet rs, int rowNum) -> new OrgTeacherInfoDTO(rs.getLong("ID"), rs.getString("workNo"), rs.getString("teacherName"), rs.getString("collegeName")));
                    if(null != teacherList && !teacherList.isEmpty()) {
                        for (OrgTeacherInfoDTO d : teacherList) {
                            teacherInfoVOMap.put(d.getTeacherId(), d);
                        }
                    }
                }
                for (TeacherRollcallAlertVO v : list) {
                    v.setCourseRollList(courseMap.get(v.getDay() + "-" + v.getTeacherId()));
                    OrgTeacherInfoDTO d = teacherInfoVOMap.get(v.getTeacherId());
                    if (null != d) {
                        v.setTeacherName(d.getTeacherName());
                        v.setTeacherNo(d.getTeacherNo());
                        v.setCollegeName(d.getCollegeName());
                    }
                }
            } else  {
                pageData.setData(new ArrayList<>());
            }
        }
        return pageData;
    }

    public PageData<ClassesRollcallAlertVO> queryClassesDkl(Long orgId, Long collegeId, Long teacherId, Date start, Date end, Double dkl, Integer pageIndex, Integer pageSize) {
        PageData<ClassesRollcallAlertVO> pageData = new PageData<>();
        if (null == pageIndex || pageIndex < 1) {
            pageIndex = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 20;
        }
        pageData.getPage().setPageNumber(pageIndex);
        pageData.getPage().setPageSize(pageSize);
        pageData.getPage().setTotalElements(0L);
        pageData.getPage().setTotalPages(1);

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        int type = getOrgArithmetic(orgId);
        String dklSql = getDklCalSql(type);
        StringBuilder sb = new StringBuilder("");
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        Set<Long> classesSet = new HashSet<>();
        if (null != collegeId && collegeId > 0) {
            sb.append(" AND r.college_id=:collegeId ");
            params.put("collegeId", collegeId);
        }
        if (null != teacherId && teacherId > 0) {//查询班级ID列表
            String sql = SQL_CLASSES_TEACHER_CLASSESIDS.replaceAll("#orgMnagerDB#", orgDatabaseName);
            List<Long> classesList = jdbcTemplate.query(sql, new Object[] {orgId, teacherId}, new int[]{Types.BIGINT, Types.BIGINT}, (ResultSet rs, int rowNum) -> rs.getLong(1));
            if (null != classesList && !classesList.isEmpty()) {
                classesSet.addAll(classesList);
            } else {
                return pageData;
            }
        }
        if (!classesSet.isEmpty()) {
            sb.append(" AND r.CLASS_ID in(:classesIdSet) ");
            params.put("classesIdSet", classesSet);
        }
        if (null == dkl || dkl <= 0) {
            dkl = 0.8;
        }
        params.put("dkl", dkl);
        Date cur = new Date();
        Date today = new Date();
        today =  DateUtil.getZerotime(today);
        if (null == start) {
            start = DateUtil.afterNDay(cur, -1);
            start = DateUtil.getZerotime(start);
        } else {
            start = DateUtil.getZerotime(start);
        }
        if (null == end) {
            end = DateUtil.getZerotime(cur);
        } else {
            end = DateUtil.afterNDay(end, 1);
            end = DateUtil.getZerotime(end);
        }
        if (end.after(today)) {
            end = today;
        }
        params.put("start", DateUtil.formatShort(start));
        params.put("end", DateUtil.formatShort(end));

        String sql_count = SQL_CLASSES_ROLLCALL_ALERT_COUNT.replaceAll("#database#", ddDatabaseName);
        sql_count = sql_count.replaceAll("#dklcal#", dklSql);
        sql_count = sql_count.replaceAll("#queryCondition#", sb.toString());
        log.info("Count classes rollcall alert sql:{}", sql_count);
        List<Long> counts =  template.query(sql_count, params, (ResultSet rs, int rowNum) -> rs.getLong(1));
        Long count = 0L;
        if (null != counts && counts.size() > 0) {
            count = counts.get(0);
        }
        pageData.getPage().setTotalElements(count);
        pageData.getPage().setTotalPages(PageUtil.cacalatePagesize(count, pageSize));
        if (count > 0) {
            String sql = SQL_CLASSES_ROLLCALL_ALERT.replaceAll("#database#", ddDatabaseName);
            sql = sql.replaceAll("#dklcal#", dklSql);
            sql = sql.replaceAll("#queryCondition#", sb.toString());
            int ps = (pageIndex - 1) * pageSize;
            sql = sql +  " LIMIT " + ps + ", " + pageSize;
            log.info("Query classes rollcall alert sql:{}", sql);
            //r.CLASS_ID as classesId,DATE_FORMAT(r.`CREATED_DATE`, '%Y-%m-%d') as caldate
            Map<Long, ClassesRollcallAlertVO> classesInfoMap = new HashMap<>();
            Map<Long, ClassesRollcallAlertVO> classesTeacherInfoMap = new HashMap<>();
            List<ClassesRollcallAlertVO> list = template.query(sql, params, (ResultSet rs, int rowNum) -> new ClassesRollcallAlertVO(rs.getString("caldate"), rs.getLong("classesId"), rs.getDouble("dkl")));
            if (null != list && !list.isEmpty()) {
                pageData.setData(list);
                Set<Long> classesIds = new HashSet<>();

                //查询填充班级和班主任信息
                for (ClassesRollcallAlertVO v : list) {
                    classesIds.add(v.getClassesId());
                }
                if (!classesIds.isEmpty()) {
                    //c.ID,c.`NAME` AS classesName, c.TEACHING_YEAR AS grade, p.`NAME` AS professionalName, g.`NAME` AS collegeName
                    params.clear();
                    params.put("classesIds", classesIds);
                    sql = SQL_CLASSES_INFO.replaceAll("#orgMnagerDB#", orgDatabaseName);
                    List<ClassesRollcallAlertVO> classesList = template.query(sql, params, (ResultSet rs, int rowNum) -> new ClassesRollcallAlertVO(rs.getLong("ID"), rs.getString("classesName"), rs.getString("classesCode"), rs.getString("grade"), rs.getString("professionalName"), rs.getString("collegeName")));
                    if(null != classesList) {
                        for (ClassesRollcallAlertVO v : classesList) {
                            classesInfoMap.put(v.getClassesId(), v);
                        }
                    }
                    //c.CLASSES_ID AS classesId, u.`NAME` AS teacherName
                    sql = SQL_CLASSES_TEACHER.replaceAll("#orgMnagerDB#", orgDatabaseName);
                    List<ClassesRollcallAlertVO> classesTeacherList = template.query(sql, params, (ResultSet rs, int rowNum) -> new ClassesRollcallAlertVO(rs.getLong("classesId"), rs.getString("teacherName")));
                    if(null != classesTeacherList) {
                        for (ClassesRollcallAlertVO v : classesTeacherList) {
                            classesTeacherInfoMap.put(v.getClassesId(), v);
                        }
                    }
                }

                for (ClassesRollcallAlertVO v : list) {
                    ClassesRollcallAlertVO c = classesInfoMap.get(v.getClassesId());
                    if (null != c) {
                        v.setClassesName(c.getClassesName());
                        v.setProfessionalName(c.getProfessionalName());
                        v.setCollegeName(c.getCollegeName());
                        v.setGrade(c.getGrade());
                    }
                    ClassesRollcallAlertVO ct = classesTeacherInfoMap.get(v.getClassesId());
                    if (null != ct) {
                        v.setTeacherName(ct.getTeacherName());
                    }
                }
            } else  {
                pageData.setData(new ArrayList<>());
            }
        } else  {
            pageData.setData(new ArrayList<>());
        }
        return pageData;
    }

    public PageData<UnNormalRollcallAlertVO> queryStudentUnNormalRollcall(Long orgId, Long collegeId, String name, Date start, Date end, Double undkl, Integer pageIndex, Integer pageSize) {
        PageData<UnNormalRollcallAlertVO> pageData = new PageData<>();
        if (null == pageIndex || pageIndex < 1) {
            pageIndex = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 20;
        }
        pageData.getPage().setPageNumber(pageIndex);
        pageData.getPage().setPageSize(pageSize);
        pageData.getPage().setTotalElements(0L);
        pageData.getPage().setTotalPages(1);

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        int type = getOrgArithmetic(orgId);
        String dklSql = getDklCalSql(type);
        StringBuilder sb = new StringBuilder("");
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        Set<Long> classesSet = new HashSet<>();
        if (null != collegeId && collegeId > 0) {
            sb.append(" AND r.college_id=:collegeId ");
            params.put("collegeId", collegeId);
        }
        if (!StringUtils.isEmpty(name)) {//姓名模糊匹配
            sb.append(" AND (r.STUDENT_NAME like :name OR r.STUDENT_NUM like :name)");
            params.put("name", "%" + name + "%");
        }
        if (null == undkl || undkl <= 0) {
            undkl = 0.3;
        }
        params.put("undkl", undkl);
        Date cur = new Date();
        Date today = new Date();
        today = DateUtil.getZerotime(today);
        if (null == start) {
            start = DateUtil.afterNDay(cur, -1);
            start = DateUtil.getZerotime(start);
        } else {
            start = DateUtil.getZerotime(start);
        }
        if (null == end) {
            end = DateUtil.getZerotime(cur);
        } else {
            end = DateUtil.afterNDay(end, 1);
            end = DateUtil.getZerotime(end);
        }
        if (end.after(today)) {
            end = today;
        }
        params.put("start", start);
        params.put("end", end);
        String sql_count = SQL_STUDENT_UNNORMAL_COURSE_ALERT_COUNT.replaceAll("#database#", ddDatabaseName);
        sql_count = sql_count.replaceAll("#queryCondition#", sb.toString());
        log.info("Count Student unNormal rollcall alert sql:{}", sql_count);
        List<Long> counts =  template.query(sql_count, params, (ResultSet rs, int rowNum) -> rs.getLong(1));
        Long count = 0L;
        if (null != counts && counts.size() > 0) {
            count = counts.get(0);
        }
        pageData.getPage().setTotalElements(count);
        pageData.getPage().setTotalPages(PageUtil.cacalatePagesize(count, pageSize));
        if (count > 0) {
            String sql = SQL_STUDENT_UNNORMAL_COURSE_ALERT.replaceAll("#database#", ddDatabaseName);
            sql = sql.replaceAll("#queryCondition#", sb.toString());
            int ps = (pageIndex - 1) * pageSize;
            sql = sql +  " LIMIT " + ps + ", " + pageSize;
            log.info("Query Student unNormal rollcall alert sql:{}", sql);
            List<UnNormalRollcallAlertVO> list = template.query(sql, params, (ResultSet rs, int rowNum) ->
                    new UnNormalRollcallAlertVO(rs.getLong("studentId"), rs.getLong("courseId"), rs.getLong("shouldCount"), rs.getLong("unNormal") )
            );
            if (null != list && !list.isEmpty()) {
                pageData.setData(list);
                Set<Long> classesIds = new HashSet<>();
                Set<Long> courseIds = new HashSet<>();
                Set<Long> idSet = new HashSet<>();
                for (UnNormalRollcallAlertVO v : list) {
//                    classesIds.add(v.getClassesId());
                    idSet.add(v.getStudentId());
                    courseIds.add(v.getCourseId());
                }
                Map<Long, IDNoNameDTO> courseMap = new HashMap<>();
                Map<Long, IDNoNameDTO> classesMap = new HashMap<>();
                //SQL_ORG_COURSE
                if (!courseIds.isEmpty()) {
                    params.clear();
                    params.put("courseIds", courseIds);
                    sql = SQL_ORG_COURSE.replaceAll("#orgMnagerDB#", orgDatabaseName);
                    List<IDNoNameDTO> courseList = template.query(sql, params, (ResultSet rs, int rowNum) ->
                            new IDNoNameDTO(rs.getLong("ID"), rs.getString("NAME"))
                    );
                    if (null != courseList && !courseList.isEmpty()) {
                        for (IDNoNameDTO d : courseList) {
                            courseMap.put(d.getId(), d);
                        }
                    }
                }
                Map<Long, StudentRollcallAlertVO> maps = new HashMap<>();
                if (!idSet.isEmpty()) {
                    params.clear();
                    params.put("ids", idSet);
                    sql = SQL_STUDENT_BASE_INFO.replaceAll("#orgMnagerDB#", orgDatabaseName);
                    //u.ID id, u.JOB_NUMBER studentNo, u.`NAME` studentName, c.`NAME` classesName, p.`NAME` professionalName, g.`NAME` collegeName
                    List<StudentRollcallAlertVO> list2 = template.query(sql, params, (ResultSet rs, int rowNum) ->
                            new StudentRollcallAlertVO(rs.getLong("id"), rs.getString("studentNo"), rs.getString("studentName"),
                                    rs.getLong("classesId"), rs.getString("classesName"), rs.getString("professionalName"), rs.getString("collegeName") )
                    );
                    if (null != list2) {
                        for (StudentRollcallAlertVO v : list2) {
                            maps.put(v.getStudentId(), v);
                            classesIds.add(v.getClassesId());
                        }
                    }
                }
                //SQL_ORG_CLASSES_SIMPLE
                if (!classesIds.isEmpty()) {
                    params.clear();
                    params.put("classesIds", classesIds);
                    sql = SQL_ORG_CLASSES_SIMPLE.replaceAll("#orgMnagerDB#", orgDatabaseName);
                    List<IDNoNameDTO> classesList = template.query(sql, params, (ResultSet rs, int rowNum) ->
                            new IDNoNameDTO(rs.getLong("ID"), rs.getString("NAME"), rs.getString("TEACHING_YEAR"))
                    );
                    if (null != classesList && !classesList.isEmpty()) {
                        for (IDNoNameDTO d : classesList) {
                            classesMap.put(d.getId(), d);
                        }
                    }
                }
                for (UnNormalRollcallAlertVO v : list) {
                    IDNoNameDTO dc = courseMap.get(v.getCourseId());
                    if (null != dc) {
                        v.setCourseName(dc.getName());
                    }
                    StudentRollcallAlertVO v2 = maps.get(v.getStudentId());
                    if (null != v2) {
                        v.setStudentName(v2.getStudentName());
                        v.setStudentNo(v2.getStudentNo());
                        v.setClassesName(v2.getClassesName());
                        v.setClassesId(v2.getClassesId());
                        v.setProfessionalName(v2.getProfessionalName());
                        v.setCollegeName(v2.getCollegeName());
                    }

                    dc = classesMap.get(v.getClassesId());
                    if (null != dc) {
                        v.setGrade(dc.getNo());
                    }
                }
            }
        }
        return pageData;
    }

    public PageData<StudentRollcallAlertVO> findStudentRollcallAlert(Long orgId, Long collegeId, String name, Date start, Date end, Integer pageIndex, Integer pageSize) {
        PageData<StudentRollcallAlertVO> pageData = new PageData<>();
        if (null == pageIndex || pageIndex < 1) {
            pageIndex = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 20;
        }
        pageData.getPage().setPageNumber(pageIndex);
        pageData.getPage().setPageSize(pageSize);
        pageData.getPage().setTotalElements(0L);
        pageData.getPage().setTotalPages(1);

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        int type = getOrgArithmetic(orgId);
        String dklSql = getDklCalSql(type);

        StringBuilder sb = new StringBuilder("");
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        if (null != collegeId && collegeId > 0) {
            sb.append(" AND r.college_id=:collegeId ");
            params.put("collegeId", collegeId);
        }
        if (!StringUtils.isEmpty(name)) {//姓名模糊匹配
            sb.append(" AND (r.STUDENT_NAME like :name OR r.STUDENT_NUM like :name)");
            params.put("name", "%" + name + "%");
        }

        params.put("dkl", 0.3);
        Date cur = new Date();
        Date today = new Date();
        today = DateUtil.getZerotime(today);
        if (null == start) {
            start = DateUtil.afterNDay(cur, -1);
            start = DateUtil.getZerotime(start);
        } else {
            start = DateUtil.getZerotime(start);
        }
        if (null == end) {
            end = DateUtil.getZerotime(cur);
        } else {
            end = DateUtil.afterNDay(end, 1);
            end = DateUtil.getZerotime(end);
        }
        if (end.after(today)) {
            end = today;
        }
        params.put("start", DateUtil.formatShort(start));
        params.put("end", DateUtil.formatShort(end));
        String sql_count = SQL_STUDENT_ROLLCALL_ALERT_COUNT.replaceAll("#database#", ddDatabaseName);
        sql_count = sql_count.replaceAll("#dklcal#", dklSql);
        sql_count = sql_count.replaceAll("#queryCondition#", sb.toString());
        log.info("Count Student rollcall alert sql:{}, start:{}, end: {}", sql_count, params.get("start"), params.get("end"));
        List<Long> counts =  template.query(sql_count, params, (ResultSet rs, int rowNum) -> rs.getLong(1));
        Long count = 0L;
        if (null != counts && counts.size() > 0) {
            count = counts.get(0);
        }
        pageData.getPage().setTotalElements(count);
        pageData.getPage().setTotalPages(PageUtil.cacalatePagesize(count, pageSize));
        if (count > 0) {
            String sql = SQL_STUDENT_ROLLCALL_ALERT.replaceAll("#database#", ddDatabaseName);
            sql = sql.replaceAll("#dklcal#", dklSql);
            sql = sql.replaceAll("#queryCondition#", sb.toString());
            int ps = (pageIndex - 1) * pageSize;
            sql = sql +  " LIMIT " + ps + ", " + pageSize;
            log.info("Query Student  rollcall alert sql:{}", sql);
            List<StudentRollcallAlertVO> list = template.query(sql, params, (ResultSet rs, int rowNum) ->
                    new StudentRollcallAlertVO(rs.getLong("studentId"), rs.getLong("shouldCount"), rs.getLong("normal"), rs.getDouble("dkl") )
            );
            if (null != list && !list.isEmpty()) {
                pageData.setData(list);
                Set<Long> idSet = new HashSet<>();
                for (StudentRollcallAlertVO v : list) {
                    idSet.add(v.getStudentId());
                }
                if (!idSet.isEmpty()) {
                    params.clear();
                    params.put("ids", idSet);
                    sql = SQL_STUDENT_BASE_INFO.replaceAll("#orgMnagerDB#", orgDatabaseName);
                    //u.ID id, u.JOB_NUMBER studentNo, u.`NAME` studentName, c.`NAME` classesName, p.`NAME` professionalName, g.`NAME` collegeName
                    List<StudentRollcallAlertVO> list2 = template.query(sql, params, (ResultSet rs, int rowNum) ->
                            new StudentRollcallAlertVO(rs.getLong("id"), rs.getString("studentNo"), rs.getString("studentName"),
                                    rs.getLong("classesId"), rs.getString("classesName"), rs.getString("professionalName"), rs.getString("collegeName") )
                    );
                    Map<Long, StudentRollcallAlertVO> maps = new HashMap<>();
                    if (null != list2) {
                        for (StudentRollcallAlertVO v : list2) {
                            maps.put(v.getStudentId(), v);
                        }
                    }
                    if (!maps.isEmpty()) {
                        for (StudentRollcallAlertVO v : list) {
                            StudentRollcallAlertVO v2 = maps.get(v.getStudentId());
                            if(null != v2) {
                                v.setStudentName(v2.getStudentName());
                                v.setStudentNo(v2.getStudentNo());
                                v.setClassesName(v2.getClassesName());
                                v.setProfessionalName(v2.getProfessionalName());
                                v.setCollegeName(v2.getCollegeName());
                            }
                        }
                    }
                }
            }
        }
        return pageData;
    }


    public UnitRollcallStatisticsDOVO findUnitRollcallStatistics(Long orgId, Long collegeId, Long professionalId, String timeRange) {
        UnitRollcallStatisticsDOVO r = new UnitRollcallStatisticsDOVO ();
        r.setStatistics(new RollcallStatisticsVO ());
        r.setUnitList(new ArrayList<>());
        if (null == orgId || orgId <= 0) {
            return r;
        }
        if (StringUtils.isEmpty(timeRange) || timeRange.indexOf("~")<= 0) {
            return r;
        }
        int type = getOrgArithmetic(orgId);
        String dklSql = getDklCalSql(type);

        int p = timeRange.indexOf("~");
        String start = timeRange.substring(0, p);
        String end = timeRange.substring(p + 1);

        StringBuilder conditon = new StringBuilder("");
        StringBuilder field = new StringBuilder("");
        StringBuilder group = new StringBuilder("");
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        params.put("start", start);
        params.put("end", end);
        if (null != professionalId && professionalId > 0) {
            conditon.append(" AND r.professional_id = :unitId");
            field.append("r.CLASS_ID as unitId, r.class_name as unitName,");
            group.append(" GROUP BY r.CLASS_ID, r.class_name ");
            params.put("unitId", professionalId);
        } else if (null != collegeId && collegeId > 0) {
            conditon.append(" AND r.college_id = :unitId");
            field.append("r.professional_id as unitId, r.professional_name as unitName,");
            group.append(" GROUP BY r.professional_id, r.professional_name ");
            params.put("unitId", collegeId);
        } else {
            field.append("r.college_id as unitId, r.college_name as unitName,");
            group.append(" GROUP BY r.college_id, r.college_name");
        }
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

        String sqlc = SQL_UNIT_ROLLCALL_STATISTICS.replaceAll("#database#", ddDatabaseName);
        sqlc = sqlc.replaceAll("#dklcal#", dklSql);
        sqlc = sqlc.replaceAll("#queryCondition#", conditon.toString());
        sqlc = sqlc.replaceAll("#unitField#", " NULL as unitId, NULL as unitName,");
        sqlc = sqlc.replaceAll("#groupunitField#", "");
        //c.unitId, c.unitName, c.total, c.normal, c.later, c.askForLeave, c.leaveEarly, c.truant, c.dkl
        log.info("Rollcall Unit statistics count sql:{}, start:{}, end: {}, professionalId:{}, collegeId:{}", sqlc, params.get("start"), params.get("end"), professionalId, collegeId);
        List<UnitRollcallStatisticsVO> list = template.query(sqlc, params, (ResultSet rs, int rowNum) ->
                new UnitRollcallStatisticsVO(rs.getLong("unitId"), rs.getString("unitName"),
                        rs.getInt("total"), rs.getInt("normal"),
                        rs.getInt("later"), rs.getInt("askForLeave"),
                        rs.getInt("leaveEarly"), rs.getInt("truant"), rs.getDouble("dkl") )
        );
        if (null != list || list.size() > 0) {
            UnitRollcallStatisticsVO v = list.get(0);
            r.setStatistics(new RollcallStatisticsVO(v.getYdrs(), v.getSdrs(), v.getCdrs(), v.getQjrs(), v.getKkrs(), v.getZtrs(), v.getDkl()));
        }
        String sql = SQL_UNIT_ROLLCALL_STATISTICS.replaceAll("#database#", ddDatabaseName);
        sql = sql.replaceAll("#dklcal#", dklSql);
        sql = sql.replaceAll("#queryCondition#", conditon.toString());
        sql = sql.replaceAll("#unitField#", field.toString());
        sql = sql.replaceAll("#groupunitField#", group.toString());
        log.info("Rollcall unit statistics sql:{}, start:{}, end: {}, professionalId:{}, collegeId:{}", sql, params.get("start"), params.get("end"), professionalId, collegeId);
        list = template.query(sql, params, (ResultSet rs, int rowNum) ->
                new UnitRollcallStatisticsVO(rs.getLong("unitId"), rs.getString("unitName"),
                        rs.getInt("total"), rs.getInt("normal"),
                        rs.getInt("later"), rs.getInt("askForLeave"), rs.getInt("truant"),
                        rs.getInt("leaveEarly"), rs.getDouble("dkl") )
        );
        if (null != list) {
            r.setUnitList(list);
        }
        return r;
    }

    public PageData<CourseRollcallStatisticsVO> findCourseRollcallStatistics(Long orgId, Long collegeId, String course, String timeRange, Integer pageIndex, Integer pageSize) {
        PageData<CourseRollcallStatisticsVO> pageData = new PageData<>();
        if (null == pageIndex || pageIndex < 1) {
            pageIndex = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 20;
        }
        pageData.getPage().setPageNumber(pageIndex);
        pageData.getPage().setPageSize(pageSize);
        pageData.getPage().setTotalElements(0L);
        pageData.getPage().setTotalPages(1);

        if (null == orgId || orgId <= 0) {
            return pageData;
        }
        if (StringUtils.isEmpty(timeRange) || timeRange.indexOf("~")<= 0) {
            return pageData;
        }

        int p = timeRange.indexOf("~");
        String start = timeRange.substring(0, p);
        String end = timeRange.substring(p + 1);

        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        params.put("start", start);
        params.put("end", end);
        StringBuilder condition = new StringBuilder();
        if (null != collegeId && collegeId > 0) {
            condition.append(" AND r.college_id = :collegeId ");
            params.put("collegeId", collegeId);
        }
        if (!StringUtils.isEmpty(course)) {
            condition.append(" AND s.COURSE_NAME like :course ");
            params.put("course", "%" + course + "%");
        }

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

        String sqlc = SQL_COUSE_ROLLCALL_STATISTICS_COUNT.replaceAll("#database#", ddDatabaseName);
        sqlc = sqlc.replaceAll("#queryCondition#", condition.toString());
        log.info("Rollcall Course statistics Count sql:{}, start:{}, end: {}", sqlc, params.get("start"), params.get("end"));
        List<Integer> clist = template.query(sqlc, params, (ResultSet rs, int rowNum) -> rs.getInt("count"));
        int count = 0;
        if (null != clist && clist.size() > 0) {
            count = clist.get(0);
        }
        pageData.getPage().setTotalPages(PageUtil.cacalatePagesize(count, pageSize));
        pageData.getPage().setTotalElements(new Long(count));
        if (count > 0) {
            int type = getOrgArithmetic(orgId);
            String dklSql = getDklCalSql(type);
            String sql = SQL_COUSE_ROLLCALL_STATISTICS.replaceAll("#database#", ddDatabaseName);
            sql = sql.replaceAll("#dklcal#", dklSql);
            sql = sql.replaceAll("#queryCondition#", condition.toString());
            int ps = (pageIndex - 1) * pageSize;
            sql = sql +  " LIMIT " + ps + ", " + pageSize;
            //c.courseName, c.teacherName, c.total, c.normal, c.later, c.askForLeave, c.leaveEarly, c.truant, c.dkl
            log.info("Rollcall Course statistics sql:{}, start:{}, end: {}", sql, params.get("start"), params.get("end"));
            List<CourseRollcallStatisticsVO> list = template.query(sql, params, (ResultSet rs, int rowNum) ->
                    new CourseRollcallStatisticsVO(rs.getLong("courseId"),
                            rs.getString("courseName"), rs.getString("teacherName"),
                            rs.getInt("total"), rs.getInt("normal"),
                            rs.getInt("later"), rs.getInt("askForLeave"),
                            rs.getInt("leaveEarly"), rs.getInt("truant"), rs.getDouble("dkl"))
            );
            pageData.setData(list);
        }
        return pageData;
    }


    public PageData<TeacherRollcallStatisticsVO> findTeacherRollcallStatistics(Long orgId, Long collegeId, String teacher, String timeRange, Integer pageIndex, Integer pageSize) {
        PageData<TeacherRollcallStatisticsVO> pageData = new PageData<>();
        if (null == pageIndex || pageIndex < 1) {
            pageIndex = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 20;
        }
        pageData.getPage().setPageNumber(pageIndex);
        pageData.getPage().setPageSize(pageSize);
        pageData.getPage().setTotalElements(0L);
        pageData.getPage().setTotalPages(1);

        if (null == orgId || orgId <= 0) {
            return pageData;
        }
        if (StringUtils.isEmpty(timeRange) || timeRange.indexOf("~")<= 0) {
            return pageData;
        }

        int p = timeRange.indexOf("~");
        String start = timeRange.substring(0, p);
        String end = timeRange.substring(p + 1);

        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        params.put("start", start);
        params.put("end", end);
        StringBuilder condition = new StringBuilder();
        if (null != collegeId && collegeId > 0) {
            condition.append(" AND r.college_id = :collegeId ");
            params.put("collegeId", collegeId);
        }
        if (!StringUtils.isEmpty(teacher)) {
            condition.append(" AND s.TEACHER_NAME like :teacher ");
            params.put("teacher", "%" + teacher + "%");
        }

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

        String sqlc = SQL_TEACHER_ROLLCALL_STATISTICS_COUNT.replaceAll("#database#", ddDatabaseName);
        sqlc = sqlc.replaceAll("#queryCondition#", condition.toString());
        log.info("Rollcall Teacher statistics Count sql:{}, start:{}, end: {}", sqlc, params.get("start"), params.get("end"));
        List<Integer> clist = template.query(sqlc, params, (ResultSet rs, int rowNum) -> rs.getInt("count"));
        int count = 0;
        if (null != clist && clist.size() > 0) {
            count = clist.get(0);
        }
        pageData.getPage().setTotalPages(PageUtil.cacalatePagesize(count, pageSize));
        pageData.getPage().setTotalElements(new Long(count));
        if (count > 0) {
            int type = getOrgArithmetic(orgId);
            String dklSql = getDklCalSql(type);
            String sql = SQL_TEACHER_ROLLCALL_STATISTICS.replaceAll("#database#", ddDatabaseName);
            sql = sql.replaceAll("#dklcal#", dklSql);
            sql = sql.replaceAll("#queryCondition#", condition.toString());
            int ps = (pageIndex - 1) * pageSize;
            sql = sql +  " LIMIT " + ps + ", " + pageSize;
            //c.teacherId, c.teacherName, c.total, c.normal, c.later, c.askForLeave, c.leaveEarly, c.truant, c.dkl
            log.info("Rollcall Teacher statistics sql:{}, start:{}, end: {}", sql, params.get("start"), params.get("end"));
            List<TeacherRollcallStatisticsVO> list = template.query(sql, params, (ResultSet rs, int rowNum) ->
                    new TeacherRollcallStatisticsVO(rs.getLong("teacherId"), rs.getString("teacherName"),
                            rs.getInt("total"), rs.getInt("normal"),
                            rs.getInt("later"), rs.getInt("askForLeave"),
                            rs.getInt("leaveEarly"), rs.getInt("truant"), rs.getDouble("dkl"))
            );
            pageData.setData(list);
            Set<Long> teacherIds = new HashSet<>();
            if (null != list) {
                for (TeacherRollcallStatisticsVO v : list) {
                    teacherIds.add(v.getTeacherId());
                }
            }
            if (!teacherIds.isEmpty()) {
                params.clear();
                params.put("ids", teacherIds);

                sql = SQL_TEACHER_WORKNO_COLLEGE.replaceAll("#orgMnagerDB#", orgDatabaseName);
                //u.ID, u.JOB_NUMBER, c.`NAME`
                List<IDNoNameDTO> dList = template.query(sql, params, (ResultSet rs, int rowNum) ->
                        new IDNoNameDTO(rs.getLong("ID"), rs.getString("NAME"),
                                rs.getString("JOB_NUMBER"))
                );
                Map<Long, IDNoNameDTO> map = new HashMap<>();
                if (null != dList) {
                    for (IDNoNameDTO d : dList) {
                        map.put(d.getId(), d);
                    }
                }
                for (TeacherRollcallStatisticsVO v : list) {
                    IDNoNameDTO d = map.get(v.getTeacherId());
                    if (null != d) {
                        v.setTeacherWorkNo(d.getNo());
                        v.setCollegeName(d.getName());
                    }
                }
            }
        }
        return pageData;
    }

    private RollcallStatisticsVO queryDayRollcallCount(String sql, NamedParameterJdbcTemplate template, Map<String, Object> params) {
        log.info("Week rollcall statics sql:({})", sql);
        List<RollcallStatisticsVO> list = template.query(sql, params, (ResultSet rs, int rowNum) ->
                new RollcallStatisticsVO(
                        rs.getInt("total"), rs.getInt("normal"),
                        rs.getInt("later"), rs.getInt("askForLeave"),
                        rs.getInt("truant"), rs.getInt("leaveEarly"), rs.getDouble("dkl"))
        );
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public CurrentDayRollcallStatisticsVO findDateStatistics(Long orgId, Long collegeId, String currentDate, String dayEndtime) {
        CurrentDayRollcallStatisticsVO v = new CurrentDayRollcallStatisticsVO ();

        if (null == orgId || orgId <= 0) {
            return v;
        }
        if (StringUtils.isEmpty(currentDate)) {
            return v;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        params.put("teachDate", currentDate);
        int type = getOrgArithmetic(orgId);
        String dklSql = getDklCalSql(type);
        String sql = getDaySql(params, dklSql, collegeId, false, null, null, true, dayEndtime);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        log.info("Rollcall currentday statistics sql:{}, orgId:{}, teachDate: {}", sql, orgId, currentDate);
        RollcallStatisticsVO vo = queryDayRollcallCount(sql, template, params);
        if (null != vo) {
            v.setDkl(vo.getDkl());
            v.setYdrc(vo.getYdrs());
            v.setSdrc(vo.getSdrs());
            v.setYcrc(vo.getKkrs() + vo.getQjrs() + vo.getCdrs() + vo.getZtrs());
        }
        sql = "SELECT COUNT(r.STUDENT_ID) AS total "  +
                "FROM #database#.dd_rollcall r LEFT JOIN #database#.dd_schedule_rollcall sr ON sr.ID=r.SCHEDULE_ROLLCALL_ID LEFT JOIN #database#.dd_schedule s ON sr.SCHEDULE_ID=s.ID " +
                "WHERE s.ORGAN_ID = ? AND s.TEACH_DATE = ?   AND s.END_TIME < ? AND s.DELETE_FLAG=0 AND r.DELETE_FLAG=0 AND sr.DELETE_FLAG=0";
        sql = sql.replaceAll("#database#", ddDatabaseName);
        int c = jdbcTemplate.queryForObject(sql, new Object[] {orgId, currentDate, dayEndtime}, new int[] {Types.BIGINT, Types.VARCHAR, Types.VARCHAR}, Integer.class);
        v.setLjxssl(c);

        sql = "SELECT COUNT(*) FROM #database#.dd_schedule s WHERE s.ORGAN_ID=? AND s.TEACH_DATE=?  AND s.END_TIME < ? AND s.DELETE_FLAG=0";
        sql = sql.replaceAll("#database#", ddDatabaseName);
        c = jdbcTemplate.queryForObject(sql, new Object[] {orgId, currentDate, dayEndtime}, new int[] {Types.BIGINT, Types.VARCHAR, Types.VARCHAR}, Integer.class);
        v.setLjbksl(c);
        return v;
    }

    private String getDaySql(Map<String, Object> params, String dklSql, Long collegeId, boolean daterange, String startday, String endday, boolean time, String dayEndtime) {
        StringBuilder sqlb = new StringBuilder("SELECT COUNT(*) AS total,")
                .append("SUM(IF(r.`TYPE` = 1, 1, 0)) AS normal,")
                .append("SUM(IF(r.`TYPE` = 3, 1, 0)) AS later,")
                .append("SUM(IF(r.`TYPE` = 4, 1, 0)) AS askForLeave,")
                .append("SUM(IF(r.`TYPE` = 5, 1, 0)) AS leaveEarly,")
                .append("SUM(IF(r.`TYPE` = 2, 1, 0)) AS truant,")
                .append("#dklcal#")
                .append("FROM #database#.dd_rollcall r LEFT JOIN #database#.dd_schedule_rollcall sr ON sr.ID=r.SCHEDULE_ROLLCALL_ID LEFT JOIN #database#.dd_schedule s ON sr.SCHEDULE_ID=s.ID ")
                .append("WHERE s.ORGAN_ID = :orgId ");
        if (daterange) {
            sqlb.append("AND s.TEACH_DATE >= :startday ");
            sqlb.append("AND s.TEACH_DATE <= :endday ");
            params.put("startday", startday);
            params.put("endday", endday);
        } else {
            sqlb.append(" AND s.TEACH_DATE = :teachDate ");//params outer set
        }
        if (time) {
            sqlb.append("AND s.END_TIME < :dayendtime ");
            params.put("dayendtime", dayEndtime);
        }
        if (null != collegeId && collegeId > 0) {
            sqlb.append("AND r.college_id = :collegeId ");
            params.put("collegeId", collegeId);
        }
        sqlb.append("AND s.DELETE_FLAG=0 AND r.DELETE_FLAG=0 AND sr.DELETE_FLAG=0");
        String sql = sqlb.toString().replaceAll("#database#", ddDatabaseName);
        sql = sql.replaceAll("#dklcal#", dklSql);
        return sql;
    }

    public SchoolWeekRollcallScreenZhV2VO queryWeekAnPreWeekRollcall(Long orgId, Long collegeId, List<DayAndDayOfWeekDTO> days, String startday, String endDay, String preWeekStartday, String preWeekEndday, String dayEndtime) {
        log.info("Week sreen rollcall count:orgId({}), collegeId:({}), monday:({}), curentday:({}), pre week monday:({}), sunday({})", orgId, collegeId, startday, endDay, preWeekStartday, preWeekEndday);
        SchoolWeekRollcallScreenZhV2VO v = new SchoolWeekRollcallScreenZhV2VO();
        v.setDays(new ArrayList<>());
        if (null == orgId || orgId <= 0) {
            return v;
        }
        if (StringUtils.isEmpty(startday) || StringUtils.isEmpty(endDay)) {
            return v;
        }
        if (null == days || days.isEmpty()) {
            return v;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        int type = getOrgArithmetic(orgId);
        String dklSql = getDklCalSql(type);

        String sql = getDaySql(params, dklSql, collegeId, false, null, null, false, null);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

         for (int i = 0; i < days.size(); i++){
             DayAndDayOfWeekDTO d = days.get(i);
             params.put("teachDate", d.getDay());
             if (i == days.size() - 1) {
                 sql = getDaySql(params, dklSql, collegeId, false, null, null, true, dayEndtime);
             }
             RollcallStatisticsVO vo = queryDayRollcallCount(sql, template, params);
             if (null != vo) {
                 SchoolWeekRollcallScreenVO o = new SchoolWeekRollcallScreenVO(d.getDay(), vo.getYdrs(), vo.getSdrs(), vo.getCdrs(), vo.getQjrs(), vo.getKkrs(), vo.getZtrs());
                 o.setDayOfWeek(d.getDayOfWeek().toString());
                 o.setDkl(vo.getDkl());
                 v.getDays().add(o);
             }
        }
        params.clear();
        sql = getDaySql(params, dklSql, collegeId, true, startday, endDay, false, null);
        params.put("orgId", orgId);
        RollcallStatisticsVO w = queryDayRollcallCount(sql, template, params);
        if (null != w) {
            v.setWeek(w);
            v.setQs(w.getDkl());
        }
        params.put("startday", preWeekStartday);
        params.put("endday", preWeekEndday);
        RollcallStatisticsVO prew = queryDayRollcallCount(sql, template, params);
        if (null != w && null != prew) {
            v.setQs(w.getDkl() - prew.getDkl());
        }
        if (null == w && null != prew) {
            v.setQs(0 - prew.getDkl());
        }
        return v;
    }




    public PageData<TeacherRollcallStatisticsVO> findManagerTeacherRollcallStatistics(Long orgId, Long collegeId, String teacher, String timeRange, Integer pageIndex, Integer pageSize) {
        PageData<TeacherRollcallStatisticsVO> pageData = new PageData<>();
        if (null == pageIndex || pageIndex < 1) {
            pageIndex = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 20;
        }
        pageData.getPage().setPageNumber(pageIndex);
        pageData.getPage().setPageSize(pageSize);
        pageData.getPage().setTotalElements(0L);
        pageData.getPage().setTotalPages(1);

        if (null == orgId || orgId <= 0) {
            return pageData;
        }
        if (StringUtils.isEmpty(timeRange) || timeRange.indexOf("~")<= 0) {
            return pageData;
        }

        int p = timeRange.indexOf("~");
        String start = timeRange.substring(0, p);
        String end = timeRange.substring(p + 1);

        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        StringBuilder condition = new StringBuilder();
        if (null != collegeId && collegeId > 0) {
            condition.append(" AND u.COLLEGE_ID = :collegeId ");
            params.put("collegeId", collegeId);
        }
        if (!StringUtils.isEmpty(teacher)) {
            condition.append(" AND (u.`NAME` like :teacher OR u.JOB_NUMBER like :teacher) ");
            params.put("teacher", "%" + teacher + "%");
        }

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        String sqlc = "SELECT COUNT(*) count FROM (SELECT COUNT(*) FROM #orgMnagerDB#.t_classes_teacher t, #orgMnagerDB#.t_user u, #orgMnagerDB#.t_college c " +
                "WHERE u.ORG_ID=:orgId AND t.TEACHER_ID=u.ID AND u.COLLEGE_ID=c.ID AND u.DELETE_FLAG=0 " +
                " #queryCondition# " +
                "GROUP BY u.ID, u.JOB_NUMBER, u.`NAME`, c.`NAME`) c";

        sqlc = sqlc.replaceAll("#orgMnagerDB#", orgDatabaseName);
        sqlc = sqlc.replaceAll("#queryCondition#", condition.toString());
        log.info("Rollcall ManagerTeacher statistics Count sql:{}", sqlc);
        List<Integer> clist = template.query(sqlc, params, (ResultSet rs, int rowNum) -> rs.getInt("count"));
        int count = 0;
        if (null != clist && clist.size() > 0) {
            count = clist.get(0);
        }
        pageData.getPage().setTotalPages(PageUtil.cacalatePagesize(count, pageSize));
        pageData.getPage().setTotalElements(new Long(count));
        if (count > 0) {
            //导员考勤列表统计
            String sql = "SELECT " +
                    "u.ID teacherId, u.JOB_NUMBER teacherNo, u.`NAME` teacherName, c.`NAME` collegeName, GROUP_CONCAT(t.CLASSES_ID) classes " +
                    "FROM #orgMnagerDB#.t_classes_teacher t, #orgMnagerDB#.t_user u, #orgMnagerDB#.t_college c " +
                    "WHERE u.ORG_ID=:orgId AND t.TEACHER_ID=u.ID AND u.COLLEGE_ID=c.ID AND u.DELETE_FLAG=0 " +
                    " #queryCondition# " +
                    "GROUP BY u.ID, u.JOB_NUMBER, u.`NAME`, c.`NAME`";
            sql = sql.replaceAll("#orgMnagerDB#", orgDatabaseName);
            sql = sql.replaceAll("#queryCondition#", condition.toString());
            int ps = (pageIndex - 1) * pageSize;
            sql = sql +  " LIMIT " + ps + ", " + pageSize;
            //c.teacherId, c.teacherName, c.total, c.normal, c.later, c.askForLeave, c.leaveEarly, c.truant, c.dkl
            log.info("Rollcall ManagerTeacher statistics sql:{}", sql);
            List<TeacherForRollcallDTO> tlist = template.query(sql, params, (ResultSet rs, int rowNum) ->
                    new TeacherForRollcallDTO(rs.getLong("teacherId"), rs.getString("teacherName"),
                            rs.getString("teacherNo"),rs.getString("collegeName"),
                            rs.getString("classes"))
            );

            List<TeacherRollcallStatisticsVO> list = new ArrayList<>();
            if (null != tlist) {
                int type = getOrgArithmetic(orgId);
                params.clear();
                params.put("orgId", orgId);
                params.put("start", start);
                params.put("end", end);
                String dklSql = getDklCalSql(type);
                sqlc =   "SELECT " +
                        "COUNT(*) AS total, " +
                        "SUM(IF(r.`TYPE` = 1, 1, 0)) AS normal," +
                        "SUM(IF(r.`TYPE` = 3, 1, 0)) AS later," +
                        "SUM(IF(r.`TYPE` = 4, 1, 0)) AS askForLeave," +
                        "SUM(IF(r.`TYPE` = 5, 1, 0)) AS leaveEarly," +
                        "SUM(IF(r.`TYPE` = 2, 1, 0)) AS truant," +
                        "#dklcal#" +
                        "FROM #database#.dd_rollcall r , #database#.dd_schedule_rollcall sr, #database#.dd_schedule s " +
                        "WHERE  r.org_id = :orgId AND s.ORGAN_ID = :orgId AND r.SCHEDULE_ROLLCALL_ID = sr.ID AND sr.SCHEDULE_ID=s.ID AND s.TEACH_DATE >= :start AND s.TEACH_DATE <= :end " +
                        " #queryCondition# ";

                sqlc = sqlc.replaceAll("#database#", ddDatabaseName);
                sqlc = sqlc.replaceAll("#dklcal#", dklSql);
                for (TeacherForRollcallDTO d : tlist) {
                    TeacherRollcallStatisticsVO vo = new TeacherRollcallStatisticsVO();
                    vo.setTeacherId(d.getTeacherId());
                    vo.setTeacherName(d.getTeacherName());
                    vo.setTeacherWorkNo(d.getTeacherWorkNo());
                    vo.setCollegeName(d.getCollegeName());
                    list.add(vo);

                    Set<Long> cids = new HashSet<>();
                    if (!StringUtils.isEmpty(d.getClasses())) {
                        String[] cc = d.getClasses().split(",");
                        for (String cid : cc) {
                            if (!StringUtils.isEmpty(cid)) {
                                cids.add(new Long(cid));
                            }
                        }
                    }
                    String c = "";
                    if (!cids.isEmpty()) {
                        params.put("classes", cids);
                        c = " AND r.CLASS_ID IN (:classes) ";
                    }
                    sql = sqlc.replaceAll("#queryCondition#", c);
                    List<RollcallStatisticsVO> rlist = template.query(sql, params, (ResultSet rs, int rowNum) ->
                            new RollcallStatisticsVO(rs.getInt("total"), rs.getInt("normal"),
                                    rs.getInt("later"),rs.getInt("askForLeave"),
                                    rs.getInt("truant"), rs.getInt("leaveEarly"), rs.getDouble("dkl"))
                    );
                    if (null != rlist && rlist.size() > 0) {
                        RollcallStatisticsVO rvo = rlist.get(0);
                        vo.setYdrs(rvo.getYdrs());
                        vo.setSdrs(rvo.getSdrs());
                        vo.setCdrs(rvo.getCdrs());
                        vo.setQjrs(rvo.getQjrs());
                        vo.setKkrs(rvo.getKkrs());
                        vo.setZtrs(rvo.getZtrs());
                        vo.setDkl(rvo.getDkl());
                    }
                }
            }


            pageData.setData(list);
        }
        return pageData;
    }
}
