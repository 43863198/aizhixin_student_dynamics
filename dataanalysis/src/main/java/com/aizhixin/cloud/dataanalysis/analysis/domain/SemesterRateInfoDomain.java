package com.aizhixin.cloud.dataanalysis.analysis.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SemesterRateInfoDomain {
    private List<SemesterPassRateDomain> semesterPassRateDomains=new ArrayList<>();
    private List<SemesterAddRateDomain> semesterAddRateDomains=new ArrayList<>();
    private List<SemesterLeiJiRateDomain> semesterLeiJiRateDomainList=new ArrayList<>();
    private  List<SemesterLeiJiAddRateDomain> semesterLeiJiAddRateDomainList=new ArrayList<>();
}
