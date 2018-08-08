package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import com.aizhixin.cloud.dataanalysis.alertinformation.JdbcTemplate.StudentJdbc;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.*;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.*;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AttachmentInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.OperationRecord;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.alertinformation.repository.AlertWarningInformationRepository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.constant.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningTypeConstant;
import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.common.domain.SortDTO;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import com.aizhixin.cloud.dataanalysis.common.util.ProportionUtil;
import com.aizhixin.cloud.dataanalysis.feign.OrgManagerFeignService;
import com.aizhixin.cloud.dataanalysis.notice.service.NotificationRecordService;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.entity.RuleParameter;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;
import com.aizhixin.cloud.dataanalysis.setup.service.RuleParameterService;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-15
 */
@Component
@Transactional
public class AlertWarningInformationService {
    @Autowired
    private EntityManager em;
    @Autowired
    private AlertWarningInformationRepository alertWarningInformationRepository;
    @Autowired
    private PageJdbcUtil pageJdbcUtil;
    @Autowired
    private OperaionRecordService operaionRecordService;
    @Autowired
    private AttachmentInfomationService attachmentInfomationService;
    @Autowired
    private WarningTypeService warningTypeService;
    @Autowired
    private OrgManagerFeignService orgManagerFeignService;
    @Autowired
    private StudentJdbc studentJdbc;
    @Autowired
    @Lazy
    private AlarmSettingsService alarmSettingsService;
    @Autowired
    private RuleParameterService ruleParameterService;
    @Autowired
    private NotificationRecordService notificationRecordService;

    /**
     * 修改预警状态按预警等级和机构id
     *
     * @param warningState
     * @param warningLevel
     * @param
     */
    public void updateWarningStateByWarningLevel(int warningState, int warningLevel, HashSet<Long> orgIds) {
        alertWarningInformationRepository.updateWarningStateByWarningLevel(warningState, warningLevel, orgIds);
    }

    public List<WarningInformation> findWarningInfoByWarningLevel(int warningState, int warningLevel, HashSet<Long> orgIds) {
        return alertWarningInformationRepository.findByWarningStateAndWarningLevelAndOrgIdIn(warningState, warningLevel, orgIds);
    }

    public Long countyWarningLevel(int warningState, int warningLevel) {
        return alertWarningInformationRepository.countByDeleteFlagAndWarningStateAndWarningLevel(DataValidity.VALID.getState(), warningState, warningLevel);
    }


    public void deleteWarningInformation(Long orgId, String warningType, String schoolYear, String semester) {
        alertWarningInformationRepository.deletePageDataByOrgIdAndTeacherYearAndSemester(orgId, warningType, schoolYear, semester);
    }

    /**
     * 按预警类型逻辑删除预警信息
     *
     * @param warningType
     * @param orgId
     */
    public void logicDeleteByOrgIdAndWarnType(String warningType, Long orgId, int schoolYear, int semester) {
        alertWarningInformationRepository.logicDeleteByOrgIdAndWarnType(warningType, orgId, schoolYear, semester);
    }

    RowMapper<RegisterAlertCountDomain> registerCountRm = new RowMapper<RegisterAlertCountDomain>() {

        @Override
        public RegisterAlertCountDomain mapRow(ResultSet rs, int rowNum)
                throws SQLException {
            // TODO Auto-generated method stub
            RegisterAlertCountDomain domain = new RegisterAlertCountDomain();
            domain.setCollogeCode(rs.getString("COLLOGE_CODE"));
            domain.setCollogeName(rs.getString("COLLOGE_NAME"));
            domain.setCountNum(rs.getLong("countNum"));
            domain.setWarningLevel(rs.getInt("WARNING_LEVEL"));
            return domain;
        }
    };


    public WarningInformation getOneById(String id) {
        return alertWarningInformationRepository.findOne(id);
    }

    public List<RegisterAlertCountDomain> findRegisterCountInfor(Long orgId) {

        String querySql = " SELECT COUNT(1) as countNum,COLLOGE_CODE,COLLOGE_NAME,WARNING_LEVEL FROM `t_warning_information` where DELETE_FLAG = " + DataValidity.VALID.getState() + " and ORG_ID =" + orgId + " and WARNING_TYPE ='Register' GROUP BY COLLOGE_CODE,WARNING_LEVEL ORDER BY COLLOGE_CODE,WARNING_LEVEL ;";

        return pageJdbcUtil.getInfo(querySql, registerCountRm);
    }

    public PageData<WarningDetailsDTO> findPageWarningInfor(Pageable pageable, Long orgId, String collegeCode, String type, String warningLevel, String workNo) {
        PageData<WarningDetailsDTO> p = new PageData<>();
        Map<String, Object> condition = new HashMap<>();
        StringBuilder cql = new StringBuilder("SELECT count(1) FROM t_warning_information aw WHERE 1 = 1");
        StringBuilder sql = new StringBuilder("SELECT aw.* FROM t_warning_information aw WHERE 1 = 1");
        if (null != orgId) {
            cql.append(" and aw.ORG_ID = :orgId");
            sql.append(" and aw.ORG_ID = :orgId");
            condition.put("orgId", orgId);
        }
        if (!StringUtils.isBlank(collegeCode)) {
            cql.append(" and aw.COLLOGE_CODE = :collegeId");
            sql.append(" and aw.COLLOGE_CODE = :collegeId");
            condition.put("COLLOGE_CODE", collegeCode);
        }
        if (!StringUtils.isBlank(type)) {
            cql.append(" and aw.WARNING_TYPE = :type");
            sql.append(" and aw.WARNING_TYPE = :type");
            condition.put("type", type);
        }
        if (null != warningLevel) {
            cql.append(" and aw.WARNING_LEVEL = :warningLevel");
            sql.append(" and aw.WARNING_LEVEL = :warningLevel");
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            boolean flag = pattern.matcher(warningLevel).matches();
            if (flag) {
                condition.put("warningLevel", Integer.valueOf(warningLevel));
            } else {
                //假设0级是不存在的
                condition.put("warningLevel", 0);
            }
        }
        cql.append(" and aw.DELETE_FLAG = 0");
        sql.append(" and aw.DELETE_FLAG = 0");
        Query cq = em.createNativeQuery(cql.toString());
        Query sq = em.createNativeQuery(sql.toString(), WarningInformation.class);
        for (Map.Entry<String, Object> e : condition.entrySet()) {
            cq.setParameter(e.getKey(), e.getValue());
            sq.setParameter(e.getKey(), e.getValue());
        }
        Long count = Long.valueOf(String.valueOf(cq.getSingleResult()));
        sq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        sq.setMaxResults(pageable.getPageSize());
        List<WarningInformation> data = sq.getResultList();
        List<WarningDetailsDTO> warningInformationDTOList = new ArrayList<>();
        for (WarningInformation alertWarningInformation : data) {
            WarningDetailsDTO warningDetailsDTO = new WarningDetailsDTO();
            warningDetailsDTO.setId(alertWarningInformation.getId());
            warningDetailsDTO.setName(alertWarningInformation.getName());
            warningDetailsDTO.setJobNumber(alertWarningInformation.getJobNumber());
            warningDetailsDTO.setCollogeName(alertWarningInformation.getCollogeName());
            warningDetailsDTO.setProfessionalName(alertWarningInformation.getProfessionalName());
            warningDetailsDTO.setClassName(alertWarningInformation.getClassName());
            warningDetailsDTO.setTeacherYear(alertWarningInformation.getTeacherYear());
            warningDetailsDTO.setPhone(alertWarningInformation.getPhone());
            warningDetailsDTO.setAddress(alertWarningInformation.getAddress());
            warningDetailsDTO.setParentsContact(alertWarningInformation.getParentsContact());
            warningDetailsDTO.setWarningTime(alertWarningInformation.getWarningTime());
            warningDetailsDTO.setWarningName(WarningTypeConstant.valueOf(alertWarningInformation.getWarningType()).getValue());
            warningDetailsDTO.setWarningLevel(alertWarningInformation.getWarningLevel());
            warningInformationDTOList.add(warningDetailsDTO);
        }
        p.setData(warningInformationDTOList);
        p.getPage().setTotalElements(count);
        p.getPage().setPageNumber(pageable.getPageNumber());
        p.getPage().setPageSize(pageable.getPageSize());
        p.getPage().setTotalPages(PageUtil.cacalatePagesize(count, p.getPage().getPageSize()));


        notificationRecordService.lastAccessTag(orgId, workNo);//短信通知最后访问标记
        return p;
    }


