package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-26
 */
@ApiModel(description="教学楼使用情况")
@Data
@ToString
public class TeachingBuildingsUsegeVO {
    @ApiModelProperty(value = "教学楼")
    private String teachingBuilding;
    @ApiModelProperty(value = "教室总数量")
    private int toal;
    @ApiModelProperty(value = "使用数量")
    private int usingNumber;

}
