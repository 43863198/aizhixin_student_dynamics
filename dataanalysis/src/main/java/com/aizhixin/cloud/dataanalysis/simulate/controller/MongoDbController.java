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
import org.springframework.data.mongodb.core.MongoTemplate;
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
@RequestMapping("/v1/mongodb")
@Api(value = "mongodb数据操作", description = "mongodb数据操作")
public class MongoDbController {
	private final static Logger log = LoggerFactory
			.getLogger(MongoDbController.class);
	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "增加数据", response = Void.class, notes = "增加数据<br><br><b>@author zhengning</b>")
	public ResponseEntity<Map<String, Object>> add(
			@ApiParam(value = "json json字符串") @RequestParam(value = "json", required = true) String json) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	

}