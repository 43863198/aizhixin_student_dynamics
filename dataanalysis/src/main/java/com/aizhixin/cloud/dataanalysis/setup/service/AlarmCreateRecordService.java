package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmCreateRecord;
import com.aizhixin.cloud.dataanalysis.setup.manager.AlarmCreateRecordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  预警信息生成日志业务处理逻辑
 */
@Component
public class AlarmCreateRecordService {
//    private final static Logger LOG = LoggerFactory.getLogger(AlarmCreateRecordService.class);
    @Autowired
    private AlarmCreateRecordManager alarmCreateRecordManager;
    public AlarmCreateRecord save (AlarmCreateRecord entity) {
        return alarmCreateRecordManager.save(entity);
    }

    public PageData<AlarmCreateRecord> findByOrgIdAndCreatedId(Long orgId, Long createId, Integer pageNo, Integer pageSize) {
        return alarmCreateRecordManager.findByOrgIdAndCreatedId(orgId, createId, pageNo, pageSize);
    }
}
