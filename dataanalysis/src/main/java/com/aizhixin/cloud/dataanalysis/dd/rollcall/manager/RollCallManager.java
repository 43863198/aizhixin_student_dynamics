package com.aizhixin.cloud.dataanalysis.dd.rollcall.manager;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.dto.OrgTeacherInfoDTO;
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
            "FROM dd_api_test.dd_rollcall r  " +
            "WHERE r.org_id = :orgId AND r.CREATED_DATE >= :start AND r.CREATED_DATE < :end " +
            " #queryCondition# " +
            "GROUP BY r.STUDENT_ID , r.STUDENT_NUM, r.STUDENT_NAME, r.CLASS_ID, r.class_name, r.professional_name, r.college_name, r.COURSE_ID " +
            " ) c WHERE c.undkl > :undkl";

    private String SQL_STUDENT_UNNORMAL_COURSE_ALERT = "SELECT studentId, studentNo, studentName, classesId, classesName, prefessionalName, collegeName, courseId, shouldCount, unNormal " +
            "FROM ( " +
            "SELECT r.STUDENT_ID AS studentId, r.STUDENT_NUM AS studentNo, r.STUDENT_NAME AS studentName, r.CLASS_ID AS classesId, r.class_name AS classesName, r.professional_name AS prefessionalName, r.college_name AS collegeName, r.COURSE_ID AS courseId,  " +
            "COUNT(*) AS shouldCount, " +
            "(SUM(IF(r.`TYPE` = 3, 1, 0)) + SUM(IF(r.`TYPE` = 4, 1, 0)) + SUM(IF(r.`TYPE` = 5, 1, 0)) + SUM(IF(r.`TYPE` = 2, 1, 0))) AS unNormal, " +
            "(SUM(IF(r.`TYPE` = 3, 1, 0)) + SUM(IF(r.`TYPE` = 4, 1, 0)) + SUM(IF(r.`TYPE` = 5, 1, 0)) + SUM(IF(r.`TYPE` = 2, 1, 0)))/COUNT(*) AS undkl " +
            "FROM dd_api_test.dd_rollcall r  " +
            "WHERE r.org_id = :orgId AND r.CREATED_DATE >= :start AND r.CREATED_DATE < :end " +
            " #queryCondition# " +
            "GROUP BY r.STUDENT_ID , r.STUDENT_NUM, r.STUDENT_NAME, r.CLASS_ID, r.class_name, r.professional_name, r.college_name, r.COURSE_ID " +
            "ORDER BY undkl DESC, r.STUDENT_NUM) c WHERE c.undkl > :undkl";

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
        pageData.getPage().setPageNumber((int)(count/pageSize + (0 == count % pageSize ? 0 : 1)));
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
        pageData.getPage().setPageNumber((int)(count/pageSize + (0 == count % pageSize ? 0 : 1)));
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
        sql_count = sql_count.replaceAll("#queryCondition#", sb.toString());
        log.info("Count Student unNormal rollcall alert sql:{}", sql_count);
        List<Long> counts =  template.query(sql_count, params, (ResultSet rs, int rowNum) -> rs.getLong(1));
        Long count = 0L;
        if (null != counts && counts.size() > 0) {
            count = counts.get(0);
        }
        pageData.getPage().setTotalElements(count);
        pageData.getPage().setPageNumber((int)(count/pageSize + (0 == count % pageSize ? 0 : 1)));
        if (count > 0) {
            String sql = SQL_STUDENT_UNNORMAL_COURSE_ALERT.replaceAll("#database#", ddDatabaseName);
            sql = sql.replaceAll("#queryCondition#", sb.toString());
            int ps = (pageIndex - 1) * pageSize;
            sql = sql +  " LIMIT " + ps + ", " + pageSize;
            log.info("Query Student unNormal rollcall alert sql:{}", sql);
            List<UnNormalRollcallAlertVO> list = template.query(sql, params, (ResultSet rs, int rowNum) ->
                    new UnNormalRollcallAlertVO(rs.getLong("studentId"), rs.getString("studentNo"), rs.getString("studentName"),
                            rs.getLong("classesId"), rs.getString("classesName"), rs.getString("prefessionalName"),
                            rs.getString("collegeName"), rs.getLong("courseId"), rs.getLong("shouldCount"), rs.getLong("unNormal") )
            );
            if (null != list && !list.isEmpty()) {
                pageData.setData(list);
            }
        }
        return pageData;
    }
    }
