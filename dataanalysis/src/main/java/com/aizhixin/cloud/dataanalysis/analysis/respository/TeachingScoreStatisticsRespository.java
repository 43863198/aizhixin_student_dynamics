package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.dto.TeachingScoreStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreStatistics;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeachingScoreStatisticsRespository extends JpaRepository<TeachingScoreStatistics,String>{
    @Query("select new com.aizhixin.cloud.dataanalysis.analysis.dto.TeachingScoreStatisticsDTO(a.colloegeName,a.colloegeId,a.studentNum,a.failPassStuNum,a.avgGPA,a.avgScore) from #{#entityName} a where a.orgId = :orgId and a.state=0 ")
   List<TeachingScoreStatisticsDTO> getTeachingScoreStatisticsByOrgId(@Param(value = "orgId")Long orgId);
    @Query("select new com.aizhixin.cloud.dataanalysis.analysis.dto.TeachingScoreStatisticsDTO(avg(a.avgGPA),avg(a.avgScore)) from #{#entityName} a where a.orgId = :orgId and a.state=0 ")
    TeachingScoreStatisticsDTO getAvgTeachingScore(@Param(value = "orgId")Long orgId);

    TeachingScoreStatistics findAllByOrgIdAndStatisticsTypeAndDeleteFlag(Long orgId,int statisticsType,int deleteFlag);


}
