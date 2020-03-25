package com.ptu.zxk.entity.admin;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ����ʵ����
 */
@Component
public class Question {
    public static int QUESTION_TYPE_SINGLE = 0;//��ѡ����
    public static int QUESTION_TYPE_MUILT = 1;//��ѡѡ����
    public static int QUESTION_TYPE_CHARGE = 2;//�ж�����

    public static int QUESTION_TYPE_SINGLE_SCORE = 2;//��ѡ��ÿ��2��
    public static int QUESTION_TYPE_MUILT_SCORE = 4;//��ѡ��ÿ��2��
    public static int QUESTION_TYPE_CHARGE_SCORE = 2;//�ж���ÿ��2��

    private Long id;
    private int questionType;//��������
    private String title;//��Ŀ
    private int score;//��ֵ
    private String attrA;//ѡ��A
    private String attrB;//ѡ��B
    private String attrC;//ѡ��C
    private String attrD;//ѡ��D
    private String answer;//��ȷ��
    private Long subjectId;
    private Date createTime;//���ʱ��


    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {

        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttrA() {
        return attrA;
    }

    public void setAttrA(String attrA) {
        this.attrA = attrA;
    }

    public String getAttrB() {
        return attrB;
    }

    public void setAttrB(String attrB) {
        this.attrB = attrB;
    }

    public String getAttrC() {
        return attrC;
    }

    public void setAttrC(String attrC) {
        this.attrC = attrC;
    }

    public String getAttrD() {
        return attrD;
    }

    public void setAttrD(String attrD) {
        this.attrD = attrD;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public void setScoreByType(){
        if (questionType == QUESTION_TYPE_SINGLE) {
            score=QUESTION_TYPE_SINGLE_SCORE;
        }
        if (questionType == QUESTION_TYPE_MUILT) {
            score=QUESTION_TYPE_MUILT_SCORE;
        }
        if (questionType == QUESTION_TYPE_CHARGE) {
            score=QUESTION_TYPE_CHARGE_SCORE;
        }
    }
}
