package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.domain.*;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CetStatisticDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CetTrendDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CollegeCetStatisticDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherYearSemesterDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.CetScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.ScoreTop;
import com.aizhixin.cloud.dataanalysis.analysis.jdbcTemp.CetAvgStatisticalJdbc;
import com.aizhixin.cloud.dataanalysis.analysis.jdbcTemp.CetSexStatisticalJdbc;
import com.aizhixin.cloud.dataanalysis.analysis.respository.CetScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.vo.*;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.common.util.ProportionUtil;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;

import com.mongodb.BasicDBObject;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-07
 */
@Component
@Transactional
public class CetStatisticAnalysisService {

    @Autowired
    private EntityManager em;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CetScoreStatisticsRespository cetScoreStatisticsRespository;
    @Autowired
    private SchoolYearTermService schoolYearTermService;
    @Autowired
    private CetSexStatisticalJdbc cetSexStatisticalJdbc;
    @Autowired
    private CetAvgStatisticalJdbc cetAvgStatisticalJdbc;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void deleteAllByOrgId(Long orgId) {
        cetScoreStatisticsRespository.deleteByOrgId(orgId);
    }

    public void saveList(List<CetScoreStatistics> cetScoreStatisticsList) {
        cetScoreStatisticsRespository.save(cetScoreStatisticsList);
    }

    public void save(CetScoreStatistics cetScoreStatistics) {
        cetScoreStatisticsRespository.save(cetScoreStatistics);
    }

    public CetScoreStatistics findById(String id) {
        return cetScoreStatisticsRespository.findOne(id);
    }


    public Map<String, Object> getStatistic(Long orgId, Integer teacherYear, Integer semester) {
        Map<String, Object> result = new HashMap<>();
        CetStatisticDTO cetStatisticDTO = new CetStatisticDTO();
        Map<String, Object> condition = new HashMap<>();
        List<CollegeCetStatisticDTO> collegeCetStatisticDTOList = new ArrayList<>();
        try {
            StringBuilder cql = new StringBuilder("SELECT SUM(cet.CET_FORE_JOIN_NUM) as sumc4, SUM(cet.CET_FORE_PASS_NUM) as sump4, SUM(cet.CET_SIX_JOIN_NUM) as sumc6, SUM(cet.CET_SIX_PASS_NUM) as sump6, max(cet.CREATED_DATE) FROM T_CET_STATISTICS cet WHERE 1 = 1");
            StringBuilder sql = new StringBuilder("SELECT SUM(cet.CET_FORE_JOIN_NUM) as sumc4, SUM(cet.CET_FORE_PASS_NUM) as sump4, SUM(cet.CET_SIX_JOIN_NUM) as sumc6, SUM(cet.CET_SIX_PASS_NUM) as sump6, COLLEGE_NAME, COLLEGE_ID FROM T_CET_STATISTICS cet WHERE 1 = 1");
            if (null != orgId) {
                cql.append(" and cet.ORG_ID = :orgId");
                sql.append(" and cet.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != teacherYear) {
                cql.append(" and cet.TEACHER_YEAR = :teacherYear");
                sql.append(" and cet.TEACHER_YEAR = :teacherYear");
                condition.put("teacherYear", teacherYear);
            }
            if (null != semester) {
                cql.append(" and cet.SEMESTER = :semester");
                sql.append(" and cet.SEMESTER = :semester");
                condition.put("semester", semester);
            }
            cql.append(" and cet.DELETE_FLAG = 0");
            sql.append(" and cet.DELETE_FLAG = 0");
            sql.append(" GROUP BY cet.COLLEGE_ID");
            Query cq = em.createNativeQuery(cql.toString());
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                cq.setParameter(e.getKey(), e.getValue());
                sq.setParameter(e.getKey(), e.getValue());
            }
            Object[] cres = (Object[]) cq.getSingleResult();
            if (null != cres) {
                int count4 = 0;
                int count6 = 0;
                int pass4 = 0;
                int pass6 = 0;
                Date time = new Date();
                if (null != cres[0]) {
                    count4 = Integer.valueOf(String.valueOf(cres[0]));
                }
                if (null != cres[1]) {
                    pass4 = Integer.valueOf(String.valueOf(cres[1]));
                }
                if (null != cres[2]) {
                    count6 = Integer.valueOf(String.valueOf(cres[2]));
                }
                if (null != cres[3]) {
                    pass6 = Integer.valueOf(String.valueOf(cres[3]));
                }
                if (null != cres[4]) {
                    time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(cres[4]));
                }
                cetStatisticDTO.setCetForeJoinNum(count4);
                cetStatisticDTO.setCetForePassNum(pass4);
                cetStatisticDTO.setCetForePassRate(ProportionUtil.accuracy(pass4 * 1.0, count4 * 1.0, 2));
                cetStatisticDTO.setCetSixJoinNum(count6);
                cetStatisticDTO.setCetSixPassNum(pass6);
                cetStatisticDTO.setCetSixPassRate(ProportionUtil.accuracy(pass6 * 1.0, count6 * 1.0, 2));
                cetStatisticDTO.setStatisticalTime(time);
                List<Object> rd = sq.getResultList();
                if (null != rd && rd.size() > 0) {
                    for (Object obj : rd) {
                        Object[] d = (Object[]) obj;
                        int ccount4 = 0;
                        int ccount6 = 0;
                        int cpass4 = 0;
                        int cpass6 = 0;
                        CollegeCetStatisticDTO collegeCetStatisticDTO = new CollegeCetStatisticDTO();
                        if (null != d[0]) {
                            ccount4 = Integer.valueOf(String.valueOf(d[0]));
                        }
                        if (null != d[1]) {
                            cpass4 = Integer.valueOf(String.valueOf(d[1]));
                        }
                        if (null != d[2]) {
                            ccount6 = Integer.valueOf(String.valueOf(d[2]));
                        }
                        if (null != d[3]) {
                            cpass6 = Integer.valueOf(String.valueOf(d[3]));
                        }
                        if (null != d[4]) {
                            collegeCetStatisticDTO.setCollegeName(String.valueOf(d[4]));
                        }
                        if (null != d[5]) {
                            collegeCetStatisticDTO.setCollegeId(Long.valueOf(String.valueOf(d[5])));
                        }
                        collegeCetStatisticDTO.setCetForeJoinNum(ccount4);
                        collegeCetStatisticDTO.setCetForePassNum(cpass4);
                        collegeCetStatisticDTO.setCetForePassRate(ProportionUtil.accuracy(cpass4 * 1.0, ccount4 * 1.0, 2));
                        collegeCetStatisticDTO.setCetSixJoinNum(ccount6);
                        collegeCetStatisticDTO.setCetSixPassNum(cpass6);
                        collegeCetStatisticDTO.setCetSixPassRate(ProportionUtil.accuracy(cpass6 * 1.0, ccount6 * 1.0, 2));
                        collegeCetStatisticDTOList.add(collegeCetStatisticDTO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取英语四六级统计信息失败！");
            return result;
        }
        result.put("success", true);
        result.put("cetStatisticDTO", cetStatisticDTO);
        result.put("dataList", collegeCetStatisticDTOList);
        return result;
    }


    public Map<String, Object> getCetTrendAnalysis(Long orgId, Long collegeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> condition = new HashMap<>();
            StringBuilder sql = new StringBuilder("SELECT SUM(cet.CET_FORE_JOIN_NUM) as sumc4, SUM(cet.CET_FORE_PASS_NUM) as sump4, SUM(cet.CET_SIX_JOIN_NUM) as sumc6, SUM(cet.CET_SIX_PASS_NUM) as sump6, cet.TEACHER_YEAR, cet.SEMESTER FROM T_CET_STATISTICS cet WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" and cet.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != collegeId) {
                sql.append(" and cet.COLLEGE_ID = :collegeId");
                condition.put("collegeId", collegeId);
            }
            sql.append(" and cet.DELETE_FLAG = 0");
            sql.append(" group by cet.TEACHER_YEAR,cet.SEMESTER");
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();

            List<CetTrendDTO> trendDTOList = new ArrayList<>();
            if (null != res) {
                for (Object obj : res) {
                    Object[] d = (Object[]) obj;
                    CetTrendDTO trendDTO = new CetTrendDTO();
                    int count4 = 0;
                    int count6 = 0;
                    int pass4 = 0;
                    int pass6 = 0;
                    if (null != d[0]) {
                        count4 = Integer.valueOf(String.valueOf(d[0]));
                    }
                    if (null != d[1]) {
                        pass4 = Integer.valueOf(String.valueOf(d[1]));
                    }
                    if (null != d[2]) {
                        count6 = Integer.valueOf(String.valueOf(d[2]));
                    }
                    if (null != d[3]) {
                        pass6 = Integer.valueOf(String.valueOf(d[3]));
                    }
                    if (null != d[4]) {
                        trendDTO.setYear(String.valueOf(d[4]));
                        trendDTO.setYear(String.valueOf(d[4]));
                    }
                    if (null != d[5]) {
                        trendDTO.setSemester(String.valueOf(d[5]));
                        trendDTO.setSemester(String.valueOf(d[5]));
                    }
                    if (count4 != 0) {
                        trendDTO.setCet4PassRate(new DecimalFormat("0.00").format((double) pass4 * 100 / count4));
                    } else {
                        trendDTO.setCet4PassRate(0 + "");
                    }
                    if (count6 != 0) {
                        trendDTO.setCet6PassRate(new DecimalFormat("0.00").format((double) pass6 * 100 / count6));
                    } else {
                        trendDTO.setCet6PassRate(0 + "");
                    }
                    trendDTOList.add(trendDTO);
                }

                if (trendDTOList.size() > 1) {
                    for (int i = 1; i < trendDTOList.size(); i++) {
                        Double change1 = (Double.valueOf(trendDTOList.get(i).getCet4PassRate()) - Double.valueOf(trendDTOList.get(i - 1).getCet4PassRate())
                        ) / Double.valueOf(trendDTOList.get(i - 1).getCet4PassRate());
                        if (null != change1 && !change1.isNaN() && !change1.isInfinite()) {
                            trendDTOList.get(i).setCet4Change(new DecimalFormat("0.00").format(change1 * 100));
                        } else {
                            trendDTOList.get(i).setCet4Change(0 + "");
                        }
                        Double change2 = (Double.valueOf(trendDTOList.get(i).getCet6PassRate()) - Double.valueOf(trendDTOList.get(i - 1).getCet6PassRate())
                        ) / Double.valueOf(trendDTOList.get(i - 1).getCet6PassRate());
                        if (null != change2 && !change2.isNaN() && !change2.isInfinite()) {
                            trendDTOList.get(i).setCet6Change(new DecimalFormat("0.00").format(change2 * 100));
                        } else {
                            trendDTOList.get(i).setCet6Change(0 + "");
                        }
                    }
                }
            }
            result.put("dataList", trendDTOList);
            result.put("success", true);
            return result;

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取统计分析数据失败！");
            return result;
        }
    }


    public Map<String, Object> getCetDetail(Long orgId, String collegeId, Integer teacherYear, Integer semester, String grade, Integer type, String nj, Pageable page) {
        Map<String, Object> result = new HashMap<>();
        PageData<Score> p = new PageData<>();
        try {
            //创建排序模板Sort
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            //创建分页模板Pageable
            Pageable pageable = new PageRequest(page.getPageNumber(), page.getPageSize(), sort);
            //条件
            Criteria criteria = Criteria.where("orgId").is(orgId);

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

            if (null != teacherYear) {
                criteria.and("schoolYear").is(teacherYear);
            }
            if (null != semester) {
                criteria.and("semester").is(semester);
            }
            if (null != grade) {
                List<String> tds = new ArrayList<>();
                if (grade.indexOf(",") != -1) {
                    String[] td = grade.split(",");
                    for (String d : td) {
                        tds.add(d);
                    }
                } else {
                    tds.add(grade);
                }
                criteria.and("grade").in(tds);
            }
            if (null != type) {
                if (type.equals(4)) {
                    criteria.and("examType").is(ScoreConstant.EXAM_TYPE_CET4);
                    criteria.and("totalScore").gte(ScoreConstant.CET_PASS_SCORE_LINE);
                } else if (type.equals(6)) {
                    criteria.and("examType").is(ScoreConstant.EXAM_TYPE_CET6);
                    criteria.and("totalScore").gte(ScoreConstant.CET_PASS_SCORE_LINE);
                }
            } else {
                criteria.and("examType").in(ScoreConstant.EXAM_TYPE_CET4, ScoreConstant.EXAM_TYPE_CET6);
            }
            if (!org.apache.commons.lang.StringUtils.isBlank(nj)) {
                criteria.orOperator(criteria.where("jobNum").regex(nj), criteria.where("userName").regex(nj));
            }
            org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();
            query.addCriteria(criteria);
            //mongoTemplate.count计算总数
            long total = mongoTemplate.count(query, Score.class);
            // mongoTemplate.find 查询结果集
            List<Score> items = mongoTemplate.find(query.with(pageable), Score.class);

            p.getPage().setTotalPages(((int) total + page.getPageSize() - 1) / page.getPageSize());
            p.getPage().setPageNumber(page.getPageNumber() + 1);
            p.getPage().setPageSize(page.getPageSize());
            p.getPage().setTotalElements(total);
            p.setData(items);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取数据异常！");
            return result;
        }
        result.put("success", true);
        result.put("data", p);
        return result;
    }

    public void deleteCetStatistics(Long orgId, Integer teacherYear, Integer semester) {
        cetScoreStatisticsRespository.deleteByOrgIdAndTeacherYearAndSemester(orgId, teacherYear, semester);
    }


    public Map<String, Object> getYearAvg(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        List<CetAvgVO> cetAvgVOList = new ArrayList<>();
        try {
            //四级分数均值
            Criteria cet4 = Criteria.where("orgId").is(orgId);
            cet4.and("examType").is(ScoreConstant.EXAM_TYPE_CET4);
            cet4.and("totalScore").gt(0);
            AggregationResults<BasicDBObject> cet4avg = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(cet4),
                            Aggregation.group("schoolYear").avg("totalScore").as("avg").first("schoolYear").as("schoolYear"),
                            Aggregation.sort(Sort.Direction.ASC, "schoolYear")
                    ), Score.class, BasicDBObject.class);
            //英语六级分数均值
            Criteria cet6 = Criteria.where("orgId").is(orgId);
            cet6.and("examType").is(ScoreConstant.EXAM_TYPE_CET6);
            cet6.and("totalScore").gt(0);
            AggregationResults<BasicDBObject> cet6avg = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(cet6),
                            Aggregation.group("schoolYear").avg("totalScore").as("avg").first("schoolYear").as("schoolYear"),
                            Aggregation.sort(Sort.Direction.ASC, "schoolYear")
                    ), Score.class, BasicDBObject.class);
            if (null != cet4avg) {
                for (int x = 0; x < cet4avg.getMappedResults().size(); x++) {
                    CetAvgVO cet = new CetAvgVO();
                    cet.setYear(cet4avg.getMappedResults().get(x).getString("schoolYear"));
                    cet.setCet4Avg(Double.valueOf(new DecimalFormat("0.").format(cet4avg.getMappedResults().get(x).getDouble("avg"))));
                    cetAvgVOList.add(cet);
                }
            }
            if (null != cet6avg) {
                for (int y = 0; y < cet6avg.getMappedResults().size(); y++) {
                    for (CetAvgVO ca : cetAvgVOList) {
                        if (ca.getYear().equals(cet6avg.getMappedResults().get(y).getString("schoolYear"))) {
                            ca.setCet6Avg(Double.valueOf(new DecimalFormat("0.").format(cet6avg.getMappedResults().get(y).getDouble("avg"))));
                            break;
                        }
                    }
                }
            }
            result.put("success", true);
            result.put("data", cetAvgVOList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "历年四六级均值失败！");
            return result;
        }
    }

