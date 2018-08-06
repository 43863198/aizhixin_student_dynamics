package com.aizhixin.cloud.dataanalysis.analysis.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ApiModel(description="迎新报到率排行")
@NoArgsConstructor
@ToString
public class NewStudentReportDomain {
    @ApiModelProperty(value = "学年")
    private String teachingYear;
    @ApiModelProperty(value = "学期")
    private String semerster;
    @ApiModelProperty(value = "学院")
    private String collegeName;
    @ApiModelProperty(value = "新生总数量")
    private Long studentNumber;
    @ApiModelProperty(value = "已报到人数")
    private Long alreadyReport;

    public NewStudentReportDomain(String teachingYear, String semerster, String collegeName, Long studentNumber, Long alreadyReport) {
        this.teachingYear = teachingYear;
        this.semerster = semerster;
        this.collegeName = collegeName;
        this.studentNumber = studentNumber;
        this.alreadyReport = alreadyReport;
    }
}
