package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WaringDescParameterDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningGradeDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningSettingsDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningTypeDTO;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.setup.domain.*;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmSettingsRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.Element;
import java.util.*;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-21
 */
@Component
@Transactional
public class AlarmSettingsService {
    @Autowired
    private AlarmSettingsRepository alarmSettingsRepository;
    @Autowired
    private WarningTypeService warningTypeService;
    @Autowired
    private AlarmRuleService alarmRuleService;
    @Autowired
    private ProcessingModeService processingModeService;

    public List<AlarmSettings> getAlarmSettingsById(Long orgId, String warningType){
        return alarmSettingsRepository.getAlarmSettingsByOrgIdAndType(orgId, warningType, DataValidity.VALID.getState());
    }


    public Map<String,Object>  getWarningTypeList(Long orgId){
        Map<String,Object> result = new HashMap<>();
        List<WarningTypeDTO> data = new ArrayList<>();
        try{
            List<WarningType> warningTypeList = warningTypeService.getWarningTypeList(orgId);
            if(null!=warningTypeList&&warningTypeList.size()>0) {
                for (WarningType type :warningTypeList) {
                    WarningTypeDTO warningTypeDTO = new WarningTypeDTO();
                    warningTypeDTO.setOrgId(orgId);
                    warningTypeDTO.setId(type.getId());
                    warningTypeDTO.setWarningType(type.getWarningType());
                    warningTypeDTO.setSetupCloseFlag(type.getSetupCloseFlag());
                    warningTypeDTO.setWarningName(type.getWarningName());
                    List<AlarmSettings> alarmSettingsList = alarmSettingsRepository.getAlarmSettingsByOrgIdAndType(orgId, type.getWarningType(), DataValidity.VALID.getState());
                    warningTypeDTO.setInclusionNumber(alarmSettingsList.size());
                    data.add(warningTypeDTO);
                }
            }
        }catch (Exception e){
            result.put("success",true);
            result.put("message","获取告警类型列表异常！");
        }
        result.put("success",true);
        result.put("data",data);
        return result;
    }

