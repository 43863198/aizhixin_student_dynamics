/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.simulate.controller;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aizhixin.cloud.dataanalysis.monitor.dto.CollegeGpaDTO;
import com.aizhixin.cloud.dataanalysis.monitor.service.AbnormalAttendanceStatisticsService;
import com.aizhixin.cloud.dataanalysis.monitor.service.AbnormalTeachingStatisticsService;
import com.aizhixin.cloud.dataanalysis.monitor.service.MonitorService;
import com.aizhixin.cloud.dataanalysis.monitor.service.TeachingScheduleStatisticsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.monitor.domain.AbnormalAttendanceDomain;
import com.aizhixin.cloud.dataanalysis.monitor.domain.AbnormalTeachingDomain;
import com.aizhixin.cloud.dataanalysis.monitor.domain.TeachingScheduleDomain;
import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalAttendanceStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.entity.AbnormalTeachingStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.entity.TeachingScheduleStatistics;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import java.util.Map;

/**
 * @author zhengning
 *
 */
@RestController
@RequestMapping("/v1/test")
@Api(value = "监控大屏数据展示", description = "监控大屏数据展示")
public class TestMonitorDataController {
	private final static Logger log = LoggerFactory
			.getLogger(TestMonitorDataController.class);
	@Autowired
	private AbnormalAttendanceStatisticsService abnormalAttendanceStatisticsService;
	@Autowired
	private AbnormalTeachingStatisticsService abnormalTeachingStatisticsService;
	@Autowired
	private TeachingScheduleStatisticsService teachingScheduleStatisticsService;

