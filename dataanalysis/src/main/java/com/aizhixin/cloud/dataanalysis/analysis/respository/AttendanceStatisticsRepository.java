package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.entity.AttendanceStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceStatisticsRepository extends JpaRepository<AttendanceStatistics, Integer> {

    Long countByXxdmAndXnAndXqmAndKqrqBetweenOrderByYxsh(Long xxdm, String xn, String xqm, String start, String end);

    Long countByXxdmAndXnAndXqmAndKqjgAndKqrqBetweenOrderByYxsh(Long xxdm, String xn, String xqm, String kqjg, String start, String end);

    Long countByXxdmAndYxshAndXnAndXqmAndKqrqBetweenOrderByZyh(Long xxdm, String yxsh, String xn, String xqm, String start, String end);

    Long countByXxdmAndYxshAndXnAndXqmAndKqjgAndKqrqBetweenOrderByZyh(Long xxdm, String yxsh, String xn, String xqm, String kqjg, String start, String end);

    Long countByXxdmAndYxshAndZyhAndXnAndXqmAndKqrqBetweenOrderByBjmc(Long xxdm, String yxsh, String zyh, String xn, String xqm, String start, String end);

    Long countByXxdmAndYxshAndZyhAndXnAndXqmAndKqjgAndKqrqBetweenOrderByBjmc(Long xxdm, String yxsh, String zyh, String xn, String xqm, String kqjg, String start, String end);

    Long countByXxdmAndYxshAndZyhAndBjmcAndXnAndXqmAndKqrqBetween(Long xxdm, String yxsh, String zyh, String xn, String bjmc, String xqm, String start, String end);

    Long countByXxdmAndYxshAndZyhAndBjmcAndXnAndXqmAndKqjgAndKqrqBetween(Long xxdm, String yxsh, String zyh, String bjmc, String xn, String xqm, String kqjg, String start, String end);


    @Query(value = "select ss from #{#entityName} ss where xxdm = :xxdm and xn = :xn and xqm = :xqm and kqrq BETWEEN :start and :end group by kcmc")
    Page<AttendanceStatistics> findByXxdmAndXnAndXqmAndKqrqBetweenGroupByKcmc(@Param(value = "xxdm")Long xxdm, @Param(value = "xn")String xn, @Param(value = "xqm")String xqm, @Param(value = "start")String start, @Param(value = "end")String end, Pageable pageable);

    @Query(value = "select ss from #{#entityName} ss where xxdm = :xxdm and xn = :xn and xqm = :xqm and kqrq BETWEEN :start and :end group by jsxm")
    Page<AttendanceStatistics> findByXxdmAndXnAndXqmAndKqrqBetweenGroupByJsxm(@Param(value = "xxdm")Long xxdm, @Param(value = "xn")String xn, @Param(value = "xqm")String xqm, @Param(value = "start")String start, @Param(value = "end")String end, Pageable pageable);

    @Query(value = "select ss from #{#entityName} ss where xxdm = :xxdm and xn = :xn and xqm = :xqm and kch = :kchorkcmc or kcmc = :kchorkcmc and kqrq BETWEEN :start and :end")
    List<AttendanceStatistics> findByXxdmAndXnAndXqmAndKchOrKcmcAndKqrqBetween(@Param(value = "xxdm")Long xxdm, @Param(value = "xn")String xn, @Param(value = "xqm")String xqm, @Param(value = "kchorkcmc")String kchorkcmc, @Param(value = "start")String start, @Param(value = "end")String end);

    @Query(value = "select ss from #{#entityName} ss where xxdm = :xxdm and xn = :xn and xqm = :xqm and jsgh = :jsghorjsmc or jsxm = :jsghorjsmc and kqrq BETWEEN :start and :end")
    List<AttendanceStatistics> findByXxdmAndXnAndXqmAndJsghOrJsxmAndKqrqBetween(@Param(value = "xxdm")Long xxdm, @Param(value = "xn")String xn, @Param(value = "xqm")String xqm, @Param(value = "jsghorjsmc")String jsghorjsmc, @Param(value = "start")String start, @Param(value = "end")String end);
}