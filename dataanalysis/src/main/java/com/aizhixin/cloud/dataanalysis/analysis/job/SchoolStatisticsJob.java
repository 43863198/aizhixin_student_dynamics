//package com.aizhixin.cloud.dataanalysis.analysis.job;
//
//import java.util.List;
//
//import com.aizhixin.cloud.dataanalysis.analysis.mongoRespository.SchoolStatisticsMongoRespository;
//import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.stereotype.Component;
//
//import com.aizhixin.cloud.dataanalysis.setup.domain.WarningTypeDomain;
//import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;
//
//@Component
//public class SchoolStatisticsJob {
//
//	public volatile static boolean flag = true;
//
//	private Logger logger = Logger.getLogger(this.getClass());
//	@Autowired
//	private MongoTemplate mongoTemplate;
//	@Autowired
//	private WarningTypeService warningTypeService;
//	@Autowired
//	private SchoolStatisticsMongoRespository schoolStatisticsMongoRespository;
//	@Autowired
//	private StudentRegisterMongoRespository stuRegisterMongoRespository;
//
//	public void schoolStatisticsJob() {
//
//		
//		List<WarningTypeDomain> orgIdList = warningTypeService.getAllOrgId();	
//        for(WarningTypeDomain domain : orgIdList){
//			schoolStatisticsMongoRespository.save(null);
//        }
//	}
//
//
//}
