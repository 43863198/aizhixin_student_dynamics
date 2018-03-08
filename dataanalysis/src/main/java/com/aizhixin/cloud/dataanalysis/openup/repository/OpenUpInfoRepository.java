package com.aizhixin.cloud.dataanalysis.openup.repository;

import com.aizhixin.cloud.dataanalysis.openup.entity.OpenUpInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OpenUpInfoRepository extends PagingAndSortingRepository<OpenUpInfo,String>{
    Page<OpenUpInfo> findByDeleteFlagOrderByCreatedDateDesc(Pageable page,Integer deleteFlag);
    OpenUpInfo findByOrgCodeAndDeleteFlag(String orgCode,Integer deleteFlag);
}
