package com.aizhixin.cloud.dataanalysis.zb.app.mananger;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudyExceptionIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.respository.StudyExceptionIndexZbRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class StudyExceptionIndexManager {
    @Autowired
    private StudyExceptionIndexZbRespository studyExceptionIndexZbRespository;

    public StudyExceptionIndex save(StudyExceptionIndex entity) {
        return studyExceptionIndexZbRespository.save(entity);
    }

    public List<StudyExceptionIndex> save(List<StudyExceptionIndex> entityList) {
        return studyExceptionIndexZbRespository.save(entityList);
    }

    public void deleteByXxdmAndXnAndXqm(String xxdm, String xn, String xqm) {
        studyExceptionIndexZbRespository.deleteByXxdmAndXnAndXqm(xxdm, xn, xqm);
    }

}
