package com.aizhixin.cloud.dataanalysis.zb.controller;

import com.aizhixin.cloud.dataanalysis.zb.service.AnalysisIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 手动统计
 */
@RestController
@RequestMapping("/v1/index/manual")
@Api(description = "英语手动触发")
public class IndexAalysisManualController {
    @Autowired
    private AnalysisIndexService analysisIndexService;

    /**
     */
    @GetMapping(value = "/cet", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语等级考试基础指标统计", response = Void.class, notes = "英语等级考试基础指标统计<br><br><b>@author zhen.pan</b>")
    public void cet(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        analysisIndexService.schoolAllIndex(orgId);
    }

}
