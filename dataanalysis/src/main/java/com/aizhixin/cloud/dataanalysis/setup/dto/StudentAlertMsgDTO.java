package com.aizhixin.cloud.dataanalysis.setup.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel (description = "学生告警内容")
@ToString
@NoArgsConstructor
public class StudentAlertMsgDTO {
    @ApiModelProperty(value = "电话号码")
    @Getter @Setter private String phone;

    @ApiModelProperty(value = "告警内容")
    @Getter @Setter private String content;
}
