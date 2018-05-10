package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.entity.ScoreStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-07
 */
public interface ScoreStatisticsRespository extends JpaRepository<ScoreStatistics,String> {
}
