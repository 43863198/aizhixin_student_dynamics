package com.aizhixin.cloud.dataanalysis.zb.app.mananger;

import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.CetBaseIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.respository.CetBaseIndexRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class CetBaseIndexManager {
    @Autowired
    private CetBaseIndexRespository cetBaseIndexRespository;

    public CetBaseIndex findNewLjAllSchool(String xxdm, String kslx) {
        List<CetBaseIndex> list = cetBaseIndexRespository.findByXxdmAndDhljAndKslxAndBhAndXnIsNull(xxdm, "2", kslx, xxdm);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return new CetBaseIndex();
    }

    public CetBaseIndex findNewDwLj(String xxdm, String kslx, String pbh, String bh) {
        List<CetBaseIndex> list = cetBaseIndexRespository.findByXxdmAndDhljAndKslxAndPbhAndBhAndXnIsNull(xxdm, "2", kslx, pbh, bh);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return new CetBaseIndex();
    }

    public List<CetBaseIndex> findSubDwLj(String xxdm, String kslx, String pbh) {
        return cetBaseIndexRespository.findByXxdmAndDhljAndKslxAndPbhAndXnIsNullOrderByBh(xxdm, "2", kslx, pbh);
    }

    public CetBaseIndex findDcOneDw(String xn, String xq, String xxdm, String kslx) {
        List<CetBaseIndex> list = cetBaseIndexRespository.findByXnAndXqmAndXxdmAndDhljAndKslxAndBh(xn, xq, xxdm, "1", kslx, xxdm);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return new CetBaseIndex();
    }

    public CetBaseIndex findDcOneDw(String xn, String xq, String xxdm, String kslx, String pbh, String bh) {
        List<CetBaseIndex> list = cetBaseIndexRespository.findByXnAndXqmAndXxdmAndDhljAndKslxAndPbhAndBh(xn, xq, xxdm, "1", kslx, pbh, bh);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return new CetBaseIndex();
    }

    public List<CetBaseIndex> findDcSubDw(String xn, String xq, String xxdm, String kslx, String pbh) {
        return cetBaseIndexRespository.findByXnAndXqmAndXxdmAndDhljAndKslxAndPbhOrderByBh(xn, xq, xxdm, "1", kslx, pbh);
    }

    public List<CetBaseIndex> findAllYearCount(String xxdm, String kslx, String dhlj) {
        PageRequest p = PageUtil.createNoErrorPageRequest(0, 11);
        Page<CetBaseIndex> page = cetBaseIndexRespository.findByXxdmAndDhljAndKslxAndBhAndXnIsNotNullOrderByXnDescXqmDesc(p, xxdm, dhlj, kslx, xxdm);
        return page.getContent();
    }

    public List<CetBaseIndex> findAllYearCount(String xxdm, String kslx, String dhlj, String pbh, String bh) {
        PageRequest p = PageUtil.createNoErrorPageRequest(0, 11);
        Page<CetBaseIndex> page = cetBaseIndexRespository.findByXxdmAndDhljAndKslxAndPbhAndBhAndXnIsNotNullOrderByXnDescXqmDesc(p, xxdm, dhlj, kslx, pbh, bh);
        return page.getContent();
    }
}
