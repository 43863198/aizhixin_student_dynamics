/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.simulate.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.aizhixin.cloud.dataanalysis.common.service.InitService;
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

import com.aizhixin.cloud.dataanalysis.analysis.service.PracticeService;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.StudentRegisterConstant;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoEntity.RollCall;
import com.aizhixin.cloud.dataanalysis.rollCall.mongoRespository.RollCallMongoRespository;
import com.aizhixin.cloud.dataanalysis.rollCall.service.RollCallService;
import com.aizhixin.cloud.dataanalysis.score.job.ScoreJob;
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
@RequestMapping("/v1/test")
@Api(value = "测试生成模拟数据和调用定时任务操作", description = "生成mongodb模拟数据操作")
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
	private ScoreJob scoreJob;
	@Autowired
	private StudentRegisterService registerService;
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private RollCallService rollCallService;
	@Autowired
	private PracticeService practiceService;
	@Autowired
	private InitService initService;


	@RequestMapping(value = "/addRegisterdata", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成MongoDB学生报到数据", response = Void.class, notes = "生成MongoDB学生报到数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addRegisterData(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "jobNum 学号") @RequestParam(value = "jobNum", required = true) String jobNum,
			@ApiParam(value = "stuName 姓名") @RequestParam(value = "stuName", required = true) String stuName,
			@ApiParam(value = "dayNum 注册报到时间比当前日期推前几天(例子:-2为2天前报到注册,若输入大于0数字默认变为负数)") @RequestParam(value = "dayNum", required = false) int dayNum) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null != orgId && orgId.longValue() > 0L) {
		} else {
			orgId = 1L;
		}
		
		if(dayNum > 0){
			dayNum = 0-dayNum;
		}
		
		List<StudentRegister> stuRegisterList = new ArrayList<StudentRegister>();
			
		StudentRegister studentRegister = new StudentRegister();
			studentRegister.setUserId(1L);
			studentRegister.setClassId(1L);
			studentRegister.setClassName("测试1班");
			studentRegister.setCollegeId(1L);
			studentRegister.setCollegeName("测试学院");
			studentRegister.setGrade("大1");
			studentRegister.setJobNum(jobNum);
			studentRegister.setOrgId(orgId);
			studentRegister.setProfessionalId(1L);
			studentRegister.setProfessionalName("测试专业");
			studentRegister.setIsRegister(StudentRegisterConstant.UNREGISTER);
			studentRegister.setRegisterDate(DateUtil.afterNDay(new Date(), dayNum));
			// studentRegister.setRemarks(remarks);
			studentRegister.setSchoolYear(2017);
			studentRegister.setUserName(stuName);
			stuRegisterList.add(studentRegister);

		stuRegisterMongoRespository.save(stuRegisterList);

		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/addRollCalldata", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成MongoDB学生考勤数据", response = Void.class, notes = "生成MongoDB学生考勤数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addRollCallData(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "jobNum 学号") @RequestParam(value = "jobNum", required = true) String jobNum,
			@ApiParam(value = "stuName 姓名") @RequestParam(value = "stuName", required = true) String stuName,
			@ApiParam(value = "num 迟到次数") @RequestParam(value = "num", required = false) int num) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null != orgId && orgId.longValue() > 0L) {
		} else {
			orgId = 1L;
		}
		List<RollCall> rollCallList = new ArrayList<RollCall>();
			
            if(num > 0){
            	for(int i=0;i<num;i++){
            		RollCall rollCall = new RollCall();
            	rollCall.setUserId(1L);
    			rollCall.setClassId(1L);
    			rollCall.setClassName("测试1班");
    			rollCall.setCollegeId(1L);
    			rollCall.setCollegeName("测试学院");
    			rollCall.setGrade("大1");
    			rollCall.setJobNum(jobNum);
    			rollCall.setOrgId(orgId);
    			rollCall.setProfessionalId(1L);
    			rollCall.setProfessionalName("测试专业");
    			rollCall.setSchoolYear(2017);
    			rollCall.setSemester(2);
    			rollCall.setUserName(stuName);
    			rollCall.setScheduleId(1L);
    			rollCall.setCourseName("大学英语");
    			rollCall.setCourseType("必修");
    			rollCall.setRollCallDate(DateUtil.getMonday(new Date()));
    			rollCall.setRollCallType(1);
    			rollCall.setRollCallResult(2);
    			rollCallList.add(rollCall);
            	}
    		rollCallMongoRespository.save(rollCallList);

            }
			
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/addScoredata", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成MongoDB修读异常数据", response = Void.class, notes = "上学期不及格必修课学分大于等于num<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addScoreData(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "jobNum 学号") @RequestParam(value = "jobNum", required = true) String jobNum,
			@ApiParam(value = "stuName 姓名") @RequestParam(value = "stuName", required = true) String stuName,
			@ApiParam(value = "num 不合格必修课学分") @RequestParam(value = "num", required = false) int num) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null != orgId && orgId.longValue() > 0L) {
		} else {
			orgId = 1L;
		}
		List<Score> scoreList = new ArrayList<Score>();
			Score score = new Score();

			score.setUserId(1L);
			score.setClassId(1L);
			score.setClassName("测试1班");
			score.setCollegeId(1L);
			score.setCollegeName("测试学院");
			score.setJobNum(jobNum);
			score.setOrgId(orgId);
			score.setProfessionalId(1L);
			score.setProfessionalName("测试专业");
			score.setSchoolYear(2017);
			score.setGrade("2017");
			score.setSemester(1);
			score.setUserName(stuName);
			score.setScheduleId("1");
			score.setCourseName("英语四级");
			score.setCourseType(ScoreConstant.REQUIRED_COURSE);
			score.setExamTime(DateUtil.getMonday(new Date()));
			score.setExamType(ScoreConstant.EXAM_TYPE_COURSE);
			score.setTotalScore(33);
			score.setScoreResultType("百分制");
			score.setCredit(Float.valueOf(num));
			score.setGradePoint(0);

			scoreList.add(score);
	
		scoreMongoRespository.save(scoreList);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/addScoredata1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成MongoDB总评成绩数据", response = Void.class, notes = "上学期不及格课程门数大于等于num（目前只支持一个条件：不及格门数）<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addScoreData1(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "jobNum 学号") @RequestParam(value = "jobNum", required = true) String jobNum,
			@ApiParam(value = "stuName 姓名") @RequestParam(value = "stuName", required = true) String stuName,
			@ApiParam(value = "num 不及格课程门数") @RequestParam(value = "num", required = false) int num) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null != orgId && orgId.longValue() > 0L) {
		} else {
			orgId = 1L;
		}
		if(num == 0){
			num =1;
		}
		List<Score> scoreList = new ArrayList<Score>();
			
			for(int i=0;i<num;i++){
				Score score = new Score();
			score.setUserId(1L);
			score.setClassId(1L);
			score.setClassName("测试1班");
			score.setCollegeId(1L);
			score.setCollegeName("测试学院");
			score.setJobNum(jobNum);
			score.setOrgId(orgId);
			score.setProfessionalId(1L);
			score.setProfessionalName("测试专业");
			score.setSchoolYear(2017);
			score.setGrade("2017");
			score.setSemester(1);
			score.setUserName(stuName);
			score.setScheduleId("1");
			score.setCourseName("英语四级");
			score.setCourseType(ScoreConstant.REQUIRED_COURSE);
			score.setExamTime(DateUtil.getMonday(new Date()));
			score.setExamType(ScoreConstant.EXAM_TYPE_COURSE);
			score.setTotalScore(33);
			score.setScoreResultType("百分制");
			score.setCredit(Float.valueOf(num));
			score.setGradePoint(0);
			scoreList.add(score);
            }	
		scoreMongoRespository.save(scoreList);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/addScoredata2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成MongoDB补考成绩数据", response = Void.class, notes = "三年内不及格课程门数大于等于num<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addScoreData2(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "jobNum 学号") @RequestParam(value = "jobNum", required = true) String jobNum,
			@ApiParam(value = "stuName 姓名") @RequestParam(value = "stuName", required = true) String stuName,
			@ApiParam(value = "num 不及格课程门数") @RequestParam(value = "num", required = false) int num) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null != orgId && orgId.longValue() > 0L) {
		} else {
			orgId = 1L;
		}
		if(num == 0){
			num =1;
		}
		List<Score> scoreList = new ArrayList<Score>();
			
			for(int i=0;i<num;i++){
				Score score = new Score();
			score.setUserId(1L);
			score.setClassId(1L);
			score.setClassName("测试1班");
			score.setCollegeId(1L);
			score.setCollegeName("测试学院");
			score.setJobNum(jobNum);
			score.setOrgId(orgId);
			score.setProfessionalId(1L);
			score.setProfessionalName("测试专业");
			score.setSchoolYear(2017);
			score.setGrade("2017");
			score.setSemester(1);
			score.setUserName(stuName);
			score.setScheduleId(UUID.randomUUID().toString());
			score.setCourseName("英语四级");
			score.setCourseType(ScoreConstant.REQUIRED_COURSE);
			score.setExamTime(DateUtil.getMonday(new Date()));
			score.setExamType(ScoreConstant.EXAM_TYPE_COURSE);
			score.setTotalScore(33);
			score.setScoreResultType("百分制");
			score.setCredit(Float.valueOf(num));
			score.setGradePoint(0);
			scoreList.add(score);
            }	
		scoreMongoRespository.save(scoreList);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/addScoredata3", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成MongoDB成绩波动数据", response = Void.class, notes = "本学期前2学期成绩平均绩点下降大于等于num(目前只支持一个条件平均绩点下降数值大于等于)<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addScoreData3(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "jobNum 学号") @RequestParam(value = "jobNum", required = true) String jobNum,
			@ApiParam(value = "stuName 姓名") @RequestParam(value = "stuName", required = true) String stuName,
			@ApiParam(value = "num 下降绩点") @RequestParam(value = "num", required = false) Float num) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null != orgId && orgId.longValue() > 0L) {
		} else {
			orgId = 1L;
		}
		if(StringUtils.isEmpty(num)){
			num = 1F;
		}
		List<Score> scoreList = new ArrayList<Score>();
			
				Score score = new Score();
			score.setUserId(1L);
			score.setClassId(1L);
			score.setClassName("测试1班");
			score.setCollegeId(1L);
			score.setCollegeName("测试学院");
			score.setJobNum(jobNum);
			score.setOrgId(orgId);
			score.setProfessionalId(1L);
			score.setProfessionalName("测试专业");
			score.setSchoolYear(2016);
			score.setGrade("2016");
			score.setSemester(2);
			score.setUserName(stuName);
			score.setScheduleId(UUID.randomUUID().toString());
			score.setCourseName("英语四级");
			score.setCourseType(ScoreConstant.REQUIRED_COURSE);
			score.setExamTime(DateUtil.getMonday(new Date()));
			score.setExamType(ScoreConstant.EXAM_TYPE_COURSE);
			score.setTotalScore(num * 10 + 60);
			score.setScoreResultType("百分制");
			score.setCredit(num);
			score.setGradePoint(num + 1);
			scoreList.add(score);
			
			score = new Score();
			score.setUserId(1L);
			score.setClassId(1L);
			score.setClassName("测试1班");
			score.setCollegeId(1L);
			score.setCollegeName("测试学院");
			score.setJobNum(jobNum);
			score.setOrgId(orgId);
			score.setProfessionalId(1L);
			score.setProfessionalName("测试专业");
			score.setSchoolYear(2017);
			score.setGrade("2017");
			score.setSemester(1);
			score.setUserName(stuName);
			score.setScheduleId(UUID.randomUUID().toString());
			score.setCourseName("英语四级");
			score.setCourseType(ScoreConstant.REQUIRED_COURSE);
			score.setExamTime(DateUtil.getMonday(new Date()));
			score.setExamType(ScoreConstant.EXAM_TYPE_COURSE);
			score.setTotalScore(60);
			score.setScoreResultType("百分制");
			score.setCredit(num);
			score.setGradePoint(1);
			scoreList.add(score);
		scoreMongoRespository.save(scoreList);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/addScoredata4", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成MongoDB英语4级成绩数据", response = Void.class, notes = "num为在校学年数<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addScoreData4(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "jobNum 学号") @RequestParam(value = "jobNum", required = true) String jobNum,
			@ApiParam(value = "stuName 姓名") @RequestParam(value = "stuName", required = true) String stuName,
			@ApiParam(value = "num 学年数") @RequestParam(value = "num", required = false) int num) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null != orgId && orgId.longValue() > 0L) {
		} else {
			orgId = 1L;
		}
		if(num == 0){
			num =1;
		}
		List<Score> scoreList = new ArrayList<Score>();
			
			Score score = new Score();
			score.setUserId(1L);
			score.setClassId(1L);
			score.setClassName("测试1班");
			score.setCollegeId(1L);
			score.setCollegeName("测试学院");
			score.setJobNum(jobNum);
			score.setOrgId(orgId);
			score.setProfessionalId(1L);
			score.setProfessionalName("测试专业");
			score.setSchoolYear(2017 - num);
			score.setGrade(String.valueOf(2017 - num));
			score.setSemester(1);
			score.setUserName(stuName);
			score.setScheduleId(UUID.randomUUID().toString());
			score.setCourseName("英语四级");
			score.setCourseType(ScoreConstant.REQUIRED_COURSE);
			score.setExamTime(DateUtil.getMonday(new Date()));
			score.setExamType(ScoreConstant.EXAM_TYPE_CET4);
			score.setTotalScore(410);
			score.setScoreResultType("百分制");
			score.setCredit(Float.valueOf(num));
			score.setGradePoint(0);
			scoreList.add(score);
		scoreMongoRespository.save(scoreList);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/importStudent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "批量导入新生报到信息", response = Void.class, notes = "批量导入新生报到信息<br><br><b>@author bly</b>")
	public ResponseEntity<Void> importStudent(
//			@ApiParam(value = "studentInfoFile 学生信息文件", required = true) @RequestParam(value = "studentInfoFile") MultipartFile studentInfoFile,
//			@ApiParam(value = "dataBaseFile 基础数据文件", required = true) @RequestParam(value = "dataBaseFile") MultipartFile dataBaseFile,
//			@ApiParam(value = "newStudentInfoFile 基础数据文件", required = true) @RequestParam(value = "newStudentInfoFile") MultipartFile newStudentInfoFile,
//			@DateTimeFormat(pattern = "yyyy-MM-dd") @ApiParam(value = "registerDate 报到日期,传参时日期天数加1,<br/>时间格式：yyyy-MM-dd") @RequestParam(value = "registerDate", required = false) Date registerDate,
//			@ApiParam(value = "orgId 学校ID") @RequestParam(value = "orgId") Long orgId
			) {
			registerService.importData("开发环境桂林理工14-16级学生信息.xlsx", 
					"学生（学工类）信息2014-2016.xlsx", "开发环境桂林理工学生信息替换文件.xlsx");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/importScore", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "批量导入成绩", response = Void.class, notes = "批量导入成绩<br><br><b>@author bly</b>")
	public ResponseEntity<Void> importScore(
//			@ApiParam(value = "scoreFile 成绩文件", required = true) @RequestParam(value = "scoreFile") MultipartFile scoreFile,
//			@ApiParam(value = "newStudentInfoFile 基础数据文件", required = true) @RequestParam(value = "newStudentInfoFile") MultipartFile newStudentInfoFile,
			@ApiParam(value = "orgId 学校ID", required = true) @RequestParam(value = "orgId") Long orgId) {
		scoreService.importData("成绩数据.xlsx", "开发环境桂林理工17级新学生信息.xlsx", orgId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/importRollCall", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "批量导入考勤", response = Void.class, notes = "批量导入考勤<br><br><b>@author bly</b>")
	public ResponseEntity<Void> importRollCall(
//			@ApiParam(value = "dataBaseFile 考勤文件", required = true) @RequestParam(value = "dataBaseFile") MultipartFile dataBaseFile,
//			@ApiParam(value = "newStudentInfoFile 基础数据文件", required = true) @RequestParam(value = "newStudentInfoFile") MultipartFile newStudentInfoFile,
			@ApiParam(value = "orgId 学校ID", required = true) @RequestParam(value = "orgId") Long orgId) {
		rollCallService.importData("考勤6w(1)数据.xlsx", "开发环境桂林理工17级新学生信息.xlsx", orgId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/importScore46", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "批量导入为英语4级的成绩", response = Void.class, notes = "批量导入成绩<br><br><b>@author bly</b>")
	public ResponseEntity<Void> importScore46(
//			@ApiParam(value = "scoreFile 成绩文件", required = true) @RequestParam(value = "scoreFile") MultipartFile scoreFile,
//			@ApiParam(value = "newStudentInfoFile 基础数据文件", required = true) @RequestParam(value = "newStudentInfoFile") MultipartFile newStudentInfoFile,
			@ApiParam(value = "orgId 学校ID", required = true) @RequestParam(value = "orgId") Long orgId) {
		scoreService.importData46("成绩数据.xlsx", "开发环境桂林理工17级新学生信息.xlsx", orgId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/importPractice", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "批量导入实践信息", response = Void.class, notes = "批量导入实践信息<br><br><b>@author bly</b>")
	public ResponseEntity<Void> importPractice(
//			@ApiParam(value = "practiceFile 实践数据", required = true) @RequestParam(value = "practiceFile") MultipartFile practiceFile,
//			@ApiParam(value = "newStudentInfoFile 基础数据文件", required = true) @RequestParam(value = "newStudentInfoFile") MultipartFile newStudentInfoFile,
			@ApiParam(value = "orgId 学校ID", required = true) @RequestParam(value = "orgId") Long orgId) {
		practiceService.importData("实践2016第二学期数据.xlsx", "开发环境桂林理工17级新学生信息.xlsx", orgId);
		return new ResponseEntity<>(HttpStatus.OK);
	}




	@RequestMapping(value = "/dataInit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "学校数据转换", response = Void.class, notes = "学校数据转换<br><br><b>@author xg</b>")
	public ResponseEntity<Void> dataInit() {
		new Thread(){
			@Override
			public void run() {
				initService.start();
			}
		}.start();
		return new ResponseEntity<>(HttpStatus.OK);
	}
}