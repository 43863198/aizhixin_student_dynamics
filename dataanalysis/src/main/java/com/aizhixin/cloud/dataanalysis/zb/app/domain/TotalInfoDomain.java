package com.aizhixin.cloud.dataanalysis.zb.app.domain;

import com.aizhixin.cloud.dataanalysis.zb.app.domain.NjStatisticalInfoDomain;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.OrganizationStatisticsVO;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class TotalInfoDomain {
    private List<OrganizationStatisticsVO> list;
    private Map<String,Integer> sexMap;
    private List<NjStatisticalInfoDomain> njList;

}
