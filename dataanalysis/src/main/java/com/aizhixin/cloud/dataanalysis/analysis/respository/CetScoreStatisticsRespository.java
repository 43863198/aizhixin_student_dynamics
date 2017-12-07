package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.dto.CetScoreStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.PracticeStaticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.CetScoreStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CetScoreStatisticsRespository extends JpaRepository<CetScoreStatistics,String>{
    @Query("select new com.aizhixin.cloud.dataanalysis.analysis.dto.CetScoreStatisticsDTO(sum(a.cetSixPassNum),sum(a.cetForePassNum),sum(a.cetForeJoinNum),sum(a.cetSixJoinNum)) from #{#entityName} a where a.orgId = :orgId and a.state=0 ")
    CetScoreStatisticsDTO getEctStatics(@Param(value = "orgId")Long orgId);
}