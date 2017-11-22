package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-17
 */
@ApiModel(description="按院统计")
@Data
public class CollegeStatisticsDTO implements Comparable<CollegeStatisticsDTO>{

    @ApiModelProperty(value = "院系名称", required = false)
    protected String collegeName ;

    @ApiModelProperty(value = "一级告警数量", required = false)
    protected int sum1;

    @ApiModelProperty(value = "二级告警数量", required = false)
    protected int sum2;

    @ApiModelProperty(value = "三级告警数量", required = false)
    protected int sum3;

    @ApiModelProperty(value = "告警总数量", required = false)
    protected int total;

    @Override
    public int compareTo(CollegeStatisticsDTO o) {
        if (this.total > o.total) {
            return -1;//由高到底排序
        }else if (this.total < o.total){
            return 1;
        }else{
            return 0;
        }
    }


}
