package com.aizhixin.cloud.dataanalysis.score.mongoRespository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.score.mongoEntity.MakeUpScoreCount;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.TotalScoreCount;



public interface MakeUpScoreCountMongoRespository extends MongoRepository<MakeUpScoreCount, String>{
 
	List<MakeUpScoreCount> findAllByOrgId(Long orgId);
}
