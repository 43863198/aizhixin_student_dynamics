package com.aizhixin.cloud.dataanalysis.analysis.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolStatisticsService;
import com.aizhixin.cloud.dataanalysis.common.constant.UserConstant;
import com.aizhixin.cloud.dataanalysis.common.service.AuthUtilService;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aizhixin.cloud.dataanalysis.setup.domain.WarningTypeDomain;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component
public class SchoolStatisticsJob {

	public volatile static boolean flag = true;

	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private WarningTypeService warningTypeService;
	@Autowired
	private SchoolStatisticsService schoolStatisticsService;
	@Autowired
	private StudentRegisterMongoRespository stuRegisterMongoRespository;
	@Autowired
	private PageJdbcUtil pageJdbcUtil;
	@Autowired
	private AuthUtilService authUtilService;

	RowMapper<SchoolStatisticsDTO> peopleRm = new RowMapper<SchoolStatisticsDTO>() {

		@Override
		public SchoolStatisticsDTO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			// TODO Auto-generated method stub
			SchoolStatisticsDTO domain = new SchoolStatisticsDTO();
			domain.setCountNum(rs.getLong("countNum"));
			domain.setCollegeId(rs.getLong("COLLEGE_ID"));
			domain.setOrgId(rs.getLong("ORG_ID"));
			domain.setUserType(rs.getInt("USER_TYPE"));
			return domain;
		}
	};

	/**
	 * 按条件统计学生和老师数量
	 * 
	 * @return
	 */
	public List<SchoolStatisticsDTO> peopleNumCount(String orgIds) {

		String orgDbName = authUtilService.getOrgDbName();
		String querySql = "SELECT COLLEGE_ID,count(id) as countNum,USER_TYPE,ORG_ID FROM "
				+ orgDbName
				+ ".`t_user` where DELETE_FLAG = 0 and COLLEGE_ID is not null and  USER_TYPE in("
				+ UserConstant.USER_TYPE_STU
				+ ","
				+ UserConstant.USER_TYPE_TEACHER
				+ ") and ORG_ID in("
				+ orgIds
				+ ") GROUP BY COLLEGE_ID,USER_TYPE;";

		return pageJdbcUtil.getInfo(querySql, peopleRm);
	}

