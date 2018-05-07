package com.online.college.opt.controller;

import com.online.college.common.util.EncryptUtil;
import com.online.college.common.web.JsonView;
import com.online.college.common.web.SessionContext;
import com.online.college.core.auth.domain.AuthUser;
import com.online.college.core.auth.service.IAuthUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by RookieWangZhiWei on 2018/5/4.
 */


@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthUserService iAuthUserService;


    @RequestMapping(value = "/register")
    public ModelAndView register() {
        if (SessionContext.isLogin()) {
            return new ModelAndView("redirect:/index.html");

        }
        return new ModelAndView("/auth/register");
    }

    @RequestMapping(value = "/doRegister")
    @ResponseBody
    public String doRegister(AuthUser authUser, String identiryCode, HttpServletRequest request) {

        if (identiryCode != null && !identiryCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request))) {
            return JsonView.render(2);
        }
        AuthUser tmpUser = iAuthUserService.getByUsername(authUser.getUsername());

        if (tmpUser != null) {
            return JsonView.render(1);
        } else {
            authUser.setPassword(EncryptUtil.encodedByMD5(authUser.getPassword()));
            iAuthUserService.createSelectivity(authUser);
            return JsonView.render(0);
        }


    }

    @RequestMapping(value = "/login")
    public ModelAndView login() {
        if (SessionContext.isLogin()) {
            return new ModelAndView("redirect:/index.html");
        }
        return new ModelAndView("auth/login");
    }

    @RequestMapping(value = "/ajaxlogin")
    @ResponseBody
    public String ajaxlogin(AuthUser user, String identiryCode, Integer rememberMe, HttpServletRequest request) {
        if (identiryCode != null && !identiryCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request))) {
            return JsonView.render(2, "验证码不正确");
        }
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), EncryptUtil.encodedByMD5(user.getPassword()));
        try {
            if (rememberMe != null && rememberMe == 1) {
                token.setRememberMe(true);
            }
            currentUser.login(token);
            return new JsonView().toString();
        } catch (AuthenticationException e) {
            return JsonView.render(1, "用户名或密码错误");
        }
    }

    @RequestMapping(value = "/doLogin")
    public ModelAndView doLogin(AuthUser user, String identiryCode, HttpServletRequest request) {
        if (SessionContext.getAuthUser() != null) {
            return new ModelAndView("redirect:/user/home.html");
        }
        if (identiryCode != null && !identiryCode.equalsIgnoreCase(SessionContext.getIdentifyCode(request))) {
            ModelAndView mv = new ModelAndView("auth/login");
            mv.addObject("errcode", 1);
            return mv;
        }

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), EncryptUtil.encodedByMD5(user.getPassword()));

        try {
            org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            return new ModelAndView("redirect:/user/home.html");
        } catch (AuthenticationException e) {
            ModelAndView mv = new ModelAndView("auth/login");
            mv.addObject("errcode", 2);
            return mv;
        }

    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request) {
        SessionContext.shiroLogout();
        return new ModelAndView("redirect:/index.html");
    }
}
