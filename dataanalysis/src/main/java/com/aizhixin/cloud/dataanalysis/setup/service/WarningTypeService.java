package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.setup.domain.WarningTypeDomain;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.Rule;
import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import com.aizhixin.cloud.dataanalysis.setup.respository.WarningTypeRespository;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
@Service
@Transactional
public class WarningTypeService {
    @Autowired
    private WarningTypeRespository warningTypeRespository;
    @Autowired
    private RuleService ruleService;

    public WarningType getWarningTypeById(String id) {
        return warningTypeRespository.findOne(id);
    }

    public WarningType getWarningTypeByOrgIdAndType(Long orgId, String warningType) {
        return warningTypeRespository.findOneByOrgIdAndType(orgId, warningType);
    }

    public List<WarningType> getAllWarningTypeList() {
        return warningTypeRespository.getAllWarningType(DataValidity.VALID.getState());
    }

    public List<WarningType> getWarningTypeList(Long orgId) {
        return warningTypeRespository.getWarningTypeByOrgId(orgId, DataValidity.VALID.getState());
    }

    public List<WarningType> getWarningTypeByTypeList(Set<String> typeList) {
        return warningTypeRespository.getWarningTypeByTypeList(typeList, DataValidity.VALID.getState());
    }

    public void save(WarningType warningType) {
        warningTypeRespository.save(warningType);
    }

    public void save(List<WarningType> warningTypeList) {
        warningTypeRespository.save(warningTypeList);
    }

    public List<WarningTypeDomain> getAllOrgId() {
        return warningTypeRespository.getAllOrgId();
    }

    public void delete(Long orgId) {
        warningTypeRespository.deleteByOrgId(orgId);
    }

    @Transactional
    public String setWarningType(Long orgId) {
        try {
            warningTypeRespository.deleteAll();
            List<WarningType> warningTypeList = new ArrayList<>();
            WarningType warningType1 = new WarningType();
            warningType1.setOrgId(orgId);
            warningType1.setType("Register");
            warningType1.setTypeDescribe("短期过程性预警");
            warningType1.setWarningName("迎新报到预警");
            warningType1.setSetupCloseFlag(20);
            warningTypeList.add(warningType1);
            WarningType warningType2 = new WarningType();
            warningType2.setOrgId(orgId);
            warningType2.setType("LeaveSchool");
            warningType2.setTypeDescribe("结果性预警");
            warningType2.setWarningName("退学预警");
            warningType2.setSetupCloseFlag(20);
            warningTypeList.add(warningType2);
//      WarningType warningType3 = new WarningType();
//      warningType3.setOrgId(orgId);
//      warningType3.setWarningType("AttendAbnormal");
//      warningType3.setWarningTypeDescribe("结果性预警");
//      warningType3.setWarningName("修读异常预警");
//      warningType3.setSetupCloseFlag(20);
//      warningTypeList.add(warningType3);
            WarningType warningType4 = new WarningType();
            warningType4.setOrgId(orgId);
            warningType4.setType("Absenteeism");
            warningType4.setTypeDescribe("全学期过程性预警");
            warningType4.setWarningName("旷课预警");
            warningType4.setSetupCloseFlag(20);
            warningTypeList.add(warningType4);
            WarningType warningType5 = new WarningType();
            warningType5.setOrgId(orgId);
            warningType5.setType("TotalAchievement");
            warningType5.setTypeDescribe("结果性预警");
            warningType5.setWarningName("总评成绩预警");
            warningType5.setSetupCloseFlag(20);
            warningTypeList.add(warningType5);
            WarningType warningType6 = new WarningType();
            warningType6.setOrgId(orgId);
            warningType6.setType("SupplementAchievement");
            warningType6.setTypeDescribe("结果性预警");
            warningType6.setWarningName("补考成绩预警");
            warningType6.setSetupCloseFlag(20);
            warningTypeList.add(warningType6);
            WarningType warningType7 = new WarningType();
            warningType7.setOrgId(orgId);
            warningType7.setType("PerformanceFluctuation");
            warningType7.setTypeDescribe("结果性预警");
            warningType7.setWarningName("成绩波动预警");
            warningType7.setSetupCloseFlag(20);
            warningTypeList.add(warningType7);
            WarningType warningType8 = new WarningType();
            warningType8.setOrgId(orgId);
            warningType8.setType("Cet");
            warningType8.setTypeDescribe("结果性预警");
            warningType8.setWarningName("CET-4成绩预警");
            warningType8.setSetupCloseFlag(20);
            warningTypeList.add(warningType8);
            warningTypeRespository.save(warningTypeList);
            ruleService.deleteAll();
            List<Rule> ruleList = new ArrayList<>();
            Rule rule1 = new Rule();
            rule1.setOrgId(orgId);
            rule1.setName("RegisterEarlyWarning");//报到注册预警
            rule1.setRuleDescribe("未报到注册天数≥");
            ruleList.add(rule1);
            Rule rule2 = new Rule();
            rule2.setOrgId(orgId);
            rule2.setName("LeaveSchoolEarlyWarning");//退学预警
            rule2.setRuleDescribe("必修课和专业选修课不及格课程累计学分≥");
            ruleList.add(rule2);
//      Rule  rule3 = new Rule();
//      rule3.setOrgId(orgId);
//      rule3.setName("AttendAbnormalEarlyWarning");//修读异常预警
//      rule3.setRuleDescribe("不合格的必修课程（含集中性实践教学环节）学分≥");
//      ruleList.add(rule3);
            Rule rule4 = new Rule();
            rule4.setOrgId(orgId);
            rule4.setName("AbsenteeismEarlyWarning");//旷课预警
            rule4.setRuleDescribe("本学期内旷课次数≥");
            ruleList.add(rule4);
            Rule rule5 = new Rule();
            rule5.setOrgId(orgId);
            rule5.setName("TotalAchievementEarlyWarning");//总评成绩预警
            rule5.setRuleDescribe("上学期总评不及格课程门数≥");
            ruleList.add(rule5);
            Rule rule6 = new Rule();
            rule6.setOrgId(orgId);
            rule6.setName("SupplementAchievementEarlyWarning");//补考成绩预警
            rule6.setRuleDescribe("补考后上学期总评不及格课程门数≥");
            ruleList.add(rule6);
            Rule rule7 = new Rule();
            rule7.setOrgId(orgId);
            rule7.setName("PerformanceFluctuationEarlyWarning");//成绩波动预警
            rule7.setRuleDescribe("相邻两个学期平均绩点相比下降数值≥");
            ruleList.add(rule7);
            Rule rule8 = new Rule();
            rule8.setOrgId(orgId);
            rule8.setName("CetEarlyWarning");//cet预警
            rule8.setRuleDescribe("英语四级分数小于425分并且在校学年数已≥");
            ruleList.add(rule8);
            ruleService.save(ruleList);
        } catch (Exception e) {
            e.printStackTrace();
            return "添加基础数据失败！";
        }
        return "数据添加成功！";
    }


}
