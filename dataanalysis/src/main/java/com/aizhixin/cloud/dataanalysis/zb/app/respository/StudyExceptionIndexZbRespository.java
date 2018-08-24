package com.aizhixin.cloud.dataanalysis.zb.app.respository;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudyExceptionIndex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyExceptionIndexZbRespository extends JpaRepository<StudyExceptionIndex,Long>{
    void deleteByXxdmAndXnAndXqm(String xxdm, String xn, String xqm);
}
