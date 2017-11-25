package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.SubmitDealDomain;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@Component
@Transactional
public class AlarmHandlingService {

    public Map<String, Object>  submitProcessing(SubmitDealDomain submitDealDomain){
        Map<String, Object> result = new HashMap<>();
        return result;
    }



}
