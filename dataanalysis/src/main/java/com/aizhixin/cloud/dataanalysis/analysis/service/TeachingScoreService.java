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
import java.text.SimpleDateFormat;
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
            StringBuilder cql = new StringBuilder("SELECT STUDENT_NUM,AVG_GPA,FAIL_PASS_STU_NUM,CURRICULUM_NUM,AVG_SCORE, max(CREATED_DATE) FROM T_TEACHING_SCORE_STATISTICS  WHERE 1 = 1");
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
                Date time = new Date();
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
                if(null!=rd[5]){
                    time =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(rd[5]));
                }
                teachingAchievementDTO.setStatisticalTime(time);
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
        List<TeachingScoreDetails> teachingScoreDetailsList = new ArrayList<>();
        Long total = 0L;
        try {
            Map<String, Object> condition = new HashMap<>();
            StringBuilder cql = new StringBuilder("SELECT count(1) FROM T_TEACHING_SCORE_DETAILS  WHERE 1 = 1");
            StringBuilder sql = new StringBuilder("SELECT JOB_NUM,USER_NAME,CLASS_NAME,GRADE,COLLEGE_NAME,AVERAGE_GPA,REFERENCE_SUBJECTS,FAILED_SUBJECTS,FAILING_GRADE_CREDITS FROM T_TEACHING_SCORE_DETAILS  WHERE 1 = 1");
            if (null != orgId) {
                cql.append(" and ORG_ID = :orgId");
                sql.append(" and ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != collegeIds) {
                List<String> cc = new ArrayList<>();
                if(collegeIds.indexOf(",")!=-1) {
                    String[] cs = grade.split(",");
                    for (String d : cs) {
                        cc.add(d);
                    }
                }else {
                    cc.add(collegeIds);
                }
                cql.append(" and COLLEGE_ID IN :collegeIds");
                sql.append(" and COLLEGE_ID IN :collegeIds");
                condition.put("collegeIds", cc);
            }
            if (null != grade) {
                List<String> tds = new ArrayList<>();
                if(grade.indexOf(",")!=-1) {
                    String[] grades = grade.split(",");
                    for (String d : grades) {
                        tds.add(d);
                    }
                }else {
                    tds.add(grade);
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
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                cq.setParameter(e.getKey(), e.getValue());
                sq.setParameter(e.getKey(), e.getValue());
            }
           total = Long.valueOf(String.valueOf(cq.getSingleResult()));
            if(null!=total&&total>0){
                sq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                sq.setMaxResults(pageable.getPageSize());
            }
            List<Object>  res = sq.getResultList();
            if(null!=res&&res.size()>0){
                for(Object obj : res){
                    Object[] d = (Object[])obj;
                    TeachingScoreDetails teachingScoreDetails = new TeachingScoreDetails();
                    if(null!=d[0]){
                        teachingScoreDetails.setJobNum(String.valueOf(d[0]));
                    }
                    if(null!=d[1]){
                        teachingScoreDetails.setUserName(String.valueOf(d[1]));
                    }
                    if(null!=d[2]){
                        teachingScoreDetails.setClassName(String.valueOf(d[2]));
                    }
                    if(null!=d[3]){
                        teachingScoreDetails.setGrade(Integer.valueOf(String.valueOf(d[3])));
                    }
                    if(null!=d[4]){
                        teachingScoreDetails.setCollegeName(String.valueOf(d[4]));
                    }
                    if(null!=d[5]){
                        teachingScoreDetails.setAverageGPA(Float.valueOf(String.valueOf(d[5])));
                    }
                    if(null!=d[6]){
                        teachingScoreDetails.setReferenceSubjects(Integer.valueOf(String.valueOf(d[6])));
                    }
                    if(null!=d[7]){
                        teachingScoreDetails.setFailedSubjects(Integer.valueOf(String.valueOf(d[6])));
                    }
                    if(null!=d[8]){
                        teachingScoreDetails.setFailingGradeCredits(Float.valueOf(String.valueOf(d[8])));
                    }
                    teachingScoreDetailsList.add(teachingScoreDetails);
                }
            }

            p.setData(teachingScoreDetailsList);
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
