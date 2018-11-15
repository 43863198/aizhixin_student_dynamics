package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.*;
import com.aizhixin.cloud.dataanalysis.analysis.entity.CourseEvaluate;
import com.aizhixin.cloud.dataanalysis.analysis.respository.CourseEvaluateRespository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.PageDomain;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.domain.SortDTO;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import liquibase.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    @Autowired
    private CourseEvaluateRespository courseEvaluateRespository;
//
RowMapper<CourseEvaluateDTO> rowMapper = new RowMapper<CourseEvaluateDTO>() {
    @Override
    public CourseEvaluateDTO mapRow(ResultSet rs, int i) throws SQLException {
        CourseEvaluateDTO courseEvaluateDTO = new CourseEvaluateDTO();
        courseEvaluateDTO.setCourseCode(rs.getString("COURSE_CODE"));
        courseEvaluateDTO.setCourseName(rs.getString("COURSE_NAME"));
        courseEvaluateDTO.setAvgScore(rs.getFloat("score"));
        courseEvaluateDTO.setStatisticalTime(rs.getTimestamp("STATISTICAL_TIME"));
        return courseEvaluateDTO;
    }
};
public HomeData<CourseEvaluateDTO> getHomeCourseEvaluate(long orgId) {
    List<CourseEvaluateDTO> result = new ArrayList<>();
    List<SortDTO> sortDTOS = new ArrayList();

    String sql="SELECT SEMESTER ,TEACHER_YEAR  FROM `T_COURSE_EVALUATE`  where ORG_ID="+orgId+" ORDER BY TEACHER_YEAR DESC,SEMESTER DESC LIMIT 1";
    Map currentGradeMap=new HashMap() ;
    try {
        currentGradeMap= jdbcTemplate.queryForMap(sql);
    }catch (EmptyResultDataAccessException emptyResultDataAccessException){
        return null;
    }
    String teacherYear=currentGradeMap.get("TEACHER_YEAR")+"";
    String semester= currentGradeMap.get("SEMESTER")+"";
    String querySql = "SELECT * FROM (SELECT COURSE_CODE,COURSE_NAME,avg(AVG_SCORE) as score,max(STATISTICAL_TIME) as STATISTICAL_TIME FROM `T_COURSE_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + " and ORG_ID=" + orgId + " ";
    if (!StringUtils.isEmpty(semester)) {
        querySql += " and SEMESTER=" + semester + " ";
    }
    if (!StringUtils.isEmpty(teacherYear)) {
        querySql += " and TEACHER_YEAR=" + teacherYear + " ";
    }

    querySql += "  group by COURSE_CODE) aa ORDER BY aa.score DESC LIMIT 10";

    result=jdbcTemplate.query(querySql,rowMapper);
    HomeData<CourseEvaluateDTO> h=new HomeData();
    TeacherlYearData teacherlYearData=new TeacherlYearData();
    teacherlYearData.setSemester(Integer.valueOf(semester));
    teacherlYearData.setTeacherYear(Integer.valueOf(teacherYear));
    h.setTeacherlYearData(teacherlYearData);
    h.setData(result);
    return h;

}
    public PageData<CourseEvaluateDTO> getCourseEvaluate(long orgId, String semesterId,String teacherYear,String collegeIds, String courseName, String sort, Integer pageSize, Integer pageNumber) {
//        Map<String, Object> result = new HashMap<>();
//        List<SortDTO> sortDTOS = new ArrayList();
//        String querySql = "SELECT COURSE_CODE,COURSE_NAME,avg(AVG_SCORE) as score,max(STATISTICAL_TIME) as STATISTICAL_TIME FROM `T_COURSE_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + " and ORG_ID=" + orgId + " ";
//        String countSql = "SELECT COURSE_CODE FROM `T_COURSE_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + "  and ORG_ID=" + orgId + " ";
//        if (!StringUtils.isEmpty(semesterId)) {
//            querySql += " and SEMESTER=" + semesterId + " ";
//            countSql += " and SEMESTER=" + semesterId + " ";
//        }
//        if (!StringUtils.isEmpty(teacherYear)) {
//            querySql += " and TEACHER_YEAR=" + teacherYear + " ";
//            countSql += " and TEACHER_YEAR=" + teacherYear + " ";
//        }
//        if (!StringUtils.isEmpty(collegeIds)) {
//            querySql += " and COLLEGE_ID IN (" + collegeIds + ") ";
//            countSql += " and COLLEGE_ID IN (" + collegeIds + ") ";
//        }
//        if (!StringUtils.isEmpty(courseName)) {
//            querySql += " and COURSE_NAME like '%" + courseName + "%' ";
//            countSql += " and COURSE_NAME like '%" + courseName + "%' ";
//        }
//        querySql += "  group by COURSE_CODE";
//        countSql += "  group by COURSE_CODE";
//        countSql="SELECT count(1) FROM ("+countSql+") aa";
//        SortDTO sortDTO = new SortDTO();
//        if (!StringUtils.isEmpty(sort) ) {
//            if (sort == "desc") {
//                sortDTO.setKey("desc");
//                sortDTOS.add(sortDTO);
//            }
//        }
//       Map map=pageJdbcUtil
//               .getPageInfor(pageSize, pageNumber, rowMapper, sortDTOS, querySql, countSql);
//        PageData<CourseEvaluateDTO> p=new PageData<CourseEvaluateDTO>();
//        p.setData((List) map.get("data"));
//        p.setPage((PageDomain)map.get("page"));

        PageData<CourseEvaluateDTO> p=new PageData<CourseEvaluateDTO>();
        p.setData(new ArrayList<>());
        p.getPage().setPageNumber(pageNumber);
        p.getPage().setPageSize(pageSize);
        p.getPage().setTotalPages(0);
        p.getPage().setTotalElements(0L);
        return p;
    }

    public PageData<CourseEvaluateDetailDTO> getCourseEvaluateDetail(long orgId, String semesterId,String teacherYear, String courseCode, String name,Integer pageNumber,Integer pageSize) {
        RowMapper<CourseEvaluateDetailDTO> rowMapper=new RowMapper<CourseEvaluateDetailDTO>() {
            @Override
            public CourseEvaluateDetailDTO mapRow(ResultSet rs, int i) throws SQLException {
                CourseEvaluateDetailDTO courseEvaluateDetailDTO=new CourseEvaluateDetailDTO();
                courseEvaluateDetailDTO.setTeachingClassName(rs.getString("TEACHING_CLASS_NAME"));
                courseEvaluateDetailDTO.setTeacherName(rs.getString("CHARGE_PERSON"));
                courseEvaluateDetailDTO.setAvgScore(rs.getFloat("AVG_SCORE"));
                return courseEvaluateDetailDTO;
            }
        };
        String querySql = "SELECT DISTINCT TEACHING_CLASS_NAME,CHARGE_PERSON,AVG_SCORE  FROM `T_COURSE_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + " and ORG_ID=" + orgId + " ";
        String countSql = "SELECT count(DISTINCT TEACHING_CLASS_NAME,CHARGE_PERSON,AVG_SCORE) FROM `T_COURSE_EVALUATE` where DELETE_FLAG =" + DataValidity.VALID.getState() + "  and ORG_ID=" + orgId + " ";
        if (!StringUtils.isEmpty(semesterId)) {
            querySql += " and SEMESTER=" + semesterId + " ";
            countSql += " and SEMESTER=" + semesterId + " ";
        }
        if (!StringUtils.isEmpty(teacherYear)) {
            querySql += " and TEACHER_YEAR=" + teacherYear + " ";
            countSql += " and TEACHER_YEAR=" + teacherYear + " ";
        }
        if (!StringUtils.isEmpty(courseCode)) {
            querySql += " and COURSE_CODE='" + courseCode + "' ";
            countSql += " and COURSE_CODE='" + courseCode + "' ";
        }
        if (!StringUtils.isEmpty(name)) {
            querySql += " and (TEACHING_CLASS_NAME like '%" + name + "%' or CHARGE_PERSON like '%"+ name + "%') ";
            countSql += " and (TEACHING_CLASS_NAME like '%" + name + "%' or CHARGE_PERSON like '%"+ name + "%') ";
        }

        Map map=pageJdbcUtil
                .getPageInfor(pageSize, pageNumber, rowMapper, null, querySql, countSql);
        PageData<CourseEvaluateDetailDTO> p=new PageData<CourseEvaluateDetailDTO>();
        p.setData((List) map.get("data"));
        p.setPage((PageDomain)map.get("page"));
        return p;
    }
    /**
     * 课程评价数据
     * @param statisticsList
     */
    public void saveList(List<CourseEvaluate> statisticsList){
        courseEvaluateRespository.save(statisticsList);
    }
    /**
     * 保存评价数据
     * @param courseEvaluate
     */
    public void save(CourseEvaluate courseEvaluate){
        courseEvaluateRespository.save(courseEvaluate);
    }
    /**
     * 保存评价数据
     * @param courseEvaluate
     */
    public void update(CourseEvaluate courseEvaluate){
        courseEvaluateRespository.save(courseEvaluate);
    }
    public void deleteAllByOrgId(Long orgId){
        courseEvaluateRespository.deleteByOrgId(orgId);
    }
}