	@RequestMapping(value = "/todaycoursenumadd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "今日排课数量", response = Void.class, notes = "今日排课数量<br><br><b>@author zhengning</b>")
	public ResponseEntity<TeachingScheduleStatistics> todayCourseNumAdd(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId) {
		
		
		TeachingScheduleStatistics teachingScheduleStatistics = null;
		teachingScheduleStatistics = teachingScheduleStatisticsService.findByOrgIdAndStatisticalTime(orgId, DateUtil.getCurrentTime(DateUtil.FORMAT_SHORT));
		if(null == teachingScheduleStatistics){
			teachingScheduleStatistics = new TeachingScheduleStatistics();
		}
		teachingScheduleStatistics.setOrgId(orgId);
		int max = 20;
		int min = 10;
		Random random = new Random();
		int s =  random.nextInt(max) % (max - min + 1) + min;
		teachingScheduleStatistics.setCourseNum1(45-s);
		teachingScheduleStatistics.setCourseNum3(40-s);
		teachingScheduleStatistics.setCourseNum5(57-s);
		teachingScheduleStatistics.setCourseNum7(35-s);
		teachingScheduleStatistics.setCourseNum9(32-s);
		teachingScheduleStatistics.setCourseNum11(29-s);
		teachingScheduleStatistics.setStatisticalTime(DateUtil.getCurrentTime(DateUtil.FORMAT_SHORT));
		teachingScheduleStatisticsService.save(teachingScheduleStatistics);
		return new ResponseEntity<TeachingScheduleStatistics>(teachingScheduleStatistics, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/todaycoursenumupdate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "PUT", value = "今日排课数量", response = Void.class, notes = "今日排课数量<br><br><b>@author zhengning</b>")
	public ResponseEntity<TeachingScheduleStatistics> todayCourseNumUpdate(
			@ApiParam(value = "id 记录id") @RequestParam(value = "id", required = true) String id,
			@ApiParam(value = "courseNum1 机构id") @RequestParam(value = "courseNum1", required = false) Integer courseNum1,
			@ApiParam(value = "courseNum3 机构id") @RequestParam(value = "courseNum3", required = false) Integer courseNum3,
			@ApiParam(value = "courseNum5 机构id") @RequestParam(value = "courseNum5", required = false) Integer courseNum5,
			@ApiParam(value = "courseNum7 机构id") @RequestParam(value = "courseNum7", required = false) Integer courseNum7,
			@ApiParam(value = "courseNum9 机构id") @RequestParam(value = "courseNum9", required = false) Integer courseNum9,
			@ApiParam(value = "courseNum11 机构id") @RequestParam(value = "courseNum11", required = false) Integer courseNum11) {
		TeachingScheduleStatistics teachingScheduleStatistics = teachingScheduleStatisticsService.findById(id);
		if(null != teachingScheduleStatistics){
			if(null != courseNum1 && courseNum1 > 0 ){
				teachingScheduleStatistics.setCourseNum1(courseNum1);
			}
			if(null != courseNum3 && courseNum3 > 0 ){
				teachingScheduleStatistics.setCourseNum3(courseNum3);
			}
			if(null != courseNum5 && courseNum5 > 0 ){
				teachingScheduleStatistics.setCourseNum5(courseNum5);
			}
			if(null != courseNum7 && courseNum7 > 0 ){
				teachingScheduleStatistics.setCourseNum7(courseNum7);
			}
			if(null != courseNum9 && courseNum9 > 0 ){
				teachingScheduleStatistics.setCourseNum9(courseNum9);
			}
			if(null != courseNum11 && courseNum11 > 0 ){
				teachingScheduleStatistics.setCourseNum11(courseNum11);
			}
		}
		return new ResponseEntity<TeachingScheduleStatistics>(teachingScheduleStatistics, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/abnormalattendanceadd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "学生异常出勤统计", response = Void.class, notes = "学生异常出勤统计<br><br><b>@author zhengning</b>")
	public ResponseEntity<AbnormalAttendanceStatistics> abnormalAttendanceAdd(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId) {
		AbnormalAttendanceStatistics abnormalAttendanceStatistics = null;
		abnormalAttendanceStatistics = abnormalAttendanceStatisticsService.findByOrgIdAndStatisticalTime(orgId, DateUtil.getCurrentTime(DateUtil.FORMAT_SHORT));
		if(null == abnormalAttendanceStatistics){
			abnormalAttendanceStatistics = new AbnormalAttendanceStatistics();
		}
		int max = 20;
		int min = 10;
		Random random = new Random();
		int s =  random.nextInt(max) % (max - min + 1) + min;
		abnormalAttendanceStatistics.setOrgId(orgId);
		abnormalAttendanceStatistics.setAbsenteeismNum(47+s);
		abnormalAttendanceStatistics.setLeaveEarlyNum(55+s);
		abnormalAttendanceStatistics.setLateNum(90+s);
		abnormalAttendanceStatistics.setLeaveNum(35+s);
		abnormalAttendanceStatistics.setStatisticalTime(DateUtil.getCurrentTime(DateUtil.FORMAT_SHORT));
		abnormalAttendanceStatisticsService.save(abnormalAttendanceStatistics);
		return new ResponseEntity<AbnormalAttendanceStatistics>(abnormalAttendanceStatistics, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/abnormalattendanceupdate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "PUT", value = "今日排课数量", response = Void.class, notes = "今日排课数量<br><br><b>@author zhengning</b>")
	public ResponseEntity<AbnormalAttendanceStatistics> abnormalAttendanceUpdate(
			@ApiParam(value = "id 记录id") @RequestParam(value = "id", required = true) String id,
			@ApiParam(value = "absenteeismNum 旷课人数") @RequestParam(value = "absenteeismNum", required = false) Integer absenteeismNum,
			@ApiParam(value = "leaveEarlyNum 早退人数") @RequestParam(value = "leaveEarlyNum", required = false) Integer leaveEarlyNum,
			@ApiParam(value = "lateNum 迟到人数") @RequestParam(value = "lateNum", required = false) Integer lateNum,
			@ApiParam(value = "leaveNum 请假人数") @RequestParam(value = "leaveNum", required = false) Integer leaveNum) {
		AbnormalAttendanceStatistics abnormalAttendanceStatistics = abnormalAttendanceStatisticsService.findById(id);
		if(null != abnormalAttendanceStatistics){
			if(null != absenteeismNum && absenteeismNum > 0 ){
				abnormalAttendanceStatistics.setAbsenteeismNum(absenteeismNum);
			}
			if(null != leaveEarlyNum && leaveEarlyNum > 0 ){
				abnormalAttendanceStatistics.setLeaveEarlyNum(leaveEarlyNum);
			}
			if(null != lateNum && lateNum > 0 ){
				abnormalAttendanceStatistics.setLateNum(lateNum);
			}
			if(null != leaveNum && leaveNum > 0 ){
				abnormalAttendanceStatistics.setLeaveNum(leaveNum);
			}
			abnormalAttendanceStatisticsService.save(abnormalAttendanceStatistics);
		}
		return new ResponseEntity<AbnormalAttendanceStatistics>(abnormalAttendanceStatistics, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/abnormalteachingadd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "教学异常统计", response = Void.class, notes = "教学异常统计<br><br><b>@author zhengning</b>")
	public ResponseEntity<AbnormalTeachingStatistics> abnormalTeachingAdd(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId) {
		AbnormalTeachingStatistics abnormalTeachingStatistics = new AbnormalTeachingStatistics();
		abnormalTeachingStatistics = abnormalTeachingStatisticsService.findByOrgIdAndStatisticalTime(orgId, DateUtil.getCurrentTime(DateUtil.FORMAT_SHORT));
		if(null == abnormalTeachingStatistics){
			abnormalTeachingStatistics = new AbnormalTeachingStatistics();
		}
		int max = 20;
		int min = 10;
		Random random = new Random();
		int s =  random.nextInt(max) % (max - min + 1) + min;
		abnormalTeachingStatistics.setOrgId(orgId);
		abnormalTeachingStatistics.setChangeLecturerNum(21+s);
		abnormalTeachingStatistics.setLeaveEarlyNum(17+s);
		abnormalTeachingStatistics.setLateNum(22+s);
		abnormalTeachingStatistics.setStopClassNum(33+s);
		abnormalTeachingStatistics.setTeachingNum(1215+s);
		abnormalTeachingStatistics.setStatisticalTime(DateUtil.getCurrentTime(DateUtil.FORMAT_SHORT));
		abnormalTeachingStatisticsService.save(abnormalTeachingStatistics);
		return new ResponseEntity<AbnormalTeachingStatistics>(abnormalTeachingStatistics, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/abnormalteachingupdate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "PUT", value = "今日排课数量", response = Void.class, notes = "今日排课数量<br><br><b>@author zhengning</b>")
	public ResponseEntity<AbnormalTeachingStatistics> abnormalTeachingUpdate(
			@ApiParam(value = "id 记录id") @RequestParam(value = "id", required = true) String id,
			@ApiParam(value = "changeLecturerNum 更换主讲教师人次") @RequestParam(value = "changeLecturerNum", required = false) Integer changeLecturerNum,
			@ApiParam(value = "leaveEarlyNum 早退教师人数") @RequestParam(value = "leaveEarlyNum", required = false) Integer leaveEarlyNum,
			@ApiParam(value = "lateNum 迟到教师人数") @RequestParam(value = "lateNum", required = false) Integer lateNum,
			@ApiParam(value = "stopClassNum 调停课人次") @RequestParam(value = "stopClassNum", required = false) Integer stopClassNum,
			@ApiParam(value = "teachingNum 教学人数") @RequestParam(value = "teachingNum", required = false) Integer teachingNum) {
		AbnormalTeachingStatistics abnormalTeachingStatistics = abnormalTeachingStatisticsService.findById(id);
		if(null != abnormalTeachingStatistics){
			if(null != changeLecturerNum && changeLecturerNum > 0 ){
				abnormalTeachingStatistics.setChangeLecturerNum(changeLecturerNum);
			}
			if(null != leaveEarlyNum && leaveEarlyNum > 0 ){
				abnormalTeachingStatistics.setLeaveEarlyNum(leaveEarlyNum);
			}
			if(null != lateNum && lateNum > 0 ){
				abnormalTeachingStatistics.setLateNum(lateNum);
			}
			if(null != stopClassNum && stopClassNum > 0 ){
				abnormalTeachingStatistics.setStopClassNum(stopClassNum);
			}
			if(null != teachingNum && teachingNum > 0 ){
				abnormalTeachingStatistics.setTeachingNum(teachingNum);
			}
			abnormalTeachingStatisticsService.save(abnormalTeachingStatistics);
		}
		return new ResponseEntity<AbnormalTeachingStatistics>(abnormalTeachingStatistics, HttpStatus.OK);
	}

}