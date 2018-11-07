package com.aizhixin.cloud.dataanalysis.em.online.manager;

import com.aizhixin.cloud.dataanalysis.em.online.vo.ScreenOnlineCourseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OnlineCourseStaticsManager {
    @Autowired
    @Qualifier("emMongoTemplate")
    private MongoTemplate mongoTemplate;

    public ScreenOnlineCourseVO queryOnlineCouseLastest(Long orgId, Long collegeId) {
        ScreenOnlineCourseVO courseVO = new ScreenOnlineCourseVO ();
        String map = "function(){emit(this.orgId, {student:(this.stuUseTotal?this.stuUseTotal:0),teacher:(this.teacherUseTotal?this.teacherUseTotal:0)});}";
        String reduce = "function(k, v){var r = {course:0, student:0,teacher:0};r.course = v.length;for(var i = 0; i < v.length;i++) {r.student += v[i].student;r.teacher += v[i].teacher;}return r;}";
        MapReduceResults<Map> results = mongoTemplate.mapReduce(Query.query(Criteria.where("orgId").is(orgId).and("collegeId").ne(0L)),"AccessOpenCollege", map, reduce, Map.class);
        if(null != results) {
            for (Map m : results) {
                if (null != m.get("value")) {
                    Map v = (Map) m.get("value");
                    if (null != v.get("course")) {
                        courseVO.setBks(Integer.parseInt(new java.text.DecimalFormat("0").format((Double)v.get("course"))));
                    }
                    if (null != v.get("student")) {
                        courseVO.setXss(Integer.parseInt(new java.text.DecimalFormat("0").format((Double)v.get("student"))));
                    }
                    if (null != v.get("teacher")) {
                        courseVO.setJss(Integer.parseInt(new java.text.DecimalFormat("0").format((Double)v.get("teacher"))));
                    }
                }
            }
        }

        map = "function() {emit(this.orgId, {workInfos:(this.workInfos?this.workInfos.length:0),upWareTotal:(this.upWareTotal?this.upWareTotal:0)});}";
        reduce = "function(k, v) {var r = {workInfos:0,upWareTotal:0};for(var i = 0; i < v.length;i++) {r.workInfos += v[i].workInfos;r.upWareTotal += v[i].upWareTotal;}return r;}";
        results = mongoTemplate.mapReduce(Query.query(Criteria.where("orgId").is(orgId)),"TeachingClassDataStatistics", map, reduce, Map.class);
        if(null != results) {
            for (Map m : results) {
                if (null != m.get("value")) {
                    Map v = (Map) m.get("value");
                    if (null != v.get("workInfos")) {
                        courseVO.setZys(Integer.parseInt(new java.text.DecimalFormat("0").format((Double)v.get("workInfos"))));
                    }
                    if (null != v.get("upWareTotal")) {
                        courseVO.setKcs(Integer.parseInt(new java.text.DecimalFormat("0").format((Double)v.get("upWareTotal"))));
                    }
                }
            }
        }
        return courseVO;
    }
}
