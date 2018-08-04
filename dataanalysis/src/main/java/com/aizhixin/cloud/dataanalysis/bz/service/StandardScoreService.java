package com.aizhixin.cloud.dataanalysis.bz.service;

import com.aizhixin.cloud.dataanalysis.bz.entity.StandardScore;
import com.aizhixin.cloud.dataanalysis.bz.manager.ScoreETLFromDBManager;
import com.aizhixin.cloud.dataanalysis.bz.manager.StandardScoreManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StandardScoreService {
    @Autowired
    private ScoreETLFromDBManager scoreETLFromDBManager;
    @Autowired
    private StandardScoreManager standardScoreManager;

    @Async
    public void etlDB2DB(Long orgId) {
        List<StandardScore> list = scoreETLFromDBManager.queryDBData(ScoreETLFromDBManager.SQL_ETL_DB_SRC, orgId, 0, 5000);
        int pn = 0;
        int pz = 1000;
        log.info("Load data page {}.", pn);
        while (null != list && !list.isEmpty()) {
            standardScoreManager.save(list);
            pn++;
            int start = pz * pn;
            log.info("Load data page {}.", start);
            list = scoreETLFromDBManager.queryDBData(ScoreETLFromDBManager.SQL_ETL_DB_SRC, orgId, start, 5000);
        }
    }
}
