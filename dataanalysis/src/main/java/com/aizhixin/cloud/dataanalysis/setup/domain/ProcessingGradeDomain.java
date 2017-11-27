package com.aizhixin.cloud.dataanalysis.setup.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-27
 */
@ApiModel(description="每个等级的处理方式设置信息")
@Data
public class ProcessingGradeDomain {
    @ApiModelProperty(value = "预警类等级", required = false)
    private int grade;
    @ApiModelProperty(value = "预警处理操作类型集合(发送学生10)", required = false)
    private int operationTypeSet1 = 10;
    @ApiModelProperty(value = "开启或关闭")
    private int setupCloseFlag1;
    @ApiModelProperty(value = "预警处理操作集合(手机短信 1 电子邮件 2 站内信 3 注:多个用“,”隔开)", required = false)
    private String operationSet1;
    @ApiModelProperty(value = "预警处理操作类型集合(发送辅导员20)", required = false)
    private int operationTypeSet2 = 20;
    @ApiModelProperty(value = "开启或关闭")
    private int setupCloseFlag2;
    @ApiModelProperty(value = "预警处理操作集合(手机短信 1 电子邮件 2 站内信 3 注:多个用“,”隔开)", required = false)
    private String operationSet2;
    @ApiModelProperty(value = "预警处理操作类型集合(发送院系领导 30)", required = false)
    private int operationTypeSet3 = 30;
    @ApiModelProperty(value = "开启或关闭")
    private int setupCloseFlag3;
    @ApiModelProperty(value = "预警处理操作集合(手机短信 1 电子邮件 2 站内信 3 注:多个用“,”隔开)", required = false)
    private String operationSet3;

}
