package com.online.college.wechat.controller;

import com.online.college.common.storage.QiniuStorage;
import com.online.college.common.util.EncryptUtil;
import com.online.college.common.web.SessionContext;
import com.online.college.core.auth.domain.AuthUser;
import com.online.college.core.auth.service.IAuthUserService;
import com.online.college.wechat.wxapi.process.WxMemoryCacheClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by RookieWangZhiWei on 2018/5/28.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthUserService authUserService;


    @RequestMapping(value = "/login")
    public ModelAndView login(){

        return new ModelAndView("login");
    }


    @RequestMapping("/doLogin")
    public ModelAndView doLogin(AuthUser user, String toUrl, HttpServletRequest request){
        AuthUser tmpAuthUser = new AuthUser();

        tmpAuthUser.setUsername(user.getUsername());
        tmpAuthUser.setPassword(EncryptUtil.encodedByMD5(user.getPassword()));
        tmpAuthUser = authUserService.getByUsernameAndPassword(tmpAuthUser);
        if (null != tmpAuthUser){
            String openid = WxMemoryCacheClient.getOpenid(request.getSession().getId());

            if (StringUtils.isNotEmpty(openid)){
                tmpAuthUser.setOpenId(openid);
                authUserService.updateSelectivity(tmpAuthUser);
            }else {

            }

            String sessionId = request.getSession().getId();

            tmpAuthUser.setHeader(QiniuStorage.getUrl(tmpAuthUser.getHeader()));
            SessionContext.setAttribute(request,sessionId,tmpAuthUser);
            return new ModelAndView("redirect:/user/index.html");
        }

        ModelAndView mv= new ModelAndView("login");
        mv.addObject("errcode",1);
        return mv;
    }
}
