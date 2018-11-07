package com.aizhixin.cloud.dataanalysis.dd.rollcall.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel
@ToString
@NoArgsConstructor
public class PeriodDTO {
    @ApiModelProperty(value = "ID")
    @Getter @Setter private Long id;
    @ApiModelProperty(value = "节号")
    @Getter  @Setter private Integer no;
    @ApiModelProperty(value = "开始时间")
    @Getter  @Setter private String startTime;
    @ApiModelProperty(value = "结束时间")
    @Getter  @Setter private String endTime;

    public PeriodDTO(Long id, Integer no, String startTime, String endTime) {
        this.id = id;
        this.no = no;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
