package com.aizhixin.cloud.dataanalysis.alertinformation.repository;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AttachmentDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-14
 */
public interface AlertWarningInformationRepository extends JpaRepository<WarningInformation, String> {

    @Query("select aw from #{#entityName} aw where aw.deleteFlag = :deleteFlag and aw.warningType = :warningType and aw.orgId = :orgId and aw.defendantId = :defendantId")
    List<WarningInformation> getawinfoByDefendantId(@Param("orgId")Long orgId, @Param("warningType")String warningType, @Param("defendantId")Long defendantId, @Param("deleteFlag")int deleteFlag);

    @Query("select aw from #{#entityName} aw where aw.deleteFlag = :deleteFlag and aw.warningType = :warningType and aw.orgId = :orgId")
    List<WarningInformation> getawinfoByOrgIdAndWarningType(@Param("orgId")Long orgId, @Param("warningType")String warningType, @Param("deleteFlag")int deleteFlag);
    
    
    @Query("select aw from #{#entityName} aw where aw.deleteFlag = :deleteFlag and aw.warningType = :warningType and aw.orgId = :orgId and aw.warningState =:warningState ")
    List<WarningInformation> getawinfoByOrgIdAndWarningTypeAndState(@Param("orgId")Long orgId, @Param("warningType")String warningType, @Param("deleteFlag")int deleteFlag,@Param("warningState") int warningState);

    @Modifying(clearAutomatically = true) 
	@Query("update com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation wi set wi.deleteFlag = 1 where wi.deleteFlag= 0 and wi.warningType =:warningType and wi.orgId=:orgId and wi.teacherYear =:teacherYear and wi.semester =:semester")
	public void logicDeleteByOrgIdAndWarnType(@Param("warningType") String warningType,@Param("orgId") Long orgId,@Param("teacherYear") Integer teacherYear,@Param("semester") Integer semester);
    
    @Modifying(clearAutomatically = true) 
   	@Query("update com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation wi set wi.warningState = :warningState where wi.deleteFlag= 0 and wi.warningLevel =:warningLevel and wi.orgId in(:orgIds) ")
    void updateWarningStateByWarningLevel(@Param("warningState") int warningState,@Param("warningLevel") int warningLevel,@Param("orgIds") HashSet<Long> orgIds);

	public Long countByDeleteFlagAndWarningStateAndWarningLevel(int deleteFlag,int warningState,int warningLevel);

//    @Query("select aw from #{#entityName} aw where aw.deleteFlag= 0 and aw.warningLevel =:warningLevel and aw.orgId in(:orgIds)")
//    List<WarningInformation> findWarningInfoByWarningLevel(@Param("warningState") int warningState,@Param("warningLevel") int warningLevel,@Param("orgIds") HashSet<Long> orgIds);

    List<WarningInformation> findByWarningStateAndWarningLevelAndOrgIdIn(int warningState, int warningLevel, HashSet<Long> orgIds);


    @Modifying
    @Query("delete from #{#entityName} a where a.orgId = :orgId and a.warningType = :warningType and a.teacherYear = :teacherYear and a.semester = :semester")
    void deletePageDataByOrgIdAndTeacherYearAndSemester(@Param(value = "orgId") Long orgId, @Param(value = "warningType") String warningType, @Param(value = "teacherYear") Integer teacherYear, @Param(value = "semester") Integer semester);

}
