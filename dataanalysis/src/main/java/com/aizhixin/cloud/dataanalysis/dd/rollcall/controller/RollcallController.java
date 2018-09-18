package com.aizhixin.cloud.dataanalysis.dd.rollcall.controller;


import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.service.RollcallService;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.ClassesRollcallAlertVO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.Lastest3RollcallAlertVO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.SchoolWeekRollcallScreenZhVO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.TeacherRollcallAlertVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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


    @GetMapping(value = "/alert/orgId/{orgId}/teacher", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "学校管理平台老师考勤告警查询", response = Void.class, notes = "学校管理平台老师考勤告警查询<br><br><b>@author zhen.pan</b>")
    public PageData<TeacherRollcallAlertVO> queryTeacherRollcallAlert(
            @ApiParam(value = "orgId 机构id" , required = true) @PathVariable Long orgId,
            @ApiParam(value = "collegeId 学院id") @RequestParam(value = "collegeId", required = false) Long collegeId,
            @ApiParam(value = "teacherId 老师id") @RequestParam(value = "teacherId", required = false) Long teacherId,
            @ApiParam(value = "起始日期 yyyy-MM-dd")@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "start", required = false) Date start,
            @ApiParam(value = "结束日期 yyyy-MM-dd")@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "end", required = false) Date end,
            @ApiParam(value = "dkl 到课率阀值(缺省0.8)") @RequestParam(value = "dkl", required = false) Double dkl,
            @ApiParam(value = "pageIndex 第几页") @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @ApiParam(value = "pageSize 每页条数") @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        return  rollcallService.queryTeacherRollcallAlert(orgId, collegeId, teacherId, start, end, dkl, pageIndex, pageSize);
    }


    @GetMapping(value = "/alert/orgId/{orgId}/classes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "学校管理平台班级考勤告警查询", response = Void.class, notes = "学校管理平台班级考勤告警查询<br><br><b>@author zhen.pan</b>")
    public PageData<ClassesRollcallAlertVO> queryClassesRollcallAlert(
            @ApiParam(value = "orgId 机构id" , required = true) @PathVariable Long orgId,
            @ApiParam(value = "collegeId 学院id") @RequestParam(value = "collegeId", required = false) Long collegeId,
            @ApiParam(value = "teacherId 老师id") @RequestParam(value = "teacherId", required = false) Long teacherId,
            @ApiParam(value = "起始日期 yyyy-MM-dd")@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "start", required = false) Date start,
            @ApiParam(value = "结束日期 yyyy-MM-dd")@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "end", required = false) Date end,
            @ApiParam(value = "dkl 到课率阀值(缺省0.8)") @RequestParam(value = "dkl", required = false) Double dkl,
            @ApiParam(value = "pageIndex 第几页") @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @ApiParam(value = "pageSize 每页条数") @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        return  rollcallService.queryClassesRollcallAlert(orgId, collegeId, teacherId, start, end, dkl, pageIndex, pageSize);
    }


    @GetMapping(value = "/alert/orgId/{orgId}/studentlastest3", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "学校管理平台最近3天学生考勤告警查询", response = Void.class, notes = "学校管理平台最近3天学生考勤告警查询<br><br><b>@author zhen.pan</b>")
    public PageData<Lastest3RollcallAlertVO> queryClassesRollcallAlert(
            @ApiParam(value = "orgId 机构id" , required = true) @PathVariable Long orgId,
            @ApiParam(value = "collegeId 学院id") @RequestParam(value = "collegeId", required = false) Long collegeId,
            @ApiParam(value = "name 学号或姓名") @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "起始日期 yyyy-MM-dd")@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "start", required = false) Date start,
            @ApiParam(value = "结束日期 yyyy-MM-dd")@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(value = "end", required = false) Date end,
            @ApiParam(value = "pageIndex 第几页") @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @ApiParam(value = "pageSize 每页条数") @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        return  rollcallService.findLastest3RollcallAlert(orgId, collegeId, name, start, end, pageIndex, pageSize);
    }
}
