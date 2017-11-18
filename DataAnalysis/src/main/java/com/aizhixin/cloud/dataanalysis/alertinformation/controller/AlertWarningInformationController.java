package com.aizhixin.cloud.dataanalysis.alertinformation.controller;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AlertWarningInformation;
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
    public PageData<AlertWarningInformation> getWarningInforList(
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


    @RequestMapping(value = "/registercount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "按机构id统计机构下所有学院的注册报到预警数量", response = Void.class, notes = "按机构id统计机构下所有学院的注册报到预警数量<br><br><b>@author 郑宁</b>")
	public ResponseEntity<Map<String, Object>> registerCount(
			@RequestHeader("Authorization") String token,
            @ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId
			) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put(ApiReturnConstants.DATA, alertWarningInforService.findRegisterCountInfor(orgId));
		return  new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

    
    @RequestMapping(value = "/alertpage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  	@ApiOperation(httpMethod = "GET", value = "按条件分页查询预警信息", response = Void.class, notes = "按条件分页查询预警信息<br><br><b>@author 郑宁</b>")
  	public ResponseEntity<Map<String, Object>> alertPage(
  			@RequestHeader("Authorization") String token,
  			@ApiParam(value = "<b>必填:、</b><br>orgId:机构id<br><b>选填:、</b><br>collogeIds:学院id(字符串多个以,分隔);warningTypes:预警类型(字符串多个以,分隔);warningLevels:预警等级(字符串多个以,分隔);<br>collegeId:院系id、<br>")  @RequestBody AlertInforQueryDomain domain
  			) {
  		Map<String, Object> result = new HashMap<String, Object>();
  		
  		result.put(ApiReturnConstants.DATA, alertWarningInforService.getAlertInforPage(domain));
  		return  new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
  	}

}
