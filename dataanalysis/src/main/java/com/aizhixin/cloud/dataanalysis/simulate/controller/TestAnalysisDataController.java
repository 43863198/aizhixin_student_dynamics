/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.simulate.controller;

import java.util.*;

import com.aizhixin.cloud.dataanalysis.analysis.entity.TeachingScoreDetails;
import com.aizhixin.cloud.dataanalysis.analysis.job.CetStatisticsAnalysisJob;
import com.aizhixin.cloud.dataanalysis.analysis.job.TeachingScoreAnalysisJob;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aizhixin.cloud.dataanalysis.analysis.entity.CetScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.PracticeStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.job.SchoolStatisticsAnalysisJob;
import com.aizhixin.cloud.dataanalysis.analysis.service.CetStatisticAnalysisService;
import com.aizhixin.cloud.dataanalysis.analysis.service.PracticeStatisticsService;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolStatisticsService;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author zhengning
 *
 */
@RestController
@RequestMapping("/v1/test/analysis")
@Api(value = "测试生成模拟数据操作", description = "测试生成模拟数据操作")
public class TestAnalysisDataController {
	private final static Logger log = LoggerFactory
			.getLogger(TestAnalysisDataController.class);

	@Autowired
	private SchoolStatisticsService schoolStatisticsService;
	@Autowired
	private CetStatisticAnalysisService cetStatisticAnalysisService;
	@Autowired
	private PracticeStatisticsService practiceStatisticsService;
	@Autowired
	private SchoolStatisticsAnalysisJob schoolStatisticsAnalysisJob;
	@Autowired
	private TeachingScoreAnalysisJob teachingScoreAnalysisJob;
	@Autowired
	private CetStatisticsAnalysisJob cetStatisticsAnalysisJob;
	@Autowired
	private MongoTemplate mongoTemplate;

	@GetMapping(value = "/addschoolstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "生成学情分析学校人数统计", response = Void.class, notes = "生成学情分析学校人数统计<br><br><b>@author  jianwei.wu</b>")
	public Map<String, Object> addSchoolStatistics(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "teacherYear 学年)") @RequestParam(value = "teacherYear", required = true) int teacherYear
	) {
		return schoolStatisticsAnalysisJob.schoolStatistics(orgId, teacherYear);
	}

