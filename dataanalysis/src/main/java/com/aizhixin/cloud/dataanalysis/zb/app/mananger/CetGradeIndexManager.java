package com.aizhixin.cloud.dataanalysis.zb.app.mananger;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.CetGradeIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.respository.CetGradeIndexRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class CetGradeIndexManager {
    @Autowired
    private CetGradeIndexRespository cetGradeIndexRespository;

    public List<CetGradeIndex> findNewLjAllSchool(String xxdm, String kslx) {
        return cetGradeIndexRespository.findByXxdmAndDhljAndKslxAndBhAndXnIsNullOrderByNj(xxdm, "2", kslx, xxdm);
    }
    public List<CetGradeIndex> findNewLj(String xxdm, String kslx, String pbh, String bh) {
        return cetGradeIndexRespository.findByXxdmAndDhljAndKslxAndPbhAndBhAndXnIsNullOrderByBh(xxdm, "2", kslx, pbh, bh);
    }
}
