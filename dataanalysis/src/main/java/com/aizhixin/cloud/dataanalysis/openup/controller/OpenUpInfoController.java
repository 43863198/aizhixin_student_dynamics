package com.aizhixin.cloud.dataanalysis.openup.controller;

import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;
import com.aizhixin.cloud.dataanalysis.openup.domain.OpenUpInfoDomain;
import com.aizhixin.cloud.dataanalysis.openup.entity.OpenUpInfo;
import com.aizhixin.cloud.dataanalysis.openup.service.OpenUpInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/openup")
@Api(description = "授权学校操作API")
public class OpenUpInfoController {
    @Autowired
    private OpenUpInfoService openUpInfoService;

    @RequestMapping(value = "/save",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "授权学校",httpMethod = "POST",response = Void.class,notes = "授权学校<br>@author xiagen</br>")
    public ResponseEntity<Map<String,Object>> save(@RequestBody  OpenUpInfoDomain openUpInfoDomain){
        Map<String,Object> result=new HashMap<>();
        if (null==openUpInfoDomain.getOrgId()||0L==openUpInfoDomain.getOrgId()){
            result.put(ApiReturnConstants.RESULT,Boolean.FALSE);
            result.put(ApiReturnConstants.CAUSE,"学校id不能为空");
            return new ResponseEntity<>(result, HttpStatus.EXPECTATION_FAILED);
        }
        if (StringUtils.isEmpty(openUpInfoDomain.getOrgCode())){
            result.put(ApiReturnConstants.RESULT,Boolean.FALSE);
            result.put(ApiReturnConstants.CAUSE,"学校代码不能为空");
            return new ResponseEntity<>(result, HttpStatus.EXPECTATION_FAILED);
        }
         OpenUpInfoDomain openUpInfo=openUpInfoService.findByOrgCode(openUpInfoDomain.getOrgCode());
        if (openUpInfo!=null){
            if (openUpInfo.getOrgId()!=null){
                result.put(ApiReturnConstants.RESULT,Boolean.FALSE);
                result.put(ApiReturnConstants.CAUSE,"学校已授权");
                return new ResponseEntity<>(result, HttpStatus.EXPECTATION_FAILED);
            }
        }
        OpenUpInfo upInfo=openUpInfoService.save(openUpInfoDomain);
        result.put(ApiReturnConstants.RESULT,Boolean.TRUE);
        result.put(ApiReturnConstants.DATA,upInfo.getId());
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/del",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "批量删除授权信息",httpMethod = "DELETE",response = Void.class,notes = "批量删除授权信息<br>@author xiagen</br>")
    public ResponseEntity<Map<String,Object>> deleteAll(@RequestBody  List<String> ids){
        Map<String,Object> result=new HashMap<>();
        if (null==ids||0==ids.size()){
            result.put(ApiReturnConstants.RESULT,Boolean.FALSE);
            result.put(ApiReturnConstants.CAUSE,"授权信息id集合不能为空");
            return new ResponseEntity<>(result, HttpStatus.EXPECTATION_FAILED);
        }
        openUpInfoService.deleteAll(ids);
        result.put(ApiReturnConstants.RESULT,Boolean.TRUE);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询授权信息列表",httpMethod = "GET",response = Void.class,notes = "查询授权信息列表<br>@author xiagen</br>")
    public ResponseEntity<Map<String,Object>> list(@ApiParam(value = "pageNumber 起始页",required = false)@RequestParam(value = "pageNumber",required = false) Integer pageNumber, @ApiParam(value = "pageSize 分页大小",required = false)@RequestParam(value = "pageSize",required = false)Integer pageSize){
        Map<String,Object> result=new HashMap<>();
        if (null==pageNumber){
            pageNumber=1;
        }
        if (null==pageSize){
            pageSize=10;
        }
        result=openUpInfoService.findByOpenUpInfoAll(pageNumber,pageSize,result);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询授权信息",httpMethod = "GET",response = Void.class,notes = "查询授权信息<br>@author xiagen</br>")
    public ResponseEntity<Map<String,Object>> get(@ApiParam(value = "orgCode 学校代码",required = true)@RequestParam(value = "orgCode",required = true) String orgCode){
        Map<String,Object> result=new HashMap<>();
        if (StringUtils.isEmpty(orgCode)){
            result.put(ApiReturnConstants.RESULT,Boolean.FALSE);
            result.put(ApiReturnConstants.CAUSE,"学校代码不能为空");
            return new ResponseEntity<>(result,HttpStatus.EXPECTATION_FAILED);
        }
        OpenUpInfoDomain openUpInfoDomain=openUpInfoService.findByOrgCode(orgCode);
        result.put(ApiReturnConstants.RESULT,Boolean.TRUE);
        if (openUpInfoDomain.getOrgId()!=null){
            result.put("isAuth",Boolean.TRUE);
        }else{
            result.put("isAuth",Boolean.FALSE);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
