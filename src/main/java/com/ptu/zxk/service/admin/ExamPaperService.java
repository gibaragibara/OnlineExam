package com.ptu.zxk.service.admin;

import com.ptu.zxk.entity.admin.ExamPaper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ÊÔ¾íservice
 */
@Service
public interface ExamPaperService {
    int add(ExamPaper examPaper);

    int edit(ExamPaper examPaper);

    List<ExamPaper> findList(Map<String, Object> queryMap);

    int delete(Long id);

    Integer getTotal(Map<String, Object> queryMap);

    List<ExamPaper> findHistory(Map<String, Object> queryMap);

    Integer getfindHistoryTotal(Map<String, Object> queryMap);

    ExamPaper find(Map<String, Object> queryMap);

    int submit(ExamPaper examPaper);

    List<Map<String, Object>>  getExamStats(Long examId);

}
