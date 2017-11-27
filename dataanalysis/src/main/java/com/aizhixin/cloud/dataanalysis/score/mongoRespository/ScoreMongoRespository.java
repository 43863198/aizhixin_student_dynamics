package com.aizhixin.cloud.dataanalysis.score.mongoRespository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;



public interface ScoreMongoRespository extends MongoRepository<Score, String>{
 
	List<StudentRegister> findAllByOrgId(Long orgId);
}
