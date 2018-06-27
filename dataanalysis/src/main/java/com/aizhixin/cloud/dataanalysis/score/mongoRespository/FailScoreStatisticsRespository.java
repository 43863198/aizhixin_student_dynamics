package com.aizhixin.cloud.dataanalysis.score.mongoRespository;

import java.util.List;

import com.aizhixin.cloud.dataanalysis.score.mongoEntity.FailScoreStatistics;
import com.aizhixin.cloud.dataanalysis.score.mongoEntity.FirstTwoSemestersScoreStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FailScoreStatisticsRespository extends MongoRepository<FailScoreStatistics, String>{

	List<FailScoreStatistics> findAllByOrgIdAndTeachYearAndSemester(Long orgId,String teachYear, String semester);

	List<FailScoreStatistics> findAllByOrgId(Long orgId);
}
