package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-05
 */
@ApiModel(description="英语考试单次数据分析---数据统计")
@Data
@ToString
public class CetSingleDataStatisticsVO {
    @ApiModelProperty(value = "参加人数")
    private int joinNumber;
    @ApiModelProperty(value = "平均分")
    private String average;
    @ApiModelProperty(value = "最高分")
    private String highestScore;
    @ApiModelProperty(value = "通过人数")
    private int passNumber;
    @ApiModelProperty(value = "单次通过率")
    private String passRate;


}
