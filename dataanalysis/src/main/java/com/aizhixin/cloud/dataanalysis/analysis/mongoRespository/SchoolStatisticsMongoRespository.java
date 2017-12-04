package com.aizhixin.cloud.dataanalysis.analysis.mongoRespository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.analysis.mongoEntity.SchoolStatisticsMongo;



public interface SchoolStatisticsMongoRespository extends MongoRepository<SchoolStatisticsMongo, String>{
	
}
