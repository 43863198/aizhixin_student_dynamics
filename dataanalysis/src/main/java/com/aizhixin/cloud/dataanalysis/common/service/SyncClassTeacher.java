package com.aizhixin.cloud.dataanalysis.common.service;

import com.aizhixin.cloud.dataanalysis.common.dto.ClassTeacherDTO;
import com.aizhixin.cloud.dataanalysis.feign.OrgManagerFeignService;
import com.aizhixin.cloud.dataanalysis.feign.vo.ClassVO;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-05-21
 */
@Service
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
                ClassVO classes = orgManagerFeignService.getClassesByTeacher(tid);
                if(null!=classes){
                    Object[] data = new Object[3];
                    data[0] = classes.getId();
                    data[1] = classes.getName();
                    data[2] = tid;
                    classTeacherData.add(data);
                }
            }
        }
        String sql = "insert into t_class_teacher(CLASSES_ID,CLASSES_NAME,TEACHER_ID) values(?,?,?)";
        int[] row = jdbcTemplate.batchUpdate(sql.toString(), classTeacherData);
    }




}
