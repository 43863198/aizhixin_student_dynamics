package com.aizhixin.cloud.dataanalysis.em.online.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel
@ToString
@NoArgsConstructor
public class ScreenOnlineCourseVO {
    @ApiModelProperty(value = "班课数量")
    @Getter  @Setter private Integer bks;
    @ApiModelProperty(value = "教师数量")
    @Getter  @Setter private Integer jss;
    @ApiModelProperty(value = "学生数量")
    @Getter  @Setter private Integer xss;
    @ApiModelProperty(value = "课程资源数量")
    @Getter  @Setter private Integer kcs;
    @ApiModelProperty(value = "作业数量")
    @Getter  @Setter private Integer zys;
}
