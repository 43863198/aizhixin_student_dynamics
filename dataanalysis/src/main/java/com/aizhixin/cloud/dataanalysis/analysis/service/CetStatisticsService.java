package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.entity.CetStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.CetStatisticsRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-07
 */
@Service
public class CetStatisticsService {
    @Autowired
    private CetStatisticsRespository cetStatisticsRespository;

    public void save(List<CetStatistics> cetList){
        cetStatisticsRespository.save(cetList);
    }

}
