package com.ptu.zxk.dao.admin;

import com.ptu.zxk.entity.admin.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 学科专业Dao
 */
@Repository
public interface StudentDao {
    int add(Student student);
    int edit(Student student);
    List<Student> findList(Map<String, Object> queryMap);
    int delete(Long id);
    Integer getTotal(Map<String, Object> queryMap);
    Student findByName(@Param("name") String name);


}
