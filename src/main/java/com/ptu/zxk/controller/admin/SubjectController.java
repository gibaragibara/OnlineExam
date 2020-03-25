package com.ptu.zxk.controller.admin;

import com.ptu.zxk.entity.admin.Subject;
import com.ptu.zxk.page.admin.Page;
import com.ptu.zxk.service.admin.SubjectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * ѧ��רҵ������̨������
 */
@RequestMapping("/admin/subject")
@Controller
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    /**
     * ѧ��רҵ�б�ҳ��
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(ModelAndView model) {
        model.setViewName("subject/list");
        System.out.println(model);
        return model;
    }

    /**
     * ģ��������ҳ��ʾ�б���ѯ
     *
     * @param name
     * @param page
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> list(
            @RequestParam(name = "name", defaultValue = "") String name,
            Page page
    ) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("name", name);
        queryMap.put("offset", page.getOffset());
        queryMap.put("pageSize", page.getRows());
        ret.put("rows", subjectService.findList(queryMap));
        ret.put("total", subjectService.getTotal(queryMap));
        return ret;
    }


    /**
     * ����ѧ��רҵ
     *
     * @param subject
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(Subject subject) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (subject == null) {
            ret.put("type", "error");
            ret.put("msg", "����д��ȷ��ѧ����Ϣ!");
            return ret;
        }
        if (StringUtils.isEmpty(subject.getName())) {
            ret.put("type", "error");
            ret.put("msg", "����дѧ������!");
            return ret;
        }
        if (subjectService.add(subject) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "����ʧ�ܣ�����ϵ����Ա��");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "���ӳɹ�");
        return ret;

    }

    /**
     * �༭ѧ��רҵ
     *
     * @param subject
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> edit(Subject subject) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (subject == null) {
            ret.put("type", "error");
            ret.put("msg", "����д��ȷ��ѧ����Ϣ!");
            return ret;
        }
        if (StringUtils.isEmpty(subject.getName())) {
            ret.put("type", "error");
            ret.put("msg", "����дѧ������!");
            return ret;
        }
        if (subjectService.edit(subject) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "�༭ʧ�ܣ�����ϵ����Ա��");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "�༭�ɹ�");
        return ret;
    }

    /**
     * ɾ��ѧ��רҵ
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
            if (subjectService.delete(id) <= 0) {
                ret.put("type", "error");
                ret.put("msg", "ɾ��ʧ�ܣ�����ϵ����Ա��");
                return ret;
            }
        } catch (Exception e) {
            ret.put("type", "error");
            ret.put("msg", "ɾ��ʧ�ܣ���ѧ���´��ڿ�����Ϣ������ɾ����");
            return ret;
        }

        ret.put("type", "success");
        ret.put("msg", "���ӳɹ�");
        return ret;
    }

}