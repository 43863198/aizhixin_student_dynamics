package com.aizhixin.cloud.dataanalysis.alertinformation.controller;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AlertInforQueryDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.SubmitDealDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.WarningDetailsDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlarmHandlingService;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.OperaionRecordService;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @RequestMapping(value = "/submitprocessing", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "提交处理信息", response = Void.class, notes = "提交处理信息<br><br><b>@author 郑宁</b>")
    public Map<String, Object> submitProcessing(
            @ApiParam(value = "<b>必填:、</b><br>warningInformationId:预警信息id<br><b>" +
                    "选填:、" +
                    "</b><br>dealInfo:处理信息;" +
                    "dealType:处理类型 辅导员处理10 学院处理 20;" +
                    "<br>alertInforDomainList:附件信息、<br>")  @RequestBody SubmitDealDomain submitDealDomain) {
        return alarmHandlingService.submitProcessing(submitDealDomain) ;
    }

}




