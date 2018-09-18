package com.aizhixin.cloud.dataanalysis.dd.rollcall.respository;


import com.aizhixin.cloud.dataanalysis.dd.rollcall.entity.Lastest3RollcallAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Lastest3RollcallAlertRepository extends JpaRepository<Lastest3RollcallAlert, Long> {
}