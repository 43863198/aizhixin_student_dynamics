package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.service.FigurePortraitService;
import com.netflix.discovery.converters.Auto;
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
 * @Date: 2018-05-11
 */
@RestController
@RequestMapping("/v1/portrait")
@Api(value = "  ", description = "人物画像API")
public class FigurePortraitController {
    @Autowired
    FigurePortraitService figurePortraitService;
    /**
     * 学生画像---学生列表
     * @param orgId
     * @param collegeNumber
     * @param professionNumber
     * @param classNumber
     * @return
     */
    @GetMapping(value = "/avg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "学生画像---学生列表", response = Void.class, notes = "学生画像---学生列表<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> getStudentList(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeNumber 学院码", required = false) @RequestParam(value = "collegeNumber", required = false) String collegeNumber,
            @ApiParam(value = "professionNumber 专业码", required = false) @RequestParam(value = "professionNumber", required = false) String professionNumber,
            @ApiParam(value = "classNumber 班号", required = false) @RequestParam(value = "classNumber", required = false) String classNumber
    ) {
        return new ResponseEntity<Map<String, Object>>(figurePortraitService.getStudentList(orgId, collegeNumber, professionNumber, classNumber), HttpStatus.OK);
    }






}
