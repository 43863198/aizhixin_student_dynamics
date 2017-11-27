package com.aizhixin.cloud.dataanalysis.score.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aizhixin.cloud.dataanalysis.common.constant.WarningType;
import com.aizhixin.cloud.dataanalysis.score.domain.ScoreDomain;
import com.aizhixin.cloud.dataanalysis.score.service.ScoreService;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmRule;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmSettings;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmRuleService;
import com.aizhixin.cloud.dataanalysis.setup.service.AlarmSettingsService;

@Component
public class ScoreJob {

	@Autowired
	private ScoreService scoreService;
	@Autowired
	private AlarmRuleService alarmRuleService;
	@Autowired
	private AlarmSettingsService alarmSettingsService;

	/**
	 * 成绩波动预警定时任务
	 */
	@Scheduled(cron = "0 0/2 * * * ?")
	public void scoreFluctuateJob() {

		// 获取成绩波动预警配置
//		List<AlarmSettings> settingsList = alarmSettingsService
//				.getAlarmSettingsByType(WarningType.PerformanceFluctuation
//						.toString());
//		HashMap<Long, ArrayList<AlarmSettings>> alarmMap = new HashMap<Long, ArrayList<AlarmSettings>>();
//		
//		Set<String> warnRuleIdList = new  HashSet<String>();
//		// 按orgId归类告警等级阀值
//		for (AlarmSettings settings : settingsList) {
//			Long orgId = settings.getOrgId();
//			
//			String[] warnRuleIds = settings.getRuleSet().split(",");
//			for(String warnRuleId : warnRuleIds){
//				if(!StringUtils.isEmpty(warnRuleId)){
//					warnRuleIdList.add(warnRuleId);
//				}
//			}
//			if (null != alarmMap.get(orgId)) {
//				ArrayList<AlarmSettings> alarmList = alarmMap.get(orgId);
//				alarmList.add(settings);
//			} else {
//				ArrayList<AlarmSettings> alarmList = new ArrayList<AlarmSettings>();
//				alarmList.add(settings);
//				alarmMap.put(orgId, alarmList);
//			}
//		}
//		 List<AlarmRule> alarmList = alarmRuleService.getAlarmRuleByIds(warnRuleIdList);
//		Iterator iter = alarmMap.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry entry = (Map.Entry) iter.next();
//			Long key = (Long) entry.getKey();
//			ArrayList<AlarmSettings> val = (ArrayList<AlarmSettings>) entry
//					.getValue();
//			// 获取考试成绩
//			ArrayList<ScoreDomain> scoreDomainList = scoreService
//					.findScoreInfor(orgId);
//		}
		// 按学生计算平均绩点

		// 根据规则计算是否需要预警和预警的等级

		// 获取预警对应的处理设置配置生成预警处理初始信息

	}
}
