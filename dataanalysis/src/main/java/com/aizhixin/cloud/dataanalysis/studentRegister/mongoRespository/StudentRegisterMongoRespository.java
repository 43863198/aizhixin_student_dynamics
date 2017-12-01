package com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository;

import java.util.List;

import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;


public interface StudentRegisterMongoRespository extends MongoRepository<StudentRegister, String>{
 
	List<StudentRegister> findAllByOrgIdAndIsRegister(Long orgId,int isregister);
	
	List<StudentRegister> findAllByOrgIdAndActualRegisterDateIsNull(Long orgId);

	List<StudentRegister> findAllByOrgIdAndCollegeId(Long orgId,Long collegeId);



}
