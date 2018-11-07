package com.aizhixin.cloud.dataanalysis.common.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description = "大数据角色辅助工具，重置org和college的ID值")
@NoArgsConstructor
@ToString
public class OrgCollegeIdDTO {
    @ApiModelProperty(value = "orgId 学校ID")
    @Getter @Setter private Long orgId;

    @ApiModelProperty(value = "collegeId 学院ID")
    @Getter @Setter private Long collegeId;

    public OrgCollegeIdDTO(Long orgId, Long collegeId) {
        this.orgId = orgId;
        this.collegeId = collegeId;
    }
}
