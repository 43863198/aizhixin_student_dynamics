package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author: Created by jun.wang
 * @E-mail: wangjun@aizhixin.com
 * @Date: 2017-12-04
 */
@ApiModel(description="学院成绩概况统计信息")
@Data
@ToString
@NoArgsConstructor
public class TeachingScoreStatisticsDTO {
    @ApiModelProperty(value = "学院名称", required = false)
    private String colloegeName;
    @ApiModelProperty(value = "学院id", required = false)
    private Long colloegeId;
    @ApiModelProperty(value = "学生人数", required = false)
    private Integer studentNum;
    @ApiModelProperty(value = "不及格人数", required = false)
     private Integer failPassStuNum;
    @ApiModelProperty(value = "平均GPA", required = false)
     private Double avgGPA;
    @ApiModelProperty(value = "平均GPA", required = false)
    private Double avgScore;
    @ApiModelProperty(value = "学期名称", required = false)
    private String semesterName;
    @ApiModelProperty(value = "开课数量", required = false)
    private long curriculumNum;

//    public TeachingScoreStatisticsDTO(String colloegeName, Long colloegeId, Long studentNum, Long failPassStuNum, Double avgGPA, Double avgScore) {
//        this.colloegeName = colloegeName;
//        this.colloegeId = colloegeId;
//        this.studentNum = studentNum;
//        this.failPassStuNum = failPassStuNum;
//        this.avgGPA = avgGPA;
//        this.avgScore = avgScore;
//    }

    public TeachingScoreStatisticsDTO(String colloegeName, Long colloegeId, Integer studentNum, Integer failPassStuNum, Double avgGPA, Double avgScore) {
        this.colloegeName = colloegeName;
        this.colloegeId = colloegeId;
        this.studentNum = studentNum;
        this.failPassStuNum = failPassStuNum;
        this.avgGPA = avgGPA;
        this.avgScore = avgScore;
    }

    public TeachingScoreStatisticsDTO(long curriculumNum,Double avgGPA, Double avgScore) {
        this.avgGPA = avgGPA;
        this.avgScore = avgScore;
        this.curriculumNum=curriculumNum;
    }

}
