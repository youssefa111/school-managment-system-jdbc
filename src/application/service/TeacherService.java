package application.service;

public interface TeacherService {

    void seeInfo(int studentId);
    void addGrades(int studentId , int subjectId , double grade);
}
