package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.setup.domain.WarningTypeDomain;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.respository.WarningTypeRespository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
@Component
@Transactional
public class WarningTypeService {
    @Autowired
    private WarningTypeRespository warningTypeRespository;

    public WarningType getWarningTypeById(String id){
        return warningTypeRespository.findOne(id);
    }

    public List<WarningType> getWarningTypeList(Long orgId){
        return warningTypeRespository.getWarningTypeByOrgId(orgId, DataValidity.VALID.getState());
    }

    public void save(WarningType warningType){
        warningTypeRespository.save(warningType);
    }

    public void save(List<WarningType> warningTypeList){
        warningTypeRespository.save(warningTypeList);
    }

    public List<WarningTypeDomain> getAllOrgId(){
    	return warningTypeRespository.getAllOrgId();
    }
    
    public void delete(Long orgId){
        warningTypeRespository.deleteByOrgId(orgId);
    }

    
}
