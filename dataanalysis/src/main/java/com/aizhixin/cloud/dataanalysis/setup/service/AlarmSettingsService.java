package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningDescparameterDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningGradeDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningSettingsDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningTypeDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.analysis.job.SchoolStatisticsAnalysisJob;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.service.DistributeLock;
import com.aizhixin.cloud.dataanalysis.rollCall.job.RollCallJob;
import com.aizhixin.cloud.dataanalysis.score.job.ScoreJob;
import com.aizhixin.cloud.dataanalysis.setup.domain.*;
import com.aizhixin.cloud.dataanalysis.setup.entity.*;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmSettingsRepository;

import com.aizhixin.cloud.dataanalysis.studentRegister.job.StudentRegisterJob;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-21
 */
@Component
public class AlarmSettingsService {

    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private AlarmSettingsRepository alarmSettingsRepository;
    @Autowired
    private WarningTypeService warningTypeService;
    @Autowired
    private ProcessingModeService processingModeService;
//    @Autowired
//    @Lazy
//    private AlertWarningInformationService alertWarningInformationService;
    @Autowired
    private DistributeLock distributeLock;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private RuleParameterService ruleParameterService;



    public AlarmSettings getAlarmSettingsById(String id) {
        return alarmSettingsRepository.findOne(id);
    }

    public List<AlarmSettings> getAlarmSettingsByType(String type){
        return alarmSettingsRepository.getAlarmSettingsByType(type,DataValidity.VALID.getState());
    }


    public List<AlarmSettings> getAlarmSettingsByTypeList(List<String> warningTypeList) {
        return alarmSettingsRepository.getAlarmSettingsByTypeList(warningTypeList, DataValidity.VALID.getState());
    }

    public void saveAlarmSettingsList(List<AlarmSettings> alarmSettingsList) {
        alarmSettingsRepository.save(alarmSettingsList);
    }


    public List<AlarmSettings> getAlarmSettingsByOrgIdAndWarningType(Long orgId, String warningType) {
        return alarmSettingsRepository.getAlarmSettingsByOrgIdAndType(orgId, warningType, DataValidity.VALID.getState());
    }


    public Map<String, Object> getWarningTypeList(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        List<WarningTypeDTO> data = new ArrayList<>();
        try {
            List<WarningType> warningTypeList = warningTypeService.getWarningTypeList(orgId);
            if (null != warningTypeList && warningTypeList.size() > 0) {
                for (WarningType type : warningTypeList) {
                    WarningTypeDTO warningTypeDTO = new WarningTypeDTO();
                    warningTypeDTO.setOrgId(orgId);
                    warningTypeDTO.setId(type.getId());
                    warningTypeDTO.setWarningTypeDescribe(type.getTypeDescribe());
                    warningTypeDTO.setWarningType(type.getType());
                    warningTypeDTO.setSetupCloseFlag(type.getSetupCloseFlag());
                    warningTypeDTO.setWarningName(type.getWarningName());
//                    //获取正在使用的设置
                    List<AlarmSettings> alarmSettingsList = alarmSettingsRepository.getCountByOrgIdAndTypeAndOpen(orgId, type.getType(), DataValidity.VALID.getState());
                    warningTypeDTO.setInclusionNumber(alarmSettingsList.size());
                    data.add(warningTypeDTO);
                }
            }
        } catch (Exception e) {
            result.put("success", true);
            result.put("message", "获取告警类型列表异常！");
        }
        result.put("success", true);
        result.put("data", data);
        return result;
    }

