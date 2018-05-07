package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-07
 */
@ApiModel(description="英语成绩分析")
@Data
@ToString
public class CetScoreAnalysisVO {
    private String name;
    private String value;
    private String classify;
}
