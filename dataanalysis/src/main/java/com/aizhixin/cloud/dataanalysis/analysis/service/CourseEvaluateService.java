package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.CourseEvaluateDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CourseEvaluateDetailDTO;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.PageDomain;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.domain.SortDTO;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class CourseEvaluateService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PageJdbcUtil pageJdbcUtil;

    public PageData<CourseEvaluateDTO> getCourseEvaluate(long orgId, String semesterId,String grade, String courseName, String sort, Integer pageSize, Integer pageNumber) {
        Map<String, Object> result = new HashMap<>();
        List<SortDTO> sortDTOS = new ArrayList();
        RowMapper<CourseEvaluateDTO> rowMapper = new RowMapper<CourseEvaluateDTO>() {
            @Override
            public CourseEvaluateDTO mapRow(ResultSet rs, int i) throws SQLException {
                CourseEvaluateDTO courseEvaluateDTO = new CourseEvaluateDTO();
                courseEvaluateDTO.setCourseCode(rs.getString("COURSE_CODE"));
                courseEvaluateDTO.setCourseName(rs.getString("COURSE_NAME"));
                courseEvaluateDTO.setAvgScore(rs.getFloat("score"));
                return courseEvaluateDTO;
            }
        };
        String querySql = "SELECT COURSE_CODE,COURSE_NAME,avg(AVG_SCORE) as score FROM `T_COURSE_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + " and ORG_ID=" + orgId + " ";
        String countSql = "SELECT COURSE_CODE FROM `T_COURSE_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + "  and ORG_ID=" + orgId + " ";
        if (null != semesterId) {
            querySql += " and SEMESTER_ID=" + semesterId + " ";
            countSql += " and SEMESTER_ID=" + semesterId + " ";
        }
        if (null != grade) {
            querySql += " and TEACHER_YEAR=" + grade + " ";
            countSql += " and TEACHER_YEAR=" + grade + " ";
        }
        if (null != courseName) {
            querySql += " and COURSE_NAME like %" + courseName + "% ";
            countSql += " and COURSE_NAME like %" + courseName + "% ";
        }
        querySql += "  group by COURSE_CODE";
        countSql += "  group by COURSE_CODE";
        countSql="SELECT count(1) FROM ("+countSql+") aa";
        SortDTO sortDTO = new SortDTO();
        if (null != sort) {
            if (sort == "desc") {
                sortDTO.setKey("desc");
                sortDTOS.add(sortDTO);
            }
        }
       Map map=pageJdbcUtil
               .getPageInfor(pageSize, pageNumber, rowMapper, sortDTOS, querySql, countSql);
        PageData<CourseEvaluateDTO> p=new PageData<CourseEvaluateDTO>();
        p.setData((List) map.get("data"));
        p.setPage((PageDomain)map.get("page"));
        return p;
    }

    public Map<String, Object> getCourseEvaluateDetail(long orgId, String semesterId,String grade, String courseCode, String name,Integer pageNumber,Integer pageSize) {
        RowMapper<CourseEvaluateDetailDTO> rowMapper=new RowMapper<CourseEvaluateDetailDTO>() {
            @Override
            public CourseEvaluateDetailDTO mapRow(ResultSet rs, int i) throws SQLException {
                CourseEvaluateDetailDTO courseEvaluateDetailDTO=new CourseEvaluateDetailDTO();
                courseEvaluateDetailDTO.setTeachingClassName(rs.getString("TEACHING_CLASS_NAME"));
                courseEvaluateDetailDTO.setTeacherName(rs.getString("CHARGE_PERSON"));
                courseEvaluateDetailDTO.setAvgScore(rs.getFloat("AVG_SCORE"));
                return null;
            }
        };
        String querySql = "SELECT TEACHING_CLASS_NAME,CHARGE_PERSON,AVG_SCORE  FROM `T_COURSE_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + " and ORG_ID=" + orgId + " ";
        String countSql = "SELECT count(1) FROM `T_COURSE_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + "  and ORG_ID=" + orgId + " ";
        if (null != semesterId) {
            querySql += " and SEMESTER_ID=" + semesterId + " ";
            countSql += " and SEMESTER_ID=" + semesterId + " ";
        }
        if (null != grade) {
            querySql += " and TEACHER_YEAR=" + grade + " ";
            countSql += " and TEACHER_YEAR=" + grade + " ";
        }
        if (null != courseCode) {
            querySql += " and COURSE_CODE=" + courseCode + " ";
            countSql += " and COURSE_CODE=" + courseCode + " ";
        }
        if (null != name) {
            querySql += " and TEACHING_CLASS_NAME like %" + name + "% or CHARGE_PERSON like %"+ name + "% ";
            countSql += " and TEACHING_CLASS_NAME like %" + name + "% or CHARGE_PERSON like %"+ name + "% ";
        }
        return pageJdbcUtil
                .getPageInfor(pageSize, pageNumber, rowMapper, null, querySql, countSql);
    }
}
