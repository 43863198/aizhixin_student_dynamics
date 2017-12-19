package com.aizhixin.cloud.dataanalysis.monitor.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aizhixin.cloud.dataanalysis.common.service.AuthUtilService;
import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import com.aizhixin.cloud.dataanalysis.monitor.dto.TeachingScheduleDTO;
import com.aizhixin.cloud.dataanalysis.monitor.entity.TeachingScheduleStatistics;
import com.aizhixin.cloud.dataanalysis.monitor.service.TeachingScheduleStatisticsService;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aizhixin.cloud.dataanalysis.setup.domain.WarningTypeDomain;

@Component
public class TeachingScheduleJob {

	public volatile static boolean flag = true;

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private WarningTypeService warningTypeService;
	@Autowired
	private TeachingScheduleStatisticsService teachingScheduleStatisticsService;
	@Autowired
	private PageJdbcUtil pageJdbcUtil;
	@Autowired
	private AuthUtilService authUtilService;

	RowMapper<TeachingScheduleDTO> teachingScheduleRm = new RowMapper<TeachingScheduleDTO>() {

		@Override
		public TeachingScheduleDTO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			// TODO Auto-generated method stub
			TeachingScheduleDTO domain = new TeachingScheduleDTO();
			domain.setCountNum(rs.getLong("countNum"));
			domain.setPeriodNo(rs.getInt("PERIOD_NO"));
			domain.setOrgId(rs.getLong("ORGAN_ID"));
			return domain;
		}
	};

	/**
	 * 按条件统计当天排课数量
	 * 
	 * @return
	 */
	public List<TeachingScheduleDTO> teachingScheduleCount(String orgIds,String teachDate) {

		String ddDbName = authUtilService.getDdDbName();
		String querySql = "SELECT ORGAN_ID,PERIOD_NO,count(id) as countNum FROM "+ddDbName+"`.dd_schedule` where ORGAN_ID in ("+orgIds+") and TEACH_DATE = '"+teachDate+"' GROUP BY ORGAN_ID,PERIOD_NO";

		return pageJdbcUtil.getInfo(querySql, teachingScheduleRm);
	}

	@Scheduled(cron = "0 0/1 * * * ?")
	public void getTeachingScheduleJob() {

		String teachDate =DateUtil
		.getCurrentTime(DateUtil.FORMAT_SHORT);

		List<WarningTypeDomain> orgIdList = warningTypeService.getAllOrgId();
		if (null != orgIdList && orgIdList.size() > 0) {

			List<TeachingScheduleStatistics> statisticsList = new ArrayList<TeachingScheduleStatistics>();
			HashMap<Long, TeachingScheduleStatistics> statisticsMap = new HashMap<Long, TeachingScheduleStatistics>();

			String orgIds = "";
			for (WarningTypeDomain domain : orgIdList) {
				if (null != domain && null != domain.getOrgId()) {
					if (StringUtils.isEmpty(orgIds)) {
						orgIds = domain.getOrgId().toString();
					} else {
						orgIds += "," + domain.getOrgId().toString();
					}
				}
			}
			List<TeachingScheduleDTO> countList = this.teachingScheduleCount(orgIds,teachDate);
			for (TeachingScheduleDTO dto : countList) {
				if (null != dto) {
					TeachingScheduleStatistics statistics = statisticsMap.get(dto
							.getOrgId());
					if (null != statistics) {
						if(dto.getPeriodNo() == 1){
							statistics.setCourseNum1(statistics.getCourseNum1()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 2){
							statistics.setCourseNum1(statistics.getCourseNum1()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 3){
							statistics.setCourseNum3(statistics.getCourseNum3()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 4){
							statistics.setCourseNum3(statistics.getCourseNum3()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 5){
							statistics.setCourseNum5(statistics.getCourseNum5()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 6){
							statistics.setCourseNum5(statistics.getCourseNum5()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 7){
							statistics.setCourseNum7(statistics.getCourseNum7()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 8){
							statistics.setCourseNum7(statistics.getCourseNum7()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 9){
							statistics.setCourseNum9(statistics.getCourseNum9()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 10){
							statistics.setCourseNum9(statistics.getCourseNum9()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 11){
							statistics.setCourseNum11(statistics.getCourseNum11()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 12){
							statistics.setCourseNum11(statistics.getCourseNum11()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 13){
							statistics.setCourseNum13(statistics.getCourseNum13()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 14){
							statistics.setCourseNum13(statistics.getCourseNum13()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 15){
							statistics.setCourseNum15(statistics.getCourseNum15()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 16){
							statistics.setCourseNum15(statistics.getCourseNum15()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() ==17){
							statistics.setCourseNum17(statistics.getCourseNum17()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 18){
							statistics.setCourseNum17(statistics.getCourseNum17()+dto.getCountNum().intValue());
						}
						
						statisticsMap.put(dto.getOrgId(), statistics);
					} else {
						statistics = new TeachingScheduleStatistics();
						statistics.setOrgId(dto.getOrgId());
						statistics.setStatisticalTime(teachDate);
						
						if(dto.getPeriodNo() == 1){
							statistics.setCourseNum1(statistics.getCourseNum1()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 2){
							statistics.setCourseNum1(statistics.getCourseNum1()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 3){
							statistics.setCourseNum3(statistics.getCourseNum3()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 4){
							statistics.setCourseNum3(statistics.getCourseNum3()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 5){
							statistics.setCourseNum5(statistics.getCourseNum5()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 6){
							statistics.setCourseNum5(statistics.getCourseNum5()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 7){
							statistics.setCourseNum7(statistics.getCourseNum7()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 8){
							statistics.setCourseNum7(statistics.getCourseNum7()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 9){
							statistics.setCourseNum9(statistics.getCourseNum9()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 10){
							statistics.setCourseNum9(statistics.getCourseNum9()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 11){
							statistics.setCourseNum11(statistics.getCourseNum11()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 12){
							statistics.setCourseNum11(statistics.getCourseNum11()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 13){
							statistics.setCourseNum13(statistics.getCourseNum13()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 14){
							statistics.setCourseNum13(statistics.getCourseNum13()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 15){
							statistics.setCourseNum15(statistics.getCourseNum15()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 16){
							statistics.setCourseNum15(statistics.getCourseNum15()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() ==17){
							statistics.setCourseNum17(statistics.getCourseNum17()+dto.getCountNum().intValue());
						}
						if(dto.getPeriodNo() == 18){
							statistics.setCourseNum17(statistics.getCourseNum17()+dto.getCountNum().intValue());
						}
						
						statisticsMap.put(dto.getOrgId(), statistics);
					}
				}
			}

			Iterator iter = statisticsMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				TeachingScheduleStatistics statistics =(TeachingScheduleStatistics) entry.getValue();
				statisticsList.add(statistics);
			}
			teachingScheduleStatisticsService.saveList(statisticsList);
		}
	}


}
