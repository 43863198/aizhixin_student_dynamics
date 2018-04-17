package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.sql.Time;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-16
 */
@ApiModel(description="今日排课")
@Data
@ToString
public class ClassTodayVO {
    @ApiModelProperty(value = "第几节")
    private int period;
    @ApiModelProperty(value = "课程数量")
    private int courseCount;

}
