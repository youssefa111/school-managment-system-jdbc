package utils;

import application.entity.Student;
import application.entity.Teacher;

public interface ValidatorsUtil {

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
        return true;
    }
    private static boolean isValidSubject(int subjectId){
        return true;
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

    static boolean isLoginValid(String email, String password){
       return isValidEmail(email) && isValidPassword(password);
    }


}
