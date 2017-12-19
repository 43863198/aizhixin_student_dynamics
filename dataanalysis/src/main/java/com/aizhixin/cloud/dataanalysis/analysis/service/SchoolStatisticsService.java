package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.constant.TrendType;
import com.aizhixin.cloud.dataanalysis.analysis.dto.*;
import com.aizhixin.cloud.dataanalysis.analysis.entity.PracticeStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.CetScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.respository.PracticeStaticsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.respository.SchoolStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.respository.TeachingScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.util.ProportionUtil;

import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import liquibase.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.text.SimpleDateFormat;
import java.util.*;

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

    public void deleteAllByOrgId(Long orgId){
    	schoolStatisticsRespository.deleteByOrgId(orgId);
    }
    
    /**
     * 批量保存学校统计数据
     * @param statisticsList
     */
    public void saveList(List<SchoolStatistics> statisticsList){
    	schoolStatisticsRespository.save(statisticsList);
    }
    
    public void save(SchoolStatistics statistics){
    	schoolStatisticsRespository.save(statistics);
    }
    
    public SchoolStatistics findById(String id){
    	return schoolStatisticsRespository.findOne(id);
    }
    
    
    public Map<String, Object> getStatisticNewstudents(Long orgId, Integer teacherYear) {
        Map<String, Object> result = new HashMap<>();
        NewStudentProfileDTO newStudentProfileDTO = new NewStudentProfileDTO();
        Map<String, Object> condition = new HashMap<>();
        Long count = 0L;
        long studentNumber = 0;
        long alreadyReport = 0;
        int alreadyPay = 0;
        int convenienceChannel = 0;
        Date time = new Date();
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(1) AS count, SUM(ss.NEW_STUDENTS_COUNT) as sum, SUM(ss.ALREADY_REPORT) as rsum, SUM(ss.ALREADY_PAY) as psum, SUM(ss.CONVENIENCE_CHANNEL) as csum, max(ss.STATISTICAL_TIME) FROM T_SCHOOL_STATISTICS ss WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null!=teacherYear) {
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
                    count = Long.valueOf(String.valueOf(d[0]));
                }
                if (null != d[1]) {
                    studentNumber = Integer.valueOf(String.valueOf(d[1]));
                }
                if (null != d[2]) {
                    alreadyReport = Integer.valueOf(String.valueOf(d[2]));
                }
                if (null != d[3]) {
                    alreadyPay = Integer.valueOf(String.valueOf(d[3]));
                }
                if (null != d[4]) {
                    convenienceChannel = Integer.valueOf(String.valueOf(d[4]));
                }
                if (null != d[5]) {
                    time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(d[5]));
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
    public SchoolProfileDTO getSchoolPersonStatistics(Long orgId) {
        String sql="SELECT SEMESTER ,TEACHER_YEAR  FROM `t_practice_statistics` where ORG_ID="+orgId+"  ORDER BY TEACHER_YEAR DESC,SEMESTER DESC LIMIT 1";
        Map currentGradeMap=new HashMap() ;
        try {
            currentGradeMap= jdbcTemplate.queryForMap(sql);
        }catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }
        int teacherYear=Integer.valueOf(currentGradeMap.get("TEACHER_YEAR")+"");
        int semester= Integer.valueOf(currentGradeMap.get("SEMESTER")+"");
        PracticeStaticsDTO practiceStaticsDTO = practiceStaticsRespository.getPracticeStatics(orgId,teacherYear,semester);

        SchoolProfileDTO schoolProfileDTO = schoolStatisticsRespository.getSchoolPersonStatistics(orgId,teacherYear);
        schoolProfileDTO.setOutSchoolStudent(Long.valueOf(practiceStaticsDTO.getPracticeStudentNum()));
        schoolProfileDTO.setInSchoolStudent(Long.valueOf(schoolProfileDTO.getAllStudent()) - Long.valueOf(schoolProfileDTO.getOutSchoolStudent()));
        return schoolProfileDTO;
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

    public Map<String, Object> getTrend(Long orgId, Long collegeId, int typeIndex) {
        Map<String, Object> result = new HashMap<>();
        List<TrendDTO> trendDTOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            String trend = TrendType.getType(typeIndex);
            if (null != trend) {
                if (trend.equals("NEW_STUDENTS_COUNT") || trend.equals("ALREADY_REPORT") || trend.equals("ALREADY_PAY") || trend.equals("CONVENIENCE_CHANNEL")) {
                    StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR, SUM(" + trend + ") FROM T_SCHOOL_STATISTICS  WHERE 1 = 1");
                    if (null != orgId) {
                        sql.append(" and ORG_ID = :orgId");
                        condition.put("orgId", orgId);
                    }
                    if (null != collegeId) {
                        sql.append(" and COLLEGE_ID = :collegeId");
                        condition.put("collegeId", collegeId);
                    }
                    sql.append(" GROUP BY TEACHER_YEAR");
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
                } else if (trend.equals("UNREPORT")) {
                    StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR, SUM(NEW_STUDENTS_COUNT), SUM(ALREADY_REPORT) FROM T_SCHOOL_STATISTICS WHERE 1 = 1");
                    if (null != orgId) {
                        sql.append(" and ORG_ID = :orgId");
                        condition.put("orgId", orgId);
                    }
                    if (null != collegeId) {
                        sql.append(" and COLLEGE_ID = :collegeId");
                        condition.put("collegeId", collegeId);
                    }
                    sql.append(" GROUP BY TEACHER_YEAR");
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
                            if (null != d[1] && null != d[2]) {
                                trendDTO.setValue((Integer.valueOf(String.valueOf(d[1])) - Integer.valueOf(String.valueOf(d[2]))) + "");
                            }
                            trendDTOList.add(trendDTO);
                        }
                    }
                } else if (trend.equals("PAY_PROPORTION")) {
                    StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR, SUM(NEW_STUDENTS_COUNT), SUM(ALREADY_PAY) FROM T_SCHOOL_STATISTICS WHERE 1 = 1");
                    if (null != orgId) {
                        sql.append(" and ORG_ID = :orgId");
                        condition.put("orgId", orgId);
                    }
                    if (null != collegeId) {
                        sql.append(" and COLLEGE_ID = :collegeId");
                        condition.put("collegeId", collegeId);
                    }
                    sql.append(" GROUP BY TEACHER_YEAR");
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
                            if (null != d[1] && null != d[2]) {
                                trendDTO.setValue(ProportionUtil.accuracy(Double.valueOf(String.valueOf(d[2])), Double.valueOf(String.valueOf(d[1])), 2));
                            }
                            trendDTOList.add(trendDTO);
                        }
                    }
                } else if (trend.equals("REPORT_PROPORTION")) {
                    StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR, SUM(NEW_STUDENTS_COUNT), SUM(ALREADY_REPORT) FROM T_SCHOOL_STATISTICS WHERE 1 = 1");
                    if (null != orgId) {
                        sql.append(" and ORG_ID = :orgId");
                        condition.put("orgId", orgId);
                    }
                    if (null != collegeId) {
                        sql.append(" and COLLEGE_ID = :collegeId");
                        condition.put("collegeId", collegeId);
                    }
                    sql.append(" GROUP BY TEACHER_YEAR");
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
                            if (null != d[1] && null != d[2]) {
                                trendDTO.setValue(ProportionUtil.accuracy(Double.valueOf(String.valueOf(d[2])), Double.valueOf(String.valueOf(d[1])), 2));
                            }
                            trendDTOList.add(trendDTO);
                        }
                    }
                } else if (trend.equals("CHANNEL_PROPORTION")) {
                    StringBuilder sql = new StringBuilder("SELECT TEACHER_YEAR, SUM(NEW_STUDENTS_COUNT), SUM(CONVENIENCE_CHANNEL) FROM T_SCHOOL_STATISTICS WHERE 1 = 1");
                    if (null != orgId) {
                        sql.append(" and ORG_ID = :orgId");
                        condition.put("orgId", orgId);
                    }
                    if (null != collegeId) {
                        sql.append(" and COLLEGE_ID = :collegeId");
                        condition.put("collegeId", collegeId);
                    }
                    sql.append(" GROUP BY TEACHER_YEAR");
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
                            if (null != d[1] && null != d[2]) {
                                trendDTO.setValue(ProportionUtil.accuracy(Double.valueOf(String.valueOf(d[2])), Double.valueOf(String.valueOf(d[1])), 2));
                            }
                            trendDTOList.add(trendDTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取分析数据异常！");
            return result;
        }
        result.put("success", true);
        result.put("data", trendDTOList);
        return result;
    }

    /**
     * 实践学情首页统计查询
     *
     * @param orgId
     * @return
     */
    public PracticeStaticsDTO getPracticeStatics(Long orgId) {
        String sql="SELECT SEMESTER ,TEACHER_YEAR  FROM `t_practice_statistics`  where ORG_ID="+orgId+"  ORDER BY TEACHER_YEAR DESC,SEMESTER DESC LIMIT 1";
        Map currentGradeMap=new HashMap() ;
        try {
            currentGradeMap= jdbcTemplate.queryForMap(sql);
        }catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }
        int teacherYear=Integer.valueOf(currentGradeMap.get("TEACHER_YEAR")+"");
        int semester= Integer.valueOf(currentGradeMap.get("SEMESTER")+"");
        return practiceStaticsRespository.getPracticeStatics(orgId,teacherYear,semester);
    }

    /**
     * 四六级学情首页统计查询
     *
     * @param orgId
     * @return
     */
    public CetScoreStatisticsDTO getEctStatics(Long orgId) {
        String sql="SELECT SEMESTER ,TEACHER_YEAR  FROM `t_cet_statistics`  where ORG_ID="+orgId+" ORDER BY TEACHER_YEAR DESC,SEMESTER DESC LIMIT 1";
        Map currentGradeMap=new HashMap() ;
        try {
            currentGradeMap= jdbcTemplate.queryForMap(sql);
        }catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }
        int teacherYear=Integer.valueOf(currentGradeMap.get("TEACHER_YEAR")+"");
        int semester= Integer.valueOf(currentGradeMap.get("SEMESTER")+"");
        return cetScoreStatisticsRespository.getEctStatics(orgId,teacherYear,semester);
    }
    /**
     * 教学成绩首页统计查询
     *
     * @param orgId
     * @return
     */
    public Map<String,Object> getTeachingSoreStatics(Long orgId){
        Map<String,Object> map=new HashMap<String, Object>();
        List<TeachingScoreStatisticsDTO> list0=teachingScoreStatisticsRespository.getAvgTeachingScore(orgId);
        TeachingScoreStatisticsDTO obj=null;
        if (null!=list0&&list0.size()>0){
            obj=list0.get(0);
        }
        String sql="SELECT SEMESTER ,TEACHER_YEAR  FROM `t_teaching_score_statistics`  where ORG_ID="+orgId+" ORDER BY TEACHER_YEAR DESC,SEMESTER DESC LIMIT 1";
        Map currentGradeMap=new HashMap() ;
        try {
            currentGradeMap= jdbcTemplate.queryForMap(sql);
        }catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }
       int teacherYear=Integer.valueOf(currentGradeMap.get("TEACHER_YEAR")+"");
       int semester= Integer.valueOf(currentGradeMap.get("SEMESTER")+"");
        List<TeachingScoreStatisticsDTO> list=teachingScoreStatisticsRespository.getTeachingScoreStatisticsByOrgId(orgId,teacherYear,semester);
        map.put("courseAndAvgScore",obj);
        map.put("collegeAndAvgScore",list);
        return map;
    }


    public Map<String, Object> getCollegeDetails(Pageable page,Long orgId, String collegeId, String nj, String type,String isReport,String isPay) {
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
            if (null != collegeId) {
                Set<Long> collegeIds = new HashSet<>();
                if (collegeId.indexOf(",") != -1) {
                    String[] cid = collegeId.split(",");
                    for (String d : cid) {
                       collegeIds.add(Long.valueOf(d));
                     }
                }else {
                    collegeIds.add(Long.valueOf(collegeId));
                }
                criteria.and("collegeId").in(collegeIds);
            }
            if (null != type) {
                List tds = new ArrayList<>();
                if(type.indexOf(",") != -1) {
                    String[] td = type.split(",");
                    for (String d : td) {
                        tds.add(Integer.valueOf(d));
                    }
                }else {
                    tds.add(Integer.valueOf(type));
                }
                criteria.and("education").in(tds);
            }
            if (null != isReport) {
                criteria.and("isRegister").is(Integer.valueOf(isReport));
            }
            if (null != isPay) {
                List<String> pay = new ArrayList<>();
                if (isPay.indexOf(",") != -1) {
                    String[] py = type.split(",");
                    for (String s : py) {
                        pay.add(s);
                    }
                }else {
                    pay.add(isPay);
                }
                    if (pay.contains("1")) {
                        criteria.and("isPay").is(1);
                    }
                    if (isPay.contains("2")) {
                        criteria.and("isGreenChannel").is(1);
                    }
                    if(isPay.contains("3")) {
                        criteria.and("isPay").nin(1);
                        criteria.and("isGreenChannel").nin(1);
                    }
            }
            if(!org.apache.commons.lang.StringUtils.isBlank(nj)){
                criteria.orOperator(criteria.where("userName").regex(".*?\\" + nj + ".*"), criteria.where("jobNum").is(nj));
            }
            query.addCriteria(criteria);
            //mongoTemplate.count计算总数
            total = mongoTemplate.count(query, StudentRegister.class);
            // mongoTemplate.find 查询结果集
            items = mongoTemplate.find(query.with(pageable), StudentRegister.class);
        }catch (Exception e){
            result.put("success", false);
            result.put("message","获取数据异常！");
        }
        p.getPage().setTotalPages((int)Math.ceil(total/page.getPageSize())+1);
        p.getPage().setPageNumber(page.getPageNumber());
        p.getPage().setPageSize(page.getPageSize());
        p.getPage().setTotalElements(total);
        p.setData(items);
            result.put("success", true);
        result.put("data", p);
        return result;
    }
}
