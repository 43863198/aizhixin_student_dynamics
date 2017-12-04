package com.aizhixin.cloud.dataanalysis.test;

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
@RequestMapping("/v1/testdata")
@Api(description = "添加测试数据API")
public class AddDataController {
    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private AlertWarningInformationService alertWarningInformationService;
    @Autowired
    private WarningTypeService warningTypeService;

    @GetMapping(value = "/addwarninginfo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "添加测试预警信息数据", response = Void.class, notes = "添加测试预警信息数据<br><br><b>@author jianwei.wu</b>")
    public void addWarningInfo(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId) {
        String url = "http://gateway.aizhixintest.com/org-manager";
        url = url + "/v1/students/list?pageSize=1000&orgId=" + orgId;
        List<WarningInformation> awinfoList = new ArrayList<>();
        try {
            String respone = new RestUtil().getData(url, null);
            if (null != respone) {
                JSONObject json = JSONObject.fromObject(respone);
                Object data = json.get("data");
                if (null != data) {
                    JSONArray jsonArray = JSONArray.fromObject(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Long defendantId = jsonObject.getLong("id");
                        String name = jsonObject.getString("name");
                        String jobNumber = jsonObject.getString("jobNumber");
                        String collegeName = jsonObject
                                .getString("collegeName");
                        Long collegeId = jsonObject.getLong("collegeId");
                        String professionalName = jsonObject
                                .getString("professionalName");
                        Long professionalId = jsonObject
                                .getLong("professionalId");
                        String classesName = jsonObject
                                .getString("classesName");
                        Long classesId = jsonObject.getLong("classesId");
                        String phone = jsonObject.getString("phone");
                        String teachingYear = jsonObject
                                .getString("teachingYear");
                        String warningType = "";
                        if (i % 2 == 0) {
                            warningType = "Register";
                        } else if (i % 3 == 0) {
                            warningType = "LeaveSchool";
                        } else if (i % 5 == 0) {
                            warningType = "AttendAbnormal";
                        } else if (i % 7 == 0) {
                            warningType = "Absenteeism";
                        } else if (i % 9 == 0) {
                            warningType = "TotalAchievement";
                        } else if (i % 11 == 0) {
                            warningType = "SupplementAchievement";
                        } else if (i % 13 == 0) {
                            warningType = "PerformanceFluctuation";
                        } else {
                            warningType = "Cet";
                        }
                        WarningInformation awinfo = null;
                        List<WarningInformation> awinfos = alertWarningInformationService
                                .getawinfoByDefendantId(orgId, warningType,
                                        defendantId);
                        if (null != awinfos) {
                            if (awinfos.size() == 1) {
                                awinfo = awinfos.get(0);
                            } else if (awinfos.size() > 1) {
                                continue;
                            } else {
                                awinfo = new WarningInformation();
                            }
                        } else {
                            awinfo = new WarningInformation();
                        }
                        awinfo.setOrgId(orgId);
                        if (null != defendantId) {
                            awinfo.setDefendantId(defendantId);
                        }
                        if (null != name) {
                            awinfo.setName(name);
                        }
                        if (null != jobNumber) {
                            awinfo.setJobNumber(jobNumber);
                        }
                        if (null != collegeName) {
                            awinfo.setCollogeName(collegeName);
                        }
                        if (null != collegeId) {
                            awinfo.setCollogeId(collegeId);
                        }
                        if (null != professionalName) {
                            awinfo.setProfessionalName(professionalName);
                        }
                        if (null != professionalId) {
                            awinfo.setProfessionalId(professionalId);
                        }
                        if (null != classesName) {
                            awinfo.setClassName(classesName);
                        }
                        if (null != classesId) {
                            awinfo.setClassId(classesId);
                        }
                        if (null != phone) {
                            awinfo.setPhone(phone);
                        }
                        if (null != teachingYear) {
                            awinfo.setTeachingYear(teachingYear);
                        }
                        if (i % 77 == 0) {
                            awinfo.setWarningLevel(1);
                        } else if (i % 55 == 0) {
                            awinfo.setWarningLevel(2);
                        } else {
                            awinfo.setWarningLevel(3);
                        }
                        if (i % 9 == 0) {
                            awinfo.setWarningState(20);
                        } else {
                            awinfo.setWarningState(10);
                        }
                        awinfo.setWarningType(warningType);
                        awinfo.setCreatedDate(new Date());
                        awinfoList.add(awinfo);
                    }
                    alertWarningInformationService.save(awinfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping(value = "/addwaringtype", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "添加预警类型数据", response = Void.class, notes = "添加测试预警信息数据<br><br><b>@author jianwei.wu</b>")
    public  void setWarningType(
            @ApiParam(value = "orgId 机构id", required = true) @RequestParam(value = "orgId", required = true) Long orgId) {
        List<com.aizhixin.cloud.dataanalysis.setup.entity.WarningType> warningTypeList = new ArrayList<>();
       WarningType warningType1 = new WarningType();
        warningType1.setOrgId(orgId);
        warningType1.setWarningType("Register");
        warningType1.setWarningName("报道注册预警");
        warningType1.setSetupCloseFlag(10);
        warningType1.setWarningDescribe("未报到注册天数大于等于");
        warningTypeList.add(warningType1);
        WarningType warningType2 = new WarningType();
        warningType2.setOrgId(orgId);
        warningType2.setWarningType("LeaveSchool");
        warningType2.setWarningName("退学预警");
        warningType2.setSetupCloseFlag(10);
        warningType2.setWarningDescribe("必修课和专业选修课不及格课程累计学分大于等于");
        warningTypeList.add(warningType2);
        com.aizhixin.cloud.dataanalysis.setup.entity.WarningType warningType3 = new com.aizhixin.cloud.dataanalysis.setup.entity.WarningType();
        warningType3.setOrgId(orgId);
        warningType3.setWarningType("AttendAbnormal");
        warningType3.setWarningName("修读异常预警");
        warningType3.setSetupCloseFlag(10);
        warningType3.setWarningDescribe("不合格的必修课程（含集中性实践教学环节）学分大于等于");
        warningTypeList.add(warningType3);
        com.aizhixin.cloud.dataanalysis.setup.entity.WarningType warningType4 = new com.aizhixin.cloud.dataanalysis.setup.entity.WarningType();
        warningType4.setOrgId(orgId);
        warningType4.setWarningType("Absenteeism");
        warningType4.setWarningName("旷课预警");
        warningType4.setSetupCloseFlag(10);
        warningType4.setWarningDescribe("本学期内旷课次数大于等于");
        warningTypeList.add(warningType4);
        com.aizhixin.cloud.dataanalysis.setup.entity.WarningType warningType5 = new com.aizhixin.cloud.dataanalysis.setup.entity.WarningType();
        warningType5.setOrgId(orgId);
        warningType5.setWarningType("TotalAchievement");
        warningType5.setWarningName("总评成绩预警");
        warningType5.setSetupCloseFlag(10);
        warningType5.setWarningDescribe("上学期总评不及格课程门数大于等于,上学期平均学分绩点小于等于");
        warningTypeList.add(warningType5);
        com.aizhixin.cloud.dataanalysis.setup.entity.WarningType warningType6 = new com.aizhixin.cloud.dataanalysis.setup.entity.WarningType();
        warningType6.setOrgId(orgId);
        warningType6.setWarningType("SupplementAchievement");
        warningType6.setWarningName("补考成绩预警");
        warningType6.setSetupCloseFlag(10);
        warningType6.setWarningDescribe("补考后上学期总评不及格课程门数大于等于");
        warningTypeList.add(warningType6);
        com.aizhixin.cloud.dataanalysis.setup.entity.WarningType warningType7 = new com.aizhixin.cloud.dataanalysis.setup.entity.WarningType();
        warningType7.setOrgId(orgId);
        warningType7.setWarningType("PerformanceFluctuation");
        warningType7.setWarningName("成绩波动预警");
        warningType7.setSetupCloseFlag(10);
        warningType7.setWarningDescribe("相邻两个学期平均绩点相比下降数值大于等于,班级成绩排名下降名次大于等于");
        warningTypeList.add(warningType7);
        com.aizhixin.cloud.dataanalysis.setup.entity.WarningType warningType8 = new com.aizhixin.cloud.dataanalysis.setup.entity.WarningType();
        warningType8.setOrgId(orgId);
        warningType8.setWarningType("Cet");
        warningType8.setWarningName("四六级英语预警");
        warningType8.setSetupCloseFlag(10);
        warningType8.setWarningDescribe("相邻两个学期平均绩点相比下降数值大于等于,班级成绩排名下降名次大于等于");
        warningTypeList.add(warningType8);
        warningTypeService.save(warningTypeList);
    }

}
