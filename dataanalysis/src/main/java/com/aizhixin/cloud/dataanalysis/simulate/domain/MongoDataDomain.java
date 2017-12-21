/**
 * 
 */
package com.aizhixin.cloud.dataanalysis.simulate.domain;


import java.util.List;
import lombok.Data;
import lombok.ToString;




@ToString
@Data
public class MongoDataDomain {
	
	private List<String> jsonList;
	
	private String objectName;
}
