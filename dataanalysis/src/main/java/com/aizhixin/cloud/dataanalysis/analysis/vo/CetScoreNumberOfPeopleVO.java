package com.aizhixin.cloud.dataanalysis.analysis.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-08
 */
@ApiModel(description="英语成绩人数分布")
@Data
@ToString
public class CetScoreNumberOfPeopleVO {
    private String name;
    private int joinNumber;
    private int passNumber;
    private String classify;
}
