package com.aizhixin.cloud.dataanalysis.alertinformation.controller;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AlertInforQueryDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.BatchDealResultDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.DealDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.DealResultDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlarmHandlingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@RestController
@RequestMapping("/v1/alarmhandling")
@Api(description = "预警处理API")
public class AlarmHandlingController {
    @Autowired
    private AlarmHandlingService alarmHandlingService;

    @RequestMapping(value = "/addprocessing", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "提交处理信息", response = Void.class, notes = "提交处理信息<br><br><b>@author wu.jianwei</b>")
    public Map<String, Object> addProcessing(
            @ApiParam(value = "<b>必填:、</b><br>warningInformationId:预警信息id<br><b>" +
                    "选填:、" +
                    "</b><br>dealId:处理操作id;" +
                    "</b><br>dealInfo:处理信息;" +
                    "dealType:处理类型 辅导员处理10 学院处理 20;" +
                    "<br>attachmentDomain:附件信息、<br>")  @RequestBody DealDomain dealDomain) {
        return alarmHandlingService.addProcessing(dealDomain) ;
    }

    @PutMapping(value = "/updateprocessing",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "修改处理信息", response = Void.class, notes = "修改处理信息<br><br><b>@author wu.jianwei</b>")
    public Map<String, Object> updateProcessing(
            @ApiParam(value = "<b>必填:、</b><br>warningInformationId:预警信息id<br><b>" +
                    "</b><br>dealId:处理id;" +
                    "选填:、" +
                    "</b><br>dealInfo:处理信息;" +
                    "dealType:处理类型 辅导员处理10 学院处理 20;" +
                    "<br>attachmentDomain:附件信息、<br>")  @RequestBody DealDomain dealDomain) {
        return alarmHandlingService.updateProcessing(dealDomain) ;
    }


    @RequestMapping(value = "/processing", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "处理结果", response = Void.class, notes = "处理结果<br><br><b>@author wu.jianwei</b>")
    public Map<String, Object> processing(
            @ApiParam(value = "<b>必填:、</b><br>warningInformationId:预警信息id<br><b>" +
                    "<br>status:20 完成预警处理 40 撤销预警处理<br>")  @RequestBody DealResultDomain dealResultDomain) {
        return alarmHandlingService.processing(dealResultDomain) ;
    }


    @PutMapping(value = "/batchprocessing",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "批量处理本页信息", response = Void.class, notes = "批量处理本页信息<br><br><b>@author dengchao</b>")
    public Map<String, Object> processing(
            @ApiParam(value = "<b>必填:、</b><br>warningInformationId:预警信息id<br><b>" +
                    "<br>status:20 完成预警处理 40 撤销预警处理<br>")  @RequestBody BatchDealResultDomain batchDealResultDomain) {
        return alarmHandlingService.batchProcessing(batchDealResultDomain) ;
    }

    @PutMapping(value = "/batchallprocessing",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "PUT", value = "批量处理本页信息", response = Void.class, notes = "批量处理本页信息<br><br><b>@author dengchao</b>")
    public Map<String, Object> processing(
            @ApiParam(value = "<b>必填:、</b><br>orgId:机构id<br>" +
                    "<br>teacherYear:学年<br>" +
                    "<br>semester:学期<br>" +
                    "<b>选填:、</b><br>collogeCodes:学院code(字符串多个以,分隔);warningTypes:预警类型(字符串多个以,分隔);warningLevels:预警等级(字符串多个以,分隔);<br>collegeId:院系id、<br>")  @RequestBody AlertInforQueryDomain domain) {
        return alarmHandlingService.batchAllProcessing(domain) ;
    }

}




