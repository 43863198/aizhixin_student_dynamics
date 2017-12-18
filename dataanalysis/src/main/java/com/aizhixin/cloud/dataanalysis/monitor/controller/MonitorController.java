/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.monitor.controller;


import com.aizhixin.cloud.dataanalysis.monitor.dto.CollegeGpaDTO;
import com.aizhixin.cloud.dataanalysis.monitor.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/v1/monitor")
@Api(value = "监控大屏数据展示", description = "监控大屏数据展示")
public class MonitorController {
	private final static Logger log = LoggerFactory
			.getLogger(MonitorController.class);
	@Autowired
	private MonitorService monitorService;
	/**
	 * 大屏监控学院平均Gpa
	 * @param orgId
	 * @return
	 */
	@GetMapping(value = "/collegegpa", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "首页教学成绩信息", response = Void.class, notes = "首页教学成绩统计信息<br><br><b>@author 王俊</b>")
	public List<CollegeGpaDTO> getCollegeGpa(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
		return monitorService.getCollegeGpa(orgId);
	}



}