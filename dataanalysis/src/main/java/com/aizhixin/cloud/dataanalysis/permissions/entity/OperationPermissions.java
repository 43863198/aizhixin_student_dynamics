package com.aizhixin.cloud.dataanalysis.permissions.entity;

import com.aizhixin.cloud.dataanalysis.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-03-29
 */
@Entity
@Table(name = "T_OPERATION_PERMISSIONS")
@ToString
public class OperationPermissions extends AbstractEntity {

    /*
     * 角色id
     */
    @Column(name = "ROLE_ID")
    @Getter
    @Setter
    private Long roleId;

    /*
     * 角色名称
     */
    @Column(name = "ROLE_NAME")
    @Getter
    @Setter
    private String roleName;
    /*
     * 操作id
     */
    @Column(name = "OP_ID")
    @Getter
    @Setter
    private  String opid;

}
