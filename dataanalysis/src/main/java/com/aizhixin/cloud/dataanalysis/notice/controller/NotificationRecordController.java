package com.aizhixin.cloud.dataanalysis.notice.controller;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.domain.MessageVO;
import com.aizhixin.cloud.dataanalysis.notice.service.NotificationRecordService;
import com.aizhixin.cloud.dataanalysis.notice.vo.AlertContentVO;
import com.aizhixin.cloud.dataanalysis.notice.vo.NotificationRecordVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 通知相关API
 * @author zhen.pan
 */
@RestController
@RequestMapping("/v1/org/{orgId}/notification")
@Api(description = "通知相关API")
public class NotificationRecordController {
    private NotificationRecordService notificationRecordService;
    @Autowired
    public NotificationRecordController(NotificationRecordService notificationRecordService) {
        this.notificationRecordService = notificationRecordService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取特定类型告警内容及接收人信息", response = Void.class, notes = "获取特定类型告警内容及接收人信息<br><br><b>@author zhen.pan</b>")
    public List<AlertContentVO> all(
            @ApiParam(value = "orgId 学校ID", required = true) @PathVariable Long orgId,
            @ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear") String teacherYear,
            @ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester") String semester,
            @ApiParam(value = "type 告警类型", required = true) @RequestParam(value = "type") String type) {
        return notificationRecordService.getAlertMsg(orgId, teacherYear, semester, type);
    }

    @PostMapping(value = "/sendmutil", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "手动发送多个学院的告警短信", response = Void.class, notes = "手动发送多个学院的告警短信<br><br><b>@author zhen.pan</b>")
    public MessageVO send(
            @ApiParam(value = "orgId 学校ID", required = true) @PathVariable Long orgId,
            @ApiParam(value = "alertContentVOList 告警内容", required = true) @RequestBody List<AlertContentVO> alertContentVOList) {
        return notificationRecordService.send(orgId, alertContentVOList);
    }

    @GetMapping(value = "/college", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取特定类型学院告警内容信息", response = Void.class, notes = "获取特定类型学院告警内容信息<br><br><b>@author zhen.pan</b>")
    public AlertContentVO collegeAlertContent(
            @ApiParam(value = "orgId 学校ID", required = true) @PathVariable Long orgId,
            @ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear") String teacherYear,
            @ApiParam(value = "semester 学期", required = true) @RequestParam(value = "semester") String semester,
            @ApiParam(value = "type 告警类型", required = true) @RequestParam(value = "type") String type,
            @ApiParam(value = "collegeName 学院名称", required = true) @RequestParam(value = "collegeName") String collegeName) {
        return notificationRecordService.getCollegeAlertMsg(orgId, teacherYear, semester, type, collegeName);
    }


    @PostMapping(value = "/sendone", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "手动发送单个学院的告警短信", response = Void.class, notes = "手动发送单个学院的告警短信<br><br><b>@author zhen.pan</b>")
    public MessageVO send(
            @ApiParam(value = "orgId 学校ID", required = true) @PathVariable Long orgId,
            @ApiParam(value = "alertContentVOList 告警内容", required = true) @RequestBody AlertContentVO alertContentVO) {
        return notificationRecordService.send(orgId, alertContentVO);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "查询通知记录", response = Void.class, notes = "查询通知记录<br><br><b>@author zhen.pan</b>")
    public PageData<NotificationRecordVO> list(
            @ApiParam(value = "orgId 学校ID", required = true) @PathVariable Long orgId,
            @ApiParam(value = "collegeName 学院名称") @RequestParam(value = "collegeName", required = false) String collegeName,
            @ApiParam(value = "receiver 接收人") @RequestParam(value = "receiver", required = false) String receiver,
            @ApiParam(value = "rs 发送结果（10 成功，20 失败)") @RequestParam(value = "rs", required = false) Integer rs,
            @ApiParam(value = "startDate 起始日期(yyyy-MM-dd)") @RequestParam(value = "startDate", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") Date startDate,
            @ApiParam(value = "endDate 结束日期(yyyy-MM-dd)") @RequestParam(value = "endDate", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") Date endDate,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return notificationRecordService.list(orgId, collegeName, rs, receiver, startDate, endDate, pageNumber, pageSize);
    }
}










