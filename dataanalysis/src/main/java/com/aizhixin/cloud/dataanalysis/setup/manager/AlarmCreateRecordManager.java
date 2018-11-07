package com.aizhixin.cloud.dataanalysis.setup.manager;

import com.aizhixin.cloud.dataanalysis.common.PageData;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.setup.entity.AlarmCreateRecord;
import com.aizhixin.cloud.dataanalysis.setup.respository.AlarmCreateRecordRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警信息接收人DAO对象
 */
@Component
@Transactional
public class AlarmCreateRecordManager {
    @Autowired
    private AlarmCreateRecordRespository alarmCreateRecordRespository;

    public AlarmCreateRecord save (AlarmCreateRecord entity) {
        return alarmCreateRecordRespository.save(entity);
    }

    public List<AlarmCreateRecord> save (List<AlarmCreateRecord> entitys) {
        return alarmCreateRecordRespository.save(entitys);
    }

    @Transactional(readOnly = true)
    public PageData<AlarmCreateRecord> findByOrgIdAndCreatedId(Long orgId, Long createId, Integer pageNo, Integer pageSize) {
        PageData<AlarmCreateRecord> page = new PageData<>();
        PageRequest p = PageUtil.createNoErrorPageRequest(pageNo, pageSize);

        page.setData(new ArrayList<>());
        page.getPage().setTotalPages(0);
        page.getPage().setTotalElements(0L);
        page.getPage().setPageSize(p.getPageSize());
        page.getPage().setPageNumber(p.getPageNumber() + 1);

        if (null == orgId || orgId <= 0) {
            return page;
        }
        Page<AlarmCreateRecord> pg = null;
        if (null == createId || createId <= 0) {
            pg = alarmCreateRecordRespository.findByOrgIdOrderByCreatedDateDesc(p, orgId);
        } else {
            pg = alarmCreateRecordRespository.findByOrgIdAndCreatedIdOrderByCreatedDateDesc(p, orgId, createId);
        }
        if (null != pg) {
            page.getPage().setTotalPages(pg.getTotalPages());
            page.getPage().setTotalElements(pg.getTotalElements());
            page.setData(pg.getContent());
        }
        return page;
    }
}
