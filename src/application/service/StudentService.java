package application.service;

import application.entity.Student;

public interface StudentService {

    void seeInfo(int studentId);
    void seeSubjects(int studentId);
    void seeGrades(int studentId);
}
