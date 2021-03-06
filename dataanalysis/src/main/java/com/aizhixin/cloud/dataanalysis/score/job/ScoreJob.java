package com.aizhixin.cloud.dataanalysis.score.job;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherYearSemesterDTO;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolYearTermService;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.score.dto.ScoreToMongoDTO;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
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
    private JdbcTemplate jdbcTemplate;
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
//                List<FirstTwoSemestersScoreStatistics> scoreFluctuateList = firstTwoSemestersScoreStatisticsRespository
//                        .findAllByOrgIdAndTeachYearAndSemester(orgId, teachYear, semester);
//                firstTwoSemestersScoreStatisticsRespository.delete(scoreFluctuateList);
                firstTwoSemestersScoreStatisticsRespository.deleteAll();

                if (null != start1 && null != start2 && null != end1 && null != end2 && null != end3) {
                    StringBuilder aql = new StringBuilder("SELECT XH as xh, XM as xm, BH as bh, BJMC as bjmc, ZYH as zyh, ZYMC as zymc, YXSH as yxsh, YXSMC as yxsmc FROM t_xsjbxx  WHERE 1 = 1");
                    aql.append(" AND RXFS NOT IN ('12','14')");
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
                    sql2.append(" AND cj.XN = :xn");
                    sql2.append(" AND cj.XQM = :xqm");
                    sql2.append(" AND cj.XKSX = '必修'");
                    sql2.append(" GROUP BY cj.KCH, cj.XH");
                    Query sq2 = em.createNativeQuery(sql2.toString());
                    sq2.setParameter("xn", secondSchoolYear);
                    String xqm2 = "";
                    if (secondSemester.equals("春")) {
                        xqm2 = "1";
                    } else {
                        xqm2 = "2";
                    }
                    sq2.setParameter("xqm", xqm2);
                    sq2.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                    List<Map<String, Object>> res2 = sq2.getResultList();

                    StringBuilder sql1 = new StringBuilder("");
                    sql1.append(sql);
                    sql1.append(" AND cj.XN = :xn");
                    sql1.append(" AND cj.XQM = :xqm");
                    sql1.append(" AND cj.XKSX = '必修'");
                    sql1.append(" GROUP BY cj.KCH, cj.XH");
                    Query sq1 = em.createNativeQuery(sql1.toString());
                    sq1.setParameter("xn", firstSchoolYear);
                    String xqm1 = "";
                    if (firstSemester.equals("春")) {
                        xqm1 = "1";
                    } else {
                        xqm1 = "2";
                    }
                    sq1.setParameter("xqm", xqm1);
                    sq1.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                    List<Map<String, Object>> res1 = sq1.getResultList();
                    Set<FirstTwoSemestersScoreStatistics> sfcList = new HashSet<>();
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
                        sfc.setSecondTotalScores(totalCJ2);
                        sfc.setSecondTotalCourseNums(count2);
                        sfc.setSecondTotalGradePoint(totalXFJD2);
                        logger.info("学生学号:" + d.get("xh").toString());
                        logger.info("学生姓名:" + d.get("xm").toString());
                        logger.info("学生绩点:" + d.get("jd").toString());
                        logger.info("学生学分:" + d.get("xf").toString());
                        logger.info("上学期总（学分*绩点）:" + totalXFJD2);
                        logger.info("上学期总学分:" + totalXF2);
                        if (totalXF2 != 0) {
                            sfc.setSecondAvgradePoint(totalXFJD2 / totalXF2);
                        } else {
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
                        sfc.setFirstTotalScores(totalCJ1);
                        sfc.setFirstTotalCourseNums(count1);
                        sfc.setFirstTotalGradePoint(totalXFJD1);
                        sfc.setFirstAvgradePoint(totalXFJD1 / totalXF1);
//                    sfc.setDataSource(source1 + ";" + source2);
                        sfcList.add(sfc);
                    }
                    firstTwoSemestersScoreStatisticsRespository.save(sfcList);
                    logger.info("统计相邻学期绩点保存到mongo中完成！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.getMessage());
            }

    }

    /**
     * 统计在校期间累计不及格成绩汇总到mongo里
     */
    @Async
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
//            List<FailScoreStatistics> fsList = failScoreStatisticsRespository.findAllByOrgIdAndTeachYearAndSemester(orgId, teachYear, semester);
//            failScoreStatisticsRespository.delete(fsList);
            failScoreStatisticsRespository.deleteAll();
            Set<FailScoreStatistics> sfcList = new HashSet<>();
            if (null != start && null != end) {
                StringBuilder aql = new StringBuilder("SELECT XH as xh, XM as xm, YDDH as yddh, BH as bh, BJMC as bjmc, ZYH as zyh, ZYMC as zymc, YXSH as yxsh, YXSMC as yxsmc FROM t_xsjbxx  WHERE 1 = 1");
                aql.append(" AND RXFS NOT IN ('12','14')");
                aql.append(" AND RXNY <= :start");
                aql.append(" AND YBYNY >= :end");
                Query aq = em.createNativeQuery(aql.toString());
                aq.setParameter("start", start);
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
                for (Map d : res) {
                    FailScoreStatistics sfc = new FailScoreStatistics();
                    sfc.setOrgId(orgId);
                    if (null != d.get("xh")) {
                        sfc.setJobNum(d.get("xh").toString());
                    }
                    if (null != d.get("xm")) {
                        sfc.setUserName(d.get("xm").toString());
                    }
                    if (null != d.get("yddh")) {
                        sfc.setPhone(d.get("yddh").toString());
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
                        if (null != d1.get("xh") && null!= sfc.getJobNum() && sfc.getJobNum().equals(d1.get("xh").toString())) {
                            if (null != d1.get("cj")) {
                                count++;
                                if (null != d1.get("kch") && null != d1.get("kcmc") && null != d1.get("xf")) {
                                    source.append("【课程号:" + d1.get("kch") + ";");
                                    source.append("课程名称:" + d1.get("kcmc") + ";");
                                    source.append("学分:" + d1.get("xf") + "】 ");
                                }
                                if (null != d1.get("xf")) {
                                    totalXF = totalXF + Float.valueOf(d1.get("xf").toString());
                                }
                            }
                        }
                    }
                    sfc.setFailCourseCredit(totalXF);
                    sfc.setFailCourseNum(count);
                    sfc.setDataSource(source.toString());
                    sfcList.add(sfc);
                }
            }
            failScoreStatisticsRespository.save(sfcList);
            logger.info("统计在校期间累计不及格成绩汇总到mongo里完成！");
        } catch (ParseException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }


    /**
     * 统计上学期成绩汇总到mongo里
     */