    RowMapper<AlertInforDomain> alertInforRm = new RowMapper<AlertInforDomain>() {

        @Override
        public AlertInforDomain mapRow(ResultSet rs, int rowNum)
                throws SQLException {
            // TODO Auto-generated method stub
            AlertInforDomain domain = new AlertInforDomain();
            domain.setId(rs.getString("ID"));
            domain.setName(rs.getString("NAME"));
            domain.setCollogeName(rs.getString("COLLOGE_NAME"));
            domain.setClassName(rs.getString("CLASS_NAME"));
            domain.setJobNumber(rs.getString("JOB_NUMBER"));
            domain.setWarningLevel(rs.getInt("WARNING_LEVEL"));
            domain.setWarningCondition(rs.getString("WARNING_CONDITION"));
            domain.setWarningType(WarningTypeConstant.valueOf(rs.getString("WARNING_TYPE")).getValue());
            domain.setWarningState(rs.getInt("WARNING_STATE"));
            domain.setWarningTime(rs.getTimestamp("WARNING_TIME"));
            domain.setWarningSource(rs.getString("WARNING_SOURCE"));
            return domain;
        }
    };

    public Map<String, Object> queryAlertInforPage(AlertInforQueryDomain domain) {

        String querySql = "SELECT ID,NAME,COLLOGE_NAME,CLASS_NAME,JOB_NUMBER,WARNING_LEVEL,WARNING_CONDITION,WARNING_TYPE,WARNING_TIME,WARNING_STATE,WARNING_SOURCE FROM `t_warning_information` where DELETE_FLAG =" + DataValidity.VALID.getState() + " ";
        String countSql = "SELECT count(1) FROM `t_warning_information` where DELETE_FLAG =" + DataValidity.VALID.getState() + " ";

        if (!StringUtils.isEmpty(domain.getKeywords())) {

            querySql += " and ( NAME like '%" + domain.getKeywords() + "%' or JOB_NUMBER like '%" + domain.getKeywords() + "%') ";
            countSql += " and ( NAME like '%" + domain.getKeywords() + "%' or JOB_NUMBER like '%" + domain.getKeywords() + "%') ";
        }


        if (!StringUtils.isEmpty(domain.getCollegeCodes())) {
            String[] collogeIdArr = domain.getCollegeCodes().split(",");
            String collogeCodes = "";
            for (String collogeCode : collogeIdArr) {
                if (!StringUtils.isEmpty(collogeCode)) {
                    if (StringUtils.isEmpty(collogeCodes)) {
                        collogeCodes = collogeCode;
                    } else {
                        collogeCodes += "," + collogeCode;
                    }
                }
            }

            querySql += " and COLLOGE_CODE in (" + collogeCodes
                    + ")";
            countSql += " and COLLOGE_CODE in (" + collogeCodes
                    + ")";
        }

        if (!StringUtils.isEmpty(domain.getWarningLevels())) {
            String[] warnLevelArr = domain.getWarningLevels().split(",");
            String warnLevels = "";
            for (String warnLevel : warnLevelArr) {
                if (!StringUtils.isEmpty(warnLevel)) {
                    if (StringUtils.isEmpty(warnLevels)) {
                        warnLevels = warnLevel;
                    } else {
                        warnLevels += "," + warnLevel;
                    }
                }
            }

            querySql += " and WARNING_LEVEL in (" + warnLevels
                    + ")";
            countSql += " and WARNING_LEVEL in (" + warnLevels
                    + ")";
        }

        if (!StringUtils.isEmpty(domain.getWarningTypes())) {
            String[] warnTypeArr = domain.getWarningTypes().split(",");
            String warnTypes = "";
            for (String warnType : warnTypeArr) {
                if (!StringUtils.isEmpty(warnType)) {
                    if (StringUtils.isEmpty(warnTypes)) {
                        warnTypes = "'" + warnType + "'";
                    } else {
                        warnTypes += "," + "'" + warnType + "'";
                    }
                }
            }

            querySql += " and WARNING_TYPE in (" + warnTypes + ")";
            countSql += " and WARNING_TYPE in (" + warnTypes + ")";
        }

        if (!StringUtils.isEmpty(domain.getWarningStates())) {
            String[] warnStateArr = domain.getWarningStates().split(",");
            String warnStates = "";
            for (String warnState : warnStateArr) {
                if (!StringUtils.isEmpty(warnState)) {
                    if (StringUtils.isEmpty(warnStates)) {
                        warnStates = warnState;
                    } else {
                        warnStates += "," + warnState;
                    }
                }
            }

            querySql += " and WARNING_STATE in (" + warnStates + ")";
            countSql += " and WARNING_STATE in (" + warnStates + ")";
        }
        if (null != domain.getTeacherYear()) {
            querySql += " and TEACHING_YEAR = " + domain.getTeacherYear();
            countSql += " and TEACHING_YEAR = " + domain.getTeacherYear();
        }
        if (null != domain.getSemester()) {
            if(domain.getSemester().equals("2")){
                domain.setSemester("秋");
            }
            if(domain.getSemester().equals("1")){
                domain.setSemester("春");
            }
            querySql += " and SEMESTER = '" + domain.getSemester()+"'";
            countSql += " and SEMESTER = '" + domain.getSemester()+"'";
        }

        querySql += " and ORG_ID =" + domain.getOrgId();
        querySql += "  ORDER BY CREATED_DATE";
        countSql += " and ORG_ID =" + domain.getOrgId();
        List<SortDTO> sort = new ArrayList<SortDTO>();
        SortDTO dto = new SortDTO();
        dto.setKey("WARNING_TIME");
        dto.setAsc(false);

        return pageJdbcUtil.getPageInfor(domain.getPageSize(), domain.getPageNumber(), alertInforRm, sort, querySql, countSql);
    }

