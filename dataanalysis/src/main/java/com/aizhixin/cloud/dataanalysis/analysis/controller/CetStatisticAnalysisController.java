package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.service.CetStatisticAnalysisService;
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
 * @Date: 2017-12-07
 */
@RestController
@RequestMapping("/v1/cet")
@Api(description = "英语四六级统计分析API")
public class CetStatisticAnalysisController {
    @Autowired
    private CetStatisticAnalysisService cetStatisticAnalysisService;

    /**
     * cet———统计
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "cet———统计", response = Void.class, notes = "cet———统计<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getStatisticCet(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "grade 学年" , required = true) @RequestParam(value = "grade", required = true) String grade,
            @ApiParam(value = "semester 学期" , required = true) @RequestParam(value = "semester", required = true) Integer semester
//            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
//            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        return cetStatisticAnalysisService.getStatistic(orgId, grade, semester);
    }

    /**
     * cet———趋势分析
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/analysis", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "cet———趋势分析", response = Void.class, notes = "cet———趋势分析<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getCetTrendAnalysis(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 机构id", required = false) @RequestParam(value = "collegeId", required = false) Long collegeId,
            @ApiParam(value = "type 分析指标：4:四级通过率，6:六级通过率" , required = true)
            @RequestParam(value = "type", required = true) Integer type) {
        return cetStatisticAnalysisService.getCetTrendAnalysis(orgId, collegeId, type);
    }

    /**
     * cet———详情列表
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "cet———详情列表", response = Void.class, notes = "cet———详情列表<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getCetDetail(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 机构id 注:多个时候用“，”隔开", required = false) @RequestParam(value = "collegeId", required = false) String collegeId,
            @ApiParam(value = "grade 年级 注:多个时候用“，”隔开", required = false) @RequestParam(value = "grade", required = false) String grade,
            @ApiParam(value = "type 成绩类型：4:四级通过率，6:六级通过率" , required = false) @RequestParam(value = "type", required = false) Integer type,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return cetStatisticAnalysisService.getCetDetail(orgId, collegeId, grade, type, PageUtil.createNoErrorPageRequest(pageNumber, pageSize));
    }





}
