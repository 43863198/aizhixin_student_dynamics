package com.aizhixin.cloud.dataanalysis.zb.app.controller;


import com.aizhixin.cloud.dataanalysis.zb.app.service.CetBaseIndexService;
import com.aizhixin.cloud.dataanalysis.zb.app.service.CetLevelTestService;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/index/cet")
@Api(description = "英语等级考试指标应用")
public class CetIndexController {

    @Autowired
    private CetLevelTestService cetLevelTestService;
    @Autowired
    private CetBaseIndexService cetBaseIndexService;

    @GetMapping(value = "/pass", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级英语等级考试大屏通过率", response = Void.class, notes = "三、四、六级英语等级考试大屏通过率<br><br><b>@author zhen.pan</b>")
    public List<EnglishLevelBigScreenVO> cetPassAll(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId) {
        return cetLevelTestService.getCetLevelBigScreenPass(orgId);
    }

    @GetMapping(value = "/lj/cetorgcount", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级累计单位统计", response = Void.class, notes = "三、四、六级累计单位统计<br><br><b>@author zhen.pan</b>")
    public DwLjCountVO cetOrgLjCount(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                     @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
                                     @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                                     @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
                                     @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findDwLjCount(orgId, cetType, collegeCode, professionCode, className);
    }

    @GetMapping(value = "/lj/cetorgdistr", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级累计子单位人数分布", response = Void.class, notes = "三、四、六级累计子单位人数分布<br><br><b>@author zhen.pan</b>")
    public List<DwDistributeCountVO> cetOrgLjDistr(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                   @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
                                                   @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                                                   @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
                                                   @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findSubDwLjCount(orgId, cetType, collegeCode, professionCode, className);
    }

    @GetMapping(value = "/lj/cetorgavgdistr", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级累计子单位均值分布", response = Void.class, notes = "三、四、六级累计子单位均值分布<br><br><b>@author zhen.pan</b>")
    public List<DwDistributeCountVO> cetOrgLjAvgDistr(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                     @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
                                     @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                                     @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
                                     @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findSubDwLjCount(orgId, cetType, collegeCode, professionCode, className);
    }

    @GetMapping(value = "/lj/sexrs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级累计性别人数分布", response = Void.class, notes = "三、四、六级累计性别人数分布<br><br><b>@author zhen.pan</b>")
    public SexRsVO sexrs(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                         @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
                         @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                         @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
                         @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findDwLjSexRsCount(orgId, cetType, collegeCode, professionCode, className);
    }

    @GetMapping(value = "/lj/sexavg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级累计性别均值分布", response = Void.class, notes = "三、四、六级累计性别均值分布<br><br><b>@author zhen.pan</b>")
    public SexAvgVO sexavg(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                          @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
                          @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                          @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
                          @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findDwLjSexAvgCount(orgId, cetType, collegeCode, professionCode, className);
    }

    @GetMapping(value = "/lj/graders", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级累计年级人数分布", response = Void.class, notes = "三、四、六级累计年级人数分布<br><br><b>@author zhen.pan</b>")
    public List<CetGradeRsVo> graders(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                         @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
                         @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                         @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
                         @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findDwLjGradeRsCount(orgId, cetType, collegeCode, professionCode, className);
    }

    @GetMapping(value = "/lj/gradeavg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级累计年级均值分布", response = Void.class, notes = "三、四、六级累计年级均值分布<br><br><b>@author zhen.pan</b>")
    public List<CetGradeAvgVo> gradeavg(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
            @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findDwLjGradeAvgCount(orgId, cetType, collegeCode, professionCode, className);
    }



    @GetMapping(value = "/dc/cetorgcount", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级单次单位统计", response = Void.class, notes = "三、四、六级单次单位统计<br><br><b>@author zhen.pan</b>")
    public DwDcCountVO cetOrgDcCount(
            @ApiParam(value = "xnxq 学年学期YYYY-CCCC-x", required = true) @RequestParam(value = "xnxq") String xnxq,
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
            @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findDcDwCount(xnxq, orgId, cetType, collegeCode, professionCode, className);
    }

    @GetMapping(value = "/dc/cetorgdistr", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级单次子单位人数分布", response = Void.class, notes = "三、四、六级单次子单位人数分布<br><br><b>@author zhen.pan</b>")
    public List<DwDistributeCountVO> cetDcOrgDistri(
            @ApiParam(value = "xnxq 学年学期YYYY-CCCC-x", required = true) @RequestParam(value = "xnxq") String xnxq,
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
            @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findSubDwDcCount(xnxq, orgId, cetType, collegeCode, professionCode, className);
    }

    @GetMapping(value = "/dc/cetorgavgdistr", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级累计子单位均值分布", response = Void.class, notes = "三、四、六级累计子单位均值分布<br><br><b>@author zhen.pan</b>")
    public List<DwDistributeCountVO> cetDcOrgLjAvgDistr(
            @ApiParam(value = "xnxq 学年学期YYYY-CCCC-x", required = true) @RequestParam(value = "xnxq") String xnxq,
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
            @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findSubDwDcCount(xnxq, orgId, cetType, collegeCode, professionCode, className);
    }

//    @GetMapping(value = "/cetstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---数据统计", response = Void.class, notes = "等级考试单次考试统计---数据统计<br><br><b>@author dengchao</b>")
//    public ResponseEntity<Map<String, Object>> cetSingleDataStatistics(
//            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
//            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
//            @ApiParam(value = "teacherYear 学年 (格式如：2017)", required = true) @RequestParam(value = "teacherYear", required = true) String teacherYear,
//            @ApiParam(value = "semester 学期 (1,2; 1表示秋，2表示春)", required = true) @RequestParam(value = "semester", required = true) String semester,
//            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
//            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
//            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
//    ) {
//        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.cetSingleDataStatistics(orgId, cetType, teacherYear, semester, collegeCode, professionCode, className), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/avg", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---均值分布---按行政单位", response = Void.class, notes = "英语考试单次数据分析---均值分布---按行政单位<br><br><b>@author dengchao</b>")
//    public ResponseEntity<Map<String, Object>> cetSingleDataAvgScoure(
//            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
//            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
//            @ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) String teacherYear,
//            @ApiParam(value = "semester 学期 (1,2; 1表示秋，2表示春)", required = true) @RequestParam(value = "semester", required = true) String semester,
//            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
//            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
//            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
//    ) {
//        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.cetSingleDataAvgScoure(orgId, cetType, teacherYear, semester, collegeCode, professionCode, className), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/numberofpeople", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语考试单次数据分析---人数分布---按行政单位", response = Void.class, notes = "英语考试单次数据分析---人数分布---按行政单位<br><br><b>@author dengchao</b>")
//    public ResponseEntity<Map<String, Object>> cetSingleDataNumberOfPeople(
//            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
//            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
//            @ApiParam(value = "teacherYear 学年", required = true) @RequestParam(value = "teacherYear", required = true) String teacherYear,
//            @ApiParam(value = "semester 学期 (1,2; 1表示秋，2表示春)", required = true) @RequestParam(value = "semester", required = true) String semester,
//            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
//            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
//            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
//    ) {
//        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.cetSingleDataNumberOfPeople(orgId, cetType, teacherYear, semester, collegeCode, professionCode, className), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/currentstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语考试当前状况---数据统计", response = Void.class, notes = "等级考试当前状况---数据统计<br><br><b>@author dengchao</b>")
//    public ResponseEntity<Map<String, Object>> currentStatistics(
//            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
//            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
//            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
//            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
//            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
//    ) {
//        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.currentStatistics(orgId, cetType, collegeCode, professionCode, className), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/organizationstatistics", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语考试当前状况---人数分布", response = Void.class, notes = "英语考试当前状况---人数分布<br><br><b>@author dengchao</b>")
//    public ResponseEntity<Map<String, Object>> organizationStatistics(
//            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
//            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
//            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
//            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
//            @ApiParam(value = "className 班名", required = false) @RequestParam(value = "className", required = false) String className
//    ) {
//        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.organizationStatistics(orgId, cetType, collegeCode, professionCode, className), HttpStatus.OK);
//    }

    /*@GetMapping(value = "/detaillist", produces = MediaType.APPLICATION_JSON_VALUE)
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
    }*/

//    @GetMapping(value = "/organizationstatistics/avg", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "英语考试当前状况---均值分布", response = Void.class, notes = "英语考试当前状况---均值分布---按行政班<br><br><b>@author dengchao</b>")
//    public ResponseEntity<Map<String, Object>> organizationStatisticsAvg(
//            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
//            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
//            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
//            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
//            @ApiParam(value = "className 班级名称", required = false) @RequestParam(value = "className", required = false) String className
//    ) {
//        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.avg(orgId, cetType, collegeCode, professionCode, className), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/organizationstatistics/over/year", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "历年数据分析---通过率趋势分析", response = Void.class, notes = "历年数据分析---通过率趋势分析<br><br><b>@author dengchao</b>")
//    public ResponseEntity<Map<String, Object>> organizationStatisticsOverYear(
//            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
//            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
//            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
//            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
//            @ApiParam(value = "className 班级名称", required = false) @RequestParam(value = "className", required = false) String className
//    ) {
//        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.OverYearsPassRate(orgId, cetType, collegeCode, professionCode, className), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/organizationstatistics/over/year/avg", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "历年数据分析---成绩均值趋势分析", response = Void.class, notes = "历年数据分析---成绩均值趋势分析<br><br><b>@author dengchao</b>")
//    public ResponseEntity<Map<String, Object>> organizationStatisticsOverYearAvg(
//            @ApiParam(value = "orgId 学校id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
//            @ApiParam(value = "cetType 成绩类型： (3;4;6;)", required = true) @RequestParam(value = "cetType", required = true) String cetType,
//            @ApiParam(value = "collegeCode 学院码", required = false) @RequestParam(value = "collegeCode", required = false) String collegeCode,
//            @ApiParam(value = "professionCode 专业码", required = false) @RequestParam(value = "professionCode", required = false) String professionCode,
//            @ApiParam(value = "className 班级名称", required = false) @RequestParam(value = "className", required = false) String className
//    ) {
//        return new ResponseEntity<Map<String, Object>>(cetLevelTestService.OverYearsAvgScore(orgId, cetType, collegeCode, professionCode, className), HttpStatus.OK);
//    }


}
