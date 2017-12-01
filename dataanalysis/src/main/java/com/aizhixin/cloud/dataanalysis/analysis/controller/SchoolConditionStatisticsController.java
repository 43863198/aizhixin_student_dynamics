package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.dto.NewStudentProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolStatisticsService;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.DealDomain;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolStatisticsService;
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
     *
     */

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
            @ApiParam(value = "year 年" , required = true) @RequestParam(value = "year", required = true) String year,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return schoolStatisticsService.getStatisticNewstudents(orgId,year, PageUtil.createNoErrorPageRequest(pageNumber, pageSize));
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
            @ApiParam(value = "colloegeId 学院id") @RequestParam(value = "colloegeId", required = false) Long colloegeId,
            @ApiParam(value = "typeIndex 指标", required = true) @RequestParam(value = "typeIndex", required = true) int typeIndex) {
        return schoolStatisticsService.getTrend(orgId, colloegeId, typeIndex);
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




}
