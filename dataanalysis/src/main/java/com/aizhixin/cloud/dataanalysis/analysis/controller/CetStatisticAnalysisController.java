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
    public Map<String,Object> getStatisticNewstudents(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "grade 学年" , required = true) @RequestParam(value = "grade", required = true) String grade,
            @ApiParam(value = "semester 学期" , required = true) @RequestParam(value = "semester", required = true) String semester,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return cetStatisticAnalysisService.getStatistic(orgId, grade, semester, PageUtil.createNoErrorPageRequest(pageNumber, pageSize));
    }



}
