package com.aizhixin.cloud.dataanalysis.analysis.domain;

import com.aizhixin.cloud.dataanalysis.analysis.vo.CetScoreNumberOfPeopleVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TotalInfoDomain {
    private List<CetScoreNumberOfPeopleVO>  csnpList=new ArrayList<>();
    private Map<String,CetSexStatisticalInfoDomain> mapSex=new HashMap<>();
    private List<AgeSexStatisticalInfoDomain> ageSexStatisticalInfoDomains=new ArrayList<>();
    private List<NjStatisticalInfoDomain> njStatisticalInfoDomains=new ArrayList<>();
    private List<ScoreScaleDomain> scoreScaleDomains=new ArrayList<>();
}
