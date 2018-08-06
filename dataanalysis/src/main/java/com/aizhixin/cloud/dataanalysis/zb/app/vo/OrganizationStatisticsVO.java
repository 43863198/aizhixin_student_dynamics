package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

@ApiModel(description="英语成绩人数分布")
@Data
@ToString
public class OrganizationStatisticsVO {
    private String name;
    private int total;
    private int joinTotal;
    private int passTotal;
}
