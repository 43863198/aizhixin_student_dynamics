package com.aizhixin.cloud.dataanalysis.etl.score.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.etl.cet.dto.XnXqDTO;
import com.aizhixin.cloud.dataanalysis.etl.score.dto.StudentScorezbDTO;
import com.aizhixin.cloud.dataanalysis.etl.score.dto.StudentSemesterScoreAlertIndexDTO;
import com.aizhixin.cloud.dataanalysis.etl.score.dto.StudentSemesterScoreIndexDTO;
import com.aizhixin.cloud.dataanalysis.etl.score.manager.StandardScoreSemesterManager;
import com.aizhixin.cloud.dataanalysis.etl.score.manager.StudentScoreIndexManager;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.RuleParameter;
import com.aizhixin.cloud.dataanalysis.setup.service.RuleParameterService;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudentSemesterScoreIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.mananger.StudentSemesterScoreIndexManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 学生学习成绩学期指标
 */
@Component
@Slf4j
public class StandardScoreSemesterIndexService {
    @Autowired
    private StandardScoreSemesterManager standardScoreSemesterManager;
    @Autowired
    private StudentSemesterScoreIndexManager studentSemesterScoreIndexManager;
    @Autowired
    private StudentScoreIndexManager studentScoreIndexManager;
    @Autowired
    private RuleParameterService ruleParameterService;

    @Async
    public void oneSemesterStudentScoreIndex(String xxdm, String xn, String xq) {
        standardScoreSemesterManager.deleteXnXqIndex(xxdm, xn, xq);

        Map<String, StudentSemesterScoreIndexDTO> zbMap = new HashMap<>();
        List<StudentSemesterScoreIndexDTO> zbList = standardScoreSemesterManager.queryStudentSemsterIndex(xxdm, xn, xq);
        for (StudentSemesterScoreIndexDTO d : zbList) {
            zbMap.put(d.getXh(), d);
        }

        List<StudentSemesterScoreIndex> data = standardScoreSemesterManager.queryStudentSemsterGpaIndex(xxdm, xn, xq);
        for (StudentSemesterScoreIndex d : data) {
            d.setXxdm(xxdm);
            d.setXn(xn);
            d.setXqm(xq);
            StudentSemesterScoreIndexDTO zb = zbMap.get(d.getXh());
            if (null != zb) {
                d.setCkkcs(zb.getCkkcs());
                d.setBjgkcs(zb.getBjgkcs());
                d.setBjgzxf(zb.getBjgzxf());
            } else {
                System.out.println("No zb---------" + d.toString());
            }
        }
        if (!data.isEmpty()) {
            studentSemesterScoreIndexManager.save(data);
        }
    }

    @Async
    public void allSemesterStudentScoreIndex(String xxdm) {
        List<XnXqDTO> list = standardScoreSemesterManager.queryAllYearAndSemester(xxdm);
        for (XnXqDTO xnxq : list) {
            oneSemesterStudentScoreIndex(xxdm, xnxq.getXn(), xnxq.getXq());
        }
    }

     @Async
    public void schoolStudentScoreIndex(Long orgId) {
        List<StudentScorezbDTO> cache = new ArrayList<>();
        List<XnXqDTO> xnList = studentScoreIndexManager.queryStudentScoreXnXq(StudentScoreIndexManager.SQL_DC_XN_XQ, orgId.toString());
        for (XnXqDTO d : xnList) {
            List<StudentScorezbDTO> rs = studentScoreIndexManager.queryDczb(StudentScoreIndexManager.SQL_DC_COLLEGE, orgId.toString(), d.getXn(), d.getXq());
            if (null != rs && !rs.isEmpty()) {
                cache.addAll(rs);
            }
            rs = studentScoreIndexManager.queryDczb(StudentScoreIndexManager.SQL_DC_PROFESSIONAL, orgId.toString(), d.getXn(), d.getXq());
            if (null != rs && !rs.isEmpty()) {
                cache.addAll(rs);
            }
        }
        if (!cache.isEmpty()) {
            studentScoreIndexManager.saveStudentScoreIndex(cache, orgId.toString());
        }
    }