//    @Async
//    public void LastSemesterScoreStatisticsJob(Long orgId, String teachYear, String semester) {
//            try {
//                // 上学年学期
//                String lastSchoolYear = teachYear;
//                String lastSemester = "春";
//                if (semester == "春") {
//                    lastSemester = "秋";
//                    lastSchoolYear = Integer.valueOf(teachYear) - 1 + "";
//                }
//
//                Date start = null;
//                Date end = null;
//                if (!org.apache.commons.lang.StringUtils.isBlank(lastSchoolYear) && !org.apache.commons.lang.StringUtils.isBlank(lastSemester)) {
//                    Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, lastSchoolYear, lastSemester);
//                    if (null != schoolCalendar) {
//                        if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
//                            List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
//                            if (tysList.size() > 0) {
//                                start = sdf.parse(tysList.get(0).getStartTime());
//                                end = sdf.parse(tysList.get(0).getEndTime());
//                            }
//                        }
//                    }
//                }
//
//                // 清除之前总评成绩不及格统计数据
////                List<LastSemesterScoreStatistics> fsList = lastSemesterScoreStatisticsRespository.findAllByTeachYearAndSemesterAndOrgId(teachYear, semester, orgId);
////                lastSemesterScoreStatisticsRespository.delete(fsList);
//                lastSemesterScoreStatisticsRespository.deleteAll();
//
//                if (null != start && null != end) {
//                    StringBuilder aql = new StringBuilder("SELECT XH as xh, XM as xm, BH as bh, BJMC as bjmc, ZYH as zyh, ZYMC as zymc, YXSH as yxsh, YXSMC as yxsmc, NJ as nj FROM t_xsjbxx  WHERE 1 = 1");
//                    aql.append(" AND RXFS NOT IN ('12','14')");
//                    aql.append(" AND RXNY <= :start");
//                    aql.append(" AND YBYNY >= :end");
//                    Query aq = em.createNativeQuery(aql.toString());
//                    aq.setParameter("start", start);
//                    aq.setParameter("end", end);
//                    aq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//                    List<Map<String, Object>> res = aq.getResultList();
//
//                    StringBuilder sql = new StringBuilder("SELECT c.COURSE_NUMBER as kch, c.COURSE_NAME as kcmc, cj.XH as xh, MAX(cj.KCCJ) as cj, c.CREDIT as xf, count(1) as count FROM t_xscjxx cj" +
//                            " LEFT JOIN t_course c ON cj.KCH = c.COURSE_NUMBER WHERE 1 = 1");
//                    StringBuilder sql1 = new StringBuilder("");
//                    sql1.append(sql);
//                    sql1.append(" AND cj.KSRQ <= :end");
//                    sql1.append(" AND cj.XKSX = '必修'");
//                    sql1.append(" GROUP BY cj.KCH, cj.XH");
//                    Query sq1 = em.createNativeQuery(sql1.toString());
//                    sq1.setParameter("end", end);
//                    sq1.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//                    List<Map<String, Object>> res1 = sq1.getResultList();
//                    Set<LastSemesterScoreStatistics> sfcList = new HashSet<>();
//                    for (Map d : res) {
//                        LastSemesterScoreStatistics sfc = new LastSemesterScoreStatistics();
//                        sfc.setOrgId(orgId);
//                        if (null != d.get("xh")) {
//                            sfc.setJobNum(d.get("xh").toString());
//                        }
//                        if (null != d.get("xm")) {
//                            sfc.setUserName(d.get("xm").toString());
//                        }
//                        if (null != d.get("bh")) {
//                            sfc.setClassCode(d.get("bh").toString());
//                        }
//                        if (null != d.get("bjmc")) {
//                            sfc.setClassName(d.get("bjmc").toString());
//                        }
//                        if (null != d.get("zyh")) {
//                            sfc.setProfessionalCode(d.get("zyh").toString());
//                        }
//                        if (null != d.get("zymc")) {
//                            sfc.setProfessionalName(d.get("zymc").toString());
//                        }
//                        if (null != d.get("yxsh")) {
//                            sfc.setCollegeCode(d.get("yxsh").toString());
//                        }
//                        if (null != d.get("yxsmc")) {
//                            sfc.setCollegeName(d.get("yxsmc").toString());
//                        }
//                        if (null != d.get("nj")) {
//                            sfc.setGrade(d.get("nj").toString());
//                        }
//                        sfc.setTeachYear(teachYear);
//                        sfc.setSemester(semester);
//                        sfc.setLastSchoolYear(lastSchoolYear);
//                        sfc.setLastSemester(lastSemester);
//                        int count = 0;
//                        int bkcount = 0;
//                        float totalXF = 0;
//                        Set<String> kchs = new HashSet<>();
//                        StringBuilder source = new StringBuilder("");
//                        for (Map d1 : res1) {
//                            if (null != d1.get("xh") && sfc.getJobNum().equals(d1.get("xh").toString())) {
//                                if (null != d.get("cj") && Float.valueOf(d.get("cj").toString()) < 60) {
//                                    if (null != d1.get("kch") && null != d1.get("kcmc") && null != d1.get("xf")) {
//                                        source.append("【KCH:" + d1.get("kch") + ";");
//                                        source.append("KCMC:" + d1.get("kcmc") + ";");
//                                        source.append("XF:" + d1.get("xf") + "】 ");
//                                        if (null != d1.get("cj")) {
//                                            count++;
//                                            kchs.add(d1.get("kch").toString());
//                                        }
//                                        if (null != d1.get("xf")) {
//                                            totalXF = totalXF + Float.valueOf(d1.get("xf").toString());
//                                        }
//                                        if (null != d1.get("count") && Integer.valueOf(d1.get("count").toString()) > 1) {
//                                            bkcount++;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        sfc.setRequireCreditCount(totalXF);
//                        sfc.setFailRequiredCourseNum(count);
//                        sfc.setMakeUpFailRequiredCourseNum(bkcount);
//                        sfc.setDataSource(source.toString());
//                        sfc.setScheduleCodeList(kchs);
//                        sfcList.add(sfc);
//                    }
//                    lastSemesterScoreStatisticsRespository.save(sfcList);
//                    logger.info("统计上学期成绩汇总到mongo里完成！");
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//                logger.info(e.getMessage());
//            }
//    }




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

            lastSemesterScoreStatisticsRespository.deleteAll();

            if (null != start && null != end) {
                StringBuilder sql = new StringBuilder("SELECT c.COURSE_NUMBER as kch, c.COURSE_NAME as kcmc, cj.XH as xh, MAX(cj.KCCJ) as cj, c.CREDIT as xf, count(1) as count FROM t_xscjxx cj" +
                        " LEFT JOIN t_course c ON cj.KCH = c.COURSE_NUMBER WHERE cj.XKSX = '必修' ");
                sql.append(" AND cj.KSRQ >= ?");
                sql.append(" AND cj.KSRQ <= ?");
                sql.append(" GROUP BY cj.KCH, c.COURSE_NAME, c.CREDIT, cj.XH HAVING MAX(cj.KCCJ) < 60 ");

                final Map<String, ScoreToMongoDTO> maps = new HashMap<>();
                jdbcTemplate.query(sql.toString(), new Object[]{start, end}, new int[]{Types.DATE, Types.DATE}, (ResultSet rs) -> {
                    String xh = rs.getString("xh");
                    ScoreToMongoDTO d = maps.get(xh);
                    if (null == d) {
                        d = new ScoreToMongoDTO();
                        maps.put(xh, d);
                    }
                    int bk = (rs.getInt("count") > 1 ? 1 : 0);
                    d.setBkcount(d.getBkcount() + bk);
                    d.setCount(d.getCount() + 1);
                    d.getKchs().add(rs.getString("kch"));
                    StringBuilder t = new StringBuilder();
                    t.append("【KCH:").append(rs.getString("kch")).append(";");
                    t.append("KCMC:").append(rs.getString("kcmc")).append(";");
                    t.append("XF:").append(rs.getDouble("xf")).append("】 ");
//                    d.getSource().append("【KCH:" + rs.getString("kch") + ";");
//                    d.getSource().append("KCMC:" + rs.getString("kcmc") + ";");
//                    d.getSource().append("XF:" + rs.getDouble("xf") + "】 ");
                    d.getSource().append(t.toString());
                    if (bk > 0) {
                        d.getSource2().append(t.toString());
                    }
                });

                StringBuilder aql = new StringBuilder("SELECT XH as xh, XM as xm, YDDH as yddh, BH as bh, BJMC as bjmc, ZYH as zyh, ZYMC as zymc, YXSH as yxsh, YXSMC as yxsmc, NJ as nj FROM t_xsjbxx  WHERE 1 = 1");
                aql.append(" AND RXFS NOT IN ('12','14')");
                aql.append(" AND RXNY <= :start");
                aql.append(" AND YBYNY >= :end");
                Query aq = em.createNativeQuery(aql.toString());
                aq.setParameter("start", start);
                aq.setParameter("end", end);
                aq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String, Object>> res = aq.getResultList();
                Set<LastSemesterScoreStatistics> sfcList = new HashSet<>();
                for (Map d : res) {
                    LastSemesterScoreStatistics sfc = new LastSemesterScoreStatistics();
                    sfc.setOrgId(orgId);
                    if (null != d.get("xh")) {
                        sfc.setJobNum(d.get("xh").toString());
                    }
                    if (null != d.get("xm")) {
                        sfc.setUserName(d.get("xm").toString());
                    }
                    if (null != d.get("yddh")) {
                        sfc.setPhone(d.get("yddh").toString());
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

                    ScoreToMongoDTO cc = maps.get(sfc.getJobNum());
                    if (null != cc) {
                        sfc.setRequireCreditCount(new Float(cc.getTotalXF()));
                        sfc.setFailRequiredCourseNum(cc.getCount());
                        sfc.setMakeUpFailRequiredCourseNum(cc.getBkcount());
                        if (null != cc.getSource()) {
                            sfc.setDataSource(cc.getSource().toString());
                        }
                        if (null != cc.getSource2() && cc.getSource2().length() > 0) {
                            sfc.setDataSource2(cc.getSource2().toString());
                        }
                        sfc.setScheduleCodeList(cc.getKchs());
                    }
                    sfcList.add(sfc);
                }


                lastSemesterScoreStatisticsRespository.save(sfcList);
                logger.info("统计上学期成绩汇总到mongo里完成！");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

//    private void batchScoreStatics(List<Map<String, Object>> res1, Map<String, Map<String, Object>> maps) {
//        for (Map d1 : res1) {
//            if (null != d1.get("xh")) {
//                Map<String, Object> d = maps.get(d1.get("xh"));
//                if (null != d) {
//                    d.put("count", 0);
//                    d.put("bkcount", 0);
//                    d.put("totalXF", 0.0F);
//                    d.put("kchs", new HashSet<>());
//                    d.put("source", new StringBuilder());
//                }
//                    if (null != d1.get("kch") && null != d1.get("kcmc") && null != d1.get("xf")) {
//                        StringBuilder source = ((StringBuilder)d.get("source"));
//                        source.append("【KCH:" + d1.get("kch") + ";");
//                        source.append("KCMC:" + d1.get("kcmc") + ";");
//                        source.append("XF:" + d1.get("xf") + "】 ");
//                        if (null != d1.get("cj")) {
//                            int count = (Integer)d.get("count");
//                            count++;
//                            d.put("count", count);
//
//                            Set<String> kchs = (HashSet)d.get("kchs");
//                            kchs.add(d1.get("kch").toString());
//                        }
//                        if (null != d1.get("xf")) {
//                            float totalXF = (Float) d.get("totalXF");
//                            totalXF = totalXF + Float.valueOf(d1.get("xf").toString());
//                            d.put("totalXF", totalXF);
//                        }
//                        if (null != d1.get("count") && Integer.valueOf(d1.get("count").toString()) > 1) {
//                            int bkcount = (Integer)d.get("bkcount");
//                            bkcount++;
//                            d.put("bkcount", bkcount);
//                        }
//                    }
//            }
//        }
//    }
    /***********************************************************************************************************
                                    上面是定时统计到mongo中
     ***********************************************************************************************************/

    /**
     * 相邻两个学期(上上个学期平均学分绩点与上学期平均学分绩点)下降情况-----成绩波动
     */
    public List<WarningInformation> scoreFluctuateJob(Long orgId, String teachYear, String semester, String rpId) {
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
                    logger.info("上学期平均绩点:" + scoreFluctuateCount.getSecondAvgradePoint());
                    logger.info("上上学期平均绩点:" + scoreFluctuateCount.getFirstAvgradePoint());
                    // 上学期平均绩点小于上上学期平均绩点时
                    if (result < 0) {
                        result = Math.abs(result);
                        if (result >= Float.parseFloat(String.valueOf(ruleParameter.getRightParameter()))) {
                            WarningInformation alertInfor = new WarningInformation();
//                            String alertId = UUID.randomUUID().toString();
//                            alertInfor.setId(alertId);
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
                                    + new BigDecimal(scoreFluctuateCount.getFirstAvgradePoint()).setScale(2, RoundingMode.HALF_UP).toString() +
                                    ";" + scoreFluctuateCount.getSecondSchoolYear() + "年" + scoreFluctuateCount.getSecondSemester() + "平均学分绩点为:"
                                    + new BigDecimal(scoreFluctuateCount.getSecondAvgradePoint()).setScale(2, RoundingMode.HALF_UP).toString()
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
    public List<WarningInformation> failScoreCountJob(Long orgId, String teachYear, String semester, String rpId) {

        ArrayList<WarningInformation> alertInforList = new ArrayList<>();
        String lastSchoolYear = teachYear;
        String lastSemester = "春";
        if (semester == "春") {
            lastSemester = "秋";
            lastSchoolYear = Integer.valueOf(teachYear) - 1 + "";
        }
        //上学期必修课不及格课程数的数据
        List<LastSemesterScoreStatistics> totalScoreCountList = lastSemesterScoreStatisticsRespository.findAllByTeachYearAndSemesterAndOrgId
                (lastSchoolYear, lastSemester, orgId);
        RuleParameter ruleParameter = ruleParameterService.findById(rpId);
        if (null != totalScoreCountList
                && totalScoreCountList.size() > 0) {
            for (LastSemesterScoreStatistics totalScoreCount : totalScoreCountList) {
                if (null != ruleParameter) {
//                    if (null != ruleParameter.getRightRelationship() && ruleParameter.getRightRelationship().indexOf(">=") > 0) {
                        if (totalScoreCount.getFailRequiredCourseNum() >= Float.parseFloat(ruleParameter.getRightParameter())) {
                            WarningInformation alertInfor = new WarningInformation();
//                            String alertId = UUID.randomUUID()
//                                    .toString();
//                            alertInfor.setId(alertId);
                            alertInfor.setName(totalScoreCount.getUserName());
                            alertInfor.setJobNumber(totalScoreCount.getJobNum());
                            alertInfor.setPhone(totalScoreCount.getPhone());
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
                            alertInfor.setWarningCondition(totalScoreCount.getLastSchoolYear() + "年" + totalScoreCount.getLastSemester() + ruleParameter.getRuledescribe() + totalScoreCount.getFailRequiredCourseNum());
                            alertInfor.setPhone(totalScoreCount.getPhone());
                            alertInfor.setOrgId(ruleParameter.getOrgId());
                            alertInforList.add(alertInfor);
                        } else {
                            continue;
                        }
//                    }

                }
            }
        }
        return alertInforList;
    }

    /**
     * 上学期不合格的必修课程（含集中性实践教学环节）学分
     */
    public List<WarningInformation> failScoreCreditJob(Long orgId, String teachYear, String semester, String ruleId) {

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
                            alertInfor.setWarningCondition(totalScoreCount.getLastSchoolYear() + "年第" + totalScoreCount.getLastSemester() + ruleParameter.getRuledescribe()
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
    public ArrayList<WarningInformation> attendAbnormalJob(Long orgId, String schoolYear, String semester, String rpId) {
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

        ArrayList<WarningInformation> alertInforList = new ArrayList<>();

        String lastSchoolYear = teachYear;
        String lastSemester = "春";
        if (semester == "春") {
            lastSemester = "秋";
            lastSchoolYear = Integer.valueOf(teachYear) - 1 + "";
        }

        // 补考后上学期总评不及格的学生信息
        List<LastSemesterScoreStatistics> makeUpScoreCountList = lastSemesterScoreStatisticsRespository.findAllByTeachYearAndSemesterAndOrgId(lastSchoolYear, lastSemester, orgId);

        RuleParameter ruleParameter = ruleParameterService.findById(rpId);
        if (null != makeUpScoreCountList && makeUpScoreCountList.size() > 0) {
            for (LastSemesterScoreStatistics makeUpScoreCount : makeUpScoreCountList) {
                if (null != ruleParameter) {
                    if (makeUpScoreCount.getMakeUpFailRequiredCourseNum() >= Float
                            .parseFloat(ruleParameter
                                    .getRightParameter())) {
                        WarningInformation alertInfor = new WarningInformation();
//                        String alertId = UUID.randomUUID().toString();
//                        alertInfor.setId(alertId);
                        alertInfor.setName(makeUpScoreCount.getUserName());
                        alertInfor.setJobNumber(makeUpScoreCount.getJobNum());
                        alertInfor.setPhone(makeUpScoreCount.getPhone());
                        alertInfor.setCollogeCode(makeUpScoreCount.getCollegeCode());
                        alertInfor.setCollogeName(makeUpScoreCount.getCollegeName());
                        alertInfor.setClassCode(makeUpScoreCount.getClassCode());
                        alertInfor.setClassName(makeUpScoreCount.getClassName());
                        alertInfor.setProfessionalCode(makeUpScoreCount.getProfessionalCode());
                        alertInfor.setProfessionalName(makeUpScoreCount.getProfessionalName());
                        alertInfor.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
                        alertInfor.setWarningStandard(ruleParameter.getRuledescribe() + ":" + ruleParameter.getRightParameter());
                        alertInfor.setWarningCondition("补考后" + makeUpScoreCount.getLastSchoolYear() + "年" + makeUpScoreCount.getLastSemester() + ruleParameter.getRuledescribe()
                                + makeUpScoreCount.getMakeUpFailRequiredCourseNum());
                        alertInfor.setSemester(semester);
                        alertInfor.setTeacherYear(teachYear);
                        alertInfor.setWarningTime(new Date());
                        alertInfor.setPhone(makeUpScoreCount.getPhone());
                        alertInfor.setOrgId(ruleParameter.getOrgId());
                        if (null != makeUpScoreCount.getDataSource2() && makeUpScoreCount.getDataSource2().length() > 0) {
                            alertInfor.setWarningSource(makeUpScoreCount.getDataSource2().substring(0, makeUpScoreCount.getDataSource2().length() - 1));
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
                        alertInfor.setPhone(makeUpScoreCount.getPhone());
                        alertInfor.setCollogeCode(makeUpScoreCount.getCollegeCode());
                        alertInfor.setCollogeName(makeUpScoreCount.getCollegeName());
                        alertInfor.setClassCode(makeUpScoreCount.getClassCode());
                        alertInfor.setClassName(makeUpScoreCount.getClassName());
                        alertInfor.setProfessionalCode(makeUpScoreCount.getProfessionalCode());
                        alertInfor.setProfessionalName(makeUpScoreCount.getProfessionalName());
                        alertInfor.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
                        BigDecimal failCourseCredit = new BigDecimal(makeUpScoreCount.getFailCourseCredit());
                        alertInfor.setWarningStandard(ruleParameter.getRuledescribe() + ":" + ruleParameter.getRightParameter());
                        alertInfor.setWarningCondition("在校学习期间，考核不合格的必修课程（含集中性实践教学环节）学分:" + failCourseCredit.setScale(2, RoundingMode.HALF_UP).toString());
                        alertInfor.setWarningTime(new Date());
                        alertInfor.setPhone(makeUpScoreCount.getPhone());
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

//    /**
//     * 英语四级考试成绩未通过预警
//     */
//    public ArrayList<WarningInformation> cet4ScoreJobBack(Long orgId, String teachYear, String semester, String rpId) {
//
//        ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
//        try {
//            Date start = null;
//            Date end = null;
//            if (!org.apache.commons.lang.StringUtils.isBlank(teachYear) && !org.apache.commons.lang.StringUtils.isBlank(semester)) {
//                Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teachYear, semester);
//                if (null != schoolCalendar) {
//                    if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
//                        List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
//                        if (tysList.size() > 0) {
//                            start = sdf.parse(tysList.get(0).getStartTime());
//                            end = sdf.parse(tysList.get(0).getEndTime());
//                        }
//                    }
//                }
//            }
//            int year = Integer.valueOf(teachYear);
//            RuleParameter ruleParameter = ruleParameterService.findById(rpId);
//            if (null != ruleParameter) {
//                String parm = ruleParameter.getRightParameter();
//                if (!parm.isEmpty()) {
//                    year = year - Integer.valueOf(parm);
//                }
//            }
//            Date start1 = null;
//            if (!org.apache.commons.lang.StringUtils.isBlank(year + "") && !org.apache.commons.lang.StringUtils.isBlank(semester)) {
//                Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, year + "", semester);
//                if (null != schoolCalendar) {
//                    if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
//                        List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
//                        if (tysList.size() > 0) {
//                            start1 = sdf.parse(tysList.get(0).getStartTime());
//                        }
//                    }
//                }
//            }
//
//            if (null != start && null != end) {
//                StringBuilder aql = new StringBuilder("SELECT XH as xh, XM as xm, BH as bh, BJMC as bjmc, ZYH as zyh, ZYMC as zymc, YXSH as yxsh, YXSMC as yxsmc, NJ as nj FROM t_xsjbxx  WHERE 1 = 1");
//                aql.append(" AND RXFS NOT IN ('12','14')");
//                aql.append(" AND RXNY <= :start");
//                aql.append(" AND YBYNY >= :end");
//                Query aq = em.createNativeQuery(aql.toString());
//                aq.setParameter("start", start);
//                aq.setParameter("end", end);
//                aq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//                List<Map<String, Object>> res = aq.getResultList();
//
//                StringBuilder sql = new StringBuilder("SELECT JOB_NUMBER as xh, MAX(SCORE) as score  FROM t_cet_score WHERE score < 425 AND TYPE = '大学英语四级考试'");
//                sql.append(" AND EXAMINATION_DATE BETWEEN :start AND :end");
//                sql.append(" GROUP BY JOB_NUMBER");
//                Query sq = em.createNativeQuery(sql.toString());
//                sq.setParameter("start", start1);
//                sq.setParameter("end", end);
//                sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//                List<Map<String, Object>> res1 = sq.getResultList();
//                for (Map cd : res1) {
//                    if (null != cd.get("xh")) {
//                        for (Map d : res) {
//                            if (null != d.get("xh") && cd.get("xh").equals(d.get("xh"))) {
//                                WarningInformation alertInfor = new WarningInformation();
////                                String alertId = UUID.randomUUID().toString();
////                                alertInfor.setId(alertId);
//                                if (null != d.get("xh")) {
//                                    alertInfor.setJobNumber(d.get("xh").toString());
//                                }
//                                if (null != d.get("xm")) {
//                                    alertInfor.setName(d.get("xm").toString());
//                                }
//                                if (null != d.get("bh")) {
//                                    alertInfor.setClassCode(d.get("bh").toString());
//                                }
//                                if (null != d.get("bjmc")) {
//                                    alertInfor.setClassName(d.get("bjmc").toString());
//                                }
//                                if (null != d.get("zyh")) {
//                                    alertInfor.setProfessionalCode(d.get("zyh").toString());
//                                }
//                                if (null != d.get("zymc")) {
//                                    alertInfor.setProfessionalName(d.get("zymc").toString());
//                                }
//                                if (null != d.get("yxsh")) {
//                                    alertInfor.setCollogeCode(d.get("yxsh").toString());
//                                }
//                                if (null != d.get("yxsmc")) {
//                                    alertInfor.setCollogeName(d.get("yxsmc").toString());
//                                }
//                                alertInfor.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
//                                alertInfor.setWarningStandard(ruleParameter.getRuledescribe() + ":" + ruleParameter.getRightParameter());
//                                alertInfor.setWarningCondition("在校学年已大于等于" + ruleParameter.getRightParameter()
//                                        + "年仍未通过英语四级考试");
//                                alertInfor.setSemester(semester);
//                                alertInfor.setTeacherYear(teachYear);
//                                alertInfor.setWarningTime(new Date());
//                                alertInfor.setOrgId(ruleParameter.getOrgId());
//                                alertInforList.add(alertInfor);
//                            }
//
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.info(e.getMessage());
//        }
//        return alertInforList;
//    }



    /**
     * 英语四级考试成绩未通过预警
     */
    public List<WarningInformation> cet4ScoreJob(Long orgId, String teachYear, String semester, String rpId) {
        List<WarningInformation> alertInforList = new ArrayList<>();
        try {
            Date start = null;
            if (!org.apache.commons.lang.StringUtils.isBlank(teachYear) && !org.apache.commons.lang.StringUtils.isBlank(semester)) {
                Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teachYear, semester);
                if (null != schoolCalendar) {
                    if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                        List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                        if (tysList.size() > 0) {
                            start = sdf.parse(tysList.get(0).getStartTime());
                        }
                    }
                }
            }
            RuleParameter ruleParameter = ruleParameterService.findById(rpId);

            if (null != start) {
                String ym = DateUtil.format(start, "yyyyMM");
                if (null != ym && ym.length() > 4) {
                    ym = ym.substring(0, 4) + "12";
                }
                Integer ys = 1000;
                if (null != ruleParameter.getRightParameter()) {
                    ys = new Integer(ruleParameter.getRightParameter()) * 100;
                }
                final Date current = new Date();
                StringBuilder sql = new StringBuilder("SELECT ");
                sql.append("x.xh,x.xm, x.yddh, x.bh, x.bjmc, x.zyh, x.zymc, x.yxsh, x.yxsmc ");
                sql.append("FROM (");
                sql.append("SELECT XH as xh, XM as xm, YDDH as yddh, BH as bh, BJMC as bjmc, ZYH as zyh, ZYMC as zymc, YXSH as yxsh, YXSMC as yxsmc ");
                sql.append("FROM t_xsjbxx  WHERE RXFS NOT IN ('12','14') AND RXNY <= ? AND YBYNY >= ? AND (? - CAST(RXNY AS SIGNED)) > ? ");
                sql.append(") x, (");
                sql.append("SELECT JOB_NUMBER as xh  ");
                sql.append("FROM t_cet_score WHERE TYPE = '大学英语四级考试' ");
                sql.append("GROUP BY JOB_NUMBER HAVING MAX(SCORE) < 425 ) c ");
                sql.append("WHERE x.xh=c.xh");
                alertInforList = jdbcTemplate.query(sql.toString(), new Object[]{ym, start, new Integer(ym), ys}, new int[]{Types.VARCHAR,Types.DATE, Types.INTEGER,  Types.INTEGER},
                        (ResultSet rs, int rowNum) -> {
                            WarningInformation alertInfor = new WarningInformation();
                            alertInfor.setJobNumber(rs.getString("xh"));
                            alertInfor.setName(rs.getString("xm"));
                            alertInfor.setClassCode(rs.getString("bh"));
                            alertInfor.setClassName(rs.getString("bjmc"));
                            alertInfor.setProfessionalCode(rs.getString("zyh"));
                            alertInfor.setProfessionalName(rs.getString("zymc"));
                            alertInfor.setCollogeCode(rs.getString("yxsh"));
                            alertInfor.setCollogeName(rs.getString("yxsmc"));
                            alertInfor.setPhone(rs.getString("yddh"));

                            alertInfor.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
                            alertInfor.setWarningStandard(ruleParameter.getRuledescribe() + ":" + ruleParameter.getRightParameter());
                            alertInfor.setWarningCondition("在校学年已大于等于" + ruleParameter.getRightParameter() + "年仍未通过英语四级考试");
                            alertInfor.setSemester(semester);
                            alertInfor.setTeacherYear(teachYear);
                            alertInfor.setWarningTime(current);
                            alertInfor.setOrgId(ruleParameter.getOrgId());
                            return alertInfor;
                        });
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return alertInforList;
    }
}
