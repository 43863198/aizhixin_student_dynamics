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
public class QuestionnaireScreenVO {
    @ApiModelProperty(value = "单位ID")
    @Getter  @Setter private Long unitId;
    @ApiModelProperty(value = "单位名称")
    @Getter  @Setter private String unitName;
    @ApiModelProperty(value = "需评人数")
    @Getter @Setter private Integer xprs;
    @ApiModelProperty(value = "已评人数")
    @Getter  @Setter private Integer yprs;
    @ApiModelProperty(value = "未评人数")
    @Getter  @Setter private Integer wprs;
    @ApiModelProperty(value = "参评率")
    @Getter  @Setter private Double cpl;
    public QuestionnaireScreenVO (Long unitId, String unitName, Integer xprs, Integer yprs, Double cpl) {
        this.unitId = unitId;
        this.unitName = unitName;
        this.xprs = xprs;
        this.yprs = yprs;
        if (null != xprs && null != yprs) {
            wprs = xprs - yprs;
        }
        this.cpl = cpl;
    }
}
