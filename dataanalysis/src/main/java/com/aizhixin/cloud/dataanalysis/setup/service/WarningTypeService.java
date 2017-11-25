package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.respository.WarningTypeRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
@Component
public class WarningTypeService {
    @Autowired
    private WarningTypeRespository warningTypeRespository;

    public WarningType getWarningTypeById(String id){
        return warningTypeRespository.findOne(id);
    }

    public List<WarningType> getWarningTypeList(){
        return warningTypeRespository.findAll();
    }

    public void save(WarningType warningType){
        warningTypeRespository.save(warningType);
    }

    public void save(List<WarningType> warningTypeList){
        warningTypeRespository.save(warningTypeList);
    }

}
