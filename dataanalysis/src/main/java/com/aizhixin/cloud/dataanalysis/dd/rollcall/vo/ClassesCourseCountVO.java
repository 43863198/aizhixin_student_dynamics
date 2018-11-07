package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel
@ToString
@NoArgsConstructor
public class ClassesCourseCountVO {
    @ApiModelProperty(value = "节号")
    @Getter  @Setter private Integer no;
    @ApiModelProperty(value = "数量")
    @Getter  @Setter private Integer num;

    public ClassesCourseCountVO(Integer no, Integer num) {
        this.no = no;
        this.num = num;
    }
}
