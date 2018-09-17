package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 老师课程预警
 */
@ApiModel
@ToString
@NoArgsConstructor
public class ClassesRollcallAlertVO {
    @ApiModelProperty(value = "日期")
    @Getter  @Setter private String day;
    @ApiModelProperty(value = "班级ID")
    @Getter  @Setter private Long classesId;
    @ApiModelProperty(value = "班级名称")
    @Getter  @Setter private String classesName;
    @ApiModelProperty(value = "年级")
    @Getter  @Setter private String grade;
    @ApiModelProperty(value = "辅导员")
    @Getter  @Setter private String teacherName;
    @ApiModelProperty(value = "学院")
    @Getter  @Setter private String collegeName;
    @ApiModelProperty(value = "专业")
    @Getter  @Setter private String professionalName;
    @ApiModelProperty(value = "到课率")
    @Getter  @Setter private Double dkl;
    public ClassesRollcallAlertVO(Long classesId, String teacherName) {
        this.classesId = classesId;
        this.teacherName = teacherName;
    }

    public ClassesRollcallAlertVO(String day, Long classesId, Double dkl) {
        this.day = day;
        this.classesId = classesId;
        this.dkl = dkl;
    }
    public ClassesRollcallAlertVO (Long classesId, String classesName, String grade, String professionalName, String collegeName) {
        this.classesId = classesId;
        this.classesName = classesName;
        this.grade = grade;
        this.professionalName = professionalName;
        this.collegeName =  collegeName;
    }
}
