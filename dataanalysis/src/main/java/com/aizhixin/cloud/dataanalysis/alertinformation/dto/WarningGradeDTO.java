package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-24
 */
@ApiModel(description="预警设置")
@Data
public class WarningGradeDTO {
    @ApiModelProperty(value = "预警设置id")
    private String alarmSettingsId ;
    @ApiModelProperty(value = "等级")
    private int grade;
    @ApiModelProperty(value = "等级名称")
    private String name;
    @ApiModelProperty(value = "开启状态(10:启用 ;20:关闭；")
    private int setupCloseFlag;
    @ApiModelProperty(value = "规则预警起始时间")
    private Date startTime;
    @ApiModelProperty(value = "规则描述及参数")
    List<WarningDescparameterDTO> describeParameter;

}
