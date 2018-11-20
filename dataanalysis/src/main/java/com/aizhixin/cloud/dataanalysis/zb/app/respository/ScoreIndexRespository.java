package com.aizhixin.cloud.dataanalysis.zb.app.respository;

import com.aizhixin.cloud.dataanalysis.zb.app.entity.ScoreIndex;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreAllYearIndexVO;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreAvgYearsVO;
import com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreDwCountVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreIndexRespository extends JpaRepository<ScoreIndex,Long>{

    @Query("SELECT new com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreDwCountVO(SUM(t.ckrs), SUM(t.bxbjgrc), SUM(t.kcs), SUM(t.cjzf)/SUM(t.ckrc), SUM(t.jdzf)/SUM(t.ckrc)) FROM #{#entityName} t WHERE  t.xxdm = :xxdm and t.xn = :xn AND t.xqm = :xqm AND t.pbh IS NULL")
    List<ScoreDwCountVO> findByXxdmSemsterCountIndex(@Param(value = "xxdm") String xxdm, @Param(value = "xn") String xn, @Param(value = "xqm") String xqm);

    List<ScoreIndex> findByXxdmAndXnAndXqmAndBh(String xxdm, String xn, String xqm, String bh);

    List<ScoreIndex> findByXxdmAndXnAndXqmAndPbhIsNull(String xxdm, String xn, String xqm);

    List<ScoreIndex> findByXxdmAndXnAndXqmAndPbh(String xxdm, String xn, String xqm, String pbh);

    @Query("SELECT new com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreAllYearIndexVO(t.xn, t.xqm, t.bh, SUM(t.ckrc), SUM(t.bxbjgrc),  SUM(t.cjzf)/SUM(t.ckrc), SUM(t.jdzf)/SUM(t.ckrc)) FROM #{#entityName} t WHERE  t.xxdm = :xxdm AND t.pbh IS NULL GROUP BY t.xn, t.xqm")
    List<ScoreAllYearIndexVO> findByXxdmAllSemesterCountIndex(@Param(value = "xxdm") String xxdm);

    @Query("SELECT new com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreAllYearIndexVO(t.xn, t.xqm, t.bh, t.ckrc, t.bxbjgrc,  t.cjzf/t.ckrc, t.jdzf/t.ckrc) FROM #{#entityName} t WHERE  t.xxdm = :xxdm AND t.bh = :bh ORDER BY t.xn, t.xqm")
    List<ScoreAllYearIndexVO> findByXxdmAndCollegeAllSemesterCountIndex(@Param(value = "xxdm") String xxdm, @Param(value = "bh")String bh);

    @Query("SELECT new com.aizhixin.cloud.dataanalysis.zb.app.vo.ScoreAvgYearsVO(t.xn, SUM(t.jdzf)/SUM(t.ckrc), SUM(t.cjzf)/SUM(t.ckrc)) FROM #{#entityName} t WHERE  t.xxdm = :xxdm GROUP BY t.xn ORDER BY t.xn DESC")
    Page<ScoreAvgYearsVO> findScoreYears(Pageable page, @Param(value = "xxdm") String xxdm);
}