    public Map<String,Object>  getWarningSet(String warningTypeId){
        Map<String,Object> result = new HashMap<>();
        WarningSettingsDTO warningSettingsDTO = new WarningSettingsDTO();
        try{
            WarningType warningType = warningTypeService.getWarningTypeById(warningTypeId);
            warningSettingsDTO.setWarningName(warningType.getWarningName());
            warningSettingsDTO.setSetupCloseFlag(warningType.getSetupCloseFlag());
            warningSettingsDTO.setWarningTypeId(warningTypeId);
            List<WarningGradeDTO> warningGradeDTOList = new ArrayList<>();
            String[] wd = warningType.getWarningDescribe().split(",");
            List<WaringDescParameterDTO> waringDescParameterDTOArrayList = new ArrayList<>();
            if(wd.length>0) {
                int serialNumber = 1;
                for(String d : wd){
                    WaringDescParameterDTO waringDescParameterDTO = new WaringDescParameterDTO();
                    waringDescParameterDTO.setDescribe(d);
                    waringDescParameterDTO.setSerialNumber(serialNumber);
                    waringDescParameterDTOArrayList.add(waringDescParameterDTO);
                    serialNumber++;
                }
            }
            List<String> list = Arrays.asList(wd);
            //一级红色预警
            WarningGradeDTO warningGradeDTO1 = new WarningGradeDTO();
            warningGradeDTO1.setGrade(1);
            warningGradeDTO1.setName("红色预警");
            warningGradeDTO1.setSetupCloseFlag(20);
            warningGradeDTO1.setDescribeParameter(waringDescParameterDTOArrayList);
            warningGradeDTOList.add(warningGradeDTO1);
           //二级橙色预警
            WarningGradeDTO warningGradeDTO2 = new WarningGradeDTO();
            warningGradeDTO2.setGrade(2);
            warningGradeDTO2.setName("橙色预警");
            warningGradeDTO2.setSetupCloseFlag(20);
            warningGradeDTO2.setDescribeParameter(waringDescParameterDTOArrayList);
            warningGradeDTOList.add(warningGradeDTO2);
            //三级黄色预警
            WarningGradeDTO warningGradeDTO3 = new WarningGradeDTO();
            warningGradeDTO3.setGrade(3);
            warningGradeDTO3.setName("黄色预警");
            warningGradeDTO3.setSetupCloseFlag(20);
            warningGradeDTO3.setDescribeParameter(waringDescParameterDTOArrayList);
            warningGradeDTOList.add(warningGradeDTO3);
            List<AlarmSettings> alarmSettingsList = alarmSettingsRepository.getAlarmSettingsByOrgIdAndType(warningType.getOrgId(), warningType.getWarningType(), DataValidity.VALID.getState());
            if(null!=alarmSettingsList){
                for(AlarmSettings as :alarmSettingsList){
                    for(WarningGradeDTO warningGradeDTO: warningGradeDTOList){
                        if(warningGradeDTO.getGrade()==as.getWarningLevel()){
                            warningGradeDTO.setSetupCloseFlag(20);
                            if(!StringUtils.isBlank(as.getId())) {
                                warningGradeDTO.setAlarmSettingsId(as.getId());
                                List<AlarmRule> alarmRuleList = alarmRuleService.getByAlarmSettingId(as.getId());
                                for (WaringDescParameterDTO waringDescParameterDTO : warningGradeDTO.getDescribeParameter()) {
                                    for (AlarmRule alarmRule : alarmRuleList) {
                                        if (alarmRule.getSerialNumber() == waringDescParameterDTO.getSerialNumber()) {
                                            waringDescParameterDTO.setParameter(alarmRule.getRightParameter());
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            warningSettingsDTO.setGradeDTOList(warningGradeDTOList);
        }catch (Exception e){
            result.put("success",true);
            result.put("message","获取告警设置列表异常！");
        }
        result.put("success",true);
        result.put("data", warningSettingsDTO);
        return result;
    }

    public Map<String,Object>  warningSet(AlarmSettingDomain alarmSettingDomain){
        Map<String,Object> result = new HashMap<>();
        try{
            WarningType warningType = warningTypeService.getWarningTypeById(alarmSettingDomain.getWarningTypeId());
            warningType.setSetupCloseFlag(alarmSettingDomain.getSetupCloseFlag());
            warningTypeService.save(warningType);
            for(WarningGradeDomain wg :alarmSettingDomain.getWarningGradeDomainList() ){
                AlarmSettings alarmSettings = null;
                if(!StringUtils.isBlank(wg.getAlarmSettingsId())){
                    alarmSettings = alarmSettingsRepository.findOne(wg.getAlarmSettingsId());
                }else {
                    alarmSettings = new AlarmSettings();
                }
                alarmSettings.setSetupCloseFlag(wg.getSetupCloseFlag());
                alarmSettings.setWarningLevel(wg.getGrade());
                String alarmSettingsId = alarmSettingsRepository.save(alarmSettings).getId();
                for(WaringParameterDomain wp : wg.getWaringParameterDomainList()){
                    AlarmRule alarmRule = null;
                    alarmRule = alarmRuleService.getByAlarmSettingIdAndSerialNumber(alarmSettingsId,wp.getSerialNumber());
                    if(null==alarmRule){
                        alarmRule = new AlarmRule();
                    }
                    alarmRule.setAlarmSettingsId(alarmSettingsId);
                    alarmRule.setSerialNumber(wp.getSerialNumber());
                    alarmRule.setRightParameter(wp.getParameter());
                    alarmRuleService.save(alarmRule);
                }
            }
        }catch (Exception e){
            result.put("success",true);
            result.put("message","预警设置保存异常！");
        }
        result.put("success",true);
        result.put("message","预警设置保存成功!");
        return result;
    }


    public  Map<String,Object> getProcessingMode(String warningTypeId){
        Map<String,Object> result = new HashMap<>();
        List<ProcessingMode> processingModeList = null;
        try{
            WarningType warningType = warningTypeService.getWarningTypeById(warningTypeId);
            processingModeList =  processingModeService.getProcessingModeBywarningTypeId(warningType.getOrgId(),warningTypeId);
           }catch (Exception e){
           result.put("success",true);
            result.put("message","获取预警处理设置信息异常！");
          }
         result.put("success",true);
         result.put("processingModeList",processingModeList);
       return result;
    }

    public  Map<String,Object> setProcessingMode(ProcessingModeDomain processingModeDomain) {
        Map<String, Object> result = new HashMap<>();
        try{
            List<ProcessingGradeDomain> processingGradeDomainList = processingModeDomain.getProcessingGreadList();
            WarningType warningType = warningTypeService.getWarningTypeById(processingModeDomain.getWarningTypeId());
            if(null!=processingGradeDomainList&&processingGradeDomainList.size()>0){
                for(ProcessingGradeDomain pg : processingGradeDomainList){
                    ProcessingMode processingMode1 = null;
                    processingMode1 = processingModeService.getBywarningTypeIdAndTypeSet(processingModeDomain.getOrgId(),processingModeDomain.getWarningTypeId(),pg.getGrade(),10);
                    if(null==processingMode1){
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
                    processingMode2 = processingModeService.getBywarningTypeIdAndTypeSet(processingModeDomain.getOrgId(),processingModeDomain.getWarningTypeId(),pg.getGrade(),20);
                    if(null==processingMode2){
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
                    processingMode3 = processingModeService.getBywarningTypeIdAndTypeSet(processingModeDomain.getOrgId(),processingModeDomain.getWarningTypeId(),pg.getGrade(),30);
                    if(null==processingMode3){
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

        }catch (Exception e){
            result.put("success",true);
            result.put("message", "保存预警处理设置信息异常！");
        }
        result.put("success",true);
        result.put("message","保存预警处理设置信息成功!");
        return result;
    }
}
