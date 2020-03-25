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
 * ���������̨������
 */
@RequestMapping("/admin/student")
@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private SubjectService subjectService;

    /**
     * �����б�ҳ��
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
     * ģ��������ҳ��ʾ�б��ѯ
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
     * ��ӿ���
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
            ret.put("msg", "����д��ȷ�Ŀ�����Ϣ!");
            return ret;
        }
        if (StringUtils.isEmpty(student.getName())) {
            ret.put("type", "error");
            ret.put("msg", "����д�����û�����");
            return ret;
        }
        if (StringUtils.isEmpty(student.getPassword())) {
            ret.put("type", "error");
            ret.put("msg", "����д��ȷ�Ŀ������룡");
            return ret;
        }
        if (student.getSubjectId() == null) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ��������ѧ��רҵ");
            return ret;
        }
        student.setCreateTime(new Date());
//        ���֮ǰ�жϵ�½���Ƿ����
        if (isExistName(student.getName(), -1l)) {
            ret.put("type", "error");
            ret.put("msg", "�õ�½�˺��Ѿ�����");
            return ret;
        }
        if (studentService.add(student) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "���ʧ�ܣ�����ϵ����Ա��");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "��ӳɹ�");
        return ret;

    }

    /**
     * �༭����
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
            ret.put("msg", "����д��ȷ�Ŀ�����Ϣ!");
            return ret;
        }
        if (StringUtils.isEmpty(student.getName())) {
            ret.put("type", "error");
            ret.put("msg", "����д�����û�����");
            return ret;
        }
        if (StringUtils.isEmpty(student.getPassword())) {
            ret.put("type", "error");
            ret.put("msg", "����д��ȷ�Ŀ������룡");
            return ret;
        }
        if (student.getSubjectId() == null) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ��������ѧ��רҵ");
            return ret;
        }
//        �༭֮ǰ�жϵ�½���Ƿ����
        if (isExistName(student.getName(),student.getId())) {
            ret.put("type", "error");
            ret.put("msg", "�õ�½�˺��Ѿ�����");
            return ret;
        }
        if (studentService.edit(student) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "�༭ʧ�ܣ�����ϵ����Ա��");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "�༭�ɹ�");
        return ret;
    }

    /**
     * ɾ������
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
            ret.put("msg", "��ѡ��Ҫɾ��������");
            return ret;
        }
        try {
            if (studentService.delete(id) <= 0) {
                ret.put("type", "error");
                ret.put("msg", "ɾ��ʧ�ܣ�����ϵ����Ա��");
                return ret;
            }
        } catch (Exception e) {
            ret.put("type", "error");
            ret.put("msg", "ɾ��ʧ�ܣ��ÿ����´��ڿ�����Ϣ������ɾ����");
            return ret;
        }

        ret.put("type", "success");
        ret.put("msg", "��ӳɹ�");
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
