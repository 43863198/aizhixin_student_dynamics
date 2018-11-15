package com.aizhixin.cloud.dataanalysis.etl.coursetimetable.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 桂理排课数据
 */
@ApiModel("桂理排课数据")
@ToString
@NoArgsConstructor
public class CourseTimeTableDTO {

    @ApiModelProperty(value = "学校代码")
    @Getter @Setter private Long orgId;
    @ApiModelProperty(value = "排课号")
    @Getter @Setter private String pkh;
    @ApiModelProperty(value = "教学班号")
    @Getter @Setter private String jxbh;
    @ApiModelProperty(value = "教学班名称")
    @Getter @Setter private String jxbmc;
    @ApiModelProperty(value = "上课周次")
    @Getter @Setter private String skzc;
    @ApiModelProperty(value = "上课时间")
    @Getter @Setter private String sksj;


    public CourseTimeTableDTO(Long orgId, String pkh, String jxbh, String jxbmc, String skzc, String sksj) {
        this.orgId = orgId;
        this.pkh = pkh;
        this.jxbh = jxbh;
        this.jxbmc = jxbmc;
        this.skzc = skzc;
        this.sksj = sksj;
    }
}
