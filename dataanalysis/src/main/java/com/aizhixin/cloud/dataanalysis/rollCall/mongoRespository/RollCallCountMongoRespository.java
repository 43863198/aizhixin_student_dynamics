package com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCallCount;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import org.springframework.data.mongodb.repository.Query;


public interface RollCallCountMongoRespository extends MongoRepository<RollCallCount, String>{
	
	List<RollCallCount> findAllByTeachYearAndSemesterAndOrgId(String teachYear,String semester,Long orgId);

}
