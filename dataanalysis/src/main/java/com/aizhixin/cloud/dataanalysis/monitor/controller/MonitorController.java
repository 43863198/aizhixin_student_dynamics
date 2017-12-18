/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.monitor.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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



}