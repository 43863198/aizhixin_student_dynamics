package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.PracticeStaticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.CetScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.PracticeStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.PracticeStaticsRespository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;

import liquibase.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static liquibase.util.StringUtils.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-04
 */
@Component
@Transactional
public class PracticeStatisticsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PracticeStaticsRespository practiceStaticsRespository;
    @Autowired
    private EntityManager em;
    @Autowired
    private PageJdbcUtil pageJdbcUtil;
    
    public void saveList(List<PracticeStatistics> practiceStatisticsList){
    	practiceStaticsRespository.save(practiceStatisticsList);
    }
    
    public void save(PracticeStatistics practiceStatistics){
    	practiceStaticsRespository.save(practiceStatistics);
    }
    
    public PracticeStatistics findById(String id){
    	return practiceStaticsRespository.findOne(id);
    }
    
    
    
    public Map<String,Object> getStatistics(Long orgId){




        return null;
    }

    public Map<String, Object> getStatisticPractice(Long orgId,Long collegeId, String year, Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        PracticeStaticsDTO practiceStaticsDTO = new PracticeStaticsDTO();
        PageData<PracticeStatistics> p = new PageData<>();
        Map<String, Object> condition = new HashMap<>();
        Long count = 0L;
        long practiceStudentNum = 0;
        long practiceCompanyNum = 0;
        long taskNum = 0;
        long taskPassNum = 0;
        Date time = new Date();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(1) AS count, SUM(ss.PRACTICE_STUDENT_NUM) as sum, SUM(ss.PRACTICE_COMPANY_NUM) as rsum, SUM(ss.TASK_NUM) as psum, SUM(ss.TASK_PASS_NUM) as csum, max(ss.STATISTICAL_TIME) FROM T_PRACTICE_STATISTICS ss WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (!isEmpty(year)) {
                sql.append(" and ss.TEACHER_YEAR = :teacherYear");
                condition.put("teacherYear", year);
            }
            if (null != collegeId) {
                sql.append(" and ss.COLLEGE_ID = :collegeId");
                condition.put("collegeId", collegeId);
            }
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());

            }
            Object res = sq.getSingleResult();
            if (null != res) {
                Object[] d = (Object[]) res;
                if (null != d[0]) {
                    count = Long.valueOf(String.valueOf(d[0]));
                }
                if (null != d[1]) {
                    practiceStudentNum = Integer.valueOf(String.valueOf(d[1]));
                }
                if (null != d[2]) {
                    practiceCompanyNum = Integer.valueOf(String.valueOf(d[2]));
                }
                if (null != d[3]) {
                    taskNum = Long.valueOf(String.valueOf(d[3]));
                }
                if (null != d[4]) {
                    taskPassNum = Integer.valueOf(String.valueOf(d[4]));
                }
                if (null != d[5]) {
                    time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(d[5]));
                }
            }
            practiceStaticsDTO.setPracticeStudentNum(practiceStudentNum);
            practiceStaticsDTO.setPracticeCompanyNum(practiceCompanyNum);
            practiceStaticsDTO.setTaskNum(taskNum);
            practiceStaticsDTO.setTaskPassNum(taskPassNum);
            practiceStaticsDTO.setStatisticalTime(time);//后续要改为统计时间
            Page<PracticeStatistics> practiceStatisticsPage = practiceStaticsRespository.findPageDataByOrgIdAndTeacherYear(pageable, orgId, Integer.valueOf(year), 0);
            p.setData(practiceStatisticsPage.getContent());
            p.getPage().setTotalElements(count);
            p.getPage().setPageNumber(pageable.getPageNumber());
            p.getPage().setTotalPages(practiceStatisticsPage.getTotalPages());
            p.getPage().setPageSize(pageable.getPageSize());
            practiceStaticsDTO.setPracticeStatisticsPageData(p);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取学校新生统计信息异常！");
        }
        result.put("success", true);
        result.put("data", practiceStaticsDTO);
        return result;
    }
    public Map<String, Object> getPracticeTrend(Long orgId, Long collegeId){
        Map<String, Object> result = new HashMap<>();
        List res =null;
        Date time = new Date();
        try {
            StringBuilder sql = new StringBuilder("SELECT ss.TASK_NUM as taskNum, ss.TASK_PASS_NUM as taskPassNum,ss.TEACHER_YEAR as teacherYear FROM T_PRACTICE_STATISTICS ss WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" and ss.ORG_ID ="+orgId);

            }
            if(null != collegeId){
                sql.append(" and ss.COLLEGE_ID = :collegeId+"+collegeId);
                sql.append(" and ss.STATISTICS_TYPE = 2");
            }else {
                sql.append(" and ss.STATISTICS_TYPE = 1");
            }
            res = jdbcTemplate.queryForList(sql.toString());

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取实践学情趋势信息异常！");
        }
        result.put("success", true);
        result.put("data", res);
        return result;
    }
    public Map<String,Object> getPracticeDetail(Long orgId,String className, Long collegeId, String year, Integer pageNumber,Integer pageSize){
        RowMapper <Map> rowMapper= new RowMapper<Map>() {
            @Override
            public Map mapRow(ResultSet rs, int i) throws SQLException {
                Map<String,Object> map=new HashMap<>();
                map.put("classId",rs.getString("CLASS_ID"));
                map.put("className",rs.getString("CLASS_NAME"));
                map.put("practiceNum",Integer.valueOf(rs.getString("PRACTICE_STUDENT_NUM")));
                map.put("practiceCompanyNum",Integer.valueOf(rs.getString("PRACTICE_COMPANY_NUM")));
                map.put("taskNum",Integer.valueOf(rs.getString("TASK_NUM")));
                map.put("taskPassNum",Integer.valueOf(rs.getString("TASK_PASS_NUM")));
                return map;
            }
        };
        String querySql = "SELECT CLASS_ID,CLASS_NAME,PRACTICE_STUDENT_NUM,PRACTICE_COMPANY_NUM,TASK_NUM,TASK_PASS_NUM FROM `t_practice_statistics` where DELETE_FLAG ="+ DataValidity.VALID.getState()+"  and ORG_ID="+orgId+" ";
        String countSql = "SELECT count(1) FROM `t_practice_statistics` where DELETE_FLAG ="+DataValidity.VALID.getState()+"  and ORG_ID="+orgId+" ";
        if(null!=collegeId){
            querySql+=" and COLLEGE_ID="+collegeId+" ";
            countSql+=" and COLLEGE_ID="+collegeId+" ";
        }
        if(!StringUtils.isEmpty(className)){
            querySql+=" and CLASS_NAME like '%"+className+"%' ";
            countSql+=" and CLASS_NAME like '%"+className+"%' ";
        }
        if(!isEmpty(year)){
            querySql+=" and TEACHER_YEAR="+year+" ";
            countSql+=" and TEACHER_YEAR="+year+" ";
        }
        return pageJdbcUtil
                .getPageInfor(pageSize, pageNumber,
                        rowMapper, null, querySql, countSql);
       // return null;
    }
}
