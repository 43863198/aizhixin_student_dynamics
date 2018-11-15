package com.aizhixin.cloud.dataanalysis.etl.coursetimetable.controller;


import com.aizhixin.cloud.dataanalysis.etl.coursetimetable.service.SemesterCourseTimeTableService;
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
@RequestMapping("/v1/etl/timetable")
@Api(description = "课程表数据清洗")
public class EtlSemesterCourseTimeTableController {

    @Autowired
    private SemesterCourseTimeTableService semesterCourseTimeTableService;
    @PutMapping(value = "/etl", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "英语等级考试数据库对数据库标准数据清洗ETL", response = Void.class, notes = "英语等级考试数据库对数据库标准数据清洗ETL<br><br><b>@author zhen.pan</b>")
    public void etlCet(@ApiParam(value = "xn 学年" , required = true) @RequestParam(value = "xn") String xn,
                       @ApiParam(value = "xq 学期(1,2表示春和秋)" , required = true) @RequestParam(value = "xq") String xq) {
        semesterCourseTimeTableService.etlGuiliPkxx(xn, xq);
    }
}
