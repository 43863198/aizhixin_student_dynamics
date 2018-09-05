package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.entity.AttendanceStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AttendanceStatisticsRepository extends JpaRepository<AttendanceStatistics, Integer> {

    Long countByXxdmAndKqrqBetweenOrderByYxsh(Long xxdm, String start, String end);

    Long countByXxdmAndKqjgAndKqrqBetweenOrderByYxsh(Long xxdm, String kqjg, String start, String end);

    Long countByXxdmAndYxshAndKqrqBetweenOrderByZyh(Long xxdm, String yxsh, String start, String end);

    Long countByXxdmAndYxshAndKqjgAndKqrqBetweenOrderByZyh(Long xxdm, String yxsh, String kqjg, String start, String end);

    Long countByXxdmAndYxshAndZyhAndKqrqBetweenOrderByBjmc(Long xxdm, String yxsh, String zyh, String start, String end);

    Long countByXxdmAndYxshAndZyhAndKqjgAndKqrqBetweenOrderByBjmc(Long xxdm, String yxsh, String zyh, String kqjg, String start, String end);

    Long countByXxdmAndYxshAndZyhAndBjmcAndKqrqBetween(Long xxdm, String yxsh, String zyh, String bjmc, String start, String end);

    Long countByXxdmAndYxshAndZyhAndBjmcAndKqjgAndKqrqBetween(Long xxdm, String yxsh, String zyh, String bjmc, String kqjg, String start, String end);


    @Query(value = "select ss from #{#entityName} ss where xxdm = :xxdm and kqrq BETWEEN :start and :end group by kcmc")
    Page<AttendanceStatistics> findByXxdmAndKqrqBetweenGroupByKcmc(@Param(value = "xxdm") Long xxdm, @Param(value = "start") String start, @Param(value = "end") String end, Pageable pageable);

    @Query(value = "select ss from #{#entityName} ss where xxdm = :xxdm and kqrq BETWEEN :start and :end group by jsxm")
    Page<AttendanceStatistics> findByXxdmAndKqrqBetweenGroupByJsxm(@Param(value = "xxdm") Long xxdm, @Param(value = "start") String start, @Param(value = "end") String end, Pageable pageable);

    @Query(value = "select ss from #{#entityName} ss where xxdm = :xxdm and kch = :kchorkcmc or kcmc = :kchorkcmc and kqrq BETWEEN :start and :end")
    List<AttendanceStatistics> findByXxdmAndKchOrKcmcAndKqrqBetween(@Param(value = "xxdm") Long xxdm, @Param(value = "kchorkcmc") String kchorkcmc, @Param(value = "start") String start, @Param(value = "end") String end);

    @Query(value = "select ss from #{#entityName} ss where xxdm = :xxdm and jsgh = :jsghorjsmc or jsxm = :jsghorjsmc and kqrq BETWEEN :start and :end")
    List<AttendanceStatistics> findByXxdmAndJsghOrJsxmAndKqrqBetween(@Param(value = "xxdm") Long xxdm, @Param(value = "jsghorjsmc") String jsghorjsmc, @Param(value = "start") String start, @Param(value = "end") String end);

    @Query(value = "select ss from #{#entityName} ss where xxdm = :xxdm and kqrq BETWEEN :start and :end group by kcmc")
    List<AttendanceStatistics> findByXxdmAndKqrqBetweenGroupByKcmcBfy(@Param(value = "xxdm") Long xxdm, @Param(value = "start") String start, @Param(value = "end") String end);

    @Query(value = "select ss from #{#entityName} ss where xxdm = :xxdm and kqrq BETWEEN :start and :end group by jsxm")
    List<AttendanceStatistics> findByXxdmAndKqrqBetweenGroupByJsxmBfy(@Param(value = "xxdm") Long xxdm, @Param(value = "start") String start, @Param(value = "end") String end);


    @Modifying
    @Transactional
    @Query(value = "delete from #{#entityName} where xxdm=:xxdm and yxsh=:yxsh")
    void deleteByXxdmAndYxsh(@Param(value = "xxdm") Long xxdm, @Param("yxsh") String yxsh);

    @Modifying
    @Transactional
    @Query(value = "delete from #{#entityName} where xxdm=:xxdm and yxsh=:yxsh and zyh=:zyh")
    void deleteByXxdmAndYxshAndZyh(@Param(value = "xxdm") Long xxdm, @Param("yxsh") String yxsh, @Param("zyh") String zyh);

    @Modifying
    @Transactional
    @Query(value = "delete from #{#entityName} where xxdm=:xxdm and yxsh=:yxsh and zyh=:zyh and bjmc=:bjmc")
    void deleteByXxdmAndYxshAndZyhAndBjmc(@Param(value = "xxdm") Long xxdm, @Param("yxsh") String yxsh, @Param("zyh") String zyh,@Param("bjmc") String bjmc);
}