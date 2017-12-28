package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolYearTerm;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreDetails;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreStatistics;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-27
 */
@Component
@Transactional
public class TeachingScoreStatisticsAnalysis {
    private Logger logger = Logger.getLogger(TeachingScoreStatisticsAnalysis.class);
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TeachingScoreService teachingScoreService;

    @Async
    public void teachingScoreDetails(Set<SchoolYearTerm> sytList) {
        try {
            for (SchoolYearTerm schoolYearTerm : sytList) {
                Long orgId = schoolYearTerm.getOrgId();
                Integer schoolYear = schoolYearTerm.getTeacherYear();
                Integer semester = schoolYearTerm.getSemester();
                if (null != orgId && null != schoolYear && null != semester) {
                    List<TeachingScoreDetails> tsdList = new ArrayList<>();
//                    teachingScoreService.deleteScoreDeatail(orgId, schoolYear, semester);
                    Criteria criteria = Criteria.where("orgId").is(orgId);
                    criteria.and("schoolYear").is(schoolYear);
                    criteria.and("semester").is(semester);
                    criteria.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
                    criteria.and("gradePoint").gte(0);
                    AggregationResults<BasicDBObject> scheduleDetails = mongoTemplate.aggregate(
                            Aggregation.newAggregation(
                                    Aggregation.match(criteria),
                                    Aggregation.group("userId").avg("gradePoint").as("GPAavg")
                                            .first("jobNum").as("jobNum").first("userName").as("userName").first("className").as("className")
                                            .first("grade").as("grade").first("collegeId").as("collegeId").first("collegeName").as("collegeName")
                            ),
                            Score.class, BasicDBObject.class);
                    logger.info(orgId + ":" + schoolYear + ":" + semester + "共:" + scheduleDetails.getMappedResults().size() + "条数据");
                    for (int i = 0; i < scheduleDetails.getMappedResults().size(); i++) {
                        Long userId = scheduleDetails.getMappedResults().get(i).getLong("_id");
                        TeachingScoreDetails tsd = new TeachingScoreDetails();
                        tsd.setOrgId(orgId);
                        tsd.setTeacherYear(schoolYear);
                        tsd.setSemester(semester);
                        tsd.setFailedSubjects(0);
                        tsd.setFailingGradeCredits(0.0);
                        Criteria criteriaSub = Criteria.where("orgId").is(orgId);
                        criteriaSub.and("schoolYear").is(schoolYear);
                        criteriaSub.and("semester").is(semester);
                        criteriaSub.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
                        criteriaSub.and("userId").is(userId);
                        AggregationResults<BasicDBObject> scheduleCount = mongoTemplate.aggregate(
                                Aggregation.newAggregation(Aggregation.match(criteriaSub), Aggregation.group("scheduleId")),
                                Score.class, BasicDBObject.class);
                        if (null != scheduleCount) {
                            tsd.setReferenceSubjects(scheduleCount.getMappedResults().size());
                        }
                        tsd.setCollegeId(scheduleDetails.getMappedResults().get(i).getLong("collegeId"));
                        tsd.setCollegeName(scheduleDetails.getMappedResults().get(i).getString("collegeName"));
                        tsd.setUserId(userId);
                        tsd.setUserName(scheduleDetails.getMappedResults().get(i).getString("userName"));
                        tsd.setJobNum(scheduleDetails.getMappedResults().get(i).getString("jobNum"));
                        tsd.setClassName(scheduleDetails.getMappedResults().get(i).getString("className"));
                        if (null != scheduleDetails.getMappedResults().get(i).get("grade")) {
                            tsd.setGrade(Integer.valueOf(scheduleDetails.getMappedResults().get(i).getString("grade")));
                        } else {
                            if (!StringUtils.isBlank(tsd.getClassName())) {
                                if (tsd.getClassName().indexOf("-") != -1) {
                                    int flag = tsd.getClassName().indexOf("-");
                                    tsd.setGrade(Integer.valueOf(tsd.getClassName().substring(flag - 4, flag)));
                                }
                            }
                        }
                        if (null != scheduleDetails.getMappedResults().get(i).get("GPAavg")) {
                            tsd.setAvgGPA(scheduleDetails.getMappedResults().get(i).getDouble("GPAavg"));
                            if (tsd.getAvgGPA() == 0) {
                                tsd.setFailedSubjects(tsd.getReferenceSubjects());
                            }
                        }
                        logger.info(orgId + ":" + schoolYear + ":" + semester + "教学成绩详情:" + i);
                        tsdList.add(tsd);
                    }

                    //不及格
                    Criteria criteriFail = Criteria.where("orgId").is(orgId);
                    criteriFail.and("schoolYear").is(schoolYear);
                    criteriFail.and("semester").is(semester);
                    criteriFail.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
                    criteriFail.and("gradePoint").is(0);
                    AggregationResults<BasicDBObject> scheduleFail = mongoTemplate.aggregate(
                            Aggregation.newAggregation(
                                    Aggregation.match(criteriFail),
                                    Aggregation.group("userId").sum("credit").as("credit")),
                            Score.class, BasicDBObject.class);

                    for (int x = 0; x < scheduleFail.getMappedResults().size(); x++) {
                        Long userId = scheduleFail.getMappedResults().get(x).getLong("_id");
                        Criteria criteriaFailSub = Criteria.where("orgId").is(orgId);
                        criteriaFailSub.and("schoolYear").is(schoolYear);
                        criteriaFailSub.and("semester").is(semester);
                        criteriaFailSub.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
                        criteriaFailSub.and("gradePoint").is(0);
                        criteriaFailSub.and("userId").is(userId);
                        AggregationResults<BasicDBObject> scheduleFailCount = mongoTemplate.aggregate(
                                Aggregation.newAggregation(Aggregation.match(criteriaFailSub), Aggregation.group("scheduleId")
                                        .first("scheduleId").as("scheduleId")),
                                Score.class, BasicDBObject.class);
                        for (TeachingScoreDetails ts : tsdList) {
                            if (userId.equals(ts.getUserId())) {
                                if (null != scheduleFailCount) {
                                    ts.setFailedSubjects(scheduleFailCount.getMappedResults().size());
                                }
                                if (null != scheduleFail.getMappedResults().get(x).get("credit")) {
                                    ts.setFailingGradeCredits(scheduleFail.getMappedResults().get(x).getDouble("credit"));
                                }
                                break;
                            }
                        }
                    }
                    logger.info(orgId + ":" + schoolYear + ":" + semester + "保存教学成绩详情数据:" + tsdList.size());
                    teachingScoreService.saveDetailsList(tsdList);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.info("统计教学成绩失败！");
            return;
        }
        logger.info("统计教学成绩成功!");
    }

    @Async
    public void teachingScoreStatistics(  Set<SchoolYearTerm> sytList) {
        try {
            for (SchoolYearTerm schoolYearTerm : sytList) {
                Long orgId = schoolYearTerm.getOrgId();
                Integer schoolYear = schoolYearTerm.getTeacherYear();
                Integer semester = schoolYearTerm.getSemester();
                if (null != orgId && null != schoolYear && null != semester) {
//                    teachingScoreService.deleteScoreStatistics(orgId, schoolYear, semester);
                    List<TeachingScoreStatistics> tssList = new ArrayList<>();
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
                    for (int j = 0; j < countts.getMappedResults().size(); j++) {
                        Long collegeId = countts.getMappedResults().get(j).getLong("_id");
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
                                        Aggregation.group("userId")), Score.class, BasicDBObject.class);
                        TeachingScoreStatistics ctss = new TeachingScoreStatistics();
                        ctss.setOrgId(orgId);
                        ctss.setTeacherYear(schoolYear);
                        ctss.setSemester(semester);
                        ctss.setStatisticsType(2); //按学院统计
                        ctss.setCollegeId(collegeId);
                        ctss.setCurriculumNum(schedule.getMappedResults().size());
                        ctss.setCollegeName(countts.getMappedResults().get(j).getString("collegeName"));
                        ctss.setStudentNum(user.getMappedResults().size());
                        if (null != countts.getMappedResults().get(j).get("GPAavg")) {
                            ctss.setAvgGPA(new BigDecimal(countts.getMappedResults().get(j).getDouble("GPAavg")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        }
                        if (null != countts.getMappedResults().get(j).get("courseAVG")) {
                            ctss.setAvgScore(new BigDecimal(countts.getMappedResults().get(j).getDouble("courseAVG")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        }
                        tssList.add(ctss);
                    }

                    //院不及格人数统计
                    for (TeachingScoreStatistics fts : tssList) {
                        Criteria criteriFailSub = Criteria.where("orgId").is(orgId);
                        criteriFailSub.and("schoolYear").is(schoolYear);
                        criteriFailSub.and("semester").is(semester);
                        criteriFailSub.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
//                       criteriFailSub.and("totalScore").lt(ScoreConstant.PASS_SCORE_LINE);
                        criteriFailSub.and("gradePoint").is(0);
                        criteriFailSub.and("collegeId").is(fts.getCollegeId());
                        AggregationResults<BasicDBObject> failUser = mongoTemplate.aggregate(
                                Aggregation.newAggregation(
                                        Aggregation.match(criteriFailSub),
                                        Aggregation.group("userId")),
                                Score.class, BasicDBObject.class);
                        fts.setFailPassStuNum(failUser.getMappedResults().size());
                    }
                    teachingScoreService.saveStatisticsList(tssList);
                }
            }
            }catch(Exception e){
                e.printStackTrace();
                logger.info("统计教学成绩失败！");
                return;
            }
            logger.info("统计教学成绩成功!");
        }

    }
