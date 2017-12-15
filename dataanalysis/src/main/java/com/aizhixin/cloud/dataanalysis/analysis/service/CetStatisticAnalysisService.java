package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.CollegeWarningInfoDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CetStatisticDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CollegeCetStatisticDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.TrendDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.common.PageData;
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

    public Map<String,Object> getStatistic(Long orgId, String grade, Integer semester){
        Map<String,Object> result = new HashMap<>();
//        PageData<CollegeCetStatisticDTO> p = new PageData<>();
        CetStatisticDTO cetStatisticDTO = new CetStatisticDTO();
        Map<String, Object> condition = new HashMap<>();
        List<CollegeCetStatisticDTO> collegeCetStatisticDTOList = new ArrayList<>();
        Long count = 0L;
        try {
            StringBuilder cql = new StringBuilder("SELECT COUNT(1) AS count, SUM(cet.CET_FORE_JOIN_NUM) as sumc4, SUM(cet.CET_FORE_PASS_NUM) as sump4, SUM(cet.CET_SIX_JOIN_NUM) as sumc6, SUM(cet.CET_SIX_PASS_NUM) as sump6, max(cet.CREATED_DATE) FROM T_CET_STATISTICS cet WHERE 1 = 1");
            StringBuilder sql = new StringBuilder("SELECT SUM(cet.CET_FORE_JOIN_NUM) as sumc4, SUM(cet.CET_FORE_PASS_NUM) as sump4, SUM(cet.CET_SIX_JOIN_NUM) as sumc6, SUM(cet.CET_SIX_PASS_NUM) as sump6, COLLEGE_NAME, COLLEGE_ID FROM T_CET_STATISTICS cet WHERE 1 = 1");
            if (null != orgId) {
                cql.append(" and cet.ORG_ID = :orgId");
                sql.append(" and cet.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != grade) {
                cql.append(" and cet.TEACHER_YEAR = :grade");
                sql.append(" and cet.TEACHER_YEAR = :grade");
                condition.put("grade", grade);
            }
            if (null != semester) {
                cql.append(" and cet.SEMESTER = :semester");
                sql.append(" and cet.SEMESTER = :semester");
                condition.put("semester", semester);
            }
            Query cq = em.createNativeQuery(cql.toString());
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                cq.setParameter(e.getKey(), e.getValue());
                sq.setParameter(e.getKey(), e.getValue());
            }
            Object[] cres = (Object[]) cq.getSingleResult();
            if (null != cres && cres.length == 6) {
                int count4 = 0;
                int count6 = 0;
                int pass4 = 0;
                int pass6 = 0;
                Date time = new Date();
                if (null != cres[0]) {
                    count = Long.valueOf(String.valueOf(cres[0]));
                }
                if (null != cres[1]) {
                    count4 = Integer.valueOf(String.valueOf(cres[1]));
                }
                if (null != cres[2]) {
                    pass4 = Integer.valueOf(String.valueOf(cres[2]));
                }
                if (null != cres[3]) {
                    count6 = Integer.valueOf(String.valueOf(cres[3]));
                }
                if (null != cres[4]) {
                    pass6 = Integer.valueOf(String.valueOf(cres[4]));
                }
                if (null != cres[5]) {
                    time =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(cres[5]));
                }
                cetStatisticDTO.setCetForeJoinNum(count4);
                cetStatisticDTO.setCetForePassNum(pass4);
                cetStatisticDTO.setCetForePassRate(ProportionUtil.accuracy(pass4 * 1.0, count4 * 1.0, 2));
                cetStatisticDTO.setCetSixJoinNum(count6);
                cetStatisticDTO.setCetSixPassNum(pass6);
                cetStatisticDTO.setCetSixPassRate(ProportionUtil.accuracy(pass6 * 1.0, count6 * 1.0, 2));
                cetStatisticDTO.setStatisticalTime(time);
                if(count>0){
//                    sq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
//                    sq.setMaxResults(pageable.getPageSize());
                    List<Object> rd = sq.getResultList();
                    if (null != rd && rd.size() > 0) {
                        for (Object obj : rd) {
                            Object[] d = (Object[]) obj;
                            int ccount4 = 0;
                            int ccount6= 0;
                            int cpass4 = 0;
                            int cpass6 = 0;
                            CollegeCetStatisticDTO collegeCetStatisticDTO = new CollegeCetStatisticDTO();
                            if (null != d[0]) {
                                ccount4 = Integer.valueOf(String.valueOf(d[0]));
                            }
                            if(null!=d[1]){
                                ccount6 = Integer.valueOf(String.valueOf(d[1]));
                            }
                            if(null!=d[2]){
                                cpass4 = Integer.valueOf(String.valueOf(d[2]));
                            }
                            if(null!=d[3]){
                                cpass6 = Integer.valueOf(String.valueOf(d[3]));
                            }
                            if(null!=d[4]) {
                                collegeCetStatisticDTO.setCollegeName(String.valueOf(d[4]));
                            }
                            if(null!=d[5]){
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
            }
//            p.setData(collegeCetStatisticDTOList);
//            p.getPage().setTotalElements(count);
//            p.getPage().setPageNumber(pageable.getPageNumber());
//            p.getPage().setTotalPages((int)Math.ceil(count/pageable.getPageSize())+1);
//            p.getPage().setPageSize(pageable.getPageSize());
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message","获取英语四六级统计信息失败！");
            return result;
        }
        result.put("success",true);
        result.put("cetStatisticDTO",cetStatisticDTO);
        result.put("dataList",collegeCetStatisticDTOList);
        return result;
    }


    public Map<String,Object> getCetTrendAnalysis(Long orgId, Long collegeId, Integer type){
        Map<String,Object> result = new HashMap<>();
        List<TrendDTO> trendDTOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT SUM(cet.CET_FORE_JOIN_NUM) as sumc4, SUM(cet.CET_FORE_PASS_NUM) as sump4, SUM(cet.CET_SIX_JOIN_NUM) as sumc6, SUM(cet.CET_SIX_PASS_NUM) as sump6, cet.TEACHER_YEAR FROM T_CET_STATISTICS cet WHERE 1 = 1");
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            if (null != orgId) {
                sql.append(" and cet.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != collegeId) {
                sql.append(" and cet.COLLEGE_ID = :collegeId");
                condition.put("collegeId", collegeId);
            }
            sql.append(" group by cet.TEACHER_YEAR");
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
                        if (d.length == 5) {
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
                            trendDTO.setValue(ProportionUtil.accuracy(pass4 * 1.0, count4 * 1.0, 2));
                            trendDTOList.add(trendDTO);
                        }
                    }

                }
            }
            if (type == 6) {
                if (null != res) {
                    for (Object obj : res) {
                        Object[] d = (Object[]) obj;
                        if (d.length == 5) {
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
                            trendDTO.setValue(ProportionUtil.accuracy(pass6 * 1.0, count6 * 1.0, 2));
                            trendDTOList.add(trendDTO);
                        }
                    }

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


    public Map<String, Object> getCetDetail(Long orgId, String collegeId, String grade, Integer type, Pageable page) {
        Map<String, Object> result = new HashMap<>();
        PageData<Score> p = new PageData<>();
        List<Score> items = new ArrayList<>();
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
                String[] cid = collegeId.split(",");
                Set<Long> collegeIds = new HashSet<>();
                for (String d : cid) {
                    collegeIds.add(Long.valueOf(d));
                }
                criteria.and("collegeId").in(collegeIds);
            }
            if (null != grade) {
                String[] td = grade.split(",");
                List<String> tds = new ArrayList<>();
                for (String d : td) {
                    tds.add(d);
                }
                criteria.and("grade").in(tds);
            }
            if (null != type) {
                if(type==4) {
                    criteria.and("examType").is("cet4");
                }else if(type==6){
                    criteria.and("examType").is("cet6");
                }else {
                    criteria.orOperator(criteria.where("examType").is("cet4"), criteria.where("examType").is("cet6"));
                }
            }
            query.addCriteria(criteria);
            //mongoTemplate.count计算总数
            total = mongoTemplate.count(query, StudentRegister.class);
            // mongoTemplate.find 查询结果集
            items = mongoTemplate.find(query.with(pageable), Score.class);
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
