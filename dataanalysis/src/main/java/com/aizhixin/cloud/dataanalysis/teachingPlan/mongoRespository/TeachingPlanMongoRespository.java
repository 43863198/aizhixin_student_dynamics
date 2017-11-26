package com.aizhixin.cloud.dataanalysis.teachingPlan.mongoRespository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.teachingPlan.mongoEntity.TeachingPlan;



public interface TeachingPlanMongoRespository extends MongoRepository<TeachingPlan, String>{
 
	List<StudentRegister> findAllByOrgId(Long orgId);
}
