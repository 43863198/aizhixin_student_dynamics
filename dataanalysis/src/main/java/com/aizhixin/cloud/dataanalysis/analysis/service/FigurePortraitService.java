package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.analysis.vo.*;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-11
 */
@Service
public class FigurePortraitService {
    @Autowired
    private EntityManager em;
    @Autowired
    private AlertWarningInformationService alertWarningInformationService;

    public PageData<StudentInfoVO> getStudentList(Long orgId, String collegeNumber, String professionNumber, String classNumber,String className, String nj, Integer pageNumber,Integer pageSize) {
        PageData<StudentInfoVO> p = new PageData<>();
        List<StudentInfoVO> dataList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        try {
            StringBuilder sql = new StringBuilder("SELECT XH as xh,XM as xm,YXSMC as xy,ZYMC as zy,BJMC as bj,NJ as nj " +
                    "FROM t_xsjbxx WHERE 1 = 1");
            StringBuilder cql = new StringBuilder("SELECT  count(XH) as count FROM t_xsjbxx WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" AND XXID = :orgId");
                cql.append(" AND XXID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != collegeNumber) {
                sql.append(" AND YXSH = :collegeNumber");
                cql.append(" AND YXSH = :collegeNumber");
                condition.put("collegeNumber", collegeNumber);
            }
            if (null != professionNumber) {
                sql.append(" AND ZYH = :professionNumber");
                cql.append(" AND ZYH = :professionNumber");
                condition.put("professionNumber", professionNumber);
            }
            if (null != classNumber&&null!=className) {
                sql.append(" AND BH = :classNumber");
                cql.append(" AND BH = :classNumber");
                sql.append(" AND BJMC = :className");
                cql.append(" AND BJMC = :className");
                condition.put("classNumber", classNumber);
                condition.put("className", className);
            }
            if (!StringUtils.isBlank(nj)) {
                sql.append(" AND (XM LIKE :name or XH LIKE :xh)");
                cql.append(" AND (XM LIKE :name or XH LIKE :xh)");
                condition.put("name", "%" + nj + "%");
                condition.put("xh", "%"+nj+"%");
            }
            sql.append(" AND CURDATE() BETWEEN RXNY AND YBYNY");
            cql.append(" AND CURDATE() BETWEEN RXNY AND YBYNY");
            Query sq = em.createNativeQuery(sql.toString());
            Query cq = em.createNativeQuery(cql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
                cq.setParameter(e.getKey(), e.getValue());
            }
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Long count = Long.valueOf(String.valueOf(cq.getSingleResult()));
            if (null == pageNumber || pageNumber < 1) {
                pageNumber = 1;
            }
            if (null == pageSize) {
                pageSize = 20;
            }
            sq.setFirstResult((pageNumber - 1) * pageSize);
            sq.setMaxResults(pageSize);
            List<Object> res = sq.getResultList();
            for (Object obj : res) {
                Map row = (Map) obj;
                StudentInfoVO s = new StudentInfoVO();
                if (null != row.get("xh")) {
                    s.setJobNumber(row.get("xh").toString());
                }
                if (null != row.get("xm")) {
                    s.setName(row.get("xm").toString());
                }
                if (null != row.get("xy")) {
                    s.setCollegeName(row.get("xy").toString());
                }
                if (null != row.get("zy")) {
                    s.setProfessionName(row.get("zy").toString());
                }
                if (null != row.get("bj")) {
                    s.setClassName(row.get("bj").toString());
                }
                if (null != row.get("nj")) {
                    s.setGrade(row.get("nj").toString());
                }
                dataList.add(s);
            }
            p.setData(dataList);
            p.getPage().setPageNumber(pageNumber);
            p.getPage().setPageSize(pageSize);
            p.getPage().setTotalElements(count);
            p.getPage().setTotalPages(PageUtil.cacalatePagesize(count, p.getPage().getPageSize()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    public Map<String, Object> getPersonalFigurePortrait(Long orgId, String jobNumber) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        StudentFigurePortraitVO sfp = new StudentFigurePortraitVO();
        try {
            //学生信息
            StringBuilder student = new StringBuilder("SELECT XH as xh,XM as xm,YXSMC as xy,ZYMC as zy,BJMC as bj,NJ as nj,XB as xb,CSRQ as csrq,CSD as csd,RXNY as rxny,YBYNY as ybyny " +
                    "FROM t_xsjbxx WHERE 1 = 1 AND XH = :jobNumber limit 1");
            Query studentsql = em.createNativeQuery(student.toString());
            studentsql.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            studentsql.setParameter("jobNumber", jobNumber);
            Object sdata = studentsql.getSingleResult();
            if(null!=sdata) {
                Map srow = (Map) sdata;
                StudentInfoVO sVO = new StudentInfoVO();
                if (null != srow.get("xh")) {
                    sVO.setJobNumber(srow.get("xh").toString());
                }
                if (null != srow.get("xm")) {
                    sVO.setName(srow.get("xm").toString());
                }
                if (null != srow.get("xy")) {
                    sVO.setCollegeName(srow.get("xy").toString());
                }
                if (null != srow.get("zy")) {
                    sVO.setProfessionName(srow.get("zy").toString());
                }
                if (null != srow.get("bj")) {
                    sVO.setClassName(srow.get("bj").toString());
                }
                if (null != srow.get("nj")) {
                    sVO.setGrade(srow.get("nj").toString());
                }
                if(null!=srow.get("xb")){
                    sVO.setSex(srow.get("xb").toString());
                }
                if(null!=srow.get("csrq")){
                    sVO.setDateOfBirth(srow.get("csrq").toString());
                }
                if(null!=srow.get("rxny")){
                    sVO.setRxq(srow.get("rxny").toString());
                }
                if(null!=srow.get("ybyny")){
                    sVO.setByrq(srow.get("ybyny").toString());
                }
                sfp.setStudentIn(sVO);
            }
            //成绩详情
            StringBuilder scoresql = new StringBuilder("SELECT s.KCH as kch,c.COURSE_NAME as cName,s.JSXM as teacher, c.CREDIT as kxf,s.XKSX as kclx,s.DJLKSCJ as djlkscj,s.PSCJ as pscj,s.FSLKSCJ as fslksch,s.JD as jd " +
                    "FROM t_xscjxx s LEFT JOIN t_course c ON s.KCH = c.COURSE_NUMBER WHERE 1 = 1 AND XH = :jobNumber");
            Query scoresq = em.createNativeQuery(scoresql.toString());
            scoresq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            scoresq.setParameter("jobNumber", jobNumber);
            List<Object> crow = scoresq.getResultList();
            List<ScoreVO> scoreList = new ArrayList<>();
            if(null!=crow){
            for(Object d : crow) {
                Map row = (Map) d;
                ScoreVO scVO = new ScoreVO();
                if (null != row.get("kch")) {
                    scVO.setCourseNumber(row.get("kch").toString());
                }
                if (null != row.get("cName")) {
                    scVO.setCourseName(row.get("cName").toString());
                }
                if (null != row.get("teacher")) {
                    scVO.setTeacherName(row.get("teacher").toString());
                }
                if (null != row.get("teacher")) {
                    scVO.setTeacherName(row.get("teacher").toString());
                }
                if (null != row.get("kxf")) {
                    scVO.setCredit(Float.valueOf(row.get("kxf").toString()));
                }
                if (null != row.get("kclx")) {
                    scVO.setCorseType(row.get("kclx").toString());
                }
                if (null != row.get("djlkscj")) {
                    scVO.setGradeExamScore(row.get("djlkscj").toString());
                }
                if (null != row.get("pscj")) {
                    scVO.setAcademicGPA(row.get("pscj").toString());
                }
                if (null != row.get("fslksch")) {
                    scVO.setFractionalExamScore(row.get("fslksch").toString());
                }
                if (null != row.get("jd")) {
                    scVO.setGpaPoint(Float.valueOf(row.get("jd").toString()));
                }
                scoreList.add(scVO);
            }
            }
            sfp.setScoreDetail(scoreList);
            //成绩统计
            CourseScoreVO csVO = new CourseScoreVO();
            StringBuilder scoreStaticql = new StringBuilder("SELECT sum(If(FSLKSCJ >= 60,1,0)) as pass, count(1) as total " +
                    "FROM t_xscjxx WHERE 1 = 1 AND XH = :jobNumber");
            Query scoreStaticq = em.createNativeQuery(scoreStaticql.toString());
            scoreStaticq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            scoreStaticq.setParameter("jobNumber", jobNumber);
            Object ssData = scoreStaticq.getSingleResult();
            if(null!=ssData) {
                Map ssrow = (Map) ssData;
                if (null != ssrow.get("total")) {
                    csVO.setCourseTotalNumber(Integer.valueOf(ssrow.get("total").toString()));
                }
                if (null != ssrow.get("pass")) {
                    csVO.setCoursePassNumber(Integer.valueOf(ssrow.get("pass").toString()));
                }
                csVO.setCourseFailNumber(csVO.getCourseTotalNumber() - csVO.getCoursePassNumber());
                float xj = 0;
                float xf = 0;
                for (ScoreVO cl : scoreList) {
                    xj = xj + cl.getCredit() * cl.getGpaPoint();
                    xf = xf + cl.getCredit();
                }
                if(xf!=0) {
                    csVO.setGAP(new DecimalFormat("0.00").format((double) xj / xf));
                }
            }
            sfp.setCourseScore(csVO);
            //等级考试
            StringBuilder cetql = new StringBuilder("SELECT TYPE as type, max(SCORE) as max, EXAMINATION_DATE as date " +
                    "FROM t_cet_score WHERE 1 = 1 AND JOB_NUMBER = :jobNumber GROUP BY TYPE");
            Query cetq = em.createNativeQuery(cetql.toString());
            cetq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            cetq.setParameter("jobNumber", jobNumber);
            List<Object> cetres = cetq.getResultList();
            List<GradeExamScoreVO> cetList = new ArrayList<>();
            if(null!=cetres) {
                for (Object obj : cetres) {
                    Map d = (Map) obj;
                    GradeExamScoreVO geVO = new GradeExamScoreVO();
                    if (null != d.get("type")) {
                        geVO.setExamType(d.get("type").toString());
                    }
                    if (null != d.get("max")) {
                        geVO.setMaxScore(d.get("max").toString());
                    }
                    if (null != d.get("date")) {
                        geVO.setExamDate(d.get("date").toString());
                    }
                    cetList.add(geVO);
                }
            }
            sfp.setGradeExam(cetList);
            result.put("success", true);
            result.put("data", sfp);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "人物画像---学生个人画像获取计失败！");
            return result;
        }
    }

    public Map<String, Object> getEarlyWarning(Long orgId, String jobNumber) {
        Map<String, Object> result = new HashMap<>();
        List<EarlyWarningVO> ewVOList = new ArrayList<>();
        try {
            List<WarningInformation> warningInformationList = alertWarningInformationService.getawinfoByOrgIdAndJobNumber(orgId,jobNumber);
            if(null!=warningInformationList&&warningInformationList.size()>0){
                for(WarningInformation w : warningInformationList){
                    EarlyWarningVO  ew = new EarlyWarningVO();
                    ew.setWarningTime(w.getWarningTime().toString());
                    ew.setWarningCondition(w.getWarningCondition());
                    ew.setWarningLevel(w.getWarningLevel());
                    ew.setWarningName(WarningTypeConstant.valueOf(w.getWarningType()).getValue());
                    ew.setWarningState(w.getWarningState());
                    ew.setWarningSource(w.getWarningSource());
                    ewVOList.add(ew);
                }
            }
            result.put("success", true);
            result.put("data", ewVOList);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "人物画像---学生个人画像---预警信息获取失败！");
            return result;
        }
    }


    public Map<String, Object> getGroupPortrait(Long orgId,String collegeCode,String professionCode,String classCode) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        GroupPortraitVO grVO = new GroupPortraitVO();
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try {
            //性别分布
            StringBuilder sexql = new StringBuilder("SELECT XB as xb,count(DISTINCT XH) as count FROM t_xsjbxx WHERE 1 = 1 ");
            StringBuilder sql = new StringBuilder("");
            if (null != orgId) {
                sql.append(" and XXID = :orgId");
                condition.put("orgId", orgId);
            }
            if (!StringUtils.isBlank(collegeCode)) {
                sql.append(" and YXSH = :collegeCode");
                condition.put("collegeCode", collegeCode);
            }
            if (!StringUtils.isBlank(professionCode)) {
                sql.append(" and ZYH = :professionCode");
                condition.put("professionCode", professionCode);
            }
            if (!StringUtils.isBlank(classCode)) {
                sql.append(" and BH = :classCode");
                condition.put("classCode", classCode);
            }
            sexql.append(sql);
            sexql.append(" AND XB IS NOT NULL AND CURDATE() BETWEEN RXNY AND YBYNY");
            sexql.append(" GROUP BY XB");
            Query sq = em.createNativeQuery(sexql.toString());
            sq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> sexres = sq.getResultList();
            List<ValueVo> sexList = new ArrayList<>();
            if(null!=sexres) {
                for(Object obj : sexres) {
                    Map srow = (Map) obj;
                    ValueVo sex = new ValueVo();
                    if (null != srow.get("xb")) {
                        sex.setName(srow.get("xb").toString());
                        if (null != srow.get("count")) {
                            sex.setValue(srow.get("count").toString());
                        }
                        sexList.add(sex);
                    }

                }
            }
            grVO.setSexList(sexList);

            //年龄分布
            StringBuilder ageql = new StringBuilder("SELECT ROUND(DATEDIFF(CURDATE(), CSRQ) / 365.2422)  as age, count(DISTINCT XH) as count FROM t_xsjbxx WHERE 1 = 1 ");
            ageql.append(sql);
            ageql.append(" AND CURDATE() BETWEEN RXNY AND YBYNY GROUP BY  age");
            Query aq = em.createNativeQuery(ageql.toString());
            aq.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                aq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> ageres = aq.getResultList();
            List<ValueVo> ageList = new ArrayList<>();
            if(null!=ageres) {
                for(Object obj : ageres) {
                    Map srow = (Map) obj;
                    ValueVo age = new ValueVo();
                    if (null != srow.get("age")) {
                        age.setName(srow.get("age").toString());
                        if (null != srow.get("count")) {
                            age.setValue(srow.get("count").toString());
                        }
                        ageList.add(age);
                    }
                }
            }
            grVO.setAgeList(ageList);
            result.put("success", true);
            result.put("data", grVO);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "人物画像---群体画像获取计失败！");
            return result;
        }
    }



}
