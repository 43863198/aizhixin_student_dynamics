package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.service.CetStatisticAnalysisService;
import com.aizhixin.cloud.dataanalysis.analysis.service.TeachingScoreService;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-11
 */
@RestController
@RequestMapping("/v1/teachingscore")
@Api(description = "教学成绩统计分析API")
public class TeachingScoreController {
    @Autowired
    private TeachingScoreService teachingScoreService;

    /**
     * 教学成绩———统计
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "教学成绩———统计", response = Void.class, notes = "教学成绩———统计<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getStatisticTeachingScore(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "grade 学年" , required = false) @RequestParam(value = "grade", required = false) String grade,
            @ApiParam(value = "semester 学期" , required = false) @RequestParam(value = "semester", required = false) Integer semester) {
        return teachingScoreService.getStatistic(orgId, grade, semester);
    }

    /**
     * 教学成绩———趋势分析
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/analysis", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "教学成绩———趋势分析", response = Void.class, notes = "教学成绩———趋势分析<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getTeachingScoreTrendAnalysis(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 机构id", required = false) @RequestParam(value = "collegeId", required = false) Long collegeId,
            @ApiParam(value = "type 分析指标：1:学生人数，2:平均GPA，3:不及格人数，4:课程平均分" , required = true)
            @RequestParam(value = "type", required = true) Integer type) {
        return teachingScoreService.getTeachingScoreTrendAnalysis(orgId, collegeId, type);
    }

    /**
     * 教学成绩———详情列表
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "教学成绩———详情列表", response = Void.class, notes = "教学成绩———详情列表<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getTeachingScoreDetail(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 机构id 注:多个时候用“，”隔开", required = true) @RequestParam(value = "collegeId", required = true) String collegeId,
            @ApiParam(value = "grade 年级 注:多个时候用“，”隔开", required = false) @RequestParam(value = "grade", required = false) String grade,
            @ApiParam(value = "nj 姓名/学号" , required = false) @RequestParam(value = "nj", required = false) String nj,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return teachingScoreService.getTeachingScoreDetail(orgId, collegeId, grade, nj, PageUtil.createNoErrorPageRequest(pageNumber, pageSize));
    }




}
