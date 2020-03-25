package com.ptu.zxk.dao.admin;

import com.ptu.zxk.entity.admin.Question;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *  ‘Ã‚◊®“µDao
 */
@Repository
public interface QuestionDao {
    int add(Question question);

    int edit(Question question);

    List<Question> findList(Map<String, Object> queryMap);

    int delete(Long id);

    Integer getTotal(Map<String, Object> queryMap);

    Question findByTitle(@Param("title") String title);

    int getQuestionByType(Map<String, Long> queryMap);

    Question findById(Long id);

}
