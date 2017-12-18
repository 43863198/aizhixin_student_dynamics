/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.monitor.controller;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;
import com.aizhixin.cloud.dataanalysis.monitor.domain.AbnormalAttendanceDomain;
import com.aizhixin.cloud.dataanalysis.monitor.domain.AbnormalTeachingDomain;
import com.aizhixin.cloud.dataanalysis.monitor.domain.TodayCourseDomain;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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

	@RequestMapping(value = "/todaycoursenum", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "今日排课数量", response = Void.class, notes = "今日排课数量<br><br><b>@author zhengning</b>")
	public ResponseEntity<TodayCourseDomain> todayCourseNum(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId) {
		TodayCourseDomain domain = new TodayCourseDomain();
		return new ResponseEntity<TodayCourseDomain>(domain, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/abnormalattendance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "学生异常出勤统计", response = Void.class, notes = "学生异常出勤统计<br><br><b>@author zhengning</b>")
	public ResponseEntity<AbnormalAttendanceDomain> abnormalAttendance(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId) {
		AbnormalAttendanceDomain domain = new AbnormalAttendanceDomain();
		return new ResponseEntity<AbnormalAttendanceDomain>(domain, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/abnormalteaching", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "教学异常统计", response = Void.class, notes = "教学异常统计<br><br><b>@author zhengning</b>")
	public ResponseEntity<AbnormalTeachingDomain> abnormalTeaching(
			@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId) {
		AbnormalTeachingDomain domain = new AbnormalTeachingDomain();
		return new ResponseEntity<AbnormalTeachingDomain>(domain, HttpStatus.OK);
	}

}