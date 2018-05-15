package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.entity.ScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.respository.ScoreStatisticsRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-07
 */
@Service
public class ScoreStatisticsService {
    @Autowired
    private ScoreStatisticsRespository scoreStatisticsRespository;
    public void save(List<ScoreStatistics> ssList){
        scoreStatisticsRespository.save(ssList);
    }

    public void deleteAll(){
        scoreStatisticsRespository.deleteAll();
    }



}
