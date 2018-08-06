package com.aizhixin.cloud.dataanalysis.analysis.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel(description = "新生报到率top10")
@NoArgsConstructor
@ToString
public class NewStudentReportDomain {
    @ApiModelProperty(value = "学年")
    @Getter @Setter private String teachingYear;
    @ApiModelProperty(value = "学期")
    @Getter @Setter private String semerster;
    @ApiModelProperty(value = "学院")
    @Getter @Setter private String collegeName;
    @ApiModelProperty(value = "新生总数量")
    @Getter @Setter private Long studentNumber;
    @ApiModelProperty(value = "已报到人数")
    @Getter @Setter private Long alreadyReport;

    public NewStudentReportDomain(String teachingYear, String semerster, String collegeName, Long studentNumber, Long alreadyReport) {
        this.teachingYear = teachingYear;
        this.semerster = semerster;
        this.collegeName = collegeName;
        this.studentNumber = studentNumber;
        this.alreadyReport = alreadyReport;
    }
}
