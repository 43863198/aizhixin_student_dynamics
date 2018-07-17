package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.RegisterAlertCountDomain;
import com.aizhixin.cloud.dataanalysis.analysis.constant.TrendType;
import com.aizhixin.cloud.dataanalysis.analysis.dto.*;
import com.aizhixin.cloud.dataanalysis.analysis.entity.PracticeStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.CetScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.respository.PracticeStaticsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.respository.SchoolStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.respository.TeachingScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.vo.*;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.util.ProportionUtil;

import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.mongodb.BasicDBObject;
import liquibase.executor.jvm.RowMapper;
import liquibase.util.StringUtils;

import org.hibernate.SQLQuery;
import org.hibernate.mapping.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-29
 */
@Component
@Transactional
public class SchoolStatisticsService {
    @Autowired
    private SchoolStatisticsRespository schoolStatisticsRespository;
    @Autowired
    private EntityManager em;
    @Autowired
    private PracticeStaticsRespository practiceStaticsRespository;
    @Autowired
    private CetScoreStatisticsRespository cetScoreStatisticsRespository;
    @Autowired
    private TeachingScoreStatisticsRespository teachingScoreStatisticsRespository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SchoolYearTermService schoolYearTermService;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public void deleteAllByOrgId(Long orgId) {
        schoolStatisticsRespository.deleteByOrgId(orgId);
    }

    public void deleteByOrgIdAndTeacherYear(Long orgId, Integer teacherYear) {
        schoolStatisticsRespository.deleteByOrgIdAndTeacherYear(orgId, teacherYear);
    }

    /**
     * 批量保存学校统计数据
     *
     * @param statisticsList
     */
    public void saveList(List<SchoolStatistics> statisticsList) {
        schoolStatisticsRespository.save(statisticsList);
    }

    public void save(SchoolStatistics statistics) {
        schoolStatisticsRespository.save(statistics);
    }

    public SchoolStatistics findById(String id) {
        return schoolStatisticsRespository.findOne(id);
    }


