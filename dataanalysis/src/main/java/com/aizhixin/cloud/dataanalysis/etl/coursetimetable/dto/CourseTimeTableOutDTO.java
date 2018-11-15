package com.aizhixin.cloud.dataanalysis.etl.coursetimetable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 桂理排课计算输出数据
 */
@ApiModel("桂理排课计算输出数据")
@ToString
@NoArgsConstructor
public class CourseTimeTableOutDTO {
    @ApiModelProperty(value = "学校代码")
    @Getter @Setter private Long orgId;
    @ApiModelProperty(value = "教学班号")
    @Getter @Setter private String jxbh;
    @ApiModelProperty(value = "教学班名称")
    @Getter @Setter private String jxbmc;
    @ApiModelProperty(value = "起始周")
    @Getter @Setter private int qsz;
    @ApiModelProperty(value = "结束周")
    @Getter @Setter private int jsz;
    @ApiModelProperty(value = "周几")
    @Getter @Setter private int xqj;
    @ApiModelProperty(value = "第几节")
    @Getter @Setter private int djj;
    @ApiModelProperty(value = "持续节")
    @Getter @Setter private int cxj;

}
