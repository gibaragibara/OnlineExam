package com.ptu.zxk.service.admin;

import com.ptu.zxk.entity.admin.Question;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  ‘Ã‚service
 */
@Service
public interface QuestionService {
    int add(Question question);

    int edit(Question question);

    List<Question> findList(Map<String, Object> queryMap);

    int delete(Long id);

    Integer getTotal(Map<String, Object> queryMap);

    Question findByTitle(String title);

    int getQuestionByType(Map<String ,Long> queryMap);

    Question findById(Long id);

}
