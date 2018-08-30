package com.aizhixin.cloud.dataanalysis.etl.study.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.RuleParameter;
import com.aizhixin.cloud.dataanalysis.setup.service.RuleParameterService;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudyExceptionIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.mananger.StudyExceptionIndexManager;
import com.aizhixin.cloud.dataanalysis.etl.study.manager.StudyExceptionIndexZbManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 修读异常指标计算
 */
@Component
@Slf4j
public class StudyExceptionIndexService {

    @Autowired
    private StudyExceptionIndexZbManager studyExceptionIndexZbManager;
    @Autowired
    private StudyExceptionIndexManager studyExceptionIndexManager;
    @Autowired
    private RuleParameterService ruleParameterService;

    /**
     * 修读异常指标计算
     */
    @Async
    public void calCurrentDateIndex(Long orgId) {
        Date current = new Date();
        int year, month;
        String xn, xq;
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        if (month >= 9 || month < 3) {
            xq = "1";
        } else {
            xq = "2";
        }
        if (month >= 9) {
            xn = year + "-" + (year + 1);
        } else {
            xn = (year - 1) + "-" + year;
        }

        studyExceptionIndexManager.deleteByXxdmAndXnAndXqm(orgId.toString(), xn, xq);
        log.info("Clear xxdm({}) xn({}), xq({}) studyException index data", orgId, xn, xq);
        List<String> collegesList = studyExceptionIndexZbManager.queryAllYxsh(orgId, current);
        for (String yxsh : collegesList) {
            log.info("start cal yxsh({}) studyException index", yxsh);
            List<StudyExceptionIndex> dataList = studyExceptionIndexZbManager.queryXyxdyczb(orgId, yxsh, current);
            if (null != dataList) {
                for (StudyExceptionIndex d : dataList) {
                    d.setXxdm(orgId.toString());
                    d.setXn(xn);
                    d.setXqm(xq);
                }
                studyExceptionIndexManager.save(dataList);
                log.info("Save count({}) yxsh ({}) studyException index data", dataList.size(), yxsh);
            }
        }

        log.info("complete cal xxdm({}) xn({}), xq({}) studyException index.", orgId, xn, xq);
    }

    /**
     * 修读异常告警计算
     */
//    @Async
    public List<WarningInformation> generalAlertInfo (Long orgId, String xn, String xq, AlarmSettings as) {
        List<WarningInformation> rsList = new ArrayList<>();
        String zxn = null;
        String zxq = null;
        if (xn.indexOf("-") < 0) {//非标准学年学期转换
            if ("秋".equals(xq)) {
                zxn = xn + "-" + (new Integer(xn) + 1);
                zxq = "1";
            } else if ("春".equals(xq)) {
                zxn = (new Integer(xn) - 1) + "-" + xn;
                zxq = "2";
            }
        } else {
            zxn = xn;
            zxq = xq;
        }
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
                log.info("No study exception alert params");
                return rsList;
            }

