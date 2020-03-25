package com.ptu.zxk.service.admin.impl;

import com.ptu.zxk.dao.admin.ExamDao;
import com.ptu.zxk.entity.admin.Exam;
import com.ptu.zxk.service.admin.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    private ExamDao examDao;

    @Override
    public int add(Exam exam) {
        return examDao.add(exam);
    }

    @Override
    public int edit(Exam exam) {
        return examDao.edit(exam);
    }

    @Override
    public int updateExam(Exam exam) {
        return examDao.updateExam(exam);
    }

    @Override
    public List<Exam> findList(Map<String, Object> queryMap) {
        return examDao.findList(queryMap);
    }

    @Override
    public int delete(Long id) {
        return examDao.delete(id);
    }

    @Override
    public Integer getTotal(Map<String, Object> queryMap) {
        return examDao.getTotal(queryMap);
    }

    @Override
    public List<Exam> findListByUser(Map<String, Object> queryMap) {
        return examDao.findListByUser(queryMap);
    }

    @Override
    public Integer getTotalByUser(Map<String, Object> queryMap) {
        return examDao.getTotalByUser(queryMap);
    }

    @Override
    public Exam findById(Long id) {
        return examDao.findById(id);
    }
}
