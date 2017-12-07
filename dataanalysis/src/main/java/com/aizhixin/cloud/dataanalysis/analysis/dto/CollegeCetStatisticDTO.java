package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-07
 */
@ApiModel(description="四六各个学院统计信息")
@Data
@ToString
@NoArgsConstructor
public class CollegeCetStatisticDTO extends CetScoreStatisticsDTO{
    @ApiModelProperty(value = "学院id", required = false)
    private Long collegeId;
    @ApiModelProperty(value = "学院", required = false)
    private String collegeName;
}
