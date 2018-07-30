package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.constant.TeachingScoreTrendType;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CollegeTeachingAchievementDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeachingAchievementDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TrendDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreDetails;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.TeachingScoreDetailsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.respository.TeachingScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<TeachingScoreDetails> findAllByTeacherYearAndSemesterAndDeleteFlagAndOrgId(int teacherYear, int semester, int deleteFlag, Long orgId) {
        return teachingScoreDetailsRespository.findAllByTeacherYearAndSemesterAndDeleteFlagAndOrgId(teacherYear, semester, deleteFlag, orgId);
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

    public void deleteScoreStatistics(Long orgId, Integer teacherYear, Integer semester) {
        teachingScoreStatisticsRespository.deleteByOrgIdAndTeacherAndSemester(orgId, teacherYear, semester);
    }

    public void deleteScoreStatistics(Long orgId) {
        teachingScoreStatisticsRespository.deleteByOrgId(orgId);
    }

    public void deleteScoreDeatail(Long orgId, Integer teacherYear, Integer semester) {
        teachingScoreDetailsRespository.deleteByOrgIdAndTeacherAndSemester(orgId, teacherYear, semester);
    }

    public void deleteScoreDeatail(Long orgId) {
        teachingScoreDetailsRespository.deleteByOrgId(orgId);
    }


    public void saveDetailsList(List<TeachingScoreDetails> tsdList) {
        teachingScoreDetailsRespository.save(tsdList);
    }

    public Map<String, Object> getStatistic(Long orgId, String collegeCode, String teacherYear, String semester) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        TeachingAchievementDTO teachingAchievementDTO = new TeachingAchievementDTO();

        teachingAchievementDTO.setCoursesNum(0);
        teachingAchievementDTO.setStudentsNum(0);
        teachingAchievementDTO.setScoreNum(0);
        teachingAchievementDTO.setMustFailNum(0);
        teachingAchievementDTO.setMustNum(0);

        List<CollegeTeachingAchievementDTO> collegeTeachingAchievementDTOList = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("");
            if (null != orgId) {
                sql.append(" and s.XXID = :orgId ");
                condition.put("orgId", orgId);
            }
            if (null != teacherYear) {
                sql.append(" and c.XN = :teacherYear ");
                condition.put("teacherYear", teacherYear);
            }
            if (null != semester) {
                sql.append(" and c.XQM = :semester ");
                condition.put("semester", semester);
            }

            StringBuilder cql = new StringBuilder();
            StringBuilder mustSql = new StringBuilder();
            StringBuilder mustPassSQL = new StringBuilder();
            if (!StringUtils.isBlank(collegeCode)) {
                cql.append("SELECT s.ZYH as code, s.ZYMC as name, COUNT(DISTINCT c.XH) as cks , COUNT(c.XH) as ckc, AVG(c.JD) as pjjd,COUNT(DISTINCT c.KCH) as kcs,AVG(c.KCCJ) as pjcj, MAX(c.KCCJ) as zgcj FROM t_xscjxx c LEFT JOIN t_xsjbxx s ON c.XH = s.XH WHERE ");
                mustSql.append("SELECT s.ZYH as code, COUNT(c.XH) as musts FROM t_xscjxx c LEFT JOIN t_xsjbxx s ON c.XH = s.XH WHERE c.XKSX='必修' ");//必修课参考人次
                mustPassSQL.append("SELECT s.ZYH as code, COUNT(c.XH) as pass FROM t_xscjxx c LEFT JOIN t_xsjbxx s ON c.XH = s.XH WHERE c.KCCJ >= 60 and c.XKSX='必修' ");//必修课通过人次

                cql.append(" s.YXSH = '" + collegeCode + "'");
                mustSql.append(" and s.YXSH = '" + collegeCode + "'");
                mustPassSQL.append(" and s.YXSH = '" + collegeCode + "'");

                cql.append(sql);
                mustSql.append(sql);
                mustPassSQL.append(sql);

                cql.append(" GROUP BY s.ZYH, s.ZYMC");
                mustSql.append(" GROUP BY s.ZYH");
                mustPassSQL.append(" GROUP BY s.ZYH");
            } else {
                cql.append("SELECT s.YXSH as code, s.YXSMC as name, COUNT(DISTINCT c.XH) as cks, COUNT(c.XH) as ckc, AVG(c.JD) as pjjd,COUNT(DISTINCT c.KCH) as kcs,AVG(c.KCCJ) as pjcj, MAX(c.KCCJ) as zgcj FROM t_xscjxx c LEFT JOIN t_xsjbxx s ON c.XH = s.XH WHERE 1=1 ");
                mustSql.append("SELECT s.YXSH as code, COUNT(c.XH) as musts FROM t_xscjxx c LEFT JOIN t_xsjbxx s ON c.XH = s.XH WHERE c.XKSX='必修' ");//必修课参考人次
                mustPassSQL.append("SELECT s.YXSH as code, COUNT(c.XH) as pass FROM t_xscjxx c LEFT JOIN t_xsjbxx s ON c.XH = s.XH WHERE c.KCCJ >= 60 and c.XKSX='必修' ");//必修课通过人次

                cql.append(sql);
                mustSql.append(sql);
                mustPassSQL.append(sql);

                cql.append(" GROUP BY s.YXSH, s.YXSMC");
                mustSql.append(" GROUP BY s.YXSH");
                mustPassSQL.append(" GROUP BY s.YXSH");
            }

            StringBuilder oql = new StringBuilder("SELECT AVG(c.JD) as pjjd,  AVG(c.KCCJ) as pjcj, MAX(c.KCCJ) as zgcj FROM t_xscjxx c LEFT JOIN t_xsjbxx s ON c.XH = s.XH WHERE 1=1 ");
            oql.append(sql);

            Query cq = em.createNativeQuery(cql.toString());
            Query must = em.createNativeQuery(mustSql.toString());
            Query mustPass = em.createNativeQuery(mustPassSQL.toString());
            Query oq = em.createNativeQuery(oql.toString());

            cq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            must.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            mustPass.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            oq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

            for (Map.Entry<String, Object> e : condition.entrySet()) {
                cq.setParameter(e.getKey(), e.getValue());
                must.setParameter(e.getKey(), e.getValue());
                mustPass.setParameter(e.getKey(), e.getValue());
                oq.setParameter(e.getKey(), e.getValue());
            }

            List<Map<String, Object>> rcq = cq.getResultList();
            List<Map<String, Object>> rmust = must.getResultList();
            List<Map<String, Object>> rmustPass = mustPass.getResultList();
            List<Map<String, Object>> roq = oq.getResultList();

            if (null != rcq && rcq.size() > 0) {
                Map<String, CollegeTeachingAchievementDTO> cache = new HashMap<>();
                for (Map<String, Object> q : rcq) {
                    CollegeTeachingAchievementDTO collegeTeachingAchievementDTO = new CollegeTeachingAchievementDTO();
                    if (null != q.get("name")) {
                        collegeTeachingAchievementDTO.setName(q.get("name").toString());
                    }
                    if (null != q.get("code")) {
                        collegeTeachingAchievementDTO.setCode(q.get("code").toString());
                    }
                    if (null != q.get("cks")) {
                        collegeTeachingAchievementDTO.setStudentsNum(Integer.valueOf(q.get("cks").toString()));
                        teachingAchievementDTO.setStudentsNum(teachingAchievementDTO.getStudentsNum() + collegeTeachingAchievementDTO.getStudentsNum());
                    }
                    if (null != q.get("ckc")) {
                        collegeTeachingAchievementDTO.setScoreNum(Integer.valueOf(q.get("ckc").toString()));
                        teachingAchievementDTO.setScoreNum(teachingAchievementDTO.getScoreNum() + collegeTeachingAchievementDTO.getScoreNum());
                    }
                    if (null != q.get("pjjd")) {
                        collegeTeachingAchievementDTO.setAverageGPA(new BigDecimal(Double.valueOf(q.get("pjjd").toString())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                    if (null != q.get("kcs")) {
                        collegeTeachingAchievementDTO.setCoursesNum(Integer.valueOf(q.get("kcs").toString()));
                        teachingAchievementDTO.setCoursesNum(teachingAchievementDTO.getCoursesNum() + collegeTeachingAchievementDTO.getCoursesNum());
                    }
                    if (null != q.get("pjcj")) {
                        collegeTeachingAchievementDTO.setCoursesAVGScore(new BigDecimal(Double.valueOf(q.get("pjcj").toString())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                    cache.put(collegeTeachingAchievementDTO.getCode(), collegeTeachingAchievementDTO);
                    collegeTeachingAchievementDTOList.add(collegeTeachingAchievementDTO);
                }

                for (Map<String, Object> p : rmust) {
                    String code = (String)p.get("code");
                    if (null != code) {
                        CollegeTeachingAchievementDTO collegeTeachingAchievementDTO = cache.get(code);
                        if (null != collegeTeachingAchievementDTO && null != p.get("musts")) {
                            int musts = Integer.valueOf(p.get("musts").toString());
                            collegeTeachingAchievementDTO.setMustNum(musts);
                            teachingAchievementDTO.setMustNum(teachingAchievementDTO.getMustNum() + collegeTeachingAchievementDTO.getMustNum());
                        }
                    }
                }
                for (Map<String, Object> p : rmustPass) {
                    String code = (String)p.get("code");
                    if (null != code) {
                        CollegeTeachingAchievementDTO collegeTeachingAchievementDTO = cache.get(code);
                        if (null != collegeTeachingAchievementDTO && null != p.get("pass")) {
                            int pass = Integer.valueOf(p.get("pass").toString());
                            collegeTeachingAchievementDTO.setMustFailNum(collegeTeachingAchievementDTO.getMustNum() - pass);
                            teachingAchievementDTO.setMustFailNum(teachingAchievementDTO.getMustFailNum() + collegeTeachingAchievementDTO.getMustFailNum());
                        }
                    }
                }
            }
            if (null != roq && roq.size() > 0) {
                Map<String, Object> o = roq.get(0);
                if (null != o.get("pjjd")) {
                    teachingAchievementDTO.setAverageGPA(new BigDecimal(Double.valueOf(o.get("pjjd").toString())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                if (null != o.get("pjcj")) {
                    teachingAchievementDTO.setCoursesAVGScore(new BigDecimal(Double.valueOf(o.get("pjcj").toString())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
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
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取统计分析数据失败！");
            return result;
        }
        result.put("success", true);
        result.put("data", trendDTOList);
        return result;
    }


    public Map<String, Object> getTeachingScoreDetail(Integer teacherYear, Integer semester, Long orgId, String collegeIds, String grade, String nj,  String professionCode, Pageable pageable) {
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

            if(!StringUtils.isBlank(professionCode)){
                cql.append(" and PROFESSIONAL_ID = :professionCode");
                sql.append(" and PROFESSIONAL_ID = :professionCode");
                condition.put("professionCode", professionCode);
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


    public Map<String, Object> modifyTeachingScoreDetail(String id, Integer teacherYear, Integer semester, Integer grade,
                                                         Double averageGPA, Integer referenceSubjects, Integer failedSubjects, Double failingGradeCredits) {
        Map<String, Object> result = new HashMap<>();
        try {
            TeachingScoreDetails teachingScoreDetails = teachingScoreDetailsRespository.findOne(id);
            if (null != teachingScoreDetails) {
                if (null != teacherYear) {
                    teachingScoreDetails.setTeacherYear(teacherYear);
                }
                if (null != semester) {
                    teachingScoreDetails.setSemester(semester);
                }
                if (null != grade) {
                    teachingScoreDetails.setGrade(grade);
                }
                if (null != averageGPA) {
                    teachingScoreDetails.setAvgGPA(averageGPA);
                }
                if (null != referenceSubjects) {
                    teachingScoreDetails.setReferenceSubjects(referenceSubjects);
                }
                if (null != failedSubjects) {
                    teachingScoreDetails.setFailedSubjects(failedSubjects);
                }
                if (null != failingGradeCredits) {
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
