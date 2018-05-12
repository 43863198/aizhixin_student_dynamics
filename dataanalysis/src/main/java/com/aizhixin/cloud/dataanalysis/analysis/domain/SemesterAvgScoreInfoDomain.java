package com.aizhixin.cloud.dataanalysis.analysis.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SemesterAvgScoreInfoDomain {
    private List<SemesterAvgScoreDomain> semesterAvgScoreDomains=new ArrayList<>();
    private List<SemesterAvgScoreAddRateDomain> semesterAvgScoreAddRateDomains=new ArrayList<>();
}
