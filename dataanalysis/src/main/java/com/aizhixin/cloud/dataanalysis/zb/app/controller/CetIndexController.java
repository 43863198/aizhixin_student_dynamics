package com.aizhixin.cloud.dataanalysis.zb.app.controller;


import com.aizhixin.cloud.dataanalysis.zb.app.service.CetLevelTestService;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.EnglishLevelBigScreenVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/index/cet")
@Api(description = "英语等级考试指标应用")
public class CetIndexController {

    @Autowired
    private CetLevelTestService cetLevelTestService;

    @GetMapping(value = "/pass", produces = MediaType.APPLICATION_JSON_VALUE)

    @ApiOperation(httpMethod = "GET", value = "三、四、六级英语等级考试大屏通过率", response = Void.class, notes = "三、四、六级英语等级考试大屏通过率<br><br><b>@author zhen.pan</b>")
    public List<EnglishLevelBigScreenVO> cet(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId) {
        return cetLevelTestService.getCetLevelBigScreenPass(orgId);
    }


    @GetMapping(value = "/currentstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "等级考试当前状况---数据统计", response = Void.class, notes = "等级考试当前状况---数据统计<br><br><b>@author dengchao</b>")
    public ResponseEntity<Map<String, Object>> currentStatistics(
            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.currentStatistics(orgId, cetType, collegeCode, professionCode, className), HttpStatus.OK);
    }

    @GetMapping(value = "/organizationstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试当前状况---人数分布", response = Void.class, notes = "英语考试当前状况---人数分布<br><br><b>@author dengchao</b>")
    public ResponseEntity<Map<String, Object>> organizationStatistics(
            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.organizationStatistics(orgId, cetType, collegeCode, professionCode, className), HttpStatus.OK);
    }

    @GetMapping(value = "/detaillist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "查看数据详情---数据列表", response = Void.class, notes = "英语考试查看数据详情---数据列表<br><br><b>@author dengchao</b>")
    public ResponseEntity<Object> getDetailList(
            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (三级;四级;六级;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班级名称", required = false) @RequestParam(value = "className", required = false) String className,
            @ApiParam(value = "nj 姓名或学号", required = false) @RequestParam(value = "nj", required = false) String nj,
            @ApiParam(value = "isPass 是否通过（1:通过;0:未通过）", required = false) @RequestParam(value = "isPass", required = false) String isPass,
            @ApiParam(value = "scoreSeg 成绩段 1(0-390) 2(390-425) 3(425,550), 4(550及以上)", required = false) @RequestParam(value = "scoreSeg", required = false) Integer scoreSeg,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return new ResponseEntity<Object>(cetLevelTestService.getDetailList(orgId, cetType, collegeCode, professionCode, className, nj, isPass, scoreSeg, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping(value = "/organizationstatistics/avg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "英语考试当前状况---均值分布", response = Void.class, notes = "英语考试当前状况---均值分布---按行政班<br><br><b>@author dengchao</b>")
    public ResponseEntity<Map<String, Object>> organizationStatisticsAvg(
            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班级名称", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.avg(orgId, cetType, collegeCode, professionCode, className), HttpStatus.OK);
    }

    @GetMapping(value = "/organizationstatistics/over/year", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "历年数据分析---通过率趋势分析", response = Void.class, notes = "历年数据分析---通过率趋势分析<br><br><b>@author dengchao</b>")
    public ResponseEntity<Map<String, Object>> organizationStatisticsOverYear(
            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班级名称", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.OverYearsPassRate(orgId, cetType, collegeCode, professionCode, className), HttpStatus.OK);
    }

    @GetMapping(value = "/organizationstatistics/over/year/avg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "历年数据分析---成绩均值趋势分析", response = Void.class, notes = "历年数据分析---成绩均值趋势分析<br><br><b>@author dengchao</b>")
    public ResponseEntity<Map<String, Object>> organizationStatisticsOverYearAvg(
            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班级名称", required = false) @RequestParam(value = "className", required = false) String className
    ) {
        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.OverYearsAvgScore(orgId, cetType, collegeCode, professionCode, className), HttpStatus.OK);
    }


}
