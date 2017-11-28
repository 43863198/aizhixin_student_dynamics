package com.aizhixin.cloud.dataanalysis.setup.respository;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;

public interface AlarmRuleRespository extends PagingAndSortingRepository<AlarmRule, String> {
	
	@Query("select ar from com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule ar where ar.id in (:ids) ")
	List<AlarmRule> findAllByIds(@Param("ids") String[] ids);

	@Query("select ar from  #{#entityName} ar where ar.alarmSettingsId = :alarmSettingId and ar.serialNumber = :serialNumber and ar.deleteFlag = :deleteFlag")
	AlarmRule getBySettingIdAndSerialNumber(@Param("alarmSettingId")String alarmSettingId, @Param("serialNumber")int serialNumber, @Param("deleteFlag")int deleteFlag);

	@Query("select ar from  #{#entityName} ar where ar.alarmSettingsId = :alarmSettingId and ar.deleteFlag = :deleteFlag")
	List<AlarmRule> getByAlarmSettingId(@Param("alarmSettingId")String alarmSettingId, @Param("deleteFlag")int deleteFlag);


}
