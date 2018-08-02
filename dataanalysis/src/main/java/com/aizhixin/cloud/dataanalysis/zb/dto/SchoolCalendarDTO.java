package com.aizhixin.cloud.dataanalysis.zb.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 校历
 */
@ApiModel
@ToString
@NoArgsConstructor
public class SchoolCalendarDTO {
    @ApiModelProperty(value = "学年")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "学学期年")
    @Getter @Setter private String xq;
    @ApiModelProperty(value = "开始日期")
    @Getter @Setter private Date ksrq;
    @ApiModelProperty(value = "结束日期")
    @Getter @Setter private Date jsrq;
    @ApiModelProperty(value = "总周数量")
    @Getter @Setter private int zs;

    public SchoolCalendarDTO(String xn, String xq, Date ksrq, Date jsrq, int zs) {
        this.xn = xn;
        this.xq = xq;
        this.ksrq = ksrq;
        this.jsrq = jsrq;
        this.zs = zs;
    }
}
