package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.dto.TeacherlYearData;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolYearTermService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Created by wangjun
 * @E-mail: wangjun@aizhixin.com
 * @Date: 2017-12-25
 */
@RestController
@RequestMapping("/v1/teacheryear")
@Api(description = "学年学期api")
public class SchoolYearTermController {
    @Autowired
    private SchoolYearTermService schoolYearTermService;
    /**
     * 根据数据库数据初始学年学期
     * @param orgId
     * @return
     */
    @GetMapping(value = "/initschoolyearterm", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "课程评价", response = Void.class, notes = "根据数据库数据初始学年学期(会删除之前已生成过的数据)<br><br><b>@author wangjun</b>")
    public ResponseEntity<Map<String, Object>> initSchoolYearTerm(@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId){
        schoolYearTermService.initSchoolYearTerm(orgId);
        Map<String, Object> result = new HashMap<String, Object>();
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
    /**
     * 根据类型获取学年学期
     * @param orgId
     * @return
     */
    @GetMapping(value = "/schoolyearterm", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "课程评价", response = Void.class, notes = "根据类型获取学年学期")
    public List<TeacherlYearData> getSchoolYearTerm(@ApiParam(value = "orgId 机构id") @RequestParam(value = "orgId", required = true) Long orgId,
                                                    @ApiParam(value = "type:英语四六级:1,迎新学情:2 ,教学成绩:3,教师评价:4,课程评价:5,实践学情:6") @RequestParam(value = "type", required = true) Integer type){



       return schoolYearTermService.getSchoolYearTerm(orgId,type);

    }
}
