package com.online.college.wechat.controller;

import com.online.college.common.page.TailPage;
import com.online.college.core.consts.CourseEnum;
import com.online.college.core.course.domain.Course;
import com.online.college.core.course.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author RookieWangZhiWei
 * @date 2018/5/28
 */
@Controller
@RequestMapping()
public class IndexController {


    @Autowired
    private ICourseService courseService;


    @RequestMapping("/index")
    public ModelAndView index(TailPage<Course> page){
        ModelAndView mv = new ModelAndView("index");

        Course queryEntity = new Course();

        queryEntity.setOnsale(CourseEnum.ONSALE.value());

        page.descSortField("weight");

        page = this.courseService.queryPage(queryEntity,page);

        mv.addObject("page",page);

        return mv;
    }




}
