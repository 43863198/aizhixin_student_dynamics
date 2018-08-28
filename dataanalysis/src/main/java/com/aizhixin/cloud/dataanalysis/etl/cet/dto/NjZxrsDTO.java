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
public class NjZxrsDTO {
    @ApiModelProperty(value = "编号")
    @Getter @Setter private String bh;
    @ApiModelProperty(value = "年级")
    @Getter @Setter private String nj;
    @ApiModelProperty(value = "在校人数")
    @Getter @Setter private long zxrs;


    public NjZxrsDTO(String bh, String nj, long zxrs) {
        this.bh = bh;
        if (null != nj && nj.endsWith("级") && nj.length() >= 4) {
            this.nj = nj.substring(0, 4);
        } else {
            this.nj = nj;
        }
        this.zxrs = zxrs;
    }
}
