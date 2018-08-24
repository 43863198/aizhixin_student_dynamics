package com.aizhixin.cloud.dataanalysis.analysis.service;

import com.aizhixin.cloud.dataanalysis.analysis.entity.MinorSecondDegreeInfo;
import com.aizhixin.cloud.dataanalysis.analysis.respository.MinorSecondDegreeRepository;
import com.aizhixin.cloud.dataanalysis.common.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class MinorSecondDegreeService {
    @Autowired
    private MinorSecondDegreeRepository minorSecondDegreeRepository;


    public List<MinorSecondDegreeInfo> findByXxdmAndXhAndXnAndXqm(Long xxdm, String xh) {
        List<MinorSecondDegreeInfo> result = new ArrayList<>();
        try {
            List<MinorSecondDegreeInfo> list = minorSecondDegreeRepository.findByXxdmAndXh(xxdm, xh);
            if (null != list && !list.isEmpty()) {
                MinorSecondDegreeInfo minorSecondDegreeInfo = list.get(0);
                result.add(minorSecondDegreeInfo);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public PageData<MinorSecondDegreeInfo> list(Long xxdm, String collegeCode, String professionCode, Integer pageNumber, Integer pageSize) {

        PageData<MinorSecondDegreeInfo> p = new PageData<>();
        Page<MinorSecondDegreeInfo> pageData;

        try {
            if (null == pageNumber || pageNumber < 1) {
                pageNumber = 1;
            }
            if (null == pageSize) {
                pageSize = 20;
            }

            MinorSecondDegreeInfo minorSecondDegreeInfo = new MinorSecondDegreeInfo();
            minorSecondDegreeInfo.setXxdm(xxdm);
            if (!StringUtils.isEmpty(collegeCode)) {
                minorSecondDegreeInfo.setYxsh(collegeCode);
            }
            if (!StringUtils.isEmpty(professionCode)) {
                minorSecondDegreeInfo.setZyh(professionCode);
            }

            Example<MinorSecondDegreeInfo> example = Example.of(minorSecondDegreeInfo);
            Pageable pageable = new PageRequest(pageNumber - 1, pageSize);
            pageData = minorSecondDegreeRepository.findAll(example, pageable);

            List<MinorSecondDegreeInfo> list = pageData.getContent();

            p.setData(list);
            p.getPage().setPageNumber(pageNumber);
            p.getPage().setPageSize(pageSize);
            p.getPage().setTotalElements(pageData.getTotalElements());
            p.getPage().setTotalPages(pageData.getTotalPages());
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageData<>();
        }
    }
}
