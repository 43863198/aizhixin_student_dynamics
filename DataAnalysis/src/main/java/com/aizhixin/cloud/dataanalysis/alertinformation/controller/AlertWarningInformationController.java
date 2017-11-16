package com.aizhixin.cloud.dataanalysis.alertinformation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 预警信息列表
     *
     * @return
     */
    @GetMapping(value = "/getlist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "", value = "预警信息列表", response = Void.class, notes = "预警信息列表<br><br><b>@author jianwei.wu</b>")
    public Long add(
    ) {
        Map<String, Object> result = new HashMap<>();
        return null;
    }





}
