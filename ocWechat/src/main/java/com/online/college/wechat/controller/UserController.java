package com.online.college.wechat.controller;

import com.online.college.common.page.TailPage;
import com.online.college.common.web.SessionContext;
import com.online.college.core.user.domain.UserCourseSection;
import com.online.college.core.user.domain.UserCourseSectionDto;
import com.online.college.core.user.service.IUserCourseSectionService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author RookieWangZhiWei
 * @date 2018/5/28
 */
@Controller
@RequiresRoles("/user")
public class UserController {

    @Autowired
    private IUserCourseSectionService userCourseSectionService;

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, TailPage<UserCourseSectionDto> page){
        ModelAndView mv = new ModelAndView("user");

        Long userId = SessionContext.getWxUserId(request);
        if (null == userId){
            return new ModelAndView("redirect:/auth/login.html");
        }

        UserCourseSection queryEntity = new UserCourseSection();
        queryEntity.setUserId(userId);

        page = userCourseSectionService.queryPage(queryEntity,page);
        mv.addObject("page",page);

        mv.addObject("curUser",SessionContext.getWxAuthUser(request));

        return mv;
    }
}
