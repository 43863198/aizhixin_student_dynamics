package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.service.CourseStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-16
 */
@RestController
@RequestMapping("/v1/course")
@Api(value = "课程统计分析关API", description = "课程统计分析关API")
public class CourseStatisticsController {
    @Autowired
    private CourseStatisticsService courseStatisticsService;
    /**
     * 获取今日排课的统计信息
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取今日排课的统计信息", response = Void.class, notes = "获取今日排课的统计信息<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String,Object>> getClassToday(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return new ResponseEntity<Map<String, Object>>(courseStatisticsService.getClassToday(orgId), HttpStatus.OK);
    }

    /**
     * 获取今日排课详情
     * @param orgId
     * @return
     */
    @GetMapping(value = "/todaydetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取今日排课详情", response = Void.class, notes = "获取今日排课详情<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String,Object>> getTodayDetail(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize){
        return new ResponseEntity(courseStatisticsService.getTodayDetail(orgId,pageNumber,pageSize), HttpStatus.OK);
    }





}
