package com.aizhixin.cloud.dataanalysis.zb.app.respository;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.StudentSemesterScoreIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSemesterScoreIndexRespository extends JpaRepository<StudentSemesterScoreIndex,Long>{
    Page<StudentSemesterScoreIndex> findByXxdmAndXnAndXqmAndYxsh(Pageable pageable, String xxdm, String xn, String xq, String yxsh);
    Page<StudentSemesterScoreIndex> findByXxdmAndXnAndXqmAndYxshAndZyh(Pageable pageable, String xxdm, String xn, String xq, String yxsh, String zyh);
}
