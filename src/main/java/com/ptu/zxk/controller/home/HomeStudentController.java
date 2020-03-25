package com.ptu.zxk.controller.home;

import com.ptu.zxk.entity.admin.*;
import com.ptu.zxk.service.admin.*;
import com.ptu.zxk.util.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/home/user")
public class HomeStudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ExamService examService;
    @Autowired
    private ExamPaperService examPaperService;

    @Autowired
    private ExamPaperAnswerService examPaperAnswerService;

    private static int PAGESIZE = 10;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView model) {
        model.addObject("title", "��������");
        model.setViewName("home/user/index");
        return model;
    }

    /**
     * �������Ļ�ӭҳ��
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public ModelAndView welcome(ModelAndView model, HttpServletRequest request) {
        model.addObject("title", "�������Ļ�ӭҳ��");
        Student student = (Student) request.getSession().getAttribute("student");
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("startTime", DateFormatUtil.getDate("yyyy-MM-dd hh:mm:ss", new Date()));
        queryMap.put("endTime", DateFormatUtil.getDate("yyyy-MM-dd hh:mm:ss", new Date()));
        queryMap.put("subjectId", student.getSubjectId());
        queryMap.put("offset", 0);
        queryMap.put("pageSize", 10);
        queryMap.put("studentId", student.getId());
        model.addObject("examList", examService.findListByUser(queryMap));
        model.addObject("subject", subjectService.findById(student.getSubjectId()));
        model.addObject("historyList", examPaperService.findHistory(queryMap));
        model.setViewName("/home/user/welcome");
        return model;
    }

    /**
     * ��ȡ��ǰ��¼�û����û���
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "get_current", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> get_current(HttpServletRequest request) {
        Map<String, String> ret = new HashMap<String, String>();
        Object attribute = request.getSession().getAttribute("student");
        if (attribute == null) {
            ret.put("type", "error");
            ret.put("msg", "��¼��ϢʧЧ");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "��ȡ�ɹ�");
        Student student = (Student) attribute;
        ret.put("username", student.getName());
        ret.put("truename", student.getTrueName());
        return ret;
    }

    /**
     * �û�������Ϣҳ��
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile(ModelAndView model, HttpServletRequest request) {
        Student student = (Student) request.getSession().getAttribute("student");
        model.addObject("title", "����������Ϣ");
        model.addObject("student", student);
        model.addObject("subject", subjectService.findById(student.getSubjectId()));
        model.setViewName("/home/user/profile");
        return model;
    }

    /**
     * ����ѧ����Ϣ
     *
     * @param request
     * @param student
     * @return
     */
    @RequestMapping(value = "update_info", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> updateInfo(HttpServletRequest request, Student student) {
        Map<String, String> ret = new HashMap<String, String>();
        Student onlineStudent = (Student) request.getSession().getAttribute("student");

        onlineStudent.setTrueName(student.getTrueName());
        onlineStudent.setTel(student.getTel());
        if (studentService.edit(onlineStudent) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "�޸�ʧ�ܣ�����ϵ����Ա");
            return ret;
        }
        request.getSession().setAttribute("student", onlineStudent);
        ret.put("type", "success");
        ret.put("msg", "��ȡ�ɹ�");
        ret.put("username", student.getName());
        return ret;
    }

    /**
     * �û��˳�
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        request.getSession().setAttribute("student", null);
        return "redirect:login";
    }

    /**
     * �޸�����
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public ModelAndView password(ModelAndView model, HttpServletRequest request) {
        Student student = (Student) request.getSession().getAttribute("student");
        model.addObject("student", student);
        model.setViewName("/home/user/password");
        return model;
    }

    /**
     * �޸�����
     *
     * @param request
     * @param student
     * @return
     */
    @RequestMapping(value = "update_password", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> updatePassword(HttpServletRequest request, Student student, String oldPassword) {
        Map<String, String> ret = new HashMap<String, String>();
        Student onlineStudent = (Student) request.getSession().getAttribute("student");
        if (!onlineStudent.getPassword().equals(oldPassword)) {
            ret.put("type", "error");
            ret.put("msg", "���������");
            return ret;
        }
        onlineStudent.setPassword(student.getPassword());
        if (studentService.edit(onlineStudent) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "�޸�ʧ�ܣ�����ϵ����Ա");
            return ret;
        }
        request.getSession().setAttribute("student", onlineStudent);
        request.getSession().removeAttribute("student");
        ret.put("type", "success");
        ret.put("msg", "��ȡ�ɹ�");
        return ret;
    }

    /**
     * ��ȡ��ǰѧ�����ڽ��еĿ�����Ϣ
     *
     * @param model
     * @param name
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value = "/exam_list", method = RequestMethod.GET)
    public ModelAndView examList(ModelAndView model,
                                 @RequestParam(name = "name", defaultValue = "") String name,
                                 @RequestParam(name = "page", defaultValue = "1") Integer page,
                                 HttpServletRequest request) {
        Student student = (Student) request.getSession().getAttribute("student");
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("subjectId", student.getSubjectId());
        queryMap.put("name", name);
        queryMap.put("offset", getOffset(page, PAGESIZE));
        queryMap.put("pageSize", PAGESIZE);
        model.addObject("examList", examService.findListByUser(queryMap));
        model.addObject("name", name);
        model.addObject("nowTime", System.currentTimeMillis());
        model.addObject("subject", subjectService.findById(student.getSubjectId()));
        model.setViewName("/home/user/exam_list");
        if (page < 1) page = 1;
        model.addObject("page", page);
        model.addObject("nowTime", System.currentTimeMillis());
        return model;
    }

    /**
     * ��ȡ��ǰѧ�������Ŀ�����Ϣ
     *
     * @param model
     * @param name
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value = "/history_list", method = RequestMethod.GET)
    public ModelAndView historyList(ModelAndView model,
                                    @RequestParam(name = "name", defaultValue = "") String name,
                                    @RequestParam(name = "page", defaultValue = "1") Integer page,
                                    HttpServletRequest request) {
        Student student = (Student) request.getSession().getAttribute("student");
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("name", name);
        queryMap.put("offset", getOffset(page, PAGESIZE));
        queryMap.put("pageSize", PAGESIZE);
        queryMap.put("studentId", student.getId());
        model.addObject("historyList", examPaperService.findHistory(queryMap));
        model.addObject("name", name);
        model.addObject("subject", subjectService.findById(student.getSubjectId()));
        model.setViewName("/home/user/history_list");
        if (page < 1) page = 1;
        model.addObject("page", page);
        model.addObject("nowTime", System.currentTimeMillis());
        return model;
    }

    /**
     * �ع��Ծ�
     *
     * @param model
     * @param examPaperId
     * @param examId
     * @param request
     * @return
     */

    @RequestMapping(value = "/review_exam", method = RequestMethod.GET)
    public ModelAndView reviewExam(ModelAndView model, Long examPaperId, Long examId, HttpServletRequest request) {
        Student student = (Student) request.getSession().getAttribute("student");

        Exam exam = examService.findById(examId);
        model.addObject("title", exam.getName() + "�Ծ�ع�");

        if (exam == null) {
            model.setViewName("/home/error/error");
            model.addObject("msg", "��ǰ���Բ�����!");
            return model;
        }
        if (examPaperId == null) {
            model.setViewName("/home/error/error");
            model.addObject("msg", "�Ծ�����");
            return model;
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("examId", examId);
        queryMap.put("studentId", student.getId());
        //���ݿ�����Ϣ��ѧ����Ϣ��ȡ�Ծ�
        ExamPaper examPaper = examPaperService.find(queryMap);
        if (examPaper == null) {
            model.setViewName("/home/error/error");
            model.addObject("msg", "��ǰ���Բ������Ծ�!");
            return model;
        }


        if (examPaper.getStatus() == 0) {
            model.setViewName("/home/error/error");
            model.addObject("msg", "��û�п������ſ���");
            return model;
        }

        queryMap.put("examPaperId", examPaper.getId());
        List<ExamPaperAnswer> findListByUser = examPaperAnswerService.findListByUser(queryMap);
        model.addObject("singleQuestionList", getExamPaperAnswerList(findListByUser, Question.QUESTION_TYPE_SINGLE));
        model.addObject("muiltQuestionList", getExamPaperAnswerList(findListByUser, Question.QUESTION_TYPE_MUILT));
        model.addObject("chargeQuestionList", getExamPaperAnswerList(findListByUser, Question.QUESTION_TYPE_CHARGE));
        model.addObject("singleScore", Question.QUESTION_TYPE_SINGLE_SCORE);
        model.addObject("muiltScore", Question.QUESTION_TYPE_MUILT_SCORE);
        model.addObject("chargeScore", Question.QUESTION_TYPE_CHARGE_SCORE);
        model.addObject("singleQuestion", Question.QUESTION_TYPE_SINGLE);
        model.addObject("muiltQuestion", Question.QUESTION_TYPE_MUILT);
        model.addObject("chargeQuestion", Question.QUESTION_TYPE_CHARGE);
        model.addObject("exam", exam);
        model.addObject("examPaper", examPaper);
        model.setViewName("home/user/review_exam");
        return model;
    }

    private int getOffset(int page, int pageSize) {
        if (page < 1) {
            page = 1;
        }
        return (page - 1) * pageSize;
    }

    /**
     * ����ָ�����͵�����
     *
     * @param examPaperAnswers
     * @param questionType
     * @return
     */
    private List<ExamPaperAnswer> getExamPaperAnswerList(List<ExamPaperAnswer> examPaperAnswers, int questionType) {
        List<ExamPaperAnswer> newExampaperAnswer = new ArrayList<ExamPaperAnswer>();
        for (ExamPaperAnswer examPaperAnswer : examPaperAnswers) {
            if (examPaperAnswer.getQuestion().getQuestionType() == questionType) {
                newExampaperAnswer.add(examPaperAnswer);
            }
        }
        return newExampaperAnswer;
    }

}
