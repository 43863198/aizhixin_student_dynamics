package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: Created by jun.wang
 * @E-mail: wangjun@aizhixin.com
 * @Date: 2017-11-30
 */
@ApiModel(description="四六级学情概况统计信息")
@Data
@ToString
@NoArgsConstructor
public class CetScoreStatisticsDTO {
    @ApiModelProperty(value = "参加人数", required = false)
    private Long cetJoinNum;
    @ApiModelProperty(value = "通过人数", required = false)
    private Long cetPassNum;
    @ApiModelProperty(value = "六级通过人数", required = false)
    private Long cetSixPassNum;
    @ApiModelProperty(value = "四级通过人数", required = false)
    private Long cetForePassNum;
    private Long cetForeJoinNum;
    private Long cetSixJoinNum;

    public CetScoreStatisticsDTO(Long cetSixPassNum, Long cetForePassNum, Long cetForeJoinNum, Long cetSixJoinNum) {
        this.cetSixPassNum = cetSixPassNum;
        this.cetForePassNum = cetForePassNum;
        this.cetForeJoinNum = cetForeJoinNum;
        this.cetSixJoinNum = cetSixJoinNum;
        this.cetJoinNum=this.cetForeJoinNum+ this.cetSixJoinNum;
        this.cetPassNum=this.cetSixPassNum+this.cetForePassNum;
    }
}
