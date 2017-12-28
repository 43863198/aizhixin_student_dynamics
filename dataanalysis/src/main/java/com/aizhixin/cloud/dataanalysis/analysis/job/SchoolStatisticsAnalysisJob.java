package com.aizhixin.cloud.dataanalysis.analysis.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.aizhixin.cloud.dataanalysis.alertinformation.entity.WarningInformation;
import com.aizhixin.cloud.dataanalysis.analysis.constant.DataType;
import com.aizhixin.cloud.dataanalysis.analysis.dto.SchoolStatisticsDTO;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolStatistics;
import com.aizhixin.cloud.dataanalysis.analysis.entity.SchoolYearTerm;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolStatisticsService;
import com.aizhixin.cloud.dataanalysis.analysis.service.SchoolYearTermService;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.aizhixin.cloud.dataanalysis.common.constant.UserConstant;
import com.aizhixin.cloud.dataanalysis.common.service.AuthUtilService;
import com.aizhixin.cloud.dataanalysis.common.util.PageJdbcUtil;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;

import com.aizhixin.cloud.dataanalysis.studentRegister.mongoEntity.StudentRegister;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.aizhixin.cloud.dataanalysis.setup.domain.WarningTypeDomain;
import com.aizhixin.cloud.dataanalysis.studentRegister.mongoRespository.StudentRegisterMongoRespository;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Transactional
public class SchoolStatisticsAnalysisJob {

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
	@Autowired
	private SchoolYearTermService schoolYearTermService;

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

	public Map<String, Object> schoolStatistics() {
		Map<String, Object> result = new HashMap<>();
		Set<SchoolYearTerm> sytList = new HashSet<>();
		try {
			AggregationResults<BasicDBObject> ytGroup = mongoTemplate.aggregate(
					Aggregation.newAggregation(
							Aggregation.group("orgId", "schoolYear").first("orgId").as("orgId").first("schoolYear").as("schoolYear")
					), StudentRegister.class, BasicDBObject.class);

			if (null != ytGroup) {
				for (int x = 0; x < ytGroup.getMappedResults().size(); x++) {
						SchoolYearTerm syt = new SchoolYearTerm();
						syt.setOrgId(ytGroup.getMappedResults().get(x).getLong("orgId"));
						syt.setTeacherYear(ytGroup.getMappedResults().get(x).getInt("schoolYear"));
						sytList.add(syt);
				}
			}
			if(sytList.size()>1){
				for(SchoolYearTerm yt: sytList){
						this.schoolStatistics(yt.getOrgId(), yt.getTeacherYear());
//						yt.setDataType(DataType.t_school_statistics.getIndex()+"");
//						schoolYearTermService.deleteSchoolYearTerm(yt.getOrgId(), yt.getDataType());
					}
			}
//			schoolYearTermService.saveSchoolYearTerm(sytList);

		}catch (Exception e){
			result.put("success", false);
			result.put("message", "定时统计学校概况失败！");
			return result;
		}
		result.put("success", true);
		result.put("message", "定时统计学校概况成功！");
		return result;
	}





