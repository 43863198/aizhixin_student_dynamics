/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.simulate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aizhixin.cloud.dataanalysis.analysis.entity.CetScoreStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolStatisticsService;
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
@Api(value = "测试生成模拟数据操作", description = "生成mongodb模拟数据操作")
public class TestAnalysisDataController {
	private final static Logger log = LoggerFactory
			.getLogger(TestAnalysisDataController.class);

	@Autowired
	private SchoolStatisticsService schoolStatisticsService;

	@RequestMapping(value = "/addschoolstatistics", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "生成学情分析学校人数统计数据", response = Void.class, notes = "生成学情分析学校人数统计数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addSchoolStatistics(
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

		List<SchoolStatistics> schoolStatisticsList = new ArrayList<SchoolStatistics>();
		for (int i = 0; i < num; i++) {

			int max = 20;
			int min = 10;
			Random random = new Random();
			int s = 0;
			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics1 = new SchoolStatistics();
			schoolStatistics1.setId(UUID.randomUUID().toString());
			schoolStatistics1.setOrgId(orgId);
			schoolStatistics1.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics1.setCollegeName("机械与控制工程学院");
			schoolStatistics1.setCollegeId(1722L);
			schoolStatistics1.setNewStudentsCount(365 - s);
			schoolStatistics1.setAlreadyReport(363 - s);
			schoolStatistics1.setAlreadyPay(355 - s);
			schoolStatistics1.setConvenienceChannel(35);
			schoolStatistics1.setTeacherNumber(26);
			schoolStatistics1.setStudentNumber(1375 - s);
			schoolStatistics1.setInstructorNumber(25);
			schoolStatistics1.setReadyGraduation(345 - s);
			schoolStatistics1.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics1);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics2 = new SchoolStatistics();
			schoolStatistics2.setId(UUID.randomUUID().toString());
			schoolStatistics2.setOrgId(orgId);
			schoolStatistics2.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics2.setCollegeName("外国语学院");
			schoolStatistics2.setCollegeId(1758L);
			schoolStatistics2.setNewStudentsCount(204 - s);
			schoolStatistics2.setAlreadyReport(201 - s);
			schoolStatistics2.setAlreadyPay(200 - s);
			schoolStatistics2.setConvenienceChannel(22 - s);
			schoolStatistics2.setTeacherNumber(16);
			schoolStatistics2.setStudentNumber(875 - s);
			schoolStatistics2.setInstructorNumber(10);
			schoolStatistics2.setReadyGraduation(214 - s);
			schoolStatistics2.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics2);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics3 = new SchoolStatistics();
			schoolStatistics3.setId(UUID.randomUUID().toString());
			schoolStatistics3.setOrgId(orgId);
			schoolStatistics3.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics3.setCollegeName("测绘地理信息学院");
			schoolStatistics3.setCollegeId(1724L);
			schoolStatistics3.setNewStudentsCount(206 - s);
			schoolStatistics3.setAlreadyReport(206 - s);
			schoolStatistics3.setAlreadyPay(198 - s);
			schoolStatistics3.setConvenienceChannel(28 - s);
			schoolStatistics3.setTeacherNumber(17);
			schoolStatistics3.setStudentNumber(865 - s);
			schoolStatistics3.setInstructorNumber(10);
			schoolStatistics3.setReadyGraduation(217 - s);
			schoolStatistics3.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics3);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics4 = new SchoolStatistics();
			schoolStatistics4.setId(UUID.randomUUID().toString());
			schoolStatistics4.setOrgId(orgId);
			schoolStatistics4.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics4.setCollegeName("人文社会科学学院");
			schoolStatistics4.setCollegeId(1754L);
			schoolStatistics4.setNewStudentsCount(306 - s);
			schoolStatistics4.setAlreadyReport(296 - s);
			schoolStatistics4.setAlreadyPay(290 - s);
			schoolStatistics4.setConvenienceChannel(30 - s);
			schoolStatistics4.setTeacherNumber(23);
			schoolStatistics4.setStudentNumber(1158 - s);
			schoolStatistics4.setInstructorNumber(15);
			schoolStatistics4.setReadyGraduation(312 - s);
			schoolStatistics4.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics4);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics5 = new SchoolStatistics();
			schoolStatistics5.setId(UUID.randomUUID().toString());
			schoolStatistics5.setOrgId(orgId);
			schoolStatistics5.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics5.setCollegeName("化学与生物工程学院");
			schoolStatistics5.setCollegeId(1720L);
			schoolStatistics5.setNewStudentsCount(345 - s);
			schoolStatistics5.setAlreadyReport(345 - s);
			schoolStatistics5.setAlreadyPay(340 - s);
			schoolStatistics5.setConvenienceChannel(40 - s);
			schoolStatistics5.setTeacherNumber(18);
			schoolStatistics5.setStudentNumber(1275 - s);
			schoolStatistics5.setInstructorNumber(13);
			schoolStatistics5.setReadyGraduation(327 - s);
			schoolStatistics5.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics5);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics6 = new SchoolStatistics();
			schoolStatistics6.setId(UUID.randomUUID().toString());
			schoolStatistics6.setOrgId(orgId);
			schoolStatistics6.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics6.setCollegeName("旅游学院");
			schoolStatistics6.setCollegeId(1752L);
			schoolStatistics6.setNewStudentsCount(265 - s);
			schoolStatistics6.setAlreadyReport(230 - s);
			schoolStatistics6.setAlreadyPay(228 - s);
			schoolStatistics6.setConvenienceChannel(28 - s);
			schoolStatistics6.setTeacherNumber(17);
			schoolStatistics6.setStudentNumber(857 - s);
			schoolStatistics6.setInstructorNumber(10);
			schoolStatistics6.setReadyGraduation(258 - s);
			schoolStatistics6.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics6);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics7 = new SchoolStatistics();
			schoolStatistics7.setId(UUID.randomUUID().toString());
			schoolStatistics7.setOrgId(orgId);
			schoolStatistics7.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics7.setCollegeName("信息科学与工程学院");
			schoolStatistics7.setCollegeId(1718L);
			schoolStatistics7.setNewStudentsCount(545 - s);
			schoolStatistics7.setAlreadyReport(525 - s);
			schoolStatistics7.setAlreadyPay(520 - s);
			schoolStatistics7.setConvenienceChannel(72 - s);
			schoolStatistics7.setTeacherNumber(46);
			schoolStatistics7.setStudentNumber(2278 - s);
			schoolStatistics7.setInstructorNumber(25);
			schoolStatistics7.setReadyGraduation(558 - s);
			schoolStatistics7.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics7);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics8 = new SchoolStatistics();
			schoolStatistics8.setId(UUID.randomUUID().toString());
			schoolStatistics8.setOrgId(orgId);
			schoolStatistics8.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics8.setCollegeName("管理学院");
			schoolStatistics8.setCollegeId(1750L);
			schoolStatistics8.setNewStudentsCount(745 - s);
			schoolStatistics8.setAlreadyReport(744 - s);
			schoolStatistics8.setAlreadyPay(724 - s);
			schoolStatistics8.setConvenienceChannel(90 - s);
			schoolStatistics8.setTeacherNumber(65);
			schoolStatistics8.setStudentNumber(2695 - s);
			schoolStatistics8.setInstructorNumber(32);
			schoolStatistics8.setReadyGraduation(750 - s);
			schoolStatistics8.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics8);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics9 = new SchoolStatistics();
			schoolStatistics9.setId(UUID.randomUUID().toString());
			schoolStatistics9.setOrgId(orgId);
			schoolStatistics9.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics9.setCollegeName("材料科学与工程学院");
			schoolStatistics9.setCollegeId(1716L);
			schoolStatistics9.setNewStudentsCount(357 - s);
			schoolStatistics9.setAlreadyReport(348 - s);
			schoolStatistics9.setAlreadyPay(338 - s);
			schoolStatistics9.setConvenienceChannel(38 - s);
			schoolStatistics9.setTeacherNumber(23);
			schoolStatistics9.setStudentNumber(1358 - s);
			schoolStatistics9.setInstructorNumber(18);
			schoolStatistics9.setReadyGraduation(313 - s);
			schoolStatistics9.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics9);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics10 = new SchoolStatistics();
			schoolStatistics10.setId(UUID.randomUUID().toString());
			schoolStatistics10.setOrgId(orgId);
			schoolStatistics10.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics10.setCollegeName("理学院");
			schoolStatistics10.setCollegeId(1748L);
			schoolStatistics10.setNewStudentsCount(307 - s);
			schoolStatistics10.setAlreadyReport(300 - s);
			schoolStatistics10.setAlreadyPay(293 - s);
			schoolStatistics10.setConvenienceChannel(29 - s);
			schoolStatistics10.setTeacherNumber(19);
			schoolStatistics10.setStudentNumber(1668 - s);
			schoolStatistics10.setInstructorNumber(11);
			schoolStatistics10.setReadyGraduation(324 - s);
			schoolStatistics10.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics10);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics11 = new SchoolStatistics();
			schoolStatistics11.setId(UUID.randomUUID().toString());
			schoolStatistics11.setOrgId(orgId);
			schoolStatistics11.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics11.setCollegeName("土木与建筑工程学院");
			schoolStatistics11.setCollegeId(1714L);
			schoolStatistics11.setNewStudentsCount(467 - s);
			schoolStatistics11.setAlreadyReport(445 - s);
			schoolStatistics11.setAlreadyPay(436 - s);
			schoolStatistics11.setConvenienceChannel(65 - s);
			schoolStatistics11.setTeacherNumber(22);
			schoolStatistics11.setStudentNumber(1668 - s);
			schoolStatistics11.setInstructorNumber(16);
			schoolStatistics11.setReadyGraduation(461 - s);
			schoolStatistics11.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics11);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics12 = new SchoolStatistics();
			schoolStatistics12.setId(UUID.randomUUID().toString());
			schoolStatistics12.setOrgId(orgId);
			schoolStatistics12.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics12.setCollegeName("环境科学与工程学院");
			schoolStatistics12.setCollegeId(1712L);
			schoolStatistics12.setNewStudentsCount(331);
			schoolStatistics12.setAlreadyReport(331 - s);
			schoolStatistics12.setAlreadyPay(318 - s);
			schoolStatistics12.setConvenienceChannel(49 - s);
			schoolStatistics12.setTeacherNumber(18);
			schoolStatistics12.setStudentNumber(978 - s);
			schoolStatistics12.setInstructorNumber(10);
			schoolStatistics12.setReadyGraduation(318 - s);
			schoolStatistics12.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics12);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics13 = new SchoolStatistics();
			schoolStatistics13.setId(UUID.randomUUID().toString());
			schoolStatistics13.setOrgId(orgId);
			schoolStatistics13.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics13.setCollegeName("地球科学学院");
			schoolStatistics13.setCollegeId(1710L);
			schoolStatistics13.setNewStudentsCount(240 - s);
			schoolStatistics13.setAlreadyReport(237 - s);
			schoolStatistics13.setAlreadyPay(225 - s);
			schoolStatistics13.setConvenienceChannel(32 - s);
			schoolStatistics13.setTeacherNumber(13);
			schoolStatistics13.setStudentNumber(856 - s);
			schoolStatistics13.setInstructorNumber(20);
			schoolStatistics13.setReadyGraduation(221 - s);
			schoolStatistics13.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics13);

			if (i > 0) {
				s = random.nextInt(max) % (max - min + 1) + min;
			}
			SchoolStatistics schoolStatistics14 = new SchoolStatistics();
			schoolStatistics14.setId(UUID.randomUUID().toString());
			schoolStatistics14.setOrgId(orgId);
			schoolStatistics14.setTeacherYear(String.valueOf(teacherYear - i));
			schoolStatistics14.setCollegeName("艺术学院");
			schoolStatistics14.setCollegeId(1760L);
			schoolStatistics14.setNewStudentsCount(391 - s);
			schoolStatistics14.setAlreadyReport(378 - s);
			schoolStatistics14.setAlreadyPay(367 - s);
			schoolStatistics14.setConvenienceChannel(51 - s);
			schoolStatistics14.setTeacherNumber(20);
			schoolStatistics14.setStudentNumber(1388 - s);
			schoolStatistics14.setInstructorNumber(14);
			schoolStatistics14.setReadyGraduation(375 - s);
			schoolStatistics14.setStatisticalTime(new Date());
			schoolStatisticsList.add(schoolStatistics14);

		}

	    schoolStatisticsService.saveList(schoolStatisticsList);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/updatecetstatistics", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "PUT", value = "修改学情分析学校人数统计数据", response = Void.class, notes = "修改学情分析学校人数统计数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> updateCetStatistics(
			@ApiParam(value = "id 记录id") @RequestParam(value = "id", required = false) String id,
			@ApiParam(value = "newStudentsCount 新生人数)") @RequestParam(value = "newStudentsCount", required = false) Integer newStudentsCount,
			@ApiParam(value = "alreadyReport 已报到人数)") @RequestParam(value = "alreadyReport", required = false) Integer alreadyReport,
			@ApiParam(value = "alreadyPay 已缴费人数)") @RequestParam(value = "alreadyPay", required = false) Integer alreadyPay,
			@ApiParam(value = "convenienceChannel 绿色通道人数)") @RequestParam(value = "convenienceChannel", required = false) Integer convenienceChannel,
			@ApiParam(value = "teacherNumber 院系老师总人数)") @RequestParam(value = "teacherNumber", required = false) Integer teacherNumber,
			@ApiParam(value = "studentNumber 院系学生总人数)") @RequestParam(value = "studentNumber", required = false) Integer studentNumber,
			@ApiParam(value = "instructorNumber 院系辅导员总人数)") @RequestParam(value = "instructorNumber", required = false) Integer instructorNumber,
			@ApiParam(value = "readyGraduation 即将毕业人数)") @RequestParam(value = "readyGraduation", required = false) Integer readyGraduation) {
		Map<String, Object> result = new HashMap<String, Object>();

		SchoolStatistics schoolStatistics = schoolStatisticsService
				.findById(id);
		if(null != schoolStatistics){
			if(null != newStudentsCount && newStudentsCount > 0){
				schoolStatistics.setNewStudentsCount(newStudentsCount);
			}
			if(null != alreadyReport && alreadyReport > 0){
				schoolStatistics.setAlreadyReport(alreadyReport);
			}
			if(null != alreadyPay && alreadyPay > 0){
				schoolStatistics.setAlreadyPay(alreadyPay);
			}
			if(null != convenienceChannel && convenienceChannel > 0){
				schoolStatistics.setConvenienceChannel(convenienceChannel);
			}
			if(null != teacherNumber && teacherNumber > 0){
				schoolStatistics.setTeacherNumber(teacherNumber);
			}
			if(null != studentNumber && studentNumber > 0){
				schoolStatistics.setStudentNumber(studentNumber);
			}
			if(null != instructorNumber && instructorNumber > 0){
				schoolStatistics.setInstructorNumber(instructorNumber);
			}
			if(null != readyGraduation && readyGraduation > 0){
				schoolStatistics.setReadyGraduation(readyGraduation);
			}
		}
		schoolStatisticsService.save(schoolStatistics);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	
//	@RequestMapping(value = "/addcetstatistics", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiOperation(httpMethod = "POST", value = "生成学情分析英语等级考试统计数据", response = Void.class, notes = "生成学情分析英语等级考试统计数据<br><br><b>@author zhengning</b>")
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
//			int max = 20;
//			int min = 10;
//			Random random = new Random();
//			int s = 0;
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			CetScoreStatistics cetScoreStatistics1 = new CetScoreStatistics();
//			cetScoreStatistics1.setId(UUID.randomUUID().toString());
//			cetScoreStatistics1.setOrgId(orgId);
//			cetScoreStatistics1.setTeacherYear(String.valueOf(teacherYear - i));
//			cetScoreStatistics1.setCollegeName("机械与控制工程学院");
//			cetScoreStatistics1.setCollegeId(1722L);
//			cetScoreStatistics1.setCetForeJoinNum(cetForeJoinNum);
//			cetScoreStatistics1.setCetForePassNum(cetForePassNum);
//			cetScoreStatistics1.setCetSixJoinNum(cetSixJoinNum);
//			cetScoreStatistics1.setCetSixPassNum(cetSixPassNum);
//			cetScoreStatistics1.setGrade();
//			cetScoreStatistics1.setTeacherYear(String.valueOf(teacherYear));
//			cetScoreStatistics1.setSemester(j);
//			cetStatisticsList.add(cetScoreStatistics1);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics2 = new SchoolStatistics();
//			schoolStatistics2.setId(UUID.randomUUID().toString());
//			schoolStatistics2.setOrgId(orgId);
//			schoolStatistics2.setTeacherYear(String.valueOf(teacherYear - i));
//			schoolStatistics2.setCollegeName("外国语学院");
//			schoolStatistics2.setCollegeId(1758L);
//			schoolStatistics2.setNewStudentsCount(204 - s);
//			schoolStatistics2.setAlreadyReport(201 - s);
//			schoolStatistics2.setAlreadyPay(200 - s);
//			schoolStatistics2.setConvenienceChannel(22 - s);
//			schoolStatistics2.setTeacherNumber(16);
//			schoolStatistics2.setStudentNumber(875 - s);
//			schoolStatistics2.setInstructorNumber(10);
//			schoolStatistics2.setReadyGraduation(214 - s);
//			schoolStatistics2.setStatisticalTime(new Date());
//			cetStatisticsList.add(schoolStatistics2);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics3 = new SchoolStatistics();
//			schoolStatistics3.setId(UUID.randomUUID().toString());
//			schoolStatistics3.setOrgId(orgId);
//			schoolStatistics3.setTeacherYear(String.valueOf(teacherYear - i));
//			schoolStatistics3.setCollegeName("测绘地理信息学院");
//			schoolStatistics3.setCollegeId(1724L);
//			schoolStatistics3.setNewStudentsCount(206 - s);
//			schoolStatistics3.setAlreadyReport(206 - s);
//			schoolStatistics3.setAlreadyPay(198 - s);
//			schoolStatistics3.setConvenienceChannel(28 - s);
//			schoolStatistics3.setTeacherNumber(17);
//			schoolStatistics3.setStudentNumber(865 - s);
//			schoolStatistics3.setInstructorNumber(10);
//			schoolStatistics3.setReadyGraduation(217 - s);
//			schoolStatistics3.setStatisticalTime(new Date());
//			cetStatisticsList.add(schoolStatistics3);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics4 = new SchoolStatistics();
//			schoolStatistics4.setId(UUID.randomUUID().toString());
//			schoolStatistics4.setOrgId(orgId);
//			schoolStatistics4.setTeacherYear(String.valueOf(teacherYear - i));
//			schoolStatistics4.setCollegeName("人文社会科学学院");
//			schoolStatistics4.setCollegeId(1754L);
//			schoolStatistics4.setNewStudentsCount(306 - s);
//			schoolStatistics4.setAlreadyReport(296 - s);
//			schoolStatistics4.setAlreadyPay(290 - s);
//			schoolStatistics4.setConvenienceChannel(30 - s);
//			schoolStatistics4.setTeacherNumber(23);
//			schoolStatistics4.setStudentNumber(1158 - s);
//			schoolStatistics4.setInstructorNumber(15);
//			schoolStatistics4.setReadyGraduation(312 - s);
//			schoolStatistics4.setStatisticalTime(new Date());
//			cetStatisticsList.add(schoolStatistics4);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics5 = new SchoolStatistics();
//			schoolStatistics5.setId(UUID.randomUUID().toString());
//			schoolStatistics5.setOrgId(orgId);
//			schoolStatistics5.setTeacherYear(String.valueOf(teacherYear - i));
//			schoolStatistics5.setCollegeName("化学与生物工程学院");
//			schoolStatistics5.setCollegeId(1720L);
//			schoolStatistics5.setNewStudentsCount(345 - s);
//			schoolStatistics5.setAlreadyReport(345 - s);
//			schoolStatistics5.setAlreadyPay(340 - s);
//			schoolStatistics5.setConvenienceChannel(40 - s);
//			schoolStatistics5.setTeacherNumber(18);
//			schoolStatistics5.setStudentNumber(1275 - s);
//			schoolStatistics5.setInstructorNumber(13);
//			schoolStatistics5.setReadyGraduation(327 - s);
//			schoolStatistics5.setStatisticalTime(new Date());
//			cetStatisticsList.add(schoolStatistics5);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics6 = new SchoolStatistics();
//			schoolStatistics6.setId(UUID.randomUUID().toString());
//			schoolStatistics6.setOrgId(orgId);
//			schoolStatistics6.setTeacherYear(String.valueOf(teacherYear - i));
//			schoolStatistics6.setCollegeName("旅游学院");
//			schoolStatistics6.setCollegeId(1752L);
//			schoolStatistics6.setNewStudentsCount(265 - s);
//			schoolStatistics6.setAlreadyReport(230 - s);
//			schoolStatistics6.setAlreadyPay(228 - s);
//			schoolStatistics6.setConvenienceChannel(28 - s);
//			schoolStatistics6.setTeacherNumber(17);
//			schoolStatistics6.setStudentNumber(857 - s);
//			schoolStatistics6.setInstructorNumber(10);
//			schoolStatistics6.setReadyGraduation(258 - s);
//			schoolStatistics6.setStatisticalTime(new Date());
//			cetStatisticsList.add(schoolStatistics6);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics7 = new SchoolStatistics();
//			schoolStatistics7.setId(UUID.randomUUID().toString());
//			schoolStatistics7.setOrgId(orgId);
//			schoolStatistics7.setTeacherYear(String.valueOf(teacherYear - i));
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
//			cetStatisticsList.add(schoolStatistics7);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics8 = new SchoolStatistics();
//			schoolStatistics8.setId(UUID.randomUUID().toString());
//			schoolStatistics8.setOrgId(orgId);
//			schoolStatistics8.setTeacherYear(String.valueOf(teacherYear - i));
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
//			cetStatisticsList.add(schoolStatistics8);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics9 = new SchoolStatistics();
//			schoolStatistics9.setId(UUID.randomUUID().toString());
//			schoolStatistics9.setOrgId(orgId);
//			schoolStatistics9.setTeacherYear(String.valueOf(teacherYear - i));
//			schoolStatistics9.setCollegeName("材料科学与工程学院");
//			schoolStatistics9.setCollegeId(1716L);
//			schoolStatistics9.setNewStudentsCount(357 - s);
//			schoolStatistics9.setAlreadyReport(348 - s);
//			schoolStatistics9.setAlreadyPay(338 - s);
//			schoolStatistics9.setConvenienceChannel(38 - s);
//			schoolStatistics9.setTeacherNumber(23);
//			schoolStatistics9.setStudentNumber(1358 - s);
//			schoolStatistics9.setInstructorNumber(18);
//			schoolStatistics9.setReadyGraduation(313 - s);
//			schoolStatistics9.setStatisticalTime(new Date());
//			cetStatisticsList.add(schoolStatistics9);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics10 = new SchoolStatistics();
//			schoolStatistics10.setId(UUID.randomUUID().toString());
//			schoolStatistics10.setOrgId(orgId);
//			schoolStatistics10.setTeacherYear(String.valueOf(teacherYear - i));
//			schoolStatistics10.setCollegeName("理学院");
//			schoolStatistics10.setCollegeId(1748L);
//			schoolStatistics10.setNewStudentsCount(307 - s);
//			schoolStatistics10.setAlreadyReport(300 - s);
//			schoolStatistics10.setAlreadyPay(293 - s);
//			schoolStatistics10.setConvenienceChannel(29 - s);
//			schoolStatistics10.setTeacherNumber(19);
//			schoolStatistics10.setStudentNumber(1668 - s);
//			schoolStatistics10.setInstructorNumber(11);
//			schoolStatistics10.setReadyGraduation(324 - s);
//			schoolStatistics10.setStatisticalTime(new Date());
//			cetStatisticsList.add(schoolStatistics10);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics11 = new SchoolStatistics();
//			schoolStatistics11.setId(UUID.randomUUID().toString());
//			schoolStatistics11.setOrgId(orgId);
//			schoolStatistics11.setTeacherYear(String.valueOf(teacherYear - i));
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
//			cetStatisticsList.add(schoolStatistics11);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics12 = new SchoolStatistics();
//			schoolStatistics12.setId(UUID.randomUUID().toString());
//			schoolStatistics12.setOrgId(orgId);
//			schoolStatistics12.setTeacherYear(String.valueOf(teacherYear - i));
//			schoolStatistics12.setCollegeName("环境科学与工程学院");
//			schoolStatistics12.setCollegeId(1712L);
//			schoolStatistics12.setNewStudentsCount(331);
//			schoolStatistics12.setAlreadyReport(331 - s);
//			schoolStatistics12.setAlreadyPay(318 - s);
//			schoolStatistics12.setConvenienceChannel(49 - s);
//			schoolStatistics12.setTeacherNumber(18);
//			schoolStatistics12.setStudentNumber(978 - s);
//			schoolStatistics12.setInstructorNumber(10);
//			schoolStatistics12.setReadyGraduation(318 - s);
//			schoolStatistics12.setStatisticalTime(new Date());
//			cetStatisticsList.add(schoolStatistics12);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics13 = new SchoolStatistics();
//			schoolStatistics13.setId(UUID.randomUUID().toString());
//			schoolStatistics13.setOrgId(orgId);
//			schoolStatistics13.setTeacherYear(String.valueOf(teacherYear - i));
//			schoolStatistics13.setCollegeName("地球科学学院");
//			schoolStatistics13.setCollegeId(1710L);
//			schoolStatistics13.setNewStudentsCount(240 - s);
//			schoolStatistics13.setAlreadyReport(237 - s);
//			schoolStatistics13.setAlreadyPay(225 - s);
//			schoolStatistics13.setConvenienceChannel(32 - s);
//			schoolStatistics13.setTeacherNumber(13);
//			schoolStatistics13.setStudentNumber(856 - s);
//			schoolStatistics13.setInstructorNumber(20);
//			schoolStatistics13.setReadyGraduation(221 - s);
//			schoolStatistics13.setStatisticalTime(new Date());
//			cetStatisticsList.add(schoolStatistics13);
//
//			if (i > 0) {
//				s = random.nextInt(max) % (max - min + 1) + min;
//			}
//			SchoolStatistics schoolStatistics14 = new SchoolStatistics();
//			schoolStatistics14.setId(UUID.randomUUID().toString());
//			schoolStatistics14.setOrgId(orgId);
//			schoolStatistics14.setTeacherYear(String.valueOf(teacherYear - i));
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
//			cetStatisticsList.add(schoolStatistics14);
//			}
//
//		}
//
//	    schoolStatisticsService.saveList(cetStatisticsList);
//		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
//	}
//
//	@RequestMapping(value = "/updateschoolstatistics", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
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
}