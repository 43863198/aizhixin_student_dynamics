package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.CourseEvaluateDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CourseEvaluateDetailDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherEvaluateDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherEvaluateDetailDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.CourseEvaluate;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeacherEvaluate;
import com.aizhixin.cloud.dataanalysis.analysis.respository.TeacherEvaluateRespository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.PageDomain;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.domain.SortDTO;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import liquibase.util.StringUtils;
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
    @Autowired
    private TeacherEvaluateRespository teacherEvaluateRespository;

    public PageData<TeacherEvaluateDTO> getTeacherEvaluate(long orgId, String semesterId, String teacherYear,String collegeIds, String teacherName, String sort, Integer pageSize, Integer pageNumber) {
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
        if (!StringUtils.isEmpty(semesterId)) {
            querySql += " and SEMESTER=" + semesterId + " ";
            countSql += " and SEMESTER=" + semesterId + " ";
        }
        if (!StringUtils.isEmpty(teacherYear)) {
            querySql += " and TEACHER_YEAR=" + teacherYear + " ";
            countSql += " and TEACHER_YEAR=" + teacherYear + " ";
        }
        if (!StringUtils.isEmpty(collegeIds)) {
            querySql += " and COLLEGE_ID IN (" + collegeIds + ") ";
            countSql += " and COLLEGE_ID IN (" + collegeIds + ") ";
        }
        if (!StringUtils.isEmpty(teacherName)) {
            querySql += " and TEACHER_NAME like '%" + teacherName + "%' ";
            countSql += " and TEACHER_NAME like '%" + teacherName + "%' ";
        }
        querySql += "  group by TEACHER_ID";
        countSql += "  group by TEACHER_ID";
        countSql="SELECT count(1) FROM ("+countSql+") aa";
        SortDTO sortDTO = new SortDTO();
        if (!StringUtils.isEmpty(sort)) {
            if (sort == "desc") {
                sortDTO.setKey("desc");
                sortDTOS.add(sortDTO);
            }
        }

        Map map=pageJdbcUtil
                .getPageInfor(pageSize, pageNumber, rowMapper, sortDTOS, querySql, countSql);
        PageData<TeacherEvaluateDTO> p=new PageData<TeacherEvaluateDTO>();
        p.setData((List) map.get("data"));
        p.setPage((PageDomain)map.get("page"));
        return p;
    }

    public PageData<TeacherEvaluateDetailDTO> getTeacherEvaluateDetail(long orgId, String semesterId,String teacherYear ,String teacherId, String className, Integer pageNumber, Integer pageSize) {
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
        if (!StringUtils.isEmpty(semesterId)) {
            querySql += " and SEMESTER=" + semesterId + " ";
            countSql += " and SEMESTER=" + semesterId + " ";
        }
        if (!StringUtils.isEmpty(teacherYear)) {
            querySql += " and TEACHER_YEAR=" + teacherYear + " ";
            countSql += " and TEACHER_YEAR=" + teacherYear + " ";
        }
        if (!StringUtils.isEmpty(teacherId)) {
            querySql += " and TEACHER_ID=" + teacherId + " ";
            countSql += " and TEACHER_ID=" + teacherId + " ";
        }
        if (!StringUtils.isEmpty(className)) {
            querySql += " and CLASS_NAME like '%" + className + "%' " ;
            countSql += " and CLASS_NAME like '%" + className + "%'  ";
        }
        Map map=pageJdbcUtil
                .getPageInfor(pageSize, pageNumber, rowMapper, null, querySql, countSql);
        PageData<TeacherEvaluateDetailDTO> p=new PageData<TeacherEvaluateDetailDTO>();
        p.setData((List) map.get("data"));
        p.setPage((PageDomain)map.get("page"));
        return p;
    }
    /**
     * 课程评价数据
     * @param statisticsList
     */
    public void saveList(List<TeacherEvaluate> statisticsList){
        teacherEvaluateRespository.save(statisticsList);
    }
    /**
     * 保存评价数据
     * @param teacherEvaluate
     */
    public void save(TeacherEvaluate teacherEvaluate){
        teacherEvaluateRespository.save(teacherEvaluate);
    }
    /**
     * 保存评价数据
     * @param teacherEvaluate
     */
    public void update(TeacherEvaluate teacherEvaluate){
        teacherEvaluateRespository.save(teacherEvaluate);
    }
}
