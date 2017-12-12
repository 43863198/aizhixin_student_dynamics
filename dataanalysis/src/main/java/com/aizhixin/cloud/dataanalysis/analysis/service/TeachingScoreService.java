package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.constant.TeachingScoreTrendType;
import com.aizhixin.cloud.dataanalysis.analysis.dto.*;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreDetails;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.TeachingScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import org.hibernate.jpa.criteria.ValueHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-11
 */
@Component
@Transactional
public class TeachingScoreService {
    @Autowired
    private EntityManager em;
    @Autowired
    private TeachingScoreStatisticsRespository teachingScoreStatisticsRespository;

    public Map<String,Object> getStatistic(Long orgId, String grade, Integer semester){
        Map<String,Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        TeachingAchievementDTO teachingAchievementDTO = new TeachingAchievementDTO();
        List<CollegeTeachingAchievementDTO> collegeTeachingAchievementDTOList = new ArrayList<>();
        try {
            StringBuilder cql = new StringBuilder("SELECT STUDENT_NUM,AVG_GPA,FAIL_PASS_STU_NUM,CURRICULUM_NUM,AVG_SCORE FROM T_TEACHING_SCORE_STATISTICS  WHERE 1 = 1");
            StringBuilder sql = new StringBuilder("SELECT COLLOEGE_NAME,COLLOEGE_ID,STUDENT_NUM,AVG_GPA,FAIL_PASS_STU_NUM,CURRICULUM_NUM,AVG_SCORE FROM T_TEACHING_SCORE_STATISTICS  WHERE 1 = 1");
            if (null != orgId) {
                cql.append(" and ORG_ID = :orgId");
                sql.append(" and ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != grade) {
                cql.append(" and GRADE = :grade");
                sql.append(" and GRADE = :grade");
                condition.put("grade", grade);
            }
            if (null != semester) {
                cql.append(" and SEMESTER = :semester");
                sql.append(" and SEMESTER = :semester");
                condition.put("semester", semester);
            }
            cql.append(" and STATISTICS_TYPE = 1");
            sql.append(" and STATISTICS_TYPE = 2");
            Query cq = em.createNativeQuery(cql.toString());
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                cq.setParameter(e.getKey(), e.getValue());
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if(null!=res&&res.size()>0){
                for(Object obj : res){
                    Object[] d = (Object[])obj;
                    CollegeTeachingAchievementDTO collegeTeachingAchievementDTO = new CollegeTeachingAchievementDTO();
                    if(null!=d[0]){
                        collegeTeachingAchievementDTO.setCollegeName(String.valueOf(d[0]));
                    }
                    if(null!=d[1]){
                        collegeTeachingAchievementDTO.setCollegeId(Long.valueOf(String.valueOf(d[1])));
                    }
                    if(null!=d[2]){
                        collegeTeachingAchievementDTO.setStudentsNum(Integer.valueOf(String.valueOf(d[2])));
                    }
                    if(null!=d[3]){
                        collegeTeachingAchievementDTO.setAverageGPA(Float.valueOf(String.valueOf(d[3])));
                    }
                    if(null!=d[4]){
                        collegeTeachingAchievementDTO.setFailNum(Integer.valueOf(String.valueOf(d[4])));
                    }
                    if(null!=d[5]){
                       collegeTeachingAchievementDTO.setCoursesNum(Integer.valueOf(String.valueOf(d[5])));
                    }
                    if(null!=d[6]){
                        collegeTeachingAchievementDTO.setCoursesAVGScore(Float.valueOf(String.valueOf(d[6])));
                    }
                    collegeTeachingAchievementDTOList.add(collegeTeachingAchievementDTO);
                }
            }
            List<Object> rc = cq.getResultList();
            if(null!=rc&&rc.size()>0){
                Object[] rd = (Object[]) rc.get(0);
                if(null!=rd[0]){
                    teachingAchievementDTO.setAverageGPA(Integer.valueOf(String.valueOf(rd[0])));
                }
                if(null!=rd[1]){
                    teachingAchievementDTO.setAverageGPA(Integer.valueOf(String.valueOf(rd[1])));
                }
                if(null!=rd[2]){
                    teachingAchievementDTO.setFailNum(Integer.valueOf(String.valueOf(rd[2])));
                }
                if(null!=rd[3]){
                    teachingAchievementDTO.setCoursesNum(Integer.valueOf(String.valueOf(rd[3])));
                }
                if(null!=rd[4]){
                    teachingAchievementDTO.setCoursesAVGScore(Double.valueOf(String.valueOf(rd[4])));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message","获取教学成绩统计信息失败！");
            return result;
        }
        result.put("success",true);
        result.put("collegeTeachingAchievementDTOList",collegeTeachingAchievementDTOList);
        result.put("teachingAchievementDTO",teachingAchievementDTO);
        return result;
    }


    public Map<String,Object> getTeachingScoreTrendAnalysis(Long orgId, Long collegeId, Integer type){
        Map<String,Object> result = new HashMap<>();
        List<TrendDTO> trendDTOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            String trend = "";
            trend = TeachingScoreTrendType.getType(type);
            StringBuilder sql = new StringBuilder("SELECT GRADE,"+trend+" FROM T_TEACHING_SCORE_STATISTICS  WHERE 1 = 1");
            if(null!=orgId){
                sql.append(" and ORG_ID =:orgId ");
                condition.put("orgId",orgId);
            }
            if(null!=collegeId){
                sql.append(" and COLLOEGE_ID =:collegeId ");
                condition.put("collegeId",collegeId);
            }
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if(null!=res&&res.size()>0){
                for(Object obj: res){
                    Object[] d = (Object[])obj;
                    TrendDTO trendDTO = new TrendDTO();
                    if(null!=d[0]){
                         trendDTO.setYear(String.valueOf(d[0]));
                    }
                    if(null!=d[1]){
                        trendDTO.setValue(String.valueOf(d[1]));
                    }
                    trendDTOList.add(trendDTO);
                }
            }
        }catch (Exception e){
            result.put("success",false);
            result.put("message","获取统计分析数据失败！");
            return result;
        }
        result.put("success",true);
        result.put("data",trendDTOList);
        return result;
    }


    public Map<String, Object> getTeachingScoreDetail(Long orgId, String collegeIds, String grade, String nj, Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        PageData<TeachingScoreDetails> p = new PageData<>();
        Long total = 0L;
        try {
            Map<String, Object> condition = new HashMap<>();
            StringBuilder cql = new StringBuilder("SELECT count(1) FROM T_TEACHING_SCORE_DETAILS  WHERE 1 = 1");
            StringBuilder sql = new StringBuilder("SELECT * FROM T_TEACHING_SCORE_DETAILS  WHERE 1 = 1");
            if (null != orgId) {
                cql.append(" and ORG_ID = :orgId");
                sql.append(" and ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != collegeIds) {
                String [] cs = grade.split(",");
                List<String> cc = new ArrayList<>();
                for (String d : cs) {
                    cc.add(d);
                }
                cql.append(" and COLLEGE_ID IN :collegeIds");
                sql.append(" and COLLEGE_ID IN :collegeIds");
                condition.put("collegeIds", cc);
            }
            if (null != grade) {
                String [] grades = grade.split(",");
                List<String> tds = new ArrayList<>();
                for (String d : grades) {
                    tds.add(d);
                }
                cql.append(" and GRADE IN :grades");
                sql.append(" and GRADE IN :grades");
                condition.put("grades", tds);
            }
            if (null != nj) {
                cql.append(" and (USER_NAME = :nj OR JOB_NUM = :nj)");
                sql.append(" and (USER_NAME = :nj OR JOB_NUM = :nj)");
                condition.put("nj", nj);
            }
            cql.append(" and STATISTICS_TYPE = 2");
            sql.append(" and STATISTICS_TYPE = 2");
            Query cq = em.createNativeQuery(cql.toString());
            Query sq = em.createNativeQuery(sql.toString(), TeachingScoreDetails.class);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                cq.setParameter(e.getKey(), e.getValue());
                sq.setParameter(e.getKey(), e.getValue());
            }
           total = Long.valueOf(String.valueOf(cq.getSingleResult()));
            if(null!=total&&total>0){
                sq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                sq.setMaxResults(pageable.getPageSize());
            }
            p.setData(sq.getResultList());
            p.getPage().setTotalElements(total);
            p.getPage().setPageNumber(pageable.getPageNumber());
            p.getPage().setPageSize(pageable.getPageSize());
            p.getPage().setTotalPages(PageUtil.cacalatePagesize(total, p.getPage().getPageSize()));

        }catch (Exception e){
            result.put("success", false);
            result.put("message","获取教学成绩详情数据异常！");
        }
        result.put("success", true);
        result.put("data", p);
        return result;
    }


}
