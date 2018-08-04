package com.aizhixin.cloud.dataanalysis.zb.manager;

import com.aizhixin.cloud.dataanalysis.zb.dto.StudentScorezbDTO;
import com.aizhixin.cloud.dataanalysis.zb.dto.XnXqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Transactional
@Component
public class StudentScoreIndexManager {

    public static String SQL_DC_XN_XQ = "select DISTINCT c.XN, c.XQM FROM t_b_xscjxx c WHERE c.XXDM=?";

    public static String SQL_DC_COLLEGE = "SELECT c.XN, c.XQM, null AS P_BH, c.YXSH AS BH, COUNT(DISTINCT c.XH) AS CKRS, COUNT(c.XH) AS CKRC, SUM(c.JD) AS JDZF,COUNT(DISTINCT c.KCH) as KCS, SUM(c.BFCJ) as CJZF,  " +
            "SUM(if(c.KCLBM='1',1,0)) AS BXCKRC, " +
            "SUM(if(c.KCLBM='1',if(c.BFCJ >= 60, 1, 0),0 )) AS BXJGRC " +
            "FROM t_b_xscjxx c " +
            "WHERE c.XXDM = ?  and c.XN = ? and c.XQM = ?  " +
            "GROUP BY c.YXSH";

    public static String SQL_DC_PROFESSIONAL = "SELECT c.XN, c.XQM, c.YXSH AS P_BH, c.ZYH AS BH, COUNT(DISTINCT c.XH) AS CKRS, COUNT(c.XH) AS CKRC, SUM(c.JD) AS JDZF,COUNT(DISTINCT c.KCH) as KCS, SUM(c.BFCJ) as CJZF,  " +
            "SUM(if(c.KCLBM='1',1,0)) AS BXCKRC, " +
            "SUM(if(c.KCLBM='1',if(c.BFCJ >= 60, 1, 0),0 )) AS BXJGRC " +
            "FROM t_b_xscjxx c " +
            "WHERE c.XXDM = ?  and c.XN = ? and c.XQM = ?  " +
            "GROUP BY c.YXSH, c.ZYH";

    public static  String SQL_INSERT = "INSERT INTO T_ZB_XSCJ (XN,XQM,XXDM,P_BH,BH,CKRS,CKRC,BXCKRC,BXBJGRC,KCS,JDZF,CJZF) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<StudentScorezbDTO> queryDczb(String sql, String orgId, String xn, String xq) {
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId, xn, xq}, new int [] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}, new RowMapper<StudentScorezbDTO>() {
            public StudentScorezbDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new StudentScorezbDTO(rs.getString("XN"), rs.getString("XQM"),
                        rs.getString("P_BH"),rs.getString("BH"),
                        rs.getLong("CKRS"), rs.getLong("CKRC"),
                        rs.getLong("BXCKRC"), rs.getLong("BXJGRC"),
                        rs.getLong("KCS"), rs.getDouble("JDZF"), rs.getDouble("CJZF"));
            }
        });
    }
    @Transactional(readOnly = true)
    public List<XnXqDTO> queryStudentScoreXnXq(String sql, String orgId) {
        return jdbcTemplate.query(sql.toString(), new Object[]{orgId}, new int [] {Types.VARCHAR}, new RowMapper<XnXqDTO>() {
            public XnXqDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new XnXqDTO(rs.getString("XN"), rs.getString("XQM"));
            }
        });
    }


    public void saveStudentScoreIndex(final List<StudentScorezbDTO> list, String xxdm) {
        jdbcTemplate.batchUpdate(SQL_INSERT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                //(XN,XQM,XXDM,P_BH,BH,CKRS,CKRC,BXCKRC,BXBJGRC,KCS,JDZF,CJZF)
                StudentScorezbDTO d = list.get(i);
                preparedStatement.setString(1, d.getXn());//XN
                preparedStatement.setString(2, d.getXq());//XQ
                preparedStatement.setString(3, xxdm);//XXDM
                if (null != d.getPbh()) {
                    preparedStatement.setString(4, d.getPbh());//P_BH
                } else {
                    preparedStatement.setNull(4, Types.VARCHAR);
                }
                preparedStatement.setString(5, d.getBh());//BH
                preparedStatement.setLong(6, d.getCkrs());//CKRS
                preparedStatement.setLong(7, d.getCkrc());//CKRC
                preparedStatement.setLong(8, d.getBxckrc());//BXCKRC
                preparedStatement.setLong(9, d.getBxckrc() - d.getBxbjgrc());//BXBJGRC
                preparedStatement.setLong(10, d.getKcs());//KCS
                preparedStatement.setDouble(11, d.getJdzf());//JDZF
                preparedStatement.setDouble(12, d.getCjzf());//CJZF
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }
}
