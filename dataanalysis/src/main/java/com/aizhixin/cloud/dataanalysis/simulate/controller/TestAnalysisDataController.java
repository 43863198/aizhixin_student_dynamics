/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.simulate.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@Api(value = "测试生成模拟数据和调用定时任务操作", description = "生成mongodb模拟数据操作")
public class TestAnalysisDataController {
	private final static Logger log = LoggerFactory
			.getLogger(TestAnalysisDataController.class);

	@Autowired
	private SchoolStatisticsService schoolStatisticsService;


	@RequestMapping(value = "/schoolstatisticsdata", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "生成MongoDB学生报到数据", response = Void.class, notes = "生成MongoDB学生报到数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> addRegisterData(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
			@ApiParam(value = "teacherYear 学年)") @RequestParam(value = "teacherYear", required = true) int teacherYear,
			@ApiParam(value = "num teacherYear几年内)") @RequestParam(value = "num", required = false) int num) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (null != orgId && orgId.longValue() > 0L) {
		} else {
			orgId = 1L;
		}
		if(num == 0){
			num = 1;
		}
		
		List<SchoolStatistics> schoolStatisticsList = new ArrayList<SchoolStatistics>();
	    for(int i=0;i<num;i++){
	    	SchoolStatistics schoolStatistics1 = new SchoolStatistics();
	    	schoolStatistics1.setId(UUID.randomUUID().toString());
	    	schoolStatistics1.setOrgId(orgId);
	    	schoolStatistics1.setTeacherYear(teacherYear-i);
	    	schoolStatistics1.setCollegeName("机械与控制工程学院");
	    	schoolStatistics1.setCollegeId(1722L);
	    	schoolStatistics1.setNewStudentsCount(365);
	    	schoolStatistics1.setAlreadyReport(363);
	    	schoolStatistics1.setAlreadyPay(355);
	    	schoolStatistics1.setConvenienceChannel(35);
	    	schoolStatistics1.setTeacherNumber(26);
	    	schoolStatistics1.setStudentNumber(1375);
	    	schoolStatistics1.setInstructorNumber(25);
	    	schoolStatistics1.setReadyGraduation(345);
	    	schoolStatistics1.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics1);
	    	
