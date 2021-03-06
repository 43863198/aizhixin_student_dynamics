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
    private String collegeName;
    @ApiModelProperty(value = "学院id", required = false)
    private Long collegeId;
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
    private Integer curriculumNum;

//    public TeachingScoreStatisticsDTO(String collegeName, Long collegeId, Long studentNum, Long failPassStuNum, Double avgGPA, Double avgScore) {
//        this.collegeName = collegeName;
//        this.collegeId = collegeId;
//        this.studentNum = studentNum;
//        this.failPassStuNum = failPassStuNum;
//        this.avgGPA = avgGPA;
//        this.avgScore = avgScore;
//    }


    public TeachingScoreStatisticsDTO(String collegeName, Long collegeId, Integer studentNum, Integer failPassStuNum, Double avgGPA, Double avgScore) {
        this.collegeName = collegeName;
        this.collegeId = collegeId;
        this.studentNum = studentNum;
        this.failPassStuNum = failPassStuNum;
        this.avgGPA = avgGPA;
        this.avgScore = avgScore;
    }

    public TeachingScoreStatisticsDTO(Integer curriculumNum,Double avgGPA, Double avgScore) {
        this.avgGPA = avgGPA;
        this.avgScore = avgScore;
        this.curriculumNum=curriculumNum;
    }

}
