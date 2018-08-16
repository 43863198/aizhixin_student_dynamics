package com.aizhixin.cloud.dataanalysis.zb.app.mananger;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.ScoreIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.respository.ScoreIndexRespository;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreDwCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class ScoreIndexManager {
    @Autowired
    private ScoreIndexRespository scoreIndexRespository;

    public ScoreDwCountVO getDwCountIndex(String xxdm, String xn, String xqm) {
        List<ScoreDwCountVO> list =  scoreIndexRespository.findByXxdmSemsterCountIndex(xxdm, xn, xqm);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return new ScoreDwCountVO();
    }


    public ScoreIndex findByXxdmAndXnAndXqmAndBh(String xxdm, String xn, String xqm, String bh) {
        List<ScoreIndex> list =  scoreIndexRespository.findByXxdmAndXnAndXqmAndBh(xxdm, xn, xqm, bh);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<ScoreIndex> findByXxdmAndXnAndXqmAndPbhIsNull(String xxdm, String xn, String xqm) {
        return  scoreIndexRespository.findByXxdmAndXnAndXqmAndPbhIsNull(xxdm, xn, xqm);
    }

    public List<ScoreIndex> findByXxdmAndXnAndXqmAndPbh(String xxdm, String xn, String xqm, String parentBh) {
        return  scoreIndexRespository.findByXxdmAndXnAndXqmAndPbh(xxdm, xn, xqm, parentBh);
    }
}
