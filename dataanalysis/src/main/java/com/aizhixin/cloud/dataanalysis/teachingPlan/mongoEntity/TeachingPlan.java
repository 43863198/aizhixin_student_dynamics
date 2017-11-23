package com.aizhixin.cloud.dataanalysis.teachingPlan.mongoEntity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="TeachingPlan")
@Data
public class TeachingPlan {

	@Id
	private String id;
	
	/**
	 * 机构id
	 */
	@Indexed
	private Long orgId;

	
	/**
	 * 专业id
	 */
	private Long professionalId;
	
	/**
	 * 专业名称
	 */
	private String professionalName;
	
	/**
	 * 学期
	 */
	private int semester;
	
	/**
	 * 最低必修学分要求
	 */
	private int minRequireScore;
	
	/**
	 * 最低限选学分要求
	 */
	private int minLimiteScore;
	
	/**
	 * 最低必修学分要求
	 */
	private int minOptionScore;
	
	/**
	 * 最低必修学分要求
	 */
	private int minTermScore;
	
	/**
	 * 最低必修学分要求
	 */
	private int minPracticeScore;
	
	/**
	 * 最低必修学分要求
	 */
	private int minSecondClassScore;
	
	/**
	 * 备注
	 */
	private String remarks;
	
}
