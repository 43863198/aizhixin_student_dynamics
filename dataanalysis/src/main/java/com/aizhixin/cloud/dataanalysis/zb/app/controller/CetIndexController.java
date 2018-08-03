package com.aizhixin.cloud.dataanalysis.zb.app.controller;


import com.aizhixin.cloud.dataanalysis.zb.app.service.CetLevelTestService;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.EnglishLevelBigScreenVO;
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
@RequestMapping("/v1/index/cet")
@Api(description = "英语等级考试指标应用")
public class CetIndexController {

    @Autowired
    private CetLevelTestService cetLevelTestService;

    @GetMapping(value = "/pass", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级英语等级考试大屏通过率", response = Void.class, notes = "三、四、六级英语等级考试大屏通过率<br><br><b>@author zhen.pan</b>")
    public List<EnglishLevelBigScreenVO> cet(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        return cetLevelTestService.getCetLevelBigScreenPass(orgId);
    }
}
