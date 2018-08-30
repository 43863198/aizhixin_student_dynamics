package com.aizhixin.cloud.dataanalysis.alertinformation.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@ApiModel(description = "批量处理筛选的预警信息")
@Data
public class BatchAllDealDomain {

    @ApiModelProperty(value = "处理类型和处理建议 辅导员处理10 学院处理 20", required = false)
    private Map<String, String> dealTypes;

    @ApiModelProperty(value = "学年", required = false)
    private String teacherYear;

    @ApiModelProperty(value = "学期", required = false)
    private String semester;

    @ApiModelProperty(value = "学院code（多个以,分割）", required = false)
    private String collegeCodes;

    @ApiModelProperty(value = "预警类型（多个以,分割）", required = false)
    private String warningTypes;

    @ApiModelProperty(value = "预警等级（多个以,分割）", required = false)
    private String warningLevels;

    @ApiModelProperty(value = "预警状态(10:告警中；20：已处理；40:取消)（多个以,分割）", required = false)
    private String warningStates;

    @ApiModelProperty(value = "姓名/学号/工号", required = false)
    private String keywords;

    @ApiModelProperty(value = "机构id", required = false)
    private Long orgId;

    @ApiModelProperty(value = "登录人工号")
    private String workNo;
}
