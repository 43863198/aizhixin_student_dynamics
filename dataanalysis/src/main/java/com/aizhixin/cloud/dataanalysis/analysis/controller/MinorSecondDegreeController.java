package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.entity.MinorSecondDegreeInfo;
import com.aizhixin.cloud.dataanalysis.analysis.service.MinorSecondDegreeService;
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

import java.util.List;

@RestController
@RequestMapping("/v1/minorseconddegree")
@Api(description = "辅修、第二学位API")
public class MinorSecondDegreeController {
    @Autowired
    private MinorSecondDegreeService minorSecondDegreeService;


    @GetMapping(value = "/getstuinfo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取学生辅修、第二学位信息", response = Void.class, notes = "获取学生辅修、第二学位信息<br><br><b>@author dengchao</b>")
    public List<MinorSecondDegreeInfo> getStuInfo(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                  @ApiParam(value = "jobNumber 学号", required = true) @RequestParam(value = "jobNumber") String jobNumber) {
        return minorSecondDegreeService.findByXxdmAndXhAndXnAndXqm(orgId, jobNumber);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "按类型查询辅修或第二学位列表", response = Void.class, notes = "按类型查询辅修或第二学位列表<br><br><b>@author dengchao</b>")
    public PageData<MinorSecondDegreeInfo> list(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                @ApiParam(value = "collegeCode 学院code") @RequestParam(value = "collegeCode",  required = false) String collegeCode,
                                                @ApiParam(value = "professionCode 专业code") @RequestParam(value = "professionCode", required = false) String professionCode,
                                                @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        {
            return minorSecondDegreeService.list(orgId, collegeCode, professionCode, pageNumber, pageSize);
        }

    }
}
