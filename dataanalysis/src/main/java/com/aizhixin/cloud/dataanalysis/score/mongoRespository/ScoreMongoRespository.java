package com.aizhixin.cloud.dataanalysis.score.mongoRespository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;



public interface ScoreMongoRespository extends MongoRepository<Score, String>{
 
	List<Score> findAllBySchoolYearInAndOrgIdAndExamType(int[] schoolYears,Long orgId,String examType);
	
	List<Score> findAllByGradeInAndOrgIdAndExamType(String[] grades,Long orgId,String examType);
	
	List<Score> findAllBySchoolYearGreaterThanEqualAndOrgIdAndExamType(int beginYear,Long orgId,String examType);
	
	List<Score> findAllByTotalScoreLessThanAndSchoolYearGreaterThanEqualAndExamTypeAndOrgId(float totalScore,int beginYear,String examType,Long orgId);
	
	List<Score> findAllByTotalScoreLessThanAndSchoolYearAndSemesterAndOrgIdAndExamType(float totalScore,int schoolYear,int semester,Long orgId,String examType);
	
	List<Score> findAllByTotalScoreGreaterThanEqualAndSchoolYearAndSemesterAndOrgIdAndExamType(float totalScore,int schoolYear,int semester,Long orgId,String examType);
}
