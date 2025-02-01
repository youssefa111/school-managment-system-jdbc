package application.service;

import application.entity.Level;
import application.entity.Student;
import application.entity.Subject;

public interface AdminService {

    // CRUD Subject
    void addSubject(String subjectName);
    void deleteSubject(int subjectId);
    void updateSubject(Subject subject);
    void getAllSubject();

    // CRUD Level
    void addLevel(String levelName);
    void deleteLevel(int levelId);
    void updateLevel(Level level);
    void getAllLevel();

    // CRUD Student
    void getAllStudent();
    void deleteStudent(int studentId);
    void deleteAllStudent(int[] studentId);
    void addStudentToClass(int studentId, int classId);

    // CRUD Teacher
    void getAllTeacher();
    void deleteTeacher(int teacherId);
    void deleteAllTeachers(int[] teacherIds);
    void addTeacherToClass(int teacherId, int classId);

    // CRUD Class
    void getAllClasses();
    void addClass(String className , int capacity);
    void deleteClass(int classId);
    void updateClass(int classId , int capacity);

}
