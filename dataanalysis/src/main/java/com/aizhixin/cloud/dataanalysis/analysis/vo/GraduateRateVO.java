package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-14
 */
@ApiModel(description="毕业变化")
@Data
@ToString
public class GraduateRateVO {
    @ApiModelProperty(value = "学年")
    private String year;
    @ApiModelProperty(value = "毕业人数")
    private int number;
    @ApiModelProperty(value = "变化率")
    private double change;

}
