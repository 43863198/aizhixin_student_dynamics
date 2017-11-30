package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-29
 */
public interface SchoolStatisticsRespository extends JpaRepository<SchoolStatistics, String> {
}
