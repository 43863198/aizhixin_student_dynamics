package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.common.core.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.core.PublicErrorCode;
import com.aizhixin.cloud.dataanalysis.common.exception.CommonException;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmReceiver;
import com.aizhixin.cloud.dataanalysis.setup.manager.AlarmReceiverManager;
import com.aizhixin.cloud.dataanalysis.setup.vo.AlertReceiverVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *  告警信息接收人管理及相关操作逻辑
 */
@Component
@Transactional
public class AlarmReceiverService {
    @Autowired
    private AlarmReceiverManager alarmReceiverManager;

    @Transactional (readOnly = true)
    public AlertReceiverVO get(String id){
        AlarmReceiver alarmReceiver = alarmReceiverManager.findById(id);
        return new AlertReceiverVO (alarmReceiver);
    }

    public AlarmReceiver save(AlertReceiverVO alertReceiverVO){
        if (null == alertReceiverVO) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "输入参数是必须的");
        }
        if (StringUtils.isEmpty(alertReceiverVO.getCollegeId())) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "学院ID是必须的");
        }
        if (null == alertReceiverVO.getTeacherId() || alertReceiverVO.getTeacherId() <= 0) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "老师ID是必须的");
        }
        if (StringUtils.isEmpty(alertReceiverVO.getTeacherJobNumber())) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "老师教工号是必须的");
        }
        if (StringUtils.isEmpty(alertReceiverVO.getTeacherPhone())) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "老师手机号码是必须的");
        }
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setCollegeId(alertReceiverVO.getCollegeId());
        alarmReceiver.setCollegeName(alertReceiverVO.getCollegeName());
        alarmReceiver.setTeacherId(alertReceiverVO.getTeacherId());
        alarmReceiver.setTeacherJobNumber(alertReceiverVO.getTeacherJobNumber());
        alarmReceiver.setTeacherName(alertReceiverVO.getTeacherName());
        alarmReceiver.setTeacherPhone(alertReceiverVO.getTeacherPhone());
        //操作，最后修改人信息
        return alarmReceiverManager.save(alarmReceiver);
    }

    public AlarmReceiver update(String id, AlertReceiverVO alertReceiverVO){
        if (null == alertReceiverVO) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "输入参数是必须的");
        }
        if (StringUtils.isEmpty(id)) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "学院ID是必须的");
        }
        AlarmReceiver alarmReceiver = alarmReceiverManager.findById(id);
        if (null == alarmReceiver) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "根据ID没有查找到相应的接收人信息");
        }
        if (StringUtils.isEmpty(alertReceiverVO.getCollegeId())) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "学院ID是必须的");
        }
        if (null == alertReceiverVO.getTeacherId() || alertReceiverVO.getTeacherId() <= 0) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "老师ID是必须的");
        }
        if (StringUtils.isEmpty(alertReceiverVO.getTeacherJobNumber())) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "老师教工号是必须的");
        }
        if (StringUtils.isEmpty(alertReceiverVO.getTeacherPhone())) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "老师手机号码是必须的");
        }
        alarmReceiver.setCollegeId(alertReceiverVO.getCollegeId());
        alarmReceiver.setCollegeName(alertReceiverVO.getCollegeName());
        alarmReceiver.setTeacherId(alertReceiverVO.getTeacherId());
        alarmReceiver.setTeacherJobNumber(alertReceiverVO.getTeacherJobNumber());
        alarmReceiver.setTeacherName(alertReceiverVO.getTeacherName());
        alarmReceiver.setTeacherPhone(alertReceiverVO.getTeacherPhone());
        //操作，最后修改人信息
        return alarmReceiverManager.save(alarmReceiver);
    }

    public AlarmReceiver delete(String id){

        if (StringUtils.isEmpty(id)) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "学院ID是必须的");
        }
        AlarmReceiver alarmReceiver = alarmReceiverManager.findById(id);
        if (null == alarmReceiver) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "根据ID没有查找到相应的接收人信息");
        }
        alarmReceiver.setDeleteFlag(DataValidity.VALID.getIntValue());
        alarmReceiverManager.save(alarmReceiver);
        return alarmReceiver;
    }

    @Transactional (readOnly = true)
    public List<AlertReceiverVO>  getByCollegeId(Long collegeId){
        List<AlarmReceiver>  list = alarmReceiverManager.findByCollegeId(collegeId);
        List<AlertReceiverVO> rs = new ArrayList<>();
        for (AlarmReceiver alarmReceiver : list) {
            AlertReceiverVO v = new AlertReceiverVO(alarmReceiver);
            rs.add(v);
        }
        return rs;
    }

}
