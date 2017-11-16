package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.RegisterAlertCountDomain;
import com.aizhixin.cloud.dataanalysis.common.core.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.service.AuthUtilService;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-11-15
 */
@Component
@Transactional
public class AlertWarningInformationService {


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

		String querySql = " SELECT COUNT(1) as countNum,COLLOGE_ID,COLLOGE_NAME,WARNING_LEVEL FROM `t_alert_warning_information` where DELETE_FLAG = "+DataValidity.VALID.getState()+" and ORG_ID ="+orgId+" and WARNING_TYPE =1 GROUP BY COLLOGE_ID,WARNING_LEVEL ORDER BY COLLOGE_ID,WARNING_LEVEL ;";

		return pageJdbcUtil.getInfo(querySql, registerCountRm);
	}

}
