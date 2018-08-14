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

    @GetMapping(value = "/lj/orgcount", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级累计单位统计", response = Void.class, notes = "三、四、六级累计单位统计<br><br><b>@author zhen.pan</b>")
    public DwLjCountVO cetOrgLjCount(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                     @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
                                     @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                                     @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
                                     @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findDwLjCount(orgId, cetType, collegeCode, professionCode, className);
    }

    @GetMapping(value = "/lj/suborgrs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级累计子单位人数分布", response = Void.class, notes = "三、四、六级累计子单位人数分布<br><br><b>@author zhen.pan</b>")
    public List<DwDistributeCountVO> cetOrgLjDistr(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                   @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
                                                   @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                                                   @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
                                                   @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findSubDwLjCount(orgId, cetType, collegeCode, professionCode, className);
    }

    @GetMapping(value = "/lj/suborgavg", produces = MediaType.APPLICATION_JSON_VALUE)
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



    @GetMapping(value = "/dc/orgcount", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/dc/suborgrs", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/dc/suborgavg", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/dc/sexrs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级单次性别人数分布", response = Void.class, notes = "三、四、六级单次性别人数分布<br><br><b>@author zhen.pan</b>")
    public SexRsVO dcsexrs(
            @ApiParam(value = "xnxq 学年学期YYYY-CCCC-x", required = true) @RequestParam(value = "xnxq") String xnxq,
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
            @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findDwDcSexRsCount(xnxq, orgId, cetType, collegeCode, professionCode, className);
    }

    @GetMapping(value = "/dc/sexavg", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "三、四、六级单次性别均值分布", response = Void.class, notes = "三、四、六级单次性别均值分布<br><br><b>@author zhen.pan</b>")
    public SexAvgVO dcsexavg(
            @ApiParam(value = "xnxq 学年学期YYYY-CCCC-x", required = true) @RequestParam(value = "xnxq") String xnxq,
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
            @ApiParam(value = "cetType 成绩类型(3;4;6;)", required = true) @RequestParam(value = "cetType") String cetType,
            @ApiParam(value = "collegeCode 学院码") @RequestParam(value = "collegeCode", required = false) String collegeCode,
            @ApiParam(value = "professionCode 专业码") @RequestParam(value = "professionCode", required = false) String professionCode,
            @ApiParam(value = "className 班名") @RequestParam(value = "className", required = false) String className) {
        return cetBaseIndexService.findDwDcSexAvgCount(xnxq, orgId, cetType, collegeCode, professionCode, className);
    }

}
