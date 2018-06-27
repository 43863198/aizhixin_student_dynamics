package com.aizhixin.cloud.dataanalysis.score.mongoRespository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.score.mongoEntity.FirstTwoSemestersScoreStatistics;


public interface FirstTwoSemestersScoreStatisticsRespository extends MongoRepository<FirstTwoSemestersScoreStatistics, String>{
 
	List<FirstTwoSemestersScoreStatistics> findAllByOrgIdAndTeachYearAndSemester(Long orgId,String teachYear, String semester);
	
	List<FirstTwoSemestersScoreStatistics> findAllByOrgIdAndJobNum(Long orgId,String jobNum);
}
