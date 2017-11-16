/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.studentRegister.mvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aizhixin.cloud.dataanalysis.common.constant.StudentRegisterConstant;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author zhengning
 *
 */
@RestController
@RequestMapping("/v1/test/studentregister")
@Api(value = "IDC环境office文档数据操作", description = "针对IDC环境office文档历史数据操作")
public class TestDataController {
	private final static Logger log = LoggerFactory
			.getLogger(TestDataController.class);

	@Autowired
	private StudentRegisterMongoRespository stuRegisterMongoRespository;


	@RequestMapping(value = "/addmongodata", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成MongoDB学生报到数据", response = Void.class, notes = "生成MongoDB学生报到数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addMongoData() {
		Map<String, Object> result = new HashMap<String, Object>();
	
		List<StudentRegister> stuRegisterList = new ArrayList<StudentRegister>();
		Long stuId = 9527L;
		for(int i=0;i<10;i++){
			StudentRegister studentRegister = new StudentRegister();
			
			studentRegister.setStuId(stuId++);
			studentRegister.setClassId(1L);
			studentRegister.setClassName("测试1班");
			studentRegister.setCollegeId(1L);
			studentRegister.setCollegeName("测试学院1");
			studentRegister.setGrade("大1");
			studentRegister.setJobNum("学号1000"+i);
			studentRegister.setOrgId(1L);
			studentRegister.setProfessionalId(1L);
			studentRegister.setProfessionalName("测试专业");
			studentRegister.setIsregister(StudentRegisterConstant.UNREGISTER);
			studentRegister.setRegisterDate(DateUtil.getMonday(new Date()));
//			studentRegister.setActualRegisterDate(DateUtil.getMonday(new Date()));
//			studentRegister.setRemarks(remarks);
			studentRegister.setSchoolYear("2017");
			studentRegister.setStuName("学生"+i);
			stuRegisterList.add(studentRegister);
		}
		
		stuRegisterMongoRespository.save(stuRegisterList);
		
			return new ResponseEntity<Map<String, Object>>(result,
					HttpStatus.OK);
	}
	
}