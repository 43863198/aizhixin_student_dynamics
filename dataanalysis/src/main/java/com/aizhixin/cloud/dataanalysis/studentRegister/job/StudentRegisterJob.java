package com.aizhixin.cloud.dataanalysis.studentRegister.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.setup.entity.RuleParameter;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.ProcessingModeService;

import com.aizhixin.cloud.dataanalysis.setup.service.RuleParameterService;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;

@Component
public class StudentRegisterJob {

    public volatile static boolean flag = true;

    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    @Lazy
    private AlarmSettingsService alarmSettingsService;
    @Autowired
    private StudentRegisterMongoRespository stuRegisterMongoRespository;
    @Autowired
    private AlertWarningInformationService alertWarningInformationService;
    @Autowired
    private WarningTypeService warningTypeService;
    @Autowired
    private RuleParameterService ruleParameterService;
    //报到注册预警指标算法(RegisterEarlyWarning)
    public ArrayList<WarningInformation> studenteRegisterJob(Long orgId, int schoolYear, int semester,  String ruleId, Date startTime) {

        ArrayList<WarningInformation> returnList = new ArrayList<>();

        //只在第一学期才产生迎新报到注册预警信息
        if (semester != 2) {
            // 获取的预警类型
//            List<WarningType> warningTypeList = warningTypeService.getAllWarningTypeList();

//            //已经开启次预警类型的组织
//            Set<Long> orgIdSet = new HashSet<>();
//            for (WarningType wt : warningTypeList) {
//                if (wt.getSetupCloseFlag() == 10) {
//                    orgIdSet.add(wt.getOrgId());
//                }
//            }

//            // 获取预警配置
//            List<AlarmSettings> settingsList = alarmSettingsService
//                    .getAlarmSettingsByType(WarningTypeConstant.Register.toString());

//            if (null != settingsList && settingsList.size() > 0) {
//                HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
//                Set<String> ruleIdList = new HashSet<String>();
//                Set<String> warnSettingsIdList = new HashSet<String>();
            // 按orgId归类告警等级阀值
//                for (AlarmSettings settings : settingsList) {
//                    if (orgIdSet.contains(settings.getOrgId()) && settings.getSetupCloseFlag() == 10) {
//                        warnSettingsIdList.add(settings.getId());
//                        Long orgId = settings.getOrgId();
//
//                        if (StringUtils.isEmpty(settings.getRuleSet())) {
//                            continue;
//                        }
//                        String[] ruleIds = settings.getRuleSet().split(",");
//                        for (String ruleId : ruleIds) {
//                            if (!StringUtils.isEmpty(ruleId)) {
//                                RuleParameter rp = ruleParameterService.findById(ruleId);
//                                if(rp.getRuleName().equals(ruleName)) {
//                                    ruleIdList.add(ruleId);
//                                }
//                            }
//                        }
//                        if (null != alarmMap.get(orgId)) {
//                            ArrayList<AlarmSettings> alarmList = alarmMap.get(orgId);
//                            alarmList.add(settings);
//                        } else {
//                            ArrayList<AlarmSettings> alarmList = new ArrayList<AlarmSettings>();
//                            alarmList.add(settings);
//                            alarmMap.put(orgId, alarmList);
//                        }
//                    }
//                }
//                // 预警规则获取
//                HashMap<String, RuleParameter> ruleParameterMap = new HashMap<String, RuleParameter>();
//                List<RuleParameter> alarmList = ruleParameterService.getRuleParameterByIds(ruleIdList);
//                for (RuleParameter rp : alarmList) {
//                    if(rp.getRuleName().equals(ruleName)) {
//                        ruleParameterMap.put(rp.getId(), rp);
//                    }
//                }
//                Iterator iter = alarmMap.entrySet().iterator();
//                while (iter.hasNext()) {


            //更新预警集合
            ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
            //撤销预警集合
            List<WarningInformation> removeAlertInforList = new ArrayList<WarningInformation>();
            //新增预警处理信息

            // 数据库已生成的处理中预警数据
            HashMap<String, WarningInformation> warnDbMap = new HashMap<String, WarningInformation>();
            // 定时任务产生的新的预警数据
            HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
//                    Map.Entry entry = (Map.Entry) iter.next();
//                    Long orgId = (Long) entry.getKey();
//                    ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
//                            .getValue();

//                    WarningType warningType = warningTypeService.getWarningTypeByOrgIdAndType(orgId, WarningTypeConstant.Register
//                            .toString());
            List<StudentRegister> stuRegisterList = stuRegisterMongoRespository
                    .findAllBySchoolYearAndOrgIdAndActualRegisterDateIsNull(schoolYear, orgId);

            // 数据库已生成的处理中预警数据
            List<WarningInformation> warnDbList = alertWarningInformationService
                    .getWarnInforByState(orgId,
                            WarningTypeConstant.Register.toString(),
                            DataValidity.VALID.getState(),
                            AlertTypeConstant.ALERT_IN_PROCESS);
            for (WarningInformation warningInfor : warnDbList) {
                warnDbMap.put(warningInfor.getJobNumber(), warningInfor);
            }
            RuleParameter alarmRule = ruleParameterService.findById(ruleId);
            if (null != stuRegisterList && stuRegisterList.size() > 0) {
                Date today = new Date();
                for (StudentRegister studentRegister : stuRegisterList) {
                    if (null != startTime) {
                        if (startTime.getTime() <= new Date().getTime()) {
                            int result = DateUtil.getDaysBetweenDate(
                                    startTime, today);
                            if (null != alarmRule) {
                                if (result >= Float.parseFloat(alarmRule.getRightParameter())) {
                                    WarningInformation alertInfor = new WarningInformation();
                                    String alertId = UUID.randomUUID()
                                            .toString();
                                    alertInfor.setId(alertId);
                                    alertInfor.setDefendantId(studentRegister
                                            .getUserId());
                                    alertInfor.setName(studentRegister
                                            .getUserName());
                                    alertInfor.setJobNumber(studentRegister
                                            .getJobNum());
                                    alertInfor.setCollogeId(studentRegister
                                            .getCollegeId());
                                    alertInfor.setCollogeName(studentRegister
                                            .getCollegeName());
                                    alertInfor.setClassId(studentRegister
                                            .getClassId());
                                    alertInfor.setClassName(studentRegister
                                            .getClassName());
                                    alertInfor
                                            .setProfessionalId(studentRegister
                                                    .getProfessionalId());
                                    alertInfor
                                            .setProfessionalName(studentRegister
                                                    .getProfessionalName());
                                    alertInfor.setSemester(semester);
                                    alertInfor.setTeacherYear(studentRegister.getSchoolYear());
//                                                alertInfor.setWarningLevel(alarmSettings
//                                                        .getWarningLevel());
//                                    alertInfor.setWarningState(AlertTypeConstant.ALERT_IN_PROCESS);
//                                                alertInfor.setAlarmSettingsId(asId);
//                                    alertInfor
//                                            .setWarningType(WarningTypeConstant.Register
//                                                    .toString());
                                    alertInfor.setWarningCondition("报到注册截至时间超过天数：" + result);
                                    alertInfor.setWarningTime(new Date());
                                    alertInfor.setPhone(studentRegister.getUserPhone());
                                    alertInfor.setOrgId(alarmRule.getOrgId());
                                    alertInforList.add(alertInfor);
                                    warnMap.put(alertInfor.getJobNumber(),
                                            alertInfor);

//                                    break;
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
            if (!alertInforList.isEmpty()) {

                // 数据库里有的预警更新预警信息(id和预警时间不变其他的信息以新的预警信息为主)
                for (WarningInformation warningInfor : alertInforList) {
                    WarningInformation warnDbInfor = warnDbMap
                            .get(warningInfor.getJobNumber());
                    if (null != warnDbInfor) {
                        warningInfor.setId(warnDbInfor.getId());
                        warningInfor.setWarningTime(warnDbInfor
                                .getWarningTime());
                    }
                }
                // 数据库中存在新产生的预警不存在的预警直接系统处理成已处理状态
                for (WarningInformation warningDbInfor : warnDbList) {
                    WarningInformation warnInfor = warnMap
                            .get(warningDbInfor.getJobNumber());
                    if (null == warnInfor) {
                        warningDbInfor
                                .setWarningState(AlertTypeConstant.ALERT_PROCESSED);
                        removeAlertInforList.add(warningDbInfor);
                    }
                }

//                alertWarningInformationService.save(alertInforList);
//
//                alertWarningInformationService.save(removeAlertInforList);

                returnList.addAll(alertInforList);
                returnList.addAll(removeAlertInforList);
            } else {
                //mongo中的报到数据都未产生预警信息则撤销数据库还处于处理中的预警信息
                removeAlertInforList = warnDbList;
//                alertWarningInformationService.save(removeAlertInforList);
                //预警处理表同步处理为撤销状态
                returnList.addAll(removeAlertInforList);
            }
        }
        return returnList;
    }
}


