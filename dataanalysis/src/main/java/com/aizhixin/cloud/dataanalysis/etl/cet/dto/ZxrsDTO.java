package com.aizhixin.cloud.dataanalysis.etl.cet.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 在校人数
 */
@ApiModel
@ToString
@NoArgsConstructor
public class ZxrsDTO {
    @ApiModelProperty(value = "编号")
    @Getter @Setter private String bh;
    @ApiModelProperty(value = "性别")
    @Getter @Setter private String xb;
    @ApiModelProperty(value = "在校人数")
    @Getter @Setter private long zxrs;
    @ApiModelProperty(value = "男在校人数")
    @Getter @Setter private long nzxrs;
    @ApiModelProperty(value = "女在校人数")
    @Getter @Setter private long vzxrs;

    public ZxrsDTO (String bh, long zxrs) {
        this.bh = bh;
        this.zxrs = zxrs;
    }
    public ZxrsDTO (String bh, String xb, long zxrs) {
        this(bh, zxrs);
        this.xb = xb;
    }
}
