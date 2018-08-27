package com.aizhixin.cloud.dataanalysis.zb.app.respository;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudyExceptionIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyExceptionIndexZbRespository extends JpaRepository<StudyExceptionIndex,Long>{
    void deleteByXxdmAndXnAndXqm(String xxdm, String xn, String xqm);
    List<StudyExceptionIndex> findByXxdmAndXnAndXqm(String xxdm, String xn, String xqm);

    @Query("SELECT max(t.xn) FROM #{#entityName} t WHERE t.xxdm = :xxdm")
    String findLastestXnByXxdm(@Param(value = "xxdm") String xxdm);

    @Query("SELECT max(t.xqm) FROM #{#entityName} t WHERE t.xxdm = :xxdm AND t.xn = :xn")
    String findLastestXqmByXxdmAndXn(@Param(value = "xxdm") String xxdm, @Param(value = "xn") String xn);
}
