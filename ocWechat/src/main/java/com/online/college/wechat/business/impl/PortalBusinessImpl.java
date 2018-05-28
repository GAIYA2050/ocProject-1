package com.online.college.wechat.business.impl;

import com.online.college.core.consts.CourseEnum;
import com.online.college.core.course.domain.CourseSection;
import com.online.college.core.course.service.ICourseSectionService;
import com.online.college.wechat.business.IPortalBusiness;
import com.online.college.wechat.vo.CourseSectionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by RookieWangZhiWei on 2018/5/28.
 */
@Service
public class PortalBusinessImpl implements IPortalBusiness {


    @Autowired
    private ICourseSectionService courseSectionService;

    @Override
    public List<CourseSectionVO> queryCourseSection(Long courseId) {
        List<CourseSectionVO> resultList=  new ArrayList<>();

        CourseSection queryEntity = new CourseSection();
        queryEntity.setId(courseId);

        queryEntity.setOnsale(CourseEnum.ONSALE.value());

        Map<Long,CourseSectionVO> tmpMap = new LinkedHashMap<>();

        Iterator<CourseSection> it = courseSectionService.queryAll(queryEntity).iterator();

        while (it.hasNext()){
            CourseSection item = it.next();
            if (Long.valueOf(0).equals(item.getParentId())){
                CourseSectionVO vo = new CourseSectionVO();
                BeanUtils.copyProperties(item,vo);

                tmpMap.put(vo.getId(),vo);

            }else{
                tmpMap.get(item.getParentId()).getSections().add(item);
            }
        }
        for (CourseSectionVO vo :tmpMap.values()){
            resultList.add(vo);
        }
        return resultList;
    }
}
