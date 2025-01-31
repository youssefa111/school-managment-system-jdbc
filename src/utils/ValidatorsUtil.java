package utils;

import application.entity.*;
import application.service.SystemService;
import application.service.impl.SystemServiceImpl;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;


public interface ValidatorsUtil {

    //==================================================================================================================
    SystemService systemService = new SystemServiceImpl();

    interface FormValidator {

            private static boolean isValidEmail(String email) {
                return email != null && email.contains("@") && email.contains(".") && email.length() < 101;
            }
            private static boolean isValidPassword(String password) {
                return password != null && password.length() >= 6 && password.length() < 101;
            }
            private static boolean isValidName(String username) {
                return username != null && username.length() >= 6 && username.length() < 101;
            }
            private static boolean isValidAddress(String address) {
                return address != null && address.length() >= 6 && address.length() < 201;
            }
            private static boolean isValidAge(int age){
                return age > 0 && age <= 150;
            }
            private static boolean isValidLevel(int levelId){
                LinkedHashSet<Level> levels = systemService.getSystemLevels();
                var levelsId = levels.stream().map(Level::id).collect(Collectors.toSet());
                return !levels.isEmpty() && levelsId.contains(levelId);
            }
            private static boolean isValidSubject(int subjectId){
                LinkedHashSet<Subject> subjects = systemService.getSystemSubjects();
                var subjectsId = subjects.stream().map(Subject::id).collect(Collectors.toSet());
                return !subjects.isEmpty() && subjectsId.contains(subjectId);
            }

            static boolean isStudentRegisterValid(Student student){
                return isValidEmail(student.getEmail())
                        && isValidPassword(student.getPassword())
                        && isValidName(student.getName())
                        && isValidAge(student.getAge())
                        && isValidAddress(student.getAddress())
                        && isValidLevel(student.getLevelId());
            }

            static boolean isTeacherRegisterValid(Teacher teacher){
                return isValidEmail(teacher.getEmail())
                        && isValidPassword(teacher.getPassword())
                        && isValidName(teacher.getName())
                        && isValidAge(teacher.getAge())
                        && isValidAddress(teacher.getAddress())
                        && isValidSubject(teacher.getSubjectId());
            }

            static boolean isAdminRegisterValid(Admin admin){
                return isValidEmail(admin.getEmail())
                        && isValidPassword(admin.getPassword())
                        && isValidName(admin.getName());
            }

            static boolean isLoginValid(String email, String password){
                return isValidEmail(email) && isValidPassword(password);
            }

    }

    //==================================================================================================================

     interface TeacherValidator{
        static boolean isValidGrade(double grade){
            return grade >= 0 && grade <= 100;
        }
    }





}
