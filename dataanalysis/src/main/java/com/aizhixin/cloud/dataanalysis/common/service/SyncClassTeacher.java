package com.aizhixin.cloud.dataanalysis.common.service;

import com.aizhixin.cloud.dataanalysis.feign.OrgManagerFeignService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-21
 */
@Service
@Transactional
public class SyncClassTeacher {
    @Autowired
    private OrgManagerFeignService orgManagerFeignService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void syncData(Long orgId){
        List<Object[]> classTeacherData = new ArrayList<>();
        Long[]  teacherIds = orgManagerFeignService.getTeacherIds(orgId);
        if(null!=teacherIds&&teacherIds.length>0){
            for(Long tid : teacherIds) {
                String classes = orgManagerFeignService.getClassesByTeacher(tid);
                if(null!=classes){
                    JSONArray jsonArray = JSONArray.fromObject(classes);
                    for(int i=0;i<jsonArray.length();i++){
                        Object[] data = new Object[3];
                        if(null!=jsonArray.getJSONObject(i).get("id")) {
                            data[0] = Long.valueOf(jsonArray.getJSONObject(i).get("id").toString());
                        }
                        if(null!=jsonArray.getJSONObject(i).get("name")) {
                            data[1] = jsonArray.getJSONObject(i).get("name").toString();
                        }
                        data[2] = tid;
                        classTeacherData.add(data);
                    }
                }
            }
        }
        final String dql = "delete from t_class_teacher ";
        final String sql = "insert into t_class_teacher(CLASSES_ID,CLASSES_NAME,TEACHER_ID) values(?,?,?)";
        int row = jdbcTemplate.update(dql);
        int[] rows = jdbcTemplate.batchUpdate(sql, classTeacherData);
    }

}
