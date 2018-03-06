package com.aizhixin.cloud.dataanalysis.score.mongoRespository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.TotalScoreCount;



public interface TotalScoreCountMongoRespository extends MongoRepository<TotalScoreCount, String>{
 
	List<TotalScoreCount> findAllBySchoolYearAndSemesterAndOrgId(int schoolYear,int semester,Long orgId);
	
	List<TotalScoreCount> findAllBySchoolYearAndSemesterAndOrgIdAndJobNum(int schoolYear,int semester,Long orgId,String jobNum);
}
