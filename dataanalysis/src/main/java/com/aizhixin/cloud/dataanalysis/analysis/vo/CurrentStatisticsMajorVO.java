package com.aizhixin.cloud.dataanalysis.analysis.vo;

import lombok.Data;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-24
 */
@Data
public class CurrentStatisticsMajorVO {
    private String majorName;
    private String collegeName;
    private int joinNumber;
    private int passNumber;
    private String rate;
    private String avg;
}
