package com.aizhixin.cloud.dataanalysis.etl.cet.dto;


import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 校历
 */
@ApiModel
@ToString
@NoArgsConstructor
public class SchoolCalendarDTO {
    @ApiModelProperty(value = "学年")
    @Getter @Setter private String xn;
    @ApiModelProperty(value = "学学期年")
    @Getter @Setter private String xq;
    @ApiModelProperty(value = "开始日期")
    @Getter @Setter private Date ksrq;
    @ApiModelProperty(value = "结束日期")
    @Getter @Setter private Date jsrq;
    @ApiModelProperty(value = "总周数量")
    @Getter @Setter private int zs;

    public SchoolCalendarDTO(String xn, String xq, Date ksrq, Date jsrq, int zs) {
        this.xn = xn;
        this.xq = xq;
        this.ksrq = ksrq;
        this.jsrq = jsrq;
        this.zs = zs;
    }
    public SchoolCalendarDTO(String xn, String xq) {
        this.xn = xn;
        this.xq = xq;
        if (null != xn && null != xq) {
            int p = xn.indexOf("-");
            String n = null;
            String end = null;
            String start = null;
            if (p > 0) {
                if ("1".equals(xq)) {
                    n = xn.substring(0, p);
                    start = n + "-09-01";
                    end = (Integer.valueOf(n) + 1) + "-02-01";
                } else if ("2".equals(xq)) {
                    n = xn.substring(p + 1);
                    start = n + "-03-01";
                    end = n + "-08-01";
                }
                this.ksrq = DateUtil.parseShortDate(start);
                this.jsrq =  DateUtil.parseShortDate(end);
            }
        }
    }
}
