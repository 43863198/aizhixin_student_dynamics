package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.vo.ClassTodayVO;
import com.aizhixin.cloud.dataanalysis.analysis.vo.ExamArrangeVO;
import com.aizhixin.cloud.dataanalysis.feign.FeignService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-16
 */
@Service
public class CourseStatisticsService {
    @Autowired
    private EntityManager em;
    @Autowired
    private FeignService feignService;

    public Map<String,Object> getClassToday(Long orgId) {

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<ClassTodayVO> classTodayVOList = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int day = 0;
        long weeks = 0;
        try {
            String semester = feignService.getSemester(138L);
            if(null!=semester) {
                JSONObject json = JSONObject.parseObject(semester);
                Date startDate = json.getDate("startDate");
                Date date = df.parse(df.format(new Date()));
                Date date1 = df.parse(df.format(startDate));
                weeks = (date.getTime() - date1.getTime()) / (7 * 24 * 60 * 60 * 1000);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                day = calendar.get(Calendar.DAY_OF_WEEK);
                //
                day=day+1;
                if(day>7){
                    day=day-7;
                }
            }
        StringBuilder sql = new StringBuilder("SELECT cs.START_PERIOD as period, count(tc.COURSE_NAME) as count FROM t_curriculum_schedule cs  LEFT JOIN t_teaching_class tc ON cs.TEACHING_CLASS_NUMBER = tc.TEACHING_CLASS_NUMBER where 1 = 1");
        if(null!=orgId){
            sql.append(" AND cs.ORG_ID = :orgId");
            condition.put("orgId", orgId);
        }
        if(weeks!=0) {
            sql.append(" AND cs.START_WEEK <= :startweeks");
            sql.append(" AND cs.END_WEEK >= :endweeks");
            condition.put("startweeks", weeks);
            condition.put("endweeks", weeks);
        }
        if(day!=0) {
            sql.append(" AND cs.DAY_OF_THE_WEEK = :day");
            condition.put("day", day);
        }
        sql.append(" GROUP BY cs.START_PERIOD");
        Query sq = em.createNativeQuery(sql.toString());
        for (Map.Entry<String, Object> e : condition.entrySet()) {
            sq.setParameter(e.getKey(), e.getValue());
        }
        sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Object> res = sq.getResultList();
        for (Object obj : res) {
            Map row = (Map) obj;
            ClassTodayVO ct = new ClassTodayVO();
            if(null!=row.get("period")){
                ct.setPeriod(Integer.valueOf(row.get("period").toString()));
            }
            if(null!=row.get("count")){
                ct.setPeriod(Integer.valueOf(row.get("count").toString()));
            }
        }
            result.put("success",true);
            result.put("data",classTodayVOList);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message","今日课程统计失败！");
            return result;
        }
    }

}
