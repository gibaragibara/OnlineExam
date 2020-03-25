package com.ptu.zxk.dao.admin;

import com.ptu.zxk.entity.admin.Exam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * øº ‘Dao
 */
@Repository
public interface ExamDao {
    int add(Exam exam);

    int edit(Exam exam);

    List<Exam> findList(Map<String, Object> queryMap);

    int delete(Long id);

    Integer getTotal(Map<String, Object> queryMap);

    List<Exam> findListByUser(Map<String, Object> queryMap);

    Integer getTotalByUser(Map<String, Object> queryMap);

    Exam findById(Long id);

    int updateExam(Exam exam);
}
