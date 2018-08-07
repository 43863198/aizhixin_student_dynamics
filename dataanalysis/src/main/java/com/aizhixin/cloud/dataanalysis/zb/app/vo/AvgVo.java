package com.aizhixin.cloud.dataanalysis.zb.app.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AvgVo {
    private String name;
    private Long joinTotal;
    private Long total;
    private Long passTotal;
    private BigDecimal totalScore;
    private BigDecimal avgScore;

}
