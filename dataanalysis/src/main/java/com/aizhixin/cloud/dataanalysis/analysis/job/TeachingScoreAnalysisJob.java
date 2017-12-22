package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreDetails;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.service.TeachingScoreService;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.ScoreMongoRespository;
import com.aizhixin.cloud.dataanalysis.setup.domain.WarningTypeDomain;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import com.mongodb.BasicDBObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-18
 */
@Component
@Transactional
public class TeachingScoreAnalysisJob {
    private Logger logger = Logger.getLogger(TeachingScoreAnalysisJob.class);
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private WarningTypeService warningTypeService;
    @Autowired
    private ScoreMongoRespository scoreMongoRespository;
    @Autowired
    private TeachingScoreService teachingScoreService;

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
    @Transactional
    public Map<String, Object> teachingScoreStatistics(Long orgId, Integer schoolYear,Integer semester) {
        Map<String, Object> result = new HashMap<>();
        List<TeachingScoreStatistics> tssList  = new ArrayList<>();
        try {
            TeachingScoreStatistics tss = new TeachingScoreStatistics();
            tss.setOrgId(orgId);
            tss.setTeacherYear(schoolYear);
            tss.setSemester(semester);
            tss.setStatisticsType(1); //全校统计
            //参考人数统计
            Criteria criterias = Criteria.where("orgId").is(orgId);
            criterias.and("schoolYear").is(schoolYear);
            criterias.and("semester").is(semester);
            criterias.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
            AggregationResults<BasicDBObject> orgSchedule = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criterias),
                            Aggregation.group("scheduleId")),
                    Score.class, BasicDBObject.class);
            AggregationResults<BasicDBObject> orgUser = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criterias),
                            Aggregation.group("userId")),
                    Score.class, BasicDBObject.class);
            AggregationResults<BasicDBObject> avg = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criterias),
                            Aggregation.group("orgId").avg("gradePoint").as("GPAavg").avg("totalScore").as("courseAVG")),
                    Score.class, BasicDBObject.class);
            tss.setAvgGPA(avg.getMappedResults().get(0).getDouble("GPAavg"));
            tss.setAvgScore(avg.getMappedResults().get(0).getDouble("courseAVG"));
            tss.setCurriculumNum(orgSchedule.getMappedResults().size());
            tss.setStudentNum(orgUser.getMappedResults().size());
            Criteria tcriteriFail = Criteria.where("orgId").is(orgId);
            tcriteriFail.and("schoolYear").is(schoolYear);
            tcriteriFail.and("semester").is(semester);
            tcriteriFail.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
            tcriteriFail.and("totalScore").lt(ScoreConstant.PASS_SCORE_LINE);
            AggregationResults<BasicDBObject> orgUserFail = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(tcriteriFail),
                            Aggregation.group("userId")),
                    Score.class, BasicDBObject.class);
            tss.setFailPassStuNum(orgUserFail.getMappedResults().size());
            teachingScoreService.saveStatistics(tss);
            //院级统计
            Criteria ccriteria = Criteria.where("orgId").is(orgId);
            ccriteria.and("schoolYear").is(schoolYear);
            ccriteria.and("semester").is(semester);
            ccriteria.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
            AggregationResults<BasicDBObject> countts = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(ccriteria),
                            Aggregation.group("collegeId").first("collegeName").as("collegeName").avg("gradePoint").as("GPAavg").avg("totalScore").as("courseAVG")
                    ),
                    Score.class, BasicDBObject.class);
            for (int i=0;i<countts.getMappedResults().size();i++){
                Long collegeId = countts.getMappedResults().get(i).getLong("_id");
                Criteria ccriteriaSub = Criteria.where("orgId").is(orgId);
                ccriteriaSub.and("schoolYear").is(schoolYear);
                ccriteriaSub.and("semester").is(semester);
                ccriteriaSub.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
                ccriteriaSub.and("collegeId").is(collegeId);
                AggregationResults<BasicDBObject> schedule = mongoTemplate.aggregate(
                        Aggregation.newAggregation(
                                Aggregation.match(ccriteriaSub),
                                Aggregation.group("scheduleId")),
                        Score.class, BasicDBObject.class);
                AggregationResults<BasicDBObject> user = mongoTemplate.aggregate(
                        Aggregation.newAggregation(
                                Aggregation.match(ccriteriaSub),
                                Aggregation.group("userId")),
                        Score.class, BasicDBObject.class);
                TeachingScoreStatistics ctss = new TeachingScoreStatistics();
                ctss.setOrgId(orgId);
                ctss.setTeacherYear(schoolYear);
                ctss.setSemester(semester);
                ctss.setStatisticsType(2); //按学院统计
                ctss.setCollegeId(collegeId);
                ctss.setCurriculumNum(schedule.getMappedResults().size());
                ctss.setCollegeName(countts.getMappedResults().get(i).getString("collegeName"));
                ctss.setStudentNum(user.getMappedResults().size());
                ctss.setAvgGPA(new BigDecimal(countts.getMappedResults().get(i).getDouble("GPAavg")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                ctss.setAvgScore(new BigDecimal(countts.getMappedResults().get(i).getDouble("courseAVG")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                tssList.add(ctss);
            }

            //院不及格人数统计
            Criteria criteriFail = Criteria.where("orgId").is(orgId);
            criteriFail.and("schoolYear").is(schoolYear);
            criteriFail.and("semester").is(semester);
            criteriFail.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
            criteriFail.and("totalScore").lt(ScoreConstant.PASS_SCORE_LINE);
            for(TeachingScoreStatistics fts:  tssList){
                Criteria criteriFailSub = Criteria.where("orgId").is(orgId);
                criteriFailSub.and("schoolYear").is(schoolYear);
                criteriFailSub.and("semester").is(semester);
                criteriFailSub.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
                criteriFailSub.and("totalScore").lt(ScoreConstant.PASS_SCORE_LINE);
                criteriFailSub.and("collegeId").is(fts.getCollegeId());
                AggregationResults<BasicDBObject> failUser = mongoTemplate.aggregate(
                        Aggregation.newAggregation(
                                Aggregation.match(criteriFailSub),
                                Aggregation.group("userId")),
                        Score.class, BasicDBObject.class);
                fts.setFailPassStuNum(failUser.getMappedResults().size());
            }
            teachingScoreService.saveStatisticsList(tssList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "定时统计教学成绩失败！");
            return result;
        }
        result.put("success", true);
        result.put("message", "定时统计教学成绩成功");
        return result;
    }
    @Transactional
    public Map<String, Object> teachingScoreDetails(Long orgId,Integer schoolYear,Integer semester) {
        Map<String, Object> result = new HashMap<>();
        List<TeachingScoreDetails> tsdList = new ArrayList<>();
        try {
            //参考人数统计
            Criteria criteria = Criteria.where("orgId").is(orgId);
            criteria.and("schoolYear").is(schoolYear);
            criteria.and("semester").is(semester);
            criteria.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
            AggregationResults<BasicDBObject> scheduleDetails = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criteria),
                            Aggregation.group("userId").avg("gradePoint").as("GPAavg").count().as("count")
                                    .first("jobNum").as("jobNum").first("userName").as("userName").first("className").as("className")
                                    .first("grade").as("grade").first("collegeId").as("collegeId").first("collegeName").as("collegeName")
                    ),
                    Score.class, BasicDBObject.class);
            for (int i =0;i<scheduleDetails.getMappedResults().size();i++){
                Long userId = scheduleDetails.getMappedResults().get(i).getLong("_id");
                TeachingScoreDetails tsd = new TeachingScoreDetails();
                tsd.setOrgId(orgId);
                tsd.setTeacherYear(schoolYear);
                tsd.setSemester(semester);
                tsd.setFailedSubjects(0);
                tsd.setFailingGradeCredits(0.0);
                tsd.setReferenceSubjects(scheduleDetails.getMappedResults().get(i).getInt("count"));
                tsd.setCollegeId(scheduleDetails.getMappedResults().get(i).getLong("collegeId"));
                tsd.setCollegeName(scheduleDetails.getMappedResults().get(i).getString("collegeName"));
                tsd.setUserId(userId);
                tsd.setUserName(scheduleDetails.getMappedResults().get(i).getString("userName"));
                tsd.setJobNum(scheduleDetails.getMappedResults().get(i).getString("jobNum"));
                if(null!=scheduleDetails.getMappedResults().get(i).getString("grade")) {
                    tsd.setGrade(Integer.valueOf(scheduleDetails.getMappedResults().get(i).getString("grade")));
                }
                tsd.setClassName(scheduleDetails.getMappedResults().get(i).getString("className"));
                tsd.setAvgGPA(scheduleDetails.getMappedResults().get(i).getDouble("GPAavg"));
                tsdList.add(tsd);
            }

            //不及格
            Criteria criteriFail = Criteria.where("orgId").is(orgId);
            criteriFail.and("schoolYear").is(schoolYear);
            criteriFail.and("semester").is(semester);
            criteriFail.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
            criteriFail.and("totalScore").lt(ScoreConstant.PASS_SCORE_LINE);
            AggregationResults<BasicDBObject> scheduleFail = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(criteriFail),
                            Aggregation.group("userId").count().as("count").sum("credit").as("credit")),
                    Score.class, BasicDBObject.class);

            for (int x = 0;x<scheduleFail.getMappedResults().size();x++){
                Long userId = scheduleFail.getMappedResults().get(x).getLong("_id");
                for(TeachingScoreDetails ts:  tsdList){
                    if(userId.equals(ts.getUserId())){
                        ts.setFailedSubjects(scheduleFail.getMappedResults().get(x).getInt("count"));
                        ts.setFailedSubjects(Integer.parseInt(String.valueOf(scheduleFail.getMappedResults().get(x).getDouble("credit"))));
                        break;
                    }
                }
            }
            teachingScoreService.saveDetailsList(tsdList);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "定时统计学生成绩失败！");
            return result;
        }
        result.put("success", true);
        result.put("message", "定时统计学生成绩成功！");
        return result;
    }

}
