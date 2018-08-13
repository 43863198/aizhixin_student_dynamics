package com.aizhixin.cloud.dataanalysis.zb.controller;

import com.aizhixin.cloud.dataanalysis.bz.service.CetEtlService;
import com.aizhixin.cloud.dataanalysis.bz.service.StandardScoreService;
import com.aizhixin.cloud.dataanalysis.zb.service.AnalysisIndexService;
import com.aizhixin.cloud.dataanalysis.zb.service.CetLjIndexAnalysisService;
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
public class IndexAnalysisManualController {
    @Autowired
    private AnalysisIndexService analysisIndexService;
    @Autowired
    private CetEtlService cetEtlService;
    @Autowired
    private StandardScoreService standardScoreService;
    @Autowired
    private CetLjIndexAnalysisService cetLjIndexAnalysisService;

    @GetMapping(value = "/cet/etl", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语等级考试数据库对数据库标准数据清洗ETL", response = Void.class, notes = "英语等级考试数据库对数据库标准数据清洗ETL<br><br><b>@author zhen.pan</b>")
    public void etlCet(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        cetEtlService.etlDB2DB(orgId);
    }

    @GetMapping(value = "/score/etl", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "学生成绩数据库对数据库标准数据清洗ETL", response = Void.class, notes = "学生成绩数据库对数据库标准数据清洗ETL<br><br><b>@author zhen.pan</b>")
    public void etlScore(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        standardScoreService.etlDB2DB(orgId);
    }

    @GetMapping(value = "/cet", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语等级考试基础指标统计", response = Void.class, notes = "英语等级考试基础指标统计<br><br><b>@author zhen.pan</b>")
    public void cet(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        analysisIndexService.schoolBaseIndex(orgId);
    }

    @GetMapping(value = "/cet/grade", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语等级考试基础指标年级分布统计", response = Void.class, notes = "英语等级考试基础指标年级分布统计<br><br><b>@author zhen.pan</b>")
    public void cetGrade(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        analysisIndexService.schoolNjIndex(orgId);
    }

    @GetMapping(value = "/score", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "学生成绩指标的计算", response = Void.class, notes = "学生成绩指标的计算<br><br><b>@author zhen.pan</b>")
    public void score(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        analysisIndexService.schoolStudentScoreIndex(orgId);
    }

    @GetMapping(value = "/cetlj", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语等级考试累计基础指标统计", response = Void.class, notes = "英语等级考试累计基础指标统计<br><br><b>@author zhen.pan</b>")
    public void cetlj(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        cetLjIndexAnalysisService.schoolBaseIndex(orgId);
    }

    @GetMapping(value = "/cetljnewest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语等级考试最新累计基础指标统计", response = Void.class, notes = "英语等级考试最新累计基础指标统计<br><br><b>@author zhen.pan</b>")
    public void cetljNewest(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        cetLjIndexAnalysisService.calLjNewest(orgId);
    }
}
