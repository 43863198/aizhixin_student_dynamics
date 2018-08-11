package com.aizhixin.cloud.dataanalysis.zb.app.respository;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.CetBaseIndex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CetBaseIndexRespository extends JpaRepository<CetBaseIndex,Long>{
    List<CetBaseIndex> findByXxdmAndDhljAndKslxAndBhAndXnIsNull(String xxdm, String dhlj, String kslx, String bh);
    List<CetBaseIndex> findByXxdmAndDhljAndKslxAndPbhAndBhAndXnIsNull(String xxdm, String dhlj, String kslx, String pbh, String bh);
    List<CetBaseIndex> findByXxdmAndDhljAndKslxAndPbhAndXnIsNullOrderByBh(String xxdm, String dhlj, String kslx, String pbh);
}
