package com.aizhixin.cloud.dataanalysis.stuscore.controller;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.stuscore.domain.CetScoreDomain;
import com.aizhixin.cloud.dataanalysis.stuscore.domain.CourseScoreDomain;
import com.aizhixin.cloud.dataanalysis.stuscore.service.StuScoreService;
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

@RestController
@RequestMapping("/v1/stuscore")
@Api(description = "学生成绩API")
public class StuScoreController {

    @Autowired
    private StuScoreService scoreService;

    @GetMapping(value = "/getCourseScore", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "课程成绩列表", response = Void.class, notes = "课程成绩列表<br><br><b>@author hsh</b>")
    public ResponseEntity<PageData<CourseScoreDomain>> getCourseScore(@ApiParam(value = "机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
                                                                      @ApiParam(value = "userId") @RequestParam(value = "userId", required = false) Long userId,
                                                                      @ApiParam(value = "学号") @RequestParam(value = "jobNum", required = false) String jobNum,
                                                                      @ApiParam(value = "课程名称") @RequestParam(value = "courseName", required = false) String courseName,
                                                                      @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                                      @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        PageData<CourseScoreDomain> pageData = scoreService.getCourseScore(orgId, userId, jobNum, courseName, pageNumber, pageSize);
        return new ResponseEntity<PageData<CourseScoreDomain>>(pageData, HttpStatus.OK);
    }

    @GetMapping(value = "/getCetScore", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语等级考试成绩列表", response = Void.class, notes = "英语等级考试成绩列表<br><br><b>@author hsh</b>")
    public ResponseEntity<PageData<CetScoreDomain>> getCetScore(@ApiParam(value = "机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
                                                                @ApiParam(value = "userId") @RequestParam(value = "userId", required = false) Long userId,
                                                                @ApiParam(value = "学号") @RequestParam(value = "jobNum", required = false) String jobNum,
                                                                @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                                @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        PageData<CetScoreDomain> pageData = scoreService.getCetScore(orgId, userId, jobNum, pageNumber, pageSize);
        return new ResponseEntity<PageData<CetScoreDomain>>(pageData, HttpStatus.OK);
    }
}
