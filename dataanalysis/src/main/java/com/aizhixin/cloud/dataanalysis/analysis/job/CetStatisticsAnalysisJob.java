package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.entity.CetScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.CetScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.mongodb.BasicDBObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-13
 */
@Component
@Transactional
public class CetStatisticsAnalysisJob {
    private Logger logger = Logger.getLogger(CetStatisticsAnalysisJob.class);
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CetScoreStatisticsRespository cetScoreStatisticsRespository;

    public Map<String, Object> cetScoreStatistics(Long orgId, int teacherYear,int semester) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<CetScoreStatistics> cetList = new ArrayList<>();
        try {
            cetScoreStatisticsRespository.deleteByOrgIdAndTeacherYearAndSemester(orgId, teacherYear,semester);
            //英语四级参加人数统计
            Criteria cet4 = Criteria.where("orgId").is(orgId);
            cet4.and("schoolYear").is(teacherYear);
            cet4.and("semester").is(semester);
            cet4.and("examType").is(ScoreConstant.EXAM_TYPE_CET4);
            AggregationResults<BasicDBObject> collegeCet4 = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(cet4),
                            Aggregation.group("collegeId").count().as("count").first("collegeId").as("collegeId").first("collegeName").as("collegeName")
                                    .first("grade").as("grade")
                    ),
                    Score.class, BasicDBObject.class);
            for (int i = 0; i < collegeCet4.getMappedResults().size(); i++) {
                CetScoreStatistics cet = new CetScoreStatistics();
                cet.setOrgId(orgId);
                cet.setTeacherYear(teacherYear);
                cet.setSemester(semester);
                cet.setCetForePassNum(0);
                cet.setCetSixPassNum(0);
                cet.setCollegeId(collegeCet4.getMappedResults().get(i).getLong("_id"));
                cet.setCollegeName(collegeCet4.getMappedResults().get(i).getString("collegeName"));
                if(null!=collegeCet4.getMappedResults().get(i).getString("grade")) {
                    cet.setGrade(Integer.valueOf(collegeCet4.getMappedResults().get(i).getString("grade")));
                }
                cet.setCetForeJoinNum(collegeCet4.getMappedResults().get(i).getInt("count"));
                cetList.add(cet);
            }
            //英语四级通过人数统计
            Criteria cet4Pass = Criteria.where("orgId").is(orgId);
            cet4Pass.and("schoolYear").is(teacherYear);
            cet4Pass.and("semester").is(semester);
            cet4.and("examType").is(ScoreConstant.EXAM_TYPE_CET4);
            cet4Pass.and("totalScore").gte(ScoreConstant.CET_PASS_SCORE_LINE);
            AggregationResults<BasicDBObject> collegeCet4Pass = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(cet4Pass),
                            Aggregation.group("collegeId").count().as("count")),
                    Score.class, BasicDBObject.class);
            for (int j = 0; j < collegeCet4Pass.getMappedResults().size(); j++) {
                Long collegeId = collegeCet4Pass.getMappedResults().get(j).getLong("_id");
                for (CetScoreStatistics cs : cetList) {
                    if (cs.getCollegeId().equals(collegeId)) {
                        cs.setCetForePassNum(collegeCet4Pass.getMappedResults().get(j).getInt("count"));
                    }
                }
            }
            //英语六级参加人数统计
            Criteria cet6 = Criteria.where("orgId").is(orgId);
            cet6.and("schoolYear").is(teacherYear);
            cet6.and("semester").is(semester);
            cet6.and("examType").is(ScoreConstant.EXAM_TYPE_CET6);
            AggregationResults<BasicDBObject> collegeCet6 = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(cet6),
                            Aggregation.group("collegeId").count().as("count")),
                    Score.class, BasicDBObject.class);
            for (int m = 0; m < collegeCet6.getMappedResults().size(); m++) {
                Long collegeId = collegeCet6.getMappedResults().get(m).getLong("_id");
                for (CetScoreStatistics cs : cetList) {
                    if (cs.getCollegeId().equals(collegeId)) {
                        cs.setCetSixJoinNum(collegeCet6.getMappedResults().get(m).getInt("count"));
                    }
                }
            }

            //英语六级通过人数统计
            Criteria cet6Pass = Criteria.where("orgId").is(orgId);
            cet6Pass.and("schoolYear").is(teacherYear);
            cet6Pass.and("semester").is(semester);
            cet6Pass.and("examType").is(ScoreConstant.EXAM_TYPE_CET6);
            cet6Pass.and("totalScore").gte(ScoreConstant.CET_PASS_SCORE_LINE);
            AggregationResults<BasicDBObject> collegeCet6Pass = mongoTemplate.aggregate(
                    Aggregation.newAggregation(
                            Aggregation.match(cet6Pass),
                            Aggregation.group("collegeId").count().as("count")),
                    Score.class, BasicDBObject.class);
            for (int n = 0; n < collegeCet6Pass.getMappedResults().size(); n++) {
                Long collegeId = collegeCet6Pass.getMappedResults().get(n).getLong("_id");
                for (CetScoreStatistics cs : cetList) {
                    if (cs.getCollegeId().equals(collegeId)) {
                        cs.setCetSixPassNum(collegeCet6Pass.getMappedResults().get(n).getInt("count"));
                    }
                }
            }
            cetScoreStatisticsRespository.save(cetList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "定时统计英语cet成绩失败！");
            return result;
        }
        result.put("success", true);
        result.put("message", "定时统计英语cet成绩成功!");
        return result;
    }
}
