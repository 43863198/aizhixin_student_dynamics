package com.aizhixin.cloud.dataanalysis.score.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScoreJob {

	
	
	@Scheduled(cron = "0 0/2 * * * ?")
	public void ScorefluctuateJob() {
		
		
		
	}
}