//    public Map<String, Object> cet4SingleDataStatistics(Long orgId,Integer teacherYear,Integer semester,Long collegeId,Long professionId,Long classId) {
//        Map<String, Object> result = new HashMap<>();
//        CetSingleDataStatisticsVO csdsVO = new CetSingleDataStatisticsVO();
//        try{
//            //四级单次统计
//            Criteria cet4 = Criteria.where("orgId").is(orgId);
//            cet4.and("examType").is(ScoreConstant.EXAM_TYPE_CET4);
//            if(null!=teacherYear&&teacherYear.intValue()>0){
//                cet4.and("schoolYear").is(teacherYear);
//            }
//            if(null!=semester&&semester.intValue()>0){
//                cet4.and("semester").is(semester);
//            }
//            if(null!=collegeId){
//                cet4.and("collegeId").is(collegeId);
//            }
//            if(null!=professionId){
//                cet4.and("professionalId").is(professionId);
//            }
//            if(null!=classId){
//                cet4.and("classId").is(classId);
//            }
//
//            AggregationResults<BasicDBObject> cet = mongoTemplate.aggregate(
//                    Aggregation.newAggregation(
//                            Aggregation.match(cet4),
//                            Aggregation.group("orgId").count().as("count").avg("totalScore").as("avg").max("totalScore").as("max")
//                    ), Score.class, BasicDBObject.class);
//
//            cet4.and("totalScore").gt(ScoreConstant.CET_PASS_SCORE_LINE);
//            AggregationResults<BasicDBObject> cetPass = mongoTemplate.aggregate(
//                    Aggregation.newAggregation(
//                            Aggregation.match(cet4),
//                            Aggregation.group("orgId").count().as("pass")),
//                    Score.class, BasicDBObject.class);
//            if (null != cet&&cet.getMappedResults().size()>0) {
//                Map d = (Map)cet.getUniqueMappedResult().get(0);
//                if(null !=d.get("count")) {
//                    csdsVO.setJoinNumber(Integer.valueOf(d.get("count").toString()));
//                }
//                if(null!=d.get("avg")){
//                    csdsVO.setAverage(Float.valueOf(d.get("avg").toString()));
//                }
//                if(null!=d.get("max")){
//                    csdsVO.setHighestScore(Float.valueOf(d.get("max").toString()));
//                }
//            }
//            if (null != cetPass&&cetPass.getMappedResults().size()>0) {
//                Map p = (Map)cetPass.getUniqueMappedResult().get(0);
//                if(null!=p.get("pass")){
//                    csdsVO.setPassNumber(Integer.valueOf(p.get("pass").toString()));
//                }
//            }
//            if(csdsVO.getJoinNumber()>0){
//               String rate = new DecimalFormat("0.00").format(csdsVO.getPassNumber()*100 / csdsVO.getJoinNumber());
//                csdsVO.setPassRate(rate);
//            }
//
//            result.put("success",true);
//            result.put("data",csdsVO);
//            return result;
//        }catch (Exception e){
//            result.put("success",false);
//            result.put("message","英语四级考试单次数据分析---数据统计失败！");
//            return result;
//        }
//    }

    public Map<String, Object> cetSingleDataStatistics(Long orgId, String teacherYear, String semester, String collegeCode, String professionCode, String classCode,String className, String cetType) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        CetSingleDataStatisticsVO csdsVO = new CetSingleDataStatisticsVO();
        try {
            Date start = null;
            Date end = null;
            Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teacherYear, semester);
            if (null != schoolCalendar) {
                if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                    List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                    if (tysList.size() > 0) {
                        Date date = sdf.parse(tysList.get(0).getStartTime());
                        int week = tysList.get(0).getWeek();
                        start = date;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(start);
                        calendar.add(Calendar.WEEK_OF_YEAR, week+1);
                        end = calendar.getTime();
                    }
                }
            }

            StringBuilder sql = new StringBuilder("SELECT ss.JOIN_NUMBER as count,ss.PASS_NUMBER as pass, ss.AVG_SCORE as avg, ss.MAX_SCORE as max FROM t_score_statistics ss WHERE 1=1");
            if (null != orgId) {
                sql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != start && null != end) {
                sql.append(" and ss.EXAMINATION_DATE BETWEEN :start AND :end");
                condition.put("start", start);
                condition.put("end", end);
            }
            if (!StringUtils.isBlank(cetType)) {
                sql.append(" and ss.SCORE_TYPE LIKE :cetType");
                condition.put("cetType", "%大学英语" + cetType + "%");
            }
            if (!StringUtils.isBlank(collegeCode)) {
                if (!StringUtils.isBlank(professionCode)) {
                    if (!StringUtils.isBlank(classCode)) {
                        sql.append(" and ss.CODE = :classCode");
                        condition.put("classCode", classCode);
                        sql.append(" and ss.CLASS_NAME = :className");
                        condition.put("className", className);
                        sql.append(" and ss.STATISTICS_TYPE = '003'");
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", professionCode);
                    }else {
                        sql.append(" and ss.CODE = :professionCode");
                        condition.put("professionCode", professionCode);
                        sql.append(" and ss.STATISTICS_TYPE = '002'");
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", collegeCode);
                    }
                }else {
                    sql.append(" and ss.CODE = :collegeCode");
                    condition.put("collegeCode", collegeCode);
                    sql.append(" and ss.STATISTICS_TYPE = '001'");
                    sql.append(" and ss.PARENT_CODE = :pCode");
                    condition.put("pCode", orgId);
                }
            }else {
                sql.append(" and ss.CODE = :orgCode");
                condition.put("orgCode", orgId);
                sql.append(" and ss.STATISTICS_TYPE = '000'");
                sql.append(" and ss.PARENT_CODE = :pCode");
                condition.put("pCode", orgId);
            }
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for(Object obj: res) {
                    Map d = (Map) obj;
                    if (null != d.get("count")) {
                        csdsVO.setJoinNumber(Integer.valueOf(d.get("count").toString()));
                    }
                    if (null != d.get("avg")) {
                        csdsVO.setAverage(Math.round(Float.valueOf(d.get("avg").toString())) + "");
                    }
                    if (null != d.get("max")) {
                        csdsVO.setHighestScore(Math.round(Float.valueOf(d.get("max").toString())) + "");
                    }
                    if (null != d.get("pass")) {
                        csdsVO.setPassNumber(Integer.valueOf(d.get("pass").toString()));
                    }
                }

            }
            if (csdsVO.getJoinNumber() > 0) {
                String rate = new DecimalFormat("0.00").format((double) csdsVO.getPassNumber() * 100 / csdsVO.getJoinNumber());
                csdsVO.setPassRate(rate);
            }
            result.put("success", true);
            result.put("data", csdsVO);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "英语考试单次数据分析---数据统计失败！");
            return result;
        }
    }


    public Map<String, Object> cetSingleDataAvgScoure(Long orgId, String teacherYear, String semester, String collegeCode, String professionCode, String classCode, String className, String cetType) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<CetScoreAnalysisVO> csaList = new ArrayList<>();
        try {
            Date start = null;
            Date end = null;
            Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teacherYear, semester);
            if (null != schoolCalendar) {
                if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                    List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                    if (tysList.size() > 0) {
                        Date date = sdf.parse(tysList.get(0).getStartTime());
                        int week = tysList.get(0).getWeek();
                        start = date;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(start);
                        calendar.add(Calendar.WEEK_OF_YEAR, week+1);
                        end = calendar.getTime();
                    }
                }
            }
            StringBuilder ql = new StringBuilder("");
            if (null != orgId) {
                ql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != start && null != end) {
                ql.append(" and ss.EXAMINATION_DATE BETWEEN :start AND :end");
                condition.put("start", start);
                condition.put("end", end);
            }
            if (!StringUtils.isBlank(cetType)) {
                ql.append(" and ss.SCORE_TYPE LIKE :cetType");
                condition.put("cetType", "%大学英语" + cetType + "%");
            }
            StringBuilder sql = new StringBuilder("");
            if (!StringUtils.isBlank(collegeCode)) {
                if (!StringUtils.isBlank(professionCode)) {
                    if (!StringUtils.isBlank(classCode)) {
                        sql.append("SELECT ss.CLASS_NAME as name, ss.AVG_SCORE as avg FROM t_score_statistics ss WHERE 1=1 AND ss.STATISTICS_TYPE = '003'");
                        sql.append(ql);
                        sql.append(" AND ss.STATISTICS_TYPE = '003'");
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", professionCode);
                        sql.append(" and ss.CODE = :classCode");
                        condition.put("classCode", classCode);
                        sql.append(" and ss.CLASS_NAME = :className");
                        condition.put("className", className);
                        sql.append(" AND ss.CLASS_NAME IS NOT NULL");
                    }else {
                        sql.append("SELECT ss.CLASS_NAME as name, ss.AVG_SCORE as avg FROM t_score_statistics ss WHERE 1=1 AND ss.STATISTICS_TYPE = '003'");
                        sql.append(ql);
                        sql.append(" AND ss.STATISTICS_TYPE = '003'");
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", professionCode);
                        sql.append(" AND ss.CLASS_NAME IS NOT NULL");
                    }
                } else {
                    sql.append("SELECT p.NAME as name, ss.AVG_SCORE as avg FROM t_score_statistics ss LEFT JOIN t_profession p ON ss.CODE = p.CODE WHERE 1=1");
                    sql.append(ql);
                    sql.append(" AND ss.STATISTICS_TYPE = '002'");
                    sql.append(" and ss.PARENT_CODE = :pCode");
                    condition.put("pCode", collegeCode);
                    sql.append(" AND p.NAME IS NOT NULL");
                }
            } else {
                sql.append("SELECT d.COMPANY_NAME as name, ss.AVG_SCORE as avg FROM t_score_statistics ss LEFT JOIN t_department d ON ss.CODE = d.COMPANY_NUMBER WHERE 1=1");
                sql.append(ql);
                sql.append(" AND ss.STATISTICS_TYPE = '001'");
                sql.append(" and ss.PARENT_CODE = :pCode");
                condition.put("pCode", orgId);
                sql.append(" AND d.COMPANY_NAME IS NOT NULL");
            }
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object row : res) {
                    Map d = (Map) row;
                    CetScoreAnalysisVO csa = new CetScoreAnalysisVO();
                    if (null != d.get("name")) {
                        csa.setName(d.get("name").toString());
                    }
                    if (null != d.get("avg")) {
                        csa.setValue(Math.round(Float.valueOf(d.get("avg").toString())) + "");
                    }
                    csaList.add(csa);
                }
            }
            result.put("success", true);
            result.put("data", csaList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "英语考试单次数据分析---均值分布---按行政单位统计失败！");
            return result;
        }
    }


    public Map<String, Object> cetSingleDataSexAvgScoure(Long orgId, String teacherYear, String semester, String collegeCode, String professionCode, String classCode,String className, String cetType) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<CetScoreAnalysisVO> csaList = new ArrayList<>();
        try {
            Date start = null;
            Date end = null;
            Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teacherYear, semester);
            if (null != schoolCalendar) {
                if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                    List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                    if (tysList.size() > 0) {
                        Date date = sdf.parse(tysList.get(0).getStartTime());
                        int week = tysList.get(0).getWeek();
                        start = date;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(start);
                        calendar.add(Calendar.WEEK_OF_YEAR, week+1);
                        end = calendar.getTime();
                    }
                }
            }
            StringBuilder ql = new StringBuilder("");
            if (null != orgId) {
                ql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != start && null != end) {
                ql.append(" and ss.EXAMINATION_DATE BETWEEN :start AND :end");
                condition.put("start", start);
                condition.put("end", end);
            }
            if (!StringUtils.isBlank(cetType)) {
                ql.append(" and ss.SCORE_TYPE LIKE :cetType");
                condition.put("cetType", "%大学英语" + cetType + "%");
            }
            StringBuilder sql = new StringBuilder("");
            if (!StringUtils.isBlank(collegeCode)) {
                if (!StringUtils.isBlank(professionCode)) {
                    if (!StringUtils.isBlank(classCode)) {
                            sql.append("SELECT ss.NAME_CODE as sex, ss.AVG_SCORE as avg FROM t_score_statistics ss  WHERE 1=1");
                            sql.append(ql);
                            sql.append(" AND ss.STATISTICS_TYPE = '013'");
                            sql.append(" and ss.CODE = :classCode");
                            condition.put("classCode", classCode);
                            sql.append(" and ss.CLASS_NAME = :className");
                            condition.put("className", className);
                            sql.append(" and ss.PARENT_CODE = :pCode");
                            condition.put("pCode", professionCode);
                            sql.append(" AND ss.NAME_CODE IS NOT NULL");
                    } else {
                        sql.append("SELECT ss.NAME_CODE as sex, ss.AVG_SCORE as avg FROM t_score_statistics ss WHERE 1=1");
                        sql.append(ql);
                        sql.append(" AND ss.STATISTICS_TYPE = '012'");
                        sql.append(" and ss.CODE = :professionCode");
                        condition.put("professionCode", professionCode);
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", collegeCode);
                        sql.append(" AND ss.NAME_CODE IS NOT NULL");
                    }
                } else {
                    sql.append("SELECT ss.NAME_CODE as sex, ss.AVG_SCORE as avg FROM t_score_statistics ss WHERE 1=1");
                    sql.append(ql);
                    sql.append(" AND ss.STATISTICS_TYPE = '011'");
                    sql.append(" and ss.CODE = :collegeCode");
                    condition.put("collegeCode", collegeCode);
                    sql.append(" and ss.PARENT_CODE = :pCode");
                    condition.put("pCode", orgId);
                    sql.append(" AND ss.NAME_CODE IS NOT NULL");
                }
            } else {
                sql.append("SELECT ss.NAME_CODE as sex, ss.AVG_SCORE as avg FROM t_score_statistics ss WHERE 1=1");
                sql.append(ql);
                sql.append(" AND ss.STATISTICS_TYPE = '010'");
                sql.append(" AND ss.NAME_CODE IS NOT NULL");
            }
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object row : res) {
                    Map d = (Map) row;
                    CetScoreAnalysisVO csa = new CetScoreAnalysisVO();
                    if (null != d.get("sex")) {
                        csa.setClassify(d.get("sex").toString());
                    }
                    if (null != d.get("avg")) {
                        csa.setValue(Math.round(Float.valueOf(d.get("avg").toString())) + "");
                    }
                    csaList.add(csa);
                }
            }
            result.put("success", true);
            result.put("data", csaList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "英语考试单次数据分析---均值分布---按男女计失败！");
            return result;
        }
    }


    public Map<String, Object> cetSingleDataGradeNumberOfPeople(Long orgId, String teacherYear, String semester, String collegeCode, String professionCode, String classCode, String className, String grade, String cetType) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<CetScoreNumberOfPeopleVO> cnpList = new ArrayList<>();
        try {
            Date start = null;
            Date end = null;
            Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teacherYear, semester);
            if (null != schoolCalendar) {
                if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                    List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                    if (tysList.size() > 0) {
                        Date date = sdf.parse(tysList.get(0).getStartTime());
                        int week = tysList.get(0).getWeek();
                        start = date;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(start);
                        calendar.add(Calendar.WEEK_OF_YEAR, week+1);
                        end = calendar.getTime();
                    }
                }
            }
            StringBuilder ql = new StringBuilder("");
            if (null != orgId) {
                ql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != start && null != end) {
                ql.append(" and ss.EXAMINATION_DATE BETWEEN :start AND :end");
                condition.put("start", start);
                condition.put("end", end);
            }
            if (!StringUtils.isBlank(cetType)) {
                ql.append(" and ss.SCORE_TYPE LIKE :cetType");
                condition.put("cetType", "%大学英语" + cetType + "%");
            }
            if (!StringUtils.isBlank(grade)) {
                ql.append(" and ss.GRADE LIKE :grade");
                condition.put("grade", "%" + grade + "%");
            }
            StringBuilder sql = new StringBuilder("");
            if (!StringUtils.isBlank(collegeCode)) {
                if (!StringUtils.isBlank(professionCode)) {
                    if (!StringUtils.isBlank(classCode)) {
                        sql.append("SELECT ss.NAME_CODE as grade, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss WHERE 1=1");
                        sql.append(ql);
                        sql.append(" AND ss.STATISTICS_TYPE = '023'");
                        sql.append(" and ss.CODE = :classCode");
                        condition.put("classCode", classCode);
                        sql.append(" and ss.CLASS_NAME = :className");
                        condition.put("className", className);
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", professionCode);
                        sql.append(" AND ss.NAME_CODE IS NOT NULL");
                        sql.append(" order by grade ASC");
                    } else {
                        sql.append("SELECT ss.NAME_CODE as grade, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss WHERE 1=1");
                        sql.append(ql);
                        sql.append(" AND ss.STATISTICS_TYPE = '022'");
                        sql.append(" and ss.CODE = :professionCode");
                        condition.put("professionCode", professionCode);
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", collegeCode);
                        sql.append(" AND ss.NAME_CODE IS NOT NULL");
                        sql.append(" order by grade ASC");
                    }
                } else {
                    sql.append("SELECT ss.NAME_CODE as grade, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss WHERE 1=1");
                    sql.append(ql);
                    sql.append(" AND ss.STATISTICS_TYPE = '021'");
                    sql.append(" and ss.CODE = :collegeCode");
                    condition.put("collegeCode", collegeCode);
                    sql.append(" and ss.PARENT_CODE = :pCode");
                    condition.put("pCode", orgId);
                    sql.append(" AND ss.NAME_CODE IS NOT NULL");
                    sql.append(" order by grade ASC");
                }
            } else {
                sql.append("SELECT ss.NAME_CODE as grade, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss WHERE 1=1");
                sql.append(ql);
                sql.append(" AND ss.NAME_CODE IS NOT NULL");
                sql.append(" AND ss.STATISTICS_TYPE = '020'");
                sql.append(" order by grade ASC");
            }
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object row : res) {
                    Map d = (Map) row;
                    CetScoreNumberOfPeopleVO cnp = new CetScoreNumberOfPeopleVO();
                    if (null != d.get("grade")) {
                        cnp.setName(d.get("grade").toString());
                    }
                    if (null != d.get("total")) {
                        cnp.setJoinNumber(Integer.valueOf(d.get("total").toString()));
                    }
                    if (null != d.get("pass")) {
                        cnp.setPassNumber(Integer.valueOf(d.get("pass").toString()));
                    }
                    cnpList.add(cnp);
                }
            }
            result.put("success", true);
            result.put("data", cnpList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "英语考试单次数据分析---均值分布---按年级统计失败！");
            return result;
        }
    }


    public Map<String, Object> cetSingleDataNumberOfPeople(Long orgId, String teacherYear, String semester, String collegeCode, String professionCode, String classCode,String className, String cetType) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<CetScoreNumberOfPeopleVO> csnpList = new ArrayList<>();
        try {
            Date start = null;
            Date end = null;
            Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teacherYear, semester);
            if (null != schoolCalendar) {
                if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                    List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                    if (tysList.size() > 0) {
                        Date date = sdf.parse(tysList.get(0).getStartTime());
                        int week = tysList.get(0).getWeek();
                        start = date;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(start);
                        calendar.add(Calendar.WEEK_OF_YEAR, week+1);
                        end = calendar.getTime();
                    }
                }
            }
            StringBuilder ql = new StringBuilder("");
            if (null != orgId) {
                ql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != start && null != end) {
                ql.append(" and ss.EXAMINATION_DATE BETWEEN :start AND :end");
                condition.put("start", start);
                condition.put("end", end);
            }
            if (!StringUtils.isBlank(cetType)) {
                ql.append(" and ss.SCORE_TYPE LIKE :cetType");
                condition.put("cetType", "%大学英语" + cetType + "%");
            }

            StringBuilder sql = new StringBuilder("");
            if (!StringUtils.isBlank(collegeCode)) {
                if (!StringUtils.isBlank(professionCode)) {
                    if (!StringUtils.isBlank(classCode)) {
                        sql.append("SELECT ss.CLASS_NAME as name, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss WHERE 1=1 AND ss.STATISTICS_TYPE = '003'");
                        sql.append(ql);
                        sql.append(" AND ss.STATISTICS_TYPE = '003'");
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", professionCode);
                        sql.append(" and ss.CODE = :classCode");
                        condition.put("classCode", classCode);
                        sql.append(" and ss.CLASS_NAME = :className");
                        condition.put("className", className);
                        sql.append(" AND ss.CLASS_NAME IS NOT NULL");
                    }else {
                        sql.append("SELECT ss.CLASS_NAME as name, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss  WHERE 1=1 AND ss.STATISTICS_TYPE = '003'");
                        sql.append(ql);
                        sql.append(" AND ss.STATISTICS_TYPE = '003'");
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", professionCode);
                        sql.append(" AND ss.CLASS_NAME IS NOT NULL");
                }
                } else {
                    sql.append("SELECT p.NAME as name, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss LEFT JOIN t_profession p ON ss.CODE = p.CODE WHERE 1=1");
                    sql.append(ql);
                    sql.append(" AND ss.STATISTICS_TYPE = '002'");
                    sql.append(" and ss.PARENT_CODE = :pCode");
                    condition.put("pCode", collegeCode);
                    sql.append(" AND P.NAME IS NOT NULL");
                }
            } else {
                sql.append("SELECT d.COMPANY_NAME as name, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss LEFT JOIN t_department d ON ss.CODE = d.COMPANY_NUMBER WHERE 1=1");
                sql.append(ql);
                sql.append(" AND ss.STATISTICS_TYPE = '001'");
                sql.append(" and ss.PARENT_CODE = :pCode");
                condition.put("pCode", orgId);
                sql.append(" AND d.COMPANY_NAME IS NOT NULL");
            }
             Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object row : res) {
                    Map d = (Map) row;
                    CetScoreNumberOfPeopleVO csnp = new CetScoreNumberOfPeopleVO();
                    if (null != d.get("name")) {
                        csnp.setName(d.get("name").toString());
                    }
                    if (null != d.get("total")) {
                        csnp.setJoinNumber(Integer.valueOf(d.get("total").toString()));
                    }
                    if (null != d.get("pass")) {
                        csnp.setPassNumber(Integer.valueOf(d.get("pass").toString()));
                    }
                    csnpList.add(csnp);
                }
            }
            result.put("success", true);
            result.put("data", csnpList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "英语考试单次数据分析---人数分布---按行政单位统计失败！");
            return result;
        }
    }

    public Map<String, Object> cetSingleDataSexNumberOfPeople(Long orgId, String teacherYear, String semester, String collegeCode, String professionCode, String classCode, String className,String grade, String cetType) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<CetScoreNumberOfPeopleVO> csnpList = new ArrayList<>();
        try {
            Date start = null;
            Date end = null;
            Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teacherYear, semester);
            if (null != schoolCalendar) {
                if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                    List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                    if (tysList.size() > 0) {
                        Date date = sdf.parse(tysList.get(0).getStartTime());
                        int week = tysList.get(0).getWeek();
                        start = date;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(start);
                        calendar.add(Calendar.WEEK_OF_YEAR, week+1);
                        end = calendar.getTime();
                    }
                }
            }
            StringBuilder ql = new StringBuilder("");
            if (null != orgId) {
                ql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != start && null != end) {
                ql.append(" and ss.EXAMINATION_DATE BETWEEN :start AND :end");
                condition.put("start", start);
                condition.put("end", end);
            }
            if (!StringUtils.isBlank(cetType)) {
                ql.append(" and ss.SCORE_TYPE LIKE :cetType");
                condition.put("cetType", "%大学英语" + cetType + "%");
            }
            if (!StringUtils.isBlank(grade)) {
                ql.append(" and ss.GRADE LIKE :grade");
                condition.put("grade", "%" + grade + "%");
            }
            StringBuilder sql = new StringBuilder("");
            if (!StringUtils.isBlank(collegeCode)) {
                if (!StringUtils.isBlank(professionCode)) {
                    if (!StringUtils.isBlank(classCode)) {
                        sql.append("SELECT ss.NAME_CODE as sex, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss WHERE 1=1");
                        sql.append(ql);
                        sql.append("  AND ss.STATISTICS_TYPE = '013'");
                        sql.append(" and ss.CODE = :classCode");
                        condition.put("classCode", classCode);
                        sql.append(" and ss.CLASS_NAME = :className");
                        condition.put("className", className);
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", professionCode);
                        sql.append(" AND ss.NAME_CODE IS NOT NULL");
                    } else {
                        sql.append("SELECT ss.NAME_CODE as sex, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss  WHERE 1=1");
                        sql.append(ql);
                        sql.append(" AND ss.STATISTICS_TYPE = '012'");
                        sql.append(" and ss.CODE = :professionCode");
                        condition.put("professionCode", professionCode);
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", collegeCode);
                        sql.append(" AND ss.NAME_CODE IS NOT NULL");
                    }
                } else {
                    sql.append("SELECT ss.NAME_CODE as sex, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss WHERE 1=1");
                    sql.append(ql);
                    sql.append(" AND ss.STATISTICS_TYPE = '011'");
                    sql.append(" and ss.CODE = :collegeCode");
                    condition.put("collegeCode", collegeCode);
                    sql.append(" and ss.PARENT_CODE = :pCode");
                    condition.put("pCode", orgId);
                    sql.append(" AND ss.NAME_CODE IS NOT NULL");
                }
            } else {
                sql.append("SELECT ss.NAME_CODE as sex, ss.JOIN_NUMBER as total, ss.PASS_NUMBER as pass FROM t_score_statistics ss WHERE 1=1");
                sql.append(ql);
                sql.append(" AND ss.NAME_CODE IS NOT NULL");
                sql.append(" AND ss.STATISTICS_TYPE = '010'");
            }
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object row : res) {
                    Map d = (Map) row;
                    CetScoreNumberOfPeopleVO csnp = new CetScoreNumberOfPeopleVO();
                    if (null != d.get("sex")) {
                        csnp.setClassify(d.get("sex").toString());
                    }
                    if (null != d.get("total")) {
                        csnp.setJoinNumber(Integer.valueOf(d.get("total").toString()));
                    }
                    if (null != d.get("pass")) {
                        csnp.setPassNumber(Integer.valueOf(d.get("pass").toString()));
                    }
                    csnpList.add(csnp);
                }
            }
            result.put("success", true);
            result.put("data", csnpList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "英语考试单次数据分析---人数分布---按性别统计失败！");
            return result;
        }
    }


    public Map<String, Object> cetSingleDataGradeAvgScoure(Long orgId, String teacherYear, String semester, String collegeCode, String professionCode, String classCode,String className, String cetType) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<CetScoreAnalysisVO> csaList = new ArrayList<>();
        try {
            Date start = null;
            Date end = null;
            Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teacherYear, semester);
            if (null != schoolCalendar) {
                if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                    List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                    if (tysList.size() > 0) {
                        Date date = sdf.parse(tysList.get(0).getStartTime());
                        int week = tysList.get(0).getWeek();
                        start = date;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(start);
                        calendar.add(Calendar.WEEK_OF_YEAR, week+1);
                        end = calendar.getTime();
                    }
                }
            }
            StringBuilder ql = new StringBuilder("");
            if (null != orgId) {
                ql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != start && null != end) {
                ql.append(" and ss.EXAMINATION_DATE BETWEEN :start AND :end");
                condition.put("start", start);
                condition.put("end", end);
            }
            if (!StringUtils.isBlank(cetType)) {
                ql.append(" and ss.SCORE_TYPE LIKE :cetType");
                condition.put("cetType", "%大学英语" + cetType + "%");
            }
            StringBuilder sql = new StringBuilder("");
            if (!StringUtils.isBlank(collegeCode)) {
                if (!StringUtils.isBlank(professionCode)) {
                    if (!StringUtils.isBlank(classCode)) {
                        sql.append("SELECT ss.NAME_CODE as grade, ss.AVG_SCORE as avg FROM t_score_statistics ss WHERE 1=1");
                        sql.append(ql);
                        sql.append(" AND ss.STATISTICS_TYPE = '023'");
                        sql.append(" and ss.CODE = :classCode");
                        condition.put("classCode", classCode);
                        sql.append(" and ss.CLASS_NAME = :className");
                        condition.put("className", className);
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", professionCode);
                        sql.append(" AND ss.NAME_CODE IS NOT NULL");
                        sql.append(" order by grade ASC");
                    } else {
                        sql.append("SELECT ss.NAME_CODE as grade, ss.AVG_SCORE as avg FROM t_score_statistics ss WHERE 1=1");
                        sql.append(ql);
                        sql.append(" AND ss.STATISTICS_TYPE = '022'");
                        sql.append(" and ss.CODE = :professionCode");
                        condition.put("professionCode", professionCode);
                        sql.append(" and ss.PARENT_CODE = :pCode");
                        condition.put("pCode", collegeCode);
                        sql.append(" AND ss.NAME_CODE IS NOT NULL");
                        sql.append(" order by grade ASC");
                    }
                } else {
                    sql.append("SELECT ss.NAME_CODE as grade, ss.AVG_SCORE as avg FROM t_score_statistics ss WHERE 1=1");
                    sql.append(ql);
                    sql.append(" AND ss.STATISTICS_TYPE = '021'");
                    sql.append(" and ss.CODE = :collegeCode");
                    condition.put("collegeCode", collegeCode);
                    sql.append(" and ss.PARENT_CODE = :pCode");
                    condition.put("pCode", orgId);
                    sql.append(" AND ss.NAME_CODE IS NOT NULL");
                    sql.append(" order by grade ASC");
                }
            } else {
                sql.append("SELECT ss.NAME_CODE as grade, ss.AVG_SCORE as avg FROM t_score_statistics ss WHERE 1=1");
                sql.append(ql);
                sql.append(" AND ss.NAME_CODE IS NOT NULL");
                sql.append(" AND ss.STATISTICS_TYPE = '020'");
                sql.append(" order by grade ASC");
            }
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object row : res) {
                    Map d = (Map) row;
                    CetScoreAnalysisVO csa = new CetScoreAnalysisVO();
                    if (null != d.get("grade")) {
                        csa.setClassify(d.get("grade").toString());
                    }
                    if (null != d.get("avg")) {
                        csa.setValue(Math.round(Float.valueOf(d.get("avg").toString())) + "");
                    }
                    csaList.add(csa);
                }
            }
            result.put("success", true);
            result.put("data", csaList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "英语考试单次数据分析---人数分布---按年级统计失败！");
            return result;
        }
    }


    public Map<String, Object> getTop(Long orgId, String teacherYear, String semester, String cetType) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<ScoreTop> stList = new ArrayList<>();
        try {
            Date start = null;
            Date end = null;
            Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teacherYear, semester);
            if (null != schoolCalendar) {
                if (null != schoolCalendar.get("success") && Boolean.parseBoolean(schoolCalendar.get("success").toString())) {
                    List<TeacherYearSemesterDTO> tysList = (List<TeacherYearSemesterDTO>) schoolCalendar.get("data");
                    if (tysList.size() > 0) {
                        Date date = sdf.parse(tysList.get(0).getStartTime());
                        int week = tysList.get(0).getWeek();
                        start = date;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(start);
                        calendar.add(Calendar.WEEK_OF_YEAR, week+1);
                        end = calendar.getTime();
                    }
                }
            }
            StringBuilder sql = new StringBuilder("SELECT cs.JOB_NUMBER AS xh,s.NAME AS name,d.COMPANY_NAME AS dName,p.NAME AS pName,ss.CLASS_NAME AS cName,ss.GRADE AS grade,cs.SCORE AS score " +
                    "FROM t_cet_score cs LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER  "+
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER LEFT JOIN t_department d ON d.COMPANY_NUMBER = ss.COLLEGE_CODE LEFT JOIN t_profession p ON p. CODE = ss.PROFESSION_CODE "+
                    "WHERE 1=1");
            if (null != orgId) {
                sql.append(" and cs.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != start && null != end) {
                sql.append(" and cs.EXAMINATION_DATE BETWEEN :start AND :end");
                condition.put("start", start);
                condition.put("end", end);
            }
            if (!StringUtils.isBlank(cetType)) {
                sql.append(" and cs.TYPE LIKE :cetType");
                condition.put("cetType", "%大学英语" + cetType + "%");
            }
            sql.append("  AND cs.SCORE > 0 AND d.COMPANY_NAME IS NOT NULL AND p.NAME IS NOT NULL AND ss.CLASS_NAME IS NOT NULL ORDER BY cs.SCORE DESC LIMIT 10");
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object row : res) {
                    Map d = (Map) row;
                    ScoreTop st = new ScoreTop();
                    if (null != d.get("xh")) {
                        st.setJobNumber(d.get("xh").toString());
                    }
                    if (null != d.get("name")) {
                        st.setName(d.get("name").toString());
                    }
                    if (null != d.get("dName")) {
                        st.setCollegeName(d.get("dName").toString());
                    }
                    if (null != d.get("pName")) {
                        st.setProfessionName(d.get("pName").toString());
                    }
                    if (null != d.get("cName")) {
                        st.setClassName(d.get("cName").toString());
                    }
                    if (null != d.get("grade")) {
                        st.setGrade(d.get("grade").toString());
                    }
                    if (null != d.get("score")) {
                        st.setMaxScore(Math.round(Float.valueOf(d.get("score").toString())) + "");
                    }
                    stList.add(st);
                }
            }
            result.put("success", true);
            result.put("data", stList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "英语考试单次数据分析---top10统计失败！");
            return result;
        }
    }


    public Map<String, Object> currentStatistics(Long orgId,String collegeCode,String professionCode,String classCode, String cetType) {
        Map<String, Object> result = new HashMap<>();

        try {
            //在校人数
            Map<String, Object> condition = new HashMap<>();
            StringBuilder sql = new StringBuilder("SELECT SUM(IF(c.max > 0, 1, 0)) AS total,SUM(IF(c.max >= 425, 1, 0)) AS pass FROM t_student_status ss LEFT JOIN  (SELECT cs.JOB_NUMBER as xh, MAX(cs.SCORE) as max FROM t_cet_score cs " +
                    "WHERE 1=1 AND cs.TYPE LIKE "+"'%大学英语" + cetType + "%'" +
                      " GROUP BY xh ) c ON ss.JOB_NUMBER = c.xh WHERE 1 = 1");
            StringBuilder avgsql = new StringBuilder("SELECT AVG(cs.SCORE) as avg FROM t_cet_score cs LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "WHERE cs.SCORE >= 425 and cs.TYPE LIKE " + "'%大学英语" + cetType + "%' ");
            if (null != orgId) {
                sql.append(" and ss.ORG_ID = :orgId");
                avgsql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (!StringUtils.isBlank(collegeCode)) {
                sql.append(" and ss.COLLEGE_CODE = :collegeCode");
                avgsql.append(" and ss.COLLEGE_CODE = :collegeCode");
                condition.put("collegeCode", collegeCode);
            }
            if (!StringUtils.isBlank(professionCode)) {
                sql.append(" and ss.PROFESSION_CODE = :professionCode");
                avgsql.append(" and ss.PROFESSION_CODE = :professionCode");
                condition.put("professionCode", professionCode);
            }
            if (!StringUtils.isBlank(classCode)) {
                sql.append(" and ss.CLASS_CODE = :classCode");
                avgsql.append(" and ss.CLASS_CODE = :classCode");
                condition.put("classCode", classCode);
            }
            sql.append(" AND CURDATE() BETWEEN ss.ENROL_YEAR AND ss.GRADUATION_DATE");
            avgsql.append(" AND CURDATE() BETWEEN ss.ENROL_YEAR AND ss.GRADUATION_DATE");
            Query sq = em.createNativeQuery(sql.toString());
            Query aq = em.createNativeQuery(avgsql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            aq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
                aq.setParameter(e.getKey(), e.getValue());
            }
            Map res = (Map)sq.getSingleResult();
            Map ares = (Map)aq.getSingleResult();
            ScoreStatisticsVO data = new ScoreStatisticsVO();
            if(null!=res.get("total")){
                data.setTotal(Integer.valueOf(res.get("total").toString()));
            }
            if(null!=res.get("pass")){
                data.setPass(Integer.valueOf(res.get("pass").toString()));
            }
            if(null!=ares.get("avg")){
                data.setAvg(Math.round(Float.valueOf(ares.get("avg").toString())) + "");
            }
            if(data.getTotal()>0){
                data.setRate(new DecimalFormat("0.00").format((double) data.getPass() * 100 / data.getTotal()));
            }
            result.put("success", true);
            result.put("data", data);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "英语考试当前状况---数据统计统计失败！");
            return result;
        }
    }

    public Map<String, Object> organizationStatistics(Long orgId,String collegeCode,String professionCode,String classCode, String cetType) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<CetScoreNumberOfPeopleVO> csnpList = new ArrayList<>();
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            StringBuilder ql = new StringBuilder("");
            if (null != orgId) {
                ql.append(" and ss.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            StringBuilder sql = new StringBuilder("");
            if (!StringUtils.isBlank(collegeCode)) {
                if (!StringUtils.isBlank(professionCode)) {
                    if (!StringUtils.isBlank(classCode)) {
                        sql.append("SELECT ss.CLASS_NAME as name, SUM(IF(c.max > 0, 1, 0)) AS total,SUM(IF(c.max >= 425, 1, 0)) AS pass FROM t_student_status ss LEFT JOIN  (SELECT cs.JOB_NUMBER as xh, MAX(cs.SCORE) as max FROM t_cet_score cs " +
                                " WHERE 1=1 AND cs.TYPE LIKE "+"'%大学英语" + cetType + "%'" +
                                " GROUP BY xh ) c ON ss.JOB_NUMBER = c.xh WHERE 1 = 1 AND CURDATE() BETWEEN ss.ENROL_YEAR AND ss.GRADUATION_DATE");
                        sql.append(ql);
                        sql.append(" and ss.CLASS_CODE = :classCode");
                        sql.append(" and ss.PROFESSION_CODE = :professionCode");
                        condition.put("classCode", classCode);
                        condition.put("professionCode", professionCode);
                    }else {
                        sql.append("SELECT ss.CLASS_NAME as name, SUM(IF(c.max > 0, 1, 0)) AS total,SUM(IF(c.max >= 425, 1, 0)) AS pass FROM t_student_status ss LEFT JOIN  (SELECT cs.JOB_NUMBER as xh, MAX(cs.SCORE) as max FROM t_cet_score cs " +
                                " WHERE 1=1 AND cs.TYPE LIKE "+"'%大学英语" + cetType + "%'" +
                                " GROUP BY xh ) c ON ss.JOB_NUMBER = c.xh WHERE 1 = 1 AND CURDATE() BETWEEN ss.ENROL_YEAR AND ss.GRADUATION_DATE");
                        sql.append(ql);
                        sql.append(" and ss.PROFESSION_CODE = :professionCode");
                        condition.put("professionCode", professionCode);
                        sql.append(" group by ss.CLASS_NAME");
                    }
                } else {
                    sql.append("SELECT p.NAME as name, SUM(IF(c.max > 0, 1, 0)) AS total,SUM(IF(c.max >= 425, 1, 0)) AS pass FROM t_student_status ss LEFT JOIN  (SELECT cs.JOB_NUMBER as xh, MAX(cs.SCORE) as max FROM t_cet_score cs " +
                            "WHERE 1=1 AND cs.TYPE LIKE "+"'%大学英语" + cetType + "%'" +
                            " GROUP BY xh ) c ON ss.JOB_NUMBER = c.xh LEFT JOIN t_profession P ON P.CODE = ss.PROFESSION_CODE " +
                            " WHERE 1 = 1 AND CURDATE() BETWEEN ss.ENROL_YEAR AND ss.GRADUATION_DATE");
                    sql.append(ql);
                    sql.append(" and ss.COLLEGE_CODE = :collegeCode");
                    condition.put("collegeCode", collegeCode);
                    sql.append(" group by ss.PROFESSION_CODE");
                }
            } else {
                sql.append("SELECT d.COMPANY_NAME as name, SUM(IF(c.max > 0, 1, 0)) AS total,SUM(IF(c.max >= 425, 1, 0)) AS pass FROM t_student_status ss LEFT JOIN  (SELECT cs.JOB_NUMBER as xh, MAX(cs.SCORE) as max FROM t_cet_score cs " +
                        "WHERE 1=1 AND cs.TYPE LIKE "+"'%大学英语" + cetType + "%'" +
                        " GROUP BY xh ) c ON ss.JOB_NUMBER = c.xh LEFT JOIN t_department d ON d.COMPANY_NUMBER = ss.COLLEGE_CODE" +
                        " WHERE 1 = 1 AND CURDATE() BETWEEN ss.ENROL_YEAR AND ss.GRADUATION_DATE");
                sql.append(ql);
                sql.append(" group by ss.COLLEGE_CODE");
            }
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object row : res) {
                    Map d = (Map) row;
                    if (null != d.get("name")) {
                        CetScoreNumberOfPeopleVO csnp = new CetScoreNumberOfPeopleVO();
                        csnp.setName(d.get("name").toString());
                        if (null != d.get("total")) {
                            csnp.setJoinNumber(Integer.valueOf(d.get("total").toString()));
                        }
                        if (null != d.get("pass")) {
                            csnp.setPassNumber(Integer.valueOf(d.get("pass").toString()));
                        }
                        csnpList.add(csnp);
                    }
                }
            }
            result.put("success", true);
//            StatisticalDomain sd=new StatisticalDomain();
            TotalInfoDomain totalInfoDomain=new TotalInfoDomain();
            totalInfoDomain.setCsnpList(csnpList);
            totalInfoDomain.setMapSex(sexTotal(orgId,collegeCode,professionCode,classCode,cetType));
            totalInfoDomain.setAgeSexStatisticalInfoDomains(ageTotal(orgId,collegeCode,professionCode,classCode,cetType));
            totalInfoDomain.setNjStatisticalInfoDomains(njTotal(orgId,collegeCode,professionCode,classCode,cetType));
            totalInfoDomain.setScoreScaleDomains(scoreScaleTotal(orgId,collegeCode,professionCode,classCode,cetType));
//            sd.setTotalInfoDomain(totalInfoDomain);
//            AvgInfoDomain avgInfoDomain=new AvgInfoDomain();
//            avgInfoDomain.setAvgDomains(avgInfo(orgId,collegeCode,professionCode,classCode,cetType));
//            avgInfoDomain.setAvgAgeDomains(avgAvgInfo(orgId,collegeCode,professionCode,classCode,cetType));
//            avgInfoDomain.setAvgSexDomains(avgSexInfo(orgId,collegeCode,professionCode,classCode,cetType));
//            avgInfoDomain.setAvgNjDomains(avgNjInfo(orgId,collegeCode,professionCode,classCode,cetType));
//            sd.setAvgInfoDomain(avgInfoDomain);
            result.put("data", totalInfoDomain);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "英语考试当前状况---人数分布---按行政班统计失败！");
            return result;
        }
    }

    public PageData<CetDetailVO> getDetailList(Long orgId,String collegeCode,String professionCode,String classCode,String cetType,String nj,String teacherYear,String semester,String isPass,Integer pageNumber, Integer pageSize) {
        PageData<CetDetailVO> p = new PageData<>();
        try {
            Date start = null;
            Date end = null;
            if (!StringUtils.isBlank(teacherYear)&&!StringUtils.isBlank(semester)) {
                Map<String, Object> schoolCalendar = schoolYearTermService.getSchoolCalendar(orgId, teacherYear, semester);
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
            Map<String, Object> condition = new HashMap<>();
            StringBuilder sql = new StringBuilder("SELECT cs.JOB_NUMBER as xh,x.XM as xm,x.BJMC as bj,x.ZYMC as zy,x.YXSMC xy,x.NJ nj,cs.EXAMINATION_DATE as date,cs.SCORE as score " +
                    "FROM t_cet_score cs LEFT JOIN t_xsjbxx x  ON x.XH = cs.JOB_NUMBER WHERE cs.EXAMINATION_DATE BETWEEN :start AND :end ");
            StringBuilder cql = new StringBuilder("SELECT count(1) " +
                    "FROM t_cet_score cs LEFT JOIN t_xsjbxx x  ON x.XH = cs.JOB_NUMBER WHERE cs.EXAMINATION_DATE BETWEEN :start AND :end ");
            condition.put("start", start);
            condition.put("end", end);
            if (null != orgId) {
                sql.append(" and cs.ORG_ID = :orgId");
                cql.append(" and cs.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (!StringUtils.isBlank(cetType)) {
                sql.append(" and cs.TYPE LIKE :cetType");
                cql.append(" and cs.TYPE LIKE :cetType");
                condition.put("cetType", "%大学英语" + cetType + "%");
            }
            if (!StringUtils.isBlank(collegeCode)) {
                sql.append(" and YXSH = :collegeCode");
                cql.append(" and YXSH = :collegeCode");
                condition.put("collegeCode", collegeCode);
            }
            if (!StringUtils.isBlank(professionCode)) {
                sql.append(" and ZYH = :professionCode");
                cql.append(" and ZYH = :professionCode");
                condition.put("professionCode", professionCode);
            }
            if (!StringUtils.isBlank(classCode)) {
                sql.append(" and BH = :classCode");
                cql.append(" and BH = :classCode");
                condition.put("classCode", classCode);
            }

            if(null!=isPass&&isPass.equals("1")){
                sql.append(" and cs.SCORE >= 425");
                cql.append(" and cs.SCORE >= 425");
            }else {
                if (null!=isPass&&isPass.equals("0")) {
                    sql.append(" and cs.SCORE < 425");
                    cql.append(" and cs.SCORE < 425");
                } else {
                    sql.append(" and cs.SCORE > 0");
                    cql.append(" and cs.SCORE > 0");
                }
            }
            sql.append(" ORDER BY cs.JOB_NUMBER");
            cql.append(" ORDER BY cs.JOB_NUMBER");
            Query sq = em.createNativeQuery(sql.toString());
            Query cq = em.createNativeQuery(cql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
                cq.setParameter(e.getKey(), e.getValue());
            }
            Long count = Long.parseLong(cq.getSingleResult() + "");
            if (null == pageNumber || pageNumber < 1) {
                pageNumber = 1;
            }
            if (null == pageSize) {
                pageSize = 20;
            }
            sq.setFirstResult((pageNumber - 1) * pageSize);
            sq.setMaxResults(pageSize);
            List<Object> res = sq.getResultList();
            List<CetDetailVO> sList = new ArrayList<>();
            for (Object obj : res) {
                Map row = (Map) obj;
                CetDetailVO s = new CetDetailVO();
                if (null != row.get("xh")) {
                    s.setJobNumber(row.get("xh").toString());
                }
                if (null != row.get("xm")) {
                    s.setName(row.get("xm").toString());
                }
                if (null != row.get("xy")) {
                    s.setCollegeName(row.get("xy").toString());
                }
                if (null != row.get("zy")) {
                    s.setProfessionName(row.get("zy").toString());
                }
                if (null != row.get("bj")) {
                    s.setClassName(row.get("bj").toString());
                }
                if (null != row.get("nj")) {
                    s.setGrade(row.get("nj").toString());
                }
                if (null != row.get("score")&& !row.get("score").equals("")) {
                    if(Float.valueOf(row.get("score").toString())>0) {
                        s.setScore(Math.round(Float.valueOf(row.get("score").toString())) + "");
                    }else {
                        s.setScore(0 + "");
                    }
                }
                if (null != row.get("date")) {
                    s.setDate(row.get("date").toString());
                }
                sList.add(s);
            }
            p.setData(sList);
            p.getPage().setPageNumber(pageNumber);
            p.getPage().setPageSize(pageSize);
            p.getPage().setTotalElements(count);
            p.getPage().setTotalPages(PageUtil.cacalatePagesize(count, p.getPage().getPageSize()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }





    public  Map<String,Object>  avg(Long orgId,String collegeCode,String professionCode,String classCode, String cetType){
        Map<String,Object> result=new HashMap<>();
        AvgInfoDomain avgInfoDomain=new AvgInfoDomain();
        avgInfoDomain.setAvgDomains(avgInfo(orgId,collegeCode,professionCode,classCode,cetType));
        avgInfoDomain.setAvgAgeDomains(avgAvgInfo(orgId,collegeCode,professionCode,classCode,cetType));
        avgInfoDomain.setAvgSexDomains(avgSexInfo(orgId,collegeCode,professionCode,classCode,cetType));
        avgInfoDomain.setAvgNjDomains(avgNjInfo(orgId,collegeCode,professionCode,classCode,cetType));
        result.put("data", avgInfoDomain);
        result.put("success", true);
        return result;
    }



    public  Map<String,CetSexStatisticalInfoDomain>  sexTotal(Long orgId,String collegeCode,String professionCode,String classCode, String cetType){
         List<CetSexStatisticalInfoDomain> cetSexStatisticalInfoDomainListAll=cetSexStatisticalJdbc.sexStatisticalInfo(orgId,collegeCode,professionCode,classCode,cetType,true);
         List<CetSexStatisticalInfoDomain> cetSexStatisticalInfoDomainList=cetSexStatisticalJdbc.sexStatisticalInfo(orgId,collegeCode,professionCode,classCode,cetType,false);
         Map<String,CetSexStatisticalInfoDomain> m=new HashMap<>();
         if (null!=cetSexStatisticalInfoDomainListAll&&0<cetSexStatisticalInfoDomainListAll.size()&&null!=cetSexStatisticalInfoDomainList&&0<cetSexStatisticalInfoDomainList.size()){
             for (CetSexStatisticalInfoDomain cetSexStatisticalInfoDomain:cetSexStatisticalInfoDomainListAll) {
                 for (CetSexStatisticalInfoDomain statisticalInfoDomain:cetSexStatisticalInfoDomainList) {
                      if (cetSexStatisticalInfoDomain.getSex().equals(statisticalInfoDomain.getSex())){
                          CetSexStatisticalInfoDomain c=new CetSexStatisticalInfoDomain();
                          c.setSex(cetSexStatisticalInfoDomain.getSex());
                          c.setAllTotal(cetSexStatisticalInfoDomain.getAllTotal());
                          c.setPassTotal(statisticalInfoDomain.getPassTotal());
                          if (cetSexStatisticalInfoDomain.getSex().equals("男")){
                              m.put("man",c);
                          }else{
                              m.put("woman",c);
                          }
                      }
                 }
             }
         }
         return m;
    }


    public List<AgeSexStatisticalInfoDomain>  ageTotal(Long orgId, String collegeCode, String professionCode, String classCode, String cetType) {
        List<AgeSexStatisticalInfoDomain> ageSexStatisticalInfoDomains = new ArrayList<>();
        List<AgeSexStatisticalInfoDomain> ageSexStatisticalInfoDomainListAll = cetSexStatisticalJdbc.ageStatisticalInfo(orgId, collegeCode, professionCode, classCode, cetType, true);
        List<AgeSexStatisticalInfoDomain> ageSexStatisticalInfoDomainList = cetSexStatisticalJdbc.ageStatisticalInfo(orgId, collegeCode, professionCode, classCode, cetType, false);
        if (null != ageSexStatisticalInfoDomainListAll && 0 < ageSexStatisticalInfoDomainListAll.size() && null != ageSexStatisticalInfoDomainList && 0 < ageSexStatisticalInfoDomainList.size()) {
            for (AgeSexStatisticalInfoDomain ageSexStatisticalInfoDomain : ageSexStatisticalInfoDomainListAll) {
                for (AgeSexStatisticalInfoDomain statisticalInfoDomain : ageSexStatisticalInfoDomainList) {
                    if (ageSexStatisticalInfoDomain.getAge() == statisticalInfoDomain.getAge()) {
                        AgeSexStatisticalInfoDomain c = new AgeSexStatisticalInfoDomain();
                        c.setAge(ageSexStatisticalInfoDomain.getAge());
                        c.setAllTotal(ageSexStatisticalInfoDomain.getAllTotal());
                        c.setPassTotal(statisticalInfoDomain.getPassTotal());
                        ageSexStatisticalInfoDomains.add(c);
                        break;
                    }
                }
            }
        }
        return ageSexStatisticalInfoDomains;
    }


    public List<NjStatisticalInfoDomain>  njTotal(Long orgId, String collegeCode, String professionCode, String classCode, String cetType) {
        List<NjStatisticalInfoDomain> njStatisticalInfoDomains = new ArrayList<>();
        List<NjStatisticalInfoDomain> njStatisticalInfoDomainListAll = cetSexStatisticalJdbc.njStatisticalInfo(orgId, collegeCode, professionCode, classCode, cetType, true);
        List<NjStatisticalInfoDomain> njStatisticalInfoDomainList = cetSexStatisticalJdbc.njStatisticalInfo(orgId, collegeCode, professionCode, classCode, cetType, false);
        if (null != njStatisticalInfoDomainListAll && 0 < njStatisticalInfoDomainListAll.size() && null != njStatisticalInfoDomainList && 0 < njStatisticalInfoDomainList.size()) {
            for (NjStatisticalInfoDomain njStatisticalInfoDomain : njStatisticalInfoDomainListAll) {
                for (NjStatisticalInfoDomain statisticalInfoDomain : njStatisticalInfoDomainList) {
                    if (njStatisticalInfoDomain.getNj().equals(statisticalInfoDomain.getNj())) {
                        NjStatisticalInfoDomain c = new NjStatisticalInfoDomain();
                        c.setNj(njStatisticalInfoDomain.getNj());
                        c.setAllTotal(njStatisticalInfoDomain.getAllTotal());
                        c.setPassTotal(statisticalInfoDomain.getPassTotal());
                        njStatisticalInfoDomains.add(c);
                        break;
                    }
                }
            }
        }
        return njStatisticalInfoDomains;
    }


  public List<ScoreScaleDomain> scoreScaleTotal(Long orgId, String collegeCode, String professionCode, String classCode, String cetType){
      List<ScoreScaleDomain> scoreScaleDomainList=new ArrayList<>();
//      ScoreScaleDomain scoreScaleDomain= cetSexStatisticalJdbc.scoreStatisticalInfo(orgId,collegeCode,professionCode,classCode,cetType);
//      scoreScaleDomainList.add(scoreScaleDomain);
      scoreScaleDomainList.add(cetSexStatisticalJdbc.scoreScaleStatisticalInfo(orgId,collegeCode,professionCode,classCode,cetType,0,390));
      scoreScaleDomainList.add(cetSexStatisticalJdbc.scoreScaleStatisticalInfo(orgId,collegeCode,professionCode,classCode,cetType,391,424));
      scoreScaleDomainList.add(cetSexStatisticalJdbc.scoreScaleStatisticalInfo(orgId,collegeCode,professionCode,classCode,cetType,425,480));
      scoreScaleDomainList.add(cetSexStatisticalJdbc.scoreScaleStatisticalInfo(orgId,collegeCode,professionCode,classCode,cetType,481,550));
      scoreScaleDomainList.add(cetSexStatisticalJdbc.scoreScaleStatisticalInfo(orgId,collegeCode,professionCode,classCode,cetType,551,600));
      scoreScaleDomainList.add(cetSexStatisticalJdbc.scoreScaleStatisticalInfo(orgId,collegeCode,professionCode,classCode,cetType,601,710));
      return scoreScaleDomainList;
  }

/**------------------------------------------------均值---------------------------------------**/



    public List<AvgDomain> avgInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType){
    List<AvgDomain> avgDomainList=new ArrayList<>();
    List<AvgDomain> avgDomains=cetAvgStatisticalJdbc.avgInfo(orgId,collegeCode,professionCode,classCode,cetType);
    if (null!=avgDomains&&0<avgDomains.size()){
        avgDomainList.addAll(avgDomains);
    }
    return avgDomainList;
}

//性别
    public List<AvgSexDomain> avgSexInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType){
        List<AvgSexDomain> avgDomainList=new ArrayList<>();
        List<AvgSexDomain> avgDomains=cetAvgStatisticalJdbc.avgXbInfo(orgId,collegeCode,professionCode,classCode,cetType);
        if (null!=avgDomains&&0<avgDomains.size()){
            avgDomainList.addAll(avgDomains);
        }
        return avgDomainList;
    }
//年龄
    public List<AvgAgeDomain> avgAvgInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType){
        List<AvgAgeDomain> avgDomainList=new ArrayList<>();
        List<AvgAgeDomain> avgDomains=cetAvgStatisticalJdbc.avgAgeInfo(orgId,collegeCode,professionCode,classCode,cetType);
        if (null!=avgDomains&&0<avgDomains.size()){
            avgDomainList.addAll(avgDomains);
        }
        return avgDomainList;
    }
//年级
public List<AvgNjDomain> avgNjInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType){
    List<AvgNjDomain> avgDomainList=new ArrayList<>();
    List<AvgNjDomain> avgDomains=cetAvgStatisticalJdbc.avgNjInfo(orgId,collegeCode,professionCode,classCode,cetType);
    if (null!=avgDomains&&0<avgDomains.size()){
        avgDomainList.addAll(avgDomains);
    }
    return avgDomainList;
}

}
