package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

@ApiModel(description="考试统计")
@Data
@ToString
public class CurrentStatisticsVO {
    private int total;
    private int joinTotal;
    private int pass;
    private String avg;
    private String max;
    private String rate;
}
