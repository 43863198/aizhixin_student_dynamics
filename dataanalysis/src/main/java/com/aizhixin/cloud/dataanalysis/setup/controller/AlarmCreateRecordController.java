package com.aizhixin.cloud.dataanalysis.setup.controller;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmCreateRecord;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmCreateRecordService;
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
 * 告警日志功能
 */
@RestController
@RequestMapping("/v1/alarmlog")
@Api(description = "预警设置API")
public class AlarmCreateRecordController {
    @Autowired
    private AlarmCreateRecordService alarmCreateRecordService;

    /**
     * 告警日志查询
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "告警日志分页查询，最新在最前", response = Void.class, notes = "预警类型列表<br><br><b>@author jianwei.wu</b>")
    public PageData<AlarmCreateRecord> getWarningTypeList(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "userId 登录用户id") @RequestParam(value = "userId", required = false) Long userId,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return alarmCreateRecordService.findByOrgIdAndCreatedId(orgId, userId, pageNumber, pageSize);
    }
}










