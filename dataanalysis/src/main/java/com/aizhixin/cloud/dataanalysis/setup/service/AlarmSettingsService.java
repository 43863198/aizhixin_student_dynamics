package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningDescparameterDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningGradeDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningSettingsDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningTypeDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.analysis.job.SchoolStatisticsAnalysisJob;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.rollCall.job.RollCallJob;
import com.aizhixin.cloud.dataanalysis.score.job.ScoreJob;
import com.aizhixin.cloud.dataanalysis.setup.domain.*;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmSettingsRepository;
import com.aizhixin.cloud.dataanalysis.studentRegister.job.StudentRegisterJob;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
@Transactional
public class AlarmSettingsService {
	
	private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private AlarmSettingsRepository alarmSettingsRepository;
    @Autowired
    private WarningTypeService warningTypeService;
    @Autowired
    private AlarmRuleService alarmRuleService;
    @Autowired
    private ProcessingModeService processingModeService;
    @Autowired
    private AlertWarningInformationService alertWarningInformationService;
    @Autowired
    private StudentRegisterJob studentRegisterJob;
    @Autowired
    private RollCallJob rollCallJob;
    @Autowired
    private ScoreJob scoreJob;
    @Autowired
    private RelationConversionService relationConversionService;
    @Autowired
    private SchoolStatisticsAnalysisJob schoolStatisticsJob;



    public AlarmSettings getAlarmSettingsById(String id){
        return alarmSettingsRepository.findOne(id);
    }


    public List<AlarmSettings> getAlarmSettingsByType(String warningType){
        return alarmSettingsRepository.getAlarmSettingsByType(warningType, DataValidity.VALID.getState());
    }

    public List<AlarmSettings> getAlarmSettingsByTypeList(List<String> warningTypeList){
        return alarmSettingsRepository.getAlarmSettingsByTypeList(warningTypeList, DataValidity.VALID.getState());
    }

    public void saveAlarmSettingsList(List<AlarmSettings> alarmSettingsList){
         alarmSettingsRepository.save(alarmSettingsList);
    }