    public List<WarningInformation> generalStudentSemesterScoreBdAlertInfo (Long orgId, String xn, String xq, AlarmSettings as) {
        List<WarningInformation> rsList = new ArrayList<>();
        Set<String> paramSet = new HashSet<>();
        if (null != as.getRuleSet()) {
            if (as.getRuleSet().indexOf(",") != -1) {
                String[] ruleSetIds = as.getRuleSet().split(",");
                for (String t : ruleSetIds) {
                    paramSet.add(t);
                }
            } else {
                paramSet.add(as.getRuleSet());
            }
        }

        if (!paramSet.isEmpty()) {
            List<RuleParameter> ruleParams = ruleParameterService.getRuleParameterByIds(paramSet);
            if (null == ruleParams || ruleParams.isEmpty()) {
                log.info("No score PerformanceFluctuation alert params");
                return rsList;
            }
            List<String> xnxqs = standardScoreSemesterManager.queryLastestSemester(orgId.toString());
            if (null == xnxqs || xnxqs.size() != 2) {
                log.warn("Not found lastest 2 semester data.");
                return rsList;
            }
            String jxn = null, jxq = null, yxn = null, yxq = null, tmp = null;
            int p = 0;
            tmp = xnxqs.get(0);
            p = tmp.lastIndexOf("-");
            if (p > 0) {
                jxn = tmp.substring(0, p);
                jxq = tmp.substring(p + 1);
            }
            tmp = xnxqs.get(1);
            p = tmp.lastIndexOf("-");
            if (p > 0) {
                yxn = tmp.substring(0, p);
                yxq = tmp.substring(p + 1);
            }
            if (null == jxn || null == jxq || null == yxn || null == yxq) {
                log.warn("");
                return rsList;
            }
            List<StudentSemesterScoreAlertIndexDTO> semesterScoreAlertIndexDTOList = standardScoreSemesterManager.queryStudentScoreAlertBd(orgId, jxn, jxq, yxn, yxq);
            if (null != semesterScoreAlertIndexDTOList && !semesterScoreAlertIndexDTOList.isEmpty()) {
                log.info("Query score PerformanceFluctuation data:{}", semesterScoreAlertIndexDTOList.size());
                for (StudentSemesterScoreAlertIndexDTO e : semesterScoreAlertIndexDTOList) {
                    WarningInformation warn = new WarningInformation ();
                    StringBuilder desc = new StringBuilder();
                    StringBuilder st = new StringBuilder();
                    boolean alert = false;

                    //缺省只处理只有一种确定指标的情况
                    alert = true;
                    for (RuleParameter r : ruleParams) {
                        st.append("[").append(r.getRuledescribe()).append(":").append(r.getRightParameter()).append("]");
                        desc.append("[");
                        tmp = e.getJxnxq();
                        if (null != tmp) {
                            String[] tc = tmp.split("\\-");
                            if (3 == tc.length) {
                                if ("1".equals(tc[2])) {
                                    desc.append(tc[0]).append("年秋");
                                } else {
                                    desc.append(tc[1]).append("年春");
                                }
                            }
                            desc.append("平均学分绩点为:").append(e.getJgpa()).append(";");
                        }
                        tmp = e.getYxnxq();
                        if (null != tmp) {
                            String[] tc = tmp.split("\\-");
                            if (3 == tc.length) {
                                if ("1".equals(tc[2])) {
                                    desc.append(tc[0]).append("年秋");
                                } else {
                                    desc.append(tc[1]).append("年春");
                                }
                            }
                            desc.append("平均学分绩点为:").append(e.getYgpa()).append(";");
                        }
                        desc.append("平均学分绩点下降:").append(e.getGpa()).append("]");
                        if (new Double(r.getRightParameter()) < e.getGpa()) {
                            alert = false;
                        }
                    }

                    if (!alert) {
                        continue;
                    }
                    warn.setName(e.getXm());
                    warn.setJobNumber(e.getXh());
                    warn.setCollogeCode(e.getYxsh());
                    warn.setCollogeName(e.getYxsmc());
                    warn.setClassCode(e.getBjbh());
                    warn.setClassName(e.getBjmc());
                    warn.setProfessionalCode(e.getZyh());
                    warn.setProfessionalName(e.getZymc());
                    warn.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
                    warn.setWarningType(as.getWarningType());
                    warn.setWarningLevel(as.getWarningLevel());
                    warn.setWarningTime(new Date());
                    warn.setSemester(xq);
                    warn.setTeacherYear(xn);
                    warn.setOrgId(orgId);
                    rsList.add(warn);
                }
            } else {
                log.warn("Not found any score PerformanceFluctuation data");
            }
        } else {
            log.warn("Not found any alert set params.");
        }
        return rsList;
    }
}