    public Map<String, Object> queryTeacherAlertInforPage(AlertInforQueryTeacherDomain domain) {

        String querySql = "SELECT ID,NAME,COLLOGE_NAME,CLASS_NAME,JOB_NUMBER,WARNING_LEVEL,WARNING_CONDITION,WARNING_TYPE,WARNING_TIME,WARNING_STATE,WARNING_SOURCE FROM `t_warning_information` where DELETE_FLAG =" + DataValidity.VALID.getState() + " ";
        String countSql = "SELECT count(1) FROM `t_warning_information` where DELETE_FLAG =" + DataValidity.VALID.getState() + " ";

        if (!StringUtils.isEmpty(domain.getKeywords())) {
            querySql += " and ( NAME like '%" + domain.getKeywords() + "%' or JOB_NUMBER like '%" + domain.getKeywords() + "%') ";
            countSql += " and ( NAME like '%" + domain.getKeywords() + "%' or JOB_NUMBER like '%" + domain.getKeywords() + "%') ";
        }

        if (!StringUtils.isEmpty(domain.getWarningLevels())) {
            String[] warnLevelArr = domain.getWarningLevels().split(",");
            String warnLevels = "";
            for (String warnLevel : warnLevelArr) {
                if (!StringUtils.isEmpty(warnLevel)) {
                    if (StringUtils.isEmpty(warnLevels)) {
                        warnLevels = warnLevel;
                    } else {
                        warnLevels += "," + warnLevel;
                    }
                }
            }
            querySql += " and WARNING_LEVEL in (" + warnLevels + ")";
            countSql += " and WARNING_LEVEL in (" + warnLevels + ")";
        }

        if (!StringUtils.isEmpty(domain.getWarningTypes())) {
            String[] warnTypeArr = domain.getWarningTypes().split(",");
            String warnTypes = "";
            for (String warnType : warnTypeArr) {
                if (!StringUtils.isEmpty(warnType)) {
                    if (StringUtils.isEmpty(warnTypes)) {
                        warnTypes = "'" + warnType + "'";
                    } else {
                        warnTypes += "," + "'" + warnType + "'";
                    }
                }
            }
            querySql += " and WARNING_TYPE in (" + warnTypes + ")";
            countSql += " and WARNING_TYPE in (" + warnTypes + ")";
        }

        if (!StringUtils.isEmpty(domain.getWarningStates())) {
            String[] warnStateArr = domain.getWarningStates().split(",");
            String warnStates = "";
            for (String warnState : warnStateArr) {
                if (!StringUtils.isEmpty(warnState)) {
                    if (StringUtils.isEmpty(warnStates)) {
                        warnStates = warnState;
                    } else {
                        warnStates += "," + warnState;
                    }
                }
            }
            querySql += " and WARNING_STATE in (" + warnStates + ")";
            countSql += " and WARNING_STATE in (" + warnStates + ")";
        }
        if (null != domain.getTeacherYear()) {
            querySql += " and TEACHING_YEAR = " + domain.getTeacherYear();
            countSql += " and TEACHING_YEAR = " + domain.getTeacherYear();
        }
        if (null != domain.getSemester()) {
            if(domain.getSemester().equals("2")){
                domain.setSemester("秋");
            }
            if(domain.getSemester().equals("1")){
                domain.setSemester("春");
            }
            querySql += " and SEMESTER = '" + domain.getSemester()+"'";
            countSql += " and SEMESTER = '" + domain.getSemester()+"'";
        }

        querySql += " and ORG_ID =" + domain.getOrgId();
        querySql += " and class_name in (select tct.classes_name from t_class_teacher tct where tct.teacher_id='" + domain.getUserId() + "')";
        querySql += "  ORDER BY CREATED_DATE";
        countSql += " and ORG_ID =" + domain.getOrgId();
        countSql += " and class_name in (select tct.classes_name from t_class_teacher tct where tct.teacher_id='" + domain.getUserId() + "')";
        List<SortDTO> sort = new ArrayList<SortDTO>();
        SortDTO dto = new SortDTO();
        dto.setKey("WARNING_TIME");
        dto.setAsc(false);

        return pageJdbcUtil.getPageInfor(domain.getPageSize(), domain.getPageNumber(), alertInforRm, sort, querySql, countSql);
    }

    public Map<String, Object> queryStuAlertInforPage(Long orgId, String jobNum, Integer pageNumber, Integer pageSize) {
        String querySql = "SELECT ID,NAME,COLLOGE_NAME,CLASS_NAME,JOB_NUMBER,WARNING_LEVEL,WARNING_CONDITION,WARNING_TYPE,WARNING_TIME,WARNING_STATE,WARNING_SOURCE FROM `t_warning_information` where DELETE_FLAG =" + DataValidity.VALID.getState() + " ";
        querySql += " and ORG_ID =" + orgId;
        querySql += " and job_number='" + jobNum + "'";
        querySql += "  ORDER BY CREATED_DATE";

        String countSql = "SELECT count(1) FROM `t_warning_information` where DELETE_FLAG =" + DataValidity.VALID.getState() + " ";
        countSql += " and ORG_ID =" + orgId;
        countSql += " and job_number='" + jobNum + "'";

        List<SortDTO> sort = new ArrayList<SortDTO>();
        SortDTO dto = new SortDTO();
        dto.setKey("WARNING_TIME");
        dto.setAsc(false);
        return pageJdbcUtil.getPageInfor(pageSize, pageNumber, alertInforRm, sort, querySql, countSql);
    }


    RowMapper<RegisterAlertCountDomain> AlertInforCountRm = new RowMapper<RegisterAlertCountDomain>() {
        @Override
        public RegisterAlertCountDomain mapRow(ResultSet rs, int rowNum)
                throws SQLException {
            // TODO Auto-generated method stub
            RegisterAlertCountDomain domain = new RegisterAlertCountDomain();
            domain.setCountNum(rs.getLong("countNum"));
            domain.setWarningLevel(rs.getInt("WARNING_LEVEL"));
            return domain;
        }
    };

    public List<RegisterAlertCountDomain> alertCountInfor(AlertInforQueryDomain domain) {

        String querySql = " SELECT COUNT(1) as countNum, WARNING_LEVEL FROM `t_warning_information` where DELETE_FLAG = " + DataValidity.VALID.getState() + " ";

        if (!StringUtils.isEmpty(domain.getKeywords())) {
            querySql += " and ( NAME like '%" + domain.getKeywords() + "%' or JOB_NUMBER like '%" + domain.getKeywords() + "%') ";
        }

        if (!StringUtils.isEmpty(domain.getCollegeCodes())) {
            String[] collogeIdArr = domain.getCollegeCodes().split(",");
            String collogeCodes = "";
            for (String collogeCode : collogeIdArr) {
                if (!StringUtils.isEmpty(collogeCode)) {
                    if (StringUtils.isEmpty(collogeCodes)) {
                        collogeCodes = collogeCode;
                    } else {
                        collogeCodes += "," + collogeCode;
                    }
                }
            }

            querySql += " and COLLOGE_CODE in (" + collogeCodes
                    + ")";
        }

        if (!StringUtils.isEmpty(domain.getWarningLevels())) {
            String[] warnLevelArr = domain.getWarningLevels().split(",");
            String warnLevels = "";
            for (String warnLevel : warnLevelArr) {
                if (!StringUtils.isEmpty(warnLevel)) {
                    if (StringUtils.isEmpty(warnLevels)) {
                        warnLevels = warnLevel;
                    } else {
                        warnLevels += "," + warnLevel;
                    }
                }
            }

            querySql += " and WARNING_LEVEL in (" + warnLevels
                    + ")";
        }

        if (!StringUtils.isEmpty(domain.getWarningTypes())) {
            String[] warnTypeArr = domain.getWarningTypes().split(",");
            String warnTypes = "";
            for (String warnType : warnTypeArr) {
                if (!StringUtils.isEmpty(warnType)) {
                    if (StringUtils.isEmpty(warnTypes)) {
                        warnTypes = "'" + warnType + "'";
                    } else {
                        warnTypes += "," + "'" + warnType + "'";
                    }
                }
            }

            querySql += " and WARNING_TYPE in (" + warnTypes
                    + ")";
        }

        if (!StringUtils.isEmpty(domain.getWarningStates())) {
            String[] warnStateArr = domain.getWarningStates().split(",");
            String warnStates = "";
            for (String warnState : warnStateArr) {
                if (!StringUtils.isEmpty(warnState)) {
                    if (StringUtils.isEmpty(warnStates)) {
                        warnStates = warnState;
                    } else {
                        warnStates += "," + warnState;
                    }
                }
            }

            querySql += " and WARNING_STATE in (" + warnStates
                    + ")";
        }

        if (null != domain.getTeacherYear()) {
            querySql += " and TEACHING_YEAR = " + domain.getTeacherYear();
        }
        if (null != domain.getSemester()) {
            if(domain.getSemester().equals("2")){
                domain.setSemester("秋");
            }
            if(domain.getSemester().equals("1")){
                domain.setSemester("春");
            }
            querySql += " and SEMESTER = '" + domain.getSemester()+"'";
        }

        querySql += " and ORG_ID =" + domain.getOrgId() + " GROUP BY COLLOGE_CODE,WARNING_LEVEL ORDER BY COLLOGE_CODE,WARNING_LEVEL ;";

        return pageJdbcUtil.getInfo(querySql, AlertInforCountRm);
    }

