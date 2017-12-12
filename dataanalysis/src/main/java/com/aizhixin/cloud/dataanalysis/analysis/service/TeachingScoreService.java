package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.*;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreStatistics;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.util.ProportionUtil;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-11
 */
@Component
@Transactional
public class TeachingScoreService {
    @Autowired
    private EntityManager em;

    public Map<String,Object> getStatistic(Long orgId, String grade, Integer semester){
        Map<String,Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        Long count = 0L;
        TeachingAchievementDTO teachingAchievementDTO = new TeachingAchievementDTO();
        List<CollegeTeachingAchievementDTO> collegeTeachingAchievementDTOList = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT COLLOEGE_NAME,STUDENT_NUM,avgGPA,failPassStuNum,CURRICULUM_NUM,CURRICULUM_AVG FROM T_TEACHING_SCORE_STATISTICS  WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" and ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != grade) {
                sql.append(" and GRADE = :grade");
                condition.put("grade", grade);
            }
            if (null != semester) {
                sql.append(" and SEMESTER = :semester");
                condition.put("semester", semester);
            }
            sql.append(" GROUP BY COLLOEGE_ID");
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if(null!=res&&res.size()>0){
                for(Object obj : res){
                    Object[] d = (Object[])obj;
                    CollegeTeachingAchievementDTO collegeTeachingAchievementDTO = new CollegeTeachingAchievementDTO();





                    collegeTeachingAchievementDTOList.add(collegeTeachingAchievementDTO);
                }
            }


        }catch (Exception e){
            e.printStackTrace();
            result.put("success", false);
            result.put("message","获取教学成绩统计信息失败！");
            return result;
        }
        result.put("success",true);
        return result;
    }


    public Map<String,Object> getCetTrendAnalysis(Long orgId, Long collegeId, Integer type){
        Map<String,Object> result = new HashMap<>();
        List<TrendDTO> trendDTOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {

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




        }catch (Exception e){
            result.put("success", false);
            result.put("message","获取教学成绩详情数据异常！");
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
