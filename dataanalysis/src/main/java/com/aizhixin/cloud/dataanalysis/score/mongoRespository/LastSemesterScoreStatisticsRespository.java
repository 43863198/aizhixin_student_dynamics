package com.aizhixin.cloud.dataanalysis.score.mongoRespository;

import java.util.List;

import com.aizhixin.cloud.dataanalysis.score.mongoEntity.LastSemesterScoreStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface LastSemesterScoreStatisticsRespository extends MongoRepository<LastSemesterScoreStatistics, String>{
 
	List<LastSemesterScoreStatistics> findAllByTeachYearAndSemesterAndOrgId(String teachYear,String semester,Long orgId);
	
	List<LastSemesterScoreStatistics> findAllByTeachYearAndSemesterAndOrgIdAndJobNum(String teachYear,String semester,Long orgId,String jobNum);
}
