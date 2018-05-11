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
@ApiModel(description="课程成绩统计")
@Data
@ToString
public class CourseScoreVO {
    @ApiModelProperty(value = "课程数")
    private int courseTotalNumber;
    @ApiModelProperty(value = "课程成绩通过数")
    private int coursePassNumber;
    @ApiModelProperty(value = "挂科课数")
    private int courseFailNumber;
    @ApiModelProperty(value = "GPA")
    private String GAP;
    @ApiModelProperty(value = "同专业同年级排名")
    private int ranking;

}
