package com.aizhixin.cloud.dataanalysis.score.service;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;
import com.aizhixin.cloud.dataanalysis.score.domain.ScoreDomain;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;
import com.aizhixin.cloud.dataanalysis.score.mongoRespository.ScoreMongoRespository;



@Component
public class ScoreService {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ScoreMongoRespository scoreMongoRespository;

	/**
	 * 按条件查询考试成绩
	 * 
	 */
	public ArrayList<ScoreDomain> findScoreInfor(Long orgId){
		
		Calendar c = Calendar.getInstance();
		//当前年份
		int year = c.get(Calendar.YEAR);
		//前一年
		int year1 = year-1;
		int years[] = new int[]{year1,year};
		
		//需要最终删选的成绩结果
		ArrayList<ScoreDomain> scoreDomainList = new ArrayList<ScoreDomain>();
		
		List<Score> totalScoreList = scoreMongoRespository.findAllBySchoolYearInAndOrgId(years, orgId);
		//学年成绩总集合
		HashMap<String,HashMap<String,List<Score>>> resultMap = new HashMap<String,HashMap<String,List<Score>>>(); 
		for(Score score : totalScoreList){
			if(null == resultMap.get(score.getJobNum())){
				//学生某学年的某个学期的成绩集合
				HashMap<String,List<Score>> scoreMap = new HashMap<String,List<Score>>();
				List<Score> userScoreList = new ArrayList<Score>();
				userScoreList.add(score);
				String key = score.getSchoolYear()+"-"+score.getSemester();
				scoreMap.put(key, userScoreList);
				resultMap.put(score.getJobNum(), scoreMap);
			}else{
				HashMap<String,List<Score>> scoreMap = resultMap.get(score.getJobNum());
				String key = score.getSchoolYear()+"-"+score.getSemester();
				if(null == scoreMap.get(key)){
					List<Score> userScoreList = new ArrayList<Score>();
					userScoreList.add(score);
					scoreMap.put(key, userScoreList);
				}else{
					List<Score> userScoreList = scoreMap.get(key);
					userScoreList.add(score);
				}
			}
		}
		
		return getScoreDomainList(orgId, scoreDomainList, resultMap);
	}

	/**
	 * 获取相邻2学期考试成绩数据
	 * @param orgId
	 * @param scoreDomainList
	 * @param resultMap
	 * @return
	 */
	private ArrayList<ScoreDomain> getScoreDomainList(Long orgId,
			ArrayList<ScoreDomain> scoreDomainList,
			HashMap<String, HashMap<String, List<Score>>> resultMap) {
		Iterator iter = resultMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
//			Object key = entry.getKey();
			HashMap<String,List<Score>> scoreMap = (HashMap<String,List<Score>>)entry.getValue();
			if(scoreMap.size() > 1){
				//筛选并计算最近两学期考试成绩
				Set<String> keySet = scoreMap.keySet();
				ArrayList<String> keyList = new ArrayList<String>();
				keyList.addAll(keySet);
				Collections.sort(keyList, Collections.reverseOrder());
				ScoreDomain scoreDomain = null;
				if(scoreMap.keySet().contains(keyList.get(keyList.size()-1))){
					String kegStr = keyList.get(keyList.size()-1);
					scoreDomain = setSecondScoreDomainInfor(orgId,scoreMap, kegStr);
				}
				if(scoreMap.keySet().contains(keyList.get(keyList.size()-2))){
					String kegStr = keyList.get(keyList.size()-2);
					if(null != scoreDomain){
						scoreDomain = setFirstScoreDomainInfor(orgId,scoreMap, kegStr,scoreDomain);
					}
					if(StringUtils.isEmpty(scoreDomain.getFirstAvgradePoint())){
						scoreDomainList.add(scoreDomain);
					}
			   }
			}else{
				//只有一学期成绩跳过预警
				continue;
			}
		}
		
