package com.aizhixin.cloud.dataanalysis.setup.controller;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningSettingsDTO;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
@RestController
@RequestMapping("/v1/alarmsettings")
@Api(description = "预警设置API")
public class AlarmSettingsController {
    @Autowired
    private AlarmSettingsService alarmSettingsService;

    /**
     * 预警类型列表
     * @param orgId
     * @return
     */
    @GetMapping(value = "/getwarningtypelist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "预警类型列表", response = Void.class, notes = "预警类型列表<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getWarningTypeList(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return alarmSettingsService.getWarningTypeList(orgId);
    }

    /**
     * 已设置的预警参数信息
     * @param warningTypeId
     * @return
     */
    @GetMapping(value = "/getwarningset", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "已设置的预警参数信息", response = Void.class, notes = "已设置的预警参数信息<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> getWarningSet(
            @ApiParam(value = "warningTypeId 预警类型Id" , required = true) @RequestParam(value = "warningTypeId", required = true) String warningTypeId
            ){
        return alarmSettingsService.getWarningSet(warningTypeId);
    }


    /**
     * 预警参数设置
     * @param warningSettingsDTO
     * @return
     */
    @PostMapping(value = "/warningset", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "预警参数设置", response = Void.class, notes = "预警参数设置<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object> warningSet(
            @ApiParam(value = "<b>必填:、</b><br>orgId:机构id<br><b>")
            @RequestBody WarningSettingsDTO warningSettingsDTO
    ){
        return alarmSettingsService.warningSet(warningSettingsDTO);
    }






}