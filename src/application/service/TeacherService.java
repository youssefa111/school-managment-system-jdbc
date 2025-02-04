package application.service;

import application.dto.StudentDto;

import java.util.List;

public interface TeacherService {

    void seeInfo();
    void addGrades(int studentId,int classId, double grade);
    void seeMyClasses();
    List<StudentDto> seeMyStudentsAtClass(int classId);
}
