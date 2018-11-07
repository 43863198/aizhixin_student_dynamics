package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel
@ToString
@NoArgsConstructor
public class StudentRollcallAlertVO {

    @ApiModelProperty(value = "学生ID")
    @Getter  @Setter private Long studentId;

    @ApiModelProperty(value = "学生学号")
    @Getter @Setter private String studentNo;

    @ApiModelProperty(value = "学生名称")
    @Getter  @Setter private String studentName;

    @ApiModelProperty(value = "班级ID")
    @Getter  @Setter private Long classesId;

    @ApiModelProperty(value = "班级名称")
    @Getter  @Setter private String classesName;

    @ApiModelProperty(value = "专业")
    @Getter  @Setter private String professionalName;

    @ApiModelProperty(value = "学院")
    @Getter  @Setter private String collegeName;

    @ApiModelProperty(value = "应到")
    @Getter  @Setter private Long shouldCount;

    @ApiModelProperty(value = "实到")
    @Getter  @Setter private Long normal;

    @ApiModelProperty(value = "到课率")
    @Getter  @Setter private Double dkl;

    public StudentRollcallAlertVO(Long studentId, Long shouldCount, Long normal, Double dkl) {
        this.studentId = studentId;
        this.shouldCount = shouldCount;
        this.normal = normal;
        this.dkl = dkl;
    }
    public StudentRollcallAlertVO(Long studentId, String studentNo, String studentName, Long classesId, String classesName, String professionalName, String collegeName) {
        this.studentId = studentId;
        this.studentNo = studentNo;
        this.studentName = studentName;
        this.classesId = classesId;
        this.classesName = classesName;
        this.professionalName = professionalName;
        this.collegeName = collegeName;
    }
}
