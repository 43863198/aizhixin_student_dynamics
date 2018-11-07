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
        int pn = 0;
        int pz = 5000;
        List<StandardScore> list = scoreETLFromDBManager.queryDBData(ScoreETLFromDBManager.SQL_ETL_DB_SRC, orgId, pn, 5000);
        log.info("Load data page {}.", pn);
        while (null != list && !list.isEmpty()) {
            standardScoreManager.save(list);
            pn++;
            int start = pz * pn;
            log.info("Load data page {}.", start);
            list = scoreETLFromDBManager.queryDBData(ScoreETLFromDBManager.SQL_ETL_DB_SRC, orgId, start, pz);
        }
    }

    @Async
    public void etlDB2DB(Long orgId, String xn, String xq) {
        int p = xn.indexOf("-");
        if ("1".equals(xq)) {
            xq = "2";
            if (p > 0)
                xn = xn.substring(0, p);
        } else {
            xq = "1";
            if (p > 0)
                xn = xn.substring(p + 1);
        }
        List<StandardScore> list = scoreETLFromDBManager.queryOldSemesterDBData(orgId, xn, xq);
        log.info("Load data page {}.", list.size());
        if (!list.isEmpty()) {
            standardScoreManager.save(list);
        }
    }


    /**
     * 长江数据清洗
     */
    @Async
    public void etlCjDB2DB(String xxdm, String xn, Integer xq) {
        List<StandardScore> list = scoreETLFromDBManager.queryChangjiangSemesterDBData(xxdm, xn, xq);
        log.info("Load data page {}.", list.size());
        if (!list.isEmpty()) {
            standardScoreManager.save(list);
        }
    }
}
