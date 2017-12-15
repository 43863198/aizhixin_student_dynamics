package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.dto.CetScoreStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.NewStudentProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.PracticeStaticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolStatisticsService;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.DealDomain;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolStatisticsService;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.studentRegister.service.StudentRegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-29
 */
@RestController
@RequestMapping("/v1/schoolstatistic")
@Api(description = "学校概况统计API")
public class SchoolConditionStatisticsController {
    @Autowired
    private SchoolStatisticsService schoolStatisticsService;
    @Autowired
    private StudentRegisterService studentRegisterService;

    /**
     * 全校人数统计信息
     * @param orgId
     * @return
     */
    @GetMapping(value = "/getSchoolPersonStatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "全校人数统计信息", response = Void.class, notes = "首页全校人数统计信息<br><br><b>@author 王俊</b>")
    public SchoolProfileDTO getSchoolPersonStatistics(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return schoolStatisticsService.getSchoolPersonStatistics(orgId);
    }
    /**
     * 首页迎新人数统计信息
     * @param orgId
     * @return
     */
    @GetMapping(value = "/getNewStudentStatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "首页迎新人数统计信息", response = Void.class, notes = "首页迎新人数统计信息<br><br><b>@author 王俊</b>")
    public NewStudentProfileDTO getNewStudentStatistics(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return schoolStatisticsService.getNewStudentStatistics(orgId);
    }
    /**
     * 首页实践学情统计信息
     * @param orgId
     * @return
     */
    @GetMapping(value = "/getPracticeStatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "首页实践学情统计信息", response = Void.class, notes = "首页实践学情统计信息<br><br><b>@author 王俊</b>")
    public PracticeStaticsDTO getPracticeStatistics(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return schoolStatisticsService.getPracticeStatics(orgId);
    }
    /**
     * 首页四六级学情统计信息
     * @param orgId
     * @return
     */
    @GetMapping(value = "/getEctStatics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "首页四六级学情统计信息", response = Void.class, notes = "首页四六级学情统计信息<br><br><b>@author 王俊</b>")
    public CetScoreStatisticsDTO getEctStatics(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return schoolStatisticsService.getEctStatics(orgId);
    }

    /**
     * 迎新学情———统计
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/newstudents", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "迎新学情———统计", response = Void.class, notes = "迎新学情———统计<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getStatisticNewstudents(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "year 年" , required = true) @RequestParam(value = "year", required = true) Integer year
 ) {
        return schoolStatisticsService.getStatisticNewstudents(orgId,year);
    }

    /**
     * 迎新学情———趋势分析
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/trend", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "迎新学情———趋势分析", response = Void.class, notes = "迎新学情———趋势分析<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getTrend(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 学院id") @RequestParam(value = "collegeId", required = false) Long collegeId,
            @ApiParam(value = "typeIndex 指标", required = true) @RequestParam(value = "typeIndex", required = true) int typeIndex) {
        return schoolStatisticsService.getTrend(orgId, collegeId, typeIndex);
    }

    /**
     * 趋势分析---指标类型
     *
     * @return
     */
    @GetMapping(value = "/trendtype", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "趋势分析---指标类型", response = Void.class, notes = "趋势分析---指标类型<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getTrendType() {
        return schoolStatisticsService.getTrendType();
    }


    /**
     * 迎新---学院详情
     *
     * @return
     */
    @GetMapping(value = "/collegedetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "迎新---学院详情", response = Void.class, notes = "迎新---学院详情<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getCollegeDetails(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 学院id(多个用“，”隔开)") @RequestParam(value = "collegeId", required = false) String collegeId,
            @ApiParam(value = "nj 姓名/学号") @RequestParam(value = "nj", required = false) String nj,
            @ApiParam(value = "type 学生类型(1:专科2:本科3:研究生)") @RequestParam(value = "type", required = false) String type,
            @ApiParam(value = "报道情况 isReport(0:未报到；1:已报到；)" ) @RequestParam(value = "isReport", required = false) String isReport,
            @ApiParam(value = "缴费情况 isPay(1:已缴费；2:绿色通道；3:其他)" ) @RequestParam(value = "isPay", required = false) String isPay,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return schoolStatisticsService.getCollegeDetails(PageUtil.createNoErrorPageRequest(pageNumber, pageSize), orgId, collegeId, nj, type, isReport, isPay);
    }

    /**
     * 首页教学成绩统计信息
     * @param orgId
     * @return
     */
    @GetMapping(value = "/getTeachingSoreStatics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "首页教学成绩信息", response = Void.class, notes = "首页教学成绩统计信息<br><br><b>@author 王俊</b>")
    public Map<String,Object> getTeachingSoreStatics(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return schoolStatisticsService.getTeachingSoreStatics(orgId);
    }





}
