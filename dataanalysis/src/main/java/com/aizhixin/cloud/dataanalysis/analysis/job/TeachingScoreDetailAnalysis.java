package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolYearTerm;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreDetails;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-27
 */
@Component
public class TeachingScoreDetailAnalysis {
    private Logger logger = Logger.getLogger(TeachingScoreDetailAnalysis.class);

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TeachingScoreService teachingScoreService;

    public void teachingScoreDetails(Set<SchoolYearTerm> schoolYearTermList) {
        List<TeachingScoreDetails> tsdList = new ArrayList<>();
        try {
        for(SchoolYearTerm schoolYearTerm : schoolYearTermList) {
            Long orgId = schoolYearTerm.getOrgId();
            Integer schoolYear = schoolYearTerm.getTeacherYear();
            Integer semester = schoolYearTerm.getSemester();
            if (null != orgId && null != schoolYear && null != semester) {

                    teachingScoreService.deleteScoreDeatail(orgId, schoolYear, semester);
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
                        tsdList.add(tsd);
                    }

                    //不及格
                    Criteria criteriFail = Criteria.where("orgId").is(orgId);
                    criteriFail.and("schoolYear").is(schoolYear);
                    criteriFail.and("semester").is(semester);
                    criteriFail.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
//            criteriFail.and("totalScore").lt(ScoreConstant.PASS_SCORE_LINE);
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
//                for(int z=0;z<scheduleFailCount.getMappedResults().size();z++){
//                     String scheduleId = scheduleFailCount.getMappedResults().get(z).getString("scheduleId");
//                    Criteria passCriteria = Criteria.where("orgId").is(orgId);
//                    passCriteria.and("schoolYear").is(schoolYear);
//                    passCriteria.and("semester").is(semester);
//                    passCriteria.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
//                    passCriteria.and("userId").is(userId);
//                    passCriteria.and("scheduleId").is(scheduleId);
//                    long passCount = mongoTemplate.count(new Query().addCriteria(passCriteria),Score.class);
//                    if(passCount>0){
//
//                    }
//                }
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
            }
        }
            teachingScoreService.saveDetailsList(tsdList);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("统计学生成绩详情失败！");
            return;
        }
        logger.info("统计学生成绩详情成功！");
    }



}
