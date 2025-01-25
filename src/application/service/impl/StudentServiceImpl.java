package application.service.impl;

import application.entity.Student;
import application.service.AuthService;
import application.service.StudentService;
import utils.CurrentSession;
import utils.DatabaseUtil;
import utils.ValidatorsUtil;
import utils.authorization.IsStudent;
import utils.constants.Role;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Logger;

public class StudentServiceImpl implements StudentService, AuthService<Student> {
    Logger logger = Logger.getLogger(StudentServiceImpl.class.getName());

    @Override
    public void login(String username, String password) {
        logger.info("======= Start login =======");
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM student where username = ? and password = ?");
                ResultSet rs = ps.executeQuery()
        ){
            if (rs.next()){
                CurrentSession currentSession  = CurrentSession.getInstance();
                currentSession.setAuthenticated(Boolean.TRUE);
                currentSession.setCurrentRole(Role.STUDENT);
            } else {
                logger.info("Username or password incorrect");
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        logger.info("======= End login =======");
    }

    @Override
    public void register(Student entity) {
        logger.info("======= Start register =======");
        String sql = "INSERT INTO students(name,email,password,age,address,level_id,join_date) VALUES (?,?,?,?,?,?,?)";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            if (ValidatorsUtil.isStudentRegisterValid(entity)){
                ps.setString(1,entity.getName());
                ps.setString(2,entity.getEmail());
                ps.setString(3,entity.getPassword());
                ps.setInt(4,entity.getAge());
                ps.setString(5,entity.getAddress());
                ps.setInt(6,entity.getLevelId());
                ps.setDate(7, Date.valueOf(LocalDate.now()));

                ps.executeUpdate();
                logger.info("Student register successful!!");
            }
            else {
                logger.info("Student register failed, please  validate your data and try again");
            }

        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        logger.info("======= End register =======");
    }

    @IsStudent
    @Override
    public void seeInfo(int studentId) {
        logger.info("======= Start seeInfo =======");
        String sql = "SELECT * FROM students where id = ?";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
                ){
            if (rs.next()){
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setAge(rs.getInt("age"));
                student.setAddress(rs.getString("address"));
                student.setLevelId(rs.getInt("level_id"));
                student.setJoinDate(LocalDate.parse(rs.getString("join_date")));
                logger.info(student.toString());
            }

        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        logger.info("======= End seeInfo =======");
    }

    @IsStudent
    @Override
    public void seeSubjects(int studentId) {
        logger.info("======= Start seeSubjects =======");

        logger.info("======= End seeSubjects =======");
    }

    @IsStudent
    @Override
    public void seeGrades(int studentId) {
        logger.info("======= Start seeGrades =======");

        logger.info("======= End seeGrades =======");
    }
}
