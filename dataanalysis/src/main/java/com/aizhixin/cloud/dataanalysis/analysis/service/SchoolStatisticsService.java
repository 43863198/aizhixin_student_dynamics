package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolProfileDTO;
import com.aizhixin.cloud.dataanalysis.analysis.respository.SchoolStatisticsRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-29
 */
@Component
@Transactional
public class SchoolStatisticsService {
    @Autowired
    private SchoolStatisticsRespository newStudentInformationRespository;

    public SchoolProfileDTO getSchoolPersonStatistics(Long orgId) {
        return newStudentInformationRespository.getSchoolPersonStatistics(orgId);
    }


}
