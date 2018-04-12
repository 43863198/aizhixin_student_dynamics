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
import com.mongodb.BasicDBObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
    
    public List<TeachingScoreDetails> findAllByTeacherYearAndSemesterAndDeleteFlagAndOrgId(int teacherYear,int semester,int deleteFlag,Long orgId){
    	return  teachingScoreDetailsRespository.findAllByTeacherYearAndSemesterAndDeleteFlagAndOrgId( teacherYear, semester, deleteFlag, orgId);
    }

    public void saveStatistics(TeachingScoreStatistics teachingScoreStatistics) {
        teachingScoreStatisticsRespository.save(teachingScoreStatistics);
    }

    public void saveStatisticsList(List<TeachingScoreStatistics> tssList) {
        teachingScoreStatisticsRespository.save(tssList);
    }

    public void saveDetails(TeachingScoreDetails teachingScoreDetails) {
        teachingScoreDetailsRespository.save(teachingScoreDetails);
    }
    public void deleteScoreStatistics(Long orgId,Integer teacherYear, Integer semester) {
        teachingScoreStatisticsRespository.deleteByOrgIdAndTeacherAndSemester(orgId, teacherYear, semester);
    }
    public void deleteScoreStatistics(Long orgId) {
        teachingScoreStatisticsRespository.deleteByOrgId(orgId);
    }

    public void deleteScoreDeatail(Long orgId,Integer teacherYear, Integer semester) {
        teachingScoreDetailsRespository.deleteByOrgIdAndTeacherAndSemester(orgId, teacherYear, semester);
    }

    public void deleteScoreDeatail(Long orgId) {
        teachingScoreDetailsRespository.deleteByOrgId(orgId);
    }


    public void saveDetailsList(List<TeachingScoreDetails> tsdList) {
        teachingScoreDetailsRespository.save(tsdList);
    }

    public Map<String, Object> getStatistic(Long orgId, Integer teacherYear, Integer semester) {
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
            if (null != teacherYear) {
                cql.append(" and TEACHER_YEAR = :teacherYear");
                sql.append(" and TEACHER_YEAR = :teacherYear");
                condition.put("teacherYear", teacherYear);
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
                        collegeTeachingAchievementDTO.setAverageGPA(new BigDecimal(Double.valueOf(String.valueOf(d[3]))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                    if (null != d[4]) {
                        collegeTeachingAchievementDTO.setFailNum(Integer.valueOf(String.valueOf(d[4])));
                    }
                    if (null != d[5]) {
                        collegeTeachingAchievementDTO.setCoursesNum(Integer.valueOf(String.valueOf(d[5])));
                    }
                    if (null != d[6]) {
                        collegeTeachingAchievementDTO.setCoursesAVGScore(new BigDecimal(Double.valueOf(String.valueOf(d[6]))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                    collegeTeachingAchievementDTOList.add(collegeTeachingAchievementDTO);
                }
            }
            List<Object> rc = cq.getResultList();
            if (null != rc && rc.size() > 0) {
                Date time = new Date();
                Object[] rd = (Object[]) rc.get(0);
                if (null != rd[0]) {
                    teachingAchievementDTO.setStudentsNum(Integer.valueOf(String.valueOf(rd[0])));
                }
                if (null != rd[1]) {
                    teachingAchievementDTO.setAverageGPA(new BigDecimal(Double.valueOf(String.valueOf(rd[1]))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                if (null != rd[2]) {
                    teachingAchievementDTO.setFailNum(Integer.valueOf(String.valueOf(rd[2])));
                }
                if (null != rd[3]) {
                    teachingAchievementDTO.setCoursesNum(Integer.valueOf(String.valueOf(rd[3])));
                }
                if (null != rd[4]) {
                    teachingAchievementDTO.setCoursesAVGScore(new BigDecimal(Double.valueOf(String.valueOf(rd[4]))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
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


    public Map<String, Object> getTeachingScoreTrendAnalysis(Long orgId, Long collegeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> dataList = new ArrayList<>();
            Map<String, Object> ckrs = new HashMap<>();
            Map<String, Object> gpa = new HashMap<>();
            Map<String, Object> bjgrs = new HashMap<>();
            Map<String, Object> kcpjf = new HashMap<>();
            ckrs.put("data",getTrendAnalysis(orgId,collegeId,1));
            ckrs.put("name","参考人数");
            gpa.put("data",getTrendAnalysis(orgId,collegeId,2));
            gpa.put("name","平均GPA");
            bjgrs.put("data",getTrendAnalysis(orgId,collegeId,3));
            bjgrs.put("name","不及格人数");
            kcpjf.put("data", getTrendAnalysis(orgId, collegeId, 4));
            kcpjf.put("name", "课程平均分");
            dataList.add(ckrs);
            dataList.add(gpa);
            dataList.add(bjgrs);
            dataList.add(kcpjf);
            result.put("dataList", dataList);
            result.put("success", true);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取统计分析数据失败！");
            return result;
        }
    }


    public List<TrendDTO> getTrendAnalysis (Long orgId, Long collegeId, Integer type){
        List<TrendDTO> trendDTOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        String trend = "";
        if (null != type) {
            if (type == 1 || type == 3) {
                trend = " SUM(" + TeachingScoreTrendType.getType(type) + ")";
            } else {
                trend = " AVG(" + TeachingScoreTrendType.getType(type) + ")";
            }
        }
        StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR," + trend + ",SEMESTER  FROM T_TEACHING_SCORE_STATISTICS  WHERE 1 = 1");
        if (null != orgId) {
            sql.append(" and ORG_ID =:orgId ");
            condition.put("orgId", orgId);
        }
        if (null != collegeId) {
            sql.append(" and COLLEGE_ID =:collegeId ");
            condition.put("collegeId", collegeId);
        } else {
            sql.append(" and STATISTICS_TYPE = 1");
        }
        sql.append(" and DELETE_FLAG = 0 GROUP BY TEACHER_YEAR,SEMESTER");
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
                if (null != d[2]) {
                    trendDTO.setSemester(String.valueOf(d[2]));
                }

                trendDTOList.add(trendDTO);
            }
        }

        if(trendDTOList.size()>1) {
            for (int i=1;i<trendDTOList.size();i++) {
                if (Integer.valueOf(trendDTOList.get(i - 1).getValue()).intValue() != 0) {
                    Double change = (Double.valueOf(trendDTOList.get(i).getValue()) - Double.valueOf(trendDTOList.get(i - 1).getValue())
                    ) / Double.valueOf(trendDTOList.get(i - 1).getValue());
                    trendDTOList.get(i).setChange(Double.valueOf(new DecimalFormat("0.00").format(change)));
                }
            }
        }
        return trendDTOList;
    }





    public Map<String, Object> getTeachingScoreDetail(Integer teacherYear, Integer semester, Long orgId, String collegeIds, String grade, String nj, Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        PageData<TeachingScoreDetails> p = new PageData<>();
        List<TeachingScoreDetails> teachingScoreDetailsList = new ArrayList<>();
        Long total = 0L;
        try {
            Map<String, Object> condition = new HashMap<>();
            StringBuilder cql = new StringBuilder("SELECT count(1) FROM T_TEACHING_SCORE_DETAILS  WHERE 1 = 1");
            StringBuilder sql = new StringBuilder("SELECT JOB_NUM,USER_NAME,CLASS_NAME,GRADE,COLLEGE_NAME,AVERAGE_GPA,REFERENCE_SUBJECTS,FAILED_SUBJECTS,FAILING_GRADE_CREDITS FROM T_TEACHING_SCORE_DETAILS  WHERE 1 = 1");
            if (null != teacherYear) {
                cql.append(" and TEACHER_YEAR = :teacherYear");
                sql.append(" and TEACHER_YEAR = :teacherYear");
                condition.put("teacherYear", teacherYear);
            }
            if (null != semester) {
                cql.append(" and SEMESTER = :semester");
                sql.append(" and SEMESTER = :semester");
                condition.put("semester", semester);
            }
            if (null != orgId) {
                cql.append(" and ORG_ID = :orgId");
                sql.append(" and ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != collegeIds) {
                List<Long> cc = new ArrayList<>();
                if (collegeIds.indexOf(",") != -1) {
                    String[] cs = collegeIds.split(",");
                    for (String d : cs) {
                        cc.add(Long.valueOf(d));
                    }
                } else {
                    cc.add(Long.valueOf(collegeIds));
                }
                cql.append(" and COLLEGE_ID IN :collegeIds");
                sql.append(" and COLLEGE_ID IN :collegeIds");
                condition.put("collegeIds", cc);
            }
            if (null != grade) {
                List<Integer> tds = new ArrayList<>();
                if (grade.indexOf(",") != -1) {
                    String[] grades = grade.split(",");
                    for (String d : grades) {
                        tds.add(Integer.valueOf(d));
                    }
                } else {
                    tds.add(Integer.valueOf(grade));
                }
                cql.append(" and GRADE IN :grades");
                sql.append(" and GRADE IN :grades");
                condition.put("grades", tds);
            }
            if (!org.apache.commons.lang.StringUtils.isBlank(nj)) {
                cql.append(" and (USER_NAME LIKE :nj OR JOB_NUM LIKE :nj)");
                sql.append(" and (USER_NAME LIKE :nj OR JOB_NUM LIKE :nj)");
                condition.put("nj", "%" + nj + "%");
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
                        teachingScoreDetails.setFailedSubjects(Integer.valueOf(String.valueOf(d[7])));
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
