package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AlertInforQueryDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningGradeDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningSettingsDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningTypeDTO;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmSettingsRepository;
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

    public List<AlarmSettings> getAlarmSettingsById(Long orgId, String warningType){
        return alarmSettingsRepository.getAlarmSettingsByOrgIdAndType(orgId, warningType, DataValidity.VALID.getState());
    }


    public Map<String,Object>  getWarningTypeList(Long orgId){
        Map<String,Object> result = new HashMap<>();
        List<WarningTypeDTO> data = new ArrayList<>();
        try{
            List<WarningType> warningTypeList = warningTypeService.getWarningTypeList();
            if(null!=warningTypeList&&warningTypeList.size()>0) {
                for (WarningType type :warningTypeList) {
                    WarningTypeDTO warningTypeDTO = new WarningTypeDTO();
                    warningTypeDTO.setOrgId(orgId);
                    warningTypeDTO.setWarningType(type.getWarningType());
                    warningTypeDTO.setSetupCloseFlag(type.getSetupCloseFlag());
                    warningTypeDTO.setWarningName(type.getWarningName());
                    List<AlarmSettings> alarmSettingsList = alarmSettingsRepository.getAlarmSettingsByOrgIdAndType(orgId, type.getWarningType(), DataValidity.VALID.getState());
                    warningTypeDTO.setInclusionNumber(alarmSettingsList.size());
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
            List<AlarmSettings> alarmSettingsList = alarmSettingsRepository.getAlarmSettingsByOrgIdAndType(warningType.getOrgId(),warningType.getWarningType(),DataValidity.VALID.getState());
            if(null!=alarmSettingsList){
                List<WarningGradeDTO> warningGradeDTOList = new ArrayList<>();
                for(AlarmSettings as :alarmSettingsList){
                    String[] wd = warningType.getWarningDescribe().split(",");
                    WarningGradeDTO warningGradeDTO = new WarningGradeDTO();
                    warningGradeDTO.setGrade(as.getWarningLevel());
                    warningGradeDTO.setName(as.getRelationship());
                    warningGradeDTO.setSetupCloseFlag(as.getSetupCloseFlag());
                    List<String> list = Arrays.asList(wd);
                    warningGradeDTO.setDescribeList(list);
                    String[] rule = as.getRuleSet().split(",");
                    if(rule.length>0){
                        List<AlarmRule> alarmRuleList = new ArrayList<>();
                        for(int i=0;i<rule.length;i++){
                            alarmRuleList.add(alarmRuleService.getAlarmRuleById(rule[i]));
                        }
                        warningGradeDTO.setRuleList(alarmRuleList);
                    }
                    warningGradeDTOList.add(warningGradeDTO);
                }
            }
        }catch (Exception e){
            result.put("success",true);
            result.put("message","获取告警设置列表异常！");
        }
        result.put("success",true);
        result.put("data",warningSettingsDTO);
        return result;
    }

    public Map<String,Object>  warningSet(WarningSettingsDTO warningSettingsDTO){
        Map<String,Object> result = new HashMap<>();
        List<WarningSettingsDTO> data = new ArrayList<>();
        try{



        }catch (Exception e){
            result.put("success",true);
            result.put("message","预警设置保存异常！");
        }
        result.put("success",true);
        result.put("data",data);
        return result;
    }





}
