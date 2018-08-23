package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.setup.entity.Rule;
import com.aizhixin.cloud.dataanalysis.setup.respository.RuleRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-17
 */
@Component
@Transactional
public class RuleService {
    @Autowired
    private RuleRespository ruleRespository;

    public Rule getById(String id){
        return ruleRespository.findOne(id);
    }

    public List<Rule> getByName(String name){
        return ruleRespository.findByNameAndDeleteFlag(name,DataValidity.VALID.getState());
    }

    public void deleteAll() {
        ruleRespository.deleteAll();
    }

    public void save(List<Rule> ruleList) {
        ruleRespository.save(ruleList);
    }

    public List<Rule> getRuleList() {
        return ruleRespository.findByDeleteFlag(DataValidity.VALID.getState());
    }

    public String setWarningRule() {
        try {
            ruleRespository.deleteAll();
            List<Rule> ruleList = new ArrayList<>();
            Rule rule1 = new Rule();
            rule1.setRuleDescribe("未报到注册天数≥");
            rule1.setName("A");
            ruleList.add(rule1);
            Rule rule2 = new Rule();
            rule2.setRuleDescribe("在校学习期间，考核不合格的必修课程（含集中性实践教学环节）学分≥");
            rule2.setName("B");
            ruleList.add(rule2);

            rule2 = new Rule();
            rule2.setRuleDescribe("对比培养计划，漏选的课程数量≥");
            rule2.setName("B");
            ruleList.add(rule2);

            rule2 = new Rule();
            rule2.setRuleDescribe("对比培养计划，漏选的课程学分≥");
            rule2.setName("B");
            ruleList.add(rule2);

            rule2 = new Rule();
            rule2.setRuleDescribe("已修读的课程门数占教学计划的比例≤");
            rule2.setName("B");
            ruleList.add(rule2);

            rule2 = new Rule();
            rule2.setRuleDescribe("已修读的课程学分占教学计划的比例≤");
            rule2.setName("B");
            ruleList.add(rule2);

            Rule  rule3 = new Rule();
            rule3.setRuleDescribe("上学期不合格的必修课程（含集中性实践教学环节）学分≥");
            rule3.setName("C");
            ruleList.add(rule3);
            Rule rule4 = new Rule();
            rule4.setRuleDescribe("本学期内旷课次数≥");
            rule4.setName("D");
            ruleList.add(rule4);
            Rule rule5 = new Rule();
            rule5.setRuleDescribe("上学期总评不及格课程门数≥");
            rule5.setName("E");
            ruleList.add(rule5);
            Rule rule6 = new Rule();
            rule6.setRuleDescribe("补考后上学期总评不及格课程门数≥");
            rule6.setName("F");
            ruleList.add(rule6);
            Rule rule7 = new Rule();
            rule7.setRuleDescribe("相邻两个学期平均绩点相比下降数值≥");
            rule7.setName("G");
            ruleList.add(rule7);
            Rule rule8 = new Rule();
            rule8.setRuleDescribe("英语四级分数小于425分并且在校学年数已≥");
            rule8.setName("H");
            ruleList.add(rule8);
            ruleRespository.save(ruleList);
        } catch (Exception e) {
            e.printStackTrace();
            return "添加基础数据失败！";
        }
        return "数据添加成功！";
    }


}