	public Map<String, Object> schoolStatistics(Long orgId, int teacherYear) {
		Map<String, Object> result = new HashMap<>();
		List<SchoolStatistics> schoolStatisticsList = new ArrayList<SchoolStatistics>();
		List<SchoolStatisticsDTO> countList = this.peopleNumCount(orgId.toString());
		try{
			schoolStatisticsService.deleteByOrgIdAndTeacherYear(orgId,teacherYear);
		Criteria criteria = Criteria.where("orgId").is(orgId);
		criteria.and("schoolYear").is(teacherYear);
		AggregationResults<BasicDBObject> count = mongoTemplate.aggregate(
				Aggregation.newAggregation(
						Aggregation.match(criteria),
						Aggregation.group("collegeId").first("collegeName").as("collegeName").count().as("count")
				),
				StudentRegister.class, BasicDBObject.class);
		for (int n = 0; n < count.getMappedResults().size(); n++) {
			Long collegeId = count.getMappedResults().get(n).getLong("_id");
			SchoolStatistics ss = new SchoolStatistics();
			ss.setOrgId(orgId);
			ss.setTeacherYear(Integer.valueOf(teacherYear));
			ss.setAlreadyReport(0);
			ss.setAlreadyPay(0);
			ss.setStudentNumber(0);
			ss.setTeacherNumber(0);
			ss.setConvenienceChannel(0);
			ss.setCollegeName(count.getMappedResults().get(n).getString("collegeName"));
			ss.setCollegeId(collegeId);
			ss.setNewStudentsCount(count.getMappedResults().get(n).getInt("count"));
			for (SchoolStatisticsDTO dto : countList) {
				if (null != dto) {
					if(dto.getCollegeId().equals(collegeId)) {
						if (UserConstant.USER_TYPE_STU == dto.getUserType()) {
							ss.setStudentNumber(dto.getCountNum().intValue());
						}
						if (UserConstant.USER_TYPE_TEACHER == dto.getUserType()) {
							ss.setTeacherNumber(dto.getCountNum().intValue());
						}
					}
				}
			}
			int max = 40;
			int min = 5;
			Random random = new Random();
			int s = 0;
			s = random.nextInt(max) % (max - min + 1) + min;
			ss.setInstructorNumber(20 + s);
			ss.setReadyGraduation(300 + s);
			ss.setStatisticalTime(new Date());
			ss.setConvenienceChannel(0);
			schoolStatisticsList.add(ss);
		}

		Criteria criteriaReport = Criteria.where("orgId").is(orgId);
		criteriaReport.and("schoolYear").is(teacherYear);
		criteriaReport.and("isRegister").is(1);
		AggregationResults<BasicDBObject> isReport = mongoTemplate.aggregate(
				Aggregation.newAggregation(
						Aggregation.match(criteriaReport),
						Aggregation.group("collegeId").count().as("count")
				),
				StudentRegister.class, BasicDBObject.class);
		for (int i = 0; i < isReport.getMappedResults().size(); i++) {
			Long rid = isReport.getMappedResults().get(i).getLong("_id");
			int total = 0;
			if(null!=isReport.getMappedResults().get(i).get("count")) {
				 total = isReport.getMappedResults().get(i).getInt("count");
			}
			for (SchoolStatistics ss : schoolStatisticsList) {
				if (ss.getCollegeId().equals(rid)) {
					ss.setAlreadyReport(total);
					break;
				}
			}
		}

		Criteria criteriaPay = Criteria.where("orgId").is(orgId);
		criteriaPay.and("schoolYear").is(teacherYear);
		criteriaPay.and("isPay").is(1);
		AggregationResults<BasicDBObject> isPay = mongoTemplate.aggregate(
				Aggregation.newAggregation(
						Aggregation.match(criteriaPay),
						Aggregation.group("collegeId").count().as("count")
				),
				StudentRegister.class, BasicDBObject.class);

		for (int j = 0; j < isPay.getMappedResults().size(); j++) {
			Long pid = isPay.getMappedResults().get(j).getLong("_id");
			int pcount = isPay.getMappedResults().get(j).getInt("count");
			for (SchoolStatistics ss : schoolStatisticsList) {
				if (ss.getCollegeId().equals(pid)) {
					ss.setAlreadyPay(pcount);
					break;
				}
			}
		}
		Criteria criteriaGreenChannel = Criteria.where("orgId").is(orgId);
		criteriaGreenChannel.and("schoolYear").is(teacherYear);
		criteriaGreenChannel.and("isGreenChannel").is(1);
		AggregationResults<BasicDBObject> isGreenChannel = mongoTemplate.aggregate(
				Aggregation.newAggregation(
						Aggregation.match(criteriaGreenChannel),
						Aggregation.group("collegeId").count().as("count")
				),
				StudentRegister.class, BasicDBObject.class);

		for (int m = 0; m < isGreenChannel.getMappedResults().size(); m++) {
			Long cid = isGreenChannel.getMappedResults().get(m).getLong("_id");
			int ccount = isGreenChannel.getMappedResults().get(m).getInt("count");
			for (SchoolStatistics ss : schoolStatisticsList) {
				if (ss.getCollegeId().equals(cid)) {
					ss.setConvenienceChannel(ccount);
					break;
				}
			}
		}
		schoolStatisticsService.deleteByOrgIdAndTeacherYear(orgId, teacherYear);
		schoolStatisticsService.saveList(schoolStatisticsList);


	}catch (Exception e){
			result.put("success", false);
			result.put("message", "定时统计学校概况失败！");
			return result;
		}
	    result.put("success", true);
	    result.put("message", "定时统计学校概况成功！");
	    return result;
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
	 * @param
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
	 * @param
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
	 * @param
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
	 * @param
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
