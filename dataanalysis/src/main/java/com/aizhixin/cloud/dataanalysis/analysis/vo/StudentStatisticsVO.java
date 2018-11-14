package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-17
 */
@ApiModel(description="学生统计")
@ToString
@NoArgsConstructor
public class StudentStatisticsVO {
    @ApiModelProperty(value = "总人数")
    @Getter @Setter private int total;
    @ApiModelProperty(value = "在校人数")
    @Getter @Setter private int numberOfSchools;
    @ApiModelProperty(value = "休停人数")
    @Getter @Setter private int stopNumber;

    public StudentStatisticsVO (int total, int stopNumber) {
        this.total = total;
        this.stopNumber = stopNumber;
        this.numberOfSchools = total - stopNumber;
    }
}
