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
@ApiModel(description="英语四六级统计信息")
@Data
@ToString
@NoArgsConstructor
public class CetStatisticDTO {
    @ApiModelProperty(value = "六级参加人数", required = false)
    private int cetSixJoinNum;
    @ApiModelProperty(value = "六级通过人数", required = false)
    private int cetSixPassNum;
    @ApiModelProperty(value = "六级通过率", required = false)
    private String cetSixPassRate;
    @ApiModelProperty(value = "四级参加人数", required = false)
    private int cetForeJoinNum;
    @ApiModelProperty(value = "四级通过人数", required = false)
    private int cetForePassNum;
    @ApiModelProperty(value = "四级通过率", required = false)
    private String cetForePassRate;
}
