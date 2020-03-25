package com.ptu.zxk.service.admin;

import com.ptu.zxk.entity.admin.Subject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ѧ��רҵservice
 */
@Service
public interface SubjectService {
    int add(Subject subject);
    int edit(Subject subject);
    List<Subject> findList(Map<String,Object> queryMap);
    int delete(Long id);
    Integer getTotal(Map<String,Object> queryMap);
    Subject findById(Long id);
}
