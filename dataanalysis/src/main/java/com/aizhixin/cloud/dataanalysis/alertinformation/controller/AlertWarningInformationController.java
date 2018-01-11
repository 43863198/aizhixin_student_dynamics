package com.aizhixin.cloud.dataanalysis.alertinformation.controller;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningDetailsDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AlertInforQueryDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;

import java.awt.print.Pageable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-15
 */
@RestController
@RequestMapping("/v1/alertwarninginfo")
@Api(description = "预警信息API")
public class AlertWarningInformationController {

    @Autowired
    private AlertWarningInformationService alertWarningInforService;

    /**
     * 预警信息列表
     *
     * @param orgId
     * @param collegeId
     * @param type
     * @param warningLevel
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/getlist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "预警信息列表", response = Void.class, notes = "预警信息列表<br><br><b>@author jianwei.wu</b>")
    public PageData<WarningDetailsDTO> getWarningInforList(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "collegeId 学院id") @RequestParam(value = "collegeId", required = false) Long collegeId,
            @ApiParam(value = "type 预警类型") @RequestParam(value = "type", required = false) String  type,
            @ApiParam(value = "warningLevel 预警等级") @RequestParam(value = "warningLevel", required = false) String warningLevel,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return alertWarningInforService.findPageWarningInfor(PageUtil.createNoErrorPageRequest(pageNumber, pageSize),orgId,collegeId,type,warningLevel);
    }

    /**
     * 预警级别汇总
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statisticalgrade", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "预警级别汇总", response = Void.class, notes = "预警级别汇总<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getStatisticalGrade(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return alertWarningInforService.getStatisticalGrade(orgId);
    }

    /**
     * 预警汇总
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statistical", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "预警汇总", response = Void.class, notes = "预警汇总<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getStatistical(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return alertWarningInforService.getStatistical(orgId);
    }


    /**
     * 院系预警汇总
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statisticalcollege", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "院系预警汇总", response = Void.class, notes = "院系预警汇总<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getStatisticalCollege(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return alertWarningInforService.getStatisticalCollege(orgId);
    }

    /**
     * 院系处理率top---10
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/collegeprocessedratio", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "院系处理率top---10", response = Void.class, notes = "院系处理率top---10<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getCollegeProcessedRatio(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return alertWarningInforService.getCollegeProcessedRatio(orgId);
    }


    /**
     * 预警分类汇总
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statisticaltype", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "预警分类汇总", response = Void.class, notes = "预警分类汇总<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getStatisticalType(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return alertWarningInforService.getStatisticalType(orgId);
    }


    /**
     按照学院统计每个预警级别的数量
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statisticalcollegetype", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "按照学院统计每个预警级别的数量", response = Void.class, notes = "按照学院统计每个预警级别的数量<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getStatisticalCollegeType(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "type 预警类型" , required = true) @RequestParam(value = "type", required = true) String type){
        return alertWarningInforService.getStatisticalCollegeType(orgId, type);
    }

    /**
     * 最新預警的20位學生信息
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/latestinformation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "最新預警的20位學生信息", response = Void.class, notes = "最新預警的20位學生信息<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>  getLatestinformation(
            @ApiParam(value = "orgId 学校id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId){
        return alertWarningInforService.getLatestinformation(orgId);
    }

    /**
     * 预警详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "预警详情", response = Void.class, notes = "预警详情<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getWarningDetails(
            @ApiParam(value = "id 预警id" , required = true) @RequestParam(value = "id", required = true) String id){
        return alertWarningInforService.getWarningDetails(id);
    }


    /**
     * 预警统计
     * @param orgId
     * @return
     */
    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "预警统计", response = Void.class, notes = "预警统计<br><br><b>@author jianwei.wu</b>")
    public Map<String,Object>   getStatisticsByCollege(
            @ApiParam(value = "orgId 机构id" , required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "teacherYear 学年" , required = true) @RequestParam(value = "teacherYear", required = true) Integer teacherYear,
            @ApiParam(value = "semester 学期" , required = true) @RequestParam(value = "semester", required = true) Integer semester,
            @ApiParam(value = "type 类型",  required = false ) @RequestParam(value = "type", required = false) String type,
            @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return alertWarningInforService.getStatisticsByCollege(PageUtil.createNoErrorPageRequest(pageNumber, pageSize), orgId, type, teacherYear, semester);
    }

    @RequestMapping(value = "/registercount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "按机构id统计机构下所有学院的注册报到预警数量", response = Void.class, notes = "按机构id统计机构下所有学院的注册报到预警数量<br><br><b>@author 郑宁</b>")
    public ResponseEntity<Map<String, Object>> registerCount(
//            @RequestHeader("Authorization") String token,
            @ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId
    ) {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put(ApiReturnConstants.DATA, alertWarningInforService.findRegisterCountInfor(orgId));
        return  new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }


    @RequestMapping(value = "/alertpage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "按条件分页查询预警信息", response = Void.class, notes = "按条件分页查询预警信息<br><br><b>@author 郑宁</b>")
    public ResponseEntity<Map<String, Object>> alertPage(
//            @RequestHeader("Authorization") String token,
            @ApiParam(value = "<b>必填:、</b><br>orgId:机构id<br>" +
                                         "<br>teacherYear:学年<br>" +
                                         "<br>semester:学期<br>" +
                    "<b>选填:、</b><br>collogeIds:学院id(字符串多个以,分隔);warningTypes:预警类型(字符串多个以,分隔);warningLevels:预警等级(字符串多个以,分隔);<br>collegeId:院系id、<br>")  @RequestBody AlertInforQueryDomain domain
    ) {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put(ApiReturnConstants.DATA, alertWarningInforService.getAlertInforPage(domain));
        return  new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }

}