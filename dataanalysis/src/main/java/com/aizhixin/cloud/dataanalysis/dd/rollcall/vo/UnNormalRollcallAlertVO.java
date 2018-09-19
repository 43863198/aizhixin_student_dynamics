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
public class UnNormalRollcallAlertVO {

    @ApiModelProperty(value = "学生ID")
    @Getter  @Setter private Long studentId;

    @ApiModelProperty(value = "学生学号")
    @Getter @Setter private String studentNo;

    @ApiModelProperty(value = "学生名称")
    @Column(name = "STUDENT_NAME")
    @Getter  @Setter private String studentName;

    @ApiModelProperty(value = "班级ID")
    @Getter  @Setter private Long classesId;

    @ApiModelProperty(value = "班级名称")
    @Getter  @Setter private String classesName;

    @ApiModelProperty(value = "专业")
    @Getter  @Setter private String professionalName;

    @ApiModelProperty(value = "学院")
    @Getter  @Setter private String collegeName;

    @ApiModelProperty(value = "年级")
    @Getter  @Setter private String grade;

    @ApiModelProperty(value = "课程ID")
    @Getter  @Setter private Long courseId;

    @ApiModelProperty(value = "课程名称")
    @Getter  @Setter private String courseName;

    @ApiModelProperty(value = "总应已上数量")
    @Getter  @Setter private Long shouldCount;

    @ApiModelProperty(value = "缺课数据")
    @Getter  @Setter private Long unNormal;

    public UnNormalRollcallAlertVO(Long studentId, String studentNo, String studentName, Long classesId, String classesName, String professionalName, String collegeName, Long courseId, Long shouldCount, Long unNormal) {
        this.studentId = studentId;
        this.studentNo = studentNo;
        this.studentName = studentName;
        this.classesId = classesId;
        this.classesName = classesName;
        this.professionalName = professionalName;
        this.collegeName = collegeName;
        this.courseId = courseId;
        this.shouldCount = shouldCount;
        this.unNormal = unNormal;
    }
}
