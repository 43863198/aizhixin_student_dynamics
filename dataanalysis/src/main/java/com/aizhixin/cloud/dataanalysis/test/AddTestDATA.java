package com.aizhixin.cloud.dataanalysis.test;

import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.respository.WarningTypeRespository;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-25
 */
@Component
public class AddTestDATA {
    @Autowired
    private WarningTypeService warningTypeService;


//    @Scheduled(cron = "0 0/10 * * * ?")
    public  void setWarningType(){
            Long orgId = 213L;
		    List<WarningType> warningTypeList = new ArrayList<>();
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
            WarningType warningType3 = new WarningType();
            warningType3.setOrgId(orgId);
            warningType3.setWarningType("AttendAbnormal");
            warningType3.setWarningName("修读异常预警");
            warningType3.setSetupCloseFlag(10);
            warningType3.setWarningDescribe("不合格的必修课程（含集中性实践教学环节）学分大于等于");
            warningTypeList.add(warningType3);
            WarningType warningType4 = new WarningType();
            warningType4.setOrgId(orgId);
            warningType4.setWarningType("Absenteeism");
            warningType4.setWarningName("旷课预警");
            warningType4.setSetupCloseFlag(10);
            warningType4.setWarningDescribe("本学期内旷课次数大于等于");
            warningTypeList.add(warningType4);
            WarningType warningType5 = new WarningType();
            warningType5.setOrgId(orgId);
            warningType5.setWarningType("TotalAchievement");
            warningType5.setWarningName("总评成绩预警");
            warningType5.setSetupCloseFlag(10);
            warningType5.setWarningDescribe("上学期总评不及格课程门数大于等于,上学期平均学分绩点小于等于");
            warningTypeList.add(warningType5);
            WarningType warningType6 = new WarningType();
            warningType6.setOrgId(orgId);
            warningType6.setWarningType("SupplementAchievement");
            warningType6.setWarningName("补考成绩预警");
            warningType6.setSetupCloseFlag(10);
            warningType6.setWarningDescribe("补考后上学期总评不及格课程门数大于等于");
            warningTypeList.add(warningType6);
            WarningType warningType7 = new WarningType();
            warningType7.setOrgId(orgId);
            warningType7.setWarningType("PerformanceFluctuation");
            warningType7.setWarningName("成绩波动预警");
            warningType7.setSetupCloseFlag(10);
            warningType7.setWarningDescribe("相邻两个学期平均绩点相比下降数值大于等于,班级成绩排名下降名次大于等于");
            warningTypeList.add(warningType7);
            WarningType warningType8 = new WarningType();
            warningType8.setOrgId(orgId);
            warningType8.setWarningType("Cet");
            warningType8.setWarningName("四六级英语预警");
            warningType8.setSetupCloseFlag(10);
            warningType8.setWarningDescribe("相邻两个学期平均绩点相比下降数值大于等于,班级成绩排名下降名次大于等于");
            warningTypeList.add(warningType8);
            warningTypeService.save(warningTypeList);
	    }
}