    public List<RegisterAlertCountDomain> alertTeacherCountInfor(AlertInforQueryTeacherDomain domain) {

        String querySql = " SELECT COUNT(1) as countNum, WARNING_LEVEL FROM `t_warning_information` where DELETE_FLAG = " + DataValidity.VALID.getState() + " ";

        if (!StringUtils.isEmpty(domain.getKeywords())) {
            querySql += " and ( NAME like '%" + domain.getKeywords() + "%' or JOB_NUMBER like '%" + domain.getKeywords() + "%') ";
        }

        if (!StringUtils.isEmpty(domain.getWarningLevels())) {
            String[] warnLevelArr = domain.getWarningLevels().split(",");
            String warnLevels = "";
            for (String warnLevel : warnLevelArr) {
                if (!StringUtils.isEmpty(warnLevel)) {
                    if (StringUtils.isEmpty(warnLevels)) {
                        warnLevels = warnLevel;
                    } else {
                        warnLevels += "," + warnLevel;
                    }
                }
            }

            querySql += " and WARNING_LEVEL in (" + warnLevels
                    + ")";
        }

        if (!StringUtils.isEmpty(domain.getWarningTypes())) {
            String[] warnTypeArr = domain.getWarningTypes().split(",");
            String warnTypes = "";
            for (String warnType : warnTypeArr) {
                if (!StringUtils.isEmpty(warnType)) {
                    if (StringUtils.isEmpty(warnTypes)) {
                        warnTypes = "'" + warnType + "'";
                    } else {
                        warnTypes += "," + "'" + warnType + "'";
                    }
                }
            }

            querySql += " and WARNING_TYPE in (" + warnTypes
                    + ")";
        }

        if (!StringUtils.isEmpty(domain.getWarningStates())) {
            String[] warnStateArr = domain.getWarningStates().split(",");
            String warnStates = "";
            for (String warnState : warnStateArr) {
                if (!StringUtils.isEmpty(warnState)) {
                    if (StringUtils.isEmpty(warnStates)) {
                        warnStates = warnState;
                    } else {
                        warnStates += "," + warnState;
                    }
                }
            }
            querySql += " and WARNING_STATE in (" + warnStates + ")";
        }

        if (null != domain.getTeacherYear()) {
            querySql += " and TEACHING_YEAR = " + domain.getTeacherYear();
        }
        if (null != domain.getSemester()) {
            if(domain.getSemester().equals("2")){
                domain.setSemester("秋");
            }
            if(domain.getSemester().equals("1")){
                domain.setSemester("春");
            }
            querySql += " and SEMESTER = '" + domain.getSemester()+"'";
        }

        querySql += " and ORG_ID =" + domain.getOrgId() + " and class_name in (select tct.classes_name from t_class_teacher tct where tct.teacher_id='" + domain.getUserId() + "') GROUP BY COLLOGE_CODE,WARNING_LEVEL ORDER BY COLLOGE_CODE,WARNING_LEVEL ;";

        return pageJdbcUtil.getInfo(querySql, AlertInforCountRm);
    }

    public List<RegisterAlertCountDomain> alertStuCountInfor(Long orgId, String jobNum) {
        String querySql = " SELECT COUNT(1) as countNum, WARNING_LEVEL FROM `t_warning_information` where DELETE_FLAG = " + DataValidity.VALID.getState() + " ";
        querySql += " and ORG_ID =" + orgId + " and job_number='" + jobNum + "' GROUP BY COLLOGE_CODE,WARNING_LEVEL ORDER BY COLLOGE_CODE,WARNING_LEVEL ;";
        return pageJdbcUtil.getInfo(querySql, AlertInforCountRm);
    }

    /**
     * 组装按条件查询的预警信息和按预警等级统计的数量
     *
     * @param domain
     * @return
     */
    public Map<String, Object> getAlertInforPage(AlertInforQueryDomain domain) {
        Map<String, Object> pageInfor = this.queryAlertInforPage(domain);
        List<RegisterAlertCountDomain> countList = this.alertCountInfor(domain);
        LevelAlertCountDomain countDomain = new LevelAlertCountDomain();
        if (null != countList && countList.size() > 0) {
            Long sum1 = 0L;
            Long sum2 = 0L;
            Long sum3 = 0L;
            for (RegisterAlertCountDomain countDTO : countList) {
                if (countDTO.getWarningLevel() == 1) {
                    sum1 = sum1 + countDTO.getCountNum();
                }
                if (countDTO.getWarningLevel() == 2) {
                    sum2 = sum2 + countDTO.getCountNum();
                }
                if (countDTO.getWarningLevel() == 3) {
                    sum3 = sum3 + countDTO.getCountNum();
                }
            }
            countDomain.setLevel1CountNum(sum1);
            countDomain.setLevel2CountNum(sum2);
            countDomain.setLevel3CountNum(sum3);
        }
        pageInfor.put(ApiReturnConstants.COUNT, countDomain);
        return pageInfor;
    }

    /**
     * 辅导员预警 组装按条件查询的预警信息和按预警等级统计的数量
     *
     * @param domain
     * @return
     */
    public Map<String, Object> getTeacherAlertInforPage(AlertInforQueryTeacherDomain domain) {
        Map<String, Object> pageInfor = this.queryTeacherAlertInforPage(domain);
        List<RegisterAlertCountDomain> countList = this.alertTeacherCountInfor(domain);
        LevelAlertCountDomain countDomain = new LevelAlertCountDomain();
        if (null != countList && countList.size() > 0) {
            Long sum1 = 0L;
            Long sum2 = 0L;
            Long sum3 = 0L;
            for (RegisterAlertCountDomain countDTO : countList) {
                if (countDTO.getWarningLevel() == 1) {
                    sum1 = sum1 + countDTO.getCountNum();
                }
                if (countDTO.getWarningLevel() == 2) {
                    sum2 = sum2 + countDTO.getCountNum();
                }
                if (countDTO.getWarningLevel() == 3) {
                    sum3 = sum3 + countDTO.getCountNum();
                }
            }
            countDomain.setLevel1CountNum(sum1);
            countDomain.setLevel2CountNum(sum2);
            countDomain.setLevel3CountNum(sum3);
        }
        pageInfor.put(ApiReturnConstants.COUNT, countDomain);
        return pageInfor;
    }

