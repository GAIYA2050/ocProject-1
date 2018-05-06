package com.online.college.opt.controller;

import com.online.college.common.page.TailPage;
import com.online.college.common.util.CalendarUtil;
import com.online.college.common.util.JsonUtil;
import com.online.college.common.web.JsonView;
import com.online.college.core.auth.service.IAuthUserService;
import com.online.college.core.consts.domain.ConstsClassify;
import com.online.college.core.consts.service.IConstsClassifyService;
import com.online.college.core.course.domain.Course;
import com.online.college.core.course.service.ICourseService;
import com.online.college.core.statics.domain.CourseStudyStaticsDto;
import com.online.college.core.statics.domain.StaticsVO;
import com.online.college.core.statics.service.IStaticsService;
import com.online.college.opt.business.IPortalBusiness;
import com.online.college.opt.vo.ConstsClassifyVO;
import com.online.college.opt.vo.CourseSectionVO;
import com.qiniu.util.Json;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.ColumnInfoRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by RookieWangZhiWei on 2018/5/6.
 */
@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IPortalBusiness portalBusiness;

    @Autowired
    private IConstsClassifyService constsClassifyService;

    @Autowired
    private IAuthUserService authUserService;


    @Autowired
    private IStaticsService staticsService;

    @RequestMapping("/pageList")
    public ModelAndView list(Course queryEntity, TailPage<Course> page) {
        ModelAndView mv = new ModelAndView("cms/course/pageList");

        if (StringUtils.isNotEmpty(queryEntity.getName())) {
            queryEntity.setName(queryEntity.getName().trim());
        } else {
            queryEntity.setName(null);
        }

        page.setPageSize(5);
        page = courseService.queryPage(queryEntity, page);
        mv.addObject("page", page);
        mv.addObject("queryEntity", queryEntity);
        mv.addObject("curNav", "course");
        return mv;
    }

    @RequestMapping("/doSale")
    @ResponseBody
    public String doSale(Course entity){
        courseService.updateSelectivity(entity);
        return new JsonView().toString();
    }

    @RequestMapping("/doDelete")
    @ResponseBody
    public String doDelete(Course entity){
        courseService.delete(entity);
        return new JsonView().toString();
    }


    @RequestMapping("/getById")
    @ResponseBody
    public String getById(Long id){
        return JsonView.render(courseService.getById(id));
    }


    @RequestMapping("/read")
    public ModelAndView courseRead(Long id){
        Course course = courseService.getById(id);
        if (course == null){
            return new ModelAndView("error/404");

        }
        ModelAndView mv = new ModelAndView("cms/course/read");
        mv.addObject("curNav","course");
        mv.addObject("course",course);

        List<CourseSectionVO> chaptSections = this.portalBusiness.queryCourseSection(id);

        mv.addObject("chaptSections",chaptSections);


        Map<String ,ConstsClassifyVO> classifyMap = portalBusiness.queryAllClassifyMap();
        List<ConstsClassifyVO> classifysList = new ArrayList<>();
        for (ConstsClassifyVO vo :
                classifyMap.values()) {
            classifysList.add(vo);
        }
        mv.addObject("classifys",classifysList);
        
        
        List<ConstsClassify> subClassifys = new ArrayList<>();
        for (ConstsClassifyVO vo :
                classifyMap.values()) {
            subClassifys.addAll(vo.getSubClassifyList());
        }
        mv.addObject("subClassifys",subClassifys);


        CourseStudyStaticsDto staticsDto = new CourseStudyStaticsDto();
        staticsDto.setCourseId(course.getId());
        staticsDto.setEndDate(new Date());
        staticsDto.setStartDate(CalendarUtil.getPreNDay(new Date(),7));

        StaticsVO staticsVo = staticsService.queryCourseStudyStatistics(staticsDto);

        if (null != staticsVo){
            try {
                mv.addObject("staticsVo", JsonUtil.toJson(staticsVo));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return mv;
    }
}
