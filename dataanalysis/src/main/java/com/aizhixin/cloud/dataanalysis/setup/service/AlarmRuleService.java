package com.aizhixin.cloud.dataanalysis.setup.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmRuleRespository;
import com.aizhixin.cloud.dataanalysis.setup.respository.WarningTypeRespository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
@Component
public class AlarmRuleService {
    @Autowired
    private AlarmRuleRespository alarmRuleRespository;

    public AlarmRule  getAlarmRuleById(String id){
        return alarmRuleRespository.findOne(id);
    }

    
    public List<AlarmRule>  getAlarmRuleByIds(Set<String> ids){
        return alarmRuleRespository.findAllByIdIn(ids);
    }
}
