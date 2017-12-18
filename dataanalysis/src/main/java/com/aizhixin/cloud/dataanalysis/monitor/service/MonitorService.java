package com.aizhixin.cloud.dataanalysis.monitor.service;

import com.aizhixin.cloud.dataanalysis.monitor.dto.CollegeGpaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class MonitorService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<CollegeGpaDTO> getCollegeGpa(Long orgId){
        RowMapper<CollegeGpaDTO> mapper=new RowMapper<CollegeGpaDTO>() {
            @Override
            public CollegeGpaDTO mapRow(ResultSet rs, int i) throws SQLException {
                CollegeGpaDTO collegeGpaDTO=new CollegeGpaDTO();
                collegeGpaDTO.setCollegeId(rs.getString("COLLEGE_ID"));
                collegeGpaDTO.setCollegeName(rs.getString("COLLEGE_NAME"));
                collegeGpaDTO.setAvgGPA(rs.getFloat("AVG_SCORE"));
                return collegeGpaDTO;
            }
        };
        List<CollegeGpaDTO> result;
        String currentGradeSql="SELECT SEMESTER ,TEACHER_YEAR  FROM `t_teaching_score_statistics`  where ORG_ID="+orgId+"   ORDER BY TEACHER_YEAR DESC,SEMESTER DESC LIMIT 1";
        Map currentGradeMap= jdbcTemplate.queryForMap(currentGradeSql);
        int teacherYear=Integer.valueOf(currentGradeMap.get("TEACHER_YEAR")+"");
        int semester= Integer.valueOf(currentGradeMap.get("SEMESTER")+"");
        String sql="SELECT COLLEGE_ID ,COLLEGE_NAME,AVG_SCORE  FROM `t_teaching_score_statistics` where ORG_ID="+orgId+" ";
        if(0!=teacherYear){
            sql+=" and TEACHER_YEAR="+teacherYear+" ";
        }
        if(0!=semester){
            sql+=" and SEMESTER="+semester;
        }
        result=jdbcTemplate.query(sql,mapper);
        return result;
    }
}
