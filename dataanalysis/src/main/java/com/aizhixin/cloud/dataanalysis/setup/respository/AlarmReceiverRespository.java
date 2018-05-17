package com.aizhixin.cloud.dataanalysis.setup.respository;


import com.aizhixin.cloud.dataanalysis.common.domain.IdCountDTO;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmReceiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlarmReceiverRespository extends JpaRepository<AlarmReceiver, String> {

    List<AlarmReceiver> findByCollegeIdAndDeleteFlag(Long collegeId, Integer deleteFlag);

    @Query("select new com.aizhixin.cloud.dataanalysis.common.domain.IdCountDTO(t.collegeId, count(t.id)) from #{#entityName} t where t.deleteFlag = :deleteFlag and t.orgId=:orgId group by t.collegeId")
    List<IdCountDTO> countByOrgAndGroupByCollegeId(@Param(value = "orgId") Long orgId, @Param(value = "deleteFlag")Integer deleteFlag);

    List<AlarmReceiver> findByOrgIdAndDeleteFlag(Long orgId, Integer deleteFlag);
}
