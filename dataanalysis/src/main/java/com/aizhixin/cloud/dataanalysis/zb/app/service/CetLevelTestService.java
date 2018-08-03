package com.aizhixin.cloud.dataanalysis.zb.app.service;

import com.aizhixin.cloud.dataanalysis.zb.app.vo.EnglishLevelBigScreenVO;
import com.aizhixin.cloud.dataanalysis.zb.manager.IndexAnalysisAppManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 英语等级考试指标处理逻辑
 */
@Component
public class CetLevelTestService {
    @Autowired
    private IndexAnalysisAppManager indexAnalysisAppManager;

    public List<EnglishLevelBigScreenVO> getCetLevelBigScreenPass(Long orgId) {
        if (null == orgId || orgId <= 0) {
            return new ArrayList<>();
        }
        return indexAnalysisAppManager.getNewLevelTestBigScreenPass(orgId);
    }
}
