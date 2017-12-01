package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-29
 */
@RestController
@RequestMapping("/v1/welcome")
@Api(description = "学校概况统计API")
public class SchoolConditionStatisticsController {
    @Autowired
    private SchoolStatisticsService schoolStatisticsService;
    @GetMapping(value = "/getSchoolPersonStatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "全校人数统计信息", response = Void.class, notes = "全校人数统计信息<br><br><b>@author 王俊</b>")
    public SchoolProfileDTO getSchoolPersonStatistics(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return schoolStatisticsService.getSchoolPersonStatistics(orgId);
    }

}
