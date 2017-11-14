package com.aizhixin.cloud.dataanalysis.setup.respository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmParameter;

public interface AlarmParameterRespository extends PagingAndSortingRepository<AlarmParameter, Long> {
	
//	@Query("select ap from com.aizhixin.cloud.dataanalysis.setup.entity.AlarmParameter ap where ap.deleteFlag = :deleteFlag and ap.alarmSettings.type = :type order by ap.alarmSettings.id,ap.alarmSettings.inclusionLevel desc")
//	List<AlarmParameter> findRegisterAlarmParameters(@Param("deleteFlag")int deleteFlag,@Param("type")String type);
	
	List<AlarmParameter> findAllByDeleteFlagAndAlarmSettings_SetupCloseFlagAndAlarmSettings_TypeOrderByAlarmSettings_idAscLevelDesc(int deleteFlag,int setUp,String type);
}
