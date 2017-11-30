/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.studentRegister.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aizhixin.cloud.dataanalysis.common.constant.StudentRegisterConstant;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallMongoRespository;
import com.aizhixin.cloud.dataanalysis.rollCall.service.RollCallService;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.ScoreMongoRespository;
import com.aizhixin.cloud.dataanalysis.score.service.ScoreService;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;
import com.aizhixin.cloud.dataanalysis.studentRegister.service.StudentRegisterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author zhengning
 *
 */
@RestController
@RequestMapping("/v1/test/studentregister")
@Api(value = "IDC环境office文档数据操作", description = "生成mongodb模拟数据操作")
public class TestDataController {
	private final static Logger log = LoggerFactory
			.getLogger(TestDataController.class);

	@Autowired
	private StudentRegisterMongoRespository stuRegisterMongoRespository;
	@Autowired
	private RollCallMongoRespository rollCallMongoRespository;
	@Autowired
	private ScoreMongoRespository scoreMongoRespository;
	@Autowired
	private StudentRegisterService registerService;
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private RollCallService rollCallService;


	@RequestMapping(value = "/addRegisterdata", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成MongoDB学生报到数据", response = Void.class, notes = "生成MongoDB学生报到数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addRegisterData(
			   @ApiParam(value = "orgId 机构id" ) @RequestParam(value = "orgId", required = false) Long orgId) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(null != orgId && orgId.longValue() > 0L){
		}else{
			orgId = 1L;
		}
		List<StudentRegister> stuRegisterList = new ArrayList<StudentRegister>();
		Long stuId = 9527L;
		for(int i=0;i<10;i++){
			StudentRegister studentRegister = new StudentRegister();
			
			studentRegister.setUserId(stuId++);
			studentRegister.setClassId(1L);
			studentRegister.setClassName("测试1班");
			studentRegister.setCollegeId(1L);
			studentRegister.setCollegeName("测试学院1");
			studentRegister.setGrade("大1");
			studentRegister.setJobNum("学号1000"+i);
			studentRegister.setOrgId(orgId);
			studentRegister.setProfessionalId(1L);
			studentRegister.setProfessionalName("测试专业");
			studentRegister.setIsregister(StudentRegisterConstant.UNREGISTER);
			studentRegister.setRegisterDate(DateUtil.getMonday(new Date()));
//			studentRegister.setRemarks(remarks);
			studentRegister.setSchoolYear("2017");
			studentRegister.setUserName("学生"+i);
			stuRegisterList.add(studentRegister);
		}
		
		stuRegisterMongoRespository.save(stuRegisterList);
		
			return new ResponseEntity<Map<String, Object>>(result,
					HttpStatus.OK);
	}
	
	@RequestMapping(value = "/addRollCalldata", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成MongoDB学生考勤数据", response = Void.class, notes = "生成MongoDB学生考勤数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addRollCallData(
			   @ApiParam(value = "orgId 机构id" ) @RequestParam(value = "orgId", required = false) Long orgId) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(null != orgId && orgId.longValue() > 0L){
		}else{
			orgId = 1L;
		}
		List<RollCall> rollCallList = new ArrayList<RollCall>();
		Long stuId = 9527L;
		for(int i=0;i<10;i++){
			RollCall rollCall = new RollCall();
			
			rollCall.setUserId(stuId++);
			rollCall.setClassId(1L);
			rollCall.setClassName("测试1班");
			rollCall.setCollegeId(1L);
			rollCall.setCollegeName("测试学院1");
			rollCall.setGrade("大1");
			rollCall.setJobNum("学号1000"+i);
			rollCall.setOrgId(orgId);
			rollCall.setProfessionalId(1L);
			rollCall.setProfessionalName("测试专业");
			rollCall.setSchoolYear("2017");
			rollCall.setUserName("学生"+i);
			rollCall.setScheduleId(1L);
			rollCall.setCourseName("大学英语");
			rollCall.setCourseType("必修");
			rollCall.setRollCallDate(DateUtil.getMonday(new Date()));
			rollCall.setRollCallType("上课点名");
			rollCall.setRollCallResult("旷课");
			
			rollCallList.add(rollCall);
		}
		
		rollCallMongoRespository.save(rollCallList);
			return new ResponseEntity<Map<String, Object>>(result,
					HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/addScoredata", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成MongoDB学生考勤数据", response = Void.class, notes = "生成MongoDB学生成绩数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addScoreData(
			   @ApiParam(value = "orgId 机构id" ) @RequestParam(value = "orgId", required = false) Long orgId) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(null != orgId && orgId.longValue() > 0L){
		}else{
			orgId = 1L;
		}
		List<Score> scoreList = new ArrayList<Score>();
		Long stuId = 9527L;
		for(int i=0;i<10;i++){
			Score score = new Score();
			
			score.setUserId(stuId++);
			score.setClassId(1L);
			score.setClassName("测试1班");
			score.setCollegeId(1L);
			score.setCollegeName("测试学院1");
			score.setGrade("大1");
			score.setJobNum("学号1000"+i);
			score.setOrgId(orgId);
			score.setProfessionalId(1L);
			score.setProfessionalName("测试专业");
			score.setSchoolYear("2017");
			score.setUserName("学生"+i);
			score.setScheduleId(1L);
			score.setCourseName("大学英语");
			score.setCourseType("必修");
			score.setExamTime(DateUtil.getMonday(new Date()));
			score.setScoreType("总评成绩");
			score.setFinalScore("85");
			score.setScoreResultType("百分制");
			score.setCredit(8);
			score.setGradePoint("2.5");
			
			scoreList.add(score);
		}
		
		scoreMongoRespository.save(scoreList);
			return new ResponseEntity<Map<String, Object>>(result,
					HttpStatus.OK);
	}
	
	@RequestMapping(value = "/importStudent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "批量导入新生报到信息", response = Void.class, notes = "批量导入新生报到信息<br><br><b>@author bly</b>")
	public ResponseEntity<Void> importStudent(
			@ApiParam(value = "studentInfoFile 学生信息文件", required = true) @RequestParam(value = "studentInfoFile") MultipartFile studentInfoFile,
			@ApiParam(value = "dataBaseFile 基础数据文件", required = true) @RequestParam(value = "dataBaseFile") MultipartFile dataBaseFile,
			@DateTimeFormat(pattern = "yyyy-MM-dd") @ApiParam(value = "registerDate 报到日期,<br/>时间格式：yyyy-MM-dd") @RequestParam(value = "registerDate", required = false) Date registerDate) {
			registerService.importData(studentInfoFile, dataBaseFile, registerDate);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/importScore", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "批量导入成绩", response = Void.class, notes = "批量导入成绩<br><br><b>@author bly</b>")
	public ResponseEntity<Void> importScore(
			@ApiParam(value = "studentInfoFile 学生信息文件", required = true) @RequestParam(value = "studentInfoFile") MultipartFile studentInfoFile,
			@ApiParam(value = "scoreFile 成绩文件", required = true) @RequestParam(value = "scoreFile") MultipartFile scoreFile) {
		scoreService.importData(studentInfoFile, scoreFile);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/importRollCall", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "批量导入考勤", response = Void.class, notes = "批量导入考勤<br><br><b>@author bly</b>")
	public ResponseEntity<Void> importRollCall(
			@ApiParam(value = "dataBaseFile 考勤文件", required = true) @RequestParam(value = "dataBaseFile") MultipartFile dataBaseFile) {
		rollCallService.importData(dataBaseFile);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}