		return scoreDomainList;
	}

	/**
	 * 最近学期成绩信息获取
	 * @param orgId
	 * @param scoreMap
	 * @param keyStr
	 * @return
	 */
	private ScoreDomain setSecondScoreDomainInfor(Long orgId,
			HashMap<String, List<Score>> scoreMap, String keyStr) {
		
		ScoreDomain scoreDomain = new ScoreDomain();
			//计算当前学期的信息
			List<Score> userScoreList = scoreMap.get(keyStr);
			//保存不重复的考试课程编号
			HashMap<String,String> courseMap =  new HashMap<String,String>();
			BigDecimal totalScores = new BigDecimal(0);
			BigDecimal totalGradePoint = new BigDecimal(0);
			for(Score score : userScoreList){
				
				if(StringUtils.isEmpty(scoreDomain.getUserInforDTO().getJobNum())){
					//复制用户基本信息
					scoreDomain.getUserInforDTO().setClassId(score.getClassId());
					scoreDomain.getUserInforDTO().setClassName(score.getClassName());
					scoreDomain.getUserInforDTO().setCollegeId(score.getCollegeId());
					scoreDomain.getUserInforDTO().setCollegeName(score.getCollegeName());
					scoreDomain.getUserInforDTO().setJobNum(score.getJobNum());
					scoreDomain.getUserInforDTO().setOrgId(orgId);
					scoreDomain.getUserInforDTO().setProfessionalId(score.getProfessionalId());
					scoreDomain.getUserInforDTO().setProfessionalName(score.getProfessionalName());
					scoreDomain.getUserInforDTO().setUserId(score.getUserId());
					scoreDomain.getUserInforDTO().setUserName(score.getUserName());
					scoreDomain.getUserInforDTO().setUserPhoto(score.getUserPhoto());
				}
				courseMap.put(score.getScheduleId(), score.getScheduleId());
				BigDecimal finalScore = new BigDecimal(score.getFinalScore());
				totalScores.add(finalScore);
				BigDecimal gradePoint = new BigDecimal(score.getGradePoint());
				totalGradePoint.add(gradePoint);
			}
			if(courseMap.size() > 0){
				BigDecimal avgradePoint = new BigDecimal(totalGradePoint.doubleValue()/courseMap.size());
				scoreDomain.setSecondAvgradePoint(avgradePoint.setScale(2, RoundingMode.HALF_UP).toString());
				String[] keyStrArr = keyStr.split("-");
				scoreDomain.setSecondSchoolYear(Integer.parseInt(keyStrArr[0]));
				scoreDomain.setSecondSemester(Integer.parseInt(keyStrArr[1]));
				scoreDomain.setSecondTotalCourseNums(courseMap.size());
				scoreDomain.setSecondTotalGradePoint(totalGradePoint.toString());
				scoreDomain.setSecondTotalScores(totalScores.toString());
			}else{
				return null;
			}
			
			return scoreDomain;
	}
	
	/**
	 * 最近学期的相邻学期成绩信息获取
	 * @param orgId
	 * @param scoreMap
	 * @param keyStr
	 * @param scoreDomain
	 * @return
	 */
	private ScoreDomain setFirstScoreDomainInfor(Long orgId,
			HashMap<String, List<Score>> scoreMap, String keyStr,ScoreDomain scoreDomain) {
		
			//计算当前学期的信息
			List<Score> userScoreList = scoreMap.get(keyStr);
			//保存不重复的考试课程编号
			HashMap<String,String> courseMap =  new HashMap<String,String>();
			BigDecimal totalScores = new BigDecimal(0);
			BigDecimal totalGradePoint = new BigDecimal(0);
			for(Score score : userScoreList){
				
				courseMap.put(score.getScheduleId(), score.getScheduleId());
				BigDecimal finalScore = new BigDecimal(score.getFinalScore());
				totalScores.add(finalScore);
				BigDecimal gradePoint = new BigDecimal(score.getGradePoint());
				totalGradePoint.add(gradePoint);
			}
			if(courseMap.size() > 0){
				BigDecimal avgradePoint = new BigDecimal(totalGradePoint.doubleValue()/courseMap.size());
				scoreDomain.setSecondAvgradePoint(avgradePoint.setScale(2, RoundingMode.HALF_UP).toString());
				String[] keyStrArr = keyStr.split("-");
				scoreDomain.setFirstSchoolYear(Integer.parseInt(keyStrArr[0]));
				scoreDomain.setFirstSemester(Integer.parseInt(keyStrArr[1]));
				scoreDomain.setFirstTotalCourseNums(courseMap.size());
				scoreDomain.setFirstTotalGradePoint(totalGradePoint.toString());
				scoreDomain.setFirstTotalScores(totalScores.toString());
			}
			
			return scoreDomain;
	}
}
