package com.aizhixin.cloud.dataanalysis.setup.controller;

import com.aizhixin.cloud.dataanalysis.common.domain.MessageVO;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmReceiver;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmReceiverService;
import com.aizhixin.cloud.dataanalysis.setup.vo.AlertReceiverVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 告警信息接收人相关API
 * @author zhen.pan
 */
@RestController
@RequestMapping("/v1/alarmreceiver")
@Api(description = "告警信息接收人相关API")
public class AlarmReceiverController {
    private AlarmReceiverService alarmReceiverService;
    @Autowired
    public AlarmReceiverController (AlarmReceiverService alarmReceiverService) {
        this.alarmReceiverService = alarmReceiverService;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "保存预警信息接收老师信息", response = Void.class, notes = "保存预警信息接收老师信息<br><br><b>@author zhen.pan</b>")
    public MessageVO save(
            @ApiParam(value = "<b>必填:</b><br>collegeId:学院id;<br><b>" +
                    "</b><br>teacherId:老师ID;" +
                    "</b><br>teacherName:老师姓名;" +
                    "</b><br>teacherJobNumber:老师工号;" +
                    "</b><br>teacherPhone:老师接收短信手机号码;<br><b>"
            )
            @RequestBody AlertReceiverVO alertReceiverVO) {
        MessageVO vo = new MessageVO();
        AlarmReceiver alarmReceiver = alarmReceiverService.save(alertReceiverVO);
        vo.setMessage(alarmReceiver.getId());
        return vo;
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "修改预警信息接收老师信息", response = Void.class, notes = "修改预警信息接收老师信息<br><br><b>@author zhen.pan</b>")
    public MessageVO update(
            @ApiParam(value = "ID", required = true) @PathVariable String id,
            @ApiParam(value = "<b>必填:</b><br>collegeId:学院id;<br><b>" +
                    "</b><br>teacherId:老师ID;" +
                    "</b><br>teacherName:老师姓名;" +
                    "</b><br>teacherJobNumber:老师工号;" +
                    "</b><br>teacherPhone:老师接收短信手机号码;<br><b>"
            )
            @RequestBody AlertReceiverVO alertReceiverVO) {
        MessageVO vo = new MessageVO();
        AlarmReceiver alarmReceiver = alarmReceiverService.update(id, alertReceiverVO);
        vo.setMessage(alarmReceiver.getId());
        return vo;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取预警信息接收老师信息", response = Void.class, notes = "获取预警信息接收老师信息<br><br><b>@author zhen.pan</b>")
    public AlertReceiverVO get(
            @ApiParam(value = "ID", required = true) @PathVariable String id) {
        return alarmReceiverService.get(id);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "DELETE", value = "获取预警信息接收老师信息", response = Void.class, notes = "获取预警信息接收老师信息<br><br><b>@author zhen.pan</b>")
    public MessageVO delete(
            @ApiParam(value = "ID", required = true) @PathVariable String id) {
        MessageVO vo = new MessageVO();
        AlarmReceiver alarmReceiver = alarmReceiverService.delete(id);

        if (null != vo) {
            vo.setMessage(alarmReceiver.getId());
        }
        return vo;
    }

    @GetMapping(value = "/college/{collegeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "查询学院下所有接收预警信息老师信息", response = Void.class, notes = "查询学院下所有接收预警信息老师信息<br><br><b>@author zhen.pan</b>")
    public List<AlertReceiverVO> list(
            @ApiParam(value = "collegeId 学院ID", required = true) @PathVariable Long collegeId) {
        return alarmReceiverService.getByCollegeId(collegeId);
    }
}










