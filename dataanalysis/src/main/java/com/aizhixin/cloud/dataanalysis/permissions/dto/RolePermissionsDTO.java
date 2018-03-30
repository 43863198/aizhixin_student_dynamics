package com.aizhixin.cloud.dataanalysis.permissions.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-03-30
 */
@ApiModel(description="角色对应权限节点")
@Data
public class RolePermissionsDTO {
    @ApiModelProperty(value = "角色id")
    protected Long roleId;
    @ApiModelProperty(value = "角色名称")
    protected String roleName;
    @ApiModelProperty(value = "opid集合")
    protected List<String> opids ;

}
