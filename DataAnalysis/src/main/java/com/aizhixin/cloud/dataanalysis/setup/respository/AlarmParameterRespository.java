package com.aizhixin.cloud.dataanalysis.setup.respository;


import java.util.List;

import com.aizhixin.cloud.dataanalysis.setup.entity.WarningType;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmParameter;

public interface AlarmParameterRespository extends PagingAndSortingRepository<AlarmParameter, Long> {
	
//	@Query("select ap from com.aizhixin.cloud.dataanalysis.setup.entity.AlarmParameter ap where ap.deleteFlag = :deleteFlag and ap.alarmSettings.type = :type order by ap.alarmSettings.id,ap.alarmSettings.inclusionLevel desc")
//	List<AlarmParameter> findRegisterAlarmParameters(@Param("deleteFlag")int deleteFlag,@Param("type")String type);
	
<<<<<<< HEAD
	List<AlarmParameter> findAllByDeleteFlagAndAlarmSettings_id(int deleteFlag,Long alarmSettingsId);
=======
//	List<AlarmParameter> findAllByDeleteFlagAndAlarmSettings_SetupCloseFlagAndAlarmSettings_TypeOrderByAlarmSettings_idAscLevelDesc(int deleteFlag,int setUp,String type);
>>>>>>> 599dc31c95aa1f013a2ee9dec8d09e9801e1b771
}
