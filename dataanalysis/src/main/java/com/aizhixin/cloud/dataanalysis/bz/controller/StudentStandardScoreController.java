package com.aizhixin.cloud.dataanalysis.bz.controller;

import com.aizhixin.cloud.dataanalysis.bz.entity.CetStandardScore;
import com.aizhixin.cloud.dataanalysis.bz.service.CetStandardScoreService;
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

import java.util.List;
import java.util.Map;

/**
 * @Author : dengchao
 * @Create : 2018-08-13
 * @Description :
 */

@RestController
@RequestMapping("/v1/index/stu")
@Api(description = "学生成绩指标应用")
public class StudentStandardScoreController {

    @Autowired
    private CetStandardScoreService cetStandardScoreService;

    @GetMapping(value = "/getCetScore", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语等级考试成绩列表", response = Void.class, notes = "英语等级考试成绩列表<br><br><b>@author hsh</b>")
    public ResponseEntity<List<CetStandardScore>> getCetScore(@ApiParam(value = "机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
                                                                @ApiParam(value = "学号") @RequestParam(value = "jobNum", required = true) String jobNum,
                                                                @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                                @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<CetStandardScore> pageData = cetStandardScoreService.getCetScore(orgId, jobNum, pageNumber, pageSize);
        return new ResponseEntity<List<CetStandardScore>>(pageData, HttpStatus.OK);
    }


    @GetMapping(value = "/cetTop", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---top10", response = Void.class, notes = "英语考试单次数据分析---top10<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> getTop(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "cetType 成绩类型： (3;4;6;)" , required = true ) @RequestParam(value = "cetType") String cetType,
            @ApiParam(value = "teacherYear 学年 (格式如：2017)" , required = true ) @RequestParam(value = "teacherYear") String teacherYear,
            @ApiParam(value = "semester 学期 (1,2; 1表示秋，2表示春)" , required = true) @RequestParam(value = "semester") String semester ) {
        return new ResponseEntity<>(cetStandardScoreService.getTop(orgId,cetType, teacherYear, semester), HttpStatus.OK);
    }
}
