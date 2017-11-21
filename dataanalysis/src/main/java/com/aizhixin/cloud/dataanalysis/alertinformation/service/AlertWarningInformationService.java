package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.aizhixin.cloud.dataanalysis.alertinformation.dto.AlarmStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.CollegeStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.dto.TypeStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.alertinformation.entity.AlertWarningInformation;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;
import com.aizhixin.cloud.dataanalysis.common.constant.WarningType;
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

	public PageData<AlertWarningInformation> findPageWarningInfor(Pageable pageable,Long orgId, Long collegeId, String  type, String warningLevel) {
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
		if(null!=warningLevel){
			cql.append(" and aw.WARNING_LEVEL = :warningLevel");
			sql.append(" and aw.WARNING_LEVEL = :warningLevel");
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
			boolean flag = pattern.matcher(warningLevel).matches();
			if(flag) {
				condition.put("warningLevel", Integer.valueOf(warningLevel));
			}else {
				//假设0级是不存在的
				condition.put("warningLevel",0);
			}
		}
		Query cq = em.createNativeQuery(cql.toString());
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
			domain.setId(rs.getString("ID"));
			domain.setName(rs.getString("NAME"));
			domain.setCollogeName(rs.getString("COLLOGE_NAME"));
			domain.setClassName(rs.getString("CLASS_NAME"));
			domain.setJobNumber(rs.getString("JOB_NUMBER"));
			domain.setWarningLevel(rs.getInt("WARNING_LEVEL"));
			domain.setWarningType(rs.getString("WARNING_TYPE"));
			domain.setWarningState(rs.getInt("WARNING_STATE"));
			domain.setWarningTime(rs.getTimestamp("WARNING_TIME"));
			return domain;
		}
	};
	
	public Map<String, Object> queryAlertInforPage(AlertInforQueryDomain domain) {

		String querySql = "SELECT ID,NAME,COLLOGE_NAME,CLASS_NAME,JOB_NUMBER,WARNING_LEVEL,WARNING_TYPE,WARNING_TIME,WARNING_STATE FROM `t_alert_warning_information` where DELETE_FLAG ="+DataValidity.VALID.getState()+" ";
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
	
	public Map<String,Object> getStatisticalGrade(Long orgId) {
		Map<String,Object> result = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> condition = new HashMap<>();
		int sum = 0;
		int sum1 = 0;
		int sum2 = 0;
		int sum3 = 0;
		int alreadyProcessed = 0;
		StringBuilder sql = new StringBuilder("SELECT COUNT(1) as count, SUM(IF(WARNING_LEVEL = 1, 1, 0)) as sum1, SUM(IF(WARNING_LEVEL = 2, 1, 0)) as sum2, SUM(IF(WARNING_LEVEL = 3, 1, 0)) as sum3, SUM(IF(WARNING_STATE = 20, 1, 0)) as sum4 FROM t_alert_warning_information WHERE 1 = 1");
		if(null!=orgId){
			sql.append(" and ORG_ID = :orgId");
			condition.put("orgId",orgId);
		}
		try {
			Query sq = em.createNativeQuery(sql.toString());
			for (Map.Entry<String, Object> e : condition.entrySet()) {
				sq.setParameter(e.getKey(), e.getValue());
			}
			Object[] d = (Object[]) sq.getSingleResult();
			if (null != d && d.length == 5) {
				sum = Integer.valueOf(String.valueOf(d[0]));
				sum1 = Integer.valueOf(String.valueOf(d[1]));
				sum2 = Integer.valueOf(String.valueOf(d[2]));
				sum3 = Integer.valueOf(String.valueOf(d[3]));
				alreadyProcessed = Integer.valueOf(String.valueOf(d[4]));
			}
			data.put("total", sum);
			data.put("alreadyProcessed", alreadyProcessed);
			data.put("sum", sum);
			data.put("sum1", sum1);
			data.put("sum2", sum2);
			data.put("sum3", sum3);
			//统计下的列表最近想的前20条
			StringBuilder lql = new StringBuilder("SELECT * FROM t_alert_warning_information WHERE 1 = 1");
			if (null != orgId) {
				lql.append(" and ORG_ID = :orgId");
				condition.put("orgId", orgId);
			}
			lql.append(" order by WARNING_TIME desc limit 20");
			Query lq = em.createNativeQuery(lql.toString(), AlertWarningInformation.class);
			for (Map.Entry<String, Object> e : condition.entrySet()) {
				lq.setParameter(e.getKey(), e.getValue());
			}
			List<AlertWarningInformation> alertWarningInformationList = lq.getResultList();
			for(AlertWarningInformation aw : alertWarningInformationList){
				if(!aw.getWarningType().equals("报道注册预警")) {
					aw.setWarningType(WarningType.valueOf(aw.getWarningType()).getValue());
				}
			}
			data.put("alertWarningInformationList", alertWarningInformationList);
		}catch (Exception e){
			e.printStackTrace();
			result.put("success", false);
			result.put("message","预警级别汇总统计异常！");
			return result;
		}
		result.put("success", true);
		result.put("data", data);
		return result;
	}

	public Map<String,Object> getStatisticalCollege(Long orgId) {
		Map<String,Object> result = new HashMap<>();
		List<CollegeStatisticsDTO> collegeStatisticsDTOList = new ArrayList<>();
		Map<String, Object> condition = new HashMap<>();
		StringBuilder sql = new StringBuilder("SELECT COLLOGE_NAME, SUM(IF(WARNING_LEVEL = 1, 1, 0)) as sum1, SUM(IF(WARNING_LEVEL = 2, 1, 0)) as sum2, SUM(IF(WARNING_LEVEL = 3, 1, 0)) as sum3 FROM t_alert_warning_information  WHERE 1 = 1");
		if (null != orgId) {
			sql.append(" and ORG_ID = :orgId");
			condition.put("orgId", orgId);
		}
		sql.append(" GROUP BY COLLOGE_ID");
		try{
		    Query sq = em.createNativeQuery(sql.toString());
		    for (Map.Entry<String, Object> e : condition.entrySet()) {
			    sq.setParameter(e.getKey(), e.getValue());
		    }
		    List<Object> res =  sq.getResultList();
		    if(null!=res){
            for(Object obj: res){
				Object[] d = (Object[]) obj;
				CollegeStatisticsDTO collegeStatisticsDTO = new CollegeStatisticsDTO();
				if(null!=d[0]){
					collegeStatisticsDTO.setCollegeName(String.valueOf(d[0]));
				}
				if(null!=d[1]){
					collegeStatisticsDTO.setSum1(Integer.valueOf(String.valueOf(d[1])));
				}
				if(null!=d[2]){
					collegeStatisticsDTO.setSum2(Integer.valueOf(String.valueOf(d[2])));
				}
				if(null!=d[3]){
					collegeStatisticsDTO.setSum3(Integer.valueOf(String.valueOf(d[3])));
				}
				collegeStatisticsDTOList.add(collegeStatisticsDTO);
			  }
		   }
		}catch (Exception e){
			result.put("success",false);
			result.put("message","按照学院统计每个告警等级的数量异常！");
			return result;
		}
		result.put("success",true);
		result.put("data",collegeStatisticsDTOList);
       return result;
	}


	public Map<String,Object> getStatisticalType(Long orgId) {
		Map<String,Object> result = new HashMap<>();
		List<TypeStatisticsDTO> typeStatisticsDTOList = new ArrayList<>();
		Map<String, Object> condition = new HashMap<>();
		int sum = 0;
		int total = 0;
		int total1 = 0;
		int total2 = 0;
		int total3 = 0;
		int sum1 = 0;
		int sum2 = 0;
		int sum3 = 0;
		StringBuilder sql = new StringBuilder("SELECT WARNING_TYPE, count(1), SUM(IF(WARNING_LEVEL = 1, 1, 0)) as sum1, SUM(IF(WARNING_LEVEL = 2, 1, 0)) as sum2, SUM(IF(WARNING_LEVEL = 3, 1, 0)) as sum3 FROM t_alert_warning_information  WHERE 1 = 1");
		if (null != orgId) {
			sql.append(" and ORG_ID = :orgId");
			condition.put("orgId", orgId);
		}
		sql.append(" GROUP BY WARNING_TYPE");
		try{
			Query sq = em.createNativeQuery(sql.toString());
			for (Map.Entry<String, Object> e : condition.entrySet()) {
				sq.setParameter(e.getKey(), e.getValue());
			}
			List<Object> res =  sq.getResultList();
			if(null!=res){
				for(Object obj: res){
					Object[] d = (Object[]) obj;
					TypeStatisticsDTO typeStatisticsDTO = new TypeStatisticsDTO();
					if(null!=d[0]){
						if(!String.valueOf(d[0]).equals("报道注册预警")) {
							typeStatisticsDTO.setWarningType(WarningType.valueOf(String.valueOf(d[0])).getValue());
						}else {
							typeStatisticsDTO.setWarningType(String.valueOf(d[0]));
						}
					}
					if(null!=d[1]){
						sum = Integer.valueOf(String.valueOf(d[1]));
						total = total +sum;
					}
					if(null!=d[2]){
						sum1 = Integer.valueOf(String.valueOf(d[2]));
						total1 = total1 +sum1;
					}
					if(null!=d[3]){
						sum2 =  Integer.valueOf(String.valueOf(d[3]));
						total2 = total2 +sum2;
					}
					if(null!=d[4]){
						sum3 =  Integer.valueOf(String.valueOf(d[4]));
						total3 = total3 +sum3;
					}
					typeStatisticsDTO.setSum1(sum1);
					typeStatisticsDTO.setSum2(sum2);
					typeStatisticsDTO.setSum3(sum3);
					typeStatisticsDTOList.add(typeStatisticsDTO);
				}
			}
		}catch (Exception e){
			result.put("success",false);
			result.put("message","按照学院统计每个告警等级的数量异常！");
			return result;
		}
		result.put("proportion1",accuracy(total1 * 1.0, total * 1.0, 2));
		result.put("proportion2", accuracy(total2 * 1.0, total * 1.0, 2));
		result.put("proportion3", accuracy(total3 * 1.0, total * 1.0, 2));
		result.put("success",true);
		result.put("typeStatisticsDTOList",typeStatisticsDTOList);
		return result;
	}

	public Map<String,Object> getStatisticalCollegeType(Long orgId, String type) {
		Map<String,Object> result = new HashMap<>();
		List<CollegeStatisticsDTO> collegeStatisticsDTOList = new ArrayList<>();
		Map<String, Object> condition = new HashMap<>();
		StringBuilder sql = new StringBuilder("SELECT COLLOGE_NAME, SUM(IF(WARNING_LEVEL = 1, 1, 0)) as sum1, SUM(IF(WARNING_LEVEL = 2, 1, 0)) as sum2, SUM(IF(WARNING_LEVEL = 3, 1, 0)) as sum3 FROM t_alert_warning_information  WHERE 1 = 1");
		if (null != orgId) {
			sql.append(" and ORG_ID = :orgId");
			condition.put("orgId", orgId);
		}
		if (null != type) {
			sql.append(" and WARNING_TYPE = :type");
			condition.put("type", type);
		}
		sql.append(" GROUP BY COLLOGE_ID");
		try{
			Query sq = em.createNativeQuery(sql.toString());
			for (Map.Entry<String, Object> e : condition.entrySet()) {
				sq.setParameter(e.getKey(), e.getValue());
			}
			List<Object> res =  sq.getResultList();
			if(null!=res){
				for(Object obj: res){
					Object[] d = (Object[]) obj;
					CollegeStatisticsDTO collegeStatisticsDTO = new CollegeStatisticsDTO();
					if(null!=d[0]){
						collegeStatisticsDTO.setCollegeName(String.valueOf(d[0]));
					}
					if(null!=d[1]){
						collegeStatisticsDTO.setSum1(Integer.valueOf(String.valueOf(d[1])));
					}
					if(null!=d[2]){
						collegeStatisticsDTO.setSum2(Integer.valueOf(String.valueOf(d[2])));
					}
					if(null!=d[3]){
						collegeStatisticsDTO.setSum3(Integer.valueOf(String.valueOf(d[3])));
					}
					collegeStatisticsDTOList.add(collegeStatisticsDTO);
				}
			}
		}catch (Exception e){
			result.put("success",false);
			result.put("message", "按照学院统计每个告警等级的数量异常！");
			return result;
		}
		result.put("success",true);
		result.put("data",collegeStatisticsDTOList);
		return result;
	}

	public static String accuracy(double num, double total, int scale){
		DecimalFormat df;
		df = (DecimalFormat) NumberFormat.getInstance();
		//可以设置精确几位小数
		df.setMaximumFractionDigits(scale);
		//模式 例如四舍五入
		df.setRoundingMode(RoundingMode.HALF_UP);
		double accuracy_num = num / total * 100;
		return df.format(accuracy_num);
	}
}
