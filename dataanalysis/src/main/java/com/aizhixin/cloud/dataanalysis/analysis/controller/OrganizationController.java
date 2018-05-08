package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.service.OrganizationService;
import com.aizhixin.cloud.dataanalysis.feign.OrgManagerFeignService;
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

import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-06
 */
@RestController
@RequestMapping("/v1/org")
@Api(description = "组织机构相关API")
public class OrganizationController{
    @Autowired
    private OrganizationService organizationService;
        /**
         * 获取学院列表
         * @param orgId
         * @return
         */
        @GetMapping(value = "/getcollege", produces = MediaType.APPLICATION_JSON_VALUE)
        @ApiOperation(httpMethod = "GET", value = " 获取学院列表", response = Void.class, notes = "获取学院列表")
        public ResponseEntity<Map<String,Object>> getCollege(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId){
            return new ResponseEntity<Map<String, Object>>(organizationService.getCollege(orgId), HttpStatus.OK);
        }

    /**
     * 获取专业列表
     * @param orgId
     * @return
     */
    @GetMapping(value = "/getprofession", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = " 获取专业列表", response = Void.class, notes = "获取专业列表")
    public ResponseEntity<Map<String,Object>> getProfession(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "code 所属学院码", required = false) @RequestParam(value = "code", required = false) String code
                                                            ){
        return new ResponseEntity<Map<String, Object>>(organizationService.getProfession(orgId, code), HttpStatus.OK);

    }

    /**
     * 获取班级列表
     * @param orgId
     * @return
     */
    @GetMapping(value = "/getclass", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = " 获取班级列表", response = Void.class, notes = "获取班级列表")
    public ResponseEntity<Map<String,Object>> getClass(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "ccode 所属学院码", required = false) @RequestParam(value = "ccode", required = false) String ccode,
            @ApiParam(value = "pcode 所属专业码", required = false) @RequestParam(value = "pcode", required = false) String pcode
    ){
        return new ResponseEntity<Map<String, Object>>(organizationService.getClass(orgId, ccode, pcode), HttpStatus.OK);

    }


    /**
     * 获取年级列表
     * @param orgId
     * @return
     */
    @GetMapping(value = "/getgrade", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = " 获取年级列表", response = Void.class, notes = "获取年级列表")
    public ResponseEntity<Map<String,Object>> getGrade(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId
    ){
        return new ResponseEntity<Map<String, Object>>(organizationService.getGrade(orgId), HttpStatus.OK);

    }








}
