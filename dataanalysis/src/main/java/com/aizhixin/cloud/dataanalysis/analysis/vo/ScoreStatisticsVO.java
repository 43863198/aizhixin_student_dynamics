package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-10
 */
@ApiModel(description="考试统计")
@Data
@ToString
public class ScoreStatisticsVO {
    private int total;
    private int pass;
    private String avg;
    private String max;
    private String rate;


}
