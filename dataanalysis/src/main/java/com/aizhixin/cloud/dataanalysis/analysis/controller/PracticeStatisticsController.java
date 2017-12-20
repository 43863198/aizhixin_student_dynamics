package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.service.PracticeStatisticsService;
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
 * @Date: 2017-12-04
 */
@RestController
@RequestMapping("/v1/practice")
@Api(description = "实践学情API")
public class PracticeStatisticsController {
    @Autowired
    private PracticeStatisticsService practiceStatisticsService;


    /**
     * 全校实践信息的统计
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "全校学生实践统计信息", response = Void.class, notes = "全校学生实践统计信息<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getStatistics(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return null;
    }

    /**
     * 实践学情———统计
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/practicestatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "实践学情———统计", response = Void.class, notes = "实践学情———统计<br><br><b>@author wangjun</b>")
    public Map<String,Object>   getStatisticPractice(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "teacherYear 年" , required = true) @RequestParam(value = "teacherYear", required = true) String teacherYear,
            @ApiParam(value = "semester学期" , required = true) @RequestParam(value = "semester", required = true) Integer semester,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return practiceStatisticsService.getStatisticPractice(orgId,teacherYear,semester, pageNumber, pageSize);
    }

    /**
     * 实践学情———趋势分析
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/practicetrend", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "实践学情———趋势分析", response = Void.class, notes = "实践学情———趋势分析<br><br><b>@author wangjun</b>")
    public Map<String,Object>   getPracticeTrend(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 学院id") @RequestParam(value = "collegeId", required = false) Long collegeId) {
        return practiceStatisticsService.getPracticeTrend(orgId, collegeId);
    }
    /**
     * 实践学情详情———统计
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/practicedetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "实践学情详情———统计", response = Void.class, notes = "实践学情详情———统计<br><br><b>@author wangjun</b>")
    public Map<String,Object>   getPracticeDetail(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "className 班级名称", required = false) @RequestParam(value = "className", required = false) String className,
            @ApiParam(value = "collegeId 学院id", required = true) @RequestParam(value = "collegeId", required = true) Long collegeId,
            @ApiParam(value = "teacherYear 年" , required = true) @RequestParam(value = "teacherYear", required = true) String teacherYear,
            @ApiParam(value = "semester学期" , required = true) @RequestParam(value = "semester", required = true) Integer semester,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return practiceStatisticsService.getPracticeDetail(orgId,className,collegeId,teacherYear,semester, pageNumber, pageSize);
    }
}
