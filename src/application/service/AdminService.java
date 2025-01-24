package application.service;

import application.entity.Student;
import application.entity.Subject;

public interface AdminService {

    // CRUD Subject
    void addSubject(Subject subject);
    void deleteSubject(int subjectId);
    void updateSubject(Subject subject);
    void getAllSubject();

    // CRUD Student
    void getAllStudent();
    void deleteStudent(int studentId);
    void deleteAllStudent(int[] studentId);

    // CRUD Teacher
    void getAllTeacher();
    void deleteTeacher(int teacherId);
    void deleteAllTeachers(int[] teacherIds);
}
