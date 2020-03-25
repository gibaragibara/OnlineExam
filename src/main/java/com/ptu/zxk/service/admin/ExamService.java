package com.ptu.zxk.service.admin;

import com.ptu.zxk.entity.admin.Exam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * øº ‘service
 */
@Service
public interface ExamService {
    int add(Exam exam);

    int edit(Exam exam);
    int updateExam(Exam exam);

    List<Exam> findList(Map<String, Object> queryMap);

    int delete(Long id);

    Integer getTotal(Map<String, Object> queryMap);

    List<Exam> findListByUser(Map<String, Object> queryMap);

    Integer getTotalByUser(Map<String, Object> queryMap);

    Exam findById(Long id);

}