//	 @Scheduled(cron = "0 0/15 * * * ?")
	public void schoolStatisticsJob() {

		Calendar c = Calendar.getInstance();
		// 当前年份
		int schoolYear = c.get(Calendar.YEAR);

		List<WarningTypeDomain> orgIdList = warningTypeService.getAllOrgId();
		if (null != orgIdList && orgIdList.size() > 0) {

			List<SchoolStatistics> statisticsList = new ArrayList<SchoolStatistics>();
			HashMap<Long, SchoolStatistics> statisticsMap = new HashMap<Long, SchoolStatistics>();

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
			List<SchoolStatisticsDTO> countList = this.peopleNumCount(orgIds);
			for (SchoolStatisticsDTO dto : countList) {
				if (null != dto) {
					SchoolStatistics schoolStatistics = statisticsMap.get(dto
							.getCollegeId());
					if (null != schoolStatistics) {
						if (UserConstant.USER_TYPE_STU == dto.getUserType()) {
							schoolStatistics.setStudentNumber(dto.getCountNum()
									.intValue());
						}
						if (UserConstant.USER_TYPE_TEACHER == dto.getUserType()) {
							schoolStatistics.setTeacherNumber(dto.getCountNum()
									.intValue());
						}
						statisticsMap.put(dto.getCollegeId(), schoolStatistics);
					} else {
						schoolStatistics = new SchoolStatistics();
						schoolStatistics.setTeacherYear(Integer.valueOf(schoolYear));
						if (UserConstant.USER_TYPE_STU == dto.getUserType()) {
							schoolStatistics.setStudentNumber(dto.getCountNum()
									.intValue());
						}
						if (UserConstant.USER_TYPE_TEACHER == dto.getUserType()) {
							schoolStatistics.setTeacherNumber(dto.getCountNum()
									.intValue());
						}
						schoolStatistics.setCollegeId(dto.getCollegeId());
						schoolStatistics.setOrgId(dto.getOrgId());
						statisticsMap.put(dto.getCollegeId(), schoolStatistics);
					}
				}
			}

			this.getNewStuNumCount(orgIds, statisticsMap, schoolYear);
			this.setRegisterStuNumCount(orgIds, statisticsMap, schoolYear);
			this.setPayStuNumCount(orgIds, statisticsMap, schoolYear);
			this.setGreenChannelStuNumCount(orgIds, statisticsMap, schoolYear);
			
			Iterator iter = statisticsMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				SchoolStatistics schoolStatistics =(SchoolStatistics) entry.getValue();
				statisticsList.add(schoolStatistics);
			}
			schoolStatisticsService.saveList(statisticsList);
		}
	}

	/**
	 * 按院系统计当前学年新生人数
	 * 
	 * @param domain
	 * @param statisticsMap
	 * @param schoolYear
	 */
	private void getNewStuNumCount(String orgIds,
			HashMap<Long, SchoolStatistics> statisticsMap, int schoolYear) {
		String groupStr1 = "{ $group: {\"_id\": {  \"orgId\": \"$orgId\" ,\"collegeId\" : \"$collegeId\",\"collegeName\" : \"$collegeName\",\"jobNum\" : \"$jobNum\" } } } ";
		DBObject group1 = (DBObject) JSON.parse(groupStr1);
		String groupStr2 = "{ $group: {\"_id\": { \"collegeId\" : \"$_id.collegeId\",\"collegeName\" : \"$_id.collegeName\", \"orgId\": \"$_id.orgId\"  }, count : { $sum : 1 }  } }";
		DBObject group2 = (DBObject) JSON.parse(groupStr2);
		String matchStr = "{$match :{\"orgId\":{\"$in\":[" + orgIds
				+ "]},\"schoolYear\":" + schoolYear + "}}";
		DBObject match = (DBObject) JSON.parse(matchStr);
		String projectStr = "{ $project : {\"_id\": 0, \"collegeId\" : \"$_id.collegeId\",\"collegeName\" : \"$_id.collegeName\",\"orgId\" : \"$_id.orgId\" , \"count\" : 1}}";
		DBObject project = (DBObject) JSON.parse(projectStr);
		AggregationOutput output = mongoTemplate.getCollection(
				"StudentRegister").aggregate(match, group1, group2, project);

		for (Iterator<DBObject> it = output.results().iterator(); it.hasNext();) {
			BasicDBObject dbo = (BasicDBObject) it.next();
			long collegeId = (Long) dbo.get("collegeId");
			int countNum = (Integer) dbo.get("count");
			String collegeName = (String) dbo.get("collegeName");
			SchoolStatistics schoolStatistics = statisticsMap.get(collegeId);
			if (null != schoolStatistics) {
				schoolStatistics.setCollegeId(collegeId);
				schoolStatistics.setCollegeName(collegeName);
				schoolStatistics.setNewStudentsCount(countNum);
				statisticsMap.put(collegeId, schoolStatistics);
			} else {
				schoolStatistics = new SchoolStatistics();
				schoolStatistics.setTeacherYear(Integer.valueOf(schoolYear));
				Long orgId = (Long) dbo.get("orgId");
				schoolStatistics.setOrgId(orgId);
				schoolStatistics.setCollegeId(collegeId);
				schoolStatistics.setCollegeName(collegeName);
				schoolStatistics.setNewStudentsCount(countNum);
				statisticsMap.put(collegeId, schoolStatistics);
			}
		}
	}

	/**
	 * 按院系统计已报到人数
	 * 
	 * @param domain
	 * @param statisticsMap
	 * @param schoolYear
	 */
	private void setRegisterStuNumCount(String orgIds,
			HashMap<Long, SchoolStatistics> statisticsMap, int schoolYear) {
		String groupStr1 = "{ $group: {\"_id\": {  \"orgId\": \"$orgId\" ,\"collegeId\" : \"$collegeId\",\"collegeName\" : \"$collegeName\",\"jobNum\" : \"$jobNum\" } } } ";
		DBObject group1 = (DBObject) JSON.parse(groupStr1);
		String groupStr2 = "{ $group: {\"_id\": { \"collegeId\" : \"$_id.collegeId\",\"collegeName\" : \"$_id.collegeName\", \"orgId\": \"$_id.orgId\"  }, count : { $sum : 1 }  } }";
		DBObject group2 = (DBObject) JSON.parse(groupStr2);
		String matchStr = "{$match :{\"orgId\":{\"$in\":[" + orgIds
				+ "]},\"schoolYear\":" + schoolYear
				+ ",\"actualRegisterDate\":" + "{\"$ne\":null}" + "}}";
		DBObject match = (DBObject) JSON.parse(matchStr);
		String projectStr = "{ $project : {\"_id\": 0, \"collegeId\" : \"$_id.collegeId\",\"collegeName\" : \"$_id.collegeName\",\"orgId\" : \"$_id.orgId\" , \"count\" : 1}}";
		DBObject project = (DBObject) JSON.parse(projectStr);
		AggregationOutput output = mongoTemplate.getCollection(
				"StudentRegister").aggregate(match, group1, group2, project);

		for (Iterator<DBObject> it = output.results().iterator(); it.hasNext();) {
			BasicDBObject dbo = (BasicDBObject) it.next();
			long collegeId = (Long) dbo.get("collegeId");
			String collegeName = (String) dbo.get("collegeName");
			int countNum = (Integer) dbo.get("count");
			SchoolStatistics schoolStatistics = statisticsMap.get(collegeId);
			if (null != schoolStatistics) {
				schoolStatistics.setCollegeName(collegeName);
				schoolStatistics.setAlreadyReport(countNum);
				statisticsMap.put(collegeId, schoolStatistics);
			}
		}
	}

	/**
	 * 按院系统计已缴费人数
	 * 
	 * @param domain
	 * @param statisticsMap
	 * @param schoolYear
	 */
	private void setPayStuNumCount(String orgIds,
			HashMap<Long, SchoolStatistics> statisticsMap, int schoolYear) {
		String groupStr1 = "{ $group: {\"_id\": {  \"orgId\": \"$orgId\" ,\"collegeId\" : \"$collegeId\",\"collegeName\" : \"$collegeName\",\"jobNum\" : \"$jobNum\"} } } ";
		DBObject group1 = (DBObject) JSON.parse(groupStr1);
		String groupStr2 = "{ $group: {\"_id\": { \"collegeId\" : \"$_id.collegeId\",\"collegeName\" : \"$_id.collegeName\", \"orgId\": \"$_id.orgId\"  }, count : { $sum : 1 }  } }";
		DBObject group2 = (DBObject) JSON.parse(groupStr2);
		String matchStr = "{$match :{\"orgId\":{\"$in\":[" + orgIds
				+ "]},\"schoolYear\":" + schoolYear + ",\"isPay\":" + 1 + "}}";
		DBObject match = (DBObject) JSON.parse(matchStr);
		String projectStr = "{ $project : {\"_id\": 0, \"collegeId\" : \"$_id.collegeId\",\"collegeName\" : \"$_id.collegeName\",\"orgId\" : \"$_id.orgId\" , \"count\" : 1}}";
		DBObject project = (DBObject) JSON.parse(projectStr);
		AggregationOutput output = mongoTemplate.getCollection(
				"StudentRegister").aggregate(match, group1, group2, project);

		for (Iterator<DBObject> it = output.results().iterator(); it.hasNext();) {
			BasicDBObject dbo = (BasicDBObject) it.next();
			long collegeId = (Long) dbo.get("collegeId");
			String collegeName = (String) dbo.get("collegeName");
			int countNum = (Integer) dbo.get("count");
			SchoolStatistics schoolStatistics = statisticsMap.get(collegeId);
			if (null != schoolStatistics) {
				schoolStatistics.setCollegeName(collegeName);
				schoolStatistics.setAlreadyPay(countNum);
				statisticsMap.put(collegeId, schoolStatistics);
			}
		}
	}

	/**
	 * 按院系统计绿色通道人数
	 * 
	 * @param domain
	 * @param statisticsMap
	 * @param schoolYear
	 */
	private void setGreenChannelStuNumCount(String orgIds,
			HashMap<Long, SchoolStatistics>   statisticsMap, int schoolYear) {
		String groupStr1 = "{ $group: {\"_id\": {  \"orgId\": \"$orgId\" ,\"collegeId\" : \"$collegeId\",\"collegeName\" : \"$collegeName\",\"jobNum\" : \"$jobNum\"} } } ";
		DBObject group1 = (DBObject) JSON.parse(groupStr1);
		String groupStr2 = "{ $group: {\"_id\": { \"collegeId\" : \"$_id.collegeId\",\"collegeName\" : \"$_id.collegeName\", \"orgId\": \"$_id.orgId\"  }, count : { $sum : 1 }  } }";
		DBObject group2 = (DBObject) JSON.parse(groupStr2);
		String matchStr = "{$match :{\"orgId\":{\"$in\":[" + orgIds
				+ "]},\"schoolYear\":" + schoolYear + ",\"isGreenChannel\":"
				+ 1 + "}}";
		DBObject match = (DBObject) JSON.parse(matchStr);
		String projectStr = "{ $project : {\"_id\": 0, \"collegeId\" : \"$_id.collegeId\",\"collegeName\" : \"$_id.collegeName\",\"orgId\" : \"$_id.orgId\" , \"count\" : 1}}";
		DBObject project = (DBObject) JSON.parse(projectStr);
		AggregationOutput output = mongoTemplate.getCollection(
				"StudentRegister").aggregate(match, group1, group2, project);

		for (Iterator<DBObject> it = output.results().iterator(); it.hasNext();) {
			BasicDBObject dbo = (BasicDBObject) it.next();
			long collegeId = (Long) dbo.get("collegeId");
			String collegeName = (String) dbo.get("collegeName");
			int countNum = (Integer) dbo.get("count");
			SchoolStatistics schoolStatistics = statisticsMap.get(collegeId);
			if (null != schoolStatistics) {
				schoolStatistics.setCollegeName(collegeName);
				schoolStatistics.setConvenienceChannel(countNum);
				statisticsMap.put(collegeId, schoolStatistics);
			}
		}
	}

}
