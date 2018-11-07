package com.aizhixin.cloud.dataanalysis.common;

import com.aizhixin.cloud.dataanalysis.common.dto.OrgCollegeIdDTO;
import org.springframework.util.StringUtils;

/**
 * 角色辅助工具，通过角色判断重置org和college的值，从而间接达到权限控制
 * 必须是学院及以上级别才行
 */
public class RoleTool {

    public static OrgCollegeIdDTO orgAndCollegeResetByRole(Long orgId, Long roleCollegeId, String roles) {
        Long cg = 0L, org = orgId;
        if (!StringUtils.isEmpty(roles)) {
            if (roles.indexOf("_COLLEG") > 0) {//学院管理员
                cg = roleCollegeId;
            } else {
                if (roles.indexOf("ROLE_ADMIN") < 0 && roles.indexOf("_ORG_") < 0) {//校级或超级管理员
                    org = 0L;
                }
            }
        } else {//无任何角色
            org = 0L;
        }
        return new OrgCollegeIdDTO(org, cg);
    }

    public static OrgCollegeIdDTO orgAndCollegeResetByRole(Long orgId, Long collegeId, Long roleCollegeId, String roles) {
        OrgCollegeIdDTO dto = orgAndCollegeResetByRole(orgId, roleCollegeId, roles);
        if (null != collegeId && null != dto.getCollegeId() && dto.getCollegeId() > 0) {//限制学院管理员只能查询本学院的数据
            if (collegeId.longValue() != dto.getCollegeId().longValue()) {
                dto.setOrgId(0L);
            }
        }
        if (null != collegeId && collegeId > 0) {//查询条件的学院具有最高优先级
            dto.setCollegeId(collegeId);
        }
        return dto;
    }
}
