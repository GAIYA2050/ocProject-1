package com.online.college.opt.controller;

import com.online.college.common.page.TailPage;
import com.online.college.common.web.JsonView;
import com.online.college.core.consts.domain.ConstsCollege;
import com.online.college.core.consts.service.IConstsCollegeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by RookieWangZhiWei on 2018/5/6.
 */
@Controller
@RequestMapping("/college")
public class CollegeController {

    @Autowired
    private IConstsCollegeService constsCollegeService;


    @RequestMapping(value = "/queryPageList")
    public ModelAndView queryPage(ConstsCollege queryEntity, TailPage<ConstsCollege> page) {
        ModelAndView mv = new ModelAndView("cms/college/collegePageList");
        mv.addObject("curNav", "college");
        if (StringUtils.isNotEmpty(queryEntity.getName())) {
            queryEntity.setName(queryEntity.getName().trim());
        } else {
            queryEntity.setName(null);
        }

        page = constsCollegeService.queryPage(queryEntity, page);
        mv.addObject("page", page);
        mv.addObject("queryEntity", queryEntity);
        return mv;
    }


    @RequestMapping(value = "/getById")
    @ResponseBody
    public String getById(Long id) {
        return JsonView.render(constsCollegeService.getById(id));
    }


    @RequestMapping(value = "/doMerge")
    @ResponseBody
    public String doMerge(ConstsCollege entity) {
        if (entity.getId() == null) {
            ConstsCollege tmpEntity = constsCollegeService.getByCode(entity.getCode());
            if (tmpEntity != null) {
                return JsonView.render(1, "此编码已存在");

            }
            constsCollegeService.createSelectivity(entity);
        }
        return new JsonView().toString();
    }


    @RequestMapping(value = "/deleteLogic")
    @ResponseBody
    public String deleteLogic(ConstsCollege entity) {
        constsCollegeService.deleteLogic(entity);
        return new JsonView().toString();
    }


}
