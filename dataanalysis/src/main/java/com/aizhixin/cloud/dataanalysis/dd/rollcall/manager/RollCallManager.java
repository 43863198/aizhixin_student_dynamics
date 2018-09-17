package com.aizhixin.cloud.dataanalysis.dd.rollcall.manager;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.dto.OrgTeacherInfoDTO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.SchoolWeekRollcallScreenVO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.TeacherCourseRollcallAlertVO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.TeacherRollcallAlertVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

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
            "DATE_FORMAT(r.`CREATED_DATE`, '%Y-%m-%d') as caldate ," +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r " +
            "WHERE  r.org_id = :orgId AND r.CREATED_DATE BETWEEN :start AND :end " +
            "#queryCondition#" +
            "GROUP BY r.TEACHER_ID, DATE_FORMAT(r.`CREATED_DATE`, '%Y-%m-%d') " +
            "ORDER BY TEACHER_ID " +
            ") c " +
            "WHERE c.dkl < :dkl";
    private String SQL_TEACHER_DAY_ROLLCALL_ALERT = "SELECT " +
            "c.teacherId, c.caldate, c.dkl " +
            "FROM ( " +
            "SELECT " +
            "r.TEACHER_ID as teacherId," +
            "DATE_FORMAT(r.`CREATED_DATE`, '%Y-%m-%d') as caldate ," +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r " +
            "WHERE  r.org_id = :orgId AND r.CREATED_DATE BETWEEN :start AND :end " +
            "#queryCondition#" +
            "GROUP BY r.TEACHER_ID, DATE_FORMAT(r.`CREATED_DATE`, '%Y-%m-%d') " +
            "ORDER BY TEACHER_ID " +
            ") c " +
            "WHERE c.dkl < :dkl";
    private String SQL_TEACHER_COURSE_CLASSES_ROLLCALL_ALERT = "SELECT " +
            "c.teacherId, c.caldate, c.courseId, c.courseName, c.period, c.dkl " +
            "FROM (" +
            "SELECT " +
            "r.TEACHER_ID as teacherId," +
            "DATE_FORMAT(r.`CREATED_DATE`, '%Y-%m-%d') as caldate , " +
            "r.COURSE_ID as courseId, s.COURSE_NAME as courseName," +
            " CONCAT(s.PERIOD_NO,'-',s.PERIOD_NO + s.PERIOD_NUM -1) as period," +
//            "ROUND((SUM(IF(r.`TYPE` = 1, 1, 0)) + SUM(IF(r.`TYPE` = 4, 1, 0)))/COUNT(*),4) AS dkl " +
            "#dklcal#" +
            "FROM #database#.dd_rollcall r,#database#.dd_schedule_rollcall sr, #database#.dd_schedule s " +
            "WHERE  r.org_id = :orgId AND s.ORGAN_ID = :orgId AND r.SCHEDULE_ROLLCALL_ID = sr.ID AND sr.SCHEDULE_ID=s.ID AND r.CREATED_DATE BETWEEN :start AND :end and r.TEACHER_ID in (:teachers)" +
            "GROUP BY r.TEACHER_ID, DATE_FORMAT(r.`CREATED_DATE`, '%Y-%m-%d'), r.COURSE_ID, s.COURSE_NAME,s.PERIOD_NO " +
            "ORDER BY r.CREATED_DATE, r.TEACHER_ID,r.COURSE_ID,s.PERIOD_NO " +
            ") c " +
            "WHERE c.dkl < :dkl";

    private String SQL_ORG_SET = "SELECT arithmetic FROM #database#.DD_ORGAN_SET WHERE ORGAN_ID=?";

    private String SQL_ORG_TEACHER_INFO = "SELECT " +
            "u.ID, u.`NAME` teacherName, u.JOB_NUMBER  workNo, c.`NAME` collegeName " +
            "FROM #orgMnagerDB#.t_user u LEFT JOIN #orgMnagerDB#.t_college c ON u.COLLEGE_ID=c.ID " +
            "WHERE u.ORG_ID = :orgId AND u.ID IN (:teacherIds)";

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
            sb.append(" AND r.college_id=:collegeId");
            params.put("collegeId", collegeId);
        }
        if (null != teacherId && teacherId > 0) {
            sb.append(" AND r.TEACHER_ID=:teacherId");
            params.put("teacherId", teacherId);
        }
        if (null == dkl || dkl <= 0) {
            dkl = 0.8;
        }
        params.put("dkl", dkl);
        Date cur = new Date();
        if (null == start) {
            start = DateUtil.afterNDay(cur, -1);
            start =  DateUtil.getZerotime(start);
        } else {
            start = DateUtil.getZerotime(start);
        }
        if (null == end) {
            end = DateUtil.afterNDay(cur, 1);
            end =  DateUtil.getZerotime(end);
        } else {
            end = DateUtil.afterNDay(end, 1);
            end = DateUtil.getZerotime(end);
        }
        params.put("start", start);
        params.put("end", end);

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
                            v.append("[").append(vo.getCourseName()).append(", ").append(vo.getPeriod()).append(", ").append(vo.getDkl()).append("]");
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
}
