package com.aizhixin.cloud.dataanalysis.analysis.respository;


import com.aizhixin.cloud.dataanalysis.analysis.entity.MinorSecondDegreeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface MinorSecondDegreeRepository extends JpaRepository<MinorSecondDegreeInfo, String>{

    @Query("SELECT zymc as zymc,zyh as zyh FROM #{#entityName} WHERE fxzyh IS NOT NULL AND xxdm=:xxdm AND yxsh=:yxsh AND zyh=:zyh AND xn = :xn AND xqm = :xqm")
    List<Map<String,Object>> countByXxdmAndYxshAndZyhFx(@Param(value = "xxdm")Long xxdm,@Param(value = "yxsh")String yxsh,@Param(value = "zyh")String zyh,@Param(value = "xn")String xn,@Param(value = "xqm")String xqm);

    @Query("SELECT zymc as zymc,zyh as zyh FROM #{#entityName} WHERE exwyxsh IS NOT NULL AND xxdm=:xxdm AND yxsh=:yxsh AND zyh=:zyh AND xn = :xn AND xqm = :xqm")
    List<Map<String,Object>> countByXxdmAndYxshAndZyhExw(@Param(value = "xxdm")Long xxdm,@Param(value = "yxsh")String yxsh,@Param(value = "zyh")String zyh,@Param(value = "xn")String xn,@Param(value = "xqm")String xqm);

    @Query("SELECT zymc as zymc,zyh as zyh FROM #{#entityName} WHERE xxdm=:xxdm AND fxyxsh=:yxsh AND  yxsh <> :yxsh AND fxzyh=:zyh AND xn = :xn AND xqm = :xqm")
    List<Map<String,Object>> countByXxdmAndYxshWBFX(@Param(value = "xxdm")Long xxdm,@Param(value = "yxsh")String yxsh,@Param(value = "zyh")String zyh,@Param(value = "xn")String xn,@Param(value = "xqm")String xqm);

    @Query("SELECT zymc as zymc,zyh as zyh FROM #{#entityName} WHERE  xxdm=:xxdm AND exwyxsh=:yxsh AND yxsh <> :yxsh AND exwzyh=:zyh AND xn = :xn AND xqm = :xqm")
    List<Map<String,Object>> countByXxdmAndYxshWBEXW(@Param(value = "xxdm")Long xxdm,@Param(value = "yxsh")String yxsh,@Param(value = "zyh")String zyh,@Param(value = "xn")String xn,@Param(value = "xqm")String xqm);


    @Query("select yxmc as name,yxsh as code from #{#entityName} where fxyxsh IS NOT NULL and xxdm = :xxdm and yxsh=:yxsh and xn = :xn and xqm = :xqm")
    List<Map<String,Object>> countByXxdmAndYxshFxCount(@Param(value = "xxdm") Long xxdm,@Param(value = "yxsh")String yxsh,@Param(value = "xn")String xn,@Param(value = "xqm")String xqm);

    @Query("select yxmc as name,yxsh as code from #{#entityName} where exwyxsh IS NOT NULL and xxdm = :xxdm and yxsh=:yxsh and xn = :xn and xqm = :xqm")
    List<Map<String,Object>> countByXxdmAndYxshEXWCount(@Param(value = "xxdm") Long xxdm,@Param(value = "yxsh")String yxsh,@Param(value = "xn")String xn,@Param(value = "xqm")String xqm);

    @Query("select yxmc as name,yxsh as code from #{#entityName} where xxdm = :xxdm and fxyxsh = :yxsh and yxsh <> :yxsh and xn = :xn and xqm = :xqm")
    List<Map<String,Object>> countByXxdmAndYxshWbFxCount(@Param(value = "xxdm") Long xxdm,@Param(value = "yxsh") String yxsh,@Param(value = "xn")String xn,@Param(value = "xqm")String xqm);

    @Query("select exwyxmc as name,yxsh as code from #{#entityName} where xxdm = :xxdm and exwyxsh = :yxsh and yxsh <> :yxsh and xn = :xn and xqm = :xqm")
    List<Map<String,Object>> countByXxdmAndYxshWbExwCount(@Param(value = "xxdm") Long xxdm,@Param(value = "yxsh") String yxsh,@Param(value = "xn")String xn,@Param(value = "xqm")String xqm);

    Long countByXxdmAndXnAndXqmAndFxyxshIsNotNull(Long xxdm,String xn,String xqm);

    Long countByXxdmAndXnAndXqmAndExwyxshIsNotNull(Long xxdm,String xn,String xqm);

    Long countByXxdmAndYxshAndXnAndXqmAndFxyxshIsNotNull(Long xxdm,String yxsh,String xn,String xqm);

    Long countByXxdmAndYxshAndXnAndXqmAndExwyxshIsNotNull(Long xxdm,String yxsh,String xn,String xqm);



}
