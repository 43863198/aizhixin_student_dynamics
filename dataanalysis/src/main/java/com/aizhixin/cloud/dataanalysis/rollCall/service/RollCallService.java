package com.aizhixin.cloud.dataanalysis.rollCall.service;

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
import com.mongodb.AggregationOutput;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component
public class RollCallService {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 按条件查询考勤数据
	 * 
	 */
	public void findRollCallInfor(Long orgId,int times) {

		Calendar c = Calendar.getInstance();
		// 当前年份
		int year = c.get(Calendar.YEAR);

		String groupStr = "{$group:{_id:null,countNum:{$sum:1}}}";
		DBObject group = (DBObject) JSON.parse(groupStr);
		String matchStr = "{$match:{countNum :{$gt:"+times+"}}}";
		DBObject match = (DBObject) JSON.parse(matchStr);
		AggregationOutput output = mongoTemplate.getCollection("news")
				.aggregate(group, match);
	}

}
