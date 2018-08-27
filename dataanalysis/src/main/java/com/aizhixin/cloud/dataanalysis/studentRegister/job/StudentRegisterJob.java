package com.aizhixin.cloud.dataanalysis.studentRegister.job;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.constant.AlertTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.setup.entity.RuleParameter;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.RuleParameterService;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    public ArrayList<WarningInformation> studenteRegisterJob(Long orgId, String schoolYear, String semester,  String rpId, Date startTime) {

        ArrayList<WarningInformation> returnList = new ArrayList<>();

        //只在第一学期才产生迎新报到注册预警信息
        if (!semester.equals("春")) {
            // 获取的预警类型

            //更新预警集合
            ArrayList<WarningInformation> alertInforList = new ArrayList<WarningInformation>();
            //撤销预警集合
            List<WarningInformation> removeAlertInforList = new ArrayList<WarningInformation>();
            //新增预警处理信息

            // 数据库已生成的处理中预警数据
            HashMap<String, WarningInformation> warnDbMap = new HashMap<String, WarningInformation>();
            // 定时任务产生的新的预警数据
            HashMap<String, WarningInformation> warnMap = new HashMap<String, WarningInformation>();
            List<StudentRegister> stuRegisterList = stuRegisterMongoRespository
                    .findAllByTeachYearAndOrgIdAndActualRegisterDateIsNull(schoolYear, orgId);

            // 数据库已生成的处理中预警数据
            List<WarningInformation> warnDbList = alertWarningInformationService
                    .getWarnInforByState(orgId,
                            WarningTypeConstant.Register.toString(),
                            DataValidity.VALID.getState(),
                            AlertTypeConstant.ALERT_IN_PROCESS);
            for (WarningInformation warningInfor : warnDbList) {
                warnDbMap.put(warningInfor.getJobNumber(), warningInfor);
            }
            RuleParameter alarmRule = ruleParameterService.findById(rpId);
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
//                                    String alertId = UUID.randomUUID()
//                                            .toString();
//                                    alertInfor.setId(alertId);
                                    alertInfor.setName(studentRegister.getUserName());
                                    alertInfor.setJobNumber(studentRegister.getJobNum());
                                    alertInfor.setCollogeCode(studentRegister.getCollegeCode());
                                    alertInfor.setCollogeName(studentRegister.getCollegeName());
                                    alertInfor.setClassCode(studentRegister.getClassCode());
                                    alertInfor.setClassName(studentRegister.getClassName());
                                    alertInfor.setProfessionalCode(studentRegister.getProfessionalCode());
                                    alertInfor.setProfessionalName(studentRegister.getProfessionalName());
                                    alertInfor.setSemester(semester);
                                    alertInfor.setTeacherYear(studentRegister.getTeachYear());
                                    alertInfor.setWarningCondition("报到注册截至时间超过天数：" + result);
                                    alertInfor.setWarningTime(new Date());
                                    alertInfor.setPhone(studentRegister.getUserPhone());
                                    alertInfor.setOrgId(alarmRule.getOrgId());
                                    alertInforList.add(alertInfor);
                                    warnMap.put(alertInfor.getJobNumber(),
                                            alertInfor);
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


