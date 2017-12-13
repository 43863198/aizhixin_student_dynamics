package com.aizhixin.cloud.dataanalysis.alertinformation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-13
 */
@ApiModel(description="按院统计处理率")
@Data
public class CollegeAlreadyProcessedRatioDTO implements Comparable<CollegeAlreadyProcessedRatioDTO>{

    @ApiModelProperty(value = "院系名称", required = false)
    protected String collegeName ;

    @ApiModelProperty(value = "比率", required = false)
    protected double ratio ;


    @Override
    public int compareTo(CollegeAlreadyProcessedRatioDTO o) {
        if (this.ratio > o.ratio) {
            return -1;//由高到底排序
        }else if (this.ratio < o.ratio){
            return 1;
        }else{
            return 0;
        }
    }
}
