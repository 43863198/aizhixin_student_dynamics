package com.aizhixin.cloud.dataanalysis.score.job;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherYearSemesterDTO;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolYearTermService;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.FailScoreStatistics;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.FirstTwoSemestersScoreStatistics;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.LastSemesterScoreStatistics;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.FailScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.FirstTwoSemestersScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.LastSemesterScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.setup.entity.RuleParameter;
import com.aizhixin.cloud.dataanalysis.setup.service.RuleParameterService;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class ScoreJob {

    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private RuleParameterService ruleParameterService;
    @Autowired
    private FirstTwoSemestersScoreStatisticsRespository firstTwoSemestersScoreStatisticsRespository;
    @Autowired
    private FailScoreStatisticsRespository failScoreStatisticsRespository;
    @Autowired
    private LastSemesterScoreStatisticsRespository lastSemesterScoreStatisticsRespository;
    @Autowired
    private EntityManager em;
    @Autowired
    private SchoolYearTermService schoolYearTermService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 统计相邻学期绩点保存到mongo中
     */
    @Async
    public void firstTwoSemestersScoreStatisticsJob(Long orgId, String teachYear, String semester) {
        try {
            // 上学年学期
            String secondSchoolYear = teachYear;
            String secondSemester = "春";
            if (semester.equals("春")) {
                secondSemester = "秋";
                secondSchoolYear = Integer.valueOf(teachYear) - 1 + "";
            }
            // 上上学年学期
            String firstSchoolYear = secondSchoolYear;
            String firstSemester = "春";
            if (secondSemester.equals("春")) {
                firstSemester = "秋";
                firstSchoolYear = Integer.valueOf(secondSchoolYear) - 1 + "";
            }

            Date start3 = null;
            Date end3 = null;
            if (!org.apache.commons.lang.StringUtils.isBlank(teachYear) && !org.apache.commons.lang.StringUtils.isBlank(semester)) {
                Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teachYear, semester);
                if (null != schoolCalendar) {
                    if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                        List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                        if (tysList.size() > 0) {
                            start3 = sdf.parse(tysList.get(0).getStartTime());
                            end3 = sdf.parse(tysList.get(0).getEndTime());
                        }
                    }
                }
            }

            Date start2 = null;
            Date end2 = null;
            if (!org.apache.commons.lang.StringUtils.isBlank(secondSchoolYear) && !org.apache.commons.lang.StringUtils.isBlank(secondSemester)) {
                Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, secondSchoolYear, secondSemester);
                if (null != schoolCalendar) {
                    if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                        List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                        if (tysList.size() > 0) {
                            start2 = sdf.parse(tysList.get(0).getStartTime());
                            end2 = sdf.parse(tysList.get(0).getEndTime());
                        }
                    }
                }
            }
            Date start1 = null;
            Date end1 = null;
            if (!org.apache.commons.lang.StringUtils.isBlank(firstSchoolYear) && !org.apache.commons.lang.StringUtils.isBlank(firstSchoolYear)) {
                Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, firstSchoolYear, firstSemester);
                if (null != schoolCalendar) {
                    if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                        List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                        if (tysList.size() > 0) {
                            start1 = sdf.parse(tysList.get(0).getStartTime());
                            end1 = sdf.parse(tysList.get(0).getEndTime());
                        }
                    }
                }
            }
            // 清除之前统计数据
            List<FirstTwoSemestersScoreStatistics> scoreFluctuateList = firstTwoSemestersScoreStatisticsRespository
                    .findAllByOrgIdAndTeachYearAndSemester(orgId, teachYear, semester);
            firstTwoSemestersScoreStatisticsRespository.delete(scoreFluctuateList);

            if (null != start1 && null != start2 && null != end1 && null != end2 && null != end3) {
                StringBuilder aql = new StringBuilder("SELECT XH as xh, XM as xm, BH as bh, BJMC as bjmc, ZYH as zyh, ZYMC as zymc, YXSH as yxsh, YXSMC as yxsmc FROM t_xsjbxx  WHERE 1 = 1");
                aql.append(" AND RXNY <= :start1");
                aql.append(" AND YBYNY >= :end3");
                Query aq = em.createNativeQuery(aql.toString());
                aq.setParameter("start1", start1);
                aq.setParameter("end3", end3);
                aq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String, Object>> res = aq.getResultList();

                StringBuilder sql = new StringBuilder("SELECT c.COURSE_NUMBER as kch, c.COURSE_NAME as kcmc, cj.XH as xh, MAX(cj.KCCJ) as cj, cj.JD as jd, c.CREDIT as xf FROM t_xscjxx cj" +
                        " LEFT JOIN t_xsjbxx xs ON cj.XH = xs.XH" +
                        " LEFT JOIN t_course c ON cj.KCH = c.COURSE_NUMBER WHERE 1 = 1");

                StringBuilder sql2 = new StringBuilder("");
                sql2.append(sql);
                sql2.append(" AND cj.KSRQ BETWEEN :start2 AND :end2");
                sql2.append(" AND cj.XKSX = '必修'");
                sql2.append(" GROUP BY cj.KCH, cj.XH");
                Query sq2 = em.createNativeQuery(sql2.toString());
                sq2.setParameter("start2", start2);
                sq2.setParameter("end2", end2);
                sq2.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String, Object>> res2 = sq2.getResultList();

                StringBuilder sql1 = new StringBuilder("");
                sql1.append(sql);
                sql1.append(" AND cj.KSRQ BETWEEN :start1 AND :end1");
                sql1.append(" AND cj.XKSX = '必修'");
                sql1.append(" GROUP BY cj.KCH, cj.XH");
                Query sq1 = em.createNativeQuery(sql1.toString());
                sq1.setParameter("start1", start1);
                sq1.setParameter("end1", end1);
                sq1.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String, Object>> res1 = sq1.getResultList();
                List<FirstTwoSemestersScoreStatistics> sfcList = new ArrayList<>();
                for (Map d : res) {
                    FirstTwoSemestersScoreStatistics sfc = new FirstTwoSemestersScoreStatistics();
                    sfc.setOrgId(orgId);
                    if (null != d.get("xh")) {
                        sfc.setJobNum(d.get("xh").toString());
                    }
                    if (null != d.get("xm")) {
                        sfc.setUserName(d.get("xm").toString());
                    }
                    if (null != d.get("bh")) {
                        sfc.setClassCode(d.get("bh").toString());
                    }
                    if (null != d.get("bjmc")) {
                        sfc.setClassName(d.get("bjmc").toString());
                    }
                    if (null != d.get("zyh")) {
                        sfc.setProfessionalCode(d.get("zyh").toString());
                    }
                    if (null != d.get("zymc")) {
                        sfc.setProfessionalName(d.get("zymc").toString());
                    }
                    if (null != d.get("yxsh")) {
                        sfc.setCollegeCode(d.get("yxsh").toString());
                    }
                    if (null != d.get("yxsmc")) {
                        sfc.setCollegeName(d.get("yxsmc").toString());
                    }
                    sfc.setTeachYear(teachYear);
                    sfc.setSemester(semester);
                    sfc.setFirstSchoolYear(firstSchoolYear);
                    sfc.setFirstSemester(firstSemester);
                    sfc.setSecondSchoolYear(secondSchoolYear);
                    sfc.setSecondSemester(secondSemester);
                    int count2 = 0;
                    float totalCJ2 = 0;
                    float totalXFJD2 = 0;
                    float totalXF2 = 0;
//                    StringBuilder source2 = new StringBuilder("");
                    for (Map d2 : res2) {
                        if (null != d2.get("xh") && sfc.getJobNum().equals(d2.get("xh").toString())) {
                            count2++;
//                            if (null != d2.get("kch") && null != d2.get("kcmc") && null != d2.get("xf")) {
//                                source2.append("【KCH:" + d2.get("kch") + ";");
//                                source2.append("KCMC:" + d2.get("kcmc") + ";");
//                                source2.append("XF:" + d2.get("xf") + "】 ");
//                            }
                            if (null != d2.get("cj")) {
                                totalCJ2 = totalCJ2 + Float.valueOf(d2.get("cj").toString());
                            }
                            if (null != d2.get("jd") && null != d2.get("xf")) {
                                totalXFJD2 = totalXFJD2 + Float.valueOf(d2.get("jd").toString()) * Float.valueOf(d2.get("xf").toString());
                                totalXF2 = totalXF2 + Float.valueOf(d2.get("xf").toString());
                            }
                        }
                    }
                    sfc.setSecondTotalScores((float) (Math.round(totalCJ2 * 100) / 100));
                    sfc.setSecondTotalCourseNums(count2);
                    sfc.setSecondTotalGradePoint((float)(Math.round(totalXFJD2*100)/100));
                    if(totalXF2!=0) {
                        sfc.setSecondAvgradePoint((float)(Math.round((totalXFJD2 / totalXF2)*100)/100));
                    }else{
                        sfc.setSecondAvgradePoint(0);
                    }

                    int count1 = 0;
                    float totalCJ1 = 0;
                    float totalXFJD1 = 0;
                    float totalXF1 = 0;
//                    StringBuilder source1 = new StringBuilder("");
                    for (Map d1 : res1) {
                        if (null != d1.get("xh") && sfc.getJobNum().equals(d1.get("xh").toString())) {
                            count1++;
//                            if (null != d1.get("kch") && null != d1.get("kcmc") && null != d1.get("xf")) {
//                                source1.append("【KCH:" + d1.get("kch") + ";");
//                                source1.append("KCMC:" + d1.get("kcmc") + ";");
//                                source1.append("XF:" + d1.get("xf") + "】 ");
//                            }
                            if (null != d1.get("cj")) {
                                totalCJ1 = totalCJ1 + Float.valueOf(d1.get("cj").toString());
                            }
                            if (null != d1.get("jd") && null != d1.get("xf")) {
                                totalXFJD1 = totalXFJD1 + Float.valueOf(d1.get("jd").toString()) * Float.valueOf(d1.get("xf").toString());
                                totalXF1 = totalXF1 + Float.valueOf(d1.get("xf").toString());
                            }
                        }
                    }
                    sfc.setFirstTotalScores((float)(Math.round(totalCJ1*100)/100));
                    sfc.setFirstTotalCourseNums(count1);
                    sfc.setFirstTotalGradePoint((float)(Math.round(totalXFJD1*100)/100));
                    sfc.setFirstAvgradePoint((float)(Math.round((totalXFJD1 / totalXF1)*100)/100));
//                    sfc.setDataSource(source1 + ";" + source2);
                    sfcList.add(sfc);
                }
                firstTwoSemestersScoreStatisticsRespository.save(sfcList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

    /**
     * 统计在校期间累计不及格成绩汇总到mongo里
     */
//    @Async
    public void failScoreStatisticsJob(Long orgId, String teachYear, String semester) {
        try {

            Date start = null;
            Date end = null;
            if (!org.apache.commons.lang.StringUtils.isBlank(teachYear) && !org.apache.commons.lang.StringUtils.isBlank(semester)) {
                Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teachYear, semester);
                if (null != schoolCalendar) {
                    if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                        List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                        if (tysList.size() > 0) {
                            start = sdf.parse(tysList.get(0).getStartTime());
                            end = sdf.parse(tysList.get(0).getEndTime());
                        }
                    }
                }
            }

            // 清除之前总评成绩不及格统计数据
            List<FailScoreStatistics> fsList = failScoreStatisticsRespository.findAllByOrgIdAndTeachYearAndSemester(orgId, teachYear, semester);
            failScoreStatisticsRespository.delete(fsList);

            if (null != start && null != end) {
                StringBuilder aql = new StringBuilder("SELECT XH as xh, XM as xm, BH as bh, BJMC as bjmc, ZYH as zyh, ZYMC as zymc, YXSH as yxsh, YXSMC as yxsmc FROM t_xsjbxx  WHERE 1 = 1");
                aql.append(" AND RXNY <= :start");
                aql.append(" AND YBYNY >= :end");
                Query aq = em.createNativeQuery(aql.toString());
                aq.setParameter("start",start);
                aq.setParameter("end", end);
                aq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String, Object>> res = aq.getResultList();

                StringBuilder sql = new StringBuilder("SELECT r.kch as kch, r.kcmc as kcmc, r.xh as xh, r.cj as cj, r.xf as xf FROM (SELECT c.COURSE_NUMBER as kch, c.COURSE_NAME as kcmc, cj.XH as xh, MAX(cj.KCCJ) as cj, c.CREDIT as xf FROM t_xscjxx cj" +
                        " LEFT JOIN t_course c ON cj.KCH = c.COURSE_NUMBER WHERE 1 = 1");

                StringBuilder sql1 = new StringBuilder("");
                sql1.append(sql);
                sql1.append(" AND cj.KSRQ <= :end");
                sql1.append(" AND cj.XKSX = '必修'");
                sql1.append(" GROUP BY cj.KCH, cj.XH) r");
                sql1.append(" WHERE r.cj < 60");
                Query sq1 = em.createNativeQuery(sql1.toString());
                sq1.setParameter("end", end);
                sq1.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String, Object>> res1 = sq1.getResultList();
                List<FailScoreStatistics> sfcList = new ArrayList<>();
                for (Map d : res) {
                    FailScoreStatistics sfc = new FailScoreStatistics();
                    sfc.setOrgId(orgId);
                    if (null != d.get("xh")) {
                        sfc.setJobNum(d.get("xh").toString());
                    }
                    if (null != d.get("xm")) {
                        sfc.setUserName(d.get("xm").toString());
                    }
                    if (null != d.get("bh")) {
                        sfc.setClassCode(d.get("bh").toString());
                    }
                    if (null != d.get("bjmc")) {
                        sfc.setClassName(d.get("bjmc").toString());
                    }
                    if (null != d.get("zyh")) {
                        sfc.setProfessionalCode(d.get("zyh").toString());
                    }
                    if (null != d.get("zymc")) {
                        sfc.setProfessionalName(d.get("zymc").toString());
                    }
                    if (null != d.get("yxsh")) {
                        sfc.setCollegeCode(d.get("yxsh").toString());
                    }
                    if (null != d.get("yxsmc")) {
                        sfc.setCollegeName(d.get("yxsmc").toString());
                    }
                    sfc.setTeachYear(teachYear);
                    sfc.setSemester(semester);
                    int count = 0;
                    float totalXF = 0;
                    StringBuilder source = new StringBuilder("");
                    for (Map d1 : res1) {
                        if (null != d1.get("xh") && sfc.getJobNum().equals(d1.get("xh").toString())) {
                            if (null != d1.get("cj")) {
                                    count++;
                                    if (null != d1.get("kch") && null != d1.get("kcmc") && null != d1.get("xf")) {
                                        source.append("【KCH:" + d1.get("kch") + ";");
                                        source.append("KCMC:" + d1.get("kcmc") + ";");
                                        source.append("XF:" + d1.get("xf") + "】 ");
                                    }
                                    if (null != d1.get("xf")) {
                                        totalXF = totalXF + Float.valueOf(d1.get("xf").toString());
                                    }
                                    sfc.setFailCourseCredit((float)(Math.round(totalXF*100)/100));
                                    sfc.setFailCourseNum(count);
                                    sfc.setDataSource(source.toString());
                                    sfcList.add(sfc);
                            }
                        }
                    }

                }
                failScoreStatisticsRespository.save(sfcList);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }


    /**
     * 统计上学期成绩汇总到mongo里
     */
    @Async
    public void LastSemesterScoreStatisticsJob(Long orgId, String teachYear, String semester) {
        try {
            // 上学年学期
            String lastSchoolYear = teachYear;
            String lastSemester = "春";
            if (semester == "春") {
                lastSemester = "秋";
                lastSchoolYear = Integer.valueOf(teachYear) - 1 + "";
            }

            Date start = null;
            Date end = null;
            if (!org.apache.commons.lang.StringUtils.isBlank(lastSchoolYear) && !org.apache.commons.lang.StringUtils.isBlank(lastSemester)) {
                Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, lastSchoolYear, lastSemester);
                if (null != schoolCalendar) {
                    if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                        List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                        if (tysList.size() > 0) {
                            start = sdf.parse(tysList.get(0).getStartTime());
                            end = sdf.parse(tysList.get(0).getEndTime());
                        }
                    }
                }
            }

            // 清除之前总评成绩不及格统计数据
            List<LastSemesterScoreStatistics> fsList = lastSemesterScoreStatisticsRespository.findAllByTeachYearAndSemesterAndOrgId(teachYear, semester, orgId);
            lastSemesterScoreStatisticsRespository.delete(fsList);

            if (null != start && null != end) {
                StringBuilder aql = new StringBuilder("SELECT XH as xh, XM as xm, BH as bh, BJMC as bjmc, ZYH as zyh, ZYMC as zymc, YXSH as yxsh, YXSMC as yxsmc, NJ as nj FROM t_xsjbxx  WHERE 1 = 1");
                aql.append(" AND RXNY <= :start");
                aql.append(" AND YBYNY >= :end");
                Query aq = em.createNativeQuery(aql.toString());
                aq.setParameter("start", start);
                aq.setParameter("end", end);
                aq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String, Object>> res = aq.getResultList();

                StringBuilder sql = new StringBuilder("SELECT c.COURSE_NUMBER as kch, c.COURSE_NAME as kcmc, cj.XH as xh, MAX(cj.KCCJ) as cj, c.CREDIT as xf, count(1) as count FROM t_xscjxx cj" +
                        " LEFT JOIN t_course c ON cj.KCH = c.COURSE_NUMBER WHERE 1 = 1");
                StringBuilder sql1 = new StringBuilder("");
                sql1.append(sql);
                sql1.append(" AND cj.KSRQ <= :end");
                sql1.append(" AND cj.XKSX = '必修'");
                sql1.append(" GROUP BY cj.KCH, cj.XH");
                Query sq1 = em.createNativeQuery(sql1.toString());
                sq1.setParameter("end", end);
                sq1.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String, Object>> res1 = sq1.getResultList();

                List<LastSemesterScoreStatistics> sfcList = new ArrayList<>();
                for (Map d : res) {
                    LastSemesterScoreStatistics sfc = new LastSemesterScoreStatistics();
                    sfc.setOrgId(orgId);
                    if (null != d.get("xh")) {
                        sfc.setJobNum(d.get("xh").toString());
                    }
                    if (null != d.get("xm")) {
                        sfc.setUserName(d.get("xm").toString());
                    }
                    if (null != d.get("bh")) {
                        sfc.setClassCode(d.get("bh").toString());
                    }
                    if (null != d.get("bjmc")) {
                        sfc.setClassName(d.get("bjmc").toString());
                    }
                    if (null != d.get("zyh")) {
                        sfc.setProfessionalCode(d.get("zyh").toString());
                    }
                    if (null != d.get("zymc")) {
                        sfc.setProfessionalName(d.get("zymc").toString());
                    }
                    if (null != d.get("yxsh")) {
                        sfc.setCollegeCode(d.get("yxsh").toString());
                    }
                    if (null != d.get("yxsmc")) {
                        sfc.setCollegeName(d.get("yxsmc").toString());
                    }
                    if (null != d.get("nj")) {
                        sfc.setGrade(d.get("nj").toString());
                    }
                    sfc.setTeachYear(teachYear);
                    sfc.setSemester(semester);
                    sfc.setLastSchoolYear(lastSchoolYear);
                    sfc.setLastSemester(lastSemester);
                    int count = 0;
                    int bkcount = 0;
                    float totalXF = 0;
                    Set<String> kchs = new HashSet<>();
                    StringBuilder source = new StringBuilder("");
                    for (Map d1 : res1) {
                        if (null != d1.get("xh") && sfc.getJobNum().equals(d1.get("xh").toString())) {
                            if(null!=d.get("cj")&&Float.valueOf(d.get("cj").toString())<60) {
                                if (null != d1.get("kch") && null != d1.get("kcmc") && null != d1.get("xf")) {
                                    source.append("【KCH:" + d1.get("kch") + ";");
                                    source.append("KCMC:" + d1.get("kcmc") + ";");
                                    source.append("XF:" + d1.get("xf") + "】 ");
                                    if (null != d1.get("cj")) {
                                        count++;
                                        kchs.add(d1.get("kch").toString());
                                    }
                                    if (null != d1.get("xf")) {
                                        totalXF = totalXF + Float.valueOf(d1.get("xf").toString());
                                    }
                                    if (null != d1.get("count") && Integer.valueOf(d1.get("count").toString()) > 1) {
                                        bkcount++;
                                    }
                                }
                            }
                        }
                    }
                    sfc.setRequireCreditCount((float)(Math.round(totalXF*100)/100));
                    sfc.setFailRequiredCourseNum(count);
                    sfc.setMakeUpFailRequiredCourseNum(bkcount);
                    sfc.setDataSource(source.toString());
                    sfc.setScheduleCodeList(kchs);
                    sfcList.add(sfc);
                }
                lastSemesterScoreStatisticsRespository.save(sfcList);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

    /**
     * 相邻两个学期(上上个学期平均学分绩点与上学期平均学分绩点)下降情况-----成绩波动
     */
    public List<WarningInformation> scoreFluctuateJob (Long orgId, String teachYear, String semester, String rpId)
        {
            ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();

            // 上上个学期平均学分绩点与上学期平均学分绩点数据
            List<FirstTwoSemestersScoreStatistics> scoreFluctuateList = firstTwoSemestersScoreStatisticsRespository
                    .findAllByOrgIdAndTeachYearAndSemester(orgId, teachYear, semester);
            RuleParameter ruleParameter = ruleParameterService.findById(rpId);
            if (null != scoreFluctuateList
                    && scoreFluctuateList.size() > 0) {
                for (FirstTwoSemestersScoreStatistics scoreFluctuateCount : scoreFluctuateList) {
                    if (null != ruleParameter) {
                        float result = 0;
                        result = scoreFluctuateCount.getSecondAvgradePoint() - scoreFluctuateCount.getFirstAvgradePoint();
                        // 上学期平均绩点小于上上学期平均绩点时
                        if (result < 0) {
                            result = Math.abs(result);
                            if (result >= Float.parseFloat(String.valueOf(ruleParameter.getRightParameter()))) {
                                WarningInformation alertInfor = new WarningInformation();
                                String alertId = UUID.randomUUID().toString();
                                alertInfor.setId(alertId);
                                alertInfor.setName(scoreFluctuateCount.getUserName());
                                alertInfor.setJobNumber(scoreFluctuateCount.getJobNum());
                                alertInfor.setCollogeCode(scoreFluctuateCount.getCollegeCode());
                                alertInfor.setCollogeName(scoreFluctuateCount.getCollegeName());
                                alertInfor.setClassCode(scoreFluctuateCount.getClassCode());
                                alertInfor.setClassName(scoreFluctuateCount.getClassName());
                                alertInfor.setProfessionalCode(scoreFluctuateCount.getProfessionalCode());
                                alertInfor.setProfessionalName(scoreFluctuateCount.getProfessionalName());
                                alertInfor.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
                                alertInfor.setSemester(scoreFluctuateCount.getSemester());
                                alertInfor.setTeacherYear(scoreFluctuateCount.getTeachYear());
                                alertInfor.setWarningStandard(ruleParameter.getRuledescribe() + ":" + ruleParameter.getRightParameter());
                                alertInfor.setWarningCondition(scoreFluctuateCount.getFirstSchoolYear() + "年" + scoreFluctuateCount.getFirstSemester() + "平均学分绩点为:"
                                        + scoreFluctuateCount.getFirstAvgradePoint() +
                                        ";" + scoreFluctuateCount.getSecondSchoolYear() + "年" + scoreFluctuateCount.getSecondSemester() + "平均学分绩点为:"
                                        + scoreFluctuateCount.getSecondAvgradePoint()
                                        + ",平均学分绩点下降:"
                                        + new BigDecimal(result).setScale(2, RoundingMode.HALF_UP).toString());
                                alertInfor.setWarningTime(new Date());
                                alertInfor.setPhone(scoreFluctuateCount.getUserPhone());
//                                alertInfor.setWarningSource(scoreFluctuateCount.getDataSource());
                                alertInfor.setOrgId(orgId);
                                alertInforList.add(alertInfor);
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }

            return alertInforList;
        }

        /**
         * 上学期必修课不及格课程数
        */
        public List<WarningInformation> failScoreCountJob (Long orgId,String teachYear, String semester, String rpId){

            ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();

            //上学期必修课不及格课程数的数据
            List<LastSemesterScoreStatistics> totalScoreCountList = lastSemesterScoreStatisticsRespository.findAllByTeachYearAndSemesterAndOrgId
                    (teachYear, semester, orgId);
            RuleParameter ruleParameter = ruleParameterService.findById(rpId);
            if (null != totalScoreCountList
                    && totalScoreCountList.size() > 0) {
                for (LastSemesterScoreStatistics totalScoreCount : totalScoreCountList) {
                    if (null != ruleParameter) {
                        if (">=".equals(ruleParameter.getRightRelationship())) {
                            if (totalScoreCount.getFailRequiredCourseNum() >= Float.parseFloat(ruleParameter.getRightParameter())) {
                                WarningInformation alertInfor = new WarningInformation();
                                String alertId = UUID.randomUUID()
                                        .toString();
                                alertInfor.setId(alertId);
                                alertInfor.setName(totalScoreCount
                                        .getUserName());
                                alertInfor.setJobNumber(totalScoreCount
                                        .getJobNum());
                                alertInfor.setCollogeCode(totalScoreCount.getCollegeCode());
                                alertInfor.setCollogeName(totalScoreCount.getCollegeName());
                                alertInfor.setClassCode(totalScoreCount.getClassCode());
                                alertInfor.setClassName(totalScoreCount.getClassName());
                                alertInfor.setProfessionalCode(totalScoreCount.getProfessionalCode());
                                alertInfor.setProfessionalName(totalScoreCount.getProfessionalName());
                                alertInfor.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
                                alertInfor.setWarningTime(new Date());
                                alertInfor.setSemester(semester);
                                alertInfor.setTeacherYear(teachYear);
                                if (null != totalScoreCount.getDataSource() && totalScoreCount.getDataSource().length() > 0) {
                                    alertInfor.setWarningSource(totalScoreCount.getDataSource().substring(0, totalScoreCount.getDataSource().length() - 1));
                                }
                                alertInfor.setWarningStandard(ruleParameter.getRuledescribe() + ":" + ruleParameter.getRightParameter());
                                alertInfor.setWarningCondition(totalScoreCount.getLastSchoolYear() + "年第" + totalScoreCount.getLastSemester() + "必修课不及格课程数:"
                                        + totalScoreCount.getFailRequiredCourseNum());
                                alertInfor.setPhone(totalScoreCount
                                        .getUserPhone());
                                alertInfor.setOrgId(ruleParameter
                                        .getOrgId());
                                alertInforList.add(alertInfor);
                            } else {
                                continue;
                            }
                        }

                    }
                }
            }
            return alertInforList;
        }

    /**
     * 上学期不合格的必修课程（含集中性实践教学环节）学分
     */
    public List<WarningInformation> failScoreCreditJob (Long orgId,String teachYear, String semester, String ruleId){

        ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();

        //上学期必修课不及格课程数的数据
        List<LastSemesterScoreStatistics> totalScoreCountList = lastSemesterScoreStatisticsRespository.findAllByTeachYearAndSemesterAndOrgId
                (teachYear, semester, orgId);
        RuleParameter ruleParameter = ruleParameterService.findById(ruleId);
        if (null != totalScoreCountList
                && totalScoreCountList.size() > 0) {
            for (LastSemesterScoreStatistics totalScoreCount : totalScoreCountList) {
                if (null != ruleParameter) {
                    if (">=".equals(ruleParameter.getRightRelationship())) {
                        if (totalScoreCount.getRequireCreditCount() >= Float.parseFloat(ruleParameter.getRightParameter())) {
                            WarningInformation alertInfor = new WarningInformation();
                            String alertId = UUID.randomUUID()
                                    .toString();
                            alertInfor.setId(alertId);
                            alertInfor.setName(totalScoreCount
                                    .getUserName());
                            alertInfor.setJobNumber(totalScoreCount
                                    .getJobNum());
                            alertInfor.setCollogeCode(totalScoreCount.getCollegeCode());
                            alertInfor.setCollogeName(totalScoreCount.getCollegeName());
                            alertInfor.setClassCode(totalScoreCount.getClassCode());
                            alertInfor.setClassName(totalScoreCount.getClassName());
                            alertInfor.setProfessionalCode(totalScoreCount.getProfessionalCode());
                            alertInfor.setProfessionalName(totalScoreCount.getProfessionalName());
                            alertInfor.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
                            alertInfor.setWarningTime(new Date());
                            alertInfor.setSemester(semester);
                            alertInfor.setTeacherYear(teachYear);
                            if (null != totalScoreCount.getDataSource() && totalScoreCount.getDataSource().length() > 0) {
                                alertInfor.setWarningSource(totalScoreCount.getDataSource().substring(0, totalScoreCount.getDataSource().length() - 1));
                            }
                            alertInfor.setWarningStandard(ruleParameter.getRuledescribe() + ":" + ruleParameter.getRightParameter());
                            alertInfor.setWarningCondition(totalScoreCount.getLastSchoolYear() + "年第" + totalScoreCount.getLastSemester() + "必修课不及格课程数:"
                                    + totalScoreCount.getRequireCreditCount());
                            alertInfor.setPhone(totalScoreCount
                                    .getUserPhone());
                            alertInfor.setOrgId(ruleParameter
                                    .getOrgId());
                            alertInforList.add(alertInfor);
                        } else {
                            continue;
                        }
                    }

                }
            }
        }
        return alertInforList;
    }

       /**
         * 修读异常预警 （AttendAbnormalEarlyWarning）
         */
        public ArrayList<WarningInformation> attendAbnormalJob (Long orgId,String schoolYear, String semester, String rpId){
            ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();



//            int lastSchoolYear = schoolYear;
//            // 上学期编号
//            int lastSemester = 1;
//            if (semester == 1) {
//                lastSemester = 2;
//                lastSchoolYear = schoolYear - 1;
//
//
//                HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
//                Set<String> warnRuleIdList = new HashSet<String>();
//                Set<String> warnSettingsIdList = new HashSet<String>();
//
//                // 定时任务产生的新的预警数据
//                HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
//
//                //删除已生成的预警信息
//                alertWarningInformationService.deleteWarningInformation(orgId, WarningTypeConstant.AttendAbnormal.toString(), schoolYear, semester);
//
//                List<TotalScoreCount> totalScoreCountList = totalScoreCountMongoRespository
//                        .findAllBySchoolYearAndSemesterAndOrgId(lastSchoolYear,
//                                lastSemester, orgId);
//
//                if (null != totalScoreCountList
//                        && totalScoreCountList.size() > 0) {
//                    Date today = new Date();
//                    RuleParameter alarmRule = ruleParameterService.findById(ruleId);
//                    for (TotalScoreCount totalScoreCount : totalScoreCountList) {
//                        if (null != warnMap.get(totalScoreCount
//                                .getJobNum())) {
//                            break;
//                        }
//                        if (null != alarmRule) {
//                            if (StringUtils.isEmpty(totalScoreCount
//                                    .getRequireCreditCount())) {
//                                continue;
//                            }
//                            if (totalScoreCount.getRequireCreditCount() >= Float
//                                    .parseFloat(String.valueOf(alarmRule
//                                            .getRightParameter()))) {
//                                WarningInformation alertInfor = new WarningInformation();
//                                String alertId = UUID.randomUUID()
//                                        .toString();
//                                alertInfor.setId(alertId);
//                                alertInfor.setDefendantId(totalScoreCount
//                                        .getUserId());
//                                alertInfor.setName(totalScoreCount
//                                        .getUserName());
//                                alertInfor.setJobNumber(totalScoreCount
//                                        .getJobNum());
//                                alertInfor.setCollogeId(totalScoreCount
//                                        .getCollegeId());
//                                alertInfor.setCollogeName(totalScoreCount
//                                        .getCollegeName());
//                                alertInfor.setClassId(totalScoreCount
//                                        .getClassId());
//                                alertInfor.setClassName(totalScoreCount
//                                        .getClassName());
//                                alertInfor
//                                        .setProfessionalId(totalScoreCount
//                                                .getProfessionalId());
//                                alertInfor
//                                        .setProfessionalName(totalScoreCount
//                                                .getProfessionalName());
//                                alertInfor.setTeacherYear(totalScoreCount
//                                        .getSchoolYear());
//                                alertInfor
//                                        .setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
//                                alertInfor.setWarningTime(new Date());
//                                alertInfor
//                                        .setWarningCondition(termConversion.getSemester(schoolYear, semester, 1).get("schoolYear") + "年第" + termConversion.getSemester(schoolYear, semester, 1).get("semester") + "学期不及格必修课程学分为："
//                                                + totalScoreCount
//                                                .getRequireCreditCount());
//                                alertInfor.setPhone(totalScoreCount
//                                        .getUserPhone());
//                                alertInfor.setSemester(semester);
//                                alertInfor.setTeacherYear(schoolYear);
//                                alertInfor.setOrgId(alarmRule.getOrgId());
//                                alertInforList.add(alertInfor);
//                                if (null != totalScoreCount.getDataSource() && totalScoreCount.getDataSource().length() > 0) {
//                                    alertInfor.setWarningSource(totalScoreCount.getDataSource().substring(0, totalScoreCount.getDataSource().length() - 1));
//                                }
//
//                                warnMap.put(totalScoreCount
//                                        .getJobNum(), alertInfor);
//                            } else {
//                                continue;
//                            }
//
//                        }
//                    }
//                }
//            }
            return alertInforList;
        }



    /**
     * 补考后上学期总评不及格课程门数
     */
    public ArrayList<WarningInformation> makeUpScoreJob(Long orgId, String teachYear, String semester, String rpId) {

        ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();

        // 补考后上学期总评不及格的学生信息
        List<LastSemesterScoreStatistics> makeUpScoreCountList = lastSemesterScoreStatisticsRespository.findAllByTeachYearAndSemesterAndOrgId(teachYear, semester, orgId);

        RuleParameter ruleParameter = ruleParameterService.findById(rpId);
        if (null != makeUpScoreCountList && makeUpScoreCountList.size() > 0) {
            for (LastSemesterScoreStatistics makeUpScoreCount : makeUpScoreCountList) {
                if (null != ruleParameter) {
                    if (makeUpScoreCount.getMakeUpFailRequiredCourseNum() >= Float
                            .parseFloat(ruleParameter
                                    .getRightParameter())) {
                        WarningInformation alertInfor = new WarningInformation();
                        String alertId = UUID.randomUUID().toString();
                        alertInfor.setId(alertId);
                        alertInfor.setName(makeUpScoreCount.getUserName());
                        alertInfor.setJobNumber(makeUpScoreCount.getJobNum());
                        alertInfor.setCollogeCode(makeUpScoreCount.getCollegeCode());
                        alertInfor.setCollogeName(makeUpScoreCount.getCollegeName());
                        alertInfor.setClassCode(makeUpScoreCount.getClassCode());
                        alertInfor.setClassName(makeUpScoreCount.getClassName());
                        alertInfor.setProfessionalCode(makeUpScoreCount.getProfessionalCode());
                        alertInfor.setProfessionalName(makeUpScoreCount.getProfessionalName());
                        alertInfor.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
                        alertInfor.setWarningStandard(ruleParameter.getRuledescribe() + ":" + ruleParameter.getRightParameter());
                        alertInfor.setWarningCondition("补考后" + makeUpScoreCount.getLastSchoolYear() + "年" + makeUpScoreCount.getLastSemester() + "总评成绩不及格课程数:"
                                        + makeUpScoreCount.getMakeUpFailRequiredCourseNum());
                        alertInfor.setSemester(semester);
                        alertInfor.setTeacherYear(teachYear);
                        alertInfor.setWarningTime(new Date());
                        alertInfor.setPhone(makeUpScoreCount.getUserPhone());
                        alertInfor.setOrgId(ruleParameter.getOrgId());
                        if (null != makeUpScoreCount.getDataSource() && makeUpScoreCount.getDataSource().length() > 0) {
                            alertInfor.setWarningSource(makeUpScoreCount.getDataSource().substring(0, makeUpScoreCount.getDataSource().length() - 1));
                        }
                        alertInforList.add(alertInfor);
                    } else {
                        continue;
                    }
                }
            }
        }
        return alertInforList;
    }

    /**
     * 必修课和专业选修课不及格课程累计学分
     */
    public ArrayList<WarningInformation> dropOutJob(Long orgId, String teachYear, String semester, String rpId) {

        ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();

        List<FailScoreStatistics> makeUpScoreCountList = failScoreStatisticsRespository.findAllByOrgIdAndTeachYearAndSemester(orgId, teachYear, semester);
            RuleParameter ruleParameter = ruleParameterService.findById(rpId);
              if (null != makeUpScoreCountList && makeUpScoreCountList.size() > 0) {
                for (FailScoreStatistics makeUpScoreCount : makeUpScoreCountList) {
                    if (null != ruleParameter) {
                        if (makeUpScoreCount.getFailCourseCredit() >= Float.parseFloat(ruleParameter.getRightParameter())) {
                            WarningInformation alertInfor = new WarningInformation();
                            String alertId = UUID.randomUUID().toString();
                            alertInfor.setId(alertId);
                            alertInfor.setName(makeUpScoreCount.getUserName());
                            alertInfor.setJobNumber(makeUpScoreCount.getJobNum());
                            alertInfor.setCollogeCode(makeUpScoreCount.getCollegeCode());
                            alertInfor.setCollogeName(makeUpScoreCount.getCollegeName());
                            alertInfor.setClassCode(makeUpScoreCount.getClassCode());
                            alertInfor.setClassName(makeUpScoreCount.getClassName());
                            alertInfor.setProfessionalCode(makeUpScoreCount.getProfessionalCode());
                            alertInfor.setProfessionalName(makeUpScoreCount.getProfessionalName());
                            alertInfor.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
                            BigDecimal failCourseCredit = new BigDecimal(makeUpScoreCount.getFailCourseCredit());
                            alertInfor.setWarningStandard(ruleParameter.getRuledescribe() + ":" + ruleParameter.getRightParameter());
                            alertInfor.setWarningCondition("不及格必修课和选修课累计学分:" + failCourseCredit.setScale(2, RoundingMode.HALF_UP).toString());
                            alertInfor.setWarningTime(new Date());
                            alertInfor.setPhone(makeUpScoreCount.getUserPhone());
                            alertInfor.setSemester(semester);
                            alertInfor.setTeacherYear(teachYear);
                            alertInfor.setOrgId(ruleParameter.getOrgId());
                            if (null != makeUpScoreCount.getDataSource() && makeUpScoreCount.getDataSource().length() > 0) {
                                alertInfor.setWarningSource(makeUpScoreCount.getDataSource().substring(0, makeUpScoreCount.getDataSource().length() - 1));
                            }
                            alertInforList.add(alertInfor);
                        } else {
                            continue;
                        }
                    }
                }
        }
        return alertInforList;
    }

    /**
     * 英语四级考试成绩未通过预警
     */
    public ArrayList<WarningInformation> cet4ScoreJob(Long orgId, String teachYear, String semester, String rpId) {

        ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
        try {

            Date start = null;
            Date end = null;
            if (!org.apache.commons.lang.StringUtils.isBlank(teachYear) && !org.apache.commons.lang.StringUtils.isBlank(semester)) {
                Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teachYear, semester);
                if (null != schoolCalendar) {
                    if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                        List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                        if (tysList.size() > 0) {
                            start = sdf.parse(tysList.get(0).getStartTime());
                            end = sdf.parse(tysList.get(0).getEndTime());
                        }
                    }
                }
            }
            int year = Integer.valueOf(teachYear);
            RuleParameter ruleParameter = ruleParameterService.findById(rpId);
            if(null!=ruleParameter) {
                String parm = ruleParameter.getRightParameter();
                if(!parm.isEmpty()){
                   year = year-Integer.valueOf(parm);
                }
            }
            Date start1 = null;
            if (!org.apache.commons.lang.StringUtils.isBlank(year+"") && !org.apache.commons.lang.StringUtils.isBlank(semester)) {
                Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, year + "", semester);
                if (null != schoolCalendar) {
                    if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                        List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                        if (tysList.size() > 0) {
                            start1 = sdf.parse(tysList.get(0).getStartTime());
                        }
                    }
                }
            }

            if (null != start && null != end) {
                StringBuilder aql = new StringBuilder("SELECT XH as xh, XM as xm, BH as bh, BJMC as bjmc, ZYH as zyh, ZYMC as zymc, YXSH as yxsh, YXSMC as yxsmc, NJ as nj FROM t_xsjbxx  WHERE 1 = 1");
                aql.append(" AND RXNY <= :start");
                aql.append(" AND YBYNY >= :end");
                Query aq = em.createNativeQuery(aql.toString());
                aq.setParameter("start",start);
                aq.setParameter("end", end);
                aq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String, Object>> res = aq.getResultList();

                StringBuilder sql = new StringBuilder("SELECT JOB_NUMBER as xh, MAX(SCORE) as score  FROM t_cet_score WHERE score < 425 AND TYPE = '大学英语四级考试'");
                sql.append(" AND EXAMINATION_DATE BETWEEN :start AND ：end");
                sql.append(" GROUP BY JOB_NUMBER");
                Query sq = em.createNativeQuery(sql.toString());
                sq.setParameter("start", start1);
                sq.setParameter("end", end);
                sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String, Object>> res1 = sq.getResultList();
                Set<String> xhSet = new HashSet<>();
                for(Map cd : res1) {
                    if(null!=cd.get("xh")){
                        xhSet.add(cd.get("xh").toString());
                }
                for(Map d : res){
                    if(null!=d.get("xh")&&xhSet.contains(d.get("xh").toString())){
                        WarningInformation alertInfor = new WarningInformation();
                        String alertId = UUID.randomUUID().toString();
                        alertInfor.setId(alertId);
                        if (null != d.get("xh")) {
                            alertInfor.setJobNumber(d.get("xh").toString());
                        }
                        if (null != d.get("xm")) {
                            alertInfor.setName(d.get("xm").toString());
                        }
                        if (null != d.get("bh")) {
                            alertInfor.setClassCode(d.get("bh").toString());
                        }
                        if (null != d.get("bjmc")) {
                            alertInfor.setClassName(d.get("bjmc").toString());
                        }
                        if (null != d.get("zyh")) {
                            alertInfor.setProfessionalCode(d.get("zyh").toString());
                        }
                        if (null != d.get("zymc")) {
                            alertInfor.setProfessionalName(d.get("zymc").toString());
                        }
                        if (null != d.get("yxsh")) {
                            alertInfor.setCollogeCode(d.get("yxsh").toString());
                        }
                        if (null != d.get("yxsmc")) {
                            alertInfor.setCollogeName(d.get("yxsmc").toString());
                        }
                        alertInfor.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
                        alertInfor.setWarningStandard(ruleParameter.getRuledescribe() + ":" + ruleParameter.getRightParameter());
                        alertInfor.setWarningCondition("在校学年已大于等于"+ruleParameter.getRightParameter()
                                + "年仍未通过英语四级考试");
                        alertInfor.setSemester(semester);
                        alertInfor.setTeacherYear(teachYear);
                        alertInfor.setWarningTime(new Date());
                        alertInfor.setOrgId(ruleParameter.getOrgId());
                        alertInforList.add(alertInfor);
                        } else {
                            continue;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return alertInforList;
    }


}