    public Map<String, Object> getWarningSet(String warningTypeId) {
        Map<String, Object> result = new HashMap<>();
        WarningSettingsDTO warningSettingsDTO = new WarningSettingsDTO();
        try {
            WarningType warningType = warningTypeService.getWarningTypeById(warningTypeId);
            if (null != warningType) {
                warningSettingsDTO.setWarningName(warningType.getWarningName());
                warningSettingsDTO.setSetupCloseFlag(warningType.getSetupCloseFlag());
                warningSettingsDTO.setWarningTypeId(warningTypeId);
                warningSettingsDTO.setStartTime(warningType.getStartTime());

                List<WarningGradeDTO> warningGradeDTOList = new ArrayList<>();
                //一级红色预警
                WarningGradeDTO warningGradeDTO = new WarningGradeDTO();
                warningGradeDTO.setGrade(1);
                warningGradeDTO.setName("红色预警");
                warningGradeDTO.setSetupCloseFlag(20);
                warningGradeDTOList.add(warningGradeDTO);
                //二级橙色预警
                WarningGradeDTO warningGradeDTO2 = new WarningGradeDTO();
                warningGradeDTO2.setGrade(2);
                warningGradeDTO2.setName("橙色预警");
                warningGradeDTO2.setSetupCloseFlag(20);
                warningGradeDTOList.add(warningGradeDTO2);
                //三级黄色预警
                WarningGradeDTO warningGradeDTO3 = new WarningGradeDTO();
                warningGradeDTO3.setGrade(3);
                warningGradeDTO3.setName("黄色预警");
                warningGradeDTO3.setSetupCloseFlag(20);
                warningGradeDTOList.add(warningGradeDTO3);

                List<AlarmSettings> asList = alarmSettingsRepository.findByOrgIdAndWarningTypeAndDeleteFlag(warningType.getOrgId(), warningType.getType(), DataValidity.VALID.getState());
                for (WarningGradeDTO wg : warningGradeDTOList) {
                    for (AlarmSettings as : asList) {
                        if (as.getWarningLevel() == wg.getGrade()) {
                            wg.setSetupCloseFlag(as.getSetupCloseFlag());
                            wg.setAlarmSettingsId(as.getId());
                            wg.setRelationship(as.getRelationship());
                            if (null != as.getRuleSet() && !as.getRuleSet().equals("")) {
                                String[] alarmRules = as.getRuleSet().split(",");
                                if (alarmRules.length > 0) {
                                    List<WarningDescparameterDTO> warningDescparameterDTOList = new ArrayList<>();
                                    for (int i = 0; i < alarmRules.length; i++) {
                                        WarningDescparameterDTO warningDescparameterDTO = new WarningDescparameterDTO();
                                        String rpId = alarmRules[i];
                                        RuleParameter rp = ruleParameterService.findById(rpId);
                                        List<Rule> rules = ruleService.getRuleList(rp.getOrgId(), rp.getRuleName());
                                        if (null != rules && rules.size() > 0) {
                                            warningDescparameterDTO.setDescribe(rules.get(0).getRuleDescribe());
                                            warningDescparameterDTO.setParameter(rp.getRightParameter());
                                            warningDescparameterDTO.setSerialNumber(rp.getSerialNumber());
                                        }
                                        warningDescparameterDTOList.add(warningDescparameterDTO);
                                    }
                                    wg.setDescribeParameter(warningDescparameterDTOList);
                                }
                            }
                            break;
                        }
                    }
                }
                warningSettingsDTO.setGradeDTOList(warningGradeDTOList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", true);
            result.put("message", "获取告警设置列表异常！");
        }
        result.put("success", true);
        result.put("data", warningSettingsDTO);
        return result;
    }

    public Map<String, Object> warningSet(AlarmSettingDomain alarmSettingDomain) {
        Map<String, Object> result = new HashMap<>();
        try {
            WarningType warningType = warningTypeService.getWarningTypeById(alarmSettingDomain.getWarningTypeId());
            warningType.setSetupCloseFlag(alarmSettingDomain.getSetupCloseFlag());
            List<AlarmSettings> alarmSettingsList = new ArrayList<>();
            for (WarningGradeDomain wg : alarmSettingDomain.getWarningGradeDomainList()) {
                AlarmSettings alarmSettings = null;
                if (!StringUtils.isBlank(wg.getAlarmSettingsId())) {
                    alarmSettings = alarmSettingsRepository.findOne(wg.getAlarmSettingsId());
                    if(null==alarmSettings){
                        alarmSettings = new AlarmSettings();
                    }
                }else {
                    alarmSettings = new AlarmSettings();
                }
                alarmSettings.setSetupCloseFlag(wg.getSetupCloseFlag());
                alarmSettings.setWarningLevel(wg.getGrade());
                alarmSettings.setOrgId(warningType.getOrgId());
                alarmSettings.setStartTime(wg.getStartTime());
                alarmSettings.setWarningType(warningType.getType());
                alarmSettings.setRelationship(wg.getRelationship());
                String ruids = "";
                for (WaringParameterDomain wp : wg.getWaringParameterDomainList()) {
                    RuleParameter rp = null;
                    if (!StringUtils.isBlank(wp.getId())) {
                        rp = ruleParameterService.findById(wp.getId());
                        if(null==rp){
                            rp = new RuleParameter();
                        }
                    } else {
                        rp = new RuleParameter();
                    }
                    rp.setSerialNumber(wp.getSerialNumber());
                    rp.setRightParameter(wp.getParameter());
                    rp.setOrgId(warningType.getOrgId());
                    rp.setRuleName(wp.getRuleName());
                    rp.setRuledescribe(wp.getRuledescribe());
                    ruids = ruids + ruleParameterService.save(rp) + ",";
                }
                if (!StringUtils.isBlank(ruids)) {
                    alarmSettings.setRuleSet(ruids.substring(0, ruids.length() - 1));
                }
                alarmSettingsList.add(alarmSettings);
            }
            alarmSettingsRepository.save(alarmSettingsList);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "预警设置保存异常！");
        }
        result.put("success", true);
        result.put("message", "预警设置保存成功!");
        return result;
    }

//    @Async
//    public void rebuildAlertInfor(String warningType, Long orgId) {
//        Calendar c = Calendar.getInstance();
//        // 当前年份
//        int schoolYear = c.get(Calendar.YEAR);
//        // 当前月份
//        int month = c.get(Calendar.MONTH) + 1;
//        // 当前学期编号
//        int semester = 2;
//        if (month > 1 && month < 9) {
//            semester = 1;
//        }
//        if (month == 1) {
//            schoolYear = schoolYear - 1;
//        }
//        logger.debug("开始删除warningType=" + warningType + ",orgId=" + orgId + "的预警信息。。。。。、");
//
//        alertWarningInformationService.logicDeleteByOrgIdAndWarnType(warningType, orgId, schoolYear, semester);
//
//        logger.debug("删除warningType=" + warningType + ",orgId=" + orgId + "的预警信息结束。。。。。、");
//
//        generateWarningInfoService.warningJob();

//        if (WarningTypeConstant.Register.toString().equals(warningType) && semester != 2) {
//            logger.debug("开始重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息。。。。。、");
////            studentRegisterJob.studenteRegisterJob(schoolYear, semester);
//            logger.debug("重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息结束。。。。。、");
//        }
//        if (WarningTypeConstant.Absenteeism.toString().equals(warningType)) {
//            logger.debug("开始重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息。。。。。、");
////            rollCallJob.rollCallJob(schoolYear, semester);
//            logger.debug("重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息结束。。。。。、");
//        }
////    	if(WarningTypeConstant.AttendAbnormal.toString().equals(warningType)){
////    		logger.debug("开始重新生成warningType="+warningType+",orgId="+orgId+"的预警信息。。。。。、");
////    		scoreJob.attendAbnormalJob(schoolYear,semester);
////    		logger.debug("重新生成warningType="+warningType+",orgId="+orgId+"的预警信息结束。。。。。、");
////    	}
//        if (WarningTypeConstant.Cet.toString().equals(warningType)) {
//            logger.debug("开始重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息。。。。。、");
////            scoreJob.cet4ScoreJob(schoolYear, semester);
//            logger.debug("重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息结束。。。。。、");
//        }
//        if (WarningTypeConstant.PerformanceFluctuation.toString().equals(warningType)) {
//            logger.debug("开始重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息。。。。。、");
////            scoreJob.scoreFluctuateJob(schoolYear, semester);
//            logger.debug("重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息结束。。。。。、");
//        }
//        if (WarningTypeConstant.TotalAchievement.toString().equals(warningType)) {
//            logger.debug("开始重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息。。。。。、");
////            scoreJob.totalScoreJob(schoolYear, semester);
//            logger.debug("重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息结束。。。。。、");
//        }
//        if (WarningTypeConstant.SupplementAchievement.toString().equals(warningType)) {
//            logger.debug("开始重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息。。。。。、");
////            scoreJob.makeUpScoreJob(schoolYear, semester);
//            logger.debug("重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息结束。。。。。、");
//        }
//        if (WarningTypeConstant.LeaveSchool.toString().equals(warningType)) {
//            logger.debug("开始重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息。。。。。、");
////            scoreJob.dropOutJob(schoolYear, semester);
//            logger.debug("重新生成warningType=" + warningType + ",orgId=" + orgId + "的预警信息结束。。。。。、");
//        }
//    }


    public Map<String, Object> getProcessingMode(String warningTypeId) {
        Map<String, Object> result = new HashMap<>();
        List<ProcessingMode> processingModeList = null;
        try {
            WarningType warningType = warningTypeService.getWarningTypeById(warningTypeId);
            processingModeList = processingModeService.getProcessingModeBywarningTypeId(warningType.getOrgId(), warningType.getType());
        } catch (Exception e) {
            result.put("success", true);
            result.put("message", "获取预警处理设置信息异常！");
        }
        result.put("success", true);
        result.put("processingModeList", processingModeList);
        return result;
    }


    public Map<String, Object> openAlarmSettings(String warningTypeId, String expiryDate) {
        Map<String, Object> result = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            WarningType warningType = warningTypeService.getWarningTypeById(warningTypeId);
            warningType.setSetupCloseFlag(10);
            warningType.setStartTime(sdf.parse(expiryDate));
            warningTypeService.save(warningType);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            return result;
        }
        result.put("success", true);
        return result;
    }

