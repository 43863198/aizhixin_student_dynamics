package com.aizhixin.cloud.dataanalysis.zb.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 学年学期
 */
@ApiModel
@ToString
@NoArgsConstructor
public class XnXqDTO {
    @ApiModelProperty(value = "学年")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "学期")
    @Getter @Setter private String xq;

    public XnXqDTO(String xn, String xq) {
        this.xn = xn;
        this.xq = xq;
    }
}
