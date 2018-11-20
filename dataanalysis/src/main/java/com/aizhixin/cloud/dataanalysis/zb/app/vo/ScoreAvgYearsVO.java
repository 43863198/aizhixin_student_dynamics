package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description="学校成绩历年分布")
@NoArgsConstructor
@ToString
public class ScoreAvgYearsVO {
    @ApiModelProperty(value = "参考人数")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "评价成绩")
    @Getter @Setter private double pjcj;
    @ApiModelProperty(value = "评价gpa")
    @Getter @Setter private double pjjd;

    public ScoreAvgYearsVO(String xn, Double pjjd, Double pjcj) {
        this.xn = xn;
        this.pjjd = pjjd;
        this.pjcj = pjcj;
    }
}
