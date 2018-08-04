package com.aizhixin.cloud.dataanalysis.bz.manager;

import com.aizhixin.cloud.dataanalysis.bz.entity.StandardScore;
import com.aizhixin.cloud.dataanalysis.bz.repository.StandardScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class StandardScoreManager {
    @Autowired
    private StandardScoreRepository standardScoreRepository;
    public void save(List<StandardScore> entitys) {
        standardScoreRepository.save(entitys);
    }
}
