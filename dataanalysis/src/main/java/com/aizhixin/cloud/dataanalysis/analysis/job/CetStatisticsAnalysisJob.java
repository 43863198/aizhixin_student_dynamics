package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.constant.DataType;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CetScoreAnalysisDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.CetScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.CetStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolYearTerm;
import com.aizhixin.cloud.dataanalysis.analysis.respository.CetScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.service.CetStatisticsService;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolYearTermService;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.common.service.DistributeLock;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.mongodb.BasicDBObject;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private DistributeLock distributeLock;
    @Autowired
    private EntityManager em;
    @Autowired
    private CetStatisticsService cetStatisticsService;
//    @Autowired
//    private SchoolYearTermService schoolYearTermService;

//    @Transactional
//    public Map<String, Object>  cetScoreStatistics() {
//        Map<String, Object> result = new HashMap<>();
//        Set<SchoolYearTerm> sytList = new HashSet<>();
//        try {
//        Criteria cet = Criteria.where("examType").in(ScoreConstant.EXAM_TYPE_CET4, ScoreConstant.EXAM_TYPE_CET6);
//        AggregationResults<BasicDBObject> ytGroup = mongoTemplate.aggregate(
//                Aggregation.newAggregation(
//                        Aggregation.match(cet),
//                        Aggregation.group("orgId", "schoolYear", "semester").first("orgId").as("orgId").first("schoolYear").as("schoolYear")
//                                .first("semester").as("semester")
//                ), Score.class, BasicDBObject.class);
//
//        if (null != ytGroup) {
//            for (int x = 0; x < ytGroup.getMappedResults().size(); x++) {
//                SchoolYearTerm syt = new SchoolYearTerm();
//                syt.setOrgId(ytGroup.getMappedResults().get(x).getLong("orgId"));
//                syt.setTeacherYear(ytGroup.getMappedResults().get(x).getInt("schoolYear"));
//                syt.setSemester(ytGroup.getMappedResults().get(x).getInt("semester"));
//                sytList.add(syt);
//            }
//        }
//        if(sytList.size()>1){
//            for(SchoolYearTerm yt: sytList){
//                this.cetScoreStatistics(yt.getOrgId(), yt.getTeacherYear(), yt.getSemester());
////                yt.setDataType(DataType.t_cet_statistics.getIndex() + "");
////                schoolYearTermService.deleteSchoolYearTerm(yt.getOrgId(), yt.getDataType());
//            }
//        }
////        schoolYearTermService.saveSchoolYearTerm(sytList);
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.put("success", false);
//            result.put("message", "定时统计英语cet成绩失败！");
//            return result;
//        }
//        result.put("success", true);
//        result.put("message", "定时统计英语cet成绩成功!");
//        return result;
//    }
    @Async
    public void cetScoreStatistics(Set<SchoolYearTerm> sytList) {
        try {
            for (SchoolYearTerm schoolYearTerm : sytList) {
                Long orgId = schoolYearTerm.getOrgId();
                Integer schoolYear = schoolYearTerm.getTeacherYear();
                Integer semester = schoolYearTerm.getSemester();
                if (null != orgId && null != schoolYear && null != semester) {
                    StringBuilder path = new StringBuilder("/cet");
                    path.append(orgId).append("/").append(schoolYear).append("/").append(semester);
                    if (distributeLock.getLock(path)) {
                        cetScoreStatisticsRespository.deleteByOrgIdAndTeacherYearAndSemester(orgId, schoolYear, semester);
                        List<CetScoreStatistics> cetList = new ArrayList<>();
                        //英语四级参加人数统计
                        Criteria cet4 = Criteria.where("orgId").is(orgId);
                        cet4.and("schoolYear").is(schoolYear);
                        cet4.and("semester").is(semester);
                        cet4.and("examType").is(ScoreConstant.EXAM_TYPE_CET4);
                        cet4.and("totalScore").gte(0);//四级成绩大于0分的人计算为参加人
                        AggregationResults<BasicDBObject> collegeCet4 = mongoTemplate.aggregate(
                                Aggregation.newAggregation(
                                        Aggregation.match(cet4),
                                        Aggregation.group("$collegeId").count().as("count").first("collegeId").as("collegeId").first("collegeName").as("collegeName")
                                                .first("grade").as("grade")
                                ),
                                Score.class, BasicDBObject.class);
                        for (int i = 0; i < collegeCet4.getMappedResults().size(); i++) {
                            CetScoreStatistics cet = new CetScoreStatistics();
                            cet.setOrgId(orgId);
                            cet.setTeacherYear(schoolYear);
                            cet.setSemester(semester);
                            cet.setCetForePassNum(0);
                            cet.setCetSixPassNum(0);
                            cet.setCollegeId(collegeCet4.getMappedResults().get(i).getLong("_id"));
                            cet.setCollegeName(collegeCet4.getMappedResults().get(i).getString("collegeName"));
                            if (null != collegeCet4.getMappedResults().get(i).getString("grade")) {
                                cet.setGrade(Integer.valueOf(collegeCet4.getMappedResults().get(i).getString("grade")));
                            }
                            cet.setCetForeJoinNum(collegeCet4.getMappedResults().get(i).getInt("count"));
                            cetList.add(cet);
                        }
                        //英语四级通过人数统计
                        Criteria cet4Pass = Criteria.where("orgId").is(orgId);
                        cet4Pass.and("schoolYear").is(schoolYear);
                        cet4Pass.and("semester").is(semester);
                        cet4Pass.and("examType").is(ScoreConstant.EXAM_TYPE_CET4);
                        cet4Pass.and("totalScore").gte(ScoreConstant.CET_PASS_SCORE_LINE);
                        AggregationResults<BasicDBObject> collegeCet4Pass = mongoTemplate.aggregate(
                                Aggregation.newAggregation(
                                        Aggregation.match(cet4Pass),
                                        Aggregation.group("$collegeId").count().as("count")),
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
                        cet6.and("schoolYear").is(schoolYear);
                        cet6.and("semester").is(semester);
                        cet6.and("examType").is(ScoreConstant.EXAM_TYPE_CET6);
                        cet6.and("totalScore").gte(0);//六级成绩大于0分的人计算为参加人
                        AggregationResults<BasicDBObject> collegeCet6 = mongoTemplate.aggregate(
                                Aggregation.newAggregation(
                                        Aggregation.match(cet6),
                                        Aggregation.group("$collegeId").count().as("count")),
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
                        cet6Pass.and("schoolYear").is(schoolYear);
                        cet6Pass.and("semester").is(semester);
                        cet6Pass.and("examType").is(ScoreConstant.EXAM_TYPE_CET6);
                        cet6Pass.and("totalScore").gte(ScoreConstant.CET_PASS_SCORE_LINE);
                        AggregationResults<BasicDBObject> collegeCet6Pass = mongoTemplate.aggregate(
                                Aggregation.newAggregation(
                                        Aggregation.match(cet6Pass),
                                        Aggregation.group("$collegeId").count().as("count")),
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
                        distributeLock.delete(path);//删除锁
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("定时统计英语cet成绩失败！");
        }finally {
            StringBuilder path = new StringBuilder("/cet");
            distributeLock.delete(path);
        }
        logger.info("定时统计英语cet成绩成功!");
    }

    @Async
    public void cetScoreStatistics() {
        List<CetStatistics> caList = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,cs.EXAMINATION_DATE as date,ss.COLLEGE_CODE as collegeCode, " +
                    "ss.PROFESSION_CODE as professionCode,ss.CLASS_CODE as classCode,ss.GRADE as grade, sum(if(cs.SCORE > 0,1,0)) as count,sum(if(cs.SCORE > 425,1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max,s.SEX as sex " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY s.SEX,cs.ORG_ID,cs.EXAMINATION_DATE,cs.TYPE,ss.COLLEGE_CODE, ss.PROFESSION_CODE, ss.CLASS_CODE, ss.GRADE");
            Query sq = em.createNativeQuery(sql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Object> res =  sq.getResultList();
            if(null!=res&&res.size()>0){
                for(Object obj : res){
                    Map row = (Map)obj;
                    CetStatistics cs = new CetStatistics();
                    if(null!=row.get("type")){
                        cs.setScoreType(row.get("type").toString());
                    }
                    if(null!=row.get("orgId")){
                        cs.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if(null!=row.get("date")){
                        cs.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if(null!=row.get("collegeCode")){
                        cs.setCollegeCode(row.get("collegeCode").toString());
                    }
                    if(null!=row.get("professionCode")){
                        cs.setProfessionCode(row.get("professionCode").toString());
                    }
                    if(null!=row.get("classCode")){
                        cs.setClassCode(row.get("classCode").toString());
                    }
                    if(null!=row.get("grade")){
                        cs.setGrade(row.get("grade").toString());
                    }
                    if(null!=row.get("count")){
                        cs.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if(null!=row.get("avg")){
                        cs.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if(null!=row.get("max")){
                        cs.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    if(null!=row.get("sex")){
                        cs.setSex(row.get("sex").toString());
                    }
                    if(null!=row.get("pass")){
                        cs.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    caList.add(cs);
                }
            }
            if(caList.size()>0){
                cetStatisticsService.save(caList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("统计英语cet成绩失败！");
        }
        logger.info("统计英语cet成绩成功!");
    }





}
