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
	
	

}