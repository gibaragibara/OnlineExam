package com.ptu.zxk.entity.admin;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ����ʵ����
 */
@Component
public class Student {
    private Long id;
    private String name;//�û���
    private String password;//��¼����
    private Long subjectId;//����ѧ��רҵID
    private String trueName;//ѧ������
    private String tel;//�ֻ���
    private Date createTime;//ע��ʱ��

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