    /**
     * 学生预警 组装按条件查询的预警信息和按预警等级统计的数量
     *
     * @param orgId
     * @param jobNum
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public Map<String, Object> getStuAlertInforPage(Long orgId, String jobNum, Integer pageNumber, Integer pageSize) {
        Map<String, Object> pageInfor = this.queryStuAlertInforPage(orgId, jobNum, pageNumber, pageSize);
        List<RegisterAlertCountDomain> countList = this.alertStuCountInfor(orgId, jobNum);
        LevelAlertCountDomain countDomain = new LevelAlertCountDomain();
        if (null != countList && countList.size() > 0) {
            Long sum1 = 0L;
            Long sum2 = 0L;
            Long sum3 = 0L;
            for (RegisterAlertCountDomain countDTO : countList) {
                if (countDTO.getWarningLevel() == 1) {
                    sum1 = sum1 + countDTO.getCountNum();
                }
                if (countDTO.getWarningLevel() == 2) {
                    sum2 = sum2 + countDTO.getCountNum();
                }
                if (countDTO.getWarningLevel() == 3) {
                    sum3 = sum3 + countDTO.getCountNum();
                }
            }
            countDomain.setLevel1CountNum(sum1);
            countDomain.setLevel2CountNum(sum2);
            countDomain.setLevel3CountNum(sum3);
        }
        pageInfor.put(ApiReturnConstants.COUNT, countDomain);
        return pageInfor;
    }

    public Map<String, Object> getStatisticalGrade(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        List<CollegeStatisticProportionDTO> cspDTOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        int sum = 0;
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int alreadyProcessed = 0;
        StringBuilder cql = new StringBuilder("SELECT COUNT(1) as count, SUM(IF(WARNING_LEVEL = 1, 1, 0)) as sum1, SUM(IF(WARNING_LEVEL = 2, 1, 0)) as sum2, SUM(IF(WARNING_LEVEL = 3, 1, 0)) as sum3, SUM(IF(WARNING_STATE = 20 OR WARNING_STATE = 40, 1, 0)) as sum4 FROM t_warning_information WHERE 1 = 1");
        StringBuilder sql = new StringBuilder("SELECT COLLOGE_NAME, SUM(IF(WARNING_LEVEL = 1, 1, 0)) as sum1, SUM(IF(WARNING_LEVEL = 2, 1, 0)) as sum2, SUM(IF(WARNING_LEVEL = 3, 1, 0)) as sum3 FROM t_warning_information WHERE 1 = 1");
        if (null != orgId) {
            cql.append(" and ORG_ID = :orgId");
            sql.append(" and ORG_ID = :orgId");
            condition.put("orgId", orgId);
        }

        cql.append(" and DELETE_FLAG = 0");
        sql.append(" and DELETE_FLAG = 0");
        sql.append(" GROUP BY COLLOGE_CODE");

        try {
            Query cq = em.createNativeQuery(cql.toString());
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                cq.setParameter(e.getKey(), e.getValue());
                sq.setParameter(e.getKey(), e.getValue());
            }
            Object[] cd = (Object[]) cq.getSingleResult();
            if (null != cd && cd.length == 5) {
                if (null != cd[0]) {
                    sum = Integer.valueOf(String.valueOf(cd[0]));
                }
                if (null != cd[1]) {
                    sum1 = Integer.valueOf(String.valueOf(cd[1]));
                }
                if (null != cd[2]) {
                    sum2 = Integer.valueOf(String.valueOf(cd[2]));
                }
                if (null != cd[3]) {
                    sum3 = Integer.valueOf(String.valueOf(cd[3]));
                }
                if (null != cd[4]) {
                    alreadyProcessed = Integer.valueOf(String.valueOf(cd[4]));
                }
            }
            data.put("total", sum);
            data.put("alreadyProcessed", alreadyProcessed);
            data.put("proportion", ProportionUtil.accuracy(alreadyProcessed * 1.0, sum * 1.0, 2));
            data.put("proportion1", ProportionUtil.accuracy(sum1 * 1.0, sum * 1.0, 2));
            data.put("proportion2", ProportionUtil.accuracy(sum2 * 1.0, sum * 1.0, 2));
            data.put("proportion3", ProportionUtil.accuracy(sum3 * 1.0, sum * 1.0, 2));
            List<Object> res = sq.getResultList();
            if (null != res && res.size() > 0) {
                for (Object obj : res) {
                    Object[] d = (Object[]) obj;
                    CollegeStatisticProportionDTO cspDTO = new CollegeStatisticProportionDTO();
                    if (null != d[0]) {
                        cspDTO.setCollegeName(String.valueOf(d[0]));
                    }
                    if (null != d[1]) {
                        cspDTO.setSum1(Integer.valueOf(String.valueOf(d[1])));
                        cspDTO.setProportion1(ProportionUtil.accuracy(Double.valueOf(String.valueOf(d[1])), sum * 1.0, 2));
                    }
                    if (null != d[2]) {
                        cspDTO.setSum2(Integer.valueOf(String.valueOf(d[2])));
                        cspDTO.setProportion2(ProportionUtil.accuracy(Double.valueOf(String.valueOf(d[2])), sum * 1.0, 2));
                    }
                    if (null != d[3]) {
                        cspDTO.setSum3(Integer.valueOf(String.valueOf(d[3])));
                        cspDTO.setProportion3(ProportionUtil.accuracy(Double.valueOf(String.valueOf(d[3])), sum * 1.0, 2));
                    }
                    cspDTOList.add(cspDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "预警级别汇总统计异常！");
            return result;
        }
        result.put("success", true);
        result.put("cspDTOList", cspDTOList);
        result.put("data", data);
        return result;
    }

    public Map<String, Object> getStatistical(Long orgId, String teacherYear, String semester) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        int sum = 0;
        int alreadyProcessed = 0;
        int untreated = 0;
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(1), SUM(IF(WARNING_STATE = 20 OR WARNING_STATE = 40, 1, 0)) FROM t_warning_information WHERE 1 = 1");
            if (null != orgId) {
                sql.append(" and ORG_ID = :orgId");
                condition.put("orgId", orgId);
            }
            if (null != teacherYear) {
                sql.append(" and TEACHING_YEAR = :teacherYear");
                condition.put("teacherYear", teacherYear);
            }
            if (null != semester) {
                if(semester.equals("2")){
                    semester="秋";
                }
                if(semester.equals("1")){
                    semester="春";
                }
                sql.append(" and SEMESTER = :semester");
                condition.put("semester", semester);
            }
            sql.append(" and DELETE_FLAG = 0");

            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            Object[] cd = (Object[]) sq.getSingleResult();
            if (null != cd && cd.length == 2) {
                if (null != cd[0]) {
                    sum = Integer.valueOf(String.valueOf(cd[0]));
                }
                if (null != cd[1]) {
                    alreadyProcessed = Integer.valueOf(String.valueOf(cd[1]));
                }
            }
            untreated = sum - alreadyProcessed;
            data.put("total", sum);
            data.put("alreadyProcessed", alreadyProcessed);
            data.put("untreated", untreated);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "预警汇总统计异常！");
            return result;
        }
        result.put("success", true);
        result.put("data", data);
        return result;
    }


    public Map<String, Object> getLatestinformation(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> condition = new HashMap<>();
        List<WarningDetailsDTO> data = new ArrayList<>();
        //统计下的列表最近想的前20条
        StringBuilder lql = new StringBuilder("SELECT * FROM t_warning_information WHERE 1 = 1");
        if (null != orgId) {
            lql.append(" and ORG_ID = :orgId");
            condition.put("orgId", orgId);
        }
        lql.append(" and DELETE_FLAG = 0");
        lql.append(" order by WARNING_TIME desc limit 20");
        try {
            Query lq = em.createNativeQuery(lql.toString(), WarningInformation.class);
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                lq.setParameter(e.getKey(), e.getValue());
            }
            List<WarningInformation> alertWarningInformationList = lq.getResultList();
            for (WarningInformation alertWarningInformation : alertWarningInformationList) {
                WarningDetailsDTO warningDetailsDTO = new WarningDetailsDTO();
                warningDetailsDTO.setId(alertWarningInformation.getId());
                warningDetailsDTO.setName(alertWarningInformation.getName());
                warningDetailsDTO.setJobNumber(alertWarningInformation.getJobNumber());
                warningDetailsDTO.setCollogeName(alertWarningInformation.getCollogeName());
                warningDetailsDTO.setProfessionalName(alertWarningInformation.getProfessionalName());
                warningDetailsDTO.setClassName(alertWarningInformation.getClassName());
                warningDetailsDTO.setTeacherYear(alertWarningInformation.getTeacherYear());
                warningDetailsDTO.setPhone(alertWarningInformation.getPhone());
                warningDetailsDTO.setAddress(alertWarningInformation.getAddress());
                warningDetailsDTO.setWarningState(alertWarningInformation.getWarningState());
                warningDetailsDTO.setParentsContact(alertWarningInformation.getParentsContact());
                warningDetailsDTO.setWarningTime(alertWarningInformation.getWarningTime());
                warningDetailsDTO.setWarningName(WarningTypeConstant.valueOf(alertWarningInformation.getWarningType()).getValue());
                warningDetailsDTO.setWarningLevel(alertWarningInformation.getWarningLevel());
                data.add(warningDetailsDTO);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取最新预警学生异常！");
        }
        result.put("success", true);
        result.put("data", data);
        return result;
    }

    public Map<String, Object> getStatisticalCollege(Long orgId, String teacherYear, String semester) {
        Map<String, Object> result = new HashMap<>();
        List<CollegeStatisticsDTO> collegeStatisticsDTOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT COLLOGE_NAME, COUNT(1), SUM(IF(WARNING_STATE = 20 OR WARNING_STATE = 40, 1, 0)) FROM t_warning_information  WHERE 1 = 1");
        if (null != orgId) {
            sql.append(" and ORG_ID = :orgId");
            condition.put("orgId", orgId);
        }
        if (null != teacherYear) {
            sql.append(" and TEACHING_YEAR = :teacherYear");
            condition.put("teacherYear", teacherYear);
        }
        if (null != semester) {
            if(semester.equals("2")){
                semester="秋";
            }
            if(semester.equals("1")){
                semester="春";
            }
            sql.append(" and SEMESTER = :semester");
            condition.put("semester", semester);
        }
        sql.append(" and DELETE_FLAG = 0 GROUP BY COLLOGE_CODE");
        try {
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object obj : res) {
                    Object[] d = (Object[]) obj;
                    CollegeStatisticsDTO collegeStatisticsDTO = new CollegeStatisticsDTO();
                    if (null != d[0] && null != d[1] && null != d[2]) {
                        collegeStatisticsDTO.setCollegeName(String.valueOf(d[0]));
                        collegeStatisticsDTO.setTotal(Integer.valueOf(String.valueOf(d[1])));
                        collegeStatisticsDTO.setAlreadyProcessed(Integer.valueOf(String.valueOf(d[2])));
                        collegeStatisticsDTOList.add(collegeStatisticsDTO);
                    }
                }
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "按照学院统计数量异常！");
            return result;
        }
        result.put("success", true);
        result.put("data", collegeStatisticsDTOList);
        return result;
    }


    public Map<String, Object> getCollegeProcessedRatio(Long orgId, String teacherYear, String semester) {
        Map<String, Object> result = new HashMap<>();
        List<CollegeAlreadyProcessedRatioDTO> collegeAlreadyProcessedRatioDTOArrayList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT COLLOGE_NAME, COUNT(1), SUM(IF(WARNING_STATE = 20 OR WARNING_STATE = 40, 1, 0)) FROM t_warning_information  WHERE 1 = 1");
        if (null != orgId) {
            sql.append(" and ORG_ID = :orgId");
            condition.put("orgId", orgId);
        }
        if (null != teacherYear) {
            sql.append(" and TEACHING_YEAR = :teacherYear");
            condition.put("teacherYear", teacherYear);
        }
        if (null != semester) {
            if(semester.equals("2")){
                semester="秋";
            }
            if(semester.equals("1")){
                semester="春";
            }
            sql.append(" and SEMESTER = :semester");
            condition.put("semester", semester);
        }
        sql.append(" and DELETE_FLAG = 0 GROUP BY COLLOGE_CODE");
        try {
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object obj : res) {
                    Object[] d = (Object[]) obj;
                    int total = 0;
                    int alreadyProcessed = 0;
                    CollegeAlreadyProcessedRatioDTO collegeAlreadyProcessedRatioDTO = new CollegeAlreadyProcessedRatioDTO();
                    if (null != d[0]) {
                        collegeAlreadyProcessedRatioDTO.setCollegeName(String.valueOf(d[0]));
                    }
                    if (null != d[1]) {
                        total = Integer.valueOf(String.valueOf(d[1]));
                    }
                    if (null != d[2]) {
                        alreadyProcessed = Integer.valueOf(String.valueOf(d[2]));
                    }
                    collegeAlreadyProcessedRatioDTO.setRatio(Double.valueOf(ProportionUtil.accuracy(alreadyProcessed * 1.0, total * 1.0, 2)));
                    collegeAlreadyProcessedRatioDTOArrayList.add(collegeAlreadyProcessedRatioDTO);
                }
                Collections.sort(collegeAlreadyProcessedRatioDTOArrayList);
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取学院处理率top--10异常！");
            return result;
        }
        result.put("success", true);
        if (collegeAlreadyProcessedRatioDTOArrayList.size() > 10) {
            result.put("data", collegeAlreadyProcessedRatioDTOArrayList.subList(0, 10));
        } else {
            result.put("data", collegeAlreadyProcessedRatioDTOArrayList);
        }
        return result;
    }


    public Map<String, Object> getStatisticalType(Long orgId, String teacherYear, String semester) {
        Map<String, Object> result = new HashMap<>();
        List<TypeStatisticsDTO> typeStatisticsDTOList = new ArrayList<>();
        List<TypeStatisticsDTO> typeList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        int sum = 0;
        int total = 0;
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        StringBuilder sql = new StringBuilder("SELECT WARNING_TYPE, count(1), SUM(IF(WARNING_LEVEL = 1 and (WARNING_STATE = 20 OR WARNING_STATE = 40), 1, 0)) as sum1, SUM(IF(WARNING_LEVEL = 2 and (WARNING_STATE = 20 OR WARNING_STATE = 40), 1, 0)) as sum2, SUM(IF(WARNING_LEVEL = 3 and (WARNING_STATE = 20 OR WARNING_STATE = 40), 1, 0)) as sum3 FROM t_warning_information  WHERE 1 = 1");
        if (null != orgId) {
            sql.append(" and ORG_ID = :orgId");
            condition.put("orgId", orgId);
        }
        if (null != teacherYear) {
            sql.append(" and TEACHING_YEAR = :teacherYear");
            condition.put("teacherYear", teacherYear);
        }
        if (null != semester) {
            if(semester.equals("2")){
                semester="秋";
            }
            if(semester.equals("1")){
                semester="春";
            }
            sql.append(" and SEMESTER = :semester");
            condition.put("semester", semester);
        }

        sql.append(" and DELETE_FLAG = 0");
        sql.append(" GROUP BY WARNING_TYPE");
        try {
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object obj : res) {
                    Object[] d = (Object[]) obj;
                    TypeStatisticsDTO typeStatisticsDTO = new TypeStatisticsDTO();
                    if (null != d[0]) {
                        typeStatisticsDTO.setWarningType(WarningTypeConstant.valueOf(String.valueOf(d[0])).getValue());
                    }
                    if (null != d[1]) {
                        total = Integer.valueOf(String.valueOf(d[1]));
                    }
                    if (null != d[2]) {
                        sum1 = Integer.valueOf(String.valueOf(d[2]));
                    }
                    if (null != d[3]) {
                        sum2 = Integer.valueOf(String.valueOf(d[3]));
                    }
                    if (null != d[4]) {
                        sum3 = Integer.valueOf(String.valueOf(d[4]));
                    }
                    sum = sum1 + sum2 + sum3;
                    typeStatisticsDTO.setProportion(ProportionUtil.accuracy(sum * 1.0, total * 1.0, 2));
                    typeStatisticsDTO.setSum(sum);
                    typeStatisticsDTO.setSum1(sum1);
                    typeStatisticsDTO.setSum2(sum2);
                    typeStatisticsDTO.setSum3(sum3);
                    typeStatisticsDTOList.add(typeStatisticsDTO);
                }
            }
            if (null != orgId) {
                List<com.aizhixin.cloud.dataanalysis.setup.entity.WarningType> warningTypeList = warningTypeService.getWarningTypeList(orgId);
                for (com.aizhixin.cloud.dataanalysis.setup.entity.WarningType type : warningTypeList) {
                    TypeStatisticsDTO tSDTO = new TypeStatisticsDTO();
                    tSDTO.setWarningType(type.getWarningName());
                    tSDTO.setProportion("0");
                    tSDTO.setSum(0);
                    tSDTO.setSum1(0);
                    tSDTO.setSum2(0);
                    tSDTO.setSum3(0);
                    for (TypeStatisticsDTO ts : typeStatisticsDTOList) {
                        if (type.getWarningName().equals(ts.getWarningType())) {
                            tSDTO.setProportion(ts.getProportion());
                            tSDTO.setWarningType(ts.getWarningType());
                            tSDTO.setSum(ts.getSum());
                            tSDTO.setSum1(ts.getSum1());
                            tSDTO.setSum2(ts.getSum2());
                            tSDTO.setSum3(ts.getSum3());
                            break;
                        }
                    }
                    typeList.add(tSDTO);
                }
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "按类型统计异常！");
            return result;
        }
        result.put("success", true);
        result.put("data", typeList);
        return result;
    }

    public Map<String, Object> getStatisticalCollegeType(Long orgId, String type) {
        Map<String, Object> result = new HashMap<>();
        List<CollegeStatisticsDTO> collegeStatisticsDTOList = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT COLLOGE_NAME, SUM(IF(WARNING_LEVEL = 1, 1, 0)) as sum1, SUM(IF(WARNING_LEVEL = 2, 1, 0)) as sum2, SUM(IF(WARNING_LEVEL = 3, 1, 0)) as sum3 FROM t_warning_information  WHERE 1 = 1");
        if (null != orgId) {
            sql.append(" and ORG_ID = :orgId");
            condition.put("orgId", orgId);
        }
        if (null != type) {
            sql.append(" and WARNING_TYPE = :type");
            condition.put("type", type);
        }
        sql.append(" and DELETE_FLAG = 0");
        sql.append(" GROUP BY COLLOGE_CODE");
        try {
            Query sq = em.createNativeQuery(sql.toString());
            for (Map.Entry<String, Object> e : condition.entrySet()) {
                sq.setParameter(e.getKey(), e.getValue());
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object obj : res) {
                    Object[] d = (Object[]) obj;
                    CollegeStatisticsDTO collegeStatisticsDTO = new CollegeStatisticsDTO();
                    if (null != d[0]) {
                        collegeStatisticsDTO.setCollegeName(String.valueOf(d[0]));
                    }
                    if (null != d[1]) {
                        collegeStatisticsDTO.setSum1(Integer.valueOf(String.valueOf(d[1])));
                    }
                    if (null != d[2]) {
                        collegeStatisticsDTO.setSum2(Integer.valueOf(String.valueOf(d[2])));
                    }
                    if (null != d[3]) {
                        collegeStatisticsDTO.setSum3(Integer.valueOf(String.valueOf(d[3])));
                    }
                    collegeStatisticsDTOList.add(collegeStatisticsDTO);
                }
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "按照学院统计每个告警等级的数量异常！");
            return result;
        }
        result.put("success", true);
        result.put("data", collegeStatisticsDTOList);
        return result;
    }

    public Map<String, Object> getWarningDetails(String id) {
        Map<String, Object> result = new HashMap<>();
        WarningDetailsDTO warningDetailsDTO = new WarningDetailsDTO();
        try {
            WarningInformation alertWarningInformation = alertWarningInformationRepository.findOne(id);
            if (null != alertWarningInformation) {
                warningDetailsDTO.setId(alertWarningInformation.getId());
                warningDetailsDTO.setName(alertWarningInformation.getName());
                warningDetailsDTO.setJobNumber(alertWarningInformation.getJobNumber());
                warningDetailsDTO.setCollogeName(alertWarningInformation.getCollogeName());
                warningDetailsDTO.setProfessionalName(alertWarningInformation.getProfessionalName());
                warningDetailsDTO.setClassName(alertWarningInformation.getClassName());
                warningDetailsDTO.setTeacherYear(alertWarningInformation.getTeacherYear());
                warningDetailsDTO.setPhone(alertWarningInformation.getPhone());
                warningDetailsDTO.setAddress(alertWarningInformation.getAddress());
                warningDetailsDTO.setParentsContact(alertWarningInformation.getParentsContact());
                warningDetailsDTO.setWarningTime(alertWarningInformation.getWarningTime());
                warningDetailsDTO.setWarningName(WarningTypeConstant.valueOf(alertWarningInformation.getWarningType()).getValue());
                warningDetailsDTO.setWarningLevel(alertWarningInformation.getWarningLevel());
                warningDetailsDTO.setWarningState(alertWarningInformation.getWarningState());
                warningDetailsDTO.setDealTime(alertWarningInformation.getLastModifiedDate());
                warningDetailsDTO.setWarningCondition(alertWarningInformation.getWarningCondition());
                warningDetailsDTO.setWarningSource(alertWarningInformation.getWarningSource());

                Map stuMap = studentJdbc.findOne(warningDetailsDTO.getJobNumber(), alertWarningInformation.getOrgId());
                if (stuMap != null && stuMap.get("nj") != null) {
                    warningDetailsDTO.setNj(stuMap.get("nj").toString());
                }

                String standard = "";
                if (null != alertWarningInformation.getAlarmSettingsId()) {
                    AlarmSettings as = alarmSettingsService.getAlarmSettingsById(alertWarningInformation.getAlarmSettingsId());
                    String[] rpIds = as.getRuleSet().split(",");
                    if(rpIds.length>0){
                        for(String rpId :rpIds){
                            RuleParameter rp = ruleParameterService.findById(rpId);
                            standard = standard + rp.getRuledescribe() + rp.getRightParameter() + as.getRelationship();

                        }
                    }
                }
                if (!StringUtils.isBlank(standard)&&standard.length()>1) {
                    warningDetailsDTO.setWarningStandard(standard.substring(0, standard.length() - 1));
                }
                List<DealDomain> dealDomainList = new ArrayList<>();
                List<OperationRecord> operationRecordList = operaionRecordService.getOperationRecordByWInfoId(alertWarningInformation.getId());
                if (null != operationRecordList && operationRecordList.size() > 0) {
                    for (OperationRecord or : operationRecordList) {
                        DealDomain dealDomain = new DealDomain();
                        dealDomain.setDealId(or.getId());
                        dealDomain.setDealType(or.getDealType());
                        dealDomain.setDealId(or.getId());
                        dealDomain.setDealInfo(or.getProposal());
                        dealDomain.setWarningInformationId(or.getWarningInformationId());
                        List<AttachmentDomain> attachmentDomainList = new ArrayList<>();
                        List<AttachmentInformation> attachmentInformationList = attachmentInfomationService.getAttachmentInformationByOprId(or.getId());
                        if (null != attachmentInformationList && attachmentInformationList.size() > 0) {
                            for (AttachmentInformation aif : attachmentInformationList) {
                                AttachmentDomain attachmentDomain = new AttachmentDomain();
                                attachmentDomain.setId(aif.getId());
                                attachmentDomain.setFileUrl(aif.getAttachmentPath());
                                attachmentDomain.setFileName(aif.getAttachmentName());
                                attachmentDomainList.add(attachmentDomain);
                            }
                        }
                        dealDomain.setAttachmentDomain(attachmentDomainList);
                        dealDomainList.add(dealDomain);
                    }
                }
                warningDetailsDTO.setDealDomainList(dealDomainList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "获取预警详情异常！");
            return result;
        }
        result.put("success", true);
        result.put("data", warningDetailsDTO);
        return result;
    }

    public List<WarningInformation> getawinfoByDefendantId(Long orgId, String warningType, String jobNumber) {
        return alertWarningInformationRepository.getawinfoByJobNumber(orgId, warningType, jobNumber, DataValidity.VALID.getState());
    }


    public List<WarningInformation> getawinfoByOrgIdAndWarningType(Long orgId, String warningType) {
        return alertWarningInformationRepository.getawinfoByOrgIdAndWarningType(orgId, warningType, DataValidity.VALID.getState());
    }


    public List<WarningInformation> getawinfoByOrgIdAndJobNumber(Long orgId, String jobNumber) {
        return alertWarningInformationRepository.getawinfoByOrgIdAndJobNumber(orgId, jobNumber, DataValidity.VALID.getState());
    }


    /**
     * ************************************************************
     */
    public Map<String, Object> getStatisticsByCollege(Pageable pageable, Long orgId, String type, String teacherYear, String semester) {
        Map<String, Object> result = new HashMap<>();
        //学院预警统计数量
        List<CollegeStatisticsDTO> collegeStatisticsDTOList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT COLLOGE_NAME, SUM(IF(WARNING_LEVEL = 1, 1, 0)) as sum1, SUM(IF(WARNING_LEVEL = 2, 1, 0)) as sum2, SUM(IF(WARNING_LEVEL = 3, 1, 0)) as sum3, count(1) as count FROM t_warning_information  WHERE 1 = 1");
        //学院预警信息
        PageData<CollegeWarningInfoDTO> p = new PageData<>();
        StringBuilder cql = new StringBuilder("SELECT count(sub.COLLOGE_CODE) FROM (SELECT COLLOGE_CODE FROM t_warning_information WHERE 1 = 1 ");
        StringBuilder iql = new StringBuilder("SELECT COLLOGE_NAME, count(1) as count, SUM(IF(WARNING_STATE = 20 OR WARNING_STATE = 40, 1, 0)) as sum, SUM(IF(WARNING_LEVEL = 1, 1, 0)) as sum1, SUM(IF(WARNING_LEVEL = 2, 1, 0)) as sum2, SUM(IF(WARNING_LEVEL = 3, 1, 0)) as sum3, SUM(IF(WARNING_LEVEL = 1 and (WARNING_STATE = 20 OR WARNING_STATE = 40), 1, 0)) as asum1, SUM(IF(WARNING_LEVEL = 2 and (WARNING_STATE = 20 OR WARNING_STATE = 40), 1, 0)) as asum2, SUM(IF(WARNING_LEVEL = 3 and (WARNING_STATE = 20 OR WARNING_STATE = 40), 1, 0)) as asum3 FROM t_warning_information  WHERE 1 = 1");
        if (null != orgId) {
            sql.append(" and ORG_ID = :orgId");
            cql.append(" and ORG_ID = :orgId");
            iql.append(" and ORG_ID = :orgId");
        }
        if (null != teacherYear) {
            sql.append(" and TEACHING_YEAR = :teacherYear");
            cql.append(" and TEACHING_YEAR = :teacherYear");
            iql.append(" and TEACHING_YEAR = :teacherYear");
        }
        if (null != semester) {
            if(semester.equals("2")){
                semester="秋";
            }
            if(semester.equals("1")){
                semester="春";
            }
            sql.append(" and SEMESTER = :semester");
            cql.append(" and SEMESTER = :semester");
            iql.append(" and SEMESTER = :semester");
        }
        if (!StringUtils.isBlank(type)) {
            sql.append(" and WARNING_TYPE = :type");
            cql.append(" and WARNING_TYPE = :type");
            iql.append(" and WARNING_TYPE = :type");
        }
        sql.append(" and DELETE_FLAG = 0");
        cql.append(" and DELETE_FLAG = 0");
        iql.append(" and DELETE_FLAG = 0");

        sql.append(" GROUP BY COLLOGE_CODE");
        cql.append(" GROUP BY COLLOGE_CODE) sub");
        iql.append(" GROUP BY COLLOGE_CODE");

        try {
            Query sq = em.createNativeQuery(sql.toString());
            Query cq = em.createNativeQuery(cql.toString());
            Query iq = em.createNativeQuery(iql.toString());
            if (null != orgId) {
                cq.setParameter("orgId", orgId);
                sq.setParameter("orgId", orgId);
                iq.setParameter("orgId", orgId);
            }
            if (null != teacherYear) {
                cq.setParameter("teacherYear", teacherYear);
                sq.setParameter("teacherYear", teacherYear);
                iq.setParameter("teacherYear", teacherYear);
            }
            if (null != semester) {
                if(semester.equals("2")){
                    semester="秋";
                }
                if(semester.equals("1")){
                    semester="春";
                }
                cq.setParameter("semester", semester);
                sq.setParameter("semester", semester);
                iq.setParameter("semester", semester);
            }
            if (!StringUtils.isBlank(type)) {
                sq.setParameter("type", type);
                cq.setParameter("type", type);
                iq.setParameter("type", type);
            }
            List<Object> res = sq.getResultList();
            if (null != res) {
                for (Object obj : res) {
                    Object[] d = (Object[]) obj;
                    CollegeStatisticsDTO collegeStatisticsDTO = new CollegeStatisticsDTO();
                    if (null != d[0]) {
                        collegeStatisticsDTO.setCollegeName(String.valueOf(d[0]));
                    }
                    if (null != d[1]) {
                        collegeStatisticsDTO.setSum1(Integer.valueOf(String.valueOf(d[1])));
                    }
                    if (null != d[2]) {
                        collegeStatisticsDTO.setSum2(Integer.valueOf(String.valueOf(d[2])));
                    }
                    if (null != d[3]) {
                        collegeStatisticsDTO.setSum3(Integer.valueOf(String.valueOf(d[3])));
                    }
                    if (null != d[4]) {
                        collegeStatisticsDTO.setTotal(Integer.valueOf(String.valueOf(d[4])));
                    }
                    collegeStatisticsDTOList.add(collegeStatisticsDTO);
                }
            }
            Long count = Long.valueOf(String.valueOf(cq.getSingleResult()));
            List<CollegeWarningInfoDTO> collegeWarningInfoDTOList = new ArrayList<>();
            if (count.intValue() > 0) {
                iq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                iq.setMaxResults(pageable.getPageSize());
                List<Object> rd = iq.getResultList();
                if (null != rd && rd.size() > 0) {
                    for (Object obj : rd) {
                        Object[] d = (Object[]) obj;
                        CollegeWarningInfoDTO collegeWarningInfoDTO = new CollegeWarningInfoDTO();
                        if (null != d[0]) {
                            collegeWarningInfoDTO.setCollegeName(String.valueOf(d[0]));
                        }
                        if (null != d[1]) {
                            collegeWarningInfoDTO.setTotal(Integer.valueOf(String.valueOf(d[1])));
                        }
                        if (null != d[2]) {
                            collegeWarningInfoDTO.setProcessedNumber(Integer.valueOf(String.valueOf(d[2])));
                        }
                        if (null != d[3]) {
                            collegeWarningInfoDTO.setSum1(Integer.valueOf(String.valueOf(d[3])));
                        }
                        if (null != d[4]) {
                            collegeWarningInfoDTO.setSum2(Integer.valueOf(String.valueOf(d[4])));
                        }
                        if (null != d[5]) {
                            collegeWarningInfoDTO.setSum3(Integer.valueOf(String.valueOf(d[5])));
                        }
                        if (null != d[6]) {
                            collegeWarningInfoDTO.setProcessedSum1(Integer.valueOf(String.valueOf(d[6])));
                        }
                        if (null != d[7]) {
                            collegeWarningInfoDTO.setProcessedSum2(Integer.valueOf(String.valueOf(d[7])));
                        }
                        if (null != d[8]) {
                            collegeWarningInfoDTO.setProcessedSum3(Integer.valueOf(String.valueOf(d[8])));
                        }
                        collegeWarningInfoDTO.setProcessedProportion(ProportionUtil.accuracy(collegeWarningInfoDTO.getProcessedNumber() * 1.0, collegeWarningInfoDTO.getTotal() * 1.0, 2));
                        collegeWarningInfoDTOList.add(collegeWarningInfoDTO);
                    }
                }
            }
            p.setData(collegeWarningInfoDTOList);
            p.getPage().setTotalElements(count);
            p.getPage().setPageNumber(pageable.getPageNumber());
            p.getPage().setPageSize(pageable.getPageSize());
            p.getPage().setTotalPages(PageUtil.cacalatePagesize(count, p.getPage().getPageSize()));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "按类型统计异常！");
            return result;
        }
        result.put("success", true);
        result.put("collegeStatisticsDTOList", collegeStatisticsDTOList);
        result.put("pagData", p);
        return result;
    }


    public void save(WarningInformation warningInformation) {
        alertWarningInformationRepository.save(warningInformation);
    }

    public void save(List<WarningInformation> warningInformations) {
        alertWarningInformationRepository.save(warningInformations);
    }


    public List<WarningInformation> getWarnInforByState(Long orgId, String warningType, int deleteFlag, int warningState) {

        return alertWarningInformationRepository.getawinfoByOrgIdAndWarningTypeAndState(orgId, warningType, deleteFlag, warningState);
    }


    public Map<String, Integer> getschoolYearAndSemester() {
        Map<String, Integer> result = new HashMap<>();
        Calendar c = Calendar.getInstance();
        // 当前年份
        int schoolYear = c.get(Calendar.YEAR);
        // 当前月份
        int month = c.get(Calendar.MONTH) + 1;
        // 当前学期编号
        int semester = 2;
        if (month > 1 && month < 9) {
            semester = 1;
        }
        if (month == 1) {
            schoolYear = schoolYear - 1;
        }
        result.put("schoolYear", schoolYear);
        result.put("semester", semester);
        return result;
    }
}
