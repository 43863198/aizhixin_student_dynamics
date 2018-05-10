package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.entity.CetStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.ScoreTop;
import com.aizhixin.cloud.dataanalysis.analysis.respository.CetStatisticsRespository;
import com.aizhixin.cloud.dataanalysis.analysis.respository.ScoreTopRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-07
 */
@Service
public class ScoreTopService {
    @Autowired
    private ScoreTopRespository scoreTopRespository;

    public void save(List<ScoreTop> sttList){
        scoreTopRespository.save(sttList);
    }

}
