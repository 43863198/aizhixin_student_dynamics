package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.CollegeWarningInfoDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CetStatisticDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CollegeCetStatisticDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TrendDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.CetScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.CetScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.common.util.ProportionUtil;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    
    public void deleteAllByOrgId(Long orgId){
    	cetScoreStatisticsRespository.deleteByOrgId(orgId);
    }
    
    public void saveList(List<CetScoreStatistics> cetScoreStatisticsList){
    	cetScoreStatisticsRespository.save(cetScoreStatisticsList);
    }
    
    public void save(CetScoreStatistics cetScoreStatistics){
    	cetScoreStatisticsRespository.save(cetScoreStatistics);
    }
    
    public CetScoreStatistics findById(String id){
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
                                cpass4  = Integer.valueOf(String.valueOf(d[1]));
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


    public Map<String, Object> getCetTrendAnalysis(Long orgId, Long collegeId, Integer type) {
        Map<String, Object> result = new HashMap<>();
        List<TrendDTO> trendDTOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
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
            int count4 = 0;
            int count6 = 0;
            int pass4 = 0;
            int pass6 = 0;
            if (type == 4) {
                if (null != res) {
                    for (Object obj : res) {
                        Object[] d = (Object[]) obj;
                            TrendDTO trendDTO = new TrendDTO();
                            if (null != d[0]) {
                                count4 = Integer.valueOf(String.valueOf(d[0]));
                            }
                            if (null != d[1]) {
                                pass4 = Integer.valueOf(String.valueOf(d[1]));
                            }
                            if (null != d[4]) {
                                trendDTO.setYear(String.valueOf(d[4]));
                            }
                            if (null != d[5]) {
                                trendDTO.setSemester(String.valueOf(d[5]));
                            }
                            trendDTO.setValue(ProportionUtil.accuracy(pass4 * 1.0, count4 * 1.0, 2));
                            trendDTOList.add(trendDTO);
                        }
                }
            }
            if (type == 6) {
                if (null != res) {
                    for (Object obj : res) {
                        Object[] d = (Object[]) obj;
                            TrendDTO trendDTO = new TrendDTO();
                            if (null != d[2]) {
                                count6 = Integer.valueOf(String.valueOf(d[2]));
                            }
                            if (null != d[3]) {
                                pass6 = Integer.valueOf(String.valueOf(d[3]));
                            }
                            if (null != d[4]) {
                                trendDTO.setYear(String.valueOf(d[4]));
                            }
                            if (null != d[5]) {
                               trendDTO.setSemester(String.valueOf(d[5]));
                            }
                            trendDTO.setValue(ProportionUtil.accuracy(pass6 * 1.0, count6 * 1.0, 2));
                            trendDTOList.add(trendDTO);
                        }
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

            if(null!=teacherYear){
                criteria.and("schoolYear").is(teacherYear);
            }
            if(null!=semester){
                criteria.and("semester").is(semester);
            }
            if (null != grade) {
                List<String> tds = new ArrayList<>();
                if (grade.indexOf(",") != -1) {
                    String[] td = grade.split(",");
                    for (String d : td) {
                        tds.add(d);
                    }
                }else {
                    tds.add(grade);
                }
                criteria.and("grade").in(tds);
            }
            if (null != type) {
                    if (type.equals(4)) {
                        criteria.and("examType").is(ScoreConstant.EXAM_TYPE_CET4);
                    }
                    if (type.equals(6)) {
                        criteria.and("examType").is(ScoreConstant.EXAM_TYPE_CET6);
                    }
                    criteria.and("totalScore").gte(ScoreConstant.CET_PASS_SCORE_LINE);
            }else {
                criteria.and("examType").ne(ScoreConstant.EXAM_TYPE_COURSE);
            }
            if(null!=nj){
                criteria.orOperator(criteria.where("jobNum").is(nj), criteria.where("userName").regex(".*?\\" + nj + ".*"));
            }
            //mongoTemplate.count计算总数
            long total = mongoTemplate.count(new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria), Score.class);
            // mongoTemplate.find 查询结果集
            List<Score> items  = mongoTemplate.find(new org.springframework.data.mongodb.core.query.Query().addCriteria(criteria).with(pageable), Score.class);

            p.getPage().setTotalPages(((int)total + page.getPageSize() - 1) / page.getPageSize());
            p.getPage().setPageNumber(page.getPageNumber());
            p.getPage().setPageSize(page.getPageSize());
            p.getPage().setTotalElements(total);
            p.setData(items);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取数据异常！");
        }
        result.put("success", true);
        result.put("data", p);
        return result;
    }

    public void  deleteCetStatistics(Long orgId,Integer teacherYear,Integer semester){
        cetScoreStatisticsRespository.deleteByOrgIdAndTeacherYearAndSemester(orgId, teacherYear, semester);
    }

}