	@GetMapping(value = "/addteachingscoredetail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "手动添加教学成绩详情", response = Void.class, notes = "手动添加教学成绩详情<br><br><b>@author jianwei.wu</b>")
	public Map<String, Object> addTeachingScoreDetail(
			@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
			@ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
			@ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester", required = true) Integer semester
	) {
		return teachingScoreAnalysisJob.teachingScoreDetails(orgId, teacherYear, semester);
	}
	@GetMapping(value = "/addteachingscorestatistics", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "手动添加教学成绩统计", response = Void.class, notes = "手动添加教学成绩统计<br><br><b>@author jianwei.wu</b>")
	public Map<String, Object> addTeachingScoreStatistics(
			@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
			@ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
			@ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester", required = true) Integer semester
	) {
		return teachingScoreAnalysisJob.teachingScoreStatistics(orgId, teacherYear, semester);
	}
	@GetMapping(value = "/addcetscorestatistics", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "手动添加cet成绩统计", response = Void.class, notes = "手动添加cet成绩统计<br><br><b>@author jianwei.wu</b>")
	public Map<String, Object> cetScoreStatistics(
			@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
			@ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
			@ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester", required = true) Integer semester
	) {
		return cetStatisticsAnalysisJob.cetScoreStatistics(orgId, teacherYear, semester);
	}


//	@RequestMapping(value = "/schoolstatisticsjob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiOperation(httpMethod = "GET", value = "迎新统计定时任务", response = Void.class, notes = "迎新统计定时任务<br><br><b>@author zhengning</b>")
//	public ResponseEntity<Map<String, Object>> schoolStatisticsjob(
//			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId
//			) {
//        if(null == orgId){
//        	orgId = 218L;
//        }
//        schoolStatisticsService.deleteAllByOrgId(orgId);
//        schoolStatisticsAnalysisJob.schoolStatisticsJob();
//		Map<String, Object> result = new HashMap<String, Object>();
//		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
//	}
//		if (null != orgId && orgId.longValue() > 0L) {
//		} else {
//			orgId = 1L;
//		}
//		if (num == 0) {
//			num = 1;
//		}
//
//		List<SchoolStatistics> schoolStatisticsList = new ArrayList<SchoolStatistics>();
//		for (int i = 0; i < num; i++) {
//
//			int max = 40;
//			int min = 5;
//			Random random = new Random();
//			int s = 0;
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics1 = new SchoolStatistics();
//			schoolStatistics1.setId(UUID.randomUUID().toString());
//			schoolStatistics1.setOrgId(orgId);
//			schoolStatistics1.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics1.setCollegeName("机械与控制工程学院");
//			schoolStatistics1.setCollegeId(1722L);
//			schoolStatistics1.setNewStudentsCount(365 - s);
//			schoolStatistics1.setAlreadyReport(363 - s);
//			schoolStatistics1.setAlreadyPay(355 - s);
//			schoolStatistics1.setConvenienceChannel(35);
//			schoolStatistics1.setTeacherNumber(26);
//			schoolStatistics1.setStudentNumber(1375 - s);
//			schoolStatistics1.setInstructorNumber(25);
//			schoolStatistics1.setReadyGraduation(345 - s);
//			schoolStatistics1.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics1);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics2 = new SchoolStatistics();
//			schoolStatistics2.setId(UUID.randomUUID().toString());
//			schoolStatistics2.setOrgId(orgId);
//			schoolStatistics2.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics2.setCollegeName("外国语学院");
//			schoolStatistics2.setCollegeId(1758L);
//			schoolStatistics2.setNewStudentsCount(204 - s);
//			schoolStatistics2.setAlreadyReport(201 - s);
//			schoolStatistics2.setAlreadyPay(200 - s);
//			schoolStatistics2.setConvenienceChannel(52 - s);
//			schoolStatistics2.setTeacherNumber(16);
//			schoolStatistics2.setStudentNumber(875 - s);
//			schoolStatistics2.setInstructorNumber(10);
//			schoolStatistics2.setReadyGraduation(214 - s);
//			schoolStatistics2.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics2);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics3 = new SchoolStatistics();
//			schoolStatistics3.setId(UUID.randomUUID().toString());
//			schoolStatistics3.setOrgId(orgId);
//			schoolStatistics3.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics3.setCollegeName("测绘地理信息学院");
//			schoolStatistics3.setCollegeId(1724L);
//			schoolStatistics3.setNewStudentsCount(206 - s);
//			schoolStatistics3.setAlreadyReport(206 - s);
//			schoolStatistics3.setAlreadyPay(198 - s);
//			schoolStatistics3.setConvenienceChannel(58 - s);
//			schoolStatistics3.setTeacherNumber(17);
//			schoolStatistics3.setStudentNumber(865 - s);
//			schoolStatistics3.setInstructorNumber(10);
//			schoolStatistics3.setReadyGraduation(217 - s);
//			schoolStatistics3.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics3);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics4 = new SchoolStatistics();
//			schoolStatistics4.setId(UUID.randomUUID().toString());
//			schoolStatistics4.setOrgId(orgId);
//			schoolStatistics4.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics4.setCollegeName("人文社会科学学院");
//			schoolStatistics4.setCollegeId(1754L);
//			schoolStatistics4.setNewStudentsCount(306 - s);
//			schoolStatistics4.setAlreadyReport(296 - s);
//			schoolStatistics4.setAlreadyPay(290 - s);
//			schoolStatistics4.setConvenienceChannel(55 - s);
//			schoolStatistics4.setTeacherNumber(23);
//			schoolStatistics4.setStudentNumber(1158 - s);
//			schoolStatistics4.setInstructorNumber(15);
//			schoolStatistics4.setReadyGraduation(312 - s);
//			schoolStatistics4.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics4);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics5 = new SchoolStatistics();
//			schoolStatistics5.setId(UUID.randomUUID().toString());
//			schoolStatistics5.setOrgId(orgId);
//			schoolStatistics5.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics5.setCollegeName("化学与生物工程学院");
//			schoolStatistics5.setCollegeId(1720L);
//			schoolStatistics5.setNewStudentsCount(345 - s);
//			schoolStatistics5.setAlreadyReport(345 - s);
//			schoolStatistics5.setAlreadyPay(340 - s);
//			schoolStatistics5.setConvenienceChannel(60 - s);
//			schoolStatistics5.setTeacherNumber(18);
//			schoolStatistics5.setStudentNumber(1275 - s);
//			schoolStatistics5.setInstructorNumber(13);
//			schoolStatistics5.setReadyGraduation(327 - s);
//			schoolStatistics5.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics5);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics6 = new SchoolStatistics();
//			schoolStatistics6.setId(UUID.randomUUID().toString());
//			schoolStatistics6.setOrgId(orgId);
//			schoolStatistics6.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics6.setCollegeName("旅游学院");
//			schoolStatistics6.setCollegeId(1752L);
//			schoolStatistics6.setNewStudentsCount(265 - s);
//			schoolStatistics6.setAlreadyReport(230 - s);
//			schoolStatistics6.setAlreadyPay(228 - s);
//			schoolStatistics6.setConvenienceChannel(58 - s);
//			schoolStatistics6.setTeacherNumber(17);
//			schoolStatistics6.setStudentNumber(857 - s);
//			schoolStatistics6.setInstructorNumber(10);
//			schoolStatistics6.setReadyGraduation(258 - s);
//			schoolStatistics6.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics6);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics7 = new SchoolStatistics();
//			schoolStatistics7.setId(UUID.randomUUID().toString());
//			schoolStatistics7.setOrgId(orgId);
//			schoolStatistics7.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics7.setCollegeName("信息科学与工程学院");
//			schoolStatistics7.setCollegeId(1718L);
//			schoolStatistics7.setNewStudentsCount(545 - s);
//			schoolStatistics7.setAlreadyReport(525 - s);
//			schoolStatistics7.setAlreadyPay(520 - s);
//			schoolStatistics7.setConvenienceChannel(72 - s);
//			schoolStatistics7.setTeacherNumber(46);
//			schoolStatistics7.setStudentNumber(2278 - s);
//			schoolStatistics7.setInstructorNumber(25);
//			schoolStatistics7.setReadyGraduation(558 - s);
//			schoolStatistics7.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics7);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics8 = new SchoolStatistics();
//			schoolStatistics8.setId(UUID.randomUUID().toString());
//			schoolStatistics8.setOrgId(orgId);
//			schoolStatistics8.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics8.setCollegeName("管理学院");
//			schoolStatistics8.setCollegeId(1750L);
//			schoolStatistics8.setNewStudentsCount(745 - s);
//			schoolStatistics8.setAlreadyReport(744 - s);
//			schoolStatistics8.setAlreadyPay(724 - s);
//			schoolStatistics8.setConvenienceChannel(90 - s);
//			schoolStatistics8.setTeacherNumber(65);
//			schoolStatistics8.setStudentNumber(2695 - s);
//			schoolStatistics8.setInstructorNumber(32);
//			schoolStatistics8.setReadyGraduation(750 - s);
//			schoolStatistics8.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics8);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics9 = new SchoolStatistics();
//			schoolStatistics9.setId(UUID.randomUUID().toString());
//			schoolStatistics9.setOrgId(orgId);
//			schoolStatistics9.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics9.setCollegeName("材料科学与工程学院");
//			schoolStatistics9.setCollegeId(1716L);
//			schoolStatistics9.setNewStudentsCount(357 - s);
//			schoolStatistics9.setAlreadyReport(348 - s);
//			schoolStatistics9.setAlreadyPay(338 - s);
//			schoolStatistics9.setConvenienceChannel(58 - s);
//			schoolStatistics9.setTeacherNumber(23);
//			schoolStatistics9.setStudentNumber(1358 - s);
//			schoolStatistics9.setInstructorNumber(18);
//			schoolStatistics9.setReadyGraduation(313 - s);
//			schoolStatistics9.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics9);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics10 = new SchoolStatistics();
//			schoolStatistics10.setId(UUID.randomUUID().toString());
//			schoolStatistics10.setOrgId(orgId);
//			schoolStatistics10.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics10.setCollegeName("理学院");
//			schoolStatistics10.setCollegeId(1748L);
//			schoolStatistics10.setNewStudentsCount(307 - s);
//			schoolStatistics10.setAlreadyReport(300 - s);
//			schoolStatistics10.setAlreadyPay(293 - s);
//			schoolStatistics10.setConvenienceChannel(59 - s);
//			schoolStatistics10.setTeacherNumber(19);
//			schoolStatistics10.setStudentNumber(1668 - s);
//			schoolStatistics10.setInstructorNumber(11);
//			schoolStatistics10.setReadyGraduation(324 - s);
//			schoolStatistics10.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics10);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics11 = new SchoolStatistics();
//			schoolStatistics11.setId(UUID.randomUUID().toString());
//			schoolStatistics11.setOrgId(orgId);
//			schoolStatistics11.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics11.setCollegeName("土木与建筑工程学院");
//			schoolStatistics11.setCollegeId(1714L);
//			schoolStatistics11.setNewStudentsCount(467 - s);
//			schoolStatistics11.setAlreadyReport(445 - s);
//			schoolStatistics11.setAlreadyPay(436 - s);
//			schoolStatistics11.setConvenienceChannel(65 - s);
//			schoolStatistics11.setTeacherNumber(22);
//			schoolStatistics11.setStudentNumber(1668 - s);
//			schoolStatistics11.setInstructorNumber(16);
//			schoolStatistics11.setReadyGraduation(461 - s);
//			schoolStatistics11.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics11);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics12 = new SchoolStatistics();
//			schoolStatistics12.setId(UUID.randomUUID().toString());
//			schoolStatistics12.setOrgId(orgId);
//			schoolStatistics12.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics12.setCollegeName("环境科学与工程学院");
//			schoolStatistics12.setCollegeId(1712L);
//			schoolStatistics12.setNewStudentsCount(331);
//			schoolStatistics12.setAlreadyReport(331 - s);
//			schoolStatistics12.setAlreadyPay(318 - s);
//			schoolStatistics12.setConvenienceChannel(69 - s);
//			schoolStatistics12.setTeacherNumber(18);
//			schoolStatistics12.setStudentNumber(978 - s);
//			schoolStatistics12.setInstructorNumber(10);
//			schoolStatistics12.setReadyGraduation(318 - s);
//			schoolStatistics12.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics12);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics13 = new SchoolStatistics();
//			schoolStatistics13.setId(UUID.randomUUID().toString());
//			schoolStatistics13.setOrgId(orgId);
//			schoolStatistics13.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics13.setCollegeName("地球科学学院");
//			schoolStatistics13.setCollegeId(1710L);
//			schoolStatistics13.setNewStudentsCount(240 - s);
//			schoolStatistics13.setAlreadyReport(237 - s);
//			schoolStatistics13.setAlreadyPay(225 - s);
//			schoolStatistics13.setConvenienceChannel(52 - s);
//			schoolStatistics13.setTeacherNumber(13);
//			schoolStatistics13.setStudentNumber(856 - s);
//			schoolStatistics13.setInstructorNumber(20);
//			schoolStatistics13.setReadyGraduation(221 - s);
//			schoolStatistics13.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics13);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			SchoolStatistics schoolStatistics14 = new SchoolStatistics();
//			schoolStatistics14.setId(UUID.randomUUID().toString());
//			schoolStatistics14.setOrgId(orgId);
//			schoolStatistics14.setTeacherYear(Integer.valueOf(teacherYear - i));
//			schoolStatistics14.setCollegeName("艺术学院");
//			schoolStatistics14.setCollegeId(1760L);
//			schoolStatistics14.setNewStudentsCount(391 - s);
//			schoolStatistics14.setAlreadyReport(378 - s);
//			schoolStatistics14.setAlreadyPay(367 - s);
//			schoolStatistics14.setConvenienceChannel(51 - s);
//			schoolStatistics14.setTeacherNumber(20);
//			schoolStatistics14.setStudentNumber(1388 - s);
//			schoolStatistics14.setInstructorNumber(14);
//			schoolStatistics14.setReadyGraduation(375 - s);
//			schoolStatistics14.setStatisticalTime(new Date());
//			schoolStatisticsList.add(schoolStatistics14);

//		}
//
//		schoolStatisticsService.deleteByOrgIdAndTeacherYear(orgId,teacherYear);
//	    schoolStatisticsService.saveList(schoolStatisticsList);
//		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
//	}
//
//	@RequestMapping(value = "/updateschoolStatistics", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiOperation(httpMethod = "PUT", value = "修改学情分析学校人数统计数据", response = Void.class, notes = "修改学情分析学校人数统计数据<br><br><b>@author zhengning</b>")
//	public ResponseEntity<Map<String, Object>> updateSchoolStatistics(
//			@ApiParam(value = "id 记录id") @RequestParam(value = "id", required = false) String id,
//			@ApiParam(value = "newStudentsCount 新生人数)") @RequestParam(value = "newStudentsCount", required = false) Integer newStudentsCount,
//			@ApiParam(value = "alreadyReport 已报到人数)") @RequestParam(value = "alreadyReport", required = false) Integer alreadyReport,
//			@ApiParam(value = "alreadyPay 已缴费人数)") @RequestParam(value = "alreadyPay", required = false) Integer alreadyPay,
//			@ApiParam(value = "convenienceChannel 绿色通道人数)") @RequestParam(value = "convenienceChannel", required = false) Integer convenienceChannel,
//			@ApiParam(value = "teacherNumber 院系老师总人数)") @RequestParam(value = "teacherNumber", required = false) Integer teacherNumber,
//			@ApiParam(value = "studentNumber 院系学生总人数)") @RequestParam(value = "studentNumber", required = false) Integer studentNumber,
//			@ApiParam(value = "instructorNumber 院系辅导员总人数)") @RequestParam(value = "instructorNumber", required = false) Integer instructorNumber,
//			@ApiParam(value = "readyGraduation 即将毕业人数)") @RequestParam(value = "readyGraduation", required = false) Integer readyGraduation) {
//		Map<String, Object> result = new HashMap<String, Object>();
//
//		SchoolStatistics schoolStatistics = schoolStatisticsService
//				.findById(id);
//		if(null != schoolStatistics){
//			if(null != newStudentsCount && newStudentsCount > 0){
//				schoolStatistics.setNewStudentsCount(newStudentsCount);
//			}
//			if(null != alreadyReport && alreadyReport > 0){
//				schoolStatistics.setAlreadyReport(alreadyReport);
//			}
//			if(null != alreadyPay && alreadyPay > 0){
//				schoolStatistics.setAlreadyPay(alreadyPay);
//			}
//			if(null != convenienceChannel && convenienceChannel > 0){
//				schoolStatistics.setConvenienceChannel(convenienceChannel);
//			}
//			if(null != teacherNumber && teacherNumber > 0){
//				schoolStatistics.setTeacherNumber(teacherNumber);
//			}
//			if(null != studentNumber && studentNumber > 0){
//				schoolStatistics.setStudentNumber(studentNumber);
//			}
//			if(null != instructorNumber && instructorNumber > 0){
//				schoolStatistics.setInstructorNumber(instructorNumber);
//			}
//			if(null != readyGraduation && readyGraduation > 0){
//				schoolStatistics.setReadyGraduation(readyGraduation);
//			}
//		}
//		schoolStatisticsService.save(schoolStatistics);
//		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
//	}
//
//
//	@RequestMapping(value = "/addcetstatistics", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiOperation(httpMethod = "POST", value = "生成学情分析英语等级考试统计数据(生成模拟数据前会清空表数据请谨慎使用)", response = Void.class, notes = "生成学情分析英语等级考试统计数据<br><br><b>@author zhengning</b>")
//	public ResponseEntity<Map<String, Object>> addCetStatistics(
//			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
//			@ApiParam(value = "teacherYear 学年)") @RequestParam(value = "teacherYear", required = true) int teacherYear,
//			@ApiParam(value = "num teacherYear几年内)") @RequestParam(value = "num", required = false) int num) {
//		Map<String, Object> result = new HashMap<String, Object>();
//
//		if (null != orgId && orgId.longValue() > 0L) {
//		} else {
//			orgId = 1L;
//		}
//		if (num == 0) {
//			num = 1;
//		}
//
//		List<CetScoreStatistics> cetStatisticsList = new ArrayList<CetScoreStatistics>();
//		for (int i = 0; i < num; i++) {
//
//			for(int j=1;j<3;j++){
//			int max = 40;
//			int min = 5;
//			Random random = new Random();
//			int s = 0;
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//
//			CetScoreStatistics cetScoreStatistics1 = new CetScoreStatistics();
//			cetScoreStatistics1.setId(UUID.randomUUID().toString());
//			cetScoreStatistics1.setOrgId(orgId);
//			cetScoreStatistics1.setTeacherYear(Integer.valueOf((teacherYear-i)));
//			cetScoreStatistics1.setCollegeName("机械与控制工程学院");
//			cetScoreStatistics1.setCollegeId(1722L);
//			cetScoreStatistics1.setCetForeJoinNum(1142-s);
//			cetScoreStatistics1.setCetForePassNum(521-s);
//			cetScoreStatistics1.setCetSixJoinNum(645-s);
//			cetScoreStatistics1.setCetSixPassNum(378-s);
//			cetScoreStatistics1.setSemester(j);
//
//			if(cetScoreStatistics1.getTeacherYear() == 2017 && cetScoreStatistics1.getSemester() == 2){
//				continue;
//			}else{
//				cetStatisticsList.add(cetScoreStatistics1);
//			}
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics2 = new CetScoreStatistics();
//			cetScoreStatistics2.setId(UUID.randomUUID().toString());
//			cetScoreStatistics2.setOrgId(orgId);
//			cetScoreStatistics2.setTeacherYear(Integer.valueOf(teacherYear-i));
//			cetScoreStatistics2.setCollegeName("外国语学院");
//			cetScoreStatistics2.setCollegeId(1758L);
//			cetScoreStatistics2.setCetForeJoinNum(734-s);
//			cetScoreStatistics2.setCetForePassNum(521-s);
//			cetScoreStatistics2.setCetSixJoinNum(645-s);
//			cetScoreStatistics2.setCetSixPassNum(378-s);
//			cetScoreStatistics2.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics2);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics3 = new CetScoreStatistics();
//			cetScoreStatistics3.setId(UUID.randomUUID().toString());
//			cetScoreStatistics3.setOrgId(orgId);
//			cetScoreStatistics3.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics3.setCollegeName("测绘地理信息学院");
//			cetScoreStatistics3.setCollegeId(1724L);
//			cetScoreStatistics3.setCetForeJoinNum(765-s);
//			cetScoreStatistics3.setCetForePassNum(321-s);
//			cetScoreStatistics3.setCetSixJoinNum(145-s);
//			cetScoreStatistics3.setCetSixPassNum(68-s);
//			cetScoreStatistics3.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics3);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics4 = new CetScoreStatistics();
//			cetScoreStatistics4.setId(UUID.randomUUID().toString());
//			cetScoreStatistics4.setOrgId(orgId);
//			cetScoreStatistics4.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics4.setCollegeName("人文社会科学学院");
//			cetScoreStatistics4.setCollegeId(1754L);
//			cetScoreStatistics4.setCetForeJoinNum(665-s);
//			cetScoreStatistics4.setCetForePassNum(321-s);
//			cetScoreStatistics4.setCetSixJoinNum(115-s);
//			cetScoreStatistics4.setCetSixPassNum(61-s);
//			cetScoreStatistics4.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics4);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics5 = new CetScoreStatistics();
//			cetScoreStatistics5.setId(UUID.randomUUID().toString());
//			cetScoreStatistics5.setOrgId(orgId);
//			cetScoreStatistics5.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics5.setCollegeName("化学与生物工程学院");
//			cetScoreStatistics5.setCollegeId(1720L);
//			cetScoreStatistics5.setCetForeJoinNum(1225-s);
//			cetScoreStatistics5.setCetForePassNum(421-s);
//			cetScoreStatistics5.setCetSixJoinNum(216-s);
//			cetScoreStatistics5.setCetSixPassNum(65-s);
//			cetScoreStatistics5.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics5);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics6 = new CetScoreStatistics();
//			cetScoreStatistics6.setId(UUID.randomUUID().toString());
//			cetScoreStatistics6.setOrgId(orgId);
//			cetScoreStatistics6.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics6.setCollegeName("旅游学院");
//			cetScoreStatistics6.setCollegeId(1752L);
//			cetScoreStatistics6.setCetForeJoinNum(675-s);
//			cetScoreStatistics6.setCetForePassNum(321-s);
//			cetScoreStatistics6.setCetSixJoinNum(246-s);
//			cetScoreStatistics6.setCetSixPassNum(95-s);
//			cetScoreStatistics6.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics6);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics7 = new CetScoreStatistics();
//			cetScoreStatistics7.setId(UUID.randomUUID().toString());
//			cetScoreStatistics7.setOrgId(orgId);
//			cetScoreStatistics7.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics7.setCollegeName("信息科学与工程学院");
//			cetScoreStatistics7.setCollegeId(1718L);
//			cetScoreStatistics7.setCetForeJoinNum(1675-s);
//			cetScoreStatistics7.setCetForePassNum(621-s);
//			cetScoreStatistics7.setCetSixJoinNum(486-s);
//			cetScoreStatistics7.setCetSixPassNum(121-s);
//			cetScoreStatistics7.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics7);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics8 = new CetScoreStatistics();
//			cetScoreStatistics8.setId(UUID.randomUUID().toString());
//			cetScoreStatistics8.setOrgId(orgId);
//			cetScoreStatistics8.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics8.setCollegeName("管理学院");
//			cetScoreStatistics8.setCollegeId(1750L);
//			cetScoreStatistics8.setCetForeJoinNum(2175-s);
//			cetScoreStatistics8.setCetForePassNum(921-s);
//			cetScoreStatistics8.setCetSixJoinNum(886-s);
//			cetScoreStatistics8.setCetSixPassNum(329-s);
//			cetScoreStatistics8.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics8);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics9 = new CetScoreStatistics();
//			cetScoreStatistics9.setId(UUID.randomUUID().toString());
//			cetScoreStatistics9.setOrgId(orgId);
//			cetScoreStatistics9.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics9.setCollegeName("材料科学与工程学院");
//			cetScoreStatistics9.setCollegeId(1716L);
//			cetScoreStatistics9.setCetForeJoinNum(1075-s);
//			cetScoreStatistics9.setCetForePassNum(321-s);
//			cetScoreStatistics9.setCetSixJoinNum(386-s);
//			cetScoreStatistics9.setCetSixPassNum(89-s);
//			cetScoreStatistics9.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics9);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics10 = new CetScoreStatistics();
//			cetScoreStatistics10.setId(UUID.randomUUID().toString());
//			cetScoreStatistics10.setOrgId(orgId);
//			cetScoreStatistics10.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics10.setCollegeName("理学院");
//			cetScoreStatistics10.setCollegeId(1748L);
//			cetScoreStatistics10.setCetForeJoinNum(975-s);
//			cetScoreStatistics10.setCetForePassNum(371-s);
//			cetScoreStatistics10.setCetSixJoinNum(296-s);
//			cetScoreStatistics10.setCetSixPassNum(79-s);
//			cetScoreStatistics10.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics10);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics11 = new CetScoreStatistics();
//			cetScoreStatistics11.setId(UUID.randomUUID().toString());
//			cetScoreStatistics11.setOrgId(orgId);
//			cetScoreStatistics11.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics11.setCollegeName("土木与建筑工程学院");
//			cetScoreStatistics11.setCollegeId(1714L);
//			cetScoreStatistics11.setCetForeJoinNum(1475-s);
//			cetScoreStatistics11.setCetForePassNum(671-s);
//			cetScoreStatistics11.setCetSixJoinNum(346-s);
//			cetScoreStatistics11.setCetSixPassNum(129-s);
//			cetScoreStatistics11.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics11);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics12 = new CetScoreStatistics();
//			cetScoreStatistics12.setId(UUID.randomUUID().toString());
//			cetScoreStatistics12.setOrgId(orgId);
//			cetScoreStatistics12.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics12.setCollegeName("环境科学与工程学院");
//			cetScoreStatistics12.setCollegeId(1712L);
//			cetScoreStatistics12.setCetForeJoinNum(675-s);
//			cetScoreStatistics12.setCetForePassNum(371-s);
//			cetScoreStatistics12.setCetSixJoinNum(136-s);
//			cetScoreStatistics12.setCetSixPassNum(69-s);
//			cetScoreStatistics12.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics12);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics13 = new CetScoreStatistics();
//			cetScoreStatistics13.setId(UUID.randomUUID().toString());
//			cetScoreStatistics13.setOrgId(orgId);
//			cetScoreStatistics13.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics13.setCollegeName("地球科学学院");
//			cetScoreStatistics13.setCollegeId(1710L);
//			cetScoreStatistics13.setCetForeJoinNum(625-s);
//			cetScoreStatistics13.setCetForePassNum(311-s);
//			cetScoreStatistics13.setCetSixJoinNum(96-s);
//			cetScoreStatistics13.setCetSixPassNum(59-s);
//			cetScoreStatistics13.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics13);
//
//			random = new Random();
//			s = random.nextInt(max) % (max - min + 1) + min;
//			CetScoreStatistics cetScoreStatistics14 = new CetScoreStatistics();
//			cetScoreStatistics14.setId(UUID.randomUUID().toString());
//			cetScoreStatistics14.setOrgId(orgId);
//			cetScoreStatistics14.setTeacherYear(Integer.valueOf(teacherYear - i));
//			cetScoreStatistics14.setCollegeName("艺术学院");
//			cetScoreStatistics14.setCollegeId(1760L);
//			cetScoreStatistics14.setCetForeJoinNum(825-s);
//			cetScoreStatistics14.setCetForePassNum(211-s);
//			cetScoreStatistics14.setCetSixJoinNum(116-s);
//			cetScoreStatistics14.setCetSixPassNum(61-s);
//			cetScoreStatistics14.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics14);
//			}
//
//		}
//
//		cetStatisticAnalysisService.deleteAllByOrgId(orgId);
//		cetStatisticAnalysisService.saveList(cetStatisticsList);
//		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
//	}
//
//	@RequestMapping(value = "/updatecetstatistics", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiOperation(httpMethod = "PUT", value = "修改学情分析学校人数统计数据", response = Void.class, notes = "修改学情分析学校人数统计数据<br><br><b>@author zhengning</b>")
//	public ResponseEntity<Map<String, Object>> updateCetStatistics(
//			@ApiParam(value = "id 记录id") @RequestParam(value = "id", required = false) String id,
//			@ApiParam(value = "cetForeJoinNum 英语四级报考人数)") @RequestParam(value = "cetForeJoinNum", required = false) Integer cetForeJoinNum,
//			@ApiParam(value = "cetForePassNum 英语四级通过人数)") @RequestParam(value = "cetForePassNum", required = false) Integer cetForePassNum,
//			@ApiParam(value = "cetSixJoinNum 英语六级报考人数)") @RequestParam(value = "cetSixJoinNum", required = false) Integer cetSixJoinNum,
//			@ApiParam(value = "cetSixPassNum 英语六级通过人数)") @RequestParam(value = "cetSixPassNum", required = false) Integer cetSixPassNum) {
//		Map<String, Object> result = new HashMap<String, Object>();
//
//		CetScoreStatistics cetScoreStatistics = cetStatisticAnalysisService
//				.findById(id);
//		 if(null != cetScoreStatistics){
//			if(null != cetForeJoinNum && cetForeJoinNum > 0){
//				cetScoreStatistics.setCetForeJoinNum(cetForeJoinNum);
//			}
//			if(null != cetForePassNum && cetForePassNum > 0){
//				cetScoreStatistics.setCetForePassNum(cetForePassNum);
//			}
//			if(null != cetSixJoinNum && cetSixJoinNum > 0){
//				cetScoreStatistics.setCetSixJoinNum(cetSixJoinNum);
//			}
//			if(null != cetSixPassNum && cetSixPassNum > 0){
//				cetScoreStatistics.setCetSixPassNum(cetSixPassNum);
//			}
//
//		}
//		 cetStatisticAnalysisService.save(cetScoreStatistics);
//		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
//	}
//
//
	@RequestMapping(value = "/addpracticestatistics", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "生成学情分析学生实践统计数据(生成模拟数据前会清空表数据请谨慎使用)", response = Void.class, notes = "生成学情分析学生实践统计数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addracticeStatistics(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "teacherYear 学年)") @RequestParam(value = "teacherYear", required = true) int teacherYear,
			@ApiParam(value = "num teacherYear几年内)") @RequestParam(value = "num", required = false) int num) {
		Map<String, Object> result = new HashMap<String, Object>();

		if (null != orgId && orgId.longValue() > 0L) {
		} else {
			orgId = 1L;
		}
		if (num == 0) {
			num = 1;
		}

		List<PracticeStatistics> practiceStatisticsList = new ArrayList<PracticeStatistics>();
		for (int i = 0; i < num; i++) {

			for(int j=1;j<3;j++){
			int max = 25;
			int min = 5;
			Random random = new Random();
			int s = random.nextInt(max) % (max - min + 1) + min;

			PracticeStatistics practiceStatistics1 = new PracticeStatistics();
			practiceStatistics1.setId(UUID.randomUUID().toString());
			practiceStatistics1.setOrgId(orgId);
			practiceStatistics1.setTeacherYear((teacherYear-i));
			practiceStatistics1.setCollegeName("机械与控制工程学院");
			practiceStatistics1.setCollegeId(1722L);
			practiceStatistics1.setProfessionId(2492L);
			practiceStatistics1.setProfessionName("机械设计制造及其自动化");
			practiceStatistics1.setClassId(10384L);
			practiceStatistics1.setClassName("机械2016-1班");
			practiceStatistics1.setPracticeStudentNum(51L-s);
			practiceStatistics1.setPracticeCompanyNum(1L+i);
			practiceStatistics1.setTaskNum(545L-(s*4));
			practiceStatistics1.setTaskPassNum(530L-(s*4));
			practiceStatistics1.setSemester(j);
//			if(practiceStatistics1.getTeacherYear() == 2017 && practiceStatistics1.getSemester() == 2){
//				continue;
//			}else{
				practiceStatisticsList.add(practiceStatistics1);
//			}

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;

			PracticeStatistics practiceStatistics1a = new PracticeStatistics();
			practiceStatistics1a.setId(UUID.randomUUID().toString());
			practiceStatistics1a.setOrgId(orgId);
			practiceStatistics1a.setTeacherYear((teacherYear-i));
			practiceStatistics1a.setCollegeName("机械与控制工程学院");
			practiceStatistics1a.setCollegeId(1722L);
			practiceStatistics1a.setProfessionId(2492L);
			practiceStatistics1a.setProfessionName("机械设计制造及其自动化");
			practiceStatistics1a.setClassId(10386L);
			practiceStatistics1a.setClassName("机械2016-2班");
			practiceStatistics1a.setPracticeStudentNum(49L-s);
			practiceStatistics1a.setPracticeCompanyNum(1L+i);
			practiceStatistics1a.setTaskNum(395L-(s*4));
			practiceStatistics1a.setTaskPassNum(370L-(s*4));
			practiceStatistics1a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics1a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;

			PracticeStatistics practiceStatistics2a = new PracticeStatistics();
			practiceStatistics2a.setId(UUID.randomUUID().toString());
			practiceStatistics2a.setOrgId(orgId);
			practiceStatistics2a.setTeacherYear(teacherYear-i);
			practiceStatistics2a.setCollegeName("外国语学院");
			practiceStatistics2a.setCollegeId(1758L);
			practiceStatistics2a.setProfessionId(2502L);
			practiceStatistics2a.setProfessionName("日语");
			practiceStatistics2a.setClassId(10792L);
			practiceStatistics2a.setClassName("日语2016-1班");
			practiceStatistics2a.setPracticeStudentNum(45L-s);
			practiceStatistics2a.setPracticeCompanyNum(1L+i);
			practiceStatistics2a.setTaskNum(325L-(s*4));
			practiceStatistics2a.setTaskPassNum(320L-(s*4));
			practiceStatistics2a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics2a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;

			PracticeStatistics practiceStatistics2 = new PracticeStatistics();
			practiceStatistics2.setId(UUID.randomUUID().toString());
			practiceStatistics2.setOrgId(orgId);
			practiceStatistics2.setTeacherYear(teacherYear-i);
			practiceStatistics2.setCollegeName("外国语学院");
			practiceStatistics2.setCollegeId(1758L);
			practiceStatistics2.setProfessionId(2502L);
			practiceStatistics2.setProfessionName("日语");
			practiceStatistics2.setClassId(10794L);
			practiceStatistics2.setClassName("日语2016-2班");
			practiceStatistics2.setPracticeStudentNum(55L-s);
			practiceStatistics2.setPracticeCompanyNum(1L+i);
			practiceStatistics2.setTaskNum(445L-(s*4));
			practiceStatistics2.setTaskPassNum(430L-(s*4));
			practiceStatistics2.setSemester(j);
			practiceStatisticsList.add(practiceStatistics2);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics3 = new PracticeStatistics();
			practiceStatistics3.setId(UUID.randomUUID().toString());
			practiceStatistics3.setOrgId(orgId);
			practiceStatistics3.setTeacherYear(teacherYear - i);
			practiceStatistics3.setCollegeName("测绘地理信息学院");
			practiceStatistics3.setCollegeId(1724L);
			practiceStatistics3.setProfessionId(2576L);
			practiceStatistics3.setProfessionName("测绘类");
			practiceStatistics3.setClassId(10286L);
			practiceStatistics3.setClassName("测绘类2016-5班");
			practiceStatistics3.setPracticeStudentNum(49L-s);
			practiceStatistics3.setPracticeCompanyNum(1L+i);
			practiceStatistics3.setTaskNum(485L-(s*4));
			practiceStatistics3.setTaskPassNum(470L-(s*4));
			practiceStatistics3.setSemester(j);
			practiceStatisticsList.add(practiceStatistics3);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;

			PracticeStatistics practiceStatistics3a = new PracticeStatistics();
			practiceStatistics3a.setId(UUID.randomUUID().toString());
			practiceStatistics3a.setOrgId(orgId);
			practiceStatistics3a.setTeacherYear(teacherYear - i);
			practiceStatistics3a.setCollegeName("测绘地理信息学院");
			practiceStatistics3a.setCollegeId(1724L);
			practiceStatistics3a.setProfessionId(2576L);
			practiceStatistics3a.setProfessionName("测绘类");
			practiceStatistics3a.setClassId(10284L);
			practiceStatistics3a.setClassName("测绘类2016-4班");
			practiceStatistics3a.setPracticeStudentNum(48L-s);
			practiceStatistics3a.setPracticeCompanyNum(1L+i);
			practiceStatistics3a.setTaskNum(475L-(s*4));
			practiceStatistics3a.setTaskPassNum(460L-(s*4));
			practiceStatistics3a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics3a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics4 = new PracticeStatistics();
			practiceStatistics4.setId(UUID.randomUUID().toString());
			practiceStatistics4.setOrgId(orgId);
			practiceStatistics4.setTeacherYear(teacherYear - i);
			practiceStatistics4.setCollegeName("人文社会科学学院");
			practiceStatistics4.setCollegeId(1754L);
			practiceStatistics4.setProfessionId(2530L);
			practiceStatistics4.setProfessionName("广告学");
			practiceStatistics4.setClassId(10428L);
			practiceStatistics4.setClassName("广告2016-1班");
			practiceStatistics4.setPracticeStudentNum(45L-s);
			practiceStatistics4.setPracticeCompanyNum(1L+i);
			practiceStatistics4.setTaskNum(426L-(s*4));
			practiceStatistics4.setTaskPassNum(411L-(s*4));
			practiceStatistics4.setSemester(j);
			practiceStatisticsList.add(practiceStatistics4);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics4a = new PracticeStatistics();
			practiceStatistics4a.setId(UUID.randomUUID().toString());
			practiceStatistics4a.setOrgId(orgId);
			practiceStatistics4a.setTeacherYear(teacherYear - i);
			practiceStatistics4a.setCollegeName("人文社会科学学院");
			practiceStatistics4a.setCollegeId(1754L);
			practiceStatistics4a.setProfessionId(2530L);
			practiceStatistics4a.setProfessionName("广告学");
			practiceStatistics4a.setClassId(10430L);
			practiceStatistics4a.setClassName("广告2016-2班");
			practiceStatistics4a.setPracticeStudentNum(42L-s);
			practiceStatistics4a.setPracticeCompanyNum(1L+i);
			practiceStatistics4a.setTaskNum(423L-(s*4));
			practiceStatistics4a.setTaskPassNum(412L-(s*4));
			practiceStatistics4a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics4a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics5 = new PracticeStatistics();
			practiceStatistics5.setId(UUID.randomUUID().toString());
			practiceStatistics5.setOrgId(orgId);
			practiceStatistics5.setTeacherYear(teacherYear - i);
			practiceStatistics5.setCollegeName("化学与生物工程学院");
			practiceStatistics5.setCollegeId(1720L);
			practiceStatistics5.setProfessionId(2480L);
			practiceStatistics5.setProfessionName("生物工程");
			practiceStatistics5.setClassId(10360L);
			practiceStatistics5.setClassName("生物工程2016-1班");
			practiceStatistics5.setPracticeStudentNum(59L-s);
			practiceStatistics5.setPracticeCompanyNum(1L+i);
			practiceStatistics5.setTaskNum(568L-(s*4));
			practiceStatistics5.setTaskPassNum(548L-(s*4));
			practiceStatistics5.setSemester(j);
			practiceStatisticsList.add(practiceStatistics5);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics5a = new PracticeStatistics();
			practiceStatistics5a.setId(UUID.randomUUID().toString());
			practiceStatistics5a.setOrgId(orgId);
			practiceStatistics5a.setTeacherYear(teacherYear - i);
			practiceStatistics5a.setCollegeName("化学与生物工程学院");
			practiceStatistics5a.setCollegeId(1720L);
			practiceStatistics5a.setProfessionId(2480L);
			practiceStatistics5a.setProfessionName("生物工程");
			practiceStatistics5a.setClassId(10362L);
			practiceStatistics5a.setClassName("生物工程2016-2班");
			practiceStatistics5a.setPracticeStudentNum(58L-s);
			practiceStatistics5a.setPracticeCompanyNum(1L+i);
			practiceStatistics5a.setTaskNum(545L-(s*4));
			practiceStatistics5a.setTaskPassNum(530L-(s*4));
			practiceStatistics5a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics5a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics6a = new PracticeStatistics();
			practiceStatistics6a.setId(UUID.randomUUID().toString());
			practiceStatistics6a.setOrgId(orgId);
			practiceStatistics6a.setTeacherYear(teacherYear - i);
			practiceStatistics6a.setCollegeName("旅游学院");
			practiceStatistics6a.setCollegeId(1752L);
			practiceStatistics6a.setProfessionId(2554L);
			practiceStatistics6a.setProfessionName("风景园林");
			practiceStatistics6a.setClassId(10422L);
			practiceStatistics6a.setClassName("风景园林2016-1班");
			practiceStatistics6a.setPracticeStudentNum(48L-s);
			practiceStatistics6a.setPracticeCompanyNum(1L+i);
			practiceStatistics6a.setTaskNum(479L-(s*4));
			practiceStatistics6a.setTaskPassNum(463L-(s*4));
			practiceStatistics6a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics6a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics6 = new PracticeStatistics();
			practiceStatistics6.setId(UUID.randomUUID().toString());
			practiceStatistics6.setOrgId(orgId);
			practiceStatistics6.setTeacherYear(teacherYear - i);
			practiceStatistics6.setCollegeName("旅游学院");
			practiceStatistics6.setCollegeId(1752L);
			practiceStatistics6.setProfessionId(2554L);
			practiceStatistics6.setProfessionName("风景园林");
			practiceStatistics6.setClassId(10424L);
			practiceStatistics6.setClassName("风景园林2016-2班");
			practiceStatistics6.setPracticeStudentNum(49L-s);
			practiceStatistics6.setPracticeCompanyNum(1L+i);
			practiceStatistics6.setTaskNum(489L-(s*4));
			practiceStatistics6.setTaskPassNum(460L-(s*4));
			practiceStatistics6.setSemester(j);
			practiceStatisticsList.add(practiceStatistics6);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics7a = new PracticeStatistics();
			practiceStatistics7a.setId(UUID.randomUUID().toString());
			practiceStatistics7a.setOrgId(orgId);
			practiceStatistics7a.setTeacherYear(teacherYear - i);
			practiceStatistics7a.setCollegeName("信息科学与工程学院");
			practiceStatistics7a.setCollegeId(1718L);
			practiceStatistics7a.setProfessionId(2380L);
			practiceStatistics7a.setProfessionName("电子信息类");
			practiceStatistics7a.setClassId(10530L);
			practiceStatistics7a.setClassName("电信类2016-2班");
			practiceStatistics7a.setPracticeStudentNum(58L-s);
			practiceStatistics7a.setPracticeCompanyNum(1L+i);
			practiceStatistics7a.setTaskNum(545L-(s*4));
			practiceStatistics7a.setTaskPassNum(510L-(s*4));
			practiceStatistics7a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics7a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics7 = new PracticeStatistics();
			practiceStatistics7.setId(UUID.randomUUID().toString());
			practiceStatistics7.setOrgId(orgId);
			practiceStatistics7.setTeacherYear(teacherYear - i);
			practiceStatistics7.setCollegeName("信息科学与工程学院");
			practiceStatistics7.setCollegeId(1718L);
			practiceStatistics7.setProfessionId(2380L);
			practiceStatistics7.setProfessionName("电子信息类");
			practiceStatistics7.setClassId(10532L);
			practiceStatistics7.setClassName("电信类2016-3班");
			practiceStatistics7.setPracticeStudentNum(55L-s);
			practiceStatistics7.setPracticeCompanyNum(1L+i);
			practiceStatistics7.setTaskNum(545L-(s*4));
			practiceStatistics7.setTaskPassNum(517L-(s*4));
			practiceStatistics7.setSemester(j);
			practiceStatisticsList.add(practiceStatistics7);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics8 = new PracticeStatistics();
			practiceStatistics8.setId(UUID.randomUUID().toString());
			practiceStatistics8.setOrgId(orgId);
			practiceStatistics8.setTeacherYear(teacherYear - i);
			practiceStatistics8.setCollegeName("管理学院");
			practiceStatistics8.setCollegeId(1750L);
			practiceStatistics8.setProfessionId(2520L);
			practiceStatistics8.setProfessionName("国际经济与贸易（应用）");
			practiceStatistics8.setClassId(10334L);
			practiceStatistics8.setClassName("国贸2016-1班");
			practiceStatistics8.setPracticeStudentNum(49L-s);
			practiceStatistics8.setPracticeCompanyNum(1L+i);
			practiceStatistics8.setTaskNum(415L-(s*4));
			practiceStatistics8.setTaskPassNum(400L-(s*4));
			practiceStatistics8.setSemester(j);
			practiceStatisticsList.add(practiceStatistics8);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics8a = new PracticeStatistics();
			practiceStatistics8a.setId(UUID.randomUUID().toString());
			practiceStatistics8a.setOrgId(orgId);
			practiceStatistics8a.setTeacherYear(teacherYear - i);
			practiceStatistics8a.setCollegeName("管理学院");
			practiceStatistics8a.setCollegeId(1750L);
			practiceStatistics8a.setProfessionId(2520L);
			practiceStatistics8a.setProfessionName("国际经济与贸易（应用）");
			practiceStatistics8a.setClassId(10336L);
			practiceStatistics8a.setClassName("国贸2016-2班");
			practiceStatistics8a.setPracticeStudentNum(43L-s);
			practiceStatistics8a.setPracticeCompanyNum(1L+i);
			practiceStatistics8a.setTaskNum(452L-(s*4));
			practiceStatistics8a.setTaskPassNum(421L-(s*4));
			practiceStatistics8a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics8a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics9a = new PracticeStatistics();
			practiceStatistics9a.setId(UUID.randomUUID().toString());
			practiceStatistics9a.setOrgId(orgId);
			practiceStatistics9a.setTeacherYear(teacherYear - i);
			practiceStatistics9a.setCollegeName("材料科学与工程学院");
			practiceStatistics9a.setCollegeId(1716L);
			practiceStatistics9a.setProfessionId(2404L);
			practiceStatistics9a.setProfessionName("材料类（创新班）");
			practiceStatistics9a.setClassId(10270L);
			practiceStatistics9a.setClassName("材料类2016-6班");
			practiceStatistics9a.setPracticeStudentNum(56L-s);
			practiceStatistics9a.setPracticeCompanyNum(1L+i);
			practiceStatistics9a.setTaskNum(515L-(s*4));
			practiceStatistics9a.setTaskPassNum(510L-(s*4));
			practiceStatistics9a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics9a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics9 = new PracticeStatistics();
			practiceStatistics9.setId(UUID.randomUUID().toString());
			practiceStatistics9.setOrgId(orgId);
			practiceStatistics9.setTeacherYear(teacherYear - i);
			practiceStatistics9.setCollegeName("材料科学与工程学院");
			practiceStatistics9.setCollegeId(1716L);
			practiceStatistics9.setProfessionId(2404L);
			practiceStatistics9.setProfessionName("材料类（创新班）");
			practiceStatistics9.setClassId(10272L);
			practiceStatistics9.setClassName("材料类2016-7班");
			practiceStatistics9.setPracticeStudentNum(55L-s);
			practiceStatistics9.setPracticeCompanyNum(1L+i);
			practiceStatistics9.setTaskNum(555L-(s*4));
			practiceStatistics9.setTaskPassNum(528L-(s*4));
			practiceStatistics9.setSemester(j);
			practiceStatisticsList.add(practiceStatistics9);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics10a = new PracticeStatistics();
			practiceStatistics10a.setId(UUID.randomUUID().toString());
			practiceStatistics10a.setOrgId(orgId);
			practiceStatistics10a.setTeacherYear(teacherYear - i);
			practiceStatistics10a.setCollegeName("理学院");
			practiceStatistics10a.setCollegeId(1748L);
			practiceStatistics10a.setProfessionId(2560L);
			practiceStatistics10a.setProfessionName("应用统计学");
			practiceStatistics10a.setClassId(10406L);
			practiceStatistics10a.setClassName("应用统计2016-2班");
			practiceStatistics10a.setPracticeStudentNum(52L-s);
			practiceStatistics10a.setPracticeCompanyNum(1L+i);
			practiceStatistics10a.setTaskNum(563L-(s*4));
			practiceStatistics10a.setTaskPassNum(548L-(s*4));
			practiceStatistics10a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics10a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics10 = new PracticeStatistics();
			practiceStatistics10.setId(UUID.randomUUID().toString());
			practiceStatistics10.setOrgId(orgId);
			practiceStatistics10.setTeacherYear(teacherYear - i);
			practiceStatistics10.setCollegeName("理学院");
			practiceStatistics10.setCollegeId(1748L);
			practiceStatistics10.setProfessionId(2560L);
			practiceStatistics10.setProfessionName("应用统计学");
			practiceStatistics10.setClassId(10408L);
			practiceStatistics10.setClassName("应用统计2016-3班");
			practiceStatistics10.setPracticeStudentNum(55L-s);
			practiceStatistics10.setPracticeCompanyNum(1L+i);
			practiceStatistics10.setTaskNum(723L-(s*4));
			practiceStatistics10.setTaskPassNum(711L-(s*4));
			practiceStatistics10.setSemester(j);
			practiceStatisticsList.add(practiceStatistics10);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics11a = new PracticeStatistics();
			practiceStatistics11a.setId(UUID.randomUUID().toString());
			practiceStatistics11a.setOrgId(orgId);
			practiceStatistics11a.setTeacherYear(teacherYear - i);
			practiceStatistics11a.setCollegeName("土木与建筑工程学院");
			practiceStatistics11a.setCollegeId(1714L);
			practiceStatistics11a.setProfessionId(2378L);
			practiceStatistics11a.setProfessionName("土木类");
			practiceStatistics11a.setClassId(10448L);
			practiceStatistics11a.setClassName("土木类2016-5班");
			practiceStatistics11a.setPracticeStudentNum(55L-s);
			practiceStatistics11a.setPracticeCompanyNum(1L+i);
			practiceStatistics11a.setTaskNum(619L-(s*4));
			practiceStatistics11a.setTaskPassNum(598L-(s*4));
			practiceStatistics11a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics11a);


			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics11 = new PracticeStatistics();
			practiceStatistics11.setId(UUID.randomUUID().toString());
			practiceStatistics11.setOrgId(orgId);
			practiceStatistics11.setTeacherYear(teacherYear - i);
			practiceStatistics11.setCollegeName("土木与建筑工程学院");
			practiceStatistics11.setCollegeId(1714L);
			practiceStatistics11.setProfessionId(2378L);
			practiceStatistics11.setProfessionName("土木类");
			practiceStatistics11.setClassId(10450L);
			practiceStatistics11.setClassName("土木类2016-6班");
			practiceStatistics11.setPracticeStudentNum(54L-s);
			practiceStatistics11.setPracticeCompanyNum(1L+i);
			practiceStatistics11.setTaskNum(655L-(s*4));
			practiceStatistics11.setTaskPassNum(621L-(s*4));
			practiceStatistics11.setSemester(j);
			practiceStatisticsList.add(practiceStatistics11);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics12 = new PracticeStatistics();
			practiceStatistics12.setId(UUID.randomUUID().toString());
			practiceStatistics12.setOrgId(orgId);
			practiceStatistics12.setTeacherYear(teacherYear - i);
			practiceStatistics12.setCollegeName("环境科学与工程学院");
			practiceStatistics12.setCollegeId(1712L);
			practiceStatistics12.setProfessionId(2406L);
			practiceStatistics12.setProfessionName("给排水科学与工程");
			practiceStatistics12.setClassId(10370L);
			practiceStatistics12.setClassName("给排水2016-1班");
			practiceStatistics12.setPracticeStudentNum(54L-s);
			practiceStatistics12.setPracticeCompanyNum(1L+i);
			practiceStatistics12.setTaskNum(543L-(s*4));
			practiceStatistics12.setTaskPassNum(532L-(s*4));
			practiceStatistics12.setSemester(j);
			practiceStatisticsList.add(practiceStatistics12);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics12a = new PracticeStatistics();
			practiceStatistics12a.setId(UUID.randomUUID().toString());
			practiceStatistics12a.setOrgId(orgId);
			practiceStatistics12a.setTeacherYear(teacherYear - i);
			practiceStatistics12a.setCollegeName("环境科学与工程学院");
			practiceStatistics12a.setCollegeId(1712L);
			practiceStatistics12a.setProfessionId(2406L);
			practiceStatistics12a.setProfessionName("给排水科学与工程");
			practiceStatistics12a.setClassId(10372L);
			practiceStatistics12a.setClassName("给排水2016-2班");
			practiceStatistics12a.setPracticeStudentNum(50L-s);
			practiceStatistics12a.setPracticeCompanyNum(1L+i);
			practiceStatistics12a.setTaskNum(558L-(s*4));
			practiceStatistics12a.setTaskPassNum(526L-(s*4));
			practiceStatistics12a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics12a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics13a = new PracticeStatistics();
			practiceStatistics13a.setId(UUID.randomUUID().toString());
			practiceStatistics13a.setOrgId(orgId);
			practiceStatistics13a.setTeacherYear(teacherYear - i);
			practiceStatistics13a.setCollegeName("地球科学学院");
			practiceStatistics13a.setCollegeId(1710L);
			practiceStatistics13a.setProfessionId(2564L);
			practiceStatistics13a.setProfessionName("地质类");
			practiceStatistics13a.setClassId(10298L);
			practiceStatistics13a.setClassName("地质类2016-5班");
			practiceStatistics13a.setPracticeStudentNum(46L-s);
			practiceStatistics13a.setPracticeCompanyNum(1L+i);
			practiceStatistics13a.setTaskNum(487L-(s*4));
			practiceStatistics13a.setTaskPassNum(463L-(s*4));
			practiceStatistics13a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics13a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics13 = new PracticeStatistics();
			practiceStatistics13.setId(UUID.randomUUID().toString());
			practiceStatistics13.setOrgId(orgId);
			practiceStatistics13.setTeacherYear(teacherYear - i);
			practiceStatistics13.setCollegeName("地球科学学院");
			practiceStatistics13.setCollegeId(1710L);
			practiceStatistics13.setProfessionId(2564L);
			practiceStatistics13.setProfessionName("地质类");
			practiceStatistics13.setClassId(10300L);
			practiceStatistics13.setClassName("地质类2016-6班");
			practiceStatistics13.setPracticeStudentNum(42L-s);
			practiceStatistics13.setPracticeCompanyNum(1L+i);
			practiceStatistics13.setTaskNum(499L-(s*4));
			practiceStatistics13.setTaskPassNum(477L-(s*4));
			practiceStatistics13.setSemester(j);
			practiceStatisticsList.add(practiceStatistics13);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics14a = new PracticeStatistics();
			practiceStatistics14a.setId(UUID.randomUUID().toString());
			practiceStatistics14a.setOrgId(orgId);
			practiceStatistics14a.setTeacherYear(teacherYear - i);
			practiceStatistics14a.setCollegeName("艺术学院");
			practiceStatistics14a.setCollegeId(1760L);
			practiceStatistics14a.setProfessionId(2578L);
			practiceStatistics14a.setProfessionName("设计学类");
			practiceStatistics14a.setClassId(10506L);
			practiceStatistics14a.setClassName("艺术类2016-3班");
			practiceStatistics14a.setPracticeStudentNum(55L-s);
			practiceStatistics14a.setPracticeCompanyNum(1L+i);
			practiceStatistics14a.setTaskNum(545L-(s*4));
			practiceStatistics14a.setTaskPassNum(530L-(s*4));
			practiceStatistics14a.setSemester(j);
			practiceStatisticsList.add(practiceStatistics14a);

			random = new Random();
			s = random.nextInt(max) % (max - min + 1) + min;
			PracticeStatistics practiceStatistics14 = new PracticeStatistics();
			practiceStatistics14.setId(UUID.randomUUID().toString());
			practiceStatistics14.setOrgId(orgId);
			practiceStatistics14.setTeacherYear(teacherYear - i);
			practiceStatistics14.setCollegeName("艺术学院");
			practiceStatistics14.setCollegeId(1760L);
			practiceStatistics14.setProfessionId(2578L);
			practiceStatistics14.setProfessionName("设计学类");
			practiceStatistics14.setClassId(10508L);
			practiceStatistics14.setClassName("艺术类2016-4班");
			practiceStatistics14.setPracticeStudentNum(56L-s);
			practiceStatistics14.setPracticeCompanyNum(1L+i);
			practiceStatistics14.setTaskNum(547L-(s*4));
			practiceStatistics14.setTaskPassNum(513L-(s*4));
			practiceStatistics14.setSemester(j);
			practiceStatisticsList.add(practiceStatistics14);
			}

		}

