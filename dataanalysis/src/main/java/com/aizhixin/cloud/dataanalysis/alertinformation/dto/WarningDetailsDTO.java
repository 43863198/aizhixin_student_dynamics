package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-21
 */
@ApiModel(description="预警详情")
@Data
public class WarningDetailsDTO {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "告警人姓名")
    private String name;
    @ApiModelProperty(value = "学号")
    private String jobNumber;
    @ApiModelProperty(value = "所属学院")
    private String collogeName;
    @ApiModelProperty(value = "所属专业")
    private String professionalName;
    @ApiModelProperty(value = "所属班级")
    private String className;
    @ApiModelProperty(value = "学年")
    private String teachingYear;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "家庭住址")
    private String address;
    @ApiModelProperty(value = "家长联系方式")
    private String parentsContact;
    @ApiModelProperty(value = "预警名称")
    private String warningName;
    @ApiModelProperty(value = "预警时间")
    private Date warningTime;
    @ApiModelProperty(value = "预警等级")
    private int warningLevel;
    @ApiModelProperty(value = "产生预警条件")
    private String warningCondition;
    @ApiModelProperty(value = "预警标准")
    private String warningStandard;

}
