package com.aizhixin.cloud.dataanalysis.zb.dto;


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
public class NjZxrsDTO {
    @ApiModelProperty(value = "编号")
    @Getter @Setter private String bh;
    @ApiModelProperty(value = "年级")
    @Getter @Setter private String nj;
    @ApiModelProperty(value = "在线人数")
    @Getter @Setter private long zxrs;

    public NjZxrsDTO(String bh, String nj, long zxrs) {
        this.bh = bh;
        this.nj = nj;
        this.zxrs = zxrs;
    }
}