    public Map<String, Object> getStatisticNewstudents(Long orgId, Integer teacherYear) {
        Map<String, Object> result = new HashMap<>();
        NewStudentProfileDTO newStudentProfileDTO = new NewStudentProfileDTO();
        Map<String, Object> condition = new HashMap<>();
        long studentNumber = 0;
        long alreadyReport = 0;
        int alreadyPay = 0;
        int convenienceChannel = 0;
        Date time = new Date();
        try {
            StringBuilder sql = new StringBuilder("SELECT SUM(ss.NEW_STUDENTS_COUNT) as sum, SUM(ss.ALREADY_REPORT) as rsum, SUM(ss.ALREADY_PAY) as psum, SUM(ss.CONVENIENCE_CHANNEL) as csum, max(ss.STATISTICAL_TIME) FROM T_SCHOOL_STATISTICS ss WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != teacherYear) {
                sql.append(" and ss.TEACHER_YEAR = :teacherYear");
                condition.put("teacherYear", teacherYear);
            }
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());

            }
            Object res = sq.getSingleResult();
            if (null != res) {
                Object[] d = (Object[]) res;
                if (null != d[0]) {
                    studentNumber = Integer.valueOf(String.valueOf(d[0]));
                }
                if (null != d[1]) {
                    alreadyReport = Integer.valueOf(String.valueOf(d[1]));
                }
                if (null != d[2]) {
                    alreadyPay = Integer.valueOf(String.valueOf(d[2]));
                }
                if (null != d[3]) {
                    convenienceChannel = Integer.valueOf(String.valueOf(d[3]));
                }
                if (null != d[4]) {
                    time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(d[4]));
                }
            }

            newStudentProfileDTO.setStudentNumber(studentNumber);
            newStudentProfileDTO.setAlreadyReport(alreadyReport);
            newStudentProfileDTO.setUnreported(studentNumber - alreadyReport);
            newStudentProfileDTO.setProportion(ProportionUtil.accuracy(alreadyReport * 1.0, studentNumber * 1.0, 2));
            newStudentProfileDTO.setAlreadyPay(alreadyPay);
            newStudentProfileDTO.setConvenienceChannel(convenienceChannel);
            newStudentProfileDTO.setStatisticalTime(time);//后续要改为统计时间
            List<SchoolStatistics> schoolStatisticsList = schoolStatisticsRespository.findDataByOrgIdAndTeacherYear(orgId, teacherYear, DataValidity.VALID.getState());
            newStudentProfileDTO.setSchoolStatisticsListData(schoolStatisticsList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取学校新生统计信息异常！");
        }
        result.put("success", true);
        result.put("data", newStudentProfileDTO);
        return result;
    }

    public Map<String, Object> getTrendType() {
        Map<String, Object> result = new HashMap<>();
        List<TrendTypeDTO> trendTypeDTOList = new ArrayList<>();
        try {
            for (TrendType type : TrendType.values()) {
                TrendTypeDTO trendTypeDTO = new TrendTypeDTO();
                trendTypeDTO.setIndex(type.getIndex());
                trendTypeDTO.setTyep(type.toString());
                trendTypeDTO.setTypeName(type.getName());
                trendTypeDTOList.add(trendTypeDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取指标类型失败！");
            return result;
        }
        result.put("success", true);
        result.put("data", trendTypeDTOList);
        return result;
    }

    /**
     * 全校人数统计查询
     *
     * @param orgId
     * @return
     */
    public HomeData<SchoolProfileDTO> getSchoolPersonStatistics(Long orgId) {
        String sql = "SELECT SEMESTER ,TEACHER_YEAR  FROM `t_practice_statistics` where ORG_ID=" + orgId + "  ORDER BY TEACHER_YEAR DESC,SEMESTER DESC LIMIT 1";
        Map currentGradeMap = new HashMap();
        try {
            currentGradeMap = jdbcTemplate.queryForMap(sql);
            int teacherYear = Integer.valueOf(currentGradeMap.get("TEACHER_YEAR") + "");
            int semester = Integer.valueOf(currentGradeMap.get("SEMESTER") + "");
            PracticeStaticsDTO practiceStaticsDTO = practiceStaticsRespository.getPracticeStatics(orgId, teacherYear, semester);
            SchoolProfileDTO schoolProfileDTO = schoolStatisticsRespository.getSchoolPersonStatistics(orgId, teacherYear);
//        schoolProfileDTO.setOutSchoolStudent(Long.valueOf(practiceStaticsDTO.getPracticeStudentNum()));
//        schoolProfileDTO.setInSchoolStudent(Long.valueOf(schoolProfileDTO.getAllStudent()) - Long.valueOf(schoolProfileDTO.getOutSchoolStudent()));
            HomeData<SchoolProfileDTO> h = new HomeData();
            TeacherlYearData teacherlYearData = new TeacherlYearData();
            teacherlYearData.setSemester(semester);
            teacherlYearData.setTeacherYear(teacherYear);
            h.setTeacherlYearData(teacherlYearData);

            Map<String, Object> con = new HashMap<>();
            StringBuilder ssl = new StringBuilder("SELECT sum(if(CURDATE() BETWEEN x.RXNY AND x.YBYNY,1,0)) as count, sum(if(x.YBYNY > now() and datediff(x.YBYNY,now()) < 300,1,0)) as yby FROM t_xsjbxx x WHERE 1 = 1");
            StringBuilder tsl = new StringBuilder("SELECT count(1) as count FROM t_jzgjbxx");
            StringBuilder tcsl = new StringBuilder("SELECT count(1) as count FROM t_class_teacher");
            if (null != orgId) {
                ssl.append(" AND x.XXID = :orgId");
                con.put("orgId", orgId);
            }
            Query ssq = em.createNativeQuery(ssl.toString());
            Query tsq = em.createNativeQuery(tsl.toString());
            Query tcsq = em.createNativeQuery(tcsl.toString());
            ssq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            tsq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            tcsq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : con.entrySet()) {
                ssq.setParameter(e.getKey(), e.getValue());
            }
            Object re = ssq.getSingleResult();
            Object tre = tsq.getSingleResult();
            Object tcre = tcsq.getSingleResult();
            Map<String, Object> map = (Map) re;
            Map<String, Object> tmap = (Map) tre;
            Map<String, Object> tcmap = (Map) tcre;
            if (null != map) {
                if (null != map.get("count")) {
                    schoolProfileDTO.setInSchoolStudent(Long.valueOf(map.get("count").toString()));
                    schoolProfileDTO.setAllStudent(Long.valueOf(map.get("count").toString()));
                }
                if (null != map.get("yby")) {
                    schoolProfileDTO.setReadyGraduation(Long.valueOf(map.get("yby").toString()));
                }
            }
            if (null != tmap.get("count")) {
                schoolProfileDTO.setAllTeacher(Long.valueOf(tmap.get("count").toString()));
            }
            if (null != tcmap.get("count")) {
                schoolProfileDTO.setAllInstructor(Long.valueOf(tcmap.get("count").toString()));
            }
            h.setObjData(schoolProfileDTO);
            return h;
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
    }

    /**
     * 迎新学情统计查询
     *
     * @param orgId
     * @return
     */
    public NewStudentProfileDTO getNewStudentStatistics(Long orgId) {
        return schoolStatisticsRespository.getNewStudentStatistics(orgId);
    }

    public Map<String, Object> getTrend(Long orgId, Long collegeId) {
        Map<String, Object> result = new HashMap<>();
        List<NewTrendDTO> trendDTOList = new ArrayList<>();
        try {
            Map<String, Object> nsccondition = new HashMap<>();
            StringBuilder nscsql = new StringBuilder("SELECT TEACHER_YEAR, SUM(NEW_STUDENTS_COUNT) FROM T_SCHOOL_STATISTICS  WHERE 1 = 1");
            if (null != orgId) {
                nscsql.append(" and ORG_ID = :orgId");
                nsccondition.put("orgId", orgId);
            }
            if (null != collegeId) {
                nscsql.append(" and COLLEGE_ID = :collegeId");
                nsccondition.put("collegeId", collegeId);
            }
            nscsql.append(" GROUP BY TEACHER_YEAR");
            Query nscsq = em.createNativeQuery(nscsql.toString());
            for (Map.Entry<String, Object> e : nsccondition.entrySet()) {
                nscsq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> nsc = nscsq.getResultList();
            if (null != nsc && nsc.size() > 0) {
                for (Object obj : nsc) {
                    Object[] d = (Object[]) obj;
                    NewTrendDTO trendDTO = new NewTrendDTO();
                    if (null != d[0]) {
                        trendDTO.setYear(String.valueOf(d[0]));
                        if (null != d[1]) {
                            trendDTO.setNewStudentsCount(Integer.valueOf(String.valueOf(d[1])));
                        }
                        if (trendDTO.getNewStudentsCount() > 0) {
                            trendDTOList.add(trendDTO);
                        }

                    }

                }
            }

            Map<String, Object> arcondition = new HashMap<>();
            StringBuilder arsql = new StringBuilder("SELECT TEACHER_YEAR, SUM(ALREADY_REPORT) FROM T_SCHOOL_STATISTICS  WHERE 1 = 1");
            if (null != orgId) {
                arsql.append(" and ORG_ID = :orgId");
                arcondition.put("orgId", orgId);
            }
            if (null != collegeId) {
                arsql.append(" and COLLEGE_ID = :collegeId");
                arcondition.put("collegeId", collegeId);
            }
            arsql.append(" GROUP BY TEACHER_YEAR");
            Query arsq = em.createNativeQuery(arsql.toString());
            for (Map.Entry<String, Object> e : arcondition.entrySet()) {
                arsq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> ar = arsq.getResultList();
            if (null != ar && ar.size() > 0) {
                for (NewTrendDTO td : trendDTOList) {
                    for (Object obj : ar) {
                        Object[] d = (Object[]) obj;
                        if (null != d[0]) {
                            if (td.getYear().equals(String.valueOf(d[0]))) {
                                if (null != d[1]) {
                                    td.setAlreadyReport(Integer.valueOf(String.valueOf(d[1])));
                                    if (td.getNewStudentsCount() > 0) {
                                        td.setReportRate(new DecimalFormat("0.00").format((double) td.getAlreadyReport() * 100 / td.getNewStudentsCount()));

                                    } else {
                                        td.setReportRate(0 + "");
                                    }
                                } else {
                                    td.setReportRate(0 + "");
                                }
                                break;
                            }
                        }
                    }
                }
            }
            if (trendDTOList.size() > 1) {
                for (int i = 1; i < trendDTOList.size(); i++) {
                    Double change1 = (Double.valueOf(trendDTOList.get(i).getNewStudentsCount()) - Double.valueOf(trendDTOList.get(i - 1).getNewStudentsCount())
                    ) / Double.valueOf(trendDTOList.get(i - 1).getNewStudentsCount());
                    if (null != change1 && !change1.isNaN() && !change1.isInfinite()) {
                        trendDTOList.get(i).setNscChange(new DecimalFormat("0.00").format(change1 * 100));
                    } else {
                        trendDTOList.get(i).setNscChange(0 + "");
                    }

                    Double change2 = (Double.valueOf(trendDTOList.get(i).getAlreadyReport()) - Double.valueOf(trendDTOList.get(i - 1).getAlreadyReport())
                    ) / Double.valueOf(trendDTOList.get(i - 1).getAlreadyReport());
                    if (null != change2 && !change2.isNaN() && !change2.isInfinite()) {
                        trendDTOList.get(i).setArChange(new DecimalFormat("0.00").format(change2 * 100));
                    } else {
                        trendDTOList.get(i).setArChange(0 + "");
                    }

                    Double change3 = (Double.valueOf(trendDTOList.get(i).getReportRate()) - Double.valueOf(trendDTOList.get(i - 1).getReportRate())
                    ) / Double.valueOf(trendDTOList.get(i - 1).getReportRate());
                    if (null != change3 && !change3.isNaN() && !change3.isInfinite()) {
                        trendDTOList.get(i).setRrChange(new DecimalFormat("0.00").format(change3 * 100));
                    } else {
                        trendDTOList.get(i).setRrChange(0 + "");
                    }
                }
            }
            result.put("success", true);
            result.put("dataList", trendDTOList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取分析数据异常！");
            return result;
        }
    }

    /**
     * 实践学情首页统计查询
     *
     * @param orgId
     * @return
     */
    public HomeData<PracticeStaticsDTO> getPracticeStatics(Long orgId) {
        String sql = "SELECT SEMESTER ,TEACHER_YEAR  FROM `t_practice_statistics`  where ORG_ID=" + orgId + "  ORDER BY TEACHER_YEAR DESC,SEMESTER DESC LIMIT 1";
        Map currentGradeMap = new HashMap();
        try {
            currentGradeMap = jdbcTemplate.queryForMap(sql);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
        int teacherYear = Integer.valueOf(currentGradeMap.get("TEACHER_YEAR") + "");
        int semester = Integer.valueOf(currentGradeMap.get("SEMESTER") + "");
        PracticeStaticsDTO practiceStaticsDTO = practiceStaticsRespository.getPracticeStatics(orgId, teacherYear, semester);
        HomeData<PracticeStaticsDTO> h = new HomeData();
        TeacherlYearData teacherlYearData = new TeacherlYearData();
        teacherlYearData.setSemester(semester);
        teacherlYearData.setTeacherYear(teacherYear);
        h.setTeacherlYearData(teacherlYearData);
        h.setObjData(practiceStaticsDTO);
        return h;
    }


    /**
     * 四六级学情首页统计查询
     *
     * @param orgId
     * @return
     */
    public HomeData<CetScoreStatisticsDTO> getEctStatics(Long orgId) {
        TeacherlYearData teacherlYearData = new TeacherlYearData();
        HomeData<CetScoreStatisticsDTO> h = new HomeData();
        try {
            Date start = null;
            Date end = null;
            String teacherYear = null;
            String semester = null;
            Map<String, Object> schoolCalendar = schoolYearTermService.getCurrentSchoolCalendar(orgId);
            if (null != schoolCalendar) {
                if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                    List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                    if (tysList.size() > 0) {
                        teacherYear = tysList.get(0).getTeacherYear();
                        semester = tysList.get(0).getSemester();
                    }
                }
            }
            if (null != teacherYear && null != semester) {
                // 上学年学期
                String secondSchoolYear = teacherYear;
                String secondSemester = "春";
                if (semester.equals("春")) {
                    secondSemester = "秋";
                    secondSchoolYear = Integer.valueOf(teacherYear) - 1 + "";
                }
                Map<String, Object> secondschoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, secondSchoolYear, secondSemester);
                if (null != secondschoolCalendar) {
                    if (null != secondschoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                        List<TeacherYearSemesterDTO> secondtysList = (List<TeacherYearSemesterDTO>) secondschoolCalendar.get("data");
                        if (secondtysList.size() > 0) {
                            start = sdf.parse(secondtysList.get(0).getStartTime());
                            end = sdf.parse(secondtysList.get(0).getEndTime());
                        }
                    }
                }
                /*********************这个以后统一学年学期后修改*****************/
                if (semester.equals("春")) {
                    teacherlYearData.setSemester(1);
                } else {
                    teacherlYearData.setSemester(2);
                }
                teacherlYearData.setTeacherYear(Integer.valueOf(teacherYear));
                /***************************************************************/
            }
            StringBuilder sql = new StringBuilder("SELECT ss.SCORE_TYPE as type, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss WHERE 1=1");
            if (null != start && null != end) {
                sql.append(" AND ss.EXAMINATION_DATE BETWEEN :start AND :end");
            }
            if (null!= orgId) {
                sql.append(" AND ss.ORG_ID = :orgId");
            }
            sql.append(" AND (SCORE_TYPE ='大学英语四级考试' OR SCORE_TYPE ='大学英语六级考试') AND STATISTICS_TYPE = '000'");
            sql.append(" GROUP BY type");

            Query sq = em.createNativeQuery(sql.toString());
            if (null != start && null != end) {
                sq.setParameter("start", start);
                sq.setParameter("end", end);
            }
            if(null!=orgId){
                sq.setParameter("orgId", orgId);
            }
        sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> res = sq.getResultList();
            CetScoreStatisticsDTO cetScoreStatisticsDTO = new CetScoreStatisticsDTO();
            int total = 0;
            int pass = 0;
            for(Map<String, Object> d: res){
                if(null!=d.get("type")){
                    if(String.valueOf(d.get("type")).equals("大学英语四级考试")){
                        if(null!=d.get("total")) {
                            cetScoreStatisticsDTO.setCetForeJoinNum(Long.valueOf(d.get("total").toString()));
                            total = total + Integer.valueOf(d.get("total").toString());
                        }
                        if(null!=d.get("pass")) {
                            cetScoreStatisticsDTO.setCetForePassNum(Long.valueOf(d.get("pass").toString()));
                            pass = pass + Integer.valueOf(d.get("pass").toString());
                        }

                    }
                    if(String.valueOf(d.get("type")).equals("大学英语六级考试")){
                        if(null!=d.get("total")) {
                            cetScoreStatisticsDTO.setCetSixJoinNum(Long.valueOf(d.get("total").toString()));
                            total = total + Integer.valueOf(d.get("total").toString());
                        }
                        if(null!=d.get("pass")) {
                            cetScoreStatisticsDTO.setCetSixPassNum(Long.valueOf(d.get("pass").toString()));
                            pass = pass + Integer.valueOf(d.get("pass").toString());
                        }
                    }
                }
            }
            cetScoreStatisticsDTO.setCetJoinNum(new Long(total));
            cetScoreStatisticsDTO.setCetPassNum(new Long(pass));
            h.setTeacherlYearData(teacherlYearData);
            h.setObjData(cetScoreStatisticsDTO);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return h;
    }

        /**
     * 教学成绩首页统计查询
     *
     * @param orgId
     * @return
     */
    public HomeData<TeachingScoreStatisticsDTO> getTeachingSoreStatics(Long orgId) {
        Map<String, Object> map = new HashMap<String, Object>();

        String sql = "SELECT SEMESTER ,TEACHER_YEAR  FROM `t_teaching_score_statistics`  where ORG_ID=" + orgId + " ORDER BY TEACHER_YEAR DESC,SEMESTER DESC LIMIT 1";
        Map currentGradeMap = new HashMap();
        try {
            currentGradeMap = jdbcTemplate.queryForMap(sql);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return null;
        }
        int teacherYear = Integer.valueOf(currentGradeMap.get("TEACHER_YEAR") + "");
        int semester = Integer.valueOf(currentGradeMap.get("SEMESTER") + "");
        List<TeachingScoreStatisticsDTO> list = teachingScoreStatisticsRespository.getTeachingScoreStatisticsByOrgId(orgId, teacherYear, semester);
        List<TeachingScoreStatisticsDTO> list0 = teachingScoreStatisticsRespository.getAvgTeachingScore(orgId, teacherYear, semester);
        TeachingScoreStatisticsDTO obj = null;
        if (null != list0 && list0.size() > 0) {
            obj = list0.get(0);
        }
        HomeData<TeachingScoreStatisticsDTO> h = new HomeData();
        TeacherlYearData teacherlYearData = new TeacherlYearData();
        teacherlYearData.setSemester(semester);
        teacherlYearData.setTeacherYear(teacherYear);
        h.setTeacherlYearData(teacherlYearData);
        h.setObjData(obj);
        h.setData(list);
        return h;

    }


    public Map<String, Object> getCollegeDetails(Pageable page, Integer teacherYear, Long orgId, String collegeId, String nj, String type, String isReport, String isPay) {
        Map<String, Object> result = new HashMap<>();
        PageData<StudentRegister> p = new PageData<>();
        List<StudentRegister> items = new ArrayList<>();
        long total = 0L;
        try {
            //创建排序模板Sort
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            //创建分页模板Pageable
            Pageable pageable = new PageRequest(page.getPageNumber(), page.getPageSize(), sort);
            //创建查询条件对象
            org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();
            //条件
            Criteria criteria = Criteria.where("orgId").is(orgId);
            if (null != teacherYear) {
                criteria.and("schoolYear").is(teacherYear);
            }
            if (null != collegeId) {
                Set<Long> collegeIds = new HashSet<>();
                if (collegeId.indexOf(",") != -1) {
                    String[] cid = collegeId.split(",");
                    for (String d : cid) {
                        collegeIds.add(Long.valueOf(d));
                    }
                } else {
                    collegeIds.add(Long.valueOf(collegeId));
                }
                criteria.and("collegeId").in(collegeIds);
            }
            if (null != type) {
                List tds = new ArrayList<>();
                if (type.indexOf(",") != -1) {
                    String[] td = type.split(",");
                    for (String d : td) {
                        tds.add(Integer.valueOf(d));
                    }
                } else {
                    tds.add(Integer.valueOf(type));
                }
                criteria.and("education").in(tds);
            }
            if (null != isPay) {
                List<String> pay = new ArrayList<>();
                if (isPay.indexOf(",") != -1) {
                    String[] py = isPay.split(",");
                    for (String s : py) {
                        pay.add(s);
                    }
                } else {
                    pay.add(isPay);
                }
                int flag = 0;
                if (pay.contains("3")) {
                    flag = 5;
                }
                if (pay.contains("1") && !pay.contains("2") && flag != 5) {
                    flag = 1;
                }
                if (!pay.contains("1") && pay.contains("2") && flag != 5) {
                    flag = 2;
                }
                if (pay.contains("1") && pay.contains("2") && flag != 5) {
                    flag = 3;
                }
                if (flag == 1) {
                    criteria.and("isPay").is(1);
                }
                if (flag == 2) {
                    criteria.and("isGreenChannel").is(1);
                }
                if (flag == 3) {
                    criteria.orOperator(criteria.where("isPay").is(1), criteria.where("isGreenChannel").is(1));
                }
                if (flag == 5) {
                    criteria.and("isPay").ne(1);
                    criteria.and("isGreenChannel").ne(1);
                }
            }

            if (null != isReport) {
                if (isReport.equals("0")) {
                    criteria.and("isRegister").is(0);
                }
                if (isReport.equals("1")) {
                    criteria.and("isRegister").is(1);
                }
            }

            if (!org.apache.commons.lang.StringUtils.isBlank(nj)) {
                criteria.orOperator(criteria.where("userName").regex(nj), criteria.where("jobNum").regex(nj));
            }
            query.addCriteria(criteria);
            //mongoTemplate.count计算总数
            total = mongoTemplate.count(query, StudentRegister.class);
            // mongoTemplate.find 查询结果集
            items = mongoTemplate.find(query.with(pageable), StudentRegister.class);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取数据异常！");
            return result;
        }

        p.getPage().setTotalPages((int) Math.ceil(total / page.getPageSize()) + 1);
        p.getPage().setPageNumber(page.getPageNumber());
        p.getPage().setPageSize(page.getPageSize());
        p.getPage().setTotalElements(total);
        p.setData(items);
        result.put("success", true);
        result.put("data", p);
        return result;
    }

    public void deleteSchollStatistics(Long orgId, Integer teacherYear) {
        schoolStatisticsRespository.deleteByOrgIdAndTeacherYear(orgId, teacherYear);
    }


    public Map<String, Object> getEnrollment(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        List<ReportRateVO> reportRateVOList = new ArrayList<>();
        try {
            //条件
            Criteria criteria = Criteria.where("orgId").is(orgId);
            criteria.and("isRegister").is(1);
            AggregationResults<BasicDBObject> register = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criteria),
                            Aggregation.group("schoolYear").count().as("count").first("schoolYear").as("schoolYear")
                    ), StudentRegister.class, BasicDBObject.class);

            for (int i = 0; i < register.getMappedResults().size(); i++) {
                ReportRateVO rr = new ReportRateVO();
                rr.setYear(register.getMappedResults().get(i).getString("schoolYear"));
                rr.setReportNumber(register.getMappedResults().get(i).getInt("count"));
                reportRateVOList.add(rr);
            }
            if (reportRateVOList.size() > 1) {
                for (int j = 1; j < reportRateVOList.size(); j++) {
                    Double change = (Double.valueOf(reportRateVOList.get(j).getReportNumber() - Double.valueOf(reportRateVOList.get(j - 1).getReportNumber())
                    ) / Double.valueOf(reportRateVOList.get(j - 1).getReportNumber()));
                    if (null != change && !change.isNaN() && !change.isInfinite()) {
                        reportRateVOList.get(j).setChange(new DecimalFormat("0.00").format(change * 100));
                    }
                }
            }
            result.put("success", true);
            result.put("data", reportRateVOList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取历年报到人数情况失败！");
            return result;
        }

    }


    public Map<String, Object> graduateSituation(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        List<GraduateRateVO> graduateRateVOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(Calendar.YEAR) - 1);
            StringBuilder sql = new StringBuilder("SELECT SUBSTRING(DIPLOMA_NUMBER,7,4) AS year, count(1) as count FROM t_academic_degree WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" AND ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            //因数据问题计算的是2015年开始统计到当前
            sql.append(" AND SUBSTRING(DIPLOMA_NUMBER, 7, 4) BETWEEN '2015' AND '" + year + "' GROUP BY year");
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Object> res = sq.getResultList();
            for (Object obj : res) {
                Map row = (Map) obj;
                GraduateRateVO gr = new GraduateRateVO();
                if (null != row.get("year")) {
                    gr.setYear(row.get("year").toString());
                    if (null != row.get("count")) {
                        gr.setNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    graduateRateVOList.add(gr);
                }

            }
            if (null != graduateRateVOList && graduateRateVOList.size() > 1) {
                for (int i = 1; i < graduateRateVOList.size(); i++) {
                    Double change = (double) (graduateRateVOList.get(i).getNumber() - graduateRateVOList.get(i - 1).getNumber()
                    ) / graduateRateVOList.get(i - 1).getNumber();
                    if (null != change && !change.isNaN() && !change.isInfinite()) {
                        graduateRateVOList.get(i).setChange(new DecimalFormat("0.00").format(change * 100));
                    } else {
                        graduateRateVOList.get(i).setChange(0 + "");
                    }
                }
            }
            result.put("success", true);
            result.put("data", graduateRateVOList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取毕业生人数情况失败！");
            return result;
        }
    }


    public Map<String, Object> studentStatistics(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        StudentStatisticsVO studentStatisticsVO = new StudentStatisticsVO();
        Map<String, Object> condition = new HashMap<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            Date date = new Date();
            String year = sdf.format(date);
            StringBuilder sql = new StringBuilder("SELECT count(ss.JOB_NUMBER) as count FROM t_student_status ss WHERE 1 = 1");
            StringBuilder cql = new StringBuilder("SELECT count(ss.JOB_NUMBER) AS count FROM t_student_status ss WHERE 1 = 1 ");
            if (null != orgId) {
                sql.append(" AND ORG_ID = :orgId");
                cql.append(" AND ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            sql.append(" AND CURDATE() BETWEEN ss.ENROL_YEAR AND ss.GRADUATION_DATE");
            cql.append(" AND ss.STATE IN ('02','04','16') AND CURDATE() BETWEEN ss.ENROL_YEAR AND ss.GRADUATION_DATE");

            Query sq = em.createNativeQuery(sql.toString());
            Query cq = em.createNativeQuery(cql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
                cq.setParameter(e.getKey(), e.getValue());
            }
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            cq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Object count = sq.getSingleResult();
            Object ccount = cq.getSingleResult();
            if (null != count) {
                Map row = (Map) count;
                if (null != row.get("count")) {
                    studentStatisticsVO.setTotal(Integer.valueOf(row.get("count").toString()));
                }
            }
            if (null != ccount) {
                Map crow = (Map) ccount;
                if (null != crow.get("count")) {
                    studentStatisticsVO.setStopNumber(Integer.valueOf(crow.get("count").toString()));
                }
            }
            studentStatisticsVO.setNumberOfSchools(studentStatisticsVO.getTotal() - studentStatisticsVO.getStopNumber());
//            HomeData<SchoolProfileDTO> data= this.getSchoolPersonStatistics(orgId);
//            studentStatisticsVO.setNumberOfSchools(data.getObjData().getInSchoolStudent().intValue());
//            studentStatisticsVO.setStopNumber(0);
//            studentStatisticsVO.setTotal(data.getObjData().getAllStudent().intValue());
            result.put("success", true);
            result.put("data", studentStatisticsVO);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取毕业生人数情况失败！");
            return result;
        }
    }

    public Map<String, Object> teachingBuildingUsage(Long orgId) {
        List<TeachingBuildingsUsegeVO> dataList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int day = 0;
        long weeks = 0;
        String start = "";
        try {
            if (null != orgId) {
                StringBuilder sql = new StringBuilder("SELECT START_TIME as start FROM t_school_calendar WHERE ORG_ID =" + orgId + " order by START_TIME desc limit 1");
                Query sq = em.createNativeQuery(sql.toString());
                Object time = sq.getSingleResult();
                if (null != time) {
                    start = time.toString();
                }
            }
            if (!org.apache.commons.lang.StringUtils.isBlank(start)) {
                Date endDate = df.parse(df.format(new Date()));
                Date startDate = df.parse(start);
                //实例化起始和结束Calendar对象
                Calendar startCalendar = Calendar.getInstance();
                Calendar endCalendar = Calendar.getInstance();
                //分别设置Calendar对象的时间
                startCalendar.setTime(startDate);
                endCalendar.setTime(endDate);

                //定义起始日期和结束日期分别属于第几周
                int startWeek = startCalendar.get(Calendar.WEEK_OF_YEAR);
                int endWeek = endCalendar.get(Calendar.WEEK_OF_YEAR);

                //拿到起始日期是星期几
                int startDayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);
                if (startDayOfWeek == 1) {
                    startDayOfWeek = 7;
                    startWeek--;
                } else startDayOfWeek--;

                //拿到结束日期是星期几
                int endDayOfWeek = endCalendar.get(Calendar.DAY_OF_WEEK);
                if (endDayOfWeek == 1) {
                    endDayOfWeek = 7;
                    endWeek--;
                } else endDayOfWeek--;

                //计算相差的周数
                weeks = endWeek - startWeek + 1;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                day = calendar.get(Calendar.DAY_OF_WEEK);
                day = day - 1;
                if (day <= 0) {
                    day = 7;
                }
            }
            StringBuilder oql = new StringBuilder("SELECT JC AS jc FROM t_glut_dc_xjxx WHERE 1=1 ");
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String time = format.format(new Date());
            oql.append(" AND :time BETWEEN KSSJ AND JSSJ");
            Query oq = em.createNativeQuery(oql.toString());
            oq.setParameter("time",time);
            List<Object> jcList = oq.getResultList();
            int jc = 0;
            if(null!=jcList&&jcList.size()>0){
                Pattern pattern = Pattern.compile("^[0-9]*$");
                Matcher matcher = pattern.matcher(jcList.get(0).toString());
                if(matcher.matches()){
                    jc = Integer.parseInt(jcList.get(0).toString());
                }
            }

            StringBuilder cql = new StringBuilder("SELECT TEACHING_BUILDING_NUMBER as tbn, count(1) as count FROM t_class_room WHERE NORMAL = 0 ");
            StringBuilder sql = new StringBuilder("SELECT cr.TEACHING_BUILDING_NUMBER as tbn, count(1) as count ");
            sql.append("FROM (SELECT DISTINCT ct.PLACE FROM (SELECT DISTINCT TEACHING_CLASS_NAME FROM t_curriculum_schedule WHERE 1 = 1");
            if (null != orgId) {
                cql.append(" AND ORG_ID = " + orgId);
                sql.append(" AND ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (weeks != 0) {
                sql.append(" AND START_WEEK <= :startweeks");
                sql.append(" AND END_WEEK >= :endweeks");
                condition.put("startweeks", weeks);
                condition.put("endweeks", weeks);
            }
            if (day != 0) {
                sql.append(" AND DAY_OF_THE_WEEK = :day");
                condition.put("day", day);
            }
            if(jc!=0){
                sql.append(" AND START_PERIOD <= :sjc");
                sql.append(" AND (START_PERIOD + PERIOD_NUM ) >= :ejc");
                condition.put("sjc", jc);
                condition.put("ejc", jc);
            }
            cql.append(" AND TEACHING_BUILDING_NUMBER is not null GROUP BY TEACHING_BUILDING_NUMBER");
            sql.append(" ) cs LEFT JOIN t_course_timetable ct ON cs.TEACHING_CLASS_NAME = ct.TEACHING_CLASS_NAME) p");
            sql.append(" LEFT JOIN t_class_room cr ON cr.CLASSROOM_NAME = p.PLACE");
            sql.append(" WHERE cr.TEACHING_BUILDING_NUMBER IS NOT NULL GROUP BY cr.TEACHING_BUILDING_NUMBER");
            Query cq = em.createNativeQuery(cql.toString());
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            cq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Object> cres = cq.getResultList();
            List<Object> res = sq.getResultList();
            for (Object obj : cres) {
                Map r = (Map) obj;
                TeachingBuildingsUsegeVO tb = new TeachingBuildingsUsegeVO();
                if (null != r.get("tbn")) {
                    tb.setTeachingBuilding(r.get("tbn").toString());
                }
                if (null != r.get("count")) {
                    tb.setToal(Integer.valueOf(r.get("count").toString()));
                }
                dataList.add(tb);
            }
            for (TeachingBuildingsUsegeVO tbList : dataList) {
                for (Object obj : res) {
                    Map row = (Map) obj;
                    if (null != row.get("tbn")) {
                        if (tbList.getTeachingBuilding().equals(row.get("tbn").toString())) {
                            if (null != row.get("count")) {
                                tbList.setUsingNumber(Integer.valueOf(row.get("count").toString()));
                            }
                            break;
                        }

                    }
                }
            }
            result.put("success", true);
            result.put("data", dataList);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取今日教学楼使用情况失败！");
            return result;
        }
    }


}
