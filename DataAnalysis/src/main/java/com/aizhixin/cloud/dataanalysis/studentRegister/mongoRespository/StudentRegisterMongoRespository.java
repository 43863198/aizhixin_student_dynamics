package com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;



public interface StudentRegisterMongoRespository extends MongoRepository<StudentRegister, String>{
 
	List<StudentRegister> findAllByOrgIdAndIsregister(Long orgId,int isregister);
}
