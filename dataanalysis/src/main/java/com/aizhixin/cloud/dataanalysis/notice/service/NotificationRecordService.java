package com.aizhixin.cloud.dataanalysis.notice.service;


import com.aizhixin.cloud.dataanalysis.alertinformation.dto.CollegeWarningInfoDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.service.AlertWarningInformationService;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.common.domain.MessageVO;
import com.aizhixin.cloud.dataanalysis.common.exception.ErrorCode;
import com.aizhixin.cloud.dataanalysis.common.util.RestUtil;
import com.aizhixin.cloud.dataanalysis.notice.entity.NotificationRecord;
import com.aizhixin.cloud.dataanalysis.notice.manager.NotificationRecordManager;
import com.aizhixin.cloud.dataanalysis.notice.vo.AlertContentVO;
import com.aizhixin.cloud.dataanalysis.notice.vo.AlertTeacherVO;
import com.aizhixin.cloud.dataanalysis.notice.vo.NotificationRecordVO;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmReceiver;
import com.aizhixin.cloud.dataanalysis.setup.manager.AlarmReceiverManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
@Slf4j
public class NotificationRecordService {
    @Autowired
    private NotificationRecordManager notificationRecordManager;
    @Autowired
    private AlarmReceiverManager alarmReceiverManager;
    @Autowired
    private AlertWarningInformationService alertWarningInformationService;
    @Autowired
    private RestUtil restUtil;
    @Value("${dl.dledu.back.host}")
    private String zhixinUrl;

    private String getZeroAlerContent (String collegeName, String teacherYear, String semester, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append(teacherYear).append("年");
        if (null != semester) {
            sb.append(semester);
            sb.append("学期");
        }
        sb.append(collegeName);
        sb.append("的学生共产生").append(0).append("条");
        if (!StringUtils.isEmpty(type)) {
            if ("LeaveSchool".equalsIgnoreCase(type)) {
                sb.append("退学预警");
            } else if ("Absenteeism".equalsIgnoreCase(type) || "Register".equalsIgnoreCase(type) ) {
                sb.append("旷课预警");
            } else {
                sb.append("不识别预警");
            }
        } else {
            sb.append("未知类型预警");
        }
        sb.append("，其中红色预警");
        sb.append(0).append("条，橙色预警").append(0).append("条，黄色预警").append(0).append("条");
        return sb.toString();
    }

