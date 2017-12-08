package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.CollegeWarningInfoDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CetStatisticDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CollegeCetStatisticDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.util.ProportionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String,Object> getStatistic(Long orgId, String grade, Integer semester, Pageable pageable){
        Map<String,Object> result = new HashMap<>();
        PageData<CollegeCetStatisticDTO> p = new PageData<>();
        CetStatisticDTO cetStatisticDTO = new CetStatisticDTO();
        Map<String, Object> condition = new HashMap<>();
        List<CollegeCetStatisticDTO> collegeCetStatisticDTOList = new ArrayList<>();
        Long count = 0L;
        try {
            StringBuilder cql = new StringBuilder("SELECT COUNT(1) AS count, SUM(cet.CET_FORE_JOIN_NUM) as sumc4, SUM(cet.CET_FORE_PASS_NUM) as sump4, SUM(cet.CET_SIX_JOIN_NUM) as sumc6, SUM(cet.CET_SIX_PASS_NUM) as sump6 FROM T_CET_STATISTICS cet WHERE 1 = 1");
            StringBuilder sql = new StringBuilder("SELECT SUM(cet.CET_FORE_JOIN_NUM) as sumc4, SUM(cet.CET_FORE_PASS_NUM) as sump4, SUM(cet.CET_SIX_JOIN_NUM) as sumc6, SUM(cet.CET_SIX_PASS_NUM) as sump6, COLLOEGE_NAME, COLLOEGE_ID FROM T_CET_STATISTICS cet WHERE 1 = 1");
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
                sql.append(" and cet.ORG_ID = :semester");
                condition.put("semester", semester);
            }
            Query cq = em.createNativeQuery(cql.toString());
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                cq.setParameter(e.getKey(), e.getValue());
                sq.setParameter(e.getKey(), e.getValue());
            }
            Object[] cres = (Object[]) cq.getSingleResult();
            if (null != cres && cres.length == 5) {
                int count4 = 0;
                int count6 = 0;
                int pass4 = 0;
                int pass6 = 0;
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
                cetStatisticDTO.setCetForeJoinNum(count4);
                cetStatisticDTO.setCetForePassNum(pass4);
                cetStatisticDTO.setCetForePassRate(ProportionUtil.accuracy(pass4 * 1.0, count4 * 1.0, 2));
                cetStatisticDTO.setCetSixJoinNum(count6);
                cetStatisticDTO.setCetSixPassNum(pass6);
                cetStatisticDTO.setCetSixPassRate(ProportionUtil.accuracy(pass6 * 1.0, count6 * 1.0, 2));
                if(count>0){
                    sq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                    sq.setMaxResults(pageable.getPageSize());
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
            p.setData(collegeCetStatisticDTOList);
            p.getPage().setTotalElements(count);
            p.getPage().setPageNumber(pageable.getPageNumber());
            p.getPage().setTotalPages((int)Math.ceil(count/pageable.getPageSize())+1);
            p.getPage().setPageSize(pageable.getPageSize());
        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message","获取英语四六级统计信息失败！");
            return result;
        }
        result.put("success",true);
        result.put("cetStatisticDTO",cetStatisticDTO);
        result.put("pagedata",p);
        return result;
    }



}
