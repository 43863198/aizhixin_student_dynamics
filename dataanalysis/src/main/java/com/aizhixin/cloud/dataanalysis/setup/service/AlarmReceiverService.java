package com.aizhixin.cloud.dataanalysis.setup.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.CollegeWarningInfoDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.common.core.PublicErrorCode;
import com.aizhixin.cloud.dataanalysis.common.domain.IdCountDTO;
import com.aizhixin.cloud.dataanalysis.common.exception.CommonException;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.feign.OrgManagerFeignService;
import com.aizhixin.cloud.dataanalysis.feign.vo.CollegeVO;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmReceiver;
import com.aizhixin.cloud.dataanalysis.setup.manager.AlarmReceiverManager;
import com.aizhixin.cloud.dataanalysis.setup.vo.AlertReceiverVO;
import com.aizhixin.cloud.dataanalysis.setup.vo.CollegeAlertReceiverVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  告警信息接收人管理及相关操作逻辑
 */
@Component
@Transactional
public class AlarmReceiverService {
    private final static Logger LOG = LoggerFactory.getLogger(AlarmReceiverService.class);
    @Autowired
    private AlarmReceiverManager alarmReceiverManager;
    @Autowired
    private OrgManagerFeignService orgManagerFeignService;
    @Autowired
    private AlertWarningInformationService alertWarningInformationService;
    @Autowired
    private RestUtil restUtil;
    @Value("${dl.dledu.back.host}")
    private String zhixinUrl;

    @Transactional (readOnly = true)
    public AlertReceiverVO get(String id){
        AlarmReceiver alarmReceiver = alarmReceiverManager.findById(id);
        return new AlertReceiverVO (alarmReceiver);
    }

