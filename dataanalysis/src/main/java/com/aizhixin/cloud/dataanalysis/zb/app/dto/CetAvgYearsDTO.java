package com.aizhixin.cloud.dataanalysis.zb.app.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="英语等级考试均值历年人数分布")
@NoArgsConstructor
@ToString
public class CetAvgYearsDTO {
    @ApiModelProperty(value = "学年学期")
    @Getter @Setter private String xnxq;

    @ApiModelProperty(value = "人员总分")
    @Getter @Setter private Double avg = 0.0;

    public CetAvgYearsDTO (String xnxq, Double avg) {
        this.xnxq = xnxq;
        this.avg = avg;
    }
}