	    	SchoolStatistics schoolStatistics2 = new SchoolStatistics();
	    	schoolStatistics2.setId(UUID.randomUUID().toString());
	    	schoolStatistics2.setOrgId(orgId);
	    	schoolStatistics2.setTeacherYear(teacherYear-i);
	    	schoolStatistics2.setCollegeName("外国语学院");
	    	schoolStatistics2.setCollegeId(1758L);
	    	schoolStatistics2.setNewStudentsCount(204);
	    	schoolStatistics2.setAlreadyReport(201);
	    	schoolStatistics2.setAlreadyPay(200);
	    	schoolStatistics2.setConvenienceChannel(20);
	    	schoolStatistics2.setTeacherNumber(16);
	    	schoolStatistics2.setStudentNumber(875);
	    	schoolStatistics2.setInstructorNumber(10);
	    	schoolStatistics2.setReadyGraduation(214);
	    	schoolStatistics2.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics2);
	    	
	    	SchoolStatistics schoolStatistics3 = new SchoolStatistics();
	    	schoolStatistics3.setId(UUID.randomUUID().toString());
	    	schoolStatistics3.setOrgId(orgId);
	    	schoolStatistics3.setTeacherYear(teacherYear-i);
	    	schoolStatistics3.setCollegeName("测绘地理信息学院");
	    	schoolStatistics3.setCollegeId(1724L);
	    	schoolStatistics3.setNewStudentsCount(365);
	    	schoolStatistics3.setAlreadyReport(363);
	    	schoolStatistics3.setAlreadyPay(355);
	    	schoolStatistics3.setConvenienceChannel(35);
	    	schoolStatistics3.setTeacherNumber(46);
	    	schoolStatistics3.setStudentNumber(1375);
	    	schoolStatistics3.setInstructorNumber(25);
	    	schoolStatistics3.setReadyGraduation(345);
	    	schoolStatistics3.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics3);
	    	
	    	SchoolStatistics schoolStatistics4 = new SchoolStatistics();
	    	schoolStatistics4.setId(UUID.randomUUID().toString());
	    	schoolStatistics4.setOrgId(orgId);
	    	schoolStatistics4.setTeacherYear(teacherYear-i);
	    	schoolStatistics4.setCollegeName("人文社会科学学院");
	    	schoolStatistics4.setCollegeId(1754L);
	    	schoolStatistics4.setNewStudentsCount(365);
	    	schoolStatistics4.setAlreadyReport(363);
	    	schoolStatistics4.setAlreadyPay(355);
	    	schoolStatistics4.setConvenienceChannel(35);
	    	schoolStatistics4.setTeacherNumber(46);
	    	schoolStatistics4.setStudentNumber(1375);
	    	schoolStatistics4.setInstructorNumber(25);
	    	schoolStatistics4.setReadyGraduation(345);
	    	schoolStatistics4.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics4);
	    	
	    	SchoolStatistics schoolStatistics5 = new SchoolStatistics();
	    	schoolStatistics5.setId(UUID.randomUUID().toString());
	    	schoolStatistics5.setOrgId(orgId);
	    	schoolStatistics5.setTeacherYear(teacherYear-i);
	    	schoolStatistics5.setCollegeName("化学与生物工程学院");
	    	schoolStatistics5.setCollegeId(1720L);
	    	schoolStatistics5.setNewStudentsCount(365);
	    	schoolStatistics5.setAlreadyReport(363);
	    	schoolStatistics5.setAlreadyPay(355);
	    	schoolStatistics5.setConvenienceChannel(35);
	    	schoolStatistics5.setTeacherNumber(46);
	    	schoolStatistics5.setStudentNumber(1375);
	    	schoolStatistics5.setInstructorNumber(25);
	    	schoolStatistics5.setReadyGraduation(345);
	    	schoolStatistics5.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics5);
	    	
	    	SchoolStatistics schoolStatistics6 = new SchoolStatistics();
	    	schoolStatistics6.setId(UUID.randomUUID().toString());
	    	schoolStatistics6.setOrgId(orgId);
	    	schoolStatistics6.setTeacherYear(teacherYear-i);
	    	schoolStatistics6.setCollegeName("旅游学院");
	    	schoolStatistics6.setCollegeId(1752L);
	    	schoolStatistics6.setNewStudentsCount(365);
	    	schoolStatistics6.setAlreadyReport(363);
	    	schoolStatistics6.setAlreadyPay(355);
	    	schoolStatistics6.setConvenienceChannel(35);
	    	schoolStatistics6.setTeacherNumber(46);
	    	schoolStatistics6.setStudentNumber(1375);
	    	schoolStatistics6.setInstructorNumber(25);
	    	schoolStatistics6.setReadyGraduation(345);
	    	schoolStatistics6.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics6);
	    	
	    	SchoolStatistics schoolStatistics7 = new SchoolStatistics();
	    	schoolStatistics7.setId(UUID.randomUUID().toString());
	    	schoolStatistics7.setOrgId(orgId);
	    	schoolStatistics7.setTeacherYear(teacherYear-i);
	    	schoolStatistics7.setCollegeName("信息科学与工程学院");
	    	schoolStatistics7.setCollegeId(1718L);
	    	schoolStatistics7.setNewStudentsCount(365);
	    	schoolStatistics7.setAlreadyReport(363);
	    	schoolStatistics7.setAlreadyPay(355);
	    	schoolStatistics7.setConvenienceChannel(35);
	    	schoolStatistics7.setTeacherNumber(46);
	    	schoolStatistics7.setStudentNumber(1375);
	    	schoolStatistics7.setInstructorNumber(25);
	    	schoolStatistics7.setReadyGraduation(345);
	    	schoolStatistics7.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics7);
	    	
	    	SchoolStatistics schoolStatistics8 = new SchoolStatistics();
	    	schoolStatistics8.setId(UUID.randomUUID().toString());
	    	schoolStatistics8.setOrgId(orgId);
	    	schoolStatistics8.setTeacherYear(teacherYear-i);
	    	schoolStatistics8.setCollegeName("管理学院");
	    	schoolStatistics8.setCollegeId(1750L);
	    	schoolStatistics8.setNewStudentsCount(365);
	    	schoolStatistics8.setAlreadyReport(363);
	    	schoolStatistics8.setAlreadyPay(355);
	    	schoolStatistics8.setConvenienceChannel(35);
	    	schoolStatistics8.setTeacherNumber(46);
	    	schoolStatistics8.setStudentNumber(1375);
	    	schoolStatistics8.setInstructorNumber(25);
	    	schoolStatistics8.setReadyGraduation(345);
	    	schoolStatistics8.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics8);
	    	
	    	SchoolStatistics schoolStatistics9 = new SchoolStatistics();
	    	schoolStatistics9.setId(UUID.randomUUID().toString());
	    	schoolStatistics9.setOrgId(orgId);
	    	schoolStatistics9.setTeacherYear(teacherYear-i);
	    	schoolStatistics9.setCollegeName("材料科学与工程学院");
	    	schoolStatistics9.setCollegeId(1716L);
	    	schoolStatistics9.setNewStudentsCount(365);
	    	schoolStatistics9.setAlreadyReport(363);
	    	schoolStatistics9.setAlreadyPay(355);
	    	schoolStatistics9.setConvenienceChannel(35);
	    	schoolStatistics9.setTeacherNumber(46);
	    	schoolStatistics9.setStudentNumber(1375);
	    	schoolStatistics9.setInstructorNumber(25);
	    	schoolStatistics9.setReadyGraduation(345);
	    	schoolStatistics9.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics9);
	    	
	    	SchoolStatistics schoolStatistics10 = new SchoolStatistics();
	    	schoolStatistics10.setId(UUID.randomUUID().toString());
	    	schoolStatistics10.setOrgId(orgId);
	    	schoolStatistics10.setTeacherYear(teacherYear-i);
	    	schoolStatistics10.setCollegeName("理学院");
	    	schoolStatistics10.setCollegeId(1748L);
	    	schoolStatistics10.setNewStudentsCount(365);
	    	schoolStatistics10.setAlreadyReport(363);
	    	schoolStatistics10.setAlreadyPay(355);
	    	schoolStatistics10.setConvenienceChannel(35);
	    	schoolStatistics10.setTeacherNumber(46);
	    	schoolStatistics10.setStudentNumber(1375);
	    	schoolStatistics10.setInstructorNumber(25);
	    	schoolStatistics10.setReadyGraduation(345);
	    	schoolStatistics10.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics10);
	    	
	    	SchoolStatistics schoolStatistics11 = new SchoolStatistics();
	    	schoolStatistics11.setId(UUID.randomUUID().toString());
	    	schoolStatistics11.setOrgId(orgId);
	    	schoolStatistics11.setTeacherYear(teacherYear-i);
	    	schoolStatistics11.setCollegeName("土木与建筑工程学院");
	    	schoolStatistics11.setCollegeId(1714L);
	    	schoolStatistics11.setNewStudentsCount(365);
	    	schoolStatistics11.setAlreadyReport(363);
	    	schoolStatistics11.setAlreadyPay(355);
	    	schoolStatistics11.setConvenienceChannel(35);
	    	schoolStatistics11.setTeacherNumber(46);
	    	schoolStatistics11.setStudentNumber(1375);
	    	schoolStatistics11.setInstructorNumber(25);
	    	schoolStatistics11.setReadyGraduation(345);
	    	schoolStatistics11.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics11);
	    	
	    	SchoolStatistics schoolStatistics12 = new SchoolStatistics();
	    	schoolStatistics12.setId(UUID.randomUUID().toString());
	    	schoolStatistics12.setOrgId(orgId);
	    	schoolStatistics12.setTeacherYear(teacherYear-i);
	    	schoolStatistics12.setCollegeName("环境科学与工程学院");
	    	schoolStatistics12.setCollegeId(1712L);
	    	schoolStatistics12.setNewStudentsCount(365);
	    	schoolStatistics12.setAlreadyReport(363);
	    	schoolStatistics12.setAlreadyPay(355);
	    	schoolStatistics12.setConvenienceChannel(35);
	    	schoolStatistics12.setTeacherNumber(46);
	    	schoolStatistics12.setStudentNumber(1375);
	    	schoolStatistics12.setInstructorNumber(25);
	    	schoolStatistics12.setReadyGraduation(345);
	    	schoolStatistics12.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics12);
	    	
	    	SchoolStatistics schoolStatistics13 = new SchoolStatistics();
	    	schoolStatistics13.setId(UUID.randomUUID().toString());
	    	schoolStatistics13.setOrgId(orgId);
	    	schoolStatistics13.setTeacherYear(teacherYear-i);
	    	schoolStatistics13.setCollegeName("地球科学学院");
	    	schoolStatistics13.setCollegeId(1710L);
	    	schoolStatistics13.setNewStudentsCount(365);
	    	schoolStatistics13.setAlreadyReport(363);
	    	schoolStatistics13.setAlreadyPay(355);
	    	schoolStatistics13.setConvenienceChannel(35);
	    	schoolStatistics13.setTeacherNumber(46);
	    	schoolStatistics13.setStudentNumber(1375);
	    	schoolStatistics13.setInstructorNumber(25);
	    	schoolStatistics13.setReadyGraduation(345);
	    	schoolStatistics13.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics13);
	    	
	    	SchoolStatistics schoolStatistics14 = new SchoolStatistics();
	    	schoolStatistics14.setId(UUID.randomUUID().toString());
	    	schoolStatistics14.setOrgId(orgId);
	    	schoolStatistics14.setTeacherYear(teacherYear-i);
	    	schoolStatistics14.setCollegeName("艺术学院");
	    	schoolStatistics14.setCollegeId(1760L);
	    	schoolStatistics14.setNewStudentsCount(365);
	    	schoolStatistics14.setAlreadyReport(363);
	    	schoolStatistics14.setAlreadyPay(355);
	    	schoolStatistics14.setConvenienceChannel(35);
	    	schoolStatistics14.setTeacherNumber(46);
	    	schoolStatistics14.setStudentNumber(1375);
	    	schoolStatistics14.setInstructorNumber(25);
	    	schoolStatistics14.setReadyGraduation(345);
	    	schoolStatistics14.setStatisticalTime(new Date());
	    	schoolStatisticsList.add(schoolStatistics14);
	    	
	    }
	
	    schoolStatisticsService.saveList(schoolStatisticsList);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	
}