package com.aizhixin.cloud.dataanalysis.analysis.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-08
 */
@ApiModel(description="英语四六级统计信息")
@Data
@AllArgsConstructor
public class CetStatisticDomin {
    @ApiModelProperty(value = "六级参加人数", required = false)
    private int cetSixJoinNum;
    @ApiModelProperty(value = "六级通过人数", required = false)
    private int cetSixPassNum;
    @ApiModelProperty(value = "四级参加人数", required = false)
    private int cetForeJoinNum;
    @ApiModelProperty(value = "四级通过人数", required = false)
    private int cetForePassNum;

}
