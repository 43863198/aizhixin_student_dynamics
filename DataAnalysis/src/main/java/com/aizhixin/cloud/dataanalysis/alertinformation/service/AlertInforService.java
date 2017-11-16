package com.aizhixin.cloud.dataanalysis.alertinformation.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.aizhixin.cloud.dataanalysis.alertinformation.domain.RegisterAlertCountDomain;
import com.aizhixin.cloud.dataanalysis.common.service.AuthUtilService;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import com.aizhixin.cloud.studentpractice.task.domain.StuInforDomain;


public class AlertInforService {

	@Autowired
	private PageJdbcUtil pageJdbcUtil;
	@Autowired
	private AuthUtilService authUtilService;
	
	RowMapper<StuInforDomain> registerCountRm = new RowMapper<StuInforDomain>() {

		@Override
		public StuInforDomain mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			// TODO Auto-generated method stub
			StuInforDomain domain = new StuInforDomain();
			domain.setId(rs.getLong("STUDENT_ID"));
			domain.setMentorCompanyName(rs.getString("ENTERPRISE_NAME"));
			return domain;
		}
	};
	
	public List<RegisterAlertCountDomain> findRegisterCountInfor() {

		String querySql = " SELECT DISTINCT STUDENT_ID,ENTERPRISE_NAME FROM `sp_student_task` where DELETE_FLAG = 0 and org_id is not null ";

		return pageJdbcUtil.getInfo(querySql, registerCountRm);
	}
}
