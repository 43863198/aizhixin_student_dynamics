package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AlertWarningInformation;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;

import org.springframework.data.domain.Pageable;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AlertInforDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.AlertInforQueryDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.LevelAlertCountDomain;
import com.aizhixin.cloud.dataanalysis.alertinformation.domain.RegisterAlertCountDomain;
import com.aizhixin.cloud.dataanalysis.common.core.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.domain.SortDTO;
import com.aizhixin.cloud.dataanalysis.common.service.AuthUtilService;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
	private PageJdbcUtil pageJdbcUtil;
	@Autowired
	private AuthUtilService authUtilService;
	
	RowMapper<RegisterAlertCountDomain> registerCountRm = new RowMapper<RegisterAlertCountDomain>() {

		@Override
		public RegisterAlertCountDomain mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			// TODO Auto-generated method stub
			RegisterAlertCountDomain domain = new RegisterAlertCountDomain();
			domain.setCollogeId(rs.getLong("COLLOGE_ID"));
			domain.setCollogeName(rs.getString("COLLOGE_NAME"));
			domain.setCountNum(rs.getLong("countNum"));
			domain.setWarningLevel(rs.getInt("WARNING_LEVEL"));
			return domain;
		}
	};
	
	public List<RegisterAlertCountDomain> findRegisterCountInfor(Long orgId) {

		String querySql = " SELECT COUNT(1) as countNum,COLLOGE_ID,COLLOGE_NAME,WARNING_LEVEL FROM `t_alert_warning_information` where DELETE_FLAG = "+DataValidity.VALID.getState()+" and ORG_ID ="+orgId+" and WARNING_TYPE ='Register' GROUP BY COLLOGE_ID,WARNING_LEVEL ORDER BY COLLOGE_ID,WARNING_LEVEL ;";

		return pageJdbcUtil.getInfo(querySql, registerCountRm);
	}

	public PageData<AlertWarningInformation> findRegisterCountInfor(Pageable pageable,Long orgId, Long collegeId, String  type, int warningLevel) {
		PageData<AlertWarningInformation> p = new PageData<>();
		Map<String, Object> condition = new HashMap<>();
		StringBuilder cql = new StringBuilder("SELECT count(1) FROM t_alert_warning_information aw WHERE 1 = 1");
		StringBuilder sql = new StringBuilder("SELECT aw.* FROM t_alert_warning_information aw WHERE 1 = 1");
		if(null!=orgId){
			cql.append(" and aw.ORG_ID = :orgId");
			sql.append(" and aw.ORG_ID = :orgId");
			condition.put("orgId",orgId);
		}
		if(null!=collegeId){
			cql.append(" and aw.ORG_ID = :collegeId");
			sql.append(" and aw.ORG_ID = :collegeId");
			condition.put("COLLOGE_ID",collegeId);
	}
		if(!StringUtils.isBlank(type)){
			cql.append(" and aw.WARNING_TYPE = :type");
			sql.append(" and aw.WARNING_TYPE = :type");
			condition.put("type", type);
		}
		if(warningLevel>0){
			cql.append(" and aw.WARNING_LEVEL = :warningLevel");
			sql.append(" and aw.WARNING_LEVEL = :warningLevel");
			condition.put("warningLevel", warningLevel);
		}
		Query cq = em.createNativeQuery(sql.toString());
		Query sq = em.createNativeQuery(sql.toString(), AlertWarningInformation.class);
		for (Map.Entry<String, Object> e : condition.entrySet()) {
			cq.setParameter(e.getKey(), e.getValue());
			sq.setParameter(e.getKey(), e.getValue());
		}
		Long count = Long.valueOf(String.valueOf(cq.getSingleResult()));
		sq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		sq.setMaxResults(pageable.getPageSize());
		p.setData(sq.getResultList());
		p.getPage().setTotalElements(count);
		p.getPage().setPageNumber(pageable.getPageNumber());
		p.getPage().setPageSize(pageable.getPageSize());
		p.getPage().setTotalPages(PageUtil.cacalatePagesize(count, p.getPage().getPageSize()));
		return p;
	}

	
	RowMapper<AlertInforDomain> alertInforRm = new RowMapper<AlertInforDomain>() {

		@Override
		public AlertInforDomain mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			// TODO Auto-generated method stub
			AlertInforDomain domain = new AlertInforDomain();
			domain.setId(rs.getLong("ID"));
			domain.setCollogeName(rs.getString("COLLOGE_NAME"));
			domain.setClassName(rs.getString("CLASS_NAME"));
			domain.setJobNumber(rs.getString("JOB_NUMBER"));
			domain.setWarningLevel(rs.getInt("WARNING_LEVEL"));
			domain.setWarningType(rs.getString("WARNING_TYPE"));
			domain.setWarningTime(rs.getTimestamp("WARNING_TIME"));
			return domain;
		}
	};
	
	public Map<String, Object> queryAlertInforPage(AlertInforQueryDomain domain) {

		String querySql = "SELECT ID,COLLOGE_NAME,CLASS_NAME,JOB_NUMBER,WARNING_LEVEL,WARNING_TYPE,WARNING_TIME FROM `t_alert_warning_information` where DELETE_FLAG ="+DataValidity.VALID.getState()+" ";
		String countSql = "SELECT count(1) FROM `t_alert_warning_information` where DELETE_FLAG ="+DataValidity.VALID.getState()+" ";
		
		if(!StringUtils.isEmpty(domain.getKeywords())){
			
			querySql += " and ( NAME like '%" + domain.getKeywords()+ "%' or JOB_NUMBER like '%" + domain.getKeywords()+ "%') ";
			countSql += " and ( NAME like '%" + domain.getKeywords()+ "%' or JOB_NUMBER like '%" + domain.getKeywords()+ "%') ";
		}
		
		
		if (!StringUtils.isEmpty(domain.getCollogeIds())) {
			String[] collogeIdArr = domain.getCollogeIds().split(",");
			String collogeIds = "";
			for(String collogeId : collogeIdArr){
				if(!StringUtils.isEmpty(collogeId)){
					if(StringUtils.isEmpty(collogeIds)){
						collogeIds = collogeId;
					}else{
						collogeIds += ","+collogeId;
					}
				}
			}
			
			querySql += " and COLLOGE_ID in (" + collogeIds
					+ ")";
			countSql += " and COLLOGE_ID in (" + collogeIds
					+ ")";
		}
		
		if (!StringUtils.isEmpty(domain.getWarningLevels())) {
			String[] warnLevelArr = domain.getWarningLevels().split(",");
			String warnLevels = "";
			for(String warnLevel : warnLevelArr){
				if(!StringUtils.isEmpty(warnLevel)){
					if(StringUtils.isEmpty(warnLevels)){
						warnLevels = warnLevel;
					}else{
						warnLevels += ","+warnLevel;
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
			for(String warnType : warnTypeArr){
				if(!StringUtils.isEmpty(warnType)){
					if(StringUtils.isEmpty(warnTypes)){
						warnTypes = "'"+warnType+"'";
					}else{
						warnTypes += ","+"'"+warnType+"'";
					}
				}
			}
			
			querySql += " and WARNING_TYPE in (" + warnTypes
					+ ")";
			countSql += " and WARNING_TYPE in (" + warnTypes
					+ ")";
		}
	
		querySql += " and ORG_ID =" + domain.getOrgId();
		countSql += " and ORG_ID =" + domain.getOrgId();
		List<SortDTO> sort = new ArrayList<SortDTO>();
		SortDTO dto = new SortDTO();
		dto.setKey("WARNING_TIME");
		dto.setAsc(false);

		return pageJdbcUtil
				.getPageInfor(domain.getPageSize(), domain.getPageNumber(),
						alertInforRm, sort, querySql, countSql);
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

		String querySql = " SELECT COUNT(1) as countNum,WARNING_LEVEL FROM `t_alert_warning_information` where DELETE_FLAG = "+DataValidity.VALID.getState()+" ";

		if(!StringUtils.isEmpty(domain.getKeywords())){
			querySql += " and ( NAME like '%" + domain.getKeywords()+ "%' or JOB_NUMBER like '%" + domain.getKeywords()+ "%') ";
		}
		
		if (!StringUtils.isEmpty(domain.getCollogeIds())) {
			String[] collogeIdArr = domain.getCollogeIds().split(",");
			String collogeIds = "";
			for(String collogeId : collogeIdArr){
				if(!StringUtils.isEmpty(collogeId)){
					if(StringUtils.isEmpty(collogeIds)){
						collogeIds = collogeId;
					}else{
						collogeIds += ","+collogeId;
					}
				}
			}
			
			querySql += " and COLLOGE_ID in (" + collogeIds
					+ ")";
		}
		
		if (!StringUtils.isEmpty(domain.getWarningLevels())) {
			String[] warnLevelArr = domain.getWarningLevels().split(",");
			String warnLevels = "";
			for(String warnLevel : warnLevelArr){
				if(!StringUtils.isEmpty(warnLevel)){
					if(StringUtils.isEmpty(warnLevels)){
						warnLevels = warnLevel;
					}else{
						warnLevels += ","+warnLevel;
					}
				}
			}
			
			querySql += " and WARNING_LEVEL in (" + warnLevels
					+ ")";
		}
		
		if (!StringUtils.isEmpty(domain.getWarningTypes())) {
			String[] warnTypeArr = domain.getWarningTypes().split(",");
			String warnTypes = "";
			for(String warnType : warnTypeArr){
				if(!StringUtils.isEmpty(warnType)){
					if(StringUtils.isEmpty(warnTypes)){
						warnTypes = "'"+warnType+"'";
					}else{
						warnTypes += ","+"'"+warnType+"'";
					}
				}
			}
			
			querySql += " and WARNING_TYPE in (" + warnTypes
					+ ")";
		}
		
		querySql += " and ORG_ID ="+domain.getOrgId()+" GROUP BY COLLOGE_ID,WARNING_LEVEL ORDER BY COLLOGE_ID,WARNING_LEVEL ;";
		
		return pageJdbcUtil.getInfo(querySql, AlertInforCountRm);
	}
	
	/**
	 * 组装按条件查询的预警信息和按预警等级统计的数量
	 * @param domain
	 * @return
	 */
	public Map<String, Object> getAlertInforPage(AlertInforQueryDomain domain){
		Map<String, Object> pageInfor = this.queryAlertInforPage(domain);
		List<RegisterAlertCountDomain> countList = this.alertCountInfor(domain);
		LevelAlertCountDomain countDomain = new LevelAlertCountDomain();
		if(null != countList && countList.size() > 0){
			for(RegisterAlertCountDomain countDTO : countList){
				if(countDTO.getWarningLevel() == 1){
					countDomain.setLevel1CountNum(countDTO.getCountNum());
				}
				if(countDTO.getWarningLevel() == 2){
					countDomain.setLevel2CountNum(countDTO.getCountNum());
				}
				if(countDTO.getWarningLevel() == 3){
					countDomain.setLevel3CountNum(countDTO.getCountNum());
				}
			}
		}
		pageInfor.put(ApiReturnConstants.COUNT, countDomain);
		return pageInfor;
	}
}
