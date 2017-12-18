package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.job.TeachingScoreAnalysisJob;
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
 * @Date: 2017-12-18
 */
@RestController
@RequestMapping("/v1/test")
@Api(description = "查看数据")
public class testController {
    @Autowired
    private TeachingScoreAnalysisJob teachingScoreAnalysisJob;

    @GetMapping(value = "/lookdata", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "查看mogo 成绩数据", response = Void.class, notes = "查看mogo 成绩数据<br><br><b>@author wangjun</b>")
    public Map<String,Object> getLookScore(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return teachingScoreAnalysisJob.getLookScore(orgId,null,2017,1);
    }

}
