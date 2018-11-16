package com.aizhixin.cloud.dataanalysis.etl.coursetimetable.service;

import com.aizhixin.cloud.dataanalysis.etl.cet.dto.SchoolCalendarDTO;
import com.aizhixin.cloud.dataanalysis.etl.coursetimetable.manager.SchoolCalendarManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SchoolCalendarService {

    @Autowired
    private SchoolCalendarManager schoolCalendarManager;

    public void etlGuiliSchoolCalendar(Long orgId) {
        List<SchoolCalendarDTO> list = schoolCalendarManager.queryGuiLiCalendar();
        if (null == list) {
            log.info("SchoolCalendar ORG:{} not found any data.", orgId);
            return;
        }
        log.info("SchoolCalendar ORG:{}  query count:{}", orgId, list.size());

        if (list.size() > 0) {
            schoolCalendarManager.cleanCalendar();

            schoolCalendarManager.writeGuiliCalendar(list, orgId);
        }
    }
}
