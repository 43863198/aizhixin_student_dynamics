package com.aizhixin.cloud.dataanalysis.score.mongoRespository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;



public interface ScoreMongoRespository extends MongoRepository<Score, String>{
 
	List<Score> findAllBySchoolYearInAndOrgId(int[] schoolYears,Long orgId);
}
