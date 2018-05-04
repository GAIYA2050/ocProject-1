package com.online.college.portal.controller;

import com.online.college.core.auth.domain.AuthUser;
import com.online.college.core.auth.service.IAuthUserService;
import com.online.college.core.consts.CourseEnum;
import com.online.college.core.consts.domain.ConstsSiteCarousel;
import com.online.college.core.consts.service.IConstsSiteCarouselService;
import com.online.college.core.course.domain.Course;
import com.online.college.core.course.domain.CourseQueryDto;
import com.online.college.core.course.service.ICourseService;
import com.online.college.portal.business.IPortalBusiness;
import com.online.college.portal.vo.ConstsClassifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by RookieWangZhiWei on 2018/5/4.
 */
@Controller
@RequestMapping
public class PortalController {

    @Autowired
    private IPortalBusiness portalBusiness;

    @Autowired
    private IConstsSiteCarouselService siteCarouselService;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IAuthUserService authUserService;


    /**
     * 首页
     */
    @RequestMapping("/index")
    public ModelAndView index() {

        ModelAndView mv = new ModelAndView("index");
        List<ConstsSiteCarousel> carouselList = siteCarouselService.queryCarousels(4);
        mv.addObject("carouselList", carouselList);

        List<ConstsClassifyVO> classifys = portalBusiness.queryAllClassify();

        portalBusiness.prepareRecomdCourses(classifys);
        mv.addObject("classifys", classifys);


        CourseQueryDto queryEntity = new CourseQueryDto();
        queryEntity.setCount(5);
        queryEntity.setFree(CourseEnum.FREE_NOT.value());
        queryEntity.descSortField("weight");
        List<Course> actionCourseList = this.courseService.queryList(queryEntity);
        mv.addObject("actionCourseList", actionCourseList);

        queryEntity.setFree(CourseEnum.FREE.value());
        List<Course> freeCourseList = this.courseService.queryList(queryEntity);
        mv.addObject("freeCourseList", freeCourseList);


        queryEntity.setCount(7);
        queryEntity.setFree(null);
        queryEntity.setSubClassify("java");
        queryEntity.descSortField("studyCount");
        List<Course> javaCourseList = this.courseService.queryList(queryEntity);
        mv.addObject("javaCourseList", javaCourseList);

        List<AuthUser> recomdTeacherList = authUserService.queryRecomd();
        mv.addObject("recomdTeacherList", recomdTeacherList);


        return mv;
    }
}
