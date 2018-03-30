package com.aizhixin.cloud.dataanalysis.permissions.service;

import com.aizhixin.cloud.dataanalysis.permissions.OmsCoreManager;
import com.aizhixin.cloud.dataanalysis.permissions.dto.RolePermissionsDTO;
import com.aizhixin.cloud.dataanalysis.permissions.entity.OperationPermissions;
import com.aizhixin.cloud.dataanalysis.permissions.respository.OperationPermissionsRespository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-03-29
 */
@Service
@Transactional
public class OperationPermissionsService {
    @Autowired
    private OperationPermissionsRespository operationPermissionsRespository;

    public void  save(OperationPermissions operationPermissions){
        operationPermissionsRespository.save(operationPermissions);
    }

    public void save(List<OperationPermissions> opList){
        operationPermissionsRespository.save(opList);
    }

    public void delete(String id){
        operationPermissionsRespository.delete(id);
    }

    public void deleteByRoleName(String roleName){
        operationPermissionsRespository.deleteByRoleName(roleName);
    }


    public Map<String,Object> saveRoleOperationPermissions(RolePermissionsDTO rolePermissionsDTO){
        Map<String,Object> result = new HashMap<>();
        List<OperationPermissions> opList = new ArrayList<>();
        try {
            if(StringUtils.isBlank(rolePermissionsDTO.getRoleName())){
                result.put("success",false);
                result.put("message","角色名称不能为空");
                return result;
            }else {
                this.deleteByRoleName(rolePermissionsDTO.getRoleName());//删除角色的历史权限
                List<String> opidList = OmsCoreManager.margePrivilege(rolePermissionsDTO.getOpids());//进行权限合并
                for(String opid : opidList){
                    OperationPermissions op = new OperationPermissions();
                    op.setOpid(opid);
                    op.setRoleName(rolePermissionsDTO.getRoleName());
                    opList.add(op);
                }
                this.save(opList);
            }
            result.put("success",true);
            result.put("message","角色权限保存成功");
            return result;
        }catch (Exception e){
            result.put("success",false);
            result.put("message","角色权限保存失败");
            return result;
        }
    }
}
