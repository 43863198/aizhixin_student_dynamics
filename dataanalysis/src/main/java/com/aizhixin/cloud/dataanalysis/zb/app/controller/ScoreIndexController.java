package com.aizhixin.cloud.dataanalysis.zb.app.controller;


import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.zb.app.service.ScoreIndexService;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/index/score")
@Api(description = "学生成绩指标应用")
public class ScoreIndexController {

    @Autowired
    private ScoreIndexService scoreIndexService;

    @GetMapping(value = "/dwcount", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "单位成绩统计值指标", response = Void.class, notes = "单位成绩统计值指标<br><br><b>@author zhen.pan</b>")
    public ScoreDwCountVO cetOrgCount(
            @ApiParam(value = "xnxq 学年学期YYYY-CCCC-x", required = true) @RequestParam(value = "xnxq") String xnxq,
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode) {
        return scoreIndexService.findDwCount(orgId, xnxq, collegeCode);
    }

    @GetMapping(value = "/sub/avgjd", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "子单位平均绩点", response = Void.class, notes = "子单位平均绩点<br><br><b>@author zhen.pan</b>")
    public List<ScoreAvgJdVO> subOrgAvgJd(
            @ApiParam(value = "xnxq 学年学期YYYY-CCCC-x", required = true) @RequestParam(value = "xnxq") String xnxq,
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode) {
        return scoreIndexService.findSubDwAvgJd(orgId, xnxq, collegeCode);
    }

    @GetMapping(value = "/sub/index", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "子单位成绩指标", response = Void.class, notes = "子单位成绩指标<br><br><b>@author zhen.pan</b>")
    public List<ScoreSubDwIndexVO> subOrgIndex(
            @ApiParam(value = "xnxq 学年学期YYYY-CCCC-x", required = true) @RequestParam(value = "xnxq") String xnxq,
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode) {
        return scoreIndexService.findSubDwIndex(orgId, xnxq, collegeCode);
    }

    @GetMapping(value = "/year/index", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "历年趋势指标", response = Void.class, notes = "历年趋势指标<br><br><b>@author zhen.pan</b>")
    public List<ScoreAllYearIndexVO> yearIndex(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode) {
        return scoreIndexService.findSubDwIndex(orgId, collegeCode);
    }

    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "学生学期成绩指标", response = Void.class, notes = "学生学期成绩指标<br><br><b>@author zhen.pan</b>")
    public PageData<StudentSemesterScoreIndexVO> yearIndex(
            @ApiParam(value = "xnxq 学年学期YYYY-CCCC-x", required = true) @RequestParam(value = "xnxq") String xnxq,
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "nj 年级") @RequestParam(value = "nj", required = false) String nj,
            @ApiParam(value = "name 姓名学号") @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "pageNumber 年级") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 姓名学号") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return scoreIndexService.findScoreSemesterDetails(orgId, xnxq, collegeCode, professionCode, nj, name, pageNumber, pageSize);
    }
}
