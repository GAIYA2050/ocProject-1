package com.online.college.opt.controller;

import com.online.college.common.page.TailPage;
import com.online.college.common.web.JsonView;
import com.online.college.core.consts.domain.ConstsClassify;
import com.online.college.core.consts.service.IConstsClassifyService;
import com.online.college.opt.business.IPortalBusiness;
import com.online.college.opt.vo.ConstsClassifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by RookieWangZhiWei on 2018/5/6.
 */
@Controller
@RequestMapping
public class ClassifyController {
    @Autowired
    private IConstsClassifyService constsClassifyService;

    @Autowired
    private IPortalBusiness portalBusiness;


    @RequestMapping(value = "/getById")
    @ResponseBody
    public String getById(Long id) {
        return JsonView.render(constsClassifyService.getById(id));
    }


    @RequestMapping(value = "/index")
    public ModelAndView classifyIndex(ConstsClassify queryEntity, TailPage<ConstsClassify> page) {

        ModelAndView mv = new ModelAndView("cms/classify/classifyIndex");
        mv.addObject("curNav", "classify");

        Map<String, ConstsClassifyVO> classifyMap = portalBusiness.queryAllClassifyMap();

        List<ConstsClassifyVO> classifysList = new ArrayList<ConstsClassifyVO>();

        for (ConstsClassifyVO vo :
                classifyMap.values()) {
            classifysList.add(vo);
        }
        mv.addObject("classifys", classifysList);

        ArrayList<ConstsClassify> subClassifys = new ArrayList<>();
        for (ConstsClassifyVO vo :
                classifyMap.values()) {
            subClassifys.addAll(vo.getSubClassifyList());
        }

        mv.addObject("subClassifys", subClassifys);

        return mv;
    }

    @RequestMapping(value = "/doMerge")
    @ResponseBody
    public String doMerge(ConstsClassify entity) {
        if (entity.getId() == null) {
            ConstsClassify tmpEntity = constsClassifyService.getByCode(entity.getCode());
            if (tmpEntity != null) {
                return JsonView.render(1, "此编码已存在");
            }
            constsClassifyService.createSelectivity(entity);
        } else {
            constsClassifyService.updateSelectivity(entity);
        }
        return new JsonView().toString();
    }


    @RequestMapping(value = "/deleteLogic")
    @ResponseBody
    public String deleteLogic(ConstsClassify entity) {
        constsClassifyService.deleteLogic(entity);
        return new JsonView().toString();
    }

}
