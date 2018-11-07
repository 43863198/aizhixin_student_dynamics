package com.aizhixin.cloud.dataanalysis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages="com.aizhixin.cloud.dataanalysis.em.online", mongoTemplateRef = EmMongoConfig.MONGO_TEMPLATE)
public class EmMongoConfig {
    protected static final String MONGO_TEMPLATE = "emMongoTemplate";
}
