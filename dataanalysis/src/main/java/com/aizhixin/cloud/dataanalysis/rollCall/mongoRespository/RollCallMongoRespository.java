package com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;



public interface RollCallMongoRespository extends MongoRepository<RollCall, String>{
 
	List<RollCall> findAllByTeachYearAndSemesterAndOrgIdAndRollCallResult(String teachYear,String semester,Long orgId,int rollCallResult);
}
