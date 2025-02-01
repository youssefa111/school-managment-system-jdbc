package application.service.impl;

import application.entity.Teacher;
import application.service.AuthService;
import application.service.TeacherService;
import utils.CurrentSession;
import utils.DatabaseUtil;
import utils.JsonConverterUtil;
import utils.ValidatorsUtil;
import utils.authorization.IsTeacher;
import utils.constants.Role;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Logger;

public class TeacherServiceImpl implements TeacherService , AuthService<Teacher>{

    Logger logger = Logger.getLogger(TeacherServiceImpl.class.getName());


    @Override
    public void login(String username, String password) {
        logger.info("======= start login =======");
        String sql = "SELECT * FROM teachers WHERE username= ? AND password= ?";

        if(ValidatorsUtil.FormValidator.isLoginValid(username, password)){
            try(
                    Connection conn = DatabaseUtil.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql);
            ){
                    ps.setString(1,username);
                    ps.setString(2,password);

                    try(ResultSet rs = ps.executeQuery()){
                        if(rs.next()) {
                            CurrentSession currentSession = CurrentSession.getInstance();
                            currentSession.setAuthenticated(Boolean.TRUE);
                            currentSession.setCurrentRole(Role.TEACHER);
                            currentSession.setUserId(rs.getInt("id"));

                            Teacher teacher = new Teacher();
                            teacher.setId(rs.getInt("id"));
                            teacher.setName(rs.getString("name"));
                            teacher.setEmail(rs.getString("email"));
                            teacher.setAge(rs.getInt("age"));
                            teacher.setAddress(rs.getString("address"));
                            teacher.setSubjectId(rs.getInt("subject_id"));
                            teacher.setJoinDate(LocalDate.parse(rs.getString("join_date")));

                            currentSession.setUser(teacher);
                            logger.info("Login Success");
                        } else {
                            logger.info("Username or password incorrect");
                        }
                    }
            }
            catch (SQLException e) {
                logger.warning(e.getMessage());
            }
        }
        else {
            logger.info("Please make sure you enter valid username and password");
        }
        logger.info("======= end login =======");
    }

    @Override
    public void register(Teacher entity) {
        logger.info("======= start register =======");
        String sql = "INSERT INTO teachers(name,email,password,age,address,subject_id,join_date) VALUES (?,?,?,?,?,?,?)";
        if (ValidatorsUtil.FormValidator.isTeacherRegisterValid(entity)){
            try(
                    Connection conn = DatabaseUtil.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql);
            ) {
                    ps.setString(1,entity.getName());
                    ps.setString(2,entity.getEmail());
                    ps.setString(3,entity.getPassword());
                    ps.setInt(4,entity.getAge());
                    ps.setString(5,entity.getAddress());
                    ps.setInt(6,entity.getSubjectId());
                    ps.setDate(7, Date.valueOf(LocalDate.now()));

                    ps.executeUpdate();
                    logger.info("Teacher register successful!!");
            } catch (SQLException e) {
                logger.warning(e.getMessage());
            }
        }
        else {
            logger.info("Teacher register failed, please  validate your data and try again");
        }
        logger.info("======= end register =======");
    }

    @IsTeacher
    @Override
    public void seeInfo() {
        logger.info("======= start seeInfo =======");
        CurrentSession currentSession  = CurrentSession.getInstance();
        String sql = "SELECT * FROM teachers where id = ?";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setInt(1,currentSession.getUserId());
            try(ResultSet rs = ps.executeQuery())
            {
                if (rs.next()){
                    Teacher teacher = new Teacher();
                    teacher.setId(rs.getInt("id"));
                    teacher.setName(rs.getString("name"));
                    teacher.setEmail(rs.getString("email"));
                    teacher.setAge(rs.getInt("age"));
                    teacher.setAddress(rs.getString("address"));
                    teacher.setSubjectId(rs.getInt("subject_id"));
                    teacher.setJoinDate(LocalDate.parse(rs.getString("join_date")));
                    logger.info(JsonConverterUtil.convertToJson(teacher));
                }
            } catch (SQLException e) {
                logger.warning(e.getMessage());
            }

        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        logger.info("======= end seeInfo =======");
    }

    @IsTeacher
    @Override
    public void addGrades(int studentId, double grade) {
        logger.info("======= start addGrades =======");
        String sql = "INSERT INTO grades VALUES (ss_id,?) WHERE ss_id=(SELECT ss_id FROM subject_student WHERE subject_id = ? AND student_id = ?)";
        if (ValidatorsUtil.TeacherValidator.isValidGrade(grade))
        {
            try(
                    Connection conn = DatabaseUtil.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)
            ){
                //TODO: Validate that studentId is related to this teacher
                    CurrentSession currentSession  = CurrentSession.getInstance();
                    Teacher teacher = (Teacher) currentSession.getUser();

                    ps.setDouble(1,grade);
                    ps.setInt(2,studentId);
                    ps.setInt(3,teacher.getSubjectId());
                    ps.executeUpdate();
                    logger.info("Grades added successful!");
            } catch (SQLException e) {
                logger.warning(e.getMessage());
            }
        }
        else {
            logger.info("Grades added failed, please validate your data and try again");
        }
        logger.info("======= end addGrades =======");
    }

    @IsTeacher
    @Override
    public void seeMyStudents() {
        logger.info("======= start seeMyStudents =======");
        String sql = "";
        logger.info("======= end seeMyStudents =======");
    }
}
