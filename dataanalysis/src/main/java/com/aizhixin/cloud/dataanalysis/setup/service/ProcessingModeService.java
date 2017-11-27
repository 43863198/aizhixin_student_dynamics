package com.aizhixin.cloud.dataanalysis.setup.service;

import java.util.List;
import java.util.Set;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;
import com.aizhixin.cloud.dataanalysis.setup.respository.ProcessingModeRespository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@Component
public class ProcessingModeService {
    @Autowired
    private ProcessingModeRespository processingModeRespository;

    public List<ProcessingMode> getProcessingModeList(Set<String> settingsIds,int deleteFlag){
    	return processingModeRespository.findAllByAlarmSettingsIdInAndDeleteFlag(settingsIds, deleteFlag);
    }
    public List<ProcessingMode> getProcessingModeBywarningTypeId(Long orgId, String warningTypeId){
        return processingModeRespository.getProcessingModeBywarningTypeId(orgId,warningTypeId, DataValidity.VALID.getState());

    }

}
