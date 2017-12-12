package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.CourseEvaluateDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CourseEvaluateDetailDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherEvaluateDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherEvaluateDetailDTO;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.domain.SortDTO;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TeacherEvaluateService {
    @Autowired
    private PageJdbcUtil pageJdbcUtil;

    public Map<String, Object> getTeacherEvaluate(long orgId, String semesterId, String teacherId, String sort, Integer pageSize, Integer pageNumber) {
        List<SortDTO> sortDTOS = new ArrayList();
        RowMapper<TeacherEvaluateDTO> rowMapper = new RowMapper<TeacherEvaluateDTO>() {
            @Override
            public TeacherEvaluateDTO mapRow(ResultSet rs, int i) throws SQLException {
                TeacherEvaluateDTO teacherEvaluateDTO = new TeacherEvaluateDTO();
                teacherEvaluateDTO.setTeacherId(rs.getString("TEACHER_ID"));
                teacherEvaluateDTO.setTeacherName(rs.getString("TEACHER_NAME"));
                teacherEvaluateDTO.setAvgScore(rs.getFloat("score"));
                return teacherEvaluateDTO;
            }
        };
        String querySql = "SELECT TEACHER_NAME,TEACHER_ID,avg(AVG_SCORE) as score FROM `T_TEACHER_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + " and ORG_ID=" + orgId + " ";
        String countSql = "SELECT TEACHER_ID FROM `T_TEACHER_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + "  and ORG_ID=" + orgId + " ";
        if (null != semesterId) {
            querySql += " and SEMESTER_ID=" + semesterId + " ";
            countSql += " and SEMESTER_ID=" + semesterId + " ";
        }
        if (null != teacherId) {
            querySql += " and TEACHER_ID=" + teacherId + " ";
            countSql += " and TEACHER_ID=" + teacherId + " ";
        }
        querySql += "  group by TEACHER_ID";
        countSql += "  group by TEACHER_ID";
        countSql="SELECT count(1) FROM ("+countSql+") aa";
        SortDTO sortDTO = new SortDTO();
        if (null != sort) {
            if (sort == "desc") {
                sortDTO.setKey("desc");
                sortDTOS.add(sortDTO);
            }
        }

        return pageJdbcUtil
                .getPageInfor(pageSize, pageNumber, rowMapper, sortDTOS, querySql, countSql);
    }

    public Map<String, Object> getTeacherEvaluateDetail(long orgId, String semesterId, String teacherId, String teacherName, Integer pageNumber, Integer pageSize) {
        RowMapper<TeacherEvaluateDetailDTO> rowMapper = new RowMapper<TeacherEvaluateDetailDTO>() {
            @Override
            public TeacherEvaluateDetailDTO mapRow(ResultSet rs, int i) throws SQLException {
                TeacherEvaluateDetailDTO teacherEvaluateDetailDTO = new TeacherEvaluateDetailDTO();
                teacherEvaluateDetailDTO.setClassName(rs.getString("CLASS_NAME"));
                teacherEvaluateDetailDTO.setCourseName(rs.getString("COURSE_NAME"));
                teacherEvaluateDetailDTO.setAvgScore(rs.getFloat("AVG_SCORE"));
                return teacherEvaluateDetailDTO;
            }
        };
        String querySql = "SELECT COURSE_NAME,CLASS_NAME,AVG_SCORE  FROM `T_TEACHER_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + " and ORG_ID=" + orgId + " ";
        String countSql = "SELECT count(1) FROM `T_TEACHER_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + "  and ORG_ID=" + orgId + " ";
        if (null != semesterId) {
            querySql += " and SEMESTER_ID=" + semesterId + " ";
            countSql += " and SEMESTER_ID=" + semesterId + " ";
        }
        if (null != teacherId) {
            querySql += " and COURSE_CODE=" + teacherId + " ";
            countSql += " and COURSE_CODE=" + teacherId + " ";
        }
        if (null != teacherName) {
            querySql += " and TEACHING_CLASS_NAME like %" + teacherName + "% or CHARGE_PERSON like %" + teacherName + "% ";
            countSql += " and TEACHING_CLASS_NAME like %" + teacherName + "% or CHARGE_PERSON like %" + teacherName + "% ";
        }
        return pageJdbcUtil
                .getPageInfor(pageSize, pageNumber, rowMapper, null, querySql, countSql);
    }
}
