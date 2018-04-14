package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.service.ExaminationArrangementService;
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
 * @Date: 2018-04-14
 */
@RestController
@RequestMapping("/v1/exam")
@Api(value = "考试安排API", description = "考试安排API")
public class ExaminationArrangementController {
    @Autowired
    private ExaminationArrangementService examinationArrangementService;
    /**
     * 今日考试安排
     * @return
     */
    @GetMapping(value = "/arrange", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "今日考试安排", response = Void.class, notes = "今日考试安排<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> getArrange(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId
    ) {
        return new ResponseEntity<Map<String, Object>>(examinationArrangementService.getArrange(orgId), HttpStatus.OK);
    }




}
