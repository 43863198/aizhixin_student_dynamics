package com.aizhixin.cloud.dataanalysis.score.mongoRespository;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;



public interface ScoreMongoRespository extends MongoRepository<Score, String>{
 
	List<Score> findAllByTeachYearInAndOrgIdAndExamType(Set<Integer> teachYears,Long orgId,String examType);
	
	List<Score> findAllByGradeInAndOrgIdAndExamType(String[] grades,Long orgId,String examType);
	
	List<Score> findAllByTeachYearGreaterThanEqualAndOrgIdAndExamType(int beginYear,Long orgId,String examType);
	
	List<Score> findAllByTotalScoreLessThanAndTeachYearGreaterThanEqualAndExamTypeAndOrgId(float totalScore,String beginYear,String examType,Long orgId);

	List<Score> findAllByTotalScoreLessThanAndTeachYearAndSemesterAndOrgIdAndExamType(float totalScore,String teachYear,String semester,Long orgId,String examType);
	
	List<Score> findAllByGradePointAndTeachYearAndSemesterAndOrgIdAndExamType(float gradePoint,String teachYear,String semester,Long orgId,String examType);
	
	List<Score> findAllByTotalScoreGreaterThanEqualAndTeachYearAndSemesterAndOrgIdAndExamType(float totalScore,String teachYear,String semester,Long orgId,String examType);
}
