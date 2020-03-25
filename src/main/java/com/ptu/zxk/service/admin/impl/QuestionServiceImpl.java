package com.ptu.zxk.service.admin.impl;

import com.ptu.zxk.dao.admin.QuestionDao;
import com.ptu.zxk.entity.admin.Question;
import com.ptu.zxk.service.admin.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Override
    public int add(Question question) {
        return questionDao.add(question);
    }

    @Override
    public int edit(Question question) {
        return questionDao.edit(question);
    }

    @Override
    public List<Question> findList(Map<String, Object> queryMap) {
        return questionDao.findList(queryMap);
    }

    @Override
    public int delete(Long id) {
        return questionDao.delete(id);
    }

    @Override
    public Integer getTotal(Map<String, Object> queryMap) {
        return questionDao.getTotal(queryMap);
    }

    @Override
    public Question findByTitle(String title) {
        return questionDao.findByTitle(title);
    }

    @Override
    public int getQuestionByType(Map<String, Long> queryMap) {
        return questionDao.getQuestionByType(queryMap);
    }

    @Override
    public Question findById(Long id) {
        return questionDao.findById(id);
    }


}
