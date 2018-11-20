package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="二学位TOP10")
@ToString
@NoArgsConstructor
public class ExwYxsTop10VO {
    @ApiModelProperty(value = "院系所号")
    @Getter @Setter  private String yxsh;
    @ApiModelProperty(value = "院系所名称")
    @Getter @Setter private String yxsmc;
    @ApiModelProperty(value = "在校人数")
    @Getter @Setter private long zxrs;
    @ApiModelProperty(value = "二学位人数")
    @Getter @Setter private long exwrs;
    @ApiModelProperty(value = "比例%")
    @Getter @Setter private double bl;

    public ExwYxsTop10VO (String yxsh, String yxsmc, long zxrs) {
        this.yxsh = yxsh;
        this.yxsmc = yxsmc;
        this.zxrs = zxrs;
    }
}
