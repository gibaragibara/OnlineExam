package com.ptu.zxk.dao.admin;

import com.ptu.zxk.entity.admin.Subject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 学科专业Dao
 */
@Repository
public interface SubjectDao {
    int add(Subject subject);
    int edit(Subject subject);
    List<Subject> findList(Map<String,Object> queryMap);
    int delete(Long id);
    Integer getTotal(Map<String,Object> queryMap);

    Subject findById(@Param("id") Long id);
}