    public Map<String, Object> setProcessingMode(ProcessingModeDomain processingModeDomain) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProcessingGradeDomain> processingGradeDomainList = processingModeDomain.getProcessingGreadList();
            WarningType warningType = warningTypeService.getWarningTypeById(processingModeDomain.getWarningTypeId());
            if (null != processingGradeDomainList && processingGradeDomainList.size() > 0) {
                for (ProcessingGradeDomain pg : processingGradeDomainList) {
                    ProcessingMode processingMode1 = null;
                    processingMode1 = processingModeService.getBywarningTypeIdAndTypeSet(processingModeDomain.getOrgId(), processingModeDomain.getWarningTypeId(), pg.getGrade(), 10);
                    if (null == processingMode1) {
                        processingMode1 = new ProcessingMode();
                    }
                    processingMode1.setOrgId(processingModeDomain.getOrgId());
                    processingMode1.setWarningType(warningType.getType());
                    processingMode1.setWarningLevel(pg.getGrade());
                    processingMode1.setOperationTypeSet(10);
                    processingMode1.setOperationSet(pg.getOperationSet1());
                    processingMode1.setSetupCloseFlag(pg.getSetupCloseFlag1());
                    processingModeService.save(processingMode1);
                    ProcessingMode processingMode2 = null;
                    processingMode2 = processingModeService.getBywarningTypeIdAndTypeSet(processingModeDomain.getOrgId(), processingModeDomain.getWarningTypeId(), pg.getGrade(), 20);
                    if (null == processingMode2) {
                        processingMode2 = new ProcessingMode();
                    }
                    processingMode2.setOrgId(processingModeDomain.getOrgId());
                    processingMode2.setWarningType(warningType.getType());
                    processingMode2.setWarningLevel(pg.getGrade());
                    processingMode2.setOperationTypeSet(20);
                    processingMode2.setOperationSet(pg.getOperationSet2());
                    processingMode2.setSetupCloseFlag(pg.getSetupCloseFlag2());
                    processingModeService.save(processingMode2);
                    ProcessingMode processingMode3 = null;
                    processingMode3 = processingModeService.getBywarningTypeIdAndTypeSet(processingModeDomain.getOrgId(), processingModeDomain.getWarningTypeId(), pg.getGrade(), 30);
                    if (null == processingMode3) {
                        processingMode3 = new ProcessingMode();
                    }
                    processingMode3.setOrgId(processingModeDomain.getOrgId());
                    processingMode3.setWarningType(warningType.getType());
                    processingMode3.setWarningLevel(pg.getGrade());
                    processingMode3.setOperationTypeSet(30);
                    processingMode3.setOperationSet(pg.getOperationSet3());
                    processingMode3.setSetupCloseFlag(pg.getSetupCloseFlag3());
                    processingModeService.save(processingMode3);
                }
            }

        } catch (Exception e) {
            result.put("success", true);
            result.put("message", "保存预警处理设置信息异常！");
        }
        result.put("success", true);
        result.put("message", "保存预警处理设置信息成功!");
        return result;
    }


    @Async
    public void generateData(Long orgId, String warningType) {
//        Calendar c = Calendar.getInstance();
//        // 当前年份
//        int schoolYear = c.get(Calendar.YEAR);
//        // 当前月份
//        int month = c.get(Calendar.MONTH) + 1;
//        // 当前学期编号
//        int semester = 2;
//        if (month > 1 && month < 9) {
//            semester = 1;
//        }
//        if (month == 1) {
//            schoolYear = schoolYear - 1;
//        }

        try {
            StringBuilder path = new StringBuilder("/GenerateData");

//            if (warningType.equals(WarningTypeConstant.Register.toString())) {
//                StringBuilder path = new StringBuilder("/GenerateData");
//                path.append("/Register");
//                if (distributeLock.getLock(path)) {
//                    alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.Register.toString(), orgId, schoolYear, semester);
////                    studentRegisterJob.studenteRegisterJob(schoolYear, semester);
//                    distributeLock.delete(path);//删除锁
//                }
//            }
//            if (warningType.equals(WarningTypeConstant.LeaveSchool.toString())) {
//                StringBuilder path = new StringBuilder("/GenerateData");
//                path.append("/LeaveSchool");
//                if (distributeLock.getLock(path)) {
//                    alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.LeaveSchool.toString(), orgId, schoolYear, semester);
////                    scoreJob.dropOutJob(schoolYear, semester);
//                    distributeLock.delete(path);//删除锁
//                }
//            }
////                if (warningType.equals(WarningTypeConstant.AttendAbnormal.toString())) {
////                    StringBuilder path = new StringBuilder("/GenerateData");
////                    path.append("/AttendAbnormal");
////                    if (distributeLock.getLock(path)) {
////                    alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.AttendAbnormal.toString(), orgId,schoolYear,semester);
////                    scoreJob.attendAbnormalJob(schoolYear,semester);
////                        distributeLock.delete(path);//删除锁
////                    }
////                }
//            if (warningType.equals(WarningTypeConstant.Absenteeism.toString())) {
//                StringBuilder path = new StringBuilder("/GenerateData");
//                path.append("/Absenteeism");
//                if (distributeLock.getLock(path)) {
//                    alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.Absenteeism.toString(), orgId, schoolYear, semester);
////                    rollCallJob.rollCallJob(schoolYear, semester);
//                    distributeLock.delete(path);//删除锁
//                }
//            }
//            if (warningType.equals(WarningTypeConstant.TotalAchievement.toString())) {
//                StringBuilder path = new StringBuilder("/GenerateData");
//                path.append("/TotalAchievement");
//                if (distributeLock.getLock(path)) {
//                    alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.TotalAchievement.toString(), orgId, schoolYear, semester);
////                    scoreJob.totalScoreJob(schoolYear, semester);
//                    distributeLock.delete(path);//删除锁
//                }
//            }
//            if (warningType.equals(WarningTypeConstant.SupplementAchievement.toString())) {
//                StringBuilder path = new StringBuilder("/GenerateData");
//                path.append("/SupplementAchievement");
//                if (distributeLock.getLock(path)) {
//                    alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.SupplementAchievement.toString(), orgId, schoolYear, semester);
////                    scoreJob.makeUpScoreJob(schoolYear, semester);
//                    distributeLock.delete(path);//删除锁
//                }
//            }
//            if (warningType.equals(WarningTypeConstant.PerformanceFluctuation.toString())) {
//                StringBuilder path = new StringBuilder("/GenerateData");
//                path.append("/PerformanceFluctuation");
//                if (distributeLock.getLock(path)) {
//                    alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.PerformanceFluctuation.toString(), orgId, schoolYear, semester);
////                    scoreJob.scoreFluctuateJob(schoolYear, semester);
//                    distributeLock.delete(path);//删除锁
//                }
//            }
//            if (warningType.equals(WarningTypeConstant.Cet.toString())) {
//                StringBuilder path = new StringBuilder("/GenerateData");
//                path.append("/PerformanceFluctuation");
//                if (distributeLock.getLock(path)) {
//                    alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.Cet.toString(), orgId, schoolYear, semester);
////                    scoreJob.cet4ScoreJob(schoolYear, semester);
//                    distributeLock.delete(path);//删除锁
//                }
//            }
        } catch (Exception e) {
            logger.info("手动生成数据异常！");
        } finally {
            StringBuilder path = new StringBuilder("/GenerateData");
            distributeLock.delete(path);//删除锁
        }
        logger.info("手动生成数据成功！");
    }
}
