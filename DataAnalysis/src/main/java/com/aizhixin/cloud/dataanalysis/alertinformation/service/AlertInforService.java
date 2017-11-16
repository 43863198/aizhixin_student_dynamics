package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.RegisterAlertCountDomain;
import com.aizhixin.cloud.dataanalysis.common.service.AuthUtilService;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;


public class AlertInforService {

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
			return domain;
		}
	};
	
	public List<RegisterAlertCountDomain> findRegisterCountInfor() {

		String querySql = " SELECT COUNT(1) as countNum,COLLOGE_ID,COLLOGE_NAME,WARNING_LEVEL FROM `t_alert_warning_information` where DELETE_FLAG = 0 and ORG_ID =1 and WARNING_TYPE =1 GROUP BY COLLOGE_ID,WARNING_LEVEL ORDER BY COLLOGE_ID,WARNING_LEVEL ;";

		return pageJdbcUtil.getInfo(querySql, registerCountRm);
	}
}
