package com.ptu.zxk.controller.home;

import com.ptu.zxk.entity.admin.*;
import com.ptu.zxk.service.admin.ExamPaperAnswerService;
import com.ptu.zxk.service.admin.ExamPaperService;
import com.ptu.zxk.service.admin.ExamService;
import com.ptu.zxk.service.admin.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * ǰ̨�û����Կ�����
 */
@RequestMapping("/home/exam")
@Controller
public class HomExamController {
    @Autowired
    private ExamService examService;
    @Autowired
    private ExamPaperService examPaperService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private ExamPaperAnswerService examPaperAnswerService;

    /**
     * ��ʼ����ǰ���Ϸ��ԣ������������
     *
     * @param examId
     * @return
     */
    @RequestMapping(value = "/start_exam", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> startExam(Long examId, HttpServletRequest request) {
        Map<String, String> ret = new HashMap<String, String>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        Exam exam = examService.findById(examId);
        if (exam == null) {
            ret.put("type", "error");
            ret.put("msg", "������Ϣ������");
            return ret;
        }
        if (exam.getEndTime().getTime() < new Date().getTime()) {
            ret.put("type", "error");
            ret.put("msg", "�ÿ����ѽ���");
            return ret;
        }
        Student student = (Student) request.getSession().getAttribute("student");
        if (exam.getSubjectId().longValue() != student.getSubjectId().longValue()) {
            ret.put("type", "error");
            ret.put("msg", "ѧ�Ʋ�ͬ�޷����п���");
            return ret;
        }
        queryMap.put("examId", exam.getId());
        queryMap.put("studentId", student.getId());

        ExamPaper find = examPaperService.find(queryMap);
        if (find != null) {
            if (find.getStatus() == 1) {
                //��ʾ�Ѿ��������Ծ�
                ret.put("type", "error");
                ret.put("msg", "�Ѿ����й��ÿ��ԣ��뵽��ʷ�����в鿴�ɼ�");
                return ret;
            }
            //�Ծ��Ѿ����ɣ�����δ���ԣ����Կ�ʼ���¿���
            ret.put("type", "success");
            ret.put("msg", "���Կ�ʼ����");
            return ret;
        }
        //��ʱ˵���Ѿ����Ͽ�����������ʼ�����Ծ�
        //�Ƿ����������Ծ�����
        Map<String, Long> qMap = new HashMap<String, Long>();
        qMap.put("questionType",Long.valueOf(Question.QUESTION_TYPE_SINGLE));
        qMap.put("subjectId",exam.getSubjectId());
        int singleQuestionTotalNum = questionService.getQuestionByType(qMap);//��ȡ��ѡ������
        if (exam.getSingleQuestionNum() > singleQuestionTotalNum) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ������������ⵥѡ���������޷������Ծ�");
            return ret;
        }
        qMap.put("questionType",Long.valueOf(Question.QUESTION_TYPE_MUILT));
        int muiltQuestionTotalNum = questionService.getQuestionByType(qMap);//��ȡ��ѡ������
        if (exam.getMuiltQuestionNum() > muiltQuestionTotalNum) {
            ret.put("type", "error");
            ret.put("msg", "��ѡ��������������ѡ���������޷������Ծ�");
            return ret;
        }
        qMap.put("questionType",Long.valueOf(Question.QUESTION_TYPE_CHARGE));
        int chargeQuestionTotalNum = questionService.getQuestionByType(qMap);//��ȡ�ж�������
        if (exam.getChargeQuestionNum() > chargeQuestionTotalNum) {
            ret.put("type", "error");
            ret.put("msg", "�ж���������������ж����������޷������Ծ�");
            return ret;
        }
        //�������������� ��ʼ�����Ծ�
        ExamPaper examPaper = new ExamPaper();
        examPaper.setCreateTime(new Date());
        examPaper.setExamId(examId);
        examPaper.setStatus(0);
        examPaper.setStudentId(student.getId());
        examPaper.setTotalScore(exam.getTotalScore());
        examPaper.setUseTime(0);
        if (examPaperService.add(examPaper) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "�Ծ�����ʧ�ܣ�����ϵ����Ա");
            return ret;
        }
        //�Ծ��Ѿ����ɣ����ڿ�ʼ��������
        Map<String, Object> queryQuestionMap = new HashMap<String, Object>();
        queryQuestionMap.put("questionType", Question.QUESTION_TYPE_SINGLE);
        queryQuestionMap.put("subjectId", exam.getSubjectId());
        queryQuestionMap.put("offset", 0);
        queryQuestionMap.put("pageSize", 99999);
        System.out.println(student.getId());
        if (exam.getSingleQuestionNum() > 0) {
            //���Թ涨��ѡ����������0
            //��ȡ���еĵ�ѡ��
            List<Question> singleQuestionList = questionService.findList(queryQuestionMap);
            //���ѡȡ���Թ涨�����ĵ�ѡ�⣬���뵽���ݿ���
            List<Question> selectedSingleQuestionList = getRandomList(singleQuestionList, exam.getSingleQuestionNum());
            insertQuestionAnswer(selectedSingleQuestionList, examId, examPaper.getId(), student.getId());
        }
        if (exam.getMuiltQuestionNum() > 0) {
            queryQuestionMap.put("questionType", Question.QUESTION_TYPE_MUILT);
            //��ȡ���еĶ�ѡ��
            List<Question> muiltQuestionList = questionService.findList(queryQuestionMap);
            //���ѡȡ���Թ涨�����Ķ�ѡ�⣬���뵽���ݿ���
            List<Question> selectedMuiltQuestionList = getRandomList(muiltQuestionList, exam.getMuiltQuestionNum());
            insertQuestionAnswer(selectedMuiltQuestionList, examId, examPaper.getId(), student.getId());

        }
        if (exam.getChargeQuestionNum() > 0) {
            //��ȡ���е��ж���
            queryQuestionMap.put("questionType", Question.QUESTION_TYPE_CHARGE);
            List<Question> chargeQuestionList = questionService.findList(queryQuestionMap);
            //���ѡȡ���Թ涨�������ж��⣬���뵽���ݿ���
            List<Question> selectedChargeQuestionList = getRandomList(chargeQuestionList, exam.getChargeQuestionNum());
            insertQuestionAnswer(selectedChargeQuestionList, examId, examPaper.getId(), student.getId());
        }
        exam.setPaperNum(exam.getPaperNum() + 1);
        examService.updateExam(exam);
        ret.put("type", "success");
        ret.put("msg", "�Ծ����ɳɹ�");
        return ret;
    }

    /**
     * ��ʼ����
     *
     * @param model
     * @param examId
     * @param request
     * @return
     */

    @RequestMapping(value = "/examing", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView model, Long examId, HttpServletRequest request) {
        Student student = (Student) request.getSession().getAttribute("student");
        Exam exam = examService.findById(examId);
        if (exam == null) {
            model.setViewName("/home/error/error");
            model.addObject("msg", "��ǰ���Բ�����!");
            return model;
        }
        model.addObject("title", exam.getName() + "��ʼ����");
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
        if (exam.getEndTime().getTime() < new Date().getTime()) {
            model.setViewName("/home/error/error");
            model.addObject("msg", "��ǰ�����Ѿ�����!");
            return model;
        }
        if (exam.getSubjectId().longValue() != student.getSubjectId().longValue()) {
            model.setViewName("/home/error/error");
            model.addObject("msg", "ѧ������רҵ�Ϳ�������רҵ������!");
            return model;
        }
        if (examPaper.getStatus() == 1) {
            model.setViewName("/home/error/error");
            model.addObject("msg", "�Ѿ��������ſ�����!");
            return model;
        }
        Date now = new Date();
        Object attributeStartExamTime = request.getSession().getAttribute("startExamTime");
        if (attributeStartExamTime == null) {
            request.getSession().setAttribute("startExamTime", now);
        }
        Date startExamTime = (Date) request.getSession().getAttribute("startExamTime");
        int passedTime = (int) ((now.getTime() - startExamTime.getTime())) / 1000 / 60;
        if (passedTime >= exam.getAvailableTime()) {
            //��ʾʱ���Ѿ��ľ�����0�ִ���
            examPaper.setScore(0);
            examPaper.setStartExamTime(startExamTime);
            examPaper.setStatus(1);
            examPaper.setUseTime(passedTime);
            model.setViewName("/home/error/error");
            examPaperService.submit(examPaper);
            model.addObject("msg", "��ǰ���Կ���ʱ���Ѿ��ľ�����0�ִ���!");
            request.getSession().setAttribute("startExamTime", null);
            return model;
        }
        Integer hour = (exam.getAvailableTime() - passedTime) / 60;
        Integer minite = (exam.getAvailableTime() - passedTime) % 60;
        Integer second = (exam.getAvailableTime() * 60 - (int) ((now.getTime() - startExamTime.getTime()) / 1000)) % 60;
        queryMap.put("examPaperId", examPaper.getId());
        List<ExamPaperAnswer> findListByUser = examPaperAnswerService.findListByUser(queryMap);
        model.addObject("singleQuestionList", getExamPaperAnswerList(findListByUser, Question.QUESTION_TYPE_SINGLE));
        model.addObject("muiltQuestionList", getExamPaperAnswerList(findListByUser, Question.QUESTION_TYPE_MUILT));
        model.addObject("chargeQuestionList", getExamPaperAnswerList(findListByUser, Question.QUESTION_TYPE_CHARGE));
        model.addObject("hour", hour);
        model.addObject("minite", minite);
        model.addObject("second", second);
        model.addObject("singleScore", Question.QUESTION_TYPE_SINGLE_SCORE);
        model.addObject("muiltScore", Question.QUESTION_TYPE_MUILT_SCORE);
        model.addObject("chargeScore", Question.QUESTION_TYPE_CHARGE_SCORE);
        model.addObject("singleQuestion", Question.QUESTION_TYPE_SINGLE);
        model.addObject("muiltQuestion", Question.QUESTION_TYPE_MUILT);
        model.addObject("chargeQuestion", Question.QUESTION_TYPE_CHARGE);
        model.addObject("exam", exam);
        model.addObject("examPaper", examPaper);
        model.setViewName("home/exam/examing");
        return model;
    }

    /**
     * �û�ѡ������ύ��
     *
     * @param examPaperAnswer
     * @param request
     * @return
     */
    @RequestMapping(value = "/submit_answer", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> submitAnswer(ExamPaperAnswer examPaperAnswer, HttpServletRequest request) {
        Map<String, String> ret = new HashMap<String, String>();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        Exam exam = examService.findById(examPaperAnswer.getExamId());
        if (examPaperAnswer == null) {
            ret.put("type", "error");
            ret.put("msg", "����ȷ����");
        }
        if (exam.getEndTime().getTime() < new Date().getTime()) {
            ret.put("type", "error");
            ret.put("msg", "�ÿ����ѽ���");
            return ret;
        }
        Student student = (Student) request.getSession().getAttribute("student");
        if (exam.getSubjectId().longValue() != student.getSubjectId().longValue()) {
            ret.put("type", "error");
            ret.put("msg", "ѧ�Ʋ�ͬ�޷����п���");
            return ret;
        }
        queryMap.put("examId", exam.getId());
        queryMap.put("studentId", student.getId());
        ExamPaper findExamPaper = examPaperService.find(queryMap);
        if (findExamPaper == null) {
            ret.put("type", "error");
            ret.put("msg", "�������Ծ�");
            return ret;
        }
        if (findExamPaper.getId().longValue() != examPaperAnswer.getExamPaperId().longValue()) {
            ret.put("type", "error");
            ret.put("msg", "�����Ծ���ȷ����淶����");
            return ret;
        }

        Question question = questionService.findById(examPaperAnswer.getQuestionId());
        if (question == null) {
            ret.put("type", "error");
            ret.put("msg", "�����ⲻ���ڣ���淶����");
            return ret;
        }
        //��ʱ���Խ��𰸲��뵽���ݿ���
        examPaperAnswer.setStudentId(student.getId());
        if (question.getAnswer().equals(examPaperAnswer.getAnswer())) {
            examPaperAnswer.setIsCorrect(1);
        }
        if (examPaperAnswerService.submitAnswer(examPaperAnswer) <= 0) {
            ret.put("type", "error");
            ret.put("msg", "�����������ϵ����Ա");
            return ret;
        }
        ret.put("type", "success");
        ret.put("msg", "����ɹ�");
        return ret;
    }

    /**
     * �ύ�Ծ�
     *
     * @param examId
     * @param examPaperId
     * @param request
     * @return
     */
    @RequestMapping(value = "/submit_exam", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> submitExam(Long examId, Long examPaperId, HttpServletRequest request) {
        Map<String, String> ret = new HashMap<String, String>();

        Map<String, Object> queryMap = new HashMap<String, Object>();
        Exam exam = examService.findById(examId);
        if (exam == null) {
            ret.put("type", "error");
            ret.put("msg", "���Բ�����,����ȷ����");
        }
        if (exam.getEndTime().getTime() < new Date().getTime()) {
            ret.put("type", "error");
            ret.put("msg", "�ÿ����ѽ���");
            return ret;
        }
        Student student = (Student) request.getSession().getAttribute("student");
        if (exam.getSubjectId().longValue() != student.getSubjectId().longValue()) {
            ret.put("type", "error");
            ret.put("msg", "ѧ�Ʋ�ͬ�޷����в���");
            return ret;
        }
        queryMap.put("examId", exam.getId());
        queryMap.put("studentId", student.getId());
        ExamPaper findExamPaper = examPaperService.find(queryMap);
        if (findExamPaper.getStatus() == 1) {
            ret.put("type", "error");
            ret.put("msg", "�����ظ�����");
            return ret;
        }
        if (findExamPaper == null) {
            ret.put("type", "error");
            ret.put("msg", "�������Ծ�");
            return ret;
        }
        if (findExamPaper.getId().longValue() != examPaperId.longValue()) {
            ret.put("type", "error");
            ret.put("msg", "�����Ծ���ȷ����淶����");
            return ret;
        }
        //��ʱ���㿼�Ե÷�
        queryMap.put("examPaperId", examPaperId);
        List<ExamPaperAnswer> examPaperAnswerList = examPaperAnswerService.findListByUser(queryMap);
        int score = 0;
        for (ExamPaperAnswer examPaperAnswer : examPaperAnswerList) {
            if (examPaperAnswer.getIsCorrect() == 1) {
                score += examPaperAnswer.getQuestion().getScore();
            }
        }
        findExamPaper.setEndExamTime(new Date());
        findExamPaper.setScore(score);
        findExamPaper.setStartExamTime((Date) request.getSession().getAttribute("startExamTime"));
        //��ʱ���Խ��𰸲��뵽���ݿ���
        findExamPaper.setStatus(1);
        findExamPaper.setUseTime((int) ((findExamPaper.getEndExamTime().getTime() - findExamPaper.getStartExamTime().getTime()) / 1000 / 60));
        examPaperService.submit(findExamPaper);
        //���㿼��ͳ�ƽ���������ѿ���������������
        exam.setExamedNum(exam.getExamedNum() + 1);
        if (findExamPaper.getScore() >= exam.getPassScore()) {
            //˵��������
            exam.setPassNum(exam.getPassNum() + 1);
        }
        request.getSession().setAttribute("startExamTime", null);
        //����ͳ�ƽ��
        examService.updateExam(exam);
        ret.put("type", "success");
        ret.put("msg", "�ύ�ɹ�");
        return ret;
    }

    /**
     * ����Ӹ�����list��ѡȡ����������Ԫ��
     *
     * @param questionList
     * @param n
     * @return
     */
    private List<Question> getRandomList(List<Question> questionList, int n) {
        if (questionList.size() <= n) {
            return questionList;
        }
        Map<Integer, String> selectedMap = new HashMap<Integer, String>();
        List<Question> selectedList = new ArrayList<Question>();
        while (selectedList.size() < n) {
            for (Question question : questionList) {
                int index = (int) ((Math.random() * questionList.size()));
                if (!selectedMap.containsKey(index)) {
                    selectedMap.put(index, "");
                    selectedList.add(questionList.get(index));
                    if (selectedList.size() >= n) break;
                }

            }
        }
        return selectedList;
    }

    /**
     * ����ָ��������ʵ�嵽���������
     *
     * @param questionList
     * @param examId
     * @param studentId
     * @param examPaperId
     */
    private void insertQuestionAnswer(List<Question> questionList, Long examId, Long examPaperId, Long studentId) {
        for (Question question : questionList) {
            ExamPaperAnswer examPaperAnswer = new ExamPaperAnswer();
            examPaperAnswer.setExamId(examId);
            examPaperAnswer.setExamPaperId(examPaperId);
            examPaperAnswer.setIsCorrect(0);
            examPaperAnswer.setQuestionId(question.getId());
            examPaperAnswer.setStudentId(studentId);
            examPaperAnswerService.add(examPaperAnswer);
        }
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
