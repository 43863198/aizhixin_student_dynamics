package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-17
 */
@ApiModel(description="学生统计")
@Data
@ToString
public class StudentStatisticsVO {
    @ApiModelProperty(value = "总人数")
    private int total;
    @ApiModelProperty(value = "在校人数")
    private int numberOfSchools;
    @ApiModelProperty(value = "休停人数")
    private int stopNumber;

}
