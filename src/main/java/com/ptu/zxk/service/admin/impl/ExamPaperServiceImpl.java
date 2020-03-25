package com.ptu.zxk.service.admin.impl;

import com.ptu.zxk.dao.admin.ExamPaperDao;
import com.ptu.zxk.entity.admin.ExamPaper;
import com.ptu.zxk.service.admin.ExamPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExamPaperServiceImpl implements ExamPaperService {
    @Autowired
    private ExamPaperDao examPaperDao;

    @Override
    public int add(ExamPaper examPaper) {
        return examPaperDao.add(examPaper);
    }

    @Override
    public int edit(ExamPaper examPaper) {
        return examPaperDao.edit(examPaper);
    }


    @Override
    public List<ExamPaper> findList(Map<String, Object> queryMap) {
        return examPaperDao.findList(queryMap);
    }

    @Override
    public int delete(Long id) {
        return examPaperDao.delete(id);
    }

    @Override
    public Integer getTotal(Map<String, Object> queryMap) {
        return examPaperDao.getTotal(queryMap);
    }

    @Override
    public List<ExamPaper> findHistory(Map<String, Object> queryMap) {
        return examPaperDao.findHistory(queryMap);
    }

    @Override
    public Integer getfindHistoryTotal(Map<String, Object> queryMap) {
        return examPaperDao.getfindHistoryTotal(queryMap);
    }

    @Override
    public ExamPaper find(Map<String, Object> queryMap) {
        return examPaperDao.find(queryMap);
    }

    @Override
    public int submit(ExamPaper examPaper) {
        return examPaperDao.submit(examPaper);
    }

    @Override
    public List<Map<String, Object>>  getExamStats(Long examId) {
        return examPaperDao.getExamStats(examId);
    }
}
