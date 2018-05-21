package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.setup.entity.RuleParameter;
import com.aizhixin.cloud.dataanalysis.setup.respository.RuleParameterRespository;
import com.aizhixin.cloud.dataanalysis.setup.respository.RuleRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-18
 */
@Transactional
@Service
public class RuleParameterService {
    @Autowired
    private RuleParameterRespository ruleParameterRespository;

    public String save(RuleParameter ruleParameter){
        return ruleParameterRespository.save(ruleParameter).getId();
    }

    public RuleParameter findById(String id){
        return ruleParameterRespository.findOne(id);
    }

    public List<RuleParameter>  getRuleParameterByIds(Set<String> ids){
        return ruleParameterRespository.findAllByIdIn(ids);
    }



}
