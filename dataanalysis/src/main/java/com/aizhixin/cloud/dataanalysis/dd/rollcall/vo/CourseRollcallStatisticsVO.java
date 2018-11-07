package com.aizhixin.cloud.dataanalysis.dd.rollcall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 课程考勤统计
 */
@ApiModel
@ToString
@NoArgsConstructor
public class CourseRollcallStatisticsVO extends RollcallStatisticsVO {
    @ApiModelProperty(value = "课程Id")
    @Getter  @Setter private Long courseId;
    @ApiModelProperty(value = "课程编码")
    @Getter  @Setter private String courseCode;
    @ApiModelProperty(value = "课程名称")
    @Getter  @Setter private String courseName;
    @ApiModelProperty(value = "老师名称")
    @Getter  @Setter private String teacherName;

    public CourseRollcallStatisticsVO(Long courseId, String courseName, String teacherName, Integer ydrs, Integer sdrs, Integer cdrs, Integer qjrs, Integer kkrs, Integer ztrs, Double dkl) {
        super(ydrs, sdrs, cdrs, qjrs, kkrs, ztrs, dkl);
        this.courseId = courseId;
        this.courseName = courseName;
        this.teacherName = teacherName;
    }
}
