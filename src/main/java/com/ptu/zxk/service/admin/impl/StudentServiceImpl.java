package com.ptu.zxk.service.admin.impl;

import com.ptu.zxk.dao.admin.StudentDao;
import com.ptu.zxk.entity.admin.Student;
import com.ptu.zxk.service.admin.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDao studentDao;
    @Override
    public int add(Student student) {
        return studentDao.add(student);
    }

    @Override
    public int edit(Student student) {
        return studentDao.edit(student);
    }

    @Override
    public List<Student> findList(Map<String, Object> queryMap) {
        return studentDao.findList(queryMap);
    }

    @Override
    public int delete(Long id) {
        return studentDao.delete(id);
    }

    @Override
    public Integer getTotal(Map<String, Object> queryMap) {
        return studentDao.getTotal(queryMap);
    }

    @Override
    public Student findByName(String name) {
        return studentDao.findByName(name);
    }
}