		practiceStatisticsService.deleteAllByOrgId(orgId);
		practiceStatisticsService.saveList(practiceStatisticsList);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/updatepracticestatistics", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "PUT", value = "修改学情分析学生实践统计数据", response = Void.class, notes = "修改学情分析学生实践统计数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> updatePracticeStatistics(
			@ApiParam(value = "id 记录id") @RequestParam(value = "id", required = false) String id,
			@ApiParam(value = "practiceStudentNum 实践学生人数)") @RequestParam(value = "practiceStudentNum", required = false) Long practiceStudentNum,
			@ApiParam(value = "practiceCompanyNum 实践公司人数)") @RequestParam(value = "practiceCompanyNum", required = false) Long practiceCompanyNum,
			@ApiParam(value = "taskNum 实践任务数)") @RequestParam(value = "taskNum", required = false) Long taskNum,
			@ApiParam(value = "taskPassNum 实践任务通过数)") @RequestParam(value = "taskPassNum", required = false) Long taskPassNum) {
		Map<String, Object> result = new HashMap<String, Object>();

		PracticeStatistics practiceStatistics = practiceStatisticsService
				.findById(id);
		 if(null != practiceStudentNum){
			if(null != practiceStudentNum && practiceStudentNum > 0){
				practiceStatistics.setPracticeStudentNum(practiceStudentNum);
			}
			if(null != practiceCompanyNum && practiceCompanyNum > 0){
				practiceStatistics.setPracticeCompanyNum(practiceCompanyNum);
			}
			if(null != taskNum && taskNum > 0){
				practiceStatistics.setTaskNum(taskNum);
			}
			if(null != taskPassNum && taskPassNum > 0){
				practiceStatistics.setTaskPassNum(taskPassNum);
			}

		}
		 practiceStatisticsService.save(practiceStatistics);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
}