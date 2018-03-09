package com.aizhixin.cloud.dataanalysis.openup.service;

import com.aizhixin.cloud.dataanalysis.common.PageDomain;
import com.aizhixin.cloud.dataanalysis.common.core.ApiReturnConstants;
import com.aizhixin.cloud.dataanalysis.common.core.DataValidity;
import com.aizhixin.cloud.dataanalysis.common.core.PageUtil;
import com.aizhixin.cloud.dataanalysis.openup.domain.OpenUpInfoDomain;
import com.aizhixin.cloud.dataanalysis.openup.entity.OpenUpInfo;
import com.aizhixin.cloud.dataanalysis.openup.repository.OpenUpInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author xiagen
 * @date 2018/3/8 11:07
 * @description
 */
@Service
@Transactional
public class OpenUpInfoService {

    @Autowired
    private OpenUpInfoRepository openUpInfoRepository;

    /**
     * @author xiagen
     * @date 2018/3/8 11:21
     * @param [openUpInfoDomain]
     * @return com.aizhixin.cloud.dataanalysis.openup.entity.OpenUpInfo
     * @description 保存授权信息
     */
    public OpenUpInfo save(OpenUpInfoDomain openUpInfoDomain){
       OpenUpInfo openUpInfo=new OpenUpInfo();
       openUpInfo.setId(UUID.randomUUID().toString());
       openUpInfo.setOrgCode(openUpInfoDomain.getOrgCode());
       openUpInfo.setOrgId(openUpInfoDomain.getOrgId());
       return openUpInfoRepository.save(openUpInfo);
   }


   /**
    * @author xiagen
    * @date 2018/3/8 11:20
    * @param [ids]
    * @return void
    * @description 批量删除授权信息
    */
   public void  deleteAll(List<String> ids){
       List<OpenUpInfo> openUpInfos=new ArrayList<>();
       Iterable<OpenUpInfo> openUpInfoList=openUpInfoRepository.findAll(ids);
       if (null!=openUpInfoList){
          Iterator<OpenUpInfo> openUpInfoIterator= openUpInfoList.iterator();
         while (openUpInfoIterator.hasNext()){
             OpenUpInfo openUpInfo=openUpInfoIterator.next();
             openUpInfo.setDeleteFlag(DataValidity.INVALID.getIntValue());
             openUpInfos.add(openUpInfo);
         }
       }
       if (!openUpInfos.isEmpty()){
           openUpInfoRepository.save(openUpInfos);
       }
   }

   /**
    * @author xiagen
    * @date 2018/3/8 11:35
    * @param [pageNumber, pageSize, result]
    * @return java.util.Map<java.lang.String , java.lang.Object>
    * @description 分页查询授权信息
    */
   public Map<String,Object> findByOpenUpInfoAll(Integer pageNumber,Integer pageSize,Map<String,Object> result){
       Pageable page= PageUtil.createNoErrorPageRequest(pageNumber,pageSize);
       Page<OpenUpInfo> openUpInfoPage=openUpInfoRepository.findByDeleteFlagOrderByCreatedDateDesc(page,DataValidity.VALID.getIntValue());
       List<OpenUpInfo> openUpInfoList=openUpInfoPage.getContent();
       List<OpenUpInfoDomain> openUpInfoDomainList=new ArrayList<>();
       for (OpenUpInfo openUpInfo:openUpInfoList){
           OpenUpInfoDomain openUpInfoDomain=new OpenUpInfoDomain();
           openUpInfoDomain.setId(openUpInfo.getId());
           openUpInfoDomain.setOrgCode(openUpInfo.getOrgCode());
           openUpInfoDomain.setOrgId(openUpInfo.getOrgId());
           openUpInfoDomainList.add(openUpInfoDomain);
       }
       PageDomain pageDomain=new PageDomain();
       pageDomain.setPageNumber(page.getPageNumber());
       pageDomain.setPageSize(page.getPageSize());
       pageDomain.setTotalPages(openUpInfoPage.getTotalPages());
       pageDomain.setTotalElements(openUpInfoPage.getTotalElements());
       result.put(ApiReturnConstants.RESULT,Boolean.TRUE);
       result.put(ApiReturnConstants.DATA,openUpInfoDomainList);
       result.put(ApiReturnConstants.PAGE,pageDomain);
       return  result;
   }

   /**
    * @author xiagen
    * @date 2018/3/8 13:48
    * @param [orgCode]
    * @return com.aizhixin.cloud.dataanalysis.openup.domain.OpenUpInfoDomain
    * @description 根据学校代码查询授权信息
    */
   public OpenUpInfoDomain findByOrgCode(String orgCode){
        OpenUpInfo openUpInfo=openUpInfoRepository.findByOrgCodeAndDeleteFlag(orgCode,DataValidity.VALID.getIntValue());
        OpenUpInfoDomain openUpInfoDomain=new OpenUpInfoDomain();
        if (null!=openUpInfo){
            openUpInfoDomain.setOrgId(openUpInfo.getOrgId());
            openUpInfoDomain.setOrgCode(openUpInfo.getOrgCode());
        }
        return  openUpInfoDomain;
   }

}
