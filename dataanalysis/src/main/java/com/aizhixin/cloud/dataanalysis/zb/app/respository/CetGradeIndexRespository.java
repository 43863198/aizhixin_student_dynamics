package com.aizhixin.cloud.dataanalysis.zb.app.respository;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.CetGradeIndex;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CetGradeIndexRespository extends JpaRepository<CetGradeIndex,Long>{
    List<CetGradeIndex> findByXxdmAndDhljAndKslxAndBhAndXnIsNullOrderByNj(String xxdm, String dhlj, String kslx, String bh);
    List<CetGradeIndex> findByXxdmAndDhljAndKslxAndPbhAndBhAndXnIsNullOrderByBh(String xxdm, String dhlj, String kslx, String pbh, String bh);

    List<CetGradeIndex> findByXnAndXqmAndXxdmAndDhljAndKslxAndBhOrderByNj(String xn, String xq, String xxdm, String dhlj, String kslx, String bh);
    List<CetGradeIndex> findByXnAndXqmAndXxdmAndDhljAndKslxAndPbhAndBhOrderByBh(String xn, String xq, String xxdm, String dhlj, String kslx, String pbh, String bh);
}
