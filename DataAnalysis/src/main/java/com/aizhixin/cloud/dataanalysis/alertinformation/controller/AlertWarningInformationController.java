package com.aizhixin.cloud.dataanalysis.alertinformation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-15
 */
@RestController
@RequestMapping("/v1/alertwarninginfo")
@Api(description = "预警信息API")
public class AlertWarningInformationController {
	
	@Autowired
	private AlertWarningInformationService alertWarningInforService;

    /**
     * 预警信息列表
     *
     * @return
     */
    @GetMapping(value = "/getlist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "预警信息列表", response = Void.class, notes = "预警信息列表<br><br><b>@author jianwei.wu</b>")
    public Map<String, Object>  getList(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 机构id" , required = true) @RequestParam(value = "collegeId", required = true) Long collegeId,
            @ApiParam(value = "type 预警类型") @RequestParam(value = "type", required = false) String  type,
            @ApiParam(value = "warningLevel 预警等级") @RequestParam(value = "warningLevel", required = false) int warningLevel) {



        return null;
    }


    @RequestMapping(value = "/registercount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "按机构id统计机构下所有学院的注册报到预警数量", response = Void.class, notes = "按机构id统计机构下所有学院的注册报到预警数量<br><br><b>@author 郑宁</b>")
	public ResponseEntity<Map<String, Object>> taskChart(
            @ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId
			) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put(ApiReturnConstants.DATA, alertWarningInforService.findRegisterCountInfor(orgId));
		return  new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}


}
