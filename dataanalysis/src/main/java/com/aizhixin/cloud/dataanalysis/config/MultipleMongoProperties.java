package com.aizhixin.cloud.dataanalysis.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mongo")
@NoArgsConstructor
@ToString
@Component
public class MultipleMongoProperties {
    @Getter @Setter private MongoProperties primary = new MongoProperties();
    @Getter @Setter private MongoProperties emMongoDb = new MongoProperties();
}
