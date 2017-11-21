package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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


    public AlarmSettings getAlarmSettingsById(Long orgId, String warningType){
        return alarmSettingsRepository.getAlarmSettingsByOrgId(orgId,warningType,DataValidity.VALID.getState());
    }




}
