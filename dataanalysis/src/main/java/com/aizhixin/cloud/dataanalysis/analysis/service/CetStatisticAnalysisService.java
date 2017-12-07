package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.CollegeCetStatisticDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-07
 */
@Component
@Transactional
public class CetStatisticAnalysisService {
    public Map<String,Object> getStatistic(Long orgId,String grade, String semester, Pageable pageable){
        Map<String,Object> result = new HashMap<>();
        CollegeCetStatisticDTO collegeCetStatisticDTO = new CollegeCetStatisticDTO();


        return result;
    }



}
