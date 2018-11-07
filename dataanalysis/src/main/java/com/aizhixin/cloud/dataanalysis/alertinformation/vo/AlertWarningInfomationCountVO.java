package com.aizhixin.cloud.dataanalysis.alertinformation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 告警类型分别统计
 */
@ApiModel(description="告警统计")
@NoArgsConstructor
@ToString
public class AlertWarningInfomationCountVO {
    @ApiModelProperty(value = "告警类型")
    @Getter @Setter private String warningType;

    @ApiModelProperty(value = "总告警数量")
    @Getter @Setter private int num;

    public AlertWarningInfomationCountVO (String warningType, Long num) {
        this.warningType = warningType;
        this.num = num != null ? num.intValue() : 0;
    }
    public AlertWarningInfomationCountVO (String warningType, Integer num) {
        this.warningType = warningType;
        this.num = num;
    }
}
