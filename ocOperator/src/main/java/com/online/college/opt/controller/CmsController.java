package com.online.college.opt.controller;

import com.online.college.common.web.SessionContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by RookieWangZhiWei on 2018/5/7.
 */
@Controller
@RequestMapping()
public class CmsController {


    @RequestMapping("/index")
    public ModelAndView index() {
        if (SessionContext.isLogin()) {
            ModelAndView mv = new ModelAndView("cms/index");
            mv.addObject("curNav", "home");
            return mv;
        } else {
            return new ModelAndView("auth/login");
        }
    }
}
