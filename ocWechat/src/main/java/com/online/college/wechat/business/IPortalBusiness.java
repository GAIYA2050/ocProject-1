package com.online.college.wechat.business;

import com.online.college.wechat.vo.CourseSectionVO;

import java.util.List;

/**
 * Created by RookieWangZhiWei on 2018/5/28.
 */
public interface IPortalBusiness {

    List<CourseSectionVO> queryCourseSection(Long courseId);

}
