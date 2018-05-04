package com.online.college.portal.controller;

import com.online.college.common.web.SessionContext;
import com.online.college.core.auth.domain.AuthUser;
import com.online.college.core.auth.service.IAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by RookieWangZhiWei on 2018/5/4.
 */


@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthUserService iAuthUserService;


    @RequestMapping(value = "/register")
    public ModelAndView register(){
        if(SessionContext.isLogin()){
            return new ModelAndView("redirect:/index.html");

        }
        return new ModelAndView("/auth/register");
    }

    @RequestMapping(value = "/doRegister")
    @ResponseBody
    public String doRegister(AuthUser authUser, String identiryCode, HttpServletRequest request){

        return null;

    }
}
