package com.aizhixin.cloud.dataanalysis.alertinformation.controller;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
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
      warningType1.setWarningName("迎新报到预警");
      warningType1.setSetupCloseFlag(10);
      warningType1.setWarningDescribe("未报到注册天数≥");
      warningTypeList.add(warningType1);
      WarningType warningType2 = new WarningType();
      warningType2.setOrgId(orgId);
      warningType2.setWarningType("LeaveSchool");
      warningType2.setWarningName("退学预警");
      warningType2.setSetupCloseFlag(10);
      warningType2.setWarningDescribe("必修课和专业选修课不及格课程累计学分≥");
      warningTypeList.add(warningType2);
      WarningType warningType3 = new WarningType();
      warningType3.setOrgId(orgId);
      warningType3.setWarningType("AttendAbnormal");
      warningType3.setWarningName("修读异常预警");
      warningType3.setSetupCloseFlag(10);
      warningType3.setWarningDescribe("不合格的必修课程（含集中性实践教学环节）学分≥");
      warningTypeList.add(warningType3);
      WarningType warningType4 = new WarningType();
      warningType4.setOrgId(orgId);
      warningType4.setWarningType("Absenteeism");
      warningType4.setWarningName("旷课预警");
      warningType4.setSetupCloseFlag(10);
      warningType4.setWarningDescribe("本学期内旷课次数≥");
      warningTypeList.add(warningType4);
      WarningType warningType5 = new WarningType();
      warningType5.setOrgId(orgId);
      warningType5.setWarningType("TotalAchievement");
      warningType5.setWarningName("总评成绩预警");
      warningType5.setSetupCloseFlag(10);
      warningType5.setWarningDescribe("上学期总评不及格课程门数≥,上学期平均学分绩点≤");
      warningTypeList.add(warningType5);
      WarningType warningType6 = new WarningType();
      warningType6.setOrgId(orgId);
      warningType6.setWarningType("SupplementAchievement");
      warningType6.setWarningName("补考成绩预警");
      warningType6.setSetupCloseFlag(10);
      warningType6.setWarningDescribe("补考后上学期总评不及格课程门数≥");
      warningTypeList.add(warningType6);
      WarningType warningType7 = new WarningType();
      warningType7.setOrgId(orgId);
      warningType7.setWarningType("PerformanceFluctuation");
      warningType7.setWarningName("成绩波动预警");
      warningType7.setSetupCloseFlag(10);
      warningType7.setWarningDescribe("相邻两个学期平均绩点相比下降数值≥,班级成绩排名下降名次≥");
      warningTypeList.add(warningType7);
      WarningType warningType8 = new WarningType();
      warningType8.setOrgId(orgId);
      warningType8.setWarningType("Cet");
      warningType8.setWarningName("CET-4成绩预警");
      warningType8.setSetupCloseFlag(10);
      warningType8.setWarningDescribe("英语四级分数小于425分并且在校学年数已≥");
      warningTypeList.add(warningType8);
      warningTypeService.save(warningTypeList);
     }catch (Exception e){
      e.printStackTrace();
      return "添加基础数据失败！";
     }
     return "数据添加成功！";
    }

}
