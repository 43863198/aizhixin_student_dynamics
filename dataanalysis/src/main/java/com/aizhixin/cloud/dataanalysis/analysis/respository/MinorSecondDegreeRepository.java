package com.aizhixin.cloud.dataanalysis.analysis.respository;


import com.aizhixin.cloud.dataanalysis.analysis.entity.MinorSecondDegreeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MinorSecondDegreeRepository extends JpaRepository<MinorSecondDegreeInfo, String> {
    List<MinorSecondDegreeInfo> findByXxdmAndXh(Long xxdm, String xh);

}
