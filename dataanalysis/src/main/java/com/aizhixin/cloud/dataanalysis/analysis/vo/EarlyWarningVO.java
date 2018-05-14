package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-14
 */
@ApiModel(description="预警信息")
@Data
@ToString
public class EarlyWarningVO {
    @ApiModelProperty(value = "预警时间")
    private String warningTime;
    @ApiModelProperty(value = "预警名称")
    private String warningName;
    @ApiModelProperty(value = "预警等级")
    private int warningLevel;
    @ApiModelProperty(value = "状态")
    private int warningState;
    @ApiModelProperty(value = "预警原因")
    private String warningCondition;
    @ApiModelProperty(value = "数据源")
    private String warningSource;
}
