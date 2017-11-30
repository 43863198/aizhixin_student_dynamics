package com.aizhixin.cloud.dataanalysis.score.mongoRespository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.ScoreFluctuateCount;



public interface ScoreFluctuateCountMongoRespository extends MongoRepository<ScoreFluctuateCount, String>{
 
	List<ScoreFluctuateCount> findAllByOrgId(Long orgId);
	
}
