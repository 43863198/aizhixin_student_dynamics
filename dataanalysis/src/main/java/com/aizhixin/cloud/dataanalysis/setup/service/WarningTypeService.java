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

}
