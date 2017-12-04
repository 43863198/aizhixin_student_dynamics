package com.aizhixin.cloud.dataanalysis.analysis.mongoRespository;


import org.springframework.data.mongodb.repository.MongoRepository;
import com.aizhixin.cloud.dataanalysis.analysis.mongoEntity.Practice;



public interface PracticeMongoRespository extends MongoRepository<Practice, String>{
	
}
