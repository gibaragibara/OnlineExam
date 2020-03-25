package com.ptu.zxk.service.admin;

import com.ptu.zxk.entity.admin.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ¿¼Éúservice
 */
@Service
public interface StudentService {
    int add(Student student);
    int edit(Student student);
    List<Student> findList(Map<String, Object> queryMap);
    int delete(Long id);
    Integer getTotal(Map<String, Object> queryMap);
    Student findByName(String name);
}
