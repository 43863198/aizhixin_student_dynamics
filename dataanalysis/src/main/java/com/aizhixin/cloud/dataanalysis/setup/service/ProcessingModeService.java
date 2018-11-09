package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.setup.entity.ProcessingMode;
import com.aizhixin.cloud.dataanalysis.setup.respository.ProcessingModeRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@Component
@Transactional
public class ProcessingModeService {
    @Autowired
    private ProcessingModeRespository processingModeRespository;

    public List<ProcessingMode> getProcessingModeBywarningTypeId(Long orgId, String warningTypeId){
        return processingModeRespository.getProcessingModeBywarningTypeId(orgId,warningTypeId, DataValidity.VALID.getState());
    }

   public ProcessingMode getBywarningTypeIdAndTypeSet(Long orgId, String warningTypeId, int operationTypeSet,int warningLevel){
       return processingModeRespository.getBywarningTypeIdAndTypeSet(orgId, warningTypeId, operationTypeSet, warningLevel, DataValidity.VALID.getState());
   }

   public void save(ProcessingMode processingMode){
       processingModeRespository.save(processingMode);
   }


    public List<ProcessingMode> getProcessingModeBywarningTypeAndOperationTypeSet(Long orgId, String warningType, int operationTypeSet){
        return processingModeRespository.findByOrgIdAndWarningTypeAndOperationTypeSetAndDeleteFlag(orgId, warningType, operationTypeSet, DataValidity.VALID.getState());
    }
}
