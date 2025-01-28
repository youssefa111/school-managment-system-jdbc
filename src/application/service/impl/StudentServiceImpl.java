package application.service.impl;

import application.entity.Student;
import application.service.AuthService;
import application.service.StudentService;
import utils.CurrentSession;
import utils.DatabaseUtil;
import utils.JsonConverterUtil;
import utils.ValidatorsUtil;
import utils.authorization.IsStudent;
import utils.constants.Role;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class StudentServiceImpl implements StudentService, AuthService<Student> {
    Logger logger = Logger.getLogger(StudentServiceImpl.class.getName());

    @Override
    public void login(String username, String password) {
        logger.info("======= Start login =======");
        if(ValidatorsUtil.FormValidator.isLoginValid(username, password)){
            try(
                    Connection conn = DatabaseUtil.getConnection();
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM students where username = ? and password = ?")
            ){
                ps.setString(1, username);
                ps.setString(2, password);

                try(ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        CurrentSession currentSession  = CurrentSession.getInstance();
                        currentSession.setAuthenticated(Boolean.TRUE);
                        currentSession.setCurrentRole(Role.STUDENT);
                        currentSession.setUserId(rs.getInt("id"));

                        Student student = new Student();
                        student.setId(rs.getInt("id"));
                        student.setName(rs.getString("name"));
                        student.setEmail(rs.getString("email"));
                        student.setAge(rs.getInt("age"));
                        student.setAddress(rs.getString("address"));
                        student.setLevelId(rs.getInt("level_id"));
                        student.setJoinDate(LocalDate.parse(rs.getString("join_date")));

                        currentSession.setUser(student);
                        logger.info("Login Success");
                    } else {
                        logger.info("Username or password incorrect");
                    }
                } catch (SQLException e){
                    logger.warning(e.getMessage());

                }

            } catch (SQLException e) {
                logger.warning(e.getMessage());
            }
        }
        else {
            logger.info("Please make sure you enter valid username and password");
        }

        logger.info("======= End login =======");
    }

    @Override
    public void register(Student entity) {
        logger.info("======= Start register =======");
        String sql = "INSERT INTO students(name,email,password,age,address,level_id,join_date) VALUES (?,?,?,?,?,?,?)";
        if (ValidatorsUtil.FormValidator.isStudentRegisterValid(entity)){
            try(
                    Connection conn = DatabaseUtil.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql);
            ) {

                    ps.setString(1,entity.getName());
                    ps.setString(2,entity.getEmail());
                    ps.setString(3,entity.getPassword());
                    ps.setInt(4,entity.getAge());
                    ps.setString(5,entity.getAddress());
                    ps.setInt(6,entity.getLevelId());
                    ps.setDate(7, Date.valueOf(LocalDate.now()));

                    ps.executeUpdate();
                    logger.info("Student register successful!!");


            } catch (SQLException e) {
                logger.warning(e.getMessage());
            }
        }
        else {
            logger.info("Student register failed, please  validate your data and try again");
        }
        logger.info("======= End register =======");
    }

    @IsStudent
    @Override
    public void seeInfo() {
        logger.info("======= Start seeInfo =======");
        CurrentSession currentSession  = CurrentSession.getInstance();
        String sql = "SELECT * FROM students where id = ?";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ){
            ps.setInt(1,currentSession.getUserId());
            try(ResultSet rs = ps.executeQuery())
            {
                if (rs.next()){
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setAge(rs.getInt("age"));
                    student.setAddress(rs.getString("address"));
                    student.setLevelId(rs.getInt("level_id"));
                    student.setJoinDate(LocalDate.parse(rs.getString("join_date")));
                    logger.info(JsonConverterUtil.convertToJson(student));
                }
            } catch (SQLException e) {
                logger.warning(e.getMessage());
            }

        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        logger.info("======= End seeInfo =======");
    }

    @IsStudent
    @Override
    public void seeSubjects() {
        logger.info("======= Start seeSubjects =======");
        String sql = "SELECT subject_name FROM subjects s INNER JOIN subject_student ss " +
                "ON s.id = ss.subject_id WHERE ss.student_id = ?";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ){
            ps.setInt(1,CurrentSession.getInstance().getUserId());
            try(ResultSet rs = ps.executeQuery()){
                List<String> subjects = new ArrayList<>();
                while (rs.next()){
                    subjects.add(rs.getString("subject_name"));
                }
                logger.info("Here are your subjects of this year:- \n " + JsonConverterUtil.convertToJson(subjects));
            } catch (SQLException e){
                logger.warning(e.getMessage());
            }
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= End seeSubjects =======");
    }

    @IsStudent
    @Override
    public void seeGrades() {
        logger.info("======= Start seeGrades =======");
        String sql = "SELECT s.subject_name ,g.grade FROM grades g INNER JOIN subject_student ss " +
                "ON g.ss_id = ss.ss_id  INNER JOIN subjects s ON ss.subject_id = s.id WHERE ss.student_id = ?";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ){
            ps.setInt(1,CurrentSession.getInstance().getUserId());
            try(ResultSet rs = ps.executeQuery()){
                Map<String,Double> subjectGrades = new HashMap<>();
                while (rs.next()){
                    subjectGrades.put(rs.getString("subject_name"),rs.getDouble("grade"));
                }
                if(subjectGrades.isEmpty())
                {
                    logger.info("There are no grades yet for any subject");
                }
                else{
                    logger.info("Here are your grades of this year:- " + JsonConverterUtil.convertToJson(subjectGrades));
                }
            }
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= End seeGrades =======");
    }
}
