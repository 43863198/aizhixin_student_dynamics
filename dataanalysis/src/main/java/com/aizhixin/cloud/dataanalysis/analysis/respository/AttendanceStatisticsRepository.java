package com.aizhixin.cloud.dataanalysis.analysis.respository;

import com.aizhixin.cloud.dataanalysis.analysis.entity.AttendanceStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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


    @Query("select kch,kcmc,jsgh,jsxm,kqjg from #{#entityName} where xxdm = :xxdm and xn = :xn and xqm = :xqm and kqrq BETWEEN :start and :end group by kcmc")
    List<AttendanceStatistics> findByXxdmAndXnAndXqmAndKqrqBetween(Long xxdm, String xn, String xqm, String start, String end);

    List<AttendanceStatistics> findByXxdmAndXnAndXqmAndKchAndKqrqBetween(Long xxdm, String xn, String xqm, String kch, String start, String end);

    List<AttendanceStatistics> findByXxdmAndXnAndXqmAndKcmcAndKqrqBetween(Long xxdm, String xn, String xqm, String kcmc, String start, String end);
}