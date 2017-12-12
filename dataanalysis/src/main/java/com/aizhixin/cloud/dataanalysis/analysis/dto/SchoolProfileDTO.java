package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-30
 */
@ApiModel(description="学校概况信息")
@Data
@NoArgsConstructor
@ToString
public class SchoolProfileDTO {
    @ApiModelProperty(value = "全校学生")
    private Long allStudent;

    @ApiModelProperty(value = "全校教师")
    private Long allTeacher;

    @ApiModelProperty(value = "辅导员数量")
    private Long allInstructor=0l;

    @ApiModelProperty(value = "在校学生")
    private Long inSchoolStudent;

    @ApiModelProperty(value = "校外实践学生")
    private Long outSchoolStudent=0l;

    @ApiModelProperty(value = "准毕业生")
    private Long readyGraduation;

    public SchoolProfileDTO(Long allStudent, Long allTeacher, Long allInstructor,  Long outSchoolStudent, Long readyGraduation) {
        this.allStudent = allStudent==null?0:allStudent;
        this.allTeacher = allTeacher==null?0:allTeacher;
        this.allInstructor = allInstructor==null?0:allInstructor;
        this.outSchoolStudent = outSchoolStudent==null?0:outSchoolStudent;
        this.inSchoolStudent = this.allStudent-this.outSchoolStudent;
        this.readyGraduation = readyGraduation;
    }
}
