package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-04-25
 */
@ApiModel(description="今日课程详情")
@Data
@ToString
public class CurriculumTableDetailsVO {
    @ApiModelProperty(value = "教学班名称")
    private String teachingClassName;
    @ApiModelProperty(value = "开课单位")
    private String courseUnit;
    @ApiModelProperty(value = "教师姓名")
    private String teacherName;
    @ApiModelProperty(value = "开始节")
    private Integer startSection;
    @ApiModelProperty(value = "结束节")
    private Integer endSection;
//    @ApiModelProperty(value = "校区")
//    private String campus;
    @ApiModelProperty(value = "教学楼")
    private String teachingBuilding;
    @ApiModelProperty(value = "教室")
    private String classRoom;

}
