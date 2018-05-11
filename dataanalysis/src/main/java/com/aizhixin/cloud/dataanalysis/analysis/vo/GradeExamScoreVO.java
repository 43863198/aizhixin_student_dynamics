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
@ApiModel(description="等级考试")
@Data
@ToString
public class GradeExamScoreVO {
    @ApiModelProperty(value = "考试类型")
    private String examType;
    @ApiModelProperty(value = "最高分")
    private String maxScore;
    @ApiModelProperty(value = "考试日期")
    private String examDate;

}
