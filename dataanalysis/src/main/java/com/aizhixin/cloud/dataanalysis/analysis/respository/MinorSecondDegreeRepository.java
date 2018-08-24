package com.aizhixin.cloud.dataanalysis.analysis.respository;


import com.aizhixin.cloud.dataanalysis.analysis.entity.MinorSecondDegreeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface MinorSecondDegreeRepository extends JpaRepository<MinorSecondDegreeInfo, String>{

    @Query("select yxmc as name,yxsh as code,count(xh) as total from #{#entityName}  where fxyxsh IS NOT NULL and xxdm = :xxdm GROUP BY yxmc")
    List<Map<String,Object>> findByXxdmFxCount(@Param(value = "xxdm") Long xxdm);

    @Query("select yxmc as name,yxsh as code,count(xh) as total from #{#entityName} where exwyxsh IS NOT NULL and xxdm = :xxdm GROUP BY yxmc")
    List<Map<String,Object>> findByXxdmEXWCount(@Param(value = "xxdm") Long xxdm);

    @Query("select fxyxmc as name,yxsh as code,count(xh) as total from #{#entityName} where xxdm = :xxdm and fxyxmc = :yxmc GROUP BY fxyxmc")
    List<Map<String,Object>> findByXxdmAndWbFxCount(@Param(value = "xxdm") Long xxdm,@Param(value = "yxmc") String yxmc);

    @Query("select exwyxmc as name,yxsh as code,count(xh) as total from #{#entityName} where xxdm = :xxdm and exwyxmc = :yxmc GROUP BY exwyxmc")
    List<Map<String,Object>> findByXxdmAndWbExwCount(@Param(value = "xxdm") Long xxdm,@Param(value = "yxmc") String yxmc);

    Long countByXxdmAndFxyxshIsNotNull(Long xxdm);

    Long countByXxdmAndExwyxshIsNotNull(Long xxdm);

    Long countByXxdmAndYxshAndFxyxshIsNotNull(Long xxdm,String yxsh);

    Long countByXxdmAndYxshAndExwyxshIsNotNull(Long xxdm,String yxsh);



}
