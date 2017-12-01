package com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;



public interface RollCallMongoRespository extends MongoRepository<RollCall, String>{
 
	List<RollCall> findAllBySchoolYearAndSemesterAndOrgId(int schoolYear,int semester,Long orgId);
}
