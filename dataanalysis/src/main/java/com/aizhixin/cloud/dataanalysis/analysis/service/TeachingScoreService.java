package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.constant.TeachingScoreTrendType;
import com.aizhixin.cloud.dataanalysis.analysis.dto.*;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreDetails;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.TeachingScoreDetailsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.respository.TeachingScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.ScoreFluctuateCount;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.TotalScoreCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    final static private Logger LOG = LoggerFactory.getLogger(TeachingScoreService.class);
    @Autowired
    private EntityManager em;
    @Autowired
    private TeachingScoreStatisticsRespository teachingScoreStatisticsRespository;
    @Autowired
    private TeachingScoreDetailsRespository teachingScoreDetailsRespository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveStatistics(TeachingScoreStatistics teachingScoreStatistics) {
        teachingScoreStatisticsRespository.save(teachingScoreStatistics);
    }

    public void saveDetails(TeachingScoreDetails teachingScoreDetails) {
        teachingScoreDetailsRespository.save(teachingScoreDetails);
    }

    public Map<String, Object> getStatistic(Long orgId, String grade, Integer semester) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        TeachingAchievementDTO teachingAchievementDTO = new TeachingAchievementDTO();
        List<CollegeTeachingAchievementDTO> collegeTeachingAchievementDTOList = new ArrayList<>();
        try {
            StringBuilder cql = new StringBuilder("SELECT STUDENT_NUM,AVG_GPA,FAIL_PASS_STU_NUM,CURRICULUM_NUM,AVG_SCORE, max(CREATED_DATE) FROM T_TEACHING_SCORE_STATISTICS  WHERE 1 = 1");
            StringBuilder sql = new StringBuilder("SELECT COLLEGE_NAME,COLLEGE_ID,STUDENT_NUM,AVG_GPA,FAIL_PASS_STU_NUM,CURRICULUM_NUM,AVG_SCORE FROM T_TEACHING_SCORE_STATISTICS  WHERE 1 = 1");
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
            cql.append(" and STATISTICS_TYPE = 1 and DELETE_FLAG = 0");
            sql.append(" and STATISTICS_TYPE = 2 and DELETE_FLAG = 0");
            Query cq = em.createNativeQuery(cql.toString());
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                cq.setParameter(e.getKey(), e.getValue());
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res && res.size() > 0) {
                for (Object obj : res) {
                    Object[] d = (Object[]) obj;
                    CollegeTeachingAchievementDTO collegeTeachingAchievementDTO = new CollegeTeachingAchievementDTO();
                    if (null != d[0]) {
                        collegeTeachingAchievementDTO.setCollegeName(String.valueOf(d[0]));
                    }
                    if (null != d[1]) {
                        collegeTeachingAchievementDTO.setCollegeId(Long.valueOf(String.valueOf(d[1])));
                    }
                    if (null != d[2]) {
                        collegeTeachingAchievementDTO.setStudentsNum(Integer.valueOf(String.valueOf(d[2])));
                    }
                    if (null != d[3]) {
                        collegeTeachingAchievementDTO.setAverageGPA(new BigDecimal((double)Float.valueOf(String.valueOf(d[3]))).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    }
                    if (null != d[4]) {
                        collegeTeachingAchievementDTO.setFailNum(Integer.valueOf(String.valueOf(d[4])));
                    }
                    if (null != d[5]) {
                        collegeTeachingAchievementDTO.setCoursesNum(Integer.valueOf(String.valueOf(d[5])));
                    }
                    if (null != d[6]) {
                        collegeTeachingAchievementDTO.setCoursesAVGScore(new BigDecimal((double)Float.valueOf(String.valueOf(d[6]))).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    }
                    collegeTeachingAchievementDTOList.add(collegeTeachingAchievementDTO);
                }
            }
            List<Object> rc = cq.getResultList();
            if (null != rc && rc.size() > 0) {
                Date time = new Date();
                Object[] rd = (Object[]) rc.get(0);
                if (null != rd[0]) {
                    teachingAchievementDTO.setAverageGPA(Integer.valueOf(String.valueOf(rd[0])));
                }
                if (null != rd[1]) {
                    teachingAchievementDTO.setAverageGPA(Double.valueOf(String.valueOf(rd[1])));
                }
                if (null != rd[2]) {
                    teachingAchievementDTO.setFailNum(Integer.valueOf(String.valueOf(rd[2])));
                }
                if (null != rd[3]) {
                    teachingAchievementDTO.setCoursesNum(Integer.valueOf(String.valueOf(rd[3])));
                }
                if (null != rd[4]) {
                    teachingAchievementDTO.setCoursesAVGScore(Double.valueOf(String.valueOf(rd[4])));
                }
                if (null != rd[5]) {
                    time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(rd[5]));
                }
                teachingAchievementDTO.setStatisticalTime(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取教学成绩统计信息失败！");
            return result;
        }
        result.put("success", true);
        result.put("collegeTeachingAchievementDTOList", collegeTeachingAchievementDTOList);
        result.put("teachingAchievementDTO", teachingAchievementDTO);
        return result;
    }


    public Map<String, Object> getTeachingScoreTrendAnalysis(Long orgId, Long collegeId, Integer type) {
        Map<String, Object> result = new HashMap<>();
        List<TrendDTO> trendDTOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            String trend = "";
            if(null!=type) {
                if (type == 1 || type == 3) {
                    trend = " SUM(" + TeachingScoreTrendType.getType(type) + ")";
                } else {
                    trend = " AVG(" + TeachingScoreTrendType.getType(type) + ")";
                }
            }
            StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR," + trend + " FROM T_TEACHING_SCORE_STATISTICS  WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" and ORG_ID =:orgId ");
                condition.put("orgId", orgId);
            }
            if(null!=collegeId){
                sql.append(" and COLLEGE_ID =:collegeId ");
                condition.put("collegeId",collegeId);
            }
            sql.append(" and DELETE_FLAG = 0 GROUP BY TEACHER_YEAR");
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res && res.size() > 0) {
                for (Object obj : res) {
                    Object[] d = (Object[]) obj;
                    TrendDTO trendDTO = new TrendDTO();
                    if (null != d[0]) {
                        trendDTO.setYear(String.valueOf(d[0]));
                    }
                    if (null != d[1]) {
                        trendDTO.setValue(String.valueOf(d[1]));
                    }
                    trendDTOList.add(trendDTO);
                }
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取统计分析数据失败！");
            return result;
        }
        result.put("success", true);
        result.put("data", trendDTOList);
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
                if (collegeIds.indexOf(",") != -1) {
                    String[] cs = grade.split(",");
                    for (String d : cs) {
                        cc.add(d);
                    }
                } else {
                    cc.add(collegeIds);
                }
                cql.append(" and COLLEGE_ID IN :collegeIds");
                sql.append(" and COLLEGE_ID IN :collegeIds");
                condition.put("collegeIds", cc);
            }
            if (null != grade) {
                List<String> tds = new ArrayList<>();
                if (grade.indexOf(",") != -1) {
                    String[] grades = grade.split(",");
                    for (String d : grades) {
                        tds.add(d);
                    }
                } else {
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
            cql.append(" and DELETE_FLAG = 0");
            sql.append(" and DELETE_FLAG = 0");
            Query cq = em.createNativeQuery(cql.toString());
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                cq.setParameter(e.getKey(), e.getValue());
                sq.setParameter(e.getKey(), e.getValue());
            }
            total = Long.valueOf(String.valueOf(cq.getSingleResult()));
            if (null != total && total > 0) {
                sq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                sq.setMaxResults(pageable.getPageSize());
            }
            List<Object> res = sq.getResultList();
            if (null != res && res.size() > 0) {
                for (Object obj : res) {
                    Object[] d = (Object[]) obj;
                    TeachingScoreDetails teachingScoreDetails = new TeachingScoreDetails();
                    if (null != d[0]) {
                        teachingScoreDetails.setJobNum(String.valueOf(d[0]));
                    }
                    if (null != d[1]) {
                        teachingScoreDetails.setUserName(String.valueOf(d[1]));
                    }
                    if (null != d[2]) {
                        teachingScoreDetails.setClassName(String.valueOf(d[2]));
                    }
                    if (null != d[3]) {
                        teachingScoreDetails.setGrade(Integer.valueOf(String.valueOf(d[3])));
                    }
                    if (null != d[4]) {
                        teachingScoreDetails.setCollegeName(String.valueOf(d[4]));
                    }
                    if (null != d[5]) {
                        teachingScoreDetails.setAvgGPA(Double.valueOf(String.valueOf(d[5])));
                    }
                    if (null != d[6]) {
                        teachingScoreDetails.setReferenceSubjects(Integer.valueOf(String.valueOf(d[6])));
                    }
                    if (null != d[7]) {
                        teachingScoreDetails.setFailedSubjects(Integer.valueOf(String.valueOf(d[6])));
                    }
                    if (null != d[8]) {
                        teachingScoreDetails.setFailingGradeCredits(Double.valueOf(String.valueOf(d[8])));
                    }
                    teachingScoreDetailsList.add(teachingScoreDetails);
                }
            }

            p.setData(teachingScoreDetailsList);
            p.getPage().setTotalElements(total);
            p.getPage().setPageNumber(pageable.getPageNumber());
            p.getPage().setPageSize(pageable.getPageSize());
            p.getPage().setTotalPages(PageUtil.cacalatePagesize(total, p.getPage().getPageSize()));

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取教学成绩详情数据异常！");
        }
        result.put("success", true);
        result.put("data", p);
        return result;
    }

    @Transactional
    public Map<String, Object> addTeachingScoreDetail(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        try {
//            teachingScoreDetailsRespository.deleteByOrgId(orgId);
            List<TeachingScoreDetails> teachingScoreDetailsList = new ArrayList<>();
            org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();
            //条件
            Criteria criteria = Criteria.where("orgId").is(orgId);
            query.addCriteria(criteria).limit(10000);
            List<TotalScoreCount> items = mongoTemplate.find(query, TotalScoreCount.class);
            if (null != items && items.size() > 0) {
                for (TotalScoreCount sfc : items) {
                    TeachingScoreDetails tsd = new TeachingScoreDetails();
                    tsd.setOrgId(orgId);
                    tsd.setJobNum(sfc.getJobNum());
                    tsd.setUserId(sfc.getUserId());
                    tsd.setUserName(sfc.getUserName());
                    tsd.setClassId(sfc.getClassId());
                    tsd.setClassName(sfc.getClassName());
                    tsd.setProfessionalId(sfc.getProfessionalId());
                    tsd.setProfessionalName(sfc.getProfessionalName());
                    tsd.setCollegeId(sfc.getCollegeId());
                    tsd.setCollegeName(sfc.getCollegeName());
                    tsd.setTeacherYear(sfc.getSchoolYear());
                    tsd.setSemester(sfc.getSemester());
                    if (null != sfc.getGrade()) {
                        tsd.setGrade(Integer.valueOf(sfc.getGrade()));
                    }
                    tsd.setAvgGPA(Double.valueOf(new Random().nextDouble() * 100));
                    tsd.setReferenceSubjects(new Random().nextInt(100));
                    tsd.setFailedSubjects(tsd.getReferenceSubjects() - 1 > 0 ? tsd.getReferenceSubjects() - 1 : 0);
                    tsd.setFailedSubjects(tsd.getFailedSubjects() * 3);
                    teachingScoreDetailsList.add(tsd);
                }
                teachingScoreDetailsRespository.save(teachingScoreDetailsList);
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加教学成绩详情数据异常！");
            return result;
        }
        result.put("success", true);
        result.put("message", "添加教学成绩详情数据成功！");
        return result;
    }

    public Map<String, Object> addTeachingScoreStatistics(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        List<TeachingScoreStatistics> teachingScoreStatisticsList = new ArrayList<>();
        try {
            Map<String, Object> condition = new HashMap<>();
            StringBuilder sql = new StringBuilder("SELECT COLLEGE_ID,COLLEGE_NAME,TEACHER_YEAR,SEMESTER,count(1),sum(if(FAILED_SUBJECTS>0,1,0)), max(REFERENCE_SUBJECTS),GRADE FROM T_TEACHING_SCORE_DETAILS  WHERE 1 = 1");
            StringBuilder oql = new StringBuilder("SELECT TEACHER_YEAR,SEMESTER,count(1),AVG(AVERAGE_GPA),sum(if(FAILED_SUBJECTS>0,1,0)),MAX(REFERENCE_SUBJECTS) FROM T_TEACHING_SCORE_DETAILS  WHERE 1 = 1");
            if (null != orgId) {
                oql.append(" and ORG_ID = :orgId");
                sql.append(" and ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            oql.append(" and DELETE_FLAG = 0");
            sql.append(" and DELETE_FLAG = 0");
            oql.append(" GROUP BY TEACHER_YEAR, SEMESTER");
            sql.append(" GROUP BY COLLEGE_ID, TEACHER_YEAR, SEMESTER");
            Query oq = em.createNativeQuery(oql.toString());
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                oq.setParameter(e.getKey(), e.getValue());
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            Object rul = oq.getSingleResult();
            if (null != res && res.size() > 0) {
                for (Object obj : res) {
                    Object[] d = (Object[]) obj;
                    TeachingScoreStatistics teachingScoreStatistics = new TeachingScoreStatistics();
                    if(null!=d[0]){
                        teachingScoreStatistics.setCollegeId(Long.valueOf(String.valueOf(d[0])));
                    }
                    if(null!=d[1]){
                        teachingScoreStatistics.setCollegeName(String.valueOf(d[1]));
                    }
                    if (null != d[2]) {
                        teachingScoreStatistics.setTeacherYear(Integer.valueOf(String.valueOf(d[2])));
                        teachingScoreStatistics.setGrade(Integer.valueOf(String.valueOf(d[2])));

                    }
                    if (null != d[3]) {
                        teachingScoreStatistics.setSemester(Integer.valueOf(String.valueOf(d[3])));
                    }
                    if (null != d[4]) {
                        teachingScoreStatistics.setStudentNum(Integer.valueOf(String.valueOf(d[4])));
                    }
                    if (null != d[5]) {
                        teachingScoreStatistics.setFailPassStuNum(Integer.valueOf(String.valueOf(d[5])));
                    }
                    if (null != d[6]) {
                        teachingScoreStatistics.setCurriculumNum(Integer.valueOf(String.valueOf(d[6])));
                    }
                    if (null != d[7]) {
                        teachingScoreStatistics.setGrade(Integer.valueOf(String.valueOf(d[7])));
                    }
                    teachingScoreStatistics.setAvgGPA(new Random().nextDouble() * 10);
                    teachingScoreStatistics.setAvgScore(new Random().nextDouble() * 100);
                    teachingScoreStatistics.setStatisticsType(2);
                    teachingScoreStatistics.setOrgId(orgId);
                    teachingScoreStatisticsList.add(teachingScoreStatistics);
                }
            }
            if (null != rul) {
                Object[] rd = (Object[]) rul;
                TeachingScoreStatistics otss = new TeachingScoreStatistics();
                if (null != rd[0]) {
                    otss.setTeacherYear(Integer.valueOf(String.valueOf(rd[0])));
                    otss.setGrade(Integer.valueOf(String.valueOf(rd[0])));
                }
                if (null != rd[1]) {
                    otss.setSemester(Integer.valueOf(String.valueOf(rd[1])));
                }
                if (null != rd[2]) {
                    otss.setStudentNum(Integer.valueOf(String.valueOf(rd[2])));
                }
                if (null != rd[3]) {
                    otss.setAvgGPA(Double.valueOf(String.valueOf(rd[3])));
                }
                if (null != rd[4]) {
                    otss.setFailPassStuNum(Integer.valueOf(String.valueOf(rd[4])));
                }
                if (null != rd[5]) {
                    otss.setCurriculumNum(Integer.valueOf(String.valueOf(rd[5])));
                }
                otss.setAvgScore(new Random().nextDouble() * 100);
                otss.setStatisticsType(1);
                otss.setOrgId(orgId);
                teachingScoreStatisticsList.add(otss);
            }
            if (null != teachingScoreStatisticsList && teachingScoreStatisticsList.size() > 0) {
                teachingScoreStatisticsRespository.save(teachingScoreStatisticsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "添加教学成绩统计数据异常！");
            LOG.info("添加教学成绩统计数据异常");
            return result;
        }
        result.put("success", true);
        result.put("message", "添加教学成绩统计数据成功！");
        LOG.info("添加教学成绩统计数据成功");
        return result;
    }

    public Map<String, Object> modifyTeachingScoreDetail(String id,Integer teacherYear,Integer semester,Integer grade,
                                                         Double averageGPA,Integer referenceSubjects,Integer failedSubjects,Double failingGradeCredits){
        Map<String, Object> result = new HashMap<>();
        try {
            TeachingScoreDetails teachingScoreDetails = teachingScoreDetailsRespository.findOne(id);
            if(null!=teachingScoreDetails){
                if(null!=teacherYear){
                    teachingScoreDetails.setTeacherYear(teacherYear);
                }
                if(null!=semester){
                    teachingScoreDetails.setSemester(semester);
                }
                if(null!=grade){
                    teachingScoreDetails.setGrade(grade);
                }
                if(null!=averageGPA){
                    teachingScoreDetails.setAvgGPA(averageGPA);
                }
                if(null!=referenceSubjects){
                    teachingScoreDetails.setReferenceSubjects(referenceSubjects);
                }
                if(null!=failedSubjects){
                    teachingScoreDetails.setFailedSubjects(failedSubjects);
                }
                if(null!=failingGradeCredits){
                    teachingScoreDetails.setFailingGradeCredits(failingGradeCredits);
                }
                teachingScoreDetailsRespository.save(teachingScoreDetails);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "修改教学成绩详情数据异常！");
            return result;
        }
        result.put("success", true);
        result.put("message", "修改教学成绩详情数据异常！");
        return result;
    }

}
