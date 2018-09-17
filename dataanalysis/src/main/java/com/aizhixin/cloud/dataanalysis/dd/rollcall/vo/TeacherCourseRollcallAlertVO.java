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
public class TeacherCourseRollcallAlertVO {
    @ApiModelProperty(value = "日期")
    @Getter  @Setter private String day;
    @ApiModelProperty(value = "老师ID")
    @Getter  @Setter private Long teacherId;
    @ApiModelProperty(value = "课程ID")
    @Getter  @Setter private Long courseId;
    @ApiModelProperty(value = "课程名")
    @Getter  @Setter private String courseName;
    @ApiModelProperty(value = "课程节")
    @Getter  @Setter private String period;
    @ApiModelProperty(value = "到课率")
    @Getter  @Setter private Double dkl;

    public TeacherCourseRollcallAlertVO(String day, Long teacherId, Long courseId, String courseName, String period, Double dkl) {
        this.day = day;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.period = period;
        this.dkl = dkl;
    }
}
