package com.aizhixin.cloud.dataanalysis.setup.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel (description = "告警信息接收人信息")
@ToString
@NoArgsConstructor
public class CollegeAlertReceiverVO {
    @ApiModelProperty(value = "学院ID")
    @Getter @Setter private Long collegeId;

    @ApiModelProperty(value = "学院名称")
    @Getter @Setter private String collegeName;

    @ApiModelProperty(value = "接收人数量")
    @Getter @Setter private Long receiverCount;
}
