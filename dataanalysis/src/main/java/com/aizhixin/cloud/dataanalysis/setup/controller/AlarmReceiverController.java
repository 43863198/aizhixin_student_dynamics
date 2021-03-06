package com.aizhixin.cloud.dataanalysis.setup.controller;

import com.aizhixin.cloud.dataanalysis.common.domain.MessageVO;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmReceiver;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmReceiverService;
import com.aizhixin.cloud.dataanalysis.setup.vo.AlertReceiverVO;
import com.aizhixin.cloud.dataanalysis.setup.vo.CollegeAlertReceiverVO;
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
@RequestMapping("/v1/org/{orgId}/alarmreceiver/")
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
            @ApiParam(value = "orgId", required = true) @PathVariable Long orgId,
            @ApiParam(value = "<b>必填:</b><br>collegeId:学院id;<br><b>" +
                    "</b><br>collegeName:学院名称;" +
                    "</b><br>teacherId:老师ID;" +
                    "</b><br>teacherName:老师姓名;" +
                    "</b><br>teacherJobNumber:老师工号;" +
                    "</b><br>teacherPhone:老师接收短信手机号码;<br><b>"
            )
            @RequestBody AlertReceiverVO alertReceiverVO) {
        MessageVO vo = new MessageVO();
        AlarmReceiver alarmReceiver = alarmReceiverService.save(orgId, alertReceiverVO);
        vo.setData(alarmReceiver.getId());
        return vo;
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "修改预警信息接收老师信息", response = Void.class, notes = "修改预警信息接收老师信息<br><br><b>@author zhen.pan</b>")
    public MessageVO update(
            @ApiParam(value = "orgId", required = true) @PathVariable Long orgId,
            @ApiParam(value = "ID", required = true) @PathVariable String id,
            @ApiParam(value = "<b>必填:</b><br>collegeId:学院id;<br><b>" +
                    "</b><br>collegeName:学院名称;" +
                    "</b><br>teacherId:老师ID;" +
                    "</b><br>teacherName:老师姓名;" +
                    "</b><br>teacherJobNumber:老师工号;" +
                    "</b><br>teacherPhone:老师接收短信手机号码;<br><b>"
            )
            @RequestBody AlertReceiverVO alertReceiverVO) {
        MessageVO vo = new MessageVO();
        AlarmReceiver alarmReceiver = alarmReceiverService.update(orgId, id, alertReceiverVO);
        vo.setData(alarmReceiver.getId());
        return vo;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取预警信息接收老师信息", response = Void.class, notes = "获取预警信息接收老师信息<br><br><b>@author zhen.pan</b>")
    public AlertReceiverVO get(
            @ApiParam(value = "orgId", required = true) @PathVariable Long orgId,
            @ApiParam(value = "ID", required = true) @PathVariable String id) {
        return alarmReceiverService.get(id);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "DELETE", value = "获取预警信息接收老师信息", response = Void.class, notes = "获取预警信息接收老师信息<br><br><b>@author zhen.pan</b>")
    public MessageVO delete(
            @ApiParam(value = "orgId", required = true) @PathVariable Long orgId,
            @ApiParam(value = "ID", required = true) @PathVariable String id) {
        MessageVO vo = new MessageVO();
        AlarmReceiver alarmReceiver = alarmReceiverService.delete(id);

        if (null != vo) {
            vo.setData(alarmReceiver.getId());
        }
        return vo;
    }

    @GetMapping(value = "/college/{collegeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "查询学院下所有接收预警信息老师信息", response = Void.class, notes = "查询学院下所有接收预警信息老师信息<br><br><b>@author zhen.pan</b>")
    public List<AlertReceiverVO> list(
            @ApiParam(value = "orgId", required = true) @PathVariable Long orgId,
            @ApiParam(value = "collegeId 学院ID", required = true) @PathVariable Long collegeId) {
        return alarmReceiverService.getByCollegeId(collegeId);
    }

    @GetMapping(value = "/college", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "查询学校下所有学院及预警老师数量统计信息", response = Void.class, notes = "查询学校下所有学院及预警老师数量统计信息<br><br><b>@author zhen.pan</b>")
    public List<CollegeAlertReceiverVO> getAllCollege(
            @ApiParam(value = "orgId", required = true) @PathVariable Long orgId) {
        return alarmReceiverService.getCollegeAndReceiveCount(orgId);
    }

    @PutMapping(value = "/msg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "查询告警数据并发送告警短信", response = Void.class, notes = "查询告警数据并发送告警短信<br><br><b>@author zhen.pan</b>")
    public MessageVO setMsg(
            @ApiParam(value = "orgId 学校ID", required = true) @PathVariable Long orgId,
            @ApiParam(value = "teacherYear 学年") @RequestParam(value = "teacherYear", required = false) String teacherYear,
            @ApiParam(value = "semester 学期") @RequestParam(value = "semester", required = false) String semester,
            @ApiParam(value = "type 类型") @RequestParam(value = "type", required = false) String type) {
        alarmReceiverService.sendMsg(orgId, teacherYear, semester, type);
        MessageVO vo = new MessageVO();
        return vo;
    }
}










