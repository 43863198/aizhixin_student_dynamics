package com.aizhixin.cloud.dataanalysis.analysis.controller;

import com.aizhixin.cloud.dataanalysis.analysis.entity.MinorSecondDegreeInfo;
import com.aizhixin.cloud.dataanalysis.analysis.service.MinorSecondDegreeService;
import com.aizhixin.cloud.dataanalysis.analysis.vo.ExwYxsTop10VO;
import com.aizhixin.cloud.dataanalysis.analysis.vo.MinorSecondDegreeVO;
import com.aizhixin.cloud.dataanalysis.analysis.vo.OverviewVO;
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

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "查询辅修、第二学位列表", response = Void.class, notes = "查询辅修、第二学位列表<br><br><b>@author dengchao</b>")
    public PageData<MinorSecondDegreeInfo> list(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                @ApiParam(value = "collegeCode 学院code") @RequestParam(value = "collegeCode", required = false) String collegeCode,
                                                @ApiParam(value = "professionCode 专业code") @RequestParam(value = "professionCode", required = false) String professionCode,
                                                @ApiParam(value = "pageNumber 第几页") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                @ApiParam(value = "pageSize 每页数据的数目") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        {
            return minorSecondDegreeService.list(orgId, collegeCode, professionCode, pageNumber, pageSize);
        }

    }

    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "部门辅修、二学位数据统计", response = Void.class, notes = "查询辅修、第二学位列表<br><br><b>@author dengchao</b>")
    public List<MinorSecondDegreeVO> statistics(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                @ApiParam(value = "collegeCode 学院code") @RequestParam(value = "collegeCode", required = false) String collegeCode) {
        {
            return minorSecondDegreeService.statistics(orgId, collegeCode);
        }

    }

    @GetMapping(value = "/overview", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "辅修、二学位数据概况", response = Void.class, notes = "辅修、二学位数据概况<br><br><b>@author dengchao</b>")
    public List<OverviewVO> list(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId,
                                 @ApiParam(value = "collegeCode 学院code") @RequestParam(value = "collegeCode", required = false) String collegeCode) {
        return minorSecondDegreeService.overview(orgId, collegeCode);
    }


    @GetMapping(value = "/exwtop10", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "二学位top10学院", response = Void.class, notes = "二学位top10学院<br><br><b>@author panzhen</b>")
    public List<ExwYxsTop10VO> top10(@ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId") Long orgId) {
        return minorSecondDegreeService.queryExw(orgId);
    }
}
