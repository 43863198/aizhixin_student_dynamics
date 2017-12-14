package com.aizhixin.cloud.dataanalysis.analysis.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-13
 */
@Component
public class CetStatisticsAnalysisJob {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private MongoTemplate mongoTemplate;





}
