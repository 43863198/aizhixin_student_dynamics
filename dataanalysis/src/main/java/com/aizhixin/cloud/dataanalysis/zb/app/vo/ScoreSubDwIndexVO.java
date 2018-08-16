package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="学生成绩单位统计指标")
@NoArgsConstructor
@ToString
public class ScoreSubDwIndexVO {
    @ApiModelProperty(value = "单位编码")
    @Getter @Setter private String code;
    @ApiModelProperty(value = "单位名称")
    @Getter @Setter private String name;
    @ApiModelProperty(value = "人数")
    @Getter @Setter private Long rs = 0L;
    @ApiModelProperty(value = "参考人次")
    @Getter @Setter private Long ckrc = 0L;
    @ApiModelProperty(value = "必修课参考人次")
    @Getter @Setter private Long bxckrc = 0L;
    @ApiModelProperty(value = "必修课不及格人次")
    @Getter @Setter private Long bxbjgrc = 0L;
    @ApiModelProperty(value = "课程数")
    @Getter @Setter private Long kcs = 0L;
    @ApiModelProperty(value = "评价平均成绩")
    @Getter @Setter private Double avgcj = 0.0;
    @ApiModelProperty(value = "平均绩点")
    @Getter @Setter private Double avgjd = 0.0;

}