    public AlarmReceiver save(Long orgId, AlertReceiverVO alertReceiverVO){
        if (null == alertReceiverVO) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "输入参数是必须的");
        }
        if (StringUtils.isEmpty(alertReceiverVO.getCollegeId())) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "学院ID是必须的");
        }
        if (StringUtils.isEmpty(alertReceiverVO.getCollegeName())) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "学院名称是必须的");
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
        long tc = alarmReceiverManager.countByTeacherIdAndCollege(alertReceiverVO.getTeacherId(), alertReceiverVO.getCollegeId());
        if (tc > 0) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "该老师已经存在");
        }
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setOrgId(orgId);
        alarmReceiver.setCollegeId(alertReceiverVO.getCollegeId());
        alarmReceiver.setCollegeName(alertReceiverVO.getCollegeName());
        alarmReceiver.setTeacherId(alertReceiverVO.getTeacherId());
        alarmReceiver.setTeacherJobNumber(alertReceiverVO.getTeacherJobNumber());
        alarmReceiver.setTeacherName(alertReceiverVO.getTeacherName());
        alarmReceiver.setTeacherPhone(alertReceiverVO.getTeacherPhone());
        //操作，最后修改人信息
        return alarmReceiverManager.save(alarmReceiver);
    }

    public AlarmReceiver update(Long orgId, String id, AlertReceiverVO alertReceiverVO){
        if (null == alertReceiverVO) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "输入参数是必须的");
        }
        if (StringUtils.isEmpty(id)) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "ID是必须的");
        }
        AlarmReceiver alarmReceiver = alarmReceiverManager.findById(id);
        if (null == alarmReceiver) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "根据ID没有查找到相应的接收人信息");
        }
        if (StringUtils.isEmpty(alertReceiverVO.getCollegeId())) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "学院ID是必须的");
        }
        if (StringUtils.isEmpty(alertReceiverVO.getCollegeName())) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "学院名称是必须的");
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

        long tc = alarmReceiverManager.countByTeacherIdAndCollegeAndIdNot(alertReceiverVO.getTeacherId(), alertReceiverVO.getCollegeId(), id);
        if (tc > 0) {
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "该老师已经存在");
        }
        alarmReceiver.setOrgId(orgId);
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
            throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "ID是必须的");
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

    public List<CollegeAlertReceiverVO> getCollegeAndReceiveCount(Long orgId) {
        List<CollegeAlertReceiverVO> rs = new ArrayList<>();
        PageData<CollegeVO>  page = orgManagerFeignService.queryCollege(orgId, null, 1, Integer.MAX_VALUE);
        if (null != page && null != page.getData()) {
            Map<Long, CollegeAlertReceiverVO> map = new HashMap<>();
            for (CollegeVO c : page.getData()) {
                CollegeAlertReceiverVO v = new CollegeAlertReceiverVO();
                v.setCollegeId(c.getId());
                v.setCollegeName(c.getName());
                v.setReceiverCount(0L);
                rs.add(v);
                map.put(v.getCollegeId(), v);
            }
            List<IdCountDTO> clist = alarmReceiverManager.countByOrgAndGroupByCollegeId(orgId);
            for (IdCountDTO c : clist) {
                CollegeAlertReceiverVO v = map.get(c.getId());
                if (null != v) {
                    v.setReceiverCount(c.getCount());
                }
            }
        }

        return rs;
    }

    public void sendMsg (Long orgId, Integer teacherYear, Integer semester, String type) {
        Map<String, Object> map = alertWarningInformationService.getStatisticsByCollege(PageUtil.createNoErrorPageRequest(1, Integer.MAX_VALUE), orgId, type, teacherYear, semester);
        List<AlarmReceiver> receiverList = alarmReceiverManager.findByOrgAll(orgId);
        if (null != map && (null != receiverList && receiverList.size() > 0)) {//接收人有数据，并且预警有数据

            Map<String, List<AlarmReceiver>> collegeReceiverMap = new HashMap<>();//按照学院组织接收人
            for (AlarmReceiver a : receiverList) {
                List<AlarmReceiver> collegeReceiverList = collegeReceiverMap.get(a.getCollegeName());
                if (null == collegeReceiverList) {
                    collegeReceiverList = new ArrayList<>();
                    collegeReceiverMap.put(a.getCollegeName(), collegeReceiverList);
                }
                collegeReceiverList.add(a);
            }

            PageData<CollegeWarningInfoDTO> p = (PageData<CollegeWarningInfoDTO>)map.get("pagData");
            if (null != p) {
                List<CollegeWarningInfoDTO> list = p.getData();
                if (null != list && list.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (CollegeWarningInfoDTO dto : list) {
                        sb.delete(0, sb.length());
                        List<AlarmReceiver> collegeReceiverList = collegeReceiverMap.get(dto.getCollegeName());
                        if (null == collegeReceiverList || collegeReceiverList.size() <= 0) {
                            LOG.info("College:({}) have not any receiver .", dto.getCollegeName());
                            continue;
                        }
                        sb.append(teacherYear).append("年");
                        if (null != semester) {
                            if (1 == semester.intValue()) {
                                sb.append("春季");
                            } else {
                                sb.append("秋季");
                            }
                            sb.append("学期");
                        }
                        sb.append(dto.getCollegeName());
                        sb.append("的学生共产生").append(dto.getTotal()).append("条总评成绩预警，其中红色预警");
                        sb.append(dto.getSum1()).append("条，橙色预警").append(dto.getSum2()).append("条，黄色预警").append(dto.getSum3()).append("6条。");
                        for (AlarmReceiver r : receiverList) {
                            try {
                                restUtil.post(zhixinUrl + "/api/web/v1/msg/send?phone=" + r.getTeacherPhone() + "&msg=" + sb.toString(), null);
                                LOG.warn("给[" + dto.getCollegeName() + "]电话号码[" + r.getTeacherPhone() + "]发送告警短信:[" + sb.toString() + "]成功");
                            } catch (Exception e) {
                                LOG.warn("给[" + dto.getCollegeName() + "]电话号码[" + r.getTeacherPhone() + "]发送告警短信:[" + sb.toString() + "]失败");
                            }
                        }
                    }
                }
            }
        } else {
            LOG.info("Not alert data or not any receiver .");
        }
    }
}
