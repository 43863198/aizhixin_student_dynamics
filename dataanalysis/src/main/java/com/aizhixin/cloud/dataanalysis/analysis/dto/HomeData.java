package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@ApiModel(description="首页对象")
@ToString
public class HomeData <T> {
    @ApiModelProperty(value = "学年学期数据")
    @Getter @Setter
    private TeacherlYearData teacherlYearData = new TeacherlYearData();
    @ApiModelProperty(value = "list数据内容")
    @Getter @Setter private List<T> data = new ArrayList<>();
    @ApiModelProperty(value = "对象数据内容")
    @Getter @Setter private T objData;
}