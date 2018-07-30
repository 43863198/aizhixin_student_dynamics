package com.aizhixin.cloud.dataanalysis.notice.manager;

import com.aizhixin.cloud.dataanalysis.notice.entity.NotificationRecord;
import com.aizhixin.cloud.dataanalysis.notice.repository.NotificationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class NotificationRecordManager {

    @Autowired
    private NotificationRecordRepository repository;

    /**
     * 保存实体
     * @param entity 实体
     * @return	实体
     */
    public NotificationRecord save(NotificationRecord entity) {
        return repository.save(entity);
    }

    /**
     * 保存实体
     * @param entityList 实体
     * @return	实体
     */
    public List<NotificationRecord> save(List<NotificationRecord> entityList) {
        return repository.save(entityList);
    }
}
