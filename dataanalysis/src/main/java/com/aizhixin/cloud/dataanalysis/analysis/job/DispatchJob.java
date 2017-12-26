package com.aizhixin.cloud.dataanalysis.analysis.job;

import com.aizhixin.cloud.dataanalysis.analysis.constant.DataType;
import com.aizhixin.cloud.dataanalysis.common.constant.ScoreConstant;
import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DBCursor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2017-12-26
 */
@Component
public class DispatchJob {
    private Logger logger = Logger.getLogger(DispatchJob.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    //英语四级参加人数统计
    Criteria cet4 = Criteria.where("examType").is(ScoreConstant.EXAM_TYPE_CET4);
    Criteria cet6 = Criteria.where("examType").is(ScoreConstant.EXAM_TYPE_CET6);
    Criteria cet = new Criteria();
    Query query = new Query(cet.orOperator(cet4,cet6));
//    BasicDBObject keys = new BasicDBObject();
//    keys.put("orgId", true);
//    keys.put("schoolYear", true);
//    keys.put("semester", true);
//    DBCursor cursor = mongoTemplate.getCollection("Score").find(new BasicDBObject(), keys).addOption(Bytes.QUERYOPTION_NOTIMEOUT);



    public void schedulingTask() {
        EnumSet<DataType> type = EnumSet.allOf(DataType.class);
        for (DataType data : type) {
        }
    }

}
