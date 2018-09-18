package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

@ApiModel
@ToString
@NoArgsConstructor
public class Lastest3RollcallAlertVO {

    @ApiModelProperty(value = "学生学号")
    @Getter @Setter private String studentNo;

    @ApiModelProperty(value = "学生名称")
    @Column(name = "STUDENT_NAME")
    @Getter  @Setter private String studentName;

    @ApiModelProperty(value = "班级名称")
    @Getter  @Setter private String classesName;

    @ApiModelProperty(value = "专业")
    @Getter  @Setter private String professionalName;

    @ApiModelProperty(value = "学院")
    @Getter  @Setter private String collegeName;

    @ApiModelProperty(value = "统计时间范围")
    @Getter  @Setter private String dateRange;

    @ApiModelProperty(value = "应到")
    @Getter  @Setter private Long shouldCount;

    @ApiModelProperty(value = "实到")
    @Getter  @Setter private Long normal;

    @ApiModelProperty(value = "到课率")
    @Getter  @Setter private Double dkl;

    public Lastest3RollcallAlertVO (String studentNo, String studentName, String classesName, String professionalName, String collegeName, String dateRange, Long shouldCount, Long normal, Double dkl) {
        this.studentNo = studentNo;
        this.studentName = studentName;
        this.classesName = classesName;
        this.professionalName = professionalName;
        this.collegeName = collegeName;
        this.dateRange = dateRange;
        this.shouldCount = shouldCount;
        this.normal = normal;
        this.dkl = dkl;
    }
}
