package com.ptu.zxk.controller.admin;

import com.ptu.zxk.entity.admin.Student;
import com.ptu.zxk.page.admin.Page;
import com.ptu.zxk.service.admin.StudentService;
import com.ptu.zxk.service.admin.SubjectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 考生管理后台控制器
 */
@RequestMapping("/admin/student")
@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private SubjectService subjectService;

    /**
     * 考生列表页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(ModelAndView model) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("offset", 0);
        queryMap.put("pageSize", 999999);
        model.addObject("subjectList", subjectService.findList(queryMap));
        model.setViewName("student/list");
        return model;
    }

    /**
     * 模糊搜索分页显示列表查询
     *
     * @param name
     * @param page
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> list(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "subjectId", required = false) Long subjectId,
            Page page
    ) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("name", name);
        if (subjectId != null) {
            queryMap.put("subjectId", subjectId);
        }
        queryMap.put("offset", page.getOffset());
        queryMap.put("pageSize", page.getRows());
        ret.put("rows", studentService.findList(queryMap));
        ret.put("total", studentService.getTotal(queryMap));
        System.out.println(ret);
        System.out.println("query" + queryMap);
        return ret;
    }


    /**
     * 添加考生
     *
     * @param student
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(Student student) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (student == null) {
            ret.put("type", "error");
            ret.put("msg", "请填写正确的考生信息!");
            return ret;
        }
        if (StringUtils.isEmpty(student.getName())) {
            ret.put("type", "error");
            ret.put("msg", "请填写考生用户名！");
            return ret;
        }
        if (StringUtils.isEmpty(student.getPassword())) {
            ret.put("type", "error");
            ret.put("msg", "请填写正确的考生密码！");
            return ret;
        }
        if (student.getSubjectId() == null) {
            ret.put("type", "error");
            ret.put("msg", "请选择考生所属学科专业");
            return ret;
        }
        student.setCreateTime(new Date());
//        添加之前判断登陆名是否存在
        if (isExistName(student.getName(), -1l)) {
            ret.put("type", "error");
            ret.put("msg", "该登陆账号已经存在");
            return ret;
        }
        if (studentService.add(student) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "添加失败，请联系管理员！");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "添加成功");
        return ret;

    }

    /**
     * 编辑考生
     *
     * @param student
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> edit(Student student) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (student == null) {
            ret.put("type", "error");
            ret.put("msg", "请填写正确的考生信息!");
            return ret;
        }
        if (StringUtils.isEmpty(student.getName())) {
            ret.put("type", "error");
            ret.put("msg", "请填写考生用户名！");
            return ret;
        }
        if (StringUtils.isEmpty(student.getPassword())) {
            ret.put("type", "error");
            ret.put("msg", "请填写正确的考生密码！");
            return ret;
        }
        if (student.getSubjectId() == null) {
            ret.put("type", "error");
            ret.put("msg", "请选择考生所属学科专业");
            return ret;
        }
//        编辑之前判断登陆名是否存在
        if (isExistName(student.getName(),student.getId())) {
            ret.put("type", "error");
            ret.put("msg", "该登陆账号已经存在");
            return ret;
        }
        if (studentService.edit(student) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "编辑失败，请联系管理员！");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "编辑成功");
        return ret;
    }

    /**
     * 删除考生
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(Long id) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (id == null) {
            ret.put("type", "error");
            ret.put("msg", "请选择要删除的数据");
            return ret;
        }
        try {
            if (studentService.delete(id) <= 0) {
                ret.put("type", "error");
                ret.put("msg", "删除失败，请联系管理员！");
                return ret;
            }
        } catch (Exception e) {
            ret.put("type", "error");
            ret.put("msg", "删除失败，该考生下存在考试信息，不能删除！");
            return ret;
        }

        ret.put("type", "success");
        ret.put("msg", "添加成功");
        return ret;
    }

    private boolean isExistName(String name, Long id) {
        Student studentServiceByName = studentService.findByName(name);
        if (studentServiceByName == null) {
            return false;
        }
        if (studentServiceByName.getId().longValue() == id.longValue()) {
            return false;
        }
        return true;
    }
}
