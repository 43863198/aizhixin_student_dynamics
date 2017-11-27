package com.aizhixin.cloud.dataanalysis.setup.respository;

import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-13
 */

public interface AlarmSettingsRepository extends JpaRepository<AlarmSettings, String> {

    @Query("select ast from #{#entityName} ast where ast.deleteFlag = :deleteFlag and ast.warningType = :type and ast.orgId = :orgId")
	List<AlarmSettings> getAlarmSettingsByOrgIdAndType(@Param("orgId")Long orgId, @Param("type")String type, @Param("deleteFlag")int deleteFlag);

<<<<<<< HEAD
    @Query("select ast from #{#entityName} ast where ast.deleteFlag = :deleteFlag and ast.warningType = :type order by warningLevel")
	List<AlarmSettings> getAlarmSettingsByType(@Param("type")String type, @Param("deleteFlag")int deleteFlag);
=======
    @Query("select ast from #{#entityName} ast where ast.orgId = :orgId and ast.warningType = :type and ast.warningLevel = :warningLevel and ast.deleteFlag = :deleteFlag")
    AlarmSettings getAlarmSettingsByLevel (@Param("orgId")Long orgId, @Param("type")String type, @Param("warningLevel")int warningLevel,  @Param("deleteFlag")int deleteFlag);

>>>>>>> eb81828bbd0a11559a54e22c541844f84f6b95e0

}