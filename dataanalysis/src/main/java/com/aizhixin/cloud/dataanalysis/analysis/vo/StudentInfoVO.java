package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-11
 */
@ApiModel(description="学生列表")
@Data
@ToString
public class StudentInfoVO {
    @ApiModelProperty(value = "学号")
    private String jobNumber;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "学院名称")
    private String collegeName;
    @ApiModelProperty(value = "专业")
    private String professionName;
    @ApiModelProperty(value = "班级")
    private String className;
    @ApiModelProperty(value = "年级")
    private String grade;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "出生日期")
    private String DateOfBirth;
    @ApiModelProperty(value = "民族")
    private String Nation;
    @ApiModelProperty(value = "籍贯")
    private String politicalOutlook;



}
