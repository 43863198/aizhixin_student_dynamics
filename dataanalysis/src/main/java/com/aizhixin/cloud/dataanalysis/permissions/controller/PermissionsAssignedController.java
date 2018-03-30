package com.aizhixin.cloud.dataanalysis.permissions.controller;

import com.aizhixin.cloud.dataanalysis.permissions.OmsCoreManager;
import com.aizhixin.cloud.dataanalysis.permissions.dto.RolePermissionsDTO;
import com.aizhixin.cloud.dataanalysis.permissions.service.OperationPermissionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-03-30
 */
@RestController
@RequestMapping("/v1/permissions")
@Api(description = "给角色赋予权限的相关操作API")
public class PermissionsAssignedController {
    @Autowired
    private OperationPermissionsService operationPermissionsService;

    @RequestMapping(value = "/getprivilege", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取所有权限节点", response = Void.class, notes = "获取所有权限节点<br><br><b>@author wu.jianwei</b>")
    public ResponseEntity<?> getPrivilegeList(){
        return new ResponseEntity<Object>(OmsCoreManager.PRIVIlEG_MAPPING, HttpStatus.OK);
    }

    @RequestMapping(value = "/saveassigned", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "保存角色对应分配的权限节点", response = Void.class, notes = "保存角色对应分配的权限节点<br><br><b>@author wu.jianwei</b>")
    public ResponseEntity<?> saveAssigned(
            @ApiParam(value = "<b>必填:、</b><br>roleName:角色名称;<br><b>" +
                    "<br>opids:权限节点的集合;<br>")
            @RequestBody RolePermissionsDTO rolePermissionsDTO)    {
        return new ResponseEntity<Object>(operationPermissionsService.saveRoleOperationPermissions(rolePermissionsDTO), HttpStatus.OK);
    }

}
