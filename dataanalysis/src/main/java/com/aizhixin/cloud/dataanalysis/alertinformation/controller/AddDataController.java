package com.aizhixin.cloud.dataanalysis.alertinformation.controller;

import com.aizhixin.cloud.dataanalysis.setup.entity.Rule;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.service.RuleService;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-04
 */
@RestController
@RequestMapping("/v1/base")
@Api(description = "添加基础数据API")
public class AddDataController {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private WarningTypeService warningTypeService;


    @GetMapping(value = "/addwaringtype", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "添加预警类型数据", response = Void.class, notes = "添加测试预警信息数据<br><br><b>@author jianwei.wu</b>")
    public  String setWarningType(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId) {
     return warningTypeService.setWarningType(orgId);
    }
}