    public List<AlarmSettings> getAlarmSettingsById(Long orgId, String warningType){
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
                    warningTypeDTO.setWarningTypeDescribe(type.getWarningTypeDescribe());
                    warningTypeDTO.setWarningType(type.getWarningType());
                    warningTypeDTO.setSetupCloseFlag(type.getSetupCloseFlag());
                    warningTypeDTO.setWarningName(type.getWarningName());
//                    if(type.getSetupCloseFlag()==10) {
                        List<AlarmSettings> alarmSettingsList = alarmSettingsRepository.getCountByOrgIdAndTypeAndOpen(orgId, type.getWarningType(), DataValidity.VALID.getState());
                        warningTypeDTO.setInclusionNumber(alarmSettingsList.size());
//                    }
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
            String[] wd = warningType.getWarningDescribe().split(",");
            warningSettingsDTO.setWarningName(warningType.getWarningName());
            warningSettingsDTO.setSetupCloseFlag(warningType.getSetupCloseFlag());
            warningSettingsDTO.setWarningTypeId(warningTypeId);
            warningSettingsDTO.setStartTime(warningType.getStartTime());
            List<WarningGradeDTO> warningGradeDTOList = new ArrayList<>();

            List<WarningDescparameterDTO> waringDescParameterDTOArrayList1 = new ArrayList<>();
            if (wd.length > 0) {
                int serialNumber = 1;
                for (String d : wd) {
                    WarningDescparameterDTO waringDescParameterDTO1 = new WarningDescparameterDTO();
                    waringDescParameterDTO1.setDescribe(d);
                    waringDescParameterDTO1.setSerialNumber(serialNumber);
                    waringDescParameterDTOArrayList1.add(waringDescParameterDTO1);
                    serialNumber++;
                }
            }
            List<WarningDescparameterDTO> waringDescParameterDTOArrayList2 = new ArrayList<>();
            if (wd.length > 0) {
                int serialNumber = 1;
                for (String d : wd) {
                	WarningDescparameterDTO waringDescParameterDTO2 = new WarningDescparameterDTO();
                    waringDescParameterDTO2.setDescribe(d);
                    waringDescParameterDTO2.setSerialNumber(serialNumber);
                    waringDescParameterDTOArrayList2.add(waringDescParameterDTO2);
                    serialNumber++;
                }
            }
            List<WarningDescparameterDTO> waringDescParameterDTOArrayList3 = new ArrayList<>();
            if (wd.length > 0) {
                int serialNumber = 1;
                for (String d : wd) {
                	WarningDescparameterDTO waringDescParameterDTO3 = new WarningDescparameterDTO();
                    waringDescParameterDTO3.setDescribe(d);
                    waringDescParameterDTO3.setSerialNumber(serialNumber);
                    waringDescParameterDTOArrayList3.add(waringDescParameterDTO3);
                    serialNumber++;
                }
            }
            //一级红色预警
            WarningGradeDTO warningGradeDTO = new WarningGradeDTO();
            warningGradeDTO.setGrade(1);
            warningGradeDTO.setName("红色预警");
            warningGradeDTO.setSetupCloseFlag(20);
            warningGradeDTO.setDescribeParameter(waringDescParameterDTOArrayList1);
            warningGradeDTOList.add(warningGradeDTO);
            //二级橙色预警
            WarningGradeDTO warningGradeDTO2 = new WarningGradeDTO();
            warningGradeDTO2.setGrade(2);
            warningGradeDTO2.setName("橙色预警");
            warningGradeDTO2.setSetupCloseFlag(20);
            warningGradeDTO2.setDescribeParameter(waringDescParameterDTOArrayList2);
            warningGradeDTOList.add(warningGradeDTO2);
            //三级黄色预警
            WarningGradeDTO warningGradeDTO3 = new WarningGradeDTO();
            warningGradeDTO3.setGrade(3);
            warningGradeDTO3.setName("黄色预警");
            warningGradeDTO3.setSetupCloseFlag(20);
            warningGradeDTO3.setDescribeParameter(waringDescParameterDTOArrayList3);
            warningGradeDTOList.add(warningGradeDTO3);

            List<AlarmSettings> alarmSettingsList = alarmSettingsRepository.getAlarmSettingsByOrgIdAndType(warningType.getOrgId(), warningType.getWarningType(), DataValidity.VALID.getState());
            if (null != alarmSettingsList) {
                for (WarningGradeDTO waringGradeDTO : warningGradeDTOList) {
                    for (AlarmSettings as : alarmSettingsList) {
                        if (waringGradeDTO.getGrade() == as.getWarningLevel()) {
                            waringGradeDTO.setSetupCloseFlag(as.getSetupCloseFlag());
                            waringGradeDTO.setAlarmSettingsId(as.getId());
                            List<AlarmRule> alarmRuleList = alarmRuleService.getByAlarmSettingId(as.getId());
                            for (AlarmRule alarmRule : alarmRuleList) {
                                for (WarningDescparameterDTO waringDescParameterDTO : waringGradeDTO.getDescribeParameter()) {
                                    if (alarmRule.getSerialNumber() == waringDescParameterDTO.getSerialNumber()) {
                                        waringDescParameterDTO.setParameter(alarmRule.getRightParameter());
                                        break;
                                    }
                                }
                            }
                            break;
                        }

                    }
                }
            }
            warningSettingsDTO.setGradeDTOList(warningGradeDTOList);
        } catch (Exception e) {
            result.put("success", true);
            result.put("message", "获取告警设置列表异常！");
        }
        result.put("success", true);
        result.put("data", warningSettingsDTO);
        return result;
    }

    @Transactional
    public Map<String, Object> warningSet(AlarmSettingDomain alarmSettingDomain) {
        Map<String, Object> result = new HashMap<>();
        try {
            WarningType warningType = warningTypeService.getWarningTypeById(alarmSettingDomain.getWarningTypeId());
            warningType.setSetupCloseFlag(alarmSettingDomain.getSetupCloseFlag());
            String[] wd = warningType.getWarningDescribe().split(",");
            warningTypeService.save(warningType);
            for (WarningGradeDomain wg : alarmSettingDomain.getWarningGradeDomainList()) {
                AlarmSettings alarmSettings = null;
                if (!StringUtils.isBlank(wg.getAlarmSettingsId())) {
                    alarmSettings = alarmSettingsRepository.findOne(wg.getAlarmSettingsId());
                } else {
                    alarmSettings = new AlarmSettings();
                }
                alarmSettings.setSetupCloseFlag(wg.getSetupCloseFlag());
                alarmSettings.setWarningLevel(wg.getGrade());
                alarmSettings.setOrgId(warningType.getOrgId());
                alarmSettings.setWarningType(warningType.getWarningType());
                String alarmSettingsId = alarmSettingsRepository.save(alarmSettings).getId();
                String ruids = "";
                for (WaringParameterDomain wp : wg.getWaringParameterDomainList()) {
                    AlarmRule alarmRule = null;
                    alarmRule = alarmRuleService.getByAlarmSettingIdAndSerialNumber(alarmSettingsId, wp.getSerialNumber());
                    if (null == alarmRule) {
                        alarmRule = new AlarmRule();
                    }
                    alarmRule.setAlarmSettingsId(alarmSettingsId);
                    alarmRule.setSerialNumber(wp.getSerialNumber());
                    alarmRule.setRightParameter(wp.getParameter());
                    alarmRule.setOrgId(warningType.getOrgId());
                    
                    if(null == result.get("warningType")){
                     result.put("warningType", warningType.getWarningType());
                     result.put("orgId", warningType.getOrgId());
                    }
                    
                    if (wd.length > 0) {
                        alarmRule.setName(wd[wp.getSerialNumber()-1]);
                        String relationship = alarmRule.getName().substring(alarmRule.getName().length()-1);
                        alarmRule.setRightRelationship(relationConversionService.getRelation(relationship));
                    }
                    ruids = ruids + alarmRuleService.save(alarmRule) + ",";
                }
                if(!StringUtils.isBlank(ruids)) {
                    AlarmSettings set = alarmSettingsRepository.findOne(alarmSettingsId);
                    set.setRuleSet(ruids.substring(0, ruids.length() - 1));
                    alarmSettingsRepository.save(set);
                }
            }
        } catch (Exception e) {
            result.put("success", true);
            result.put("message", "预警设置保存异常！");
        }
        result.put("success", true);
        result.put("message", "预警设置保存成功!");
        return result;
    }
    
    @Async
    public void rebuildAlertInfor(String warningType,Long orgId){
    	
    	logger.debug("开始删除warningType="+warningType+",orgId="+orgId+"的预警信息。。。。。、");
    	
    	alertWarningInformationService.logicDeleteByOrgIdAndWarnType(warningType, orgId);
    	
    	logger.debug("删除warningType="+warningType+",orgId="+orgId+"的预警信息结束。。。。。、");
    	
    	if(WarningTypeConstant.Register.toString().equals(warningType)){
    		logger.debug("开始重新生成warningType="+warningType+",orgId="+orgId+"的预警信息。。。。。、");
    		studentRegisterJob.studenteRegisterJob();
    		logger.debug("重新生成warningType="+warningType+",orgId="+orgId+"的预警信息结束。。。。。、");
    	}
    	if(WarningTypeConstant.Absenteeism.toString().equals(warningType)){
    		logger.debug("开始重新生成warningType="+warningType+",orgId="+orgId+"的预警信息。。。。。、");
    		rollCallJob.rollCallJob();
    		logger.debug("重新生成warningType="+warningType+",orgId="+orgId+"的预警信息结束。。。。。、");
    	}
    	if(WarningTypeConstant.AttendAbnormal.toString().equals(warningType)){
    		logger.debug("开始重新生成warningType="+warningType+",orgId="+orgId+"的预警信息。。。。。、");
    		scoreJob.attendAbnormalJob();
    		logger.debug("重新生成warningType="+warningType+",orgId="+orgId+"的预警信息结束。。。。。、");
    	}
    	if(WarningTypeConstant.Cet.toString().equals(warningType)){
    		logger.debug("开始重新生成warningType="+warningType+",orgId="+orgId+"的预警信息。。。。。、");
    		scoreJob.cet4ScoreJob();
    		logger.debug("重新生成warningType="+warningType+",orgId="+orgId+"的预警信息结束。。。。。、");
    	}
    	if(WarningTypeConstant.PerformanceFluctuation.toString().equals(warningType)){
    		logger.debug("开始重新生成warningType="+warningType+",orgId="+orgId+"的预警信息。。。。。、");
    		scoreJob.scoreFluctuateJob();
    		logger.debug("重新生成warningType="+warningType+",orgId="+orgId+"的预警信息结束。。。。。、");
    	}
    	if(WarningTypeConstant.TotalAchievement.toString().equals(warningType)){
    		logger.debug("开始重新生成warningType="+warningType+",orgId="+orgId+"的预警信息。。。。。、");
    		scoreJob.totalScoreJob();
    		logger.debug("重新生成warningType="+warningType+",orgId="+orgId+"的预警信息结束。。。。。、");
    	}
    	if(WarningTypeConstant.SupplementAchievement.toString().equals(warningType)){
    		logger.debug("开始重新生成warningType="+warningType+",orgId="+orgId+"的预警信息。。。。。、");
    		scoreJob.makeUpScoreJob();
    		logger.debug("重新生成warningType="+warningType+",orgId="+orgId+"的预警信息结束。。。。。、");
    	}
    	if(WarningTypeConstant.LeaveSchool.toString().equals(warningType)){
    		logger.debug("开始重新生成warningType="+warningType+",orgId="+orgId+"的预警信息。。。。。、");
    		scoreJob.dropOutJob();
    		logger.debug("重新生成warningType="+warningType+",orgId="+orgId+"的预警信息结束。。。。。、");
    	}
    }


    public Map<String, Object> getProcessingMode(String warningTypeId) {
        Map<String, Object> result = new HashMap<>();
        List<ProcessingMode> processingModeList = null;
        try {
            WarningType warningType = warningTypeService.getWarningTypeById(warningTypeId);
            processingModeList = processingModeService.getProcessingModeBywarningTypeId(warningType.getOrgId(), warningType.getWarningType());
        } catch (Exception e) {
            result.put("success", true);
            result.put("message", "获取预警处理设置信息异常！");
        }
        result.put("success", true);
        result.put("processingModeList", processingModeList);
        return result;
    }


    public Map<String, Object> openAlarmSettings(String warningTypeId,String expiryDate) {
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
                    processingMode1.setWarningType(warningType.getWarningType());
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
                    processingMode2.setWarningType(warningType.getWarningType());
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
                    processingMode3.setWarningType(warningType.getWarningType());
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



    public  Map<String, Object> generateData(Long orgId,String warningType){
        Map<String, Object> result = new HashMap<>();
        try {
            if(warningType.equals(WarningTypeConstant.Register.toString())){
                alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.Register.toString(), orgId);
                studentRegisterJob.studenteRegisterJob();
            }
            if(warningType.equals(WarningTypeConstant.LeaveSchool.toString())){
                alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.LeaveSchool.toString(), orgId);
                scoreJob.dropOutJob();
            }
            if(warningType.equals(WarningTypeConstant.AttendAbnormal.toString())){
                alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.AttendAbnormal.toString(), orgId);
                scoreJob.attendAbnormalJob();
            }
            if(warningType.equals(WarningTypeConstant.Absenteeism.toString())){
                alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.Absenteeism.toString(), orgId);
                rollCallJob.rollCallJob();
            }
            if(warningType.equals(WarningTypeConstant.TotalAchievement.toString())){
                alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.TotalAchievement.toString(), orgId);
                scoreJob.totalScoreJob();
            }
            if(warningType.equals(WarningTypeConstant.SupplementAchievement.toString())){
            alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.SupplementAchievement.toString(), orgId);
                scoreJob.makeUpScoreJob();
            }
            if(warningType.equals(WarningTypeConstant.PerformanceFluctuation.toString())){
                alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.PerformanceFluctuation.toString(), orgId);
                scoreJob.scoreFluctuateJob();
            }
            if(warningType.equals(WarningTypeConstant.Cet.toString())){
                alertWarningInformationService.logicDeleteByOrgIdAndWarnType(WarningTypeConstant.Cet.toString(), orgId);
                scoreJob.cet4ScoreJob();
            }

        } catch (Exception e) {
            result.put("success", true);
            result.put("message", "手动生成数据异常！");
        }
        result.put("success", true);
        result.put("message", "手动生成数据成功!");
        return result;
    }
}
