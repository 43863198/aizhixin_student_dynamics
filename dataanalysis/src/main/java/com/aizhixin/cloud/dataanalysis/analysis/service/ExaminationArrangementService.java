package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.vo.ExamArrangeVO;
import com.netflix.discovery.converters.Auto;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-14
 */
@Component
@Transactional
public class ExaminationArrangementService {
    @Autowired
    private EntityManager em;

    public Map<String,Object> getArrange(Long orgId){
        Map<String,Object> result = new HashMap<>();
        List<ExamArrangeVO> data = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT ea.START_TIME as start, ea.END_TIME as end, c.COURSE_NAME as cname, cr.TEACHING_BUILDING_NUMBER as jxl, cr.CLASSROOM_NAME as jsm  " +
                    "FROM t_examination_arrangement ea " +
                    "LEFT JOIN t_class_room cr ON ea.CLASSROOM_NUMBER = cr.CLASSROOM_NUMBER LEFT JOIN t_course  c ON c.COURSE_NUMBER = ea.COURSE_NUMBER WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" and ea.ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            sql.append(" and ea.TEST_DATE = current_date()");
            sql.append(" and c.COURSE_NAME IS NOT NULL");
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Object> res = sq.getResultList();
            for (Object obj : res) {
                Map row = (Map) obj;
                ExamArrangeVO ea = new ExamArrangeVO();
                if (null != row.get("start")) {
                    ea.setStartTime(Time.valueOf(row.get("start").toString()));
                }
                if (null != row.get("end")) {
                    ea.setEndTime(Time.valueOf(row.get("end").toString()));
                }
                if (null != row.get("cname")) {
                    ea.setSubject(row.get("cname").toString());
                }
                String str = "";
                if (null != row.get("jxl")) {
                    str = str+ row.get("jxl").toString();
                }
                if (null != row.get("jsm")) {
                    str = str+ row.get("jsm").toString();
                }
                ea.setRoom(str);
                data.add(ea);
            }

            result.put("success",true);
            result.put("data",data);
            return result;
        }catch (Exception e) {
            result.put("success",false);
            result.put("message","获取今日考试安排失败！");
            return result;
        }

    }


}
