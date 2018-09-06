package com.aizhixin.cloud.dataanalysis.dd.rollcall.controller;


import com.aizhixin.cloud.dataanalysis.dd.rollcall.service.RollcallService;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.SchoolWeekRollcallScreenZhVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rollcall")
@Api(description = "点点考勤数据统计查询API")
public class RollcallController {

    @Autowired
    private RollcallService rollcallService;


    @GetMapping(value = "/screen/lastestweek", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "大屏最新周考勤数据统计", response = Void.class, notes = "大屏最新周考勤数据统计<br><br><b>@author zhen.pan</b>")
    public SchoolWeekRollcallScreenZhVO lastestweek(@ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId") Long orgId) {
        return  rollcallService.queryLastestWeekRollcall(orgId);
    }
}
