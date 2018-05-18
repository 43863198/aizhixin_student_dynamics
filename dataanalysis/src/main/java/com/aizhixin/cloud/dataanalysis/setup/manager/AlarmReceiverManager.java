package com.aizhixin.cloud.dataanalysis.setup.manager;

import com.aizhixin.cloud.dataanalysis.common.core.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.domain.IdCountDTO;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmReceiver;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmReceiverRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 告警信息接收人DAO对象
 */
@Component
@Transactional
public class AlarmReceiverManager {

    @Autowired
    private AlarmReceiverRespository alarmReceiverRespository;
    public AlarmReceiver save (AlarmReceiver entity) {
        return alarmReceiverRespository.save(entity);
    }
    @Transactional (readOnly = true)
    public AlarmReceiver findById(String id) {
        return alarmReceiverRespository.findOne(id);
    }
    @Transactional (readOnly = true)
    public List<AlarmReceiver> findByCollegeId(Long collegeId) {
        return  alarmReceiverRespository.findByCollegeIdAndDeleteFlag(collegeId, DataValidity.VALID.getIntValue());
    }
    @Transactional (readOnly = true)
    public List<IdCountDTO> countByOrgAndGroupByCollegeId(Long orgId) {
        return  alarmReceiverRespository.countByOrgAndGroupByCollegeId(orgId, DataValidity.VALID.getIntValue());
    }
    @Transactional (readOnly = true)
    public List<AlarmReceiver> findByOrgAll(Long orgId) {
        return alarmReceiverRespository.findByOrgIdAndDeleteFlag(orgId, DataValidity.VALID.getIntValue());
    }
    @Transactional (readOnly = true)
    public long countByTeacherIdAndCollege(Long teacherId, Long collegeId) {
        return alarmReceiverRespository.countByTeacherdIdAndCollegeIdAndDeleteFlag(teacherId, collegeId, DataValidity.VALID.getIntValue());
    }
    @Transactional (readOnly = true)
    public long countByTeacherIdAndCollegeAndIdNot(Long teacherId, Long collegeId, String id) {
        return alarmReceiverRespository.countByTeacherIdAndCollegeIdAndIdNotAndDeleteFlag(teacherId, collegeId, id, DataValidity.VALID.getIntValue());
    }
}
