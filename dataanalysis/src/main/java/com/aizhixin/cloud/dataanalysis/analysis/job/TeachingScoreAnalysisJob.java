package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.constant.DataType;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolYearTerm;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolYearTermService;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.mongodb.BasicDBObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private TeachingScoreStatisticsAnalysis teachingScoreStatisticsAnalysis;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SchoolYearTermService schoolYearTermService;
    @Autowired
    private TeachingScoreDetailAnalysis teachingScoreDetailAnalysis;


    @Async
    public Map<String, Object> teachingScoreStatisticsAsync() {
        Map<String, Object> reslut = new HashMap<>();
        Set<SchoolYearTerm> sytList = new HashSet<>();
        try {
            Criteria ytc = Criteria.where("examType").is(ScoreConstant.EXAM_TYPE_COURSE);
            ytc.and("schoolYear").ne(null);
            ytc.and("semester").ne(null);
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
                        teachingScoreStatisticsAnalysis.teachingScoreStatistics(yt.getOrgId(), yt.getTeacherYear(), yt.getSemester());
                        teachingScoreDetailAnalysis.teachingScoreDetails(yt.getOrgId(), yt.getTeacherYear(), yt.getSemester());
                        yt.setDataType(DataType.t_teaching_score_statistics.getIndex() + "");
                        schoolYearTermService.deleteSchoolYearTerm(yt.getOrgId(), yt.getDataType());
                    }
                }
            }
            schoolYearTermService.saveSchoolYearTerm(sytList);
        } catch (Exception e) {
            e.printStackTrace();
            reslut.put("message", "统计教学成绩启动失败！");
            return reslut;
        }
        reslut.put("message", "统计教学成绩启动成功！");
        return reslut;
    }

}
