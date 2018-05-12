package com.aizhixin.cloud.dataanalysis.analysis.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AvgInfoDomain {
    private List<AvgDomain> avgDomains=new ArrayList<>();
    private List<AvgAgeDomain> avgAgeDomains=new ArrayList<>();
    private List<AvgNjDomain> avgNjDomains=new ArrayList<>();
    private List<AvgSexDomain> avgSexDomains=new ArrayList<>();
}
