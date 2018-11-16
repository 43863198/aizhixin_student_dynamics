package com.aizhixin.cloud.dataanalysis.etl.coursetimetable.controller;


import com.aizhixin.cloud.dataanalysis.etl.coursetimetable.service.SchoolCalendarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/etl/school/calendar")
@Api(description = "校历数据清洗")
public class EtlSchoolCalendarController {

    @Autowired
    private SchoolCalendarService schoolCalendarService;
    @PutMapping(value = "/etl", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "校历数据清洗到t_school_calendar表中", response = Void.class, notes = "校历数据清洗到t_school_calendar表中<br><br><b>@author zhen.pan</b>")
    public void etlCet(@ApiParam(value = "orgId 学校ID" , required = true) @RequestParam(value = "orgId") Long orgId) {
        schoolCalendarService.etlGuiliSchoolCalendar(orgId);
    }
}
