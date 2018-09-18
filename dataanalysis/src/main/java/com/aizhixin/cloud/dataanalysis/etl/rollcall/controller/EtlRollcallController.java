package com.aizhixin.cloud.dataanalysis.etl.rollcall.controller;


import com.aizhixin.cloud.dataanalysis.etl.rollcall.service.EtlRollcallAlertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/v1/etl/dd/rollcall")
@Api(description = "点点考勤数据清洗计算")
public class EtlRollcallController {

    @Autowired
    private EtlRollcallAlertService etlRollcallAlertService;

    @PutMapping(value = "/student/alert/lastest3", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "学生最近3天考勤预警数据生成", response = Void.class, notes = "学生最近3天考勤预警数据生成<br><br><b>@author zhen.pan</b>")
    public void calLastest3StudentRollAlert(@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = false) Long orgId,
                                  @ApiParam(value = "起始日期 yyyy-MM-dd")@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "start", required = false) Date start,
                                  @ApiParam(value = "结束日期 yyyy-MM-dd")@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "end", required = false) Date end,
                                  @ApiParam(value = "dkl 到课率阀值(缺省0.8)") @RequestParam(value = "dkl", required = false) Double dkl) {
        etlRollcallAlertService.calLastest3StudentRollAlert(orgId, start, end, dkl);
    }
}
