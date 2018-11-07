package com.aizhixin.cloud.dataanalysis.setup.controller;

import com.aizhixin.cloud.dataanalysis.setup.domain.AlarmSettingDomain;
import com.aizhixin.cloud.dataanalysis.setup.domain.ProcessingModeDomain;
import com.aizhixin.cloud.dataanalysis.setup.job.WarningTypeOnAndOffJob;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.GenerateWarningInfoService;
import com.aizhixin.cloud.dataanalysis.setup.service.RuleService;
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
 * @Date: 2017-11-24
 */
@RestController
@RequestMapping("/v1/alarmsettings")
@Api(description = "预警设置API")
public class AlarmSettingsController {
    @Autowired
    private AlarmSettingsService alarmSettingsService;
    @Autowired
    private WarningTypeOnAndOffJob warningTypeOnAndOffJob;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private GenerateWarningInfoService generateWarningInfoService;

    /**
     * 预警类型列表
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/getwarningtypelist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "预警类型列表", response = Void.class, notes = "预警类型列表<br><br><b>@author jianwei.wu</b>")
    public Map<String, Object> getWarningTypeList(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId) {
        return alarmSettingsService.getWarningTypeList(orgId);
    }

    /**
     * 已设置的预警参数信息
     *
     * @param warningTypeId
     * @return
     */
    @GetMapping(value = "/getwarningset", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "已设置的预警参数信息", response = Void.class, notes = "已设置的预警参数信息<br><br><b>@author jianwei.wu</b>")
    public Map<String, Object> getWarningSet(
            @ApiParam(value = "warningTypeId 预警类型Id", required = true) @RequestParam(value = "warningTypeId", required = true) String warningTypeId
    ) {
        return alarmSettingsService.getWarningSet(warningTypeId);
    }


    /**
     * 预警参数设置
     *
     * @param alarmSettingDomain
     * @return
     */
    @PostMapping(value = "/warningset", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "预警参数设置", response = Void.class, notes = "预警参数设置<br><br><b>@author jianwei.wu</b>")
    public Map<String, Object> warningSet(
            @ApiParam(value = "<b>必填:、</b><br>warningTypeId:预警类型id;<br><b>" +
                    "</b><br>setupCloseFlag:此类型预警的开关（10：开启；20:关闭）;" +
                    "</b><br>warningGradeDomainList:;包含" +
                    "</b><br>alarmSettingsId:预警设置id;" +
                    "</b><br>grade:等级;" +
                    "</b><br>setupCloseFlag:开启状态(10:启用 ;20:关闭;" +
                    "</b><br>relation:规则直接的关系(与，或);" +
                    "</b><br>waringParameterDomainList:包含;" +
                    "</b><br>id:id;" +
                    "</b><br>serialNumber:序号;" +
                    "</b><br>ruleName:规则名称;" +
                    "</b><br>ruledescribe:规则描述;" +
                    "<br><br>parameter:参数;<br><b>"
            )
            @RequestBody AlarmSettingDomain alarmSettingDomain
    ) {
        Map<String, Object> result = alarmSettingsService.warningSet(alarmSettingDomain);

//        if (null != result.get("warningType")) {
//            String warningType = (String) result.get("warningType");
//            Long orgId = (Long) result.get("orgId");
//            alarmSettingsService.rebuildAlertInfor(warningType, orgId);
//        }
        return result;
    }

    /**
     * 获取规则列表
     * @return
     */
    @GetMapping(value = "/getrulelist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取规则列表", response = Void.class, notes = "获取规则列表<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Object> getRuleList() {
        return new ResponseEntity<Object>(ruleService.getRuleList(),HttpStatus.OK);
    }



    /**
     * 已设置的预警处理设置信息
     *
     * @param warningTypeId
     * @return
     */
    @GetMapping(value = "/getprocessingmode", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "已设置的预警处理设置信息", response = Void.class, notes = "已设置的预警处理设置信息<br><br><b>@author jianwei.wu</b>")
    public Map<String, Object> getProcessingMode(
            @ApiParam(value = "warningTypeId 预警类型id", required = true) @RequestParam(value = "warningTypeId", required = true) String warningTypeId
    ) {
        return alarmSettingsService.getProcessingMode(warningTypeId);
    }


    /**
     * 预警处理设置
     *
     * @param domain
     * @return
     */
    @PostMapping(value = "/setprocessingmode", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "预警处理设置", response = Void.class, notes = "预警处理设置<br><br><b>@author jianwei.wu</b>")
    public Map<String, Object> setProcessingMode(
            @ApiParam(value = "<b>必填:、</b><br>orgId:机构id<br><b>" +
                    "</b><br>warningTypeId:预警类型id;" +
                    "</b><br>processingGreadList:包含;" +
                    "</b><br>grade:预警类等级;" +
                    "</b><br>operationTypeSet:预警处理操作类型集合(发送学生10 发送辅导员 20 发送院系领导);" +
                    "</b><br>setupCloseFlag:开启或关闭;" +
                    "<br><br>operationSet:预警处理操作集合(手机短信 1 电子邮件 2 站内信 3 注:多个用“,”隔开);<br><b>"
            )
            @RequestBody ProcessingModeDomain domain
    ) {
        return alarmSettingsService.setProcessingMode(domain);
    }


    /**
     * 迎新开启引擎
     *
     * @param warningTypeId
     * @return
     */
    @GetMapping(value = "/openalarmsettings", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "迎新开启引擎", response = Void.class, notes = "迎新开启引擎<br><br><b>@author jianwei.wu</b>")
    public Map<String, Object> openAlarmSettings(
            @ApiParam(value = "warningTypeId 预警类型id", required = true) @RequestParam(value = "warningTypeId", required = true) String warningTypeId,
            @ApiParam(value = "expiryDate 新生报到截止日期(yyyy-MM-dd)", required = true) @RequestParam(value = "expiryDate", required = true) String expiryDate
    ) {
        return alarmSettingsService.openAlarmSettings(warningTypeId, expiryDate);
    }

    /**
     * 手动生成数据
     *
     * @param orgId
     * @param warningType
     * @return
     */
    @GetMapping(value = "/eneratedata", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "手动生成数据", response = Void.class, notes = "手动生成数据<br><br><b>@author jianwei.wu</b>")
    public Map<String, Object> generateData(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId,
            @ApiParam(value = "warningType 预警类型", required = true) @RequestParam(value = "warningType", required = true) String warningType
    ) {
        Map<String, Object> result = new HashMap<>();
        generateWarningInfoService.enerateWarningInfo(orgId, warningType);
        result.put("success", true);
        result.put("message", "手动生成数据成功!");
        return result;
    }


    @RequestMapping(value = "/openingorclosing", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "手动一键开启或关闭预警设置", response = Void.class, notes = "手动一键开启或关闭预警设置<br><br><b>@author jianwei.wu</b>")
    public ResponseEntity<Map<String, Object>> openingOrClosing(
            @ApiParam(value = "noOfOff 10:打开;20:关闭", required = true) @RequestParam(value = "noOfOff", required = true) int noOfOff
    ) {
        warningTypeOnAndOffJob.updateWarningType(noOfOff);
        warningTypeOnAndOffJob.updateAbsenteeismWarningType(noOfOff);
        warningTypeOnAndOffJob.updateRegisterWarningType(noOfOff);
        Map<String, Object> result = new HashMap<String, Object>();
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
}










