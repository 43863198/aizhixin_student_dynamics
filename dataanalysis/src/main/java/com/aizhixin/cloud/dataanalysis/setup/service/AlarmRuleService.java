package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmRuleRespository;
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
public class AlarmRuleService {
    @Autowired
    private AlarmRuleRespository alarmRuleRespository;

    public AlarmRule  getAlarmRuleById(String id){
        return alarmRuleRespository.findOne(id);
    }

    public String save(AlarmRule alarmRule){
        return alarmRuleRespository.save(alarmRule).getId();
    }

    public AlarmRule  getByAlarmSettingIdAndSerialNumber(String alarmSettingId, int serialNumber){
        return alarmRuleRespository.getBySettingIdAndSerialNumber(alarmSettingId, serialNumber, DataValidity.VALID.getState());
    }

    public List<AlarmRule>  getByAlarmSettingId(String alarmSettingId){
        return alarmRuleRespository.getByAlarmSettingId(alarmSettingId, DataValidity.VALID.getState());
    }

}
