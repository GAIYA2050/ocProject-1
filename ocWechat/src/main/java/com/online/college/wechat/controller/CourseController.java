package com.online.college.wechat.controller;

import com.online.college.common.page.TailPage;
import com.online.college.common.web.SessionContext;
import com.online.college.core.course.domain.Course;
import com.online.college.core.course.domain.CourseComment;
import com.online.college.core.course.domain.CourseSection;
import com.online.college.core.course.service.ICourseCommentService;
import com.online.college.core.course.service.ICourseSectionService;
import com.online.college.core.course.service.ICourseService;
import com.online.college.core.user.domain.UserCourseSection;
import com.online.college.core.user.service.IUserCourseSectionService;
import com.online.college.wechat.business.IPortalBusiness;
import com.online.college.wechat.vo.CourseSectionVO;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 *
 * @author RookieWangZhiWei
 * @date 2018/5/28
 */
@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IPortalBusiness portalBusiness;


    @Autowired
    private ICourseCommentService courseCommentService;

    @Autowired
    private ICourseSectionService courseSectionService;

    @Autowired
    private IUserCourseSectionService userCourseSectionService;

    @RequestMapping(value = "/read")
    public ModelAndView read(Long id){
        ModelAndView mv = new ModelAndView("read");


        Course course = courseService.getById(id);

        if (null == course){
            return new ModelAndView("error/404");
        }
        mv.addObject("course",course);

        List<CourseSectionVO> chaptSections = this.portalBusiness.queryCourseSection(id);

        mv.addObject("chaptSections",chaptSections);

        return mv;
    }

    @RequestMapping(value = "/video")
    public ModelAndView video(HttpServletRequest request,Long id){
        ModelAndView mv = new ModelAndView("video");

        CourseSection courseSection = courseSectionService.getById(id);

        if (null == courseSection){
            return new ModelAndView("error/404");
        }
        mv.addObject("courseSection",courseSection);

        Long userId = SessionContext.getWxUserId(request);

        if (null != userId){
            UserCourseSection userCourseSection = new UserCourseSection();

            userCourseSection.setUserId(userId);
            userCourseSection.setCourseId(courseSection.getCourseId());

            userCourseSection.setSectionId(courseSection.getId());

            UserCourseSection result = userCourseSectionService.queryLatest(userCourseSection);

            if (null == result){
                userCourseSection.setCreateTime(new Date());
                userCourseSection.setCreateUser(SessionContext.getWxUsername(request));
                userCourseSection.setUpdateTime(new Date());
                userCourseSection.setUpdateUser(SessionContext.getWxUsername(request));
            }else{
                result.setUpdateTime(new Date());
                userCourseSectionService.update(result);
            }

        }

        return mv;
    }


    @RequestMapping("/comment")
    public ModelAndView comment(CourseComment queryEntity, TailPage<CourseComment> page){
        ModelAndView mv = new ModelAndView("comment");
        TailPage<CourseComment> commentPage = this.courseCommentService.queryPage(queryEntity, page);
        mv.addObject("page",page);

        return mv;
    }


}
