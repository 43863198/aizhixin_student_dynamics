package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.dto.PracticeStaticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolYearTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-25
 */
public interface SchoolYearTermRespository extends JpaRepository<SchoolYearTerm,String> {

    @Query("select max(a.teacherYear), from #{#entityName} a where a.orgId = :orgId and a.deleteFlag=0")
    List<Date> getPracticeStatics(@Param(value = "orgId")Long orgId,@Param(value = "teacherYear")int teacherYear,@Param(value = "semester")int semester);

}
