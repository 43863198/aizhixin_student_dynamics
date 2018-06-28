/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.monitor.controller;


import java.util.HashMap;
import java.util.Map;

import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
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
import com.aizhixin.cloud.dataanalysis.monitor.domain.AbnormalAttendanceDomain;
import com.aizhixin.cloud.dataanalysis.monitor.domain.AbnormalTeachingDomain;
import com.aizhixin.cloud.dataanalysis.monitor.domain.TeachingScheduleDomain;

import com.aizhixin.cloud.dataanalysis.monitor.domain.AbnormalAttendanceDomain;
import com.aizhixin.cloud.dataanalysis.monitor.domain.AbnormalTeachingDomain;
import com.aizhixin.cloud.dataanalysis.monitor.dto.AlarmDTO;
import com.aizhixin.cloud.dataanalysis.monitor.dto.CollegeGpaDTO;
import com.aizhixin.cloud.dataanalysis.monitor.dto.CourseEvaluateDTO;
import com.aizhixin.cloud.dataanalysis.monitor.dto.SchoolStudentStateDTO;
import com.aizhixin.cloud.dataanalysis.monitor.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhengning
 *
 */
@RestController
@RequestMapping("/v1/monitor")
@Api(value = "监控大屏数据展示", description = "监控大屏数据展示")
public class MonitorController {
	private final static Logger log = LoggerFactory
			.getLogger(MonitorController.class);
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private AbnormalAttendanceStatisticsService abnormalAttendanceStatisticsService;
	@Autowired
	private AbnormalTeachingStatisticsService abnormalTeachingStatisticsService;
	@Autowired
	private TeachingScheduleStatisticsService teachingScheduleStatisticsService;
	@Autowired
	private AlertWarningInformationService alertWarningInforService;
	/**
	 * 大屏监控学院平均Gpa
	 * @param orgId
	 * @return
	 */
	@GetMapping(value = "/collegegpa", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "大屏监控学院平均Gpa", response = Void.class, notes = "大屏监控学院平均Gpa<br><br><b>@author 王俊</b>")
	public List<CollegeGpaDTO> getCollegeGpa(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
		return monitorService.getCollegeGpa(orgId);
	}
	/**
	 * 大屏监控课程综合评分
	 * @param orgId
	 * @return
	 */
	@GetMapping(value = "/coursescore", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "大屏监课程综合评分", response = Void.class, notes = "大屏监课程综合评分<br><br><b>@author 王俊</b>")
	public CourseEvaluateDTO getCourseScore(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
		return monitorService.getCourseScore(orgId);
	}
	/**
	 * 大屏监控预警处理情况
	 * @param orgId
	 * @return
	 */
	@GetMapping(value = "/alarm", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "大屏监控预警处理情况", response = Void.class, notes = "大屏监控预警处理情况<br><br><b>@author 王俊</b>")
	public Map<String,Object> getAlarm(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
		Map<String,Integer> schoolYearSemester = alertWarningInforService.getschoolYearAndSemester();
		int s = schoolYearSemester.get("semester");
		String year = schoolYearSemester.get("schoolYear")+"";
		String semester;
		if(s==1){
			semester = "春";
		}else {
			semester = "秋";
		}
		return alertWarningInforService.getStatistical(orgId,year,semester);//getAlarm(orgId)
	}
	/**
	 * 大屏监控人数统计
	 * @param orgId
	 * @return
	 */
	@GetMapping(value = "/persons", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "大屏监控人数统计", response = Void.class, notes = "大屏监控人数统计<br><br><b>@author 王俊</b>")
	public SchoolStudentStateDTO getPersons(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
		return monitorService.getPersons(orgId);
	}

	@RequestMapping(value = "/todaycoursenum", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "今日排课数量", response = Void.class, notes = "今日排课数量<br><br><b>@author zhengning</b>")
	public ResponseEntity<TeachingScheduleDomain> todayCourseNum(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId) {
		return new ResponseEntity<TeachingScheduleDomain>(teachingScheduleStatisticsService.findByOrgId(orgId), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/abnormalattendance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "学生异常出勤统计", response = Void.class, notes = "学生异常出勤统计<br><br><b>@author zhengning</b>")
	public ResponseEntity<AbnormalAttendanceDomain> abnormalAttendance(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId) {
		return new ResponseEntity<AbnormalAttendanceDomain>(abnormalAttendanceStatisticsService.findByOrgId(orgId), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/abnormalteaching", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "教学异常统计", response = Void.class, notes = "教学异常统计<br><br><b>@author zhengning</b>")
	public ResponseEntity<AbnormalTeachingDomain> abnormalTeaching(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId) {
		return new ResponseEntity<AbnormalTeachingDomain>(abnormalTeachingStatisticsService.findByOrgId(orgId), HttpStatus.OK);
	}

}