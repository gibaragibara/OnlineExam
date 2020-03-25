package com.ptu.zxk.dao.admin;

import com.ptu.zxk.entity.admin.ExamPaperAnswer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * ÊÔ¾í´ð°¸Dao
 */
@Repository
public interface ExamPaperAnswerDao {
    int add(ExamPaperAnswer examPaperAnswer);

    int edit(ExamPaperAnswer examPaperAnswer);

    List<ExamPaperAnswer> findList(Map<String, Object> queryMap);

    int delete(Long id);

    Integer getTotal(Map<String, Object> queryMap);


    List<ExamPaperAnswer> findListByUser(Map<String, Object> queryMap);

    int submitAnswer(ExamPaperAnswer examPaperAnswer);
}
