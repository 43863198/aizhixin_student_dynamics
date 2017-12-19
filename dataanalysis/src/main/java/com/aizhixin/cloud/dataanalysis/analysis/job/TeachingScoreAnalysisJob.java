package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.service.TeachingScoreService;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.UserConstant;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.setup.domain.WarningTypeDomain;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-18
 */
@Component
public class TeachingScoreAnalysisJob {
    private Logger logger = Logger.getLogger(TeachingScoreAnalysisJob.class);
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TeachingScoreService teachingScoreService;
    @Autowired
    private WarningTypeService warningTypeService;


    public void schoolStatisticsJob() {

        Calendar c = Calendar.getInstance();
        // 当前年份
        int schoolYear = c.get(Calendar.YEAR);

        List<WarningTypeDomain> orgIdList = warningTypeService.getAllOrgId();
        if (null != orgIdList && orgIdList.size() > 0) {

            List<SchoolStatistics> statisticsList = new ArrayList<SchoolStatistics>();
            HashMap<Long, SchoolStatistics> statisticsMap = new HashMap<Long, SchoolStatistics>();

            String orgIds = "";
            for (WarningTypeDomain domain : orgIdList) {
                if (null != domain && null != domain.getOrgId()) {
                    if (StringUtils.isEmpty(orgIds)) {
                        orgIds = domain.getOrgId().toString();
                    } else {
                        orgIds += "," + domain.getOrgId().toString();
                    }
                }
            }


            Iterator iter = statisticsMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                SchoolStatistics schoolStatistics =(SchoolStatistics) entry.getValue();
                statisticsList.add(schoolStatistics);
            }
        }
    }


    public Map<String, Object> getLookScore(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        List<Score> items = null;
        try {
            //创建查询条件对象
            org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();

            //参考人数统计
            Criteria criteria = Criteria.where("orgId").is(orgId);
            criteria.and("scoreResultType").is(ScoreConstant.RESULT_TYPE_100);

            query.addCriteria(criteria).limit(1000);


            DBObject dbObject = new BasicDBObject();
            BasicDBObject fieldsObject=new BasicDBObject();
            //指定返回的字段
            fieldsObject.put("collegeId", true);
            fieldsObject.put("collegeName", true);
            fieldsObject.put("scheduleId", true);

            Query querys = new BasicQuery(dbObject,fieldsObject).limit(100);
            List<Score> user = mongoTemplate.find(querys, Score.class);


            //mongoTemplate.count计算总数
           long total = mongoTemplate.count(query, StudentRegister.class);
            // mongoTemplate.find 查询结果集
            items = mongoTemplate.find(query, Score.class);
         logger.info(items.size());

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取数据异常！");
        }
        result.put("success",  items);
        return result;
    }



    public Map<String, Object> getLookScores(Long orgId,int schoolYear,int semester) {
        Map<String, Object> result = new HashMap<>();
        List<TeachingScoreStatistics> tssList  = new ArrayList<>();
        try {
            TeachingScoreStatistics tss = new TeachingScoreStatistics();
            tss.setOrgId(orgId);
            tss.setTeacherYear(schoolYear);
            tss.setSemester(semester);
            tss.setStatisticsType(1); //全校统计
            List<TeachingScoreStatistics> ctssList = new ArrayList<>();
            //创建查询条件对象
            org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();
            //参考人数统计
            Criteria criteria = Criteria.where("orgId").is(orgId);
            criteria.and("scoreResultType").is(ScoreConstant.RESULT_TYPE_100);
            criteria.and("schoolYear").is(schoolYear);
            criteria.and("semester").is(semester);
            query.addCriteria(criteria);


            long totalt = mongoTemplate.count(query, Score.class);
            tss.setStudentNum(new Long(totalt).intValue());

            AggregationResults<BasicDBObject> countts = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criteria),
                            Aggregation.group("collegeId").count().as("count").avg("gradePoint").as("GPAavg").avg("totalScore").as("courseAVG")),
                    Score.class,BasicDBObject.class);
            while (countts.iterator().hasNext()){
                TeachingScoreStatistics ctss = new TeachingScoreStatistics();
                ctss.setOrgId(orgId);
                ctss.setTeacherYear(schoolYear);
                ctss.setSemester(semester);
                ctss.setStatisticsType(2); //按学院统计




            }





            //不及格人数统计
            Criteria criteriFail = Criteria.where("orgId").is(orgId);
            criteriFail.and("scoreResultType").is(ScoreConstant.RESULT_TYPE_100);
            criteriFail.and("schoolYear").is(schoolYear);
            criteriFail.and("semester").is(semester);
            criteriFail.and("totalScore").lt(ScoreConstant.PASS_SCORE_LINE);
            query.addCriteria(criteriFail);
            long total = mongoTemplate.count(query, Score.class);

            AggregationResults<BasicDBObject> ccount = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criteria),
                            Aggregation.group("collegeId").count().as("count")),
                    Score.class, BasicDBObject.class);













        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取数据异常！");
        }
        result.put("success", true);
        return result;
    }








}
