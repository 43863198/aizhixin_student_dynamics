package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreDetails;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.service.TeachingScoreService;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.UserConstant;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.ScoreMongoRespository;
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
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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
    @Autowired
    private ScoreMongoRespository scoreMongoRespository;


    public void schoolStatisticsJob() {

        Calendar c = Calendar.getInstance();
        // 当前年份
        int schoolYear = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int semester = 1;
        if(month>7){
            semester = 2;
        }
        List<WarningTypeDomain> orgIdList = warningTypeService.getAllOrgId();
        if (null != orgIdList && orgIdList.size() > 0) {
            for (WarningTypeDomain domain : orgIdList) {
                if (domain.getSetupCloseFlag()==10) {
                    this.teachingScoreStatistics(domain.getOrgId(),schoolYear,semester);
                    this.teachingScoreDetails(domain.getOrgId(),schoolYear,semester);
                }
            }
        }
    }

    public Map<String, Object> teachingScoreStatistics(Long orgId,int schoolYear,int semester) {
        Map<String, Object> result = new HashMap<>();
        List<TeachingScoreStatistics> tssList  = new ArrayList<>();
        TeachingScoreStatistics tss = new TeachingScoreStatistics();
        try {
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

            long total = mongoTemplate.count(query, Score.class);
            tss.setStudentNum(new Long(total).intValue());

            AggregationResults<BasicDBObject> countts = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criteria),
                            Aggregation.group("collegeId").first("collegeName").as("collegeName").count().as("count").avg("gradePoint").as("GPAavg").avg("totalScore").as("courseAVG"),
                            Aggregation.project("collegeId").and("cid").previousOperation()
                    ),
                    Score.class, BasicDBObject.class);
            int i = 0;
            while (countts.iterator().hasNext()){
                TeachingScoreStatistics ctss = new TeachingScoreStatistics();
                ctss.setOrgId(orgId);
                ctss.setTeacherYear(schoolYear);
                ctss.setSemester(semester);
                ctss.setStatisticsType(2); //按学院统计
                ctss.setCollegeId(countts.getMappedResults().get(i).getLong("cid"));
                ctss.setCollegeName(countts.getMappedResults().get(i).getString("collegeName"));
                ctss.setStudentNum(countts.getMappedResults().get(i).getInt("count"));
                ctss.setAvgGPA(new BigDecimal(countts.getMappedResults().get(i).getDouble("GPAavg")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                ctss.setAvgScore(new BigDecimal(countts.getMappedResults().get(i).getDouble("courseAVG")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                tssList.add(ctss);
                i++;
            }

            //创建查询条件对象
            org.springframework.data.mongodb.core.query.Query queryFail = new org.springframework.data.mongodb.core.query.Query();
            //不及格人数统计
            Criteria criteriFail = Criteria.where("orgId").is(orgId);
            criteriFail.and("scoreResultType").is(ScoreConstant.RESULT_TYPE_100);
            criteriFail.and("schoolYear").is(schoolYear);
            criteriFail.and("semester").is(semester);
            criteriFail.and("totalScore").lt(ScoreConstant.PASS_SCORE_LINE);
            long failTotal = mongoTemplate.count(query, Score.class);
            tss.setFailPassStuNum(new Long(failTotal).intValue());
            AggregationResults<BasicDBObject> failCount = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criteriFail),
                            Aggregation.group("collegeId").count().as("count"),
                            Aggregation.project("collegeId").and("cid").previousOperation()
                    ),
                    Score.class, BasicDBObject.class);
            int j = 0;
            while (failCount.iterator().hasNext()) {
                for(TeachingScoreStatistics ts:  tssList){
                    Long collegeId = countts.getMappedResults().get(j).getLong("cid");
                    if(collegeId.equals(ts.getCollegeId())){
                        ts.setFailPassStuNum(countts.getMappedResults().get(j).getInt("count"));
                        break;
                    }
                }
                j++;
            }
            tssList.add(tss);
            teachingScoreService.saveStatistics(tss);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "定时统计教学成绩失败！");
        }
        result.put("success", true);
        result.put("message", "定时统计教学成绩成功");
        return result;
    }

    public Map<String, Object> teachingScoreDetails(Long orgId,int schoolYear,int semester) {
        Map<String, Object> result = new HashMap<>();
        List<TeachingScoreDetails> tsdList = new ArrayList<>();
        try {
            //创建查询条件对象
            org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();
            //参考人数统计
            Criteria criteria = Criteria.where("orgId").is(orgId);
            criteria.and("scoreResultType").is(ScoreConstant.RESULT_TYPE_100);
            criteria.and("schoolYear").is(schoolYear);
            criteria.and("semester").is(semester);

            AggregationResults<BasicDBObject> scheduleDetails = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criteria),
                            Aggregation.group("userId").avg("gradePoint").as("GPAavg")
                                    .first("jobNum").as("jobNum").first("userName").as("userName").first("className").as("className")
                                    .first("grade").as("grade").first("collegeId").as("collegeId").first("collegeName").as("collegeName"),
                            Aggregation.project("userId").and("uid").previousOperation()
                    ),
                    Score.class, BasicDBObject.class);

            int i = 0;
            while (scheduleDetails.iterator().hasNext()){
                TeachingScoreDetails tsd = new TeachingScoreDetails();
                tsd.setOrgId(orgId);
                tsd.setTeacherYear(schoolYear);
                tsd.setSemester(semester);
                tsd.setCollegeId(scheduleDetails.getMappedResults().get(i).getLong("collegeId"));
                tsd.setCollegeName(scheduleDetails.getMappedResults().get(i).getString("collegeName"));
                tsd.setUserId(scheduleDetails.getMappedResults().get(i).getLong("uid"));
                tsd.setUserName(scheduleDetails.getMappedResults().get(i).getString("userName"));
                tsd.setJobNum(scheduleDetails.getMappedResults().get(i).getString("jobNum"));
                tsd.setGrade(scheduleDetails.getMappedResults().get(i).getInt("grade"));
                tsd.setClassName(scheduleDetails.getMappedResults().get(i).getString("className"));
                tsd.setAvgGPA(scheduleDetails.getMappedResults().get(i).getDouble("count"));
                tsdList.add(tsd);
                i++;
            }


            AggregationResults<BasicDBObject> scheduleStatis = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criteria),
                            Aggregation.group("userId", "scheduleId").count().as("count"),
                            Aggregation.project("userId").and("userId").previousOperation()
                    ),
                    Score.class, BasicDBObject.class);

            int j = 0;
            while (scheduleStatis.iterator().hasNext()){
                for(TeachingScoreDetails ts:  tsdList){
                    Long userId = scheduleStatis.getMappedResults().get(j).getLong("userId");
                    if(userId.equals(ts.getUserId())){
                        ts.setReferenceSubjects(scheduleStatis.getMappedResults().get(j).getInt("count"));
                        break;
                    }
                }
                j++;
            }

            //不及格人数统计
            Criteria criteriFail = Criteria.where("orgId").is(orgId);
            criteriFail.and("scoreResultType").is(ScoreConstant.RESULT_TYPE_100);
            criteriFail.and("schoolYear").is(schoolYear);
            criteriFail.and("semester").is(semester);
            criteriFail.and("totalScore").lt(ScoreConstant.PASS_SCORE_LINE);
            AggregationResults<BasicDBObject> scheduleFail = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criteriFail),
                            Aggregation.group("userId").count().as("count")),
                    Score.class, BasicDBObject.class);
            int x = 0;
            while (scheduleFail.iterator().hasNext()){
                for(TeachingScoreDetails ts:  tsdList){
                    Long userId = scheduleFail.getMappedResults().get(x).getLong("userId");
                    if(userId.equals(ts.getUserId())){
                        ts.setFailedSubjects(scheduleFail.getMappedResults().get(x).getInt("count"));
                        break;
                    }
                }
                x++;
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "定时统计学生成绩失败！");
        }
        result.put("success", true);
        result.put("message", "定时统计学生成绩成功！");
        return result;
    }

   /*****************************************编写测试查看数据**********************************************/
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

            items = mongoTemplate.find(query, Score.class);

//            scoreMongoRespository.save(items);

            AggregationResults<BasicDBObject> countts = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criteria),
                            Aggregation.group("collegeId", "scheduleId").first("collegeName").as("collegeName").count().as("count").avg("gradePoint").as("GPAavg").avg("totalScore").as("courseAVG"),
                            Aggregation.project("collegeId", "scheduleId").and("cid").previousOperation()
                    ),
                    Score.class, BasicDBObject.class);


            //mongoTemplate.count计算总数
            long total = mongoTemplate.count(query, Score.class);


        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取数据异常！");
            return result;
        }
        result.put("success",  items);
        return result;
    }






}
