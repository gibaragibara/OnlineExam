package com.ptu.zxk.controller.admin;

import com.ptu.zxk.entity.admin.Exam;
import com.ptu.zxk.entity.admin.Question;
import com.ptu.zxk.page.admin.Page;
import com.ptu.zxk.service.admin.ExamService;
import com.ptu.zxk.service.admin.QuestionService;
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
 * ���Թ����̨������
 */
@RequestMapping("/admin/exam")
@Controller
public class ExamController {


    @Autowired
    private ExamService examService;
    @Autowired
    private QuestionService questionService;
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
        model.addObject("singleScore", Question.QUESTION_TYPE_SINGLE_SCORE);
        model.addObject("muiltScore",Question.QUESTION_TYPE_MUILT_SCORE);
        model.addObject("chargeScore",Question.QUESTION_TYPE_CHARGE_SCORE);
        model.setViewName("exam/list");
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
            @RequestParam(name = "startTime", required = false) String startTime,
            @RequestParam(name = "endTime", required = false) String endTime,
            Page page
    ) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("name", name);
        if (!StringUtils.isEmpty(startTime)) {
            queryMap.put("startTime", startTime);
        }
        if (subjectId != null) {
            queryMap.put("subjectId", subjectId);
        }
        if (!StringUtils.isEmpty(endTime)) {
            queryMap.put("endTime", endTime);
        }
        queryMap.put("offset", page.getOffset());
        queryMap.put("pageSize", page.getRows());
        ret.put("rows", examService.findList(queryMap));
        ret.put("total", examService.getTotal(queryMap));
        return ret;
    }


    /**
     * ��ӿ���
     *
     * @param exam
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(Exam exam) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (exam == null) {
            ret.put("type", "error");
            ret.put("msg", "����д��ȷ�Ŀ�����Ϣ!");
            return ret;
        }
        if (StringUtils.isEmpty(exam.getName())) {
            ret.put("type", "error");
            ret.put("msg", "����д�������ƣ�");
            return ret;
        }
        if (exam.getSubjectId() == null) {
            ret.put("type", "error");
            ret.put("msg", "����д���Կ�Ŀ��");
            return ret;
        }
        if (exam.getStartTime() == null) {
            ret.put("type", "error");
            ret.put("msg", "����д���Կ�ʼʱ�䣡");
            return ret;
        }
        if (exam.getEndTime() == null) {
            ret.put("type", "error");
            ret.put("msg", "����д���Խ���ʱ�䣡");
            return ret;
        }
        if (exam.getPassScore() == 0) {
            ret.put("type", "error");
            ret.put("msg", "����д���Լ������");
            return ret;
        }
        if (exam.getSingleQuestionNum() == 0 && exam.getMuiltQuestionNum() == 0 && exam.getChargeQuestionNum() == 0) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ�⡢��ѡ�⡢�ж���������һ�����͵���Ŀ");
            return ret;
        }

        exam.setCreateTime(new Date());
        //��ѯ����д�����������Ƿ�����
        Map<String, Long> queryMap = new HashMap<String, Long>();
        queryMap.put("questionType",Long.valueOf(Question.QUESTION_TYPE_SINGLE));
        queryMap.put("subjectId",exam.getSubjectId());
        int singleQuestionTotalNum = questionService.getQuestionByType(queryMap);//��ȡ��ѡ������
        if (exam.getSingleQuestionNum() > singleQuestionTotalNum) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ������������ⵥѡ�����������޸�");
            return ret;
        }
        queryMap.put("questionType",Long.valueOf(Question.QUESTION_TYPE_MUILT));
        int muiltQuestionTotalNum = questionService.getQuestionByType(queryMap);//��ȡ��ѡ������
        if (exam.getMuiltQuestionNum() > muiltQuestionTotalNum) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ��������������ѡ�����������޸�");
            return ret;
        }
        queryMap.put("questionType",Long.valueOf(Question.QUESTION_TYPE_CHARGE));
        int chargeQuestionTotalNum = questionService.getQuestionByType(queryMap);//��ȡ�ж�������
        if (exam.getChargeQuestionNum() > chargeQuestionTotalNum) {
            ret.put("type", "error");
            ret.put("msg", "�ж���������������ж������������޸�");
            return ret;
        }
        //������������
        exam.setQuestionNum(exam.getSingleQuestionNum() + exam.getMuiltQuestionNum() + exam.getChargeQuestionNum());
        //�����ܷ�
        exam.setTotalScore(exam.getSingleQuestionNum() * Question.QUESTION_TYPE_SINGLE_SCORE + exam.getMuiltQuestionNum() * Question.QUESTION_TYPE_MUILT_SCORE + exam.getChargeQuestionNum() * Question.QUESTION_TYPE_CHARGE_SCORE);
        if (examService.add(exam) <= 0) {
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
     * @param exam
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> edit(Exam exam) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (exam == null) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ����ȷ�Ŀ�����Ϣ!");
            return ret;
        }
        if (StringUtils.isEmpty(exam.getName())) {
            ret.put("type", "error");
            ret.put("msg", "����д�������ƣ�");
            return ret;
        }
        if (exam.getSubjectId() == null) {
            ret.put("type", "error");
            ret.put("msg", "����д���Կ�Ŀ��");
            return ret;
        }
        if (exam.getStartTime() == null) {
            ret.put("type", "error");
            ret.put("msg", "����д���Կ�ʼʱ�䣡");
            return ret;
        }
        if (exam.getEndTime() == null) {
            ret.put("type", "error");
            ret.put("msg", "����д���Խ���ʱ�䣡");
            return ret;
        }
        if (exam.getPassScore() == 0) {
            ret.put("type", "error");
            ret.put("msg", "����д���Լ������");
            return ret;
        }
        if (exam.getAvailableTime() == 0) {
            ret.put("type", "error");
            ret.put("msg", "����д��������ʱ��");
            return ret;
        }
        if (exam.getSingleQuestionNum() == 0 && exam.getMuiltQuestionNum() == 0 && exam.getChargeQuestionNum() == 0) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ�⡢��ѡ�⡢�ж���������һ�����͵���Ŀ");
            return ret;
        }

        //��ѯ����д�����������Ƿ�����
        Map<String, Long> queryMap = new HashMap<String, Long>();
        queryMap.put("questionType",Long.valueOf(Question.QUESTION_TYPE_SINGLE));
        queryMap.put("subjectId",exam.getSubjectId());
        int singleQuestionTotalNum = questionService.getQuestionByType(queryMap);//��ȡ��ѡ������
        if (exam.getSingleQuestionNum() > singleQuestionTotalNum) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ������������ⵥѡ�����������޸�");
            return ret;
        }
        queryMap.put("questionType",Long.valueOf(Question.QUESTION_TYPE_MUILT));
        int muiltQuestionTotalNum = questionService.getQuestionByType(queryMap);//��ȡ��ѡ������
        if (exam.getMuiltQuestionNum() > muiltQuestionTotalNum) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ��������������ѡ�����������޸�");
            return ret;
        }
        queryMap.put("questionType",Long.valueOf(Question.QUESTION_TYPE_CHARGE));
        int chargeQuestionTotalNum = questionService.getQuestionByType(queryMap);//��ȡ�ж�������
        if (exam.getChargeQuestionNum() > chargeQuestionTotalNum) {
            ret.put("type", "error");
            ret.put("msg", "�ж���������������ж������������޸�");
            return ret;
        }
        //������������
        exam.setQuestionNum(exam.getSingleQuestionNum() + exam.getMuiltQuestionNum() + exam.getChargeQuestionNum());
        //�����ܷ�
        exam.setTotalScore(exam.getSingleQuestionNum() * Question.QUESTION_TYPE_SINGLE_SCORE + exam.getMuiltQuestionNum() * Question.QUESTION_TYPE_MUILT_SCORE + exam.getChargeQuestionNum() * Question.QUESTION_TYPE_CHARGE_SCORE);

        if (examService.edit(exam) <= 0) {
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
            if (examService.delete(id) <= 0) {
                ret.put("type", "error");
                ret.put("msg", "ɾ��ʧ�ܣ�����ϵ����Ա��");
                return ret;
            }
        } catch (Exception e) {
            ret.put("type", "error");
            ret.put("msg", "ɾ��ʧ�ܣ��ÿ����´����Ծ���Ϣ���Լ�¼������ɾ����");
            return ret;
        }

        ret.put("type", "success");
        ret.put("msg", "��ӳɹ�");
        return ret;
    }

}
