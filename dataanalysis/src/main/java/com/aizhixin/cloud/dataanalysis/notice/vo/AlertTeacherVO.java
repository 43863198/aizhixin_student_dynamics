package com.aizhixin.cloud.dataanalysis.notice.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="接收告警短信消息的老师信息")
@NoArgsConstructor
@ToString
public class AlertTeacherVO {
    @ApiModelProperty(value = "老师工号")
    @Getter @Setter private String code;
    @ApiModelProperty(value = "老师姓名")
    @Getter @Setter private String name;
    @ApiModelProperty(value = "老师电话")
    @Getter @Setter private String phone;
}
