package com.aizhixin.cloud.dataanalysis.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-07
 */
@ApiModel(description="英语成绩分析信息")
@Data
@ToString
public class CetScoreAnalysisDTO {
    private String type;
    private Long orgId;
    private Date examDate;
    private String collegeCode;
    private String professionCode;
    private String classCode;
    private String grade;
    private int joinNumber;
    private double avgScore;
    private double maxScore;
    private String sex;
}
