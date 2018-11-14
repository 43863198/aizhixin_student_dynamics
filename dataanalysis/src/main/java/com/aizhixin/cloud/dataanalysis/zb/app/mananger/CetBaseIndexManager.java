package com.aizhixin.cloud.dataanalysis.zb.app.mananger;

import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.zb.app.dto.CetAvgYearsDTO;
import com.aizhixin.cloud.dataanalysis.zb.app.entity.CetBaseIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.respository.CetBaseIndexRespository;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.Cet46AvgYearsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional(readOnly = true)
public class CetBaseIndexManager {
    @Autowired
    private CetBaseIndexRespository cetBaseIndexRespository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    public List<Cet46AvgYearsVO> findNew11SemesterCet46AvgDc(String xxdm) {
        List<Cet46AvgYearsVO> list = new ArrayList<>();
        String sql = "SELECT CONCAT(XN, '-', XQM), ROUND(ZF/CKRC, 2) score FROM t_zb_djksjc WHERE XXDM=? AND BH=? AND KSLX=? AND DHLJ='1' ORDER BY CONCAT(XN,XQM) DESC LIMIT 11";
        List<CetAvgYearsDTO>  cet4List = jdbcTemplate.query(sql, new Object[] {xxdm, xxdm, '4'}, new int[] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}, (ResultSet rs, int var2 ) -> new CetAvgYearsDTO(rs.getString(1), rs.getDouble(2)));
        List<CetAvgYearsDTO>  cet6List = jdbcTemplate.query(sql, new Object[] {xxdm, xxdm, '6'}, new int[] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}, (ResultSet rs, int var2 ) -> new CetAvgYearsDTO(rs.getString(1), rs.getDouble(2)));
        Map<String, Cet46AvgYearsVO> map = new HashMap<>();
        if (null != cet4List) {
            for(CetAvgYearsDTO d : cet4List) {
                Cet46AvgYearsVO v = new Cet46AvgYearsVO(d.getXnxq(), d.getAvg());
                list.add(v);
                map.put(d.getXnxq(), v);
            }
        }
        if (null != cet6List && !map.isEmpty()) {
            for(CetAvgYearsDTO d : cet6List) {
                Cet46AvgYearsVO v = map.get(d.getXnxq());
                if (null != v) {
                    v.setCet6(d.getAvg());
                }
            }
        }
        //数据倒转
        List<Cet46AvgYearsVO> rs = new ArrayList<>();
        if (!list.isEmpty()) {
            for (int i = list.size() - 1; i >= 0; i--) {
                rs.add(list.get(i));
            }
        }
        return rs;
    }
}