    private Map<String, String> getAlertContent(Long orgId, String teacherYear, String semester, String type) {
        Map<String, String> alertContentMap = new HashMap<>();
        Map<String, Object> map = alertWarningInformationService.getStatisticsByCollege(PageUtil.createNoErrorPageRequest(1, Integer.MAX_VALUE), orgId, type, teacherYear, semester);
        if (null != map) {//接收人有数据，并且预警有数据
            PageData<CollegeWarningInfoDTO> p = (PageData<CollegeWarningInfoDTO>)map.get("pagData");
            if (null != p) {
                List<CollegeWarningInfoDTO> list = p.getData();
                if (null != list && list.size() > 0) {
                    for (CollegeWarningInfoDTO dto : list) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(teacherYear).append("年");
                        if (null != semester) {
                            sb.append(semester);
                            sb.append("学期");
                        }
                        sb.append(dto.getCollegeName());
                        sb.append("的学生共产生").append(dto.getTotal()).append("条");
                        if (!StringUtils.isEmpty(type)) {
                            if ("LeaveSchool".equalsIgnoreCase(type)) {
                                sb.append("退学预警");
                            } else if ("Absenteeism".equalsIgnoreCase(type)) {
                                sb.append("旷课预警");
                            } else {
                                sb.append("不识别预警");
                            }
                        } else {
                            sb.append("未知类型预警");
                        }
                        sb.append("，其中红色预警");
                        sb.append(dto.getSum1()).append("条，橙色预警").append(dto.getSum2()).append("条，黄色预警").append(dto.getSum3()).append("条");
                        String msg = sb.toString();
                        alertContentMap.put(dto.getCollegeName(), msg);
                    }
                }
            }
        }
        return alertContentMap;
    }

    @Transactional(readOnly = true)
    public List<AlertContentVO> getAlertMsg (Long orgId, String teacherYear, String semester, String type) {
        Map<String, String> alertContentMap = getAlertContent(orgId, teacherYear, semester, type);
        List<AlarmReceiver> receiverList = alarmReceiverManager.findByOrgAll(orgId);
        List<AlertContentVO> rsList = new ArrayList<>();
        Map<String, List<AlertTeacherVO>> collegeReceiverMap = new HashMap<>();//按照学院组织接收人
        Set<String> collegeSet = new HashSet<>();
        if (null != receiverList) {
            for (AlarmReceiver a : receiverList) {
                if (!alertContentMap.keySet().contains(a.getCollegeName())) {
                    continue;
                }
                List<AlertTeacherVO> collegeReceiverList = collegeReceiverMap.get(a.getCollegeName());
                if (null == collegeReceiverList) {
                    collegeReceiverList = new ArrayList<>();
                    collegeReceiverMap.put(a.getCollegeName(), collegeReceiverList);
                }
                if (!collegeSet.contains(a.getCollegeName())) {
                    collegeSet.add(a.getCollegeName());

                    AlertContentVO cvo = new AlertContentVO();
                    cvo.setAlertType(type);
                    cvo.setCollegeName(a.getCollegeName());
                    cvo.setReceiver(collegeReceiverList);
                    cvo.setAlertContent(alertContentMap.get(cvo.getCollegeName()));
                    rsList.add(cvo);
                }
                AlertTeacherVO tvo = new AlertTeacherVO();
                tvo.setCode(a.getTeacherJobNumber());
                tvo.setName(a.getTeacherName());
                tvo.setPhone(a.getTeacherPhone());
                collegeReceiverList.add(tvo);
            }
        }
        return rsList;
    }

    public MessageVO send(Long orgId, List<AlertContentVO> alertContentVOList) {
        MessageVO vo = new MessageVO();
        if (null == orgId || orgId <= 0) {
            vo.setCode(ErrorCode.PARAMS_CONFLICT);
            vo.setMessage("机构ID是必须的");
            return vo;
        }
        if (null == alertContentVOList || alertContentVOList.isEmpty()) {
            vo.setCode(ErrorCode.PARAMS_CONFLICT);
            vo.setMessage("基本信息是必须的");
            return vo;
        }
        for (AlertContentVO v : alertContentVOList) {
            if (null == v.getAlertContent() || v.getAlertContent().isEmpty()) {
                vo.setCode(ErrorCode.PARAMS_CONFLICT);
                vo.setMessage("告警内容是必须的");
                return vo;
            }
            for (AlertTeacherVO t : v.getReceiver()) {
                if (null == t.getPhone() || t.getPhone().isEmpty()) {
                    vo.setCode(ErrorCode.PARAMS_CONFLICT);
                    vo.setMessage("电话号码是必须的");
                    return vo;
                }
            }
        }
        List<NotificationRecord> list = new ArrayList<>();
        Date current = new Date ();
        for (AlertContentVO v : alertContentVOList) {
            sendAction(list, orgId, v, current);
        }
        if (!list.isEmpty()) {
            notificationRecordManager.save(list);
        }
        return vo;
    }

    @Transactional(readOnly = true)
    public AlertContentVO getCollegeAlertMsg (Long orgId, String teacherYear, String semester, String type, String collegeName) {
        AlertContentVO rs = new AlertContentVO();
        List<AlertTeacherVO> collegeReceiverList = new ArrayList<>();
        rs.setReceiver(collegeReceiverList);
        rs.setAlertType(type);
        rs.setCollegeName(collegeName);
        Map<String, String> alertContentMap = getAlertContent(orgId, teacherYear, semester, type);
        if (!alertContentMap.keySet().contains(collegeName)) {
            rs.setAlertContent(getZeroAlerContent(collegeName, teacherYear, semester, type));
            return rs;
        }
        rs.setAlertContent(alertContentMap.get(rs.getCollegeName()));
        return rs;
    }

    private void sendAction(List<NotificationRecord> list, Long orgId, AlertContentVO alertContentVO, Date current) {
        for (AlertTeacherVO t : alertContentVO.getReceiver()) {
            NotificationRecord record = new NotificationRecord();
            try {
                list.add(record);
                record.setAlertType(alertContentVO.getAlertType());
                record.setCollegeName(alertContentVO.getCollegeName());
                record.setContent(alertContentVO.getAlertContent());
                record.setOrgId(orgId);
                record.setReceiverCode(t.getCode());
                record.setReceiverName(t.getName());
                record.setReceiverPhone(t.getPhone());
                record.setSendTime(current);
                restUtil.post(zhixinUrl + "/api/web/v1/msg/send?phone=" + t.getPhone() + "&msg=" + alertContentVO.getAlertContent(), null);
                log.info("给[" + alertContentVO.getCollegeName() + "]电话号码[" + t.getPhone() + "]发送告警短信:[" + alertContentVO.getAlertContent() + "]成功");
                record.setRs(10);
            } catch (Exception e) {
                record.setRs(20);
                record.setFailMsg("发送短信失败");
                log.warn("给[" + alertContentVO.getCollegeName() + "]电话号码[" + t.getPhone() + "]发送告警短信:[" + alertContentVO.getAlertContent() + "]失败");
            }
        }
    }

    public MessageVO send(Long orgId, AlertContentVO alertContentVO) {
        MessageVO vo = new MessageVO();
        if (null == orgId || orgId <= 0) {
            vo.setCode(ErrorCode.PARAMS_CONFLICT);
            vo.setMessage("机构ID是必须的");
            return vo;
        }
        if (null == alertContentVO || null == alertContentVO.getReceiver() || alertContentVO.getReceiver().isEmpty()) {
            vo.setCode(ErrorCode.PARAMS_CONFLICT);
            vo.setMessage("接收人是必须的");
            return vo;
        }
        if (null == alertContentVO.getAlertContent() || alertContentVO.getAlertContent().isEmpty()) {
            vo.setCode(ErrorCode.PARAMS_CONFLICT);
            vo.setMessage("告警内容是必须的");
            return vo;
        }
        for (AlertTeacherVO t : alertContentVO.getReceiver()) {
            if (null == t.getPhone() || t.getPhone().isEmpty()) {
                vo.setCode(ErrorCode.PARAMS_CONFLICT);
                vo.setMessage("电话号码是必须的");
                return vo;
            }
        }
        List<NotificationRecord> list = new ArrayList<>();
        Date current = new Date ();
        sendAction(list, orgId, alertContentVO, current);
        if (!list.isEmpty()) {
            notificationRecordManager.save(list);
        }
        return vo;
    }

    @Transactional(readOnly = true)
    public PageData<NotificationRecordVO> list (Long orgId, String collegeName, Integer rs, String receiver, Date startDate, Date endDate, Integer pageNumber, Integer pageSize) {
        if (null == orgId || orgId <= 0) {
            return new PageData<>();
        }
        return notificationRecordManager.list(PageUtil.createNoErrorPageRequest(pageNumber, pageSize), orgId, collegeName, rs, receiver, startDate, endDate);
    }

    public void lastAccessTag(Long orgId, String workNo) {
        if (null == orgId || orgId <= 0 || null == workNo || workNo.isEmpty()) {
            return;
        }
        Date current = new Date();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String ip = getIPAddress(request);
        System.out.println("ip------>" + request.getRemoteAddr());
        System.out.println("ip------>" + ip);
        List<NotificationRecord> list = notificationRecordManager.findSendSuccesByReceiverCode(orgId, workNo);//该工号发送完通知以后没有标记最后访问时间
        if (null != list && !list.isEmpty()) {
            for (NotificationRecord record : list) {
                record.setLastAccessTime(current);
            }
            notificationRecordManager.save(list);
        }
    }

    public  String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
