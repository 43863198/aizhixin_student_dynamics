package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.constant.DataType;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolYearTerm;
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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
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
    private TeachingScoreService teachingScoreService;
    @Autowired
    private SchoolYearTermService schoolYearTermService;

//    public Map<String, Object> teachingScoreStatistics() {
//        Map<String, Object> reslut = new HashMap<>();
//        teachingScoreStatisticsAsync();
//        reslut.put("message","教学成绩统计任务开始...");
//        return reslut;
//    }

    public Map<String, Object> teachingScoreStatistics() {
        Map<String, Object> reslut = new HashMap<>();
        Set<SchoolYearTerm> sytList = new HashSet<>();
        try {
            Criteria ytc = Criteria.where("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
            AggregationResults<BasicDBObject> ytGroup = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(ytc),
                            Aggregation.group("orgId", "schoolYear", "semester").first("orgId").as("orgId").first("schoolYear").as("schoolYear")
                                    .first("semester").as("semester")
                    ), Score.class, BasicDBObject.class);

            if (null != ytGroup) {
                for (int x = 0; x < ytGroup.getMappedResults().size(); x++) {
                    SchoolYearTerm syt = new SchoolYearTerm();
                    syt.setOrgId(ytGroup.getMappedResults().get(x).getLong("orgId"));
                    syt.setTeacherYear(ytGroup.getMappedResults().get(x).getInt("schoolYear"));
                    syt.setSemester(ytGroup.getMappedResults().get(x).getInt("semester"));
                    sytList.add(syt);
                }
            }
            if(sytList.size()>1){
                for(SchoolYearTerm yt: sytList){
                    if(null!=yt.getSemester()&&null!=yt.getTeacherYear()) {
                        this.teachingScoreStatistics(yt.getOrgId(), yt.getTeacherYear(), yt.getSemester());
                        this.teachingScoreDetails(yt.getOrgId(), yt.getTeacherYear(), yt.getSemester());
                        yt.setDataType(DataType.t_teaching_score_statistics.getIndex() + "");
                        schoolYearTermService.deleteSchoolYearTerm(yt.getOrgId(), yt.getDataType());
                    }
                }
            }
            schoolYearTermService.saveSchoolYearTerm(sytList);
        } catch (Exception e) {
            e.printStackTrace();
            reslut.put("message","统计教学成绩启动失败！");
            return reslut;
        }
        reslut.put("message", "统计教学成绩启动成功！");
        return reslut;
    }

    @Transactional
    @Async
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
            criterias.and("gradePoint").ne(null);
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
            ccriteria.and("gradePoint").ne(null);
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
                ccriteriaSub.and("gradePoint").ne(null);
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
            logger.info("定时统计教学成绩失败！");
            return;
        }
        logger.info("定时统计教学成绩成功!");
    }

    @Transactional
    @Async
    public void teachingScoreDetails(Long orgId, Integer schoolYear, Integer semester) {
        List<TeachingScoreDetails> tsdList = new ArrayList<>();
        try {
            teachingScoreService.deleteScoreDeatail(orgId, schoolYear, semester);
            Criteria criteria = Criteria.where("orgId").is(orgId);
            criteria.and("schoolYear").is(schoolYear);
            criteria.and("semester").is(semester);
            criteria.and("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
            criteria.and("gradePoint").ne(null);
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
                        if(tsd.getAvgGPA()==0){
                            tsd.setFailedSubjects(tsd.getReferenceSubjects()  );
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
            teachingScoreService.saveDetailsList(tsdList);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("定时统计学生成绩详情失败！");
            return;
        }
        logger.info("定时统计学生成绩详情成功！");
    }

}
