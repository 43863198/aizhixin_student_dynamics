package com.aizhixin.cloud.dataanalysis.etl.study.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 学修改计划
 */
@ApiModel
@ToString
@NoArgsConstructor
public class EtlStudentStudyPlanXdztDTO {
    @ApiModelProperty(value = "学年")
    @Getter @Setter private Long id;
    @ApiModelProperty(value = "学号")
    @Getter @Setter private String xh;
    @ApiModelProperty(value = "课程号")
    @Getter @Setter private String kch;
    @ApiModelProperty(value = "修读状态")
    @Getter @Setter private Integer xdzt;
    @ApiModelProperty(value = "成绩")
    @Getter @Setter private Double cj;
    @ApiModelProperty(value = "学分")
    @Getter @Setter private Double jd;

    public EtlStudentStudyPlanXdztDTO(String xh, String kch, Double cj, Double jd) {
        this.xh = xh;
        this.kch = kch;
        this.cj = cj;
        this.jd = jd;
    }



    public EtlStudentStudyPlanXdztDTO(Long id, String xh, String kch) {
        this.id = id;
        this.xh = xh;
        this.kch = kch;
    }
}
