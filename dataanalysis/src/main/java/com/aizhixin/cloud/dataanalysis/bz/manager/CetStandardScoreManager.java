package com.aizhixin.cloud.dataanalysis.bz.manager;

import com.aizhixin.cloud.dataanalysis.bz.entity.CetStandardScore;
import com.aizhixin.cloud.dataanalysis.bz.repository.CetStandardScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class CetStandardScoreManager {
    @Autowired
    private CetStandardScoreRepository cetStandardScoreRepository;
    public void save(List<CetStandardScore> entitys) {
        cetStandardScoreRepository.save(entitys);
    }
}
