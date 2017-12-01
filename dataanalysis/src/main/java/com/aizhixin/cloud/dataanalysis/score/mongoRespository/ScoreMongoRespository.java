package com.aizhixin.cloud.dataanalysis.score.mongoRespository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.Score;



public interface ScoreMongoRespository extends MongoRepository<Score, String>{
 
	List<Score> findAllBySchoolYearInAndOrgId(int[] schoolYears,Long orgId);
	
	List<Score> findAllByGradeInAndOrgIdAndExamType(String[] grades,Long orgId,String examType);
	
	List<Score> findAllBySchoolYearGreaterThanEqualAndOrgId(int beginYear,Long orgId);
	
	List<Score> findAllByTotalScoreLessThanAndSchoolYearAndSemesterAndOrgIdAndExamType(String totalScore,int schoolYear,int semester,Long orgId,String examType);
	
	List<Score> findAllByTotalScoreGreaterThanEqualAndSchoolYearAndSemesterAndOrgIdAndExamType(String totalScore,int schoolYear,int semester,Long orgId,String examType);
}
