package com.ptu.zxk.controller.admin;

import com.ptu.zxk.entity.admin.ExamPaper;
import com.ptu.zxk.page.admin.Page;
import com.ptu.zxk.service.admin.ExamPaperService;
import com.ptu.zxk.service.admin.ExamService;
import com.ptu.zxk.service.admin.StudentService;
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
 * �Ծ�����̨������
 */
@RequestMapping("/admin/examPaper")
@Controller
public class ExamPaperController {

    @Autowired
    private ExamPaperService examPaperService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ExamService examService;

    /**
     * �Ծ��б�ҳ��
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(ModelAndView model) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("offset", 0);
        queryMap.put("pageSize", 999999);
        model.addObject("examList", examService.findList(queryMap));
        model.addObject("studentList", studentService.findList(queryMap));
        model.setViewName("examPaper/list");
        return model;
    }

    /**
     * ģ��������ҳ��ʾ�б��ѯ
     *
     * @param examId
     * @param studentId
     * @param status
     * @param page
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> list(
            @RequestParam(name = "examId", required = false) Long examId,
            @RequestParam(name = "studentId", required = false) Long studentId,
            @RequestParam(name = "status", required = false) Integer status,
            Page page
    ) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (examId != null) {
            queryMap.put("examId", examId);
        }
        if (studentId != null) {
            queryMap.put("studentId", studentId);
        }
        if (status != null) {
            queryMap.put("status", status);
        }
        queryMap.put("offset", page.getOffset());
        queryMap.put("pageSize", page.getRows());
        ret.put("rows", examPaperService.findList(queryMap));
        ret.put("total", examPaperService.getTotal(queryMap));

        return ret;
    }


    /**
     * ����Ծ�
     *
     * @param examPaper
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(ExamPaper examPaper) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (examPaper == null) {
            ret.put("type", "error");
            ret.put("msg", "����д��ȷ���Ծ���Ϣ!");
            return ret;
        }
        if (examPaper.getExamId() == null) {
            ret.put("type", "error");
            ret.put("msg", "����д�Ծ��������ԣ�");
            return ret;
        }
        if (examPaper.getStudentId() == null) {
            ret.put("type", "error");
            ret.put("msg", "����д�Ծ�����ѧ����");
            return ret;
        }
        if (examPaperService.add(examPaper) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "���ʧ�ܣ�����ϵ����Ա��");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "��ӳɹ�");
        return ret;

    }

    /**
     * �༭�Ծ�
     *
     * @param examPaper
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> edit(ExamPaper examPaper) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (examPaper == null) {
            ret.put("type", "error");
            ret.put("msg", "����д��ȷ���Ծ���Ϣ!");
            return ret;
        }
        if (examPaper.getExamId() == null) {
            ret.put("type", "error");
            ret.put("msg", "����д�Ծ��������ԣ�");
            return ret;
        }
        if (examPaper.getStudentId() == null) {
            ret.put("type", "error");
            ret.put("msg", "����д�Ծ�����ѧ����");
            return ret;
        }
        if (examPaperService.edit(examPaper) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "�༭ʧ�ܣ�����ϵ����Ա��");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "�༭�ɹ�");
        return ret;
    }

    /**
     * ɾ���Ծ�
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
            if (examPaperService.delete(id) <= 0) {
                ret.put("type", "error");
                ret.put("msg", "ɾ��ʧ�ܣ�����ϵ����Ա��");
                return ret;
            }
        } catch (Exception e) {
            ret.put("type", "error");
            ret.put("msg", "ɾ��ʧ�ܣ����Ծ��´��ڴ�����Ϣ������ɾ����");
            return ret;
        }

        ret.put("type", "success");
        ret.put("msg", "��ӳɹ�");
        return ret;
    }

}
