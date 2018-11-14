package com.aizhixin.cloud.dataanalysis.zb.app.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="英语等级考试均值历年人数分布")
@NoArgsConstructor
@ToString
public class Cet46AvgYearsVO {
    @ApiModelProperty(value = "学年")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "学期")
    @Getter @Setter private String xq;

    @ApiModelProperty(value = "4级均值")
    @Getter @Setter private Double cet4 = 0.0;

    @ApiModelProperty(value = "6级均值")
    @Getter @Setter private Double cet6 = 0.0;

    public Cet46AvgYearsVO(String xnxq, Double cet4) {
        this.cet4 = cet4;
        if (null != xnxq) {
            String[] tt = xnxq.split("\\-");
            if (tt.length >= 3) {
                if ("1".equals(tt[2])) {
                    xq = "秋";
                    xn = tt[0];
                } else {
                    xq = "春";
                    xn = tt[1];
                }
            }
        }
    }
}
