package com.aizhixin.cloud.dataanalysis.notice.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ApiModel(description="告警内容")
@NoArgsConstructor
@ToString
public class AlertContentVO {
    @ApiModelProperty(value = "学院编码")
    @Getter @Setter private String collegeCode;
    @ApiModelProperty(value = "学院名称")
    @Getter @Setter private String collegeName;
    @ApiModelProperty(value = "告警类型")
    @Getter @Setter private String alertType;
    @ApiModelProperty(value = "告警内容")
    @Getter @Setter private String alertContent;
    @ApiModelProperty(value = "告警内容")
    @Getter @Setter private List<AlertTeacherVO> receiver;
}
