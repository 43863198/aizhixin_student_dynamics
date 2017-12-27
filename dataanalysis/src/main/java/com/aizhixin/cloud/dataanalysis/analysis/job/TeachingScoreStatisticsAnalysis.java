package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreDetails;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolYearTermService;
import com.aizhixin.cloud.dataanalysis.analysis.service.TeachingScoreService;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-27
 */
@Component
public class TeachingScoreStatisticsAnalysis {
    private Logger logger = Logger.getLogger(TeachingScoreStatisticsAnalysis.class);
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TeachingScoreService teachingScoreService;

    @Transactional
    public void teachingScoreStatistics(Long orgId, Integer schoolYear, Integer semester) {
        List<TeachingScoreStatistics> tssList = new ArrayList<>();
        try {
            teachingScoreService.deleteScoreStatistics(orgId, schoolYear, semester);
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
            criterias.and("gradePoint").gte(0);
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
            if (null != avg.getMappedResults().get(0)) {
                if (null != avg.getMappedResults().get(0).get("GPAavg")) {
                    tss.setAvgGPA(avg.getMappedResults().get(0).getDouble("GPAavg"));
                }
                if (null != avg.getMappedResults().get(0).get("courseAVG")) {
                    tss.setAvgScore(avg.getMappedResults().get(0).getDouble("courseAVG"));
                }
            }
            tss.setCurriculumNum(orgSchedule.getMappedResults().size());
            tss.setStudentNum(orgUser.getMappedResults().size());
            Criteria tcriteriFail = Criteria.where("orgId").is(orgId);
            tcriteriFail.and("schoolYear").is(schoolYear);
            tcriteriFail.and("semester").is(semester);
            tcriteriFail.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
//            tcriteriFail.and("totalScore").lt(ScoreConstant.PASS_SCORE_LINE);
            tcriteriFail.and("gradePoint").is(0);
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
            ccriteria.and("gradePoint").gte(0);
            AggregationResults<BasicDBObject> countts = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(ccriteria),
                            Aggregation.group("collegeId").first("collegeName").as("collegeName").avg("gradePoint").as("GPAavg").avg("totalScore").as("courseAVG")
                    ),
                    Score.class, BasicDBObject.class);
            for (int i = 0; i < countts.getMappedResults().size(); i++) {
                Long collegeId = countts.getMappedResults().get(i).getLong("_id");
                Criteria ccriteriaSub = Criteria.where("orgId").is(orgId);
                ccriteriaSub.and("schoolYear").is(schoolYear);
                ccriteriaSub.and("semester").is(semester);
                ccriteriaSub.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
                ccriteriaSub.and("gradePoint").gte(0);
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
                if (null != countts.getMappedResults().get(i).get("GPAavg")) {
                    ctss.setAvgGPA(new BigDecimal(countts.getMappedResults().get(i).getDouble("GPAavg")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                if (null != countts.getMappedResults().get(i).get("courseAVG")) {
                    ctss.setAvgScore(new BigDecimal(countts.getMappedResults().get(i).getDouble("courseAVG")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                tssList.add(ctss);
            }

            //院不及格人数统计
            Criteria criteriFail = Criteria.where("orgId").is(orgId);
            criteriFail.and("schoolYear").is(schoolYear);
            criteriFail.and("semester").is(semester);
            criteriFail.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
//            criteriFail.and("totalScore").lt(ScoreConstant.PASS_SCORE_LINE);
            criteriFail.and("gradePoint").is(0);
            for (TeachingScoreStatistics fts : tssList) {
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
            logger.info("统计教学成绩失败！");
            return;
        }
        logger.info("统计教学成绩成功!");
    }

}
