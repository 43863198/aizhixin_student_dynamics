package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.dto.TeachingScoreStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreStatistics;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeachingScoreStatisticsRespository extends JpaRepository<TeachingScoreStatistics,String>{

    @Query("select new com.aizhixin.cloud.dataanalysis.analysis.dto.TeachingScoreStatisticsDTO(a.collegeName,a.collegeId,a.studentNum,a.failPassStuNum,a.avgGPA,a.avgScore) from #{#entityName} a where a.orgId = :orgId and a.deleteFlag=0  and a.teacherYear=:teacherYear and a.semester=:semester and a.statisticsType=2")
   List<TeachingScoreStatisticsDTO> getTeachingScoreStatisticsByOrgId(@Param(value = "orgId")Long orgId,@Param(value = "teacherYear")int teacherYear,@Param(value = "semester")int semester);


    @Query("select new com.aizhixin.cloud.dataanalysis.analysis.dto.TeachingScoreStatisticsDTO(sum(a.curriculumNum),avg(a.avgGPA),avg(a.avgScore)) from #{#entityName} a where a.orgId = :orgId and a.deleteFlag=0 and a.statisticsType=2 GROUP BY a.grade,a.semester ORDER BY a.teacherYear DESC, a.semester DESC")
    List<TeachingScoreStatisticsDTO> getAvgTeachingScore(@Param(value = "orgId")Long orgId);


    @Query("select a from #{#entityName} a where a.orgId = :orgId and a.deleteFlag=:deleteFlag and a.statisticsType=:statisticsType")
    TeachingScoreStatistics findByOrgIdAndStatisticsTypeAndDeleteFlag(@Param(value = "orgId")Long orgId,@Param(value = "deleteFlag")int deleteFlag,@Param(value = "statisticsType")int statisticsType);


    //物理删除
    @Modifying
    @Query("delete from #{#entityName} a where a.orgId = :orgId")
    void deleteByOrgId(@Param(value = "orgId") Long orgId);

}
