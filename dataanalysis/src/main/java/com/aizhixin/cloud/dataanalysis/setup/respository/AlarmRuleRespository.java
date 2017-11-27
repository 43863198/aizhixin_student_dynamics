package com.aizhixin.cloud.dataanalysis.setup.respository;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;

public interface AlarmRuleRespository extends PagingAndSortingRepository<AlarmRule, String> {
	
	@Query("select ar from com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule ar where ar.id in (:ids) ")
	List<AlarmRule> findAllByIds(@Param("ids") String[] ids);
<<<<<<< HEAD
	
	List<AlarmRule> findAllByIdIn(Set<String> id);
=======

	@Query("select ar from  #{#entityName} ar where ar.alarmSettingsId = :alarmSettingsId ")
	List<AlarmRule> getAlarmRuleBySettingsId(@Param("alarmSettingsId") String alarmSettingsId);


>>>>>>> eb81828bbd0a11559a54e22c541844f84f6b95e0
}
