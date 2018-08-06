package com.aizhixin.cloud.dataanalysis.zb.app.domain;

import lombok.Data;

@Data
public class NjStatisticalInfoDomain {
    private String nj;
    private Long allTotal;
    private Long passTotal;
    private Long joinTotal;
}