            List<StudyExceptionIndex> studyExceptionIndexList = studyExceptionIndexManager.findByXxdmAndXnAndXqm(orgId.toString(), zxn, zxq);
            if(studyExceptionIndexList.isEmpty()) {
                zxn = studyExceptionIndexManager.findLastestXnByXxdm(orgId.toString());
                if (null != xn) {
                    zxq = studyExceptionIndexManager.findLastestXqmByXxdmAndXn(orgId.toString(), zxn);
                    if (null != zxq) {
                        studyExceptionIndexList = studyExceptionIndexManager.findByXxdmAndXnAndXqm(orgId.toString(), zxn, zxq);
                    }
                }
            }
            if (null != studyExceptionIndexList && !studyExceptionIndexList.isEmpty()) {
                for (StudyExceptionIndex e : studyExceptionIndexList) {
                    WarningInformation warn = new WarningInformation ();
                    if ("与".equals(as.getRelationship())) {
                        createAndAlertDescAndAlertIndex(rsList, ruleParams, e, warn);
                    } else {
                        createOrAlertDescAndAlertIndex(rsList, ruleParams, e, warn);
                    }
                    warn.setName(e.getXm());
                    warn.setJobNumber(e.getXh());
                    warn.setCollogeCode(e.getYxsh());
                    warn.setCollogeName(e.getYxsmc());
                    warn.setClassCode(null);
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
                    warn.setWarningTime(new Date ());
                }
            }
        }
        return rsList;
    }


    private void createAndAlertDescAndAlertIndex(List<WarningInformation> rsList, List<RuleParameter> ruleParams, StudyExceptionIndex e, WarningInformation warn) {
        StringBuilder desc = new StringBuilder();
        StringBuilder st = new StringBuilder();
        StringBuilder ss = new StringBuilder();
        boolean alter = true;
        String tmp;
        int p;
        for(RuleParameter r : ruleParams) {
            if (alter && null != r.getRuledescribe() && null != r.getRightParameter()) {
                st.append("[").append(r.getRuledescribe() + ":" + r.getRightParameter()).append("]");
                if (r.getRuledescribe().indexOf("漏选的课程数量≥") >= 0) {
                    if (e.getLxkcs() < new Integer(r.getRightParameter())) {
                        alter = false;
                        break;
                    } else {
                        desc.append("[").append(r.getRuledescribe()).append(e.getLxkcs()).append("]");
                        ss.append("[").append(e.getLxkcnr()).append("]");
                    }
                } else if (r.getRuledescribe().indexOf("漏选的课程学分≥") >= 0) {
                    if (e.getLxxf() < new Double(r.getRightParameter())) {
                        alter = false;
                        break;
                    } else {
                        desc.append("[").append(r.getRuledescribe()).append(e.getLxxf()).append("]");
                        ss.append("[").append(e.getLxkcnr()).append("]");
                    }
                } else if (r.getRuledescribe().indexOf("课程门数占教学计划的比例≤") >= 0) {
                    if (e.getTgkcs() * 100.0 / e.getKcs() > new Double(r.getRightParameter())) {
                        alter = false;
                        break;
                    } else {
                        tmp = (e.getTgkcs() * 100.0 / e.getKcs()) + "";
                        p = tmp.indexOf(".");
                        if (p > 0 && p + 2 < tmp.length()) {
                            tmp = tmp.substring(0, p + 2);
                        }
                        desc.append("[").append(r.getRuledescribe()).append(tmp).append("]");
                    }
                } else if (r.getRuledescribe().indexOf("课程学分占教学计划的比例≤") >= 0) {
                    if (e.getTgxf() * 100.0 / e.getXf() > new Double(r.getRightParameter())) {
                        alter = false;
                        break;
                    } else {
                        tmp = (e.getTgxf() * 100.0 / e.getXf()) + "";
                        p = tmp.indexOf(".");
                        if (p > 0 && p + 2 < tmp.length()) {
                            tmp = tmp.substring(0, p + 2);
                        }
                        desc.append("[").append(r.getRuledescribe()).append(tmp).append("]");
                    }
                } else if (r.getRuledescribe().indexOf("考核不合格的必修课程（含集中性实践教学环节）学分≥") >= 0) {
                    if (e.getBxbjgxf() < new Double(r.getRightParameter())) {
                        alter = false;
                        break;
                    } else {
                        desc.append("[").append(r.getRuledescribe()).append(e.getBxbjgxf()).append("]");
                        ss.append("[").append(e.getBxbjgkcnr()).append("]");
                    }
                }
            }
        }
        if (alter) {
            warn.setWarningCondition(desc.toString());
            warn.setWarningStandard(st.toString());
            warn.setWarningSource(ss.toString());
            rsList.add(warn);
        }
    }
    private void createOrAlertDescAndAlertIndex(List<WarningInformation> rsList, List<RuleParameter> ruleParams, StudyExceptionIndex e, WarningInformation warn) {
        StringBuilder desc = new StringBuilder();
        StringBuilder st = new StringBuilder();
        StringBuilder ss = new StringBuilder();
        String tmp;
        int p;
        for(RuleParameter r : ruleParams) {
            if (null != r.getRuledescribe() && null != r.getRightParameter()) {
                st.append("[").append(r.getRuledescribe() + ":" + r.getRightParameter()).append("]");
                if (r.getRuledescribe().indexOf("漏选的课程数量≥") >= 0) {
                    if (e.getLxkcs() < new Integer(r.getRightParameter())) {
                        break;
                    } else {
                        desc.append("[").append(r.getRuledescribe()).append(e.getLxkcs()).append("]");
                        ss.append("[").append(e.getLxkcnr()).append("]");
                    }
                } else if (r.getRuledescribe().indexOf("漏选的课程学分≥") >= 0) {
                    if (e.getLxxf() < new Double(r.getRightParameter())) {
                        break;
                    } else {
                        desc.append("[").append(r.getRuledescribe()).append(e.getLxxf()).append("]");
                        ss.append("[").append(e.getLxkcnr()).append("]");
                    }
                } else if (r.getRuledescribe().indexOf("课程门数占教学计划的比例≤") >= 0) {
                    if (e.getLxkcs() * 100.0 / e.getKcs() > new Double(r.getRightParameter())) {
                        break;
                    } else {
                        tmp = (e.getLxkcs() * 100.0 / e.getKcs()) + "";
                        p = tmp.indexOf(".");
                        if (p > 0 && p + 2 < tmp.length()) {
                            tmp = tmp.substring(0, p);
                        }
                        desc.append("[").append(r.getRuledescribe()).append(tmp).append("]");
                    }
                } else if (r.getRuledescribe().indexOf("课程学分占教学计划的比例≤") >= 0) {
                    if (e.getTgxf() * 100.0 / e.getXf() > new Double(r.getRightParameter())) {
                        break;
                    } else {
                        tmp = (e.getTgxf() * 100.0 / e.getXf()) + "";
                        p = tmp.indexOf(".");
                        if (p > 0 && p + 2 < tmp.length()) {
                            tmp = tmp.substring(0, p);
                        }
                        desc.append("[").append(r.getRuledescribe()).append(tmp).append("]");
                    }
                } else if (r.getRuledescribe().indexOf("考核不合格的必修课程（含集中性实践教学环节）学分≥") >= 0) {
                    if (e.getBxbjgxf() < new Double(r.getRightParameter())) {
                        break;
                    } else {
                        desc.append("[").append(r.getRuledescribe()).append(e.getBxbjgxf()).append("] ");
                        ss.append("[").append(e.getBxbjgkcnr()).append("]");
                    }
                }
            }
        }
        if (desc.length() > 0) {
            warn.setWarningCondition(desc.toString());
            warn.setWarningStandard(st.toString());
            warn.setWarningSource(ss.toString());
            rsList.add(warn);
        }
    }
}