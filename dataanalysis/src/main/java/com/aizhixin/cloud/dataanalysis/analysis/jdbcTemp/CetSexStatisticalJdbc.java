package com.aizhixin.cloud.dataanalysis.analysis.jdbcTemp;

import com.aizhixin.cloud.dataanalysis.analysis.domain.AgeSexStatisticalInfoDomain;
import com.aizhixin.cloud.dataanalysis.analysis.domain.CetSexStatisticalInfoDomain;
import com.aizhixin.cloud.dataanalysis.analysis.domain.NjStatisticalInfoDomain;
import com.aizhixin.cloud.dataanalysis.analysis.domain.ScoreScaleDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class CetSexStatisticalJdbc {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Logger logger= LoggerFactory.getLogger(CetSexStatisticalJdbc.class);

    public List<CetSexStatisticalInfoDomain> sexStatisticalInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType, final boolean isAll){
        String sql="SELECT ts.`xb`,COUNT(DISTINCT ts.`xh`) as total FROM `t_xsjbxx` AS ts  LEFT JOIN `t_cet_score` AS tcs ON ts.`xh`=tcs.`JOB_NUMBER` WHERE ts.`XXID`="+orgId+" AND  ts.`YBYNY`>CURRENT_TIMESTAMP AND ts.`RXNY`<CURRENT_TIMESTAMP";

        if (!isAll){
            if ("三级".equals(cetType)) {
                sql += " AND tcs.`SCORE`>=60 AND tcs.`TYPE` LIKE '%大学英语" + cetType + "%'";
            } else {
                sql += " AND tcs.`SCORE`>=425 AND tcs.`TYPE` LIKE '%大学英语" + cetType + "%'";
            }
        }else{
            //sql+=" AND tcs.`SCORE`>0 AND tcs.`TYPE` LIKE '%大学英语"+cetType+"%'";
            sql = "SELECT ts.xb,COUNT(DISTINCT ts.xh) as total FROM t_xsjbxx AS ts WHERE ts.XXID='" +orgId+"' AND  ts.YBYNY>CURRENT_TIMESTAMP AND ts.RXNY<CURRENT_TIMESTAMP AND ts.DQZT NOT IN ('02','04','16')";
        }

        if (!StringUtils.isEmpty(collegeCode)){
            sql+=" AND ts.`YXSH`='"+collegeCode+"'";
        }
        if (!StringUtils.isEmpty(professionCode)){
            sql+=" AND ts.`ZYH`='"+professionCode+"'";
        }
        if (!StringUtils.isEmpty(classCode)){
            sql+=" AND ts.`BJMC`='"+classCode+"'";
        }
        sql+="  GROUP BY ts.`xb`";

        logger.info("sql 语句>>>>>> "+sql);
        RowMapper<CetSexStatisticalInfoDomain>  rowMapper= (rs, rowNum) -> {
            CetSexStatisticalInfoDomain cetSexStatisticalInfoDomain=new CetSexStatisticalInfoDomain();
            cetSexStatisticalInfoDomain.setSex(rs.getString("xb"));
            if (isAll) {
                cetSexStatisticalInfoDomain.setAllTotal(rs.getLong("total"));
            }else{
                cetSexStatisticalInfoDomain.setPassTotal(rs.getLong("total"));
            }
            return cetSexStatisticalInfoDomain;
        };
      return  jdbcTemplate.query(sql,rowMapper);
    }


    public List<AgeSexStatisticalInfoDomain> ageStatisticalInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType, final boolean isAll){
        String sql="SELECT ts.`nl`,COUNT(DISTINCT ts.`xh`) as total FROM `t_xsjbxx` AS ts  LEFT JOIN `t_cet_score` AS tcs ON ts.`xh`=tcs.`JOB_NUMBER` WHERE ts.`XXID`="+orgId+" AND  ts.`YBYNY`>CURRENT_TIMESTAMP AND ts.`RXNY`<CURRENT_TIMESTAMP and ts.nl is not null";
        if (!StringUtils.isEmpty(collegeCode)){
            sql+=" AND ts.`YXSH`='"+collegeCode+"'";
        }
        if (!StringUtils.isEmpty(professionCode)){
            sql+=" AND ts.`ZYH`='"+professionCode+"'";
        }
        if (!StringUtils.isEmpty(classCode)){
            sql+=" AND ts.`BJMC`='"+classCode+"'";
        }
        if (!isAll){
            if ("三级".equals(cetType)) {
                sql += " AND tcs.`SCORE`>=60 AND tcs.`TYPE` LIKE '%大学英语" + cetType + "%'";
            } else {
                sql += " AND tcs.`SCORE`>=425 AND tcs.`TYPE` LIKE '%大学英语" + cetType + "%'";
            }
        }else{
            sql+=" AND tcs.`SCORE`>0 AND tcs.`TYPE` LIKE '%大学英语"+cetType+"%'";
        }
        sql+="  GROUP BY ts.`nl`";

        logger.info("sql 语句>>>>>> "+sql);
        RowMapper<AgeSexStatisticalInfoDomain>  rowMapper= (rs, rowNum) -> {
            AgeSexStatisticalInfoDomain ageSexStatisticalInfoDomain=new AgeSexStatisticalInfoDomain();
            ageSexStatisticalInfoDomain.setAge(rs.getInt("nl"));
            if (isAll) {
                ageSexStatisticalInfoDomain.setAllTotal(rs.getLong("total"));
            }else{
                ageSexStatisticalInfoDomain.setPassTotal(rs.getLong("total"));
            }
            return ageSexStatisticalInfoDomain;
        };
        return  jdbcTemplate.query(sql,rowMapper);
    }

    public List<NjStatisticalInfoDomain> njStatisticalInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType, final boolean isAll){
        String sql="SELECT ts.`nj`,COUNT(DISTINCT ts.`xh`) as total FROM `t_xsjbxx` AS ts  LEFT JOIN `t_cet_score` AS tcs ON ts.`xh`=tcs.`JOB_NUMBER` WHERE ts.`XXID`="+orgId+" AND  ts.`YBYNY`>CURRENT_TIMESTAMP AND ts.`RXNY`<CURRENT_TIMESTAMP";

        if (!isAll){
            if ("三级".equals(cetType)) {
                sql += " AND tcs.`SCORE`>=60 AND tcs.`TYPE` LIKE '%大学英语" + cetType + "%'";
            } else {
                sql += " AND tcs.`SCORE`>=425 AND tcs.`TYPE` LIKE '%大学英语" + cetType + "%'";
            }
        }else{
            //sql+=" AND tcs.`SCORE`>0 AND tcs.`TYPE` LIKE '%大学英语"+cetType+"%'";
            sql = "SELECT ts.`nj`,COUNT(DISTINCT ts.`xh`) as total FROM `t_xsjbxx` AS ts WHERE ts.`XXID`='" +orgId+"' AND  ts.`YBYNY`>CURRENT_TIMESTAMP AND ts.`RXNY`<CURRENT_TIMESTAMP AND ts.DQZT NOT IN ('02','04','16')";
        }

        if (!StringUtils.isEmpty(collegeCode)){
            sql+=" AND ts.`YXSH`='"+collegeCode+"'";
        }
        if (!StringUtils.isEmpty(professionCode)){
            sql+=" AND ts.`ZYH`='"+professionCode+"'";
        }
        if (!StringUtils.isEmpty(classCode)){
            sql+=" AND ts.`BJMC`='"+classCode+"'";
        }


        sql+="  GROUP BY ts.`nj`";

        logger.info("sql 语句>>>>>> "+sql);
        RowMapper<NjStatisticalInfoDomain>  rowMapper= (rs, rowNum) -> {
            NjStatisticalInfoDomain njStatisticalInfoDomain=new NjStatisticalInfoDomain();
            njStatisticalInfoDomain.setNj(rs.getString("nj"));
            if (isAll) {
                njStatisticalInfoDomain.setAllTotal(rs.getLong("total"));
            }else{
                njStatisticalInfoDomain.setPassTotal(rs.getLong("total"));
            }
            return njStatisticalInfoDomain;
        };
        return  jdbcTemplate.query(sql,rowMapper);
    }


    public ScoreScaleDomain scoreStatisticalInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType){
        String sql="SELECT COUNT(*) AS total FROM `t_xsjbxx` AS ts  LEFT JOIN `t_cet_score` AS tcs ON ts.`xh`=tcs.`JOB_NUMBER` WHERE ts.`XXID`=218 AND  ts.`YBYNY`>CURRENT_TIMESTAMP AND ts.`RXNY`<CURRENT_TIMESTAMP AND tcs.`ID` IS NULL";
        if (!StringUtils.isEmpty(collegeCode)){
            sql+=" AND ts.`YXSH`='"+collegeCode+"'";
        }
        if (!StringUtils.isEmpty(professionCode)){
            sql+=" AND ts.`ZYH`='"+professionCode+"'";
        }
        if (!StringUtils.isEmpty(classCode)){
            sql+=" AND ts.`BH`='"+classCode+"'";
        }
        sql+=" AND tcs.`TYPE` LIKE '%大学英语"+cetType+"%'";
        logger.info("sql 语句>>>>>> "+sql);
        RowMapper<ScoreScaleDomain>  rowMapper= (rs, rowNum) -> {
            ScoreScaleDomain scoreScaleDomain=new ScoreScaleDomain();
            scoreScaleDomain.setScoreScale("noExam");
            scoreScaleDomain.setTotal(rs.getLong("total"));
            return scoreScaleDomain;
        };
        List<ScoreScaleDomain> scoreScaleDomains=jdbcTemplate.query(sql,rowMapper);
        if (null!=scoreScaleDomains&&0<scoreScaleDomains.size()){
            return scoreScaleDomains.get(0);
        }else{
            ScoreScaleDomain scoreScaleDomain=new ScoreScaleDomain();
            scoreScaleDomain.setScoreScale("noExam");
            scoreScaleDomain.setTotal(0L);
            return scoreScaleDomain;
        }
    }

    public ScoreScaleDomain scoreScaleStatisticalInfo(Long orgId, String collegeCode, String professionCode, String classCode, String cetType,Integer startScore,Integer endScore){
        String sql="SELECT MAX(tcs.`SCORE`) AS total FROM `t_xsjbxx` AS ts  LEFT JOIN `t_cet_score` AS tcs ON ts.`xh`=tcs.`JOB_NUMBER` WHERE ts.`XXID`="+orgId+" AND  ts.`YBYNY`>CURRENT_TIMESTAMP AND ts.`RXNY`<CURRENT_TIMESTAMP AND tcs.`SCORE`>0";
        if (!StringUtils.isEmpty(collegeCode)){
            sql+=" AND ts.`YXSH`='"+collegeCode+"'";
        }
        if (!StringUtils.isEmpty(professionCode)){
            sql+=" AND ts.`ZYH`='"+professionCode+"'";
        }
        if (!StringUtils.isEmpty(classCode)){
            sql+=" AND ts.`BJMC`='"+classCode+"'";
        }
        sql+=" AND tcs.`TYPE` LIKE '%大学英语"+cetType+"%' and tcs.`SCORE`>0 GROUP BY ts.`xh`";

        String sql2="SELECT COUNT(1) AS t FROM ("+sql+") AS ss WHERE ss.total>="+startScore+" AND ss.total<= "+endScore;
        logger.info("sql 语句>>>>>> "+sql2);
        RowMapper<ScoreScaleDomain>  rowMapper= (rs, rowNum) -> {
            ScoreScaleDomain scoreScaleDomain=new ScoreScaleDomain();
            scoreScaleDomain.setScoreScale(startScore+"-"+endScore);
            scoreScaleDomain.setTotal(rs.getLong("t"));
            return scoreScaleDomain;
        };
        List<ScoreScaleDomain> scoreScaleDomains=jdbcTemplate.query(sql2,rowMapper);
        if (null!=scoreScaleDomains&&0<scoreScaleDomains.size()){
            return scoreScaleDomains.get(0);
        }else{
            ScoreScaleDomain scoreScaleDomain=new ScoreScaleDomain();
            scoreScaleDomain.setScoreScale("noExam");
            scoreScaleDomain.setTotal(0L);
            return scoreScaleDomain;
        }
    }



}
