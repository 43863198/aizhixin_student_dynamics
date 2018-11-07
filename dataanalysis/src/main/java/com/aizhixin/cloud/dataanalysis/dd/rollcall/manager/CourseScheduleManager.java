package com.aizhixin.cloud.dataanalysis.dd.rollcall.manager;

import com.aizhixin.cloud.dataanalysis.dd.rollcall.dto.PeriodDTO;
import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.ClassesCourseCountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CourseScheduleManager {

    @Value("${dl.dd.back.dbname}")
    private String ddDatabaseName;

    @Value("${dl.org.back.dbname}")
    private String orgDatabaseName;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ClassesCourseCountVO> findAllPeroidClassesCourseCountByOrgIdAndDay(String dayStr, Long orgId, Long collegeId, List<PeriodDTO> periodDTOList) {
        List<ClassesCourseCountVO> list = new ArrayList<>();
        if (null != collegeId && collegeId > 0) {
            String sql = "SELECT  COUNT(s.ID) FROM #database#.dd_schedule s LEFT JOIN #orgMnagerDB#.t_user u ON s.TEACHER_ID=u.ID AND u.COLLEGE_ID=? " +
                    "WHERE ORGAN_ID=? AND TEACH_DATE=? AND ? BETWEEN PERIOD_NO AND (PERIOD_NO + PERIOD_NUM - 1)";
            sql = sql.replaceAll("#database#", ddDatabaseName);
            sql = sql.replaceAll("#orgMnagerDB#", orgDatabaseName);
            log.info("ClasesCourse count query params: orgId:{}, collegeId:{}, day:{},native SQL:{}", orgId, collegeId, dayStr, sql);
            if (null != periodDTOList) {
                for (PeriodDTO p : periodDTOList) {
                    int pc = jdbcTemplate.queryForObject(sql, new Object[]{collegeId, orgId, dayStr, p.getNo()}, new int[]{Types.BIGINT, Types.VARCHAR, Types.INTEGER}, Integer.class);
                    list.add(new ClassesCourseCountVO(p.getNo(), pc));
                }
            }
        } else {
            String sql = "SELECT  COUNT(*) FROM #database#.dd_schedule WHERE ORGAN_ID=? AND TEACH_DATE=? AND ? BETWEEN PERIOD_NO AND (PERIOD_NO + PERIOD_NUM - 1)".replaceAll("#database#", ddDatabaseName);
            log.info("ClasesCourse count query params: orgId:{}, day:{},native SQL:{}", orgId, dayStr, sql);
            if (null != periodDTOList) {
                for (PeriodDTO p : periodDTOList) {
                    int pc = jdbcTemplate.queryForObject(sql, new Object[]{orgId, dayStr, p.getNo()}, new int[]{Types.BIGINT, Types.VARCHAR, Types.INTEGER}, Integer.class);
                    list.add(new ClassesCourseCountVO(p.getNo(), pc));
                }
            }
        }
        return list;
    }
}
