package com.aizhixin.cloud.dataanalysis.dd.rollcall.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 老师基本信息
 */
@ApiModel
@ToString
@NoArgsConstructor
public class OrgTeacherInfoDTO {
    @ApiModelProperty(value = "老师ID")
    @Getter  @Setter private Long teacherId;
    @ApiModelProperty(value = "老师工号")
    @Getter  @Setter private String teacherNo;
    @ApiModelProperty(value = "老师姓名")
    @Getter  @Setter private String teacherName;
    @ApiModelProperty(value = "学院")
    @Getter  @Setter private String collegeName;

    public OrgTeacherInfoDTO(Long teacherId, String teacherNo, String teacherName, String collegeName) {
        this.teacherId = teacherId;
        this.teacherNo = teacherNo;
        this.teacherName = teacherName;
        this.collegeName = collegeName;
    }
}
