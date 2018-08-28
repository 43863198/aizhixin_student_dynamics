package com.aizhixin.cloud.dataanalysis.etl.cet.controller;


import com.aizhixin.cloud.dataanalysis.bz.service.CetEtlService;
import com.aizhixin.cloud.dataanalysis.etl.cet.service.CetDcIndexAnalysisService;
import com.aizhixin.cloud.dataanalysis.etl.cet.service.CetGradeService;
import com.aizhixin.cloud.dataanalysis.etl.cet.service.CetLjIndexAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/etl/cet")
@Api(description = "英语等级考试ETL程序")
public class EtlCetController {

    @Autowired
    private CetEtlService cetEtlService;
    @Autowired
    private CetLjIndexAnalysisService cetLjIndexAnalysisService;
    @Autowired
    private CetGradeService cetGradeService;
    @Autowired
    private CetDcIndexAnalysisService cetDcIndexAnalysisService;


    @PutMapping(value = "/etl", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "英语等级考试数据库对数据库标准数据清洗ETL", response = Void.class, notes = "英语等级考试数据库对数据库标准数据清洗ETL<br><br><b>@author zhen.pan</b>")
    public void etlCet(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        cetEtlService.etlDB2DB(orgId);
    }

    @PutMapping(value = "/index/lj", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "英语等级考试累计基础指标统计", response = Void.class, notes = "英语等级考试累计基础指标统计<br><br><b>@author zhen.pan</b>")
    public void cetlj(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        cetLjIndexAnalysisService.calLjHaveTest(orgId);
    }

    @PutMapping(value = "/index/lj/newest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "英语等级考试最新累计基础指标统计", response = Void.class, notes = "英语等级考试最新累计基础指标统计<br><br><b>@author zhen.pan</b>")
    public void cetljNewest(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        cetLjIndexAnalysisService.calLjNewest(orgId);
    }

    @PutMapping(value = "/index/lj/grade/newest", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "英语等级考试最新累计年级指标统计", response = Void.class, notes = "英语等级考试最新累计年级指标统计<br><br><b>@author zhen.pan</b>")
    public void cetljGradeNewest(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        cetGradeService.calLjNewest(orgId);
    }

    @PutMapping(value = "/index/lj/grade", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "英语等级考试所有累计年级指标统计", response = Void.class, notes = "英语等级考试所有累计年级指标统计<br><br><b>@author zhen.pan</b>")
    public void cetljGrade(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        cetGradeService.calLjAll(orgId);
    }

    @PutMapping(value = "/index/dc/grade", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "英语等级考试单次所有年级指标统计", response = Void.class, notes = "英语等级考试单次所有年级指标统计<br><br><b>@author zhen.pan</b>")
    public void cetDcGrade(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        cetGradeService.calDcAll(orgId);
    }

    @PutMapping(value = "/index/dc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "英语等级考试单次基础指标统计", response = Void.class, notes = "英语等级考试单次基础指标统计<br><br><b>@author zhen.pan</b>")
    public void cetDc(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        cetDcIndexAnalysisService.calDcHaveTest(orgId);
    }
}
