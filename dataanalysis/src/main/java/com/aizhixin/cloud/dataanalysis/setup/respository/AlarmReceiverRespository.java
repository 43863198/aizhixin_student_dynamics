package com.aizhixin.cloud.dataanalysis.setup.respository;


import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmReceiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmReceiverRespository extends JpaRepository<AlarmReceiver, String> {

    List<AlarmReceiver> findByCollegeIdAndDeleteFlag(Long collegeId, Integer deleteFlag);
}
