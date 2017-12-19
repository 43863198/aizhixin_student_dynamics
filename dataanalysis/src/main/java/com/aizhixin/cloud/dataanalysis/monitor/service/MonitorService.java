package com.aizhixin.cloud.dataanalysis.monitor.service;

import com.aizhixin.cloud.dataanalysis.common.exception.CommonException;
import com.aizhixin.cloud.dataanalysis.common.service.AuthUtilService;
import com.aizhixin.cloud.dataanalysis.monitor.dto.AlarmDTO;
import com.aizhixin.cloud.dataanalysis.monitor.dto.CollegeGpaDTO;
import com.aizhixin.cloud.dataanalysis.monitor.dto.CourseEvaluateDTO;
import com.aizhixin.cloud.dataanalysis.monitor.dto.SchoolStudentStateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class MonitorService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AuthUtilService authUtilService;
    public List<CollegeGpaDTO> getCollegeGpa(Long orgId) {
        RowMapper<CollegeGpaDTO> mapper = new RowMapper<CollegeGpaDTO>() {
            @Override
            public CollegeGpaDTO mapRow(ResultSet rs, int i) throws SQLException {
                CollegeGpaDTO collegeGpaDTO = new CollegeGpaDTO();
                collegeGpaDTO.setCollegeId(rs.getString("COLLEGE_ID"));
                collegeGpaDTO.setCollegeName(rs.getString("COLLEGE_NAME"));
                collegeGpaDTO.setAvgGPA(rs.getFloat("AVG_SCORE"));
                return collegeGpaDTO;
            }
        };
        List<CollegeGpaDTO> result;
        String currentGradeSql = "SELECT SEMESTER ,TEACHER_YEAR  FROM `t_teaching_score_statistics`  where ORG_ID=" + orgId + "   ORDER BY TEACHER_YEAR DESC,SEMESTER DESC LIMIT 1";

        Map currentGradeMap=new HashMap() ;
        try {
            currentGradeMap= jdbcTemplate.queryForMap(currentGradeSql);
        }catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }

        int teacherYear = Integer.valueOf(currentGradeMap.get("TEACHER_YEAR") + "");
        int semester = Integer.valueOf(currentGradeMap.get("SEMESTER") + "");
        String sql = "SELECT COLLEGE_ID ,COLLEGE_NAME,AVG_SCORE  FROM `t_teaching_score_statistics` where ORG_ID=" + orgId + " ";
        if (0 != teacherYear) {
            sql += " and TEACHER_YEAR=" + teacherYear + " ";
        }
        if (0 != semester) {
            sql += " and SEMESTER=" + semester;
        }
        sql+=" ORDER BY AVG_SCORE DESC limit 10";
        result = jdbcTemplate.query(sql, mapper);
        return result;
    }
    public CourseEvaluateDTO getCourseScore(Long orgId){
        RowMapper<CourseEvaluateDTO> rowMapper=new RowMapper<CourseEvaluateDTO>() {
            @Override
            public CourseEvaluateDTO mapRow(ResultSet rs, int i) throws SQLException {
                CourseEvaluateDTO courseEvaluateDTO=new CourseEvaluateDTO();
                courseEvaluateDTO.setMustCourseScore(rs.getFloat("mustCourseScore"));
                courseEvaluateDTO.setSelectCourseScore(rs.getFloat("selectCourseScore"));
                courseEvaluateDTO.setMustCourseScore(rs.getFloat("otherCourseScore"));
                return courseEvaluateDTO;
            }
        };
        CourseEvaluateDTO result=null;
        String currentGradeSql = "SELECT SEMESTER ,TEACHER_YEAR  FROM `t_course_evaluate`  where ORG_ID="+orgId+"   ORDER BY TEACHER_YEAR DESC,SEMESTER DESC LIMIT 1";
        Map currentGradeMap=new HashMap() ;
        try {
            currentGradeMap= jdbcTemplate.queryForMap(currentGradeSql);
        }catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }

        int teacherYear = Integer.valueOf(currentGradeMap.get("TEACHER_YEAR") + "");
        int semester = Integer.valueOf(currentGradeMap.get("SEMESTER") + "");
        String sql="SELECT MAX(CASE a.type WHEN 1 then a.score ELSE 0 END) 'mustCourseScore',MAX(CASE a.type WHEN 2 then a.score ELSE 0 END) 'selectCourseScore',MAX(CASE a.type WHEN 3 then a.score ELSE 0 END) 'otherCourseScore' FROM ( ";
        String tempSql1="SELECT SUM(AVG_SCORE) AS score,COURSE_TYPE type FROM t_course_evaluate WHERE COURSE_TYPE = 1 and ORG_ID=" + orgId + "" ;
        String tempSql2="SELECT SUM(AVG_SCORE) AS score,COURSE_TYPE type FROM t_course_evaluate WHERE COURSE_TYPE = 2 and ORG_ID=" + orgId + "" ;
        String tempSql3="SELECT SUM(AVG_SCORE) AS score,COURSE_TYPE type FROM t_course_evaluate WHERE COURSE_TYPE = 3 and ORG_ID=" + orgId + "" ;
        if (0 != teacherYear) {
            tempSql1 += " and TEACHER_YEAR=" + teacherYear + " ";
            tempSql2 += " and TEACHER_YEAR=" + teacherYear + " ";
            tempSql3 += " and TEACHER_YEAR=" + teacherYear + " ";
        }
        if (0 != semester) {
            tempSql1 += " and SEMESTER=" + semester;
            tempSql2 += " and SEMESTER=" + semester;
            tempSql3 += " and SEMESTER=" + semester;
        }
        sql+=tempSql1+" UNION ALL "+tempSql2+" UNION ALL "+tempSql3+" ) a";
        result=jdbcTemplate.queryForObject(sql,rowMapper);
        return result;
    }
    public AlarmDTO getAlarm(Long orgId){
        RowMapper<AlarmDTO> rowMapper=new RowMapper<AlarmDTO>() {
            @Override
            public AlarmDTO mapRow(ResultSet rs, int i) throws SQLException {
                AlarmDTO alarmDTO=new AlarmDTO();
                alarmDTO.setAlarmTotal(rs.getInt("alarmTotal"));
                alarmDTO.setDealwithTotal(rs.getInt("dealwithTotal"));
                return alarmDTO;
            }
        };
        AlarmDTO alarmDTO=null;
        String currentGradeSql = "SELECT TEACHING_YEAR  FROM `t_warning_information`  where ORG_ID=" + orgId + " and DELETE_FLAG=0 and TEACHING_YEAR<>'null' ORDER BY TEACHING_YEAR DESC LIMIT 1";
        Map currentGradeMap=new HashMap() ;
        try {
            currentGradeMap= jdbcTemplate.queryForMap(currentGradeSql);
        }catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }
        int teacherYear = Integer.valueOf(currentGradeMap.get("TEACHING_YEAR") + "");
        String sql="SELECT MAX(CASE a.alarm WHEN 'alarm' then a.count ELSE 0 END) 'alarmTotal',MAX(CASE a.alarm WHEN 'dealwithTotal' then a.count ELSE 0 END) 'dealwithTotal' FROM( ";
        String tempSql1="SELECT count(1) as count, 'alarm'  FROM t_warning_information WHERE DELETE_FLAG=0  and ORG_ID=" + orgId + "" ;
        String tempSql2="SELECT count(1) as count, 'dealwithTotal' FROM t_warning_information where WARNING_STATE=20 or WARNING_STATE=40 and DELETE_FLAG=0 and ORG_ID=" + orgId + "" ;
        if (0 != teacherYear) {
            tempSql1 += " and TEACHING_YEAR=" + teacherYear + " ";
            tempSql2 += " and TEACHING_YEAR=" + teacherYear + " ";
        }
        sql+=tempSql1+" UNION ALL "+tempSql2+" ) a";
        alarmDTO=jdbcTemplate.queryForObject(sql,rowMapper);
        return alarmDTO;
    }
    public SchoolStudentStateDTO getPersons(Long orgId){
        RowMapper<SchoolStudentStateDTO> rowMapper=new RowMapper<SchoolStudentStateDTO>() {
            @Override
            public SchoolStudentStateDTO mapRow(ResultSet rs, int i) throws SQLException {
                SchoolStudentStateDTO schoolStudentStateDTO=new SchoolStudentStateDTO();
                schoolStudentStateDTO.setAllStudent(rs.getInt("allstudent"));
                schoolStudentStateDTO.setLeaveSchoolStudent(rs.getInt("leavestudent"));
                schoolStudentStateDTO.setOutSchoolStudent(rs.getInt("outstudent"));
                schoolStudentStateDTO.setInSchoolStudent(schoolStudentStateDTO.getAllStudent()-schoolStudentStateDTO.getLeaveSchoolStudent()-schoolStudentStateDTO.getOutSchoolStudent());
                return schoolStudentStateDTO;
            }
        };
        SchoolStudentStateDTO result=new SchoolStudentStateDTO();
//t_user t_electric_fence_statistics dd_student_leave_schedule
        String sql="SELECT MAX(CASE a.allstudent WHEN 'allstudent' then a.count ELSE 0 END) 'allstudent',MAX(CASE a.allstudent WHEN 'outstudent' then a.count ELSE 0 END) 'outstudent',MAX(CASE a.allstudent WHEN 'leavestudent' then a.count ELSE 0 END) 'leavestudent' FROM( ";
        String tempSql1="SELECT COUNT(1) as count, 'allstudent' FROM "+authUtilService.getOrgDbName()+".t_user where USER_TYPE=70 AND DELETE_FLAG=0 and ORG_ID=" + orgId + "" ;
        String tempSql2="SELECT COUNT(1) as count, 'outstudent' FROM "+authUtilService.getOrgDbName()+".t_electric_fence_statistics WHERE SEMIH_OUT_COUNT>0 AND DELETE_FLAG=0 AND  ORG_ID=" + orgId + "" ;
        String tempSql3="SELECT COUNT(1) as count, 'leavestudent' FROM "+authUtilService.getDdDbName()+".dd_student_leave_schedule WHERE DELETE_FLAG=0 AND  schedule_id in (SELECT id FROM "+authUtilService.getDdDbName()+".dd_schedule where organ_id="+orgId+" )" ;
        Date date=new Date();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String timeZone=" BETWEEN '"+df.format(date)+" 08:00:00'  AND '"+df.format(date)+" 18:00:00'";
        tempSql2+=" AND CREATED_DATE "+timeZone;
        tempSql3+=" AND CREATED_DATE "+timeZone;
        sql+=tempSql1+" UNION ALL "+tempSql2+" UNION ALL "+tempSql3+" ) a";
        result=jdbcTemplate.queryForObject(sql,rowMapper);
        return result;

    }
}
