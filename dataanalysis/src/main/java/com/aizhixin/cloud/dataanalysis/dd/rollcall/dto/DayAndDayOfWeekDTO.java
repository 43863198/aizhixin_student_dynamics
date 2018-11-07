package com.aizhixin.cloud.dataanalysis.dd.rollcall.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 星期和日期对应关系
 */
@ApiModel
@ToString
@NoArgsConstructor
public class DayAndDayOfWeekDTO {
    @ApiModelProperty(value = "星期几(1--7表示周一到周日)")
    @Getter  @Setter private Integer dayOfWeek;
    @ApiModelProperty(value = "日期yyyy-MM-dd")
    @Getter  @Setter private String day;

    public DayAndDayOfWeekDTO(Integer dayOfWeek, String day) {
        this.dayOfWeek = dayOfWeek;
        this.day = day;
    }
}
