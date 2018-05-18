package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.setup.entity.Rule;
import com.aizhixin.cloud.dataanalysis.setup.respository.RuleRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    public void deleteAll() {
        ruleRespository.deleteAll();
    }

    public void save(List<Rule> ruleList) {
        ruleRespository.save(ruleList);
    }

    public List<Rule> getRuleList(Long orgId) {
        return ruleRespository.findByOrgIdAndDeleteFlag(orgId, DataValidity.VALID.getState());
    }
}



