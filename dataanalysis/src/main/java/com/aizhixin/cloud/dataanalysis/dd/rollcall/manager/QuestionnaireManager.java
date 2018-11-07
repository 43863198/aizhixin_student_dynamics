package com.aizhixin.cloud.dataanalysis.dd.rollcall.manager;

import com.aizhixin.cloud.dataanalysis.dd.rollcall.vo.QuestionnaireScreenVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class QuestionnaireManager {
    @Value("${dl.dd.back.dbname}")
    private String ddDatabaseName;
    @Value("${dl.org.back.dbname}")
    private String orgDatabaseName;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<QuestionnaireScreenVO> findStudentQuestion(Long orgId, Long collegeId) {
        List<QuestionnaireScreenVO> r = new ArrayList<>();
        String sql = "SELECT q.ID  FROM #database#.dd_questionnaire q, #database#.dd_questionnaire_assgin a WHERE q.ORGAN_ID=? AND q.ID=a.QUESTIONNAIRE_ID AND q.DELETE_FLAG=0 AND q.`STATUS`=20  AND a.DELETE_FLAG = 0 AND a.`STATUS`=10 ORDER BY q.CREATED_DATE DESC LIMIT 1";//查询最新的已经分配的学生评价的问卷ID
        sql = sql.replaceAll("#database#", ddDatabaseName);
        if (null == orgId || orgId <= 0) {
            return r;
        }
        Long qid = jdbcTemplate.queryForObject(sql, new Object[] {orgId}, new int[] {Types.BIGINT}, Long.class);
        if (null == qid || qid <= 0) {
            return r;
        }
        if (null == collegeId || collegeId <= 0) {
            sql = "SELECT " +
                    "c.ID unitId," +
                    "c.`NAME` unitName," +
                    "COUNT(*) zs," +
                    "SUM(IF(s.`STATUS`=20,1,0)) pjs, " +
                    "ROUND(SUM(IF(s.`STATUS`=20,1,0))/COUNT(*), 4) cpl " +
                    "FROM #database#.dd_questionnaire_assgin_students s, #database#.dd_questionnaire_assgin a, #orgMnagerDB#.t_user u, #orgMnagerDB#.t_college c " +
                    "WHERE s.QUESTIONNAIRE_ASSGIN_ID=a.ID AND a.QUESTIONNAIRE_ID=? AND s.STUDENT_ID=u.ID AND u.COLLEGE_ID=c.ID " +
                    "GROUP BY c.ID, c.`NAME`";//分学院统计
            sql = sql.replaceAll("#orgMnagerDB#", orgDatabaseName);
            sql = sql.replaceAll("#database#", ddDatabaseName);

            log.info("Questional statistics Count sql:{}, questionnaireid:{}", sql, qid);
            r = jdbcTemplate.query(sql,
                    new Object[]{qid},
                    new int [] {Types.BIGINT},
                    (ResultSet rs, int rowNum) -> new QuestionnaireScreenVO(
                            rs.getLong("unitId"),rs.getString("unitName"),
                            rs.getInt("zs"), rs.getInt("pjs"),
                            rs.getDouble("cpl"))
            );
        } else {
            sql = "SELECT " +
                    "p.ID unitId," +
                    "p.`NAME` unitName," +
                    "COUNT(*) zs," +
                    "SUM(IF(s.`STATUS`=20,1,0)) pjs," +
                    "ROUND(SUM(IF(s.`STATUS`=20,1,0))/COUNT(*), 4) cpl " +
                    "FROM #database#.dd_questionnaire_assgin_students s, #database#.dd_questionnaire_assgin a, #orgMnagerDB#.t_user u, #orgMnagerDB#.t_professional p " +
                    "WHERE s.QUESTIONNAIRE_ASSGIN_ID=a.ID AND a.QUESTIONNAIRE_ID=? AND s.STUDENT_ID=u.ID AND u.PROFESSIONAL_ID=p.ID AND u.COLLEGE_ID=? " +
                    "GROUP BY p.ID, p.`NAME`";//限制学院，分专业统计

            sql = sql.replaceAll("#orgMnagerDB#", orgDatabaseName);
            sql = sql.replaceAll("#database#", ddDatabaseName);

            log.info("Questional statistics Count sql:{}, questionnaireid:{}, collegeId:{}", sql, qid, collegeId);
            r = jdbcTemplate.query(sql,
                    new Object[]{qid, collegeId},
                    new int [] {Types.BIGINT, Types.BIGINT},
                    (ResultSet rs, int rowNum) -> new QuestionnaireScreenVO(
                            rs.getLong("unitId"),rs.getString("unitName"),
                            rs.getInt("zs"), rs.getInt("pjs"),
                            rs.getDouble("cpl"))
            );
        }
        return r;
    }
}
