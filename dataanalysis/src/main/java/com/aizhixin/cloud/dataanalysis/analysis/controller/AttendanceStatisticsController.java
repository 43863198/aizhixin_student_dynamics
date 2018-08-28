package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.domain.AttendanceStatisticsCourseDomain;
import com.aizhixin.cloud.dataanalysis.analysis.service.AttendanceStatisticsService;
import com.aizhixin.cloud.dataanalysis.analysis.vo.AttendanceStatisticsVO;
import com.aizhixin.cloud.dataanalysis.common.PageData;
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
@RequestMapping("/v1/attendanceanalysis")
@Api(description = "考勤学情分析")
public class AttendanceStatisticsController {
    @Autowired
    private AttendanceStatisticsService attendanceStatisticsService;

    @GetMapping(value = "/statisticsByUnit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "按单位统计考勤数据", response = Void.class, notes = "按单位统计考勤数据<br><br><b>@author dengchao</b>")
    public AttendanceStatisticsVO list(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                       @ApiParam(value = "collegeCode 学院code") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                                       @ApiParam(value = "professionCode 专业code") @RequestParam(value = "professionCode", required = false) String professionCode,
                                       @ApiParam(value = "dateRange 时间区间(7:最近7天; 30:最近30天; 2015-08-19~2015-09-17:2015年月19日至2015年9月17日)", required = true) @RequestParam(value = "dateRange") String dateRange) {
        {
            return attendanceStatisticsService.statisticsByUnit(orgId, collegeCode, professionCode, dateRange);
        }

    }

    @GetMapping(value = "/statisticsByUnit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "按课程统计考勤数据", response = Void.class, notes = "按单位统计考勤数据<br><br><b>@author dengchao</b>")
    public PageData<AttendanceStatisticsCourseDomain> list(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                           @ApiParam(value = "kchOrkcmc 课程号或课程名称") @RequestParam(value = "kchOrkcmc", required = false) String kchOrkcmc,
                                                           @ApiParam(value = "dateRange 时间区间(7:最近7天; 30:最近30天; 2015-08-19~2015-09-17:2015年月19日至2015年9月17日)", required = true) @RequestParam(value = "dateRange") String dateRange,
                                                           @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                           @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        {
            return attendanceStatisticsService.statisticsByCourse(orgId, kchOrkcmc, dateRange,  pageNumber,  pageSize);
        }

    }


}
