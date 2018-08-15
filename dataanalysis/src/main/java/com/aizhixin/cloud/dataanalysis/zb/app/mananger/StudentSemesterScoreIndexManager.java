package com.aizhixin.cloud.dataanalysis.zb.app.mananger;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudentSemesterScoreIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.respository.StudentSemesterScoreIndexRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class StudentSemesterScoreIndexManager {
    @Autowired
    private StudentSemesterScoreIndexRespository studentSemesterScoreIndexRespository;

    public StudentSemesterScoreIndex save(StudentSemesterScoreIndex entity) {
        return studentSemesterScoreIndexRespository.save(entity);
    }

    public List<StudentSemesterScoreIndex> save(List<StudentSemesterScoreIndex> entityList) {
        return studentSemesterScoreIndexRespository.save(entityList);
    }
}
