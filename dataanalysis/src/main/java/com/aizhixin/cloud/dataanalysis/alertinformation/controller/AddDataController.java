package com.aizhixin.cloud.dataanalysis.alertinformation.controller;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.setup.entity.Rule;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.service.RuleService;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-04
 */
@RestController
@RequestMapping("/v1/base")
@Api(description = "添加基础数据API")
public class AddDataController {
    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private AlertWarningInformationService alertWarningInformationService;
    @Autowired
    private WarningTypeService warningTypeService;
    @Autowired
    private RuleService ruleService;

    @GetMapping(value = "/addwaringtype", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "添加预警类型数据", response = Void.class, notes = "添加测试预警信息数据<br><br><b>@author jianwei.wu</b>")
    public  String setWarningType(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId) {

     try {
      warningTypeService.delete(orgId);
      List<WarningType> warningTypeList = new ArrayList<>();
      WarningType warningType1 = new WarningType();
      warningType1.setOrgId(orgId);
      warningType1.setWarningType("Register");
      warningType1.setWarningTypeDescribe("短期过程性预警");
      warningType1.setWarningName("迎新报到预警");
      warningType1.setSetupCloseFlag(20);
      warningTypeList.add(warningType1);
      WarningType warningType2 = new WarningType();
      warningType2.setOrgId(orgId);
      warningType2.setWarningType("LeaveSchool");
      warningType2.setWarningTypeDescribe("结果性预警");
      warningType2.setWarningName("退学预警");
      warningType2.setSetupCloseFlag(20);
      warningTypeList.add(warningType2);
      WarningType warningType3 = new WarningType();
      warningType3.setOrgId(orgId);
      warningType3.setWarningType("AttendAbnormal");
      warningType3.setWarningTypeDescribe("结果性预警");
      warningType3.setWarningName("修读异常预警");
      warningType3.setSetupCloseFlag(20);
      warningTypeList.add(warningType3);
      WarningType warningType4 = new WarningType();
      warningType4.setOrgId(orgId);
      warningType4.setWarningType("Absenteeism");
      warningType4.setWarningTypeDescribe("全学期过程性预警");
      warningType4.setWarningName("旷课预警");
      warningType4.setSetupCloseFlag(20);
      warningTypeList.add(warningType4);
      WarningType warningType5 = new WarningType();
      warningType5.setOrgId(orgId);
      warningType5.setWarningType("TotalAchievement");
      warningType5.setWarningTypeDescribe("结果性预警");
      warningType5.setWarningName("总评成绩预警");
      warningType5.setSetupCloseFlag(20);
      warningTypeList.add(warningType5);
      WarningType warningType6 = new WarningType();
      warningType6.setOrgId(orgId);
      warningType6.setWarningType("SupplementAchievement");
      warningType6.setWarningTypeDescribe("结果性预警");
      warningType6.setWarningName("补考成绩预警");
      warningType6.setSetupCloseFlag(20);
      warningTypeList.add(warningType6);
      WarningType warningType7 = new WarningType();
      warningType7.setOrgId(orgId);
      warningType7.setWarningType("PerformanceFluctuation");
      warningType7.setWarningTypeDescribe("结果性预警");
      warningType7.setWarningName("成绩波动预警");
      warningType7.setSetupCloseFlag(20);
      warningTypeList.add(warningType7);
      WarningType warningType8 = new WarningType();
      warningType8.setOrgId(orgId);
      warningType8.setWarningType("Cet");
      warningType8.setWarningTypeDescribe("结果性预警");
      warningType8.setWarningName("CET-4成绩预警");
      warningType8.setSetupCloseFlag(20);
      warningTypeList.add(warningType8);
      warningTypeService.save(warningTypeList);

      ruleService.deleteAll();
      List<Rule> ruleList = new ArrayList<>();
      Rule  rule1 = new Rule();
      rule1.setOrgId(orgId);
      rule1.setName("未报到注册天数≥");
      ruleList.add(rule1);
      Rule  rule2 = new Rule();
      rule2.setOrgId(orgId);
      rule2.setName("必修课和专业选修课不及格课程累计学分≥");
      ruleList.add(rule2);
      Rule  rule3 = new Rule();
      rule3.setOrgId(orgId);
      rule3.setName("不合格的必修课程（含集中性实践教学环节）学分≥");
      ruleList.add(rule3);
      Rule  rule4 = new Rule();
      rule4.setOrgId(orgId);
      rule4.setName("本学期内旷课次数≥");
      ruleList.add(rule4);
      Rule  rule5 = new Rule();
      rule5.setOrgId(orgId);
      rule5.setName("上学期总评不及格课程门数≥");
      ruleList.add(rule5);
      Rule  rule6 = new Rule();
      rule6.setOrgId(orgId);
      rule6.setName("补考后上学期总评不及格课程门数≥");
      ruleList.add(rule6);
      Rule  rule7 = new Rule();
      rule7.setOrgId(orgId);
      rule7.setName("相邻两个学期平均绩点相比下降数值≥");
      ruleList.add(rule7);
      Rule  rule8 = new Rule();
      rule8.setOrgId(orgId);
      rule8.setName("英语四级分数小于425分并且在校学年数已≥");
      ruleList.add(rule8);
      ruleService.save(ruleList);

     }catch (Exception e){
      e.printStackTrace();
      return "添加基础数据失败！";
     }
     return "数据添加成功！";
    }

}
