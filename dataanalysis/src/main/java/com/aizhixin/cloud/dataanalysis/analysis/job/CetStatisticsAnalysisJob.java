package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.constant.DataType;
import com.aizhixin.cloud.dataanalysis.analysis.dto.CetScoreAnalysisDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.*;
import com.aizhixin.cloud.dataanalysis.analysis.respository.CetScoreStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.service.CetStatisticsService;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolYearTermService;
import com.aizhixin.cloud.dataanalysis.analysis.service.ScoreStatisticsService;
import com.aizhixin.cloud.dataanalysis.analysis.service.ScoreTopService;
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
    @Autowired
    private ScoreTopService scoreTopService;
    @Autowired
    private ScoreStatisticsService scoreStatisticsService;

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
        } finally {
            StringBuilder path = new StringBuilder("/cet");
            distributeLock.delete(path);
        }
        logger.info("定时统计英语cet成绩成功!");
    }

    @Async
    public void cetScoreStatisticsTop() {
        List<ScoreTop> stList = new ArrayList<>();
        try {
            StringBuilder topql = new StringBuilder("SELECT cs.JOB_NUMBER as xh, s.NAME as name, cs.TYPE as type,cs.ORG_ID as orgId,cs.EXAMINATION_DATE as date,d.COMPANY_NAME AS dName,ss.COLLEGE_CODE AS collegeCode, " +
                    "p.NAME AS pName, ss.PROFESSION_CODE as professionCode ,ss.CLASS_CODE as classCode, ss.CLASS_NAME AS cName,ss.GRADE as grade, MAX(cs.SCORE) as max " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "LEFT JOIN t_department d ON d.COMPANY_NUMBER = ss.COLLEGE_CODE "+
                    "LEFT JOIN t_profession p ON p. CODE = ss.PROFESSION_CODE "+
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY cs.ORG_ID,cs.EXAMINATION_DATE,cs.TYPE ORDER BY cs.SCORE DESC LIMIT 10");
            Query tq = em.createNativeQuery(topql.toString());
            tq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Object> tres = tq.getResultList();
            if (null != tres && tres.size() > 0) {
                for (Object obj : tres) {
                    Map row = (Map) obj;
                    ScoreTop st = new ScoreTop();
                    if (null != row.get("type")) {
                        st.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("orgId")) {
                        st.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        st.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("collegeCode")) {
                        st.setCollegeCode(row.get("collegeCode").toString());
                    }
                    if (null != row.get("dName")) {
                        st.setCollegeName(row.get("dName").toString());
                    }
                    if (null != row.get("professionCode")) {
                        st.setProfessionCode(row.get("professionCode").toString());
                    }
                    if (null != row.get("pName")) {
                        st.setProfessionName(row.get("pName").toString());
                    }
                    if (null != row.get("classCode")) {
                        st.setClassCode(row.get("classCode").toString());
                    }
                    if (null != row.get("cName")) {
                        st.setClassName(row.get("cName").toString());
                    }
                    if (null != row.get("grade")) {
                        st.setGrade(row.get("grade").toString());
                    }
                    if (null != row.get("max")) {
                        st.setMaxScore(row.get("max").toString());
                    }
                    if (null != row.get("xh")) {
                        st.setJobNumber(row.get("xh").toString());
                    }
                    if (null != row.get("name")) {
                        st.setName(row.get("name").toString());
                    }
                    stList.add(st);
                }
            }
            if (stList.size() > 0) {
                scoreTopService.save(stList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("统计英语cet成绩top失败！");
        }
        logger.info("统计英语cet成绩top成功!");
    }


    @Async
    public void cetScoreStatistics() {
        List<ScoreStatistics> ssList = new ArrayList<>();
        try {
            StringBuilder avgoql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,cs.ORG_ID as persion,cs.EXAMINATION_DATE as date,ss.ORG_ID as code, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count, sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,cs.EXAMINATION_DATE,cs.TYPE");
            StringBuilder avgdql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,cs.ORG_ID as persion,cs.EXAMINATION_DATE as date,ss.COLLEGE_CODE as code, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count,sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,cs.EXAMINATION_DATE,cs.TYPE,ss.COLLEGE_CODE");
            StringBuilder avgpql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,ss.COLLEGE_CODE as persion,cs.EXAMINATION_DATE as date,ss.PROFESSION_CODE as code, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count,sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,ss.COLLEGE_CODE,cs.EXAMINATION_DATE,cs.TYPE,ss.PROFESSION_CODE");
            StringBuilder avgcql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,ss.PROFESSION_CODE as persion,cs.EXAMINATION_DATE as date,ss.CLASS_CODE as code,ss.CLASS_NAME as className, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count,sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,ss.COLLEGE_CODE,ss.PROFESSION_CODE,cs.EXAMINATION_DATE,cs.TYPE,ss.CLASS_NAME");
            StringBuilder gradeoql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,cs.ORG_ID as persion,cs.EXAMINATION_DATE as date,cs.ORG_ID as code, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count,sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max,ss.GRADE as grade " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,cs.EXAMINATION_DATE,cs.TYPE,ss.GRADE");
            StringBuilder gradedql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,cs.ORG_ID as persion,cs.EXAMINATION_DATE as date,ss.COLLEGE_CODE as code, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count,sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max,ss.GRADE as grade " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,cs.EXAMINATION_DATE,cs.TYPE,ss.COLLEGE_CODE,ss.GRADE");
            StringBuilder gradepql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,ss.COLLEGE_CODE as persion,cs.EXAMINATION_DATE as date,ss.PROFESSION_CODE as code, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count,sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max,ss.GRADE as grade " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,ss.COLLEGE_CODE,cs.EXAMINATION_DATE,cs.TYPE,ss.PROFESSION_CODE,ss.GRADE");
            StringBuilder gradecql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,ss.PROFESSION_CODE as persion,cs.EXAMINATION_DATE as date,ss.CLASS_CODE as code,ss.CLASS_NAME as className, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count,sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max,ss.GRADE as grade " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,ss.COLLEGE_CODE,ss.PROFESSION_CODE,cs.EXAMINATION_DATE,cs.TYPE,ss.CLASS_NAME,ss.GRADE");
            StringBuilder sexoql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,cs.ORG_ID as persion,cs.EXAMINATION_DATE as date,cs.ORG_ID as code, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count,sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max,s.SEX as sex " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,cs.EXAMINATION_DATE,cs.TYPE,s.SEX");
            StringBuilder sexdql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,cs.ORG_ID as persion,cs.EXAMINATION_DATE as date,ss.COLLEGE_CODE as code, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count,sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max,s.SEX as sex " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,cs.EXAMINATION_DATE,cs.TYPE,ss.COLLEGE_CODE,s.SEX");
            StringBuilder sexpql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,ss.COLLEGE_CODE as persion,cs.EXAMINATION_DATE as date,ss.PROFESSION_CODE as code, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count,sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max,s.SEX as sex " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,ss.COLLEGE_CODE,cs.EXAMINATION_DATE,cs.TYPE,ss.PROFESSION_CODE,s.SEX");
            StringBuilder sexclql = new StringBuilder("SELECT cs.TYPE as type,cs.ORG_ID as orgId,ss.PROFESSION_CODE as persion,cs.EXAMINATION_DATE as date,ss.CLASS_CODE as code,ss.CLASS_NAME as className, " +
                    "sum(if(cs.SCORE > 0,1,0)) as count,sum(if(SCORE >= (if(TYPE = '大学英语三级考试',60,425 )),1,0)) as pass,AVG(cs.SCORE) as avg,MAX(cs.SCORE) as max,s.SEX as sex " +
                    "FROM t_cet_score cs " +
                    "LEFT JOIN t_student_status ss ON cs.JOB_NUMBER = ss.JOB_NUMBER " +
                    "LEFT JOIN t_student s ON cs.JOB_NUMBER = s.JOB_NUMBER " +
                    "WHERE cs.TYPE LIKE '%大学英语%' AND cs.SCORE > 0 " +
                    "GROUP BY ss.ORG_ID,ss.COLLEGE_CODE,ss.PROFESSION_CODE,cs.EXAMINATION_DATE,cs.TYPE,ss.CLASS_NAME,s.SEX");
            Query avgoq = em.createNativeQuery(avgoql.toString());
            Query avgdq = em.createNativeQuery(avgdql.toString());
            Query avgpq = em.createNativeQuery(avgpql.toString());
            Query avgcq = em.createNativeQuery(avgcql.toString());
            Query gradeoq = em.createNativeQuery(gradeoql.toString());
            Query gradedq = em.createNativeQuery(gradedql.toString());
            Query gradepq = em.createNativeQuery(gradepql.toString());
            Query gradecq = em.createNativeQuery(gradecql.toString());
            Query sexoq = em.createNativeQuery(sexoql.toString());
            Query sexdq = em.createNativeQuery(sexdql.toString());
            Query sexpq = em.createNativeQuery(sexpql.toString());
            Query sexclq = em.createNativeQuery(sexclql.toString());
            avgoq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            avgdq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            avgpq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            avgcq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            gradeoq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            gradedq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            gradepq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            gradecq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            sexoq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            sexdq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            sexpq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            sexclq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Object> avgores = avgoq.getResultList();
            List<Object> avgdres = avgdq.getResultList();
            List<Object> avgpres = avgpq.getResultList();
            List<Object> avgcres = avgcq.getResultList();
            List<Object> gradeores = gradeoq.getResultList();
            List<Object> gradedres = gradedq.getResultList();
            List<Object> gradepres = gradepq.getResultList();
            List<Object> gradecres = gradecq.getResultList();
            List<Object> sexores = sexoq.getResultList();
            List<Object> sexdres = sexdq.getResultList();
            List<Object> sexpres = sexpq.getResultList();
            List<Object> sexclres = sexclq.getResultList();
            if (null != avgores && avgores.size() > 0) {
                for (Object obj : avgores) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("000");
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    ssList.add(ss);
                }
            }
            if (null != avgdres && avgdres.size() > 0) {
                for (Object obj : avgdres) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("001");
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    ssList.add(ss);
                }
            }
            if (null != avgpres && avgpres.size() > 0) {
                for (Object obj : avgpres) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("002");
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    ssList.add(ss);
                }
            }
            if (null != avgcres && avgcres.size() > 0) {
                for (Object obj : avgcres) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("003");
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    if(null!=row.get("className")){
                       ss.setClassName(row.get("className").toString());
                    }
                    ssList.add(ss);
                }
            }
            if (null != gradeores && gradeores.size() > 0) {
                for (Object obj : gradeores) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("020");
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("grade")) {
                        ss.setName(row.get("grade").toString());
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    ssList.add(ss);
                }
            }
            if (null != gradedres && gradedres.size() > 0) {
                for (Object obj : gradedres) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("021");
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("grade")) {
                        ss.setName(row.get("grade").toString());
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    ssList.add(ss);
                }
            }
            if (null != gradepres && gradepres.size() > 0) {
                for (Object obj : gradepres) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("022");
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("grade")) {
                        ss.setName(row.get("grade").toString());
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    ssList.add(ss);
                }
            }
            if (null != gradecres && gradecres.size() > 0) {
                for (Object obj : gradecres) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("023");
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("grade")) {
                        ss.setName(row.get("grade").toString());
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    if(null!=row.get("className")){
                        ss.setClassName(row.get("className").toString());
                    }
                    ssList.add(ss);
                }
            }
            if (null != sexores && sexores.size() > 0) {
                for (Object obj : sexores) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("010");
                    if (null != row.get("sex")) {
                        ss.setName(row.get("sex").toString());
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    ssList.add(ss);
                }
            }
            if (null != sexdres && sexdres.size() > 0) {
                for (Object obj : sexdres) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("011");
                    if (null != row.get("sex")) {
                        ss.setName(row.get("sex").toString());
                    }
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    ssList.add(ss);
                }
            }
            if (null != sexpres && sexpres.size() > 0) {
                for (Object obj : sexpres) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("012");
                    if (null != row.get("sex")) {
                        ss.setName(row.get("sex").toString());
                    }
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    ssList.add(ss);
                }
            }
            if (null != sexclres && sexclres.size() > 0) {
                for (Object obj : sexclres) {
                    Map row = (Map) obj;
                    ScoreStatistics ss = new ScoreStatistics();
                    ss.setStatisticsType("013");
                    if (null != row.get("sex")) {
                        ss.setName(row.get("sex").toString());
                    }
                    if (null != row.get("type")) {
                        ss.setScoreType(row.get("type").toString());
                    }
                    if (null != row.get("persion")) {
                        ss.setParentCode(row.get("persion").toString());
                    }
                    if (null != row.get("orgId")) {
                        ss.setOrgId(Long.valueOf(row.get("orgId").toString()));
                    }
                    if (null != row.get("date")) {
                        ss.setExamDate(new SimpleDateFormat("yyyy-MM-dd").parse(row.get("date").toString()));
                    }
                    if (null != row.get("code")) {
                        ss.setCode(row.get("code").toString());
                    }
                    if (null != row.get("count")) {
                        ss.setJoinNumber(Integer.valueOf(row.get("count").toString()));
                    }
                    if (null != row.get("pass")) {
                        ss.setPassNumber(Integer.valueOf(row.get("pass").toString()));
                    }
                    if (null != row.get("avg")) {
                        ss.setAvgScoure(Double.valueOf(row.get("avg").toString()));
                    }
                    if (null != row.get("max")) {
                        ss.setMaxScore(Double.valueOf(row.get("max").toString()));
                    }
                    if(null!=row.get("className")){
                        ss.setClassName(row.get("className").toString());
                    }
                    ssList.add(ss);
                }
            }
            scoreStatisticsService.deleteAll();
            if (ssList.size() > 0) {
                scoreStatisticsService.save(ssList);
                logger.info(ssList.size()+"数据长度@@@@@@@@@@@@@@@@@");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("统计英语cet成绩失败！");
        }
        logger.info("统计英语cet成绩成功!");
    }


}
