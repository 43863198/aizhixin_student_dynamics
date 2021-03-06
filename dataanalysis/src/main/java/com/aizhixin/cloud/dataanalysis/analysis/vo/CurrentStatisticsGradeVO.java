package com.aizhixin.cloud.dataanalysis.analysis.vo;

import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-24
 */
@Data
public class CurrentStatisticsGradeVO {
    private String gradeName;
    private String collegeName;
    private int joinNumber;
    private int totalNumber;
    private int passNumber;
    private String rate;
    private String avg;
    private String max;
}
