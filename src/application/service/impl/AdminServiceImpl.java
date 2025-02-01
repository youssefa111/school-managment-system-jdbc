package application.service.impl;

import application.entity.*;
import application.service.AdminService;
import application.service.AuthService;
import utils.CurrentSession;
import utils.DatabaseUtil;
import utils.JsonConverterUtil;
import utils.ValidatorsUtil;
import utils.authorization.IsAdmin;
import utils.constants.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AdminServiceImpl implements AdminService , AuthService<Admin> {
    Logger logger = Logger.getLogger(AdminServiceImpl.class.getName());

    @Override
    public void login(String username, String password) {
        logger.info("======= start login =======");
        String sql = "SELECT * FROM admins WHERE username= ? AND password= ?";

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
                        currentSession.setCurrentRole(Role.ADMIN);
                        currentSession.setUserId(rs.getInt("id"));

                        Admin admin = new Admin();
                        admin.setId(rs.getInt("id"));
                        admin.setName(rs.getString("name"));
                        admin.setEmail(rs.getString("email"));

                        currentSession.setUser(admin);
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
    public void register(Admin entity) {
        logger.info("======= start register =======");
        String sql = "INSERT INTO admins(name,email,password) VALUES (?,?,?)";
        if (ValidatorsUtil.FormValidator.isAdminRegisterValid(entity)){
            try(
                    Connection conn = DatabaseUtil.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql);
            ) {
                ps.setString(1,entity.getName());
                ps.setString(2,entity.getEmail());
                ps.setString(3,entity.getPassword());
                ps.executeUpdate();
                logger.info("Admin register successful!!");
            } catch (SQLException e) {
                logger.warning(e.getMessage());
            }
        }
        else {
            logger.info("Admins register failed, please  validate your data and try again");
        }
        logger.info("======= end register =======");
    }

    // =============== Subject Section ===========================

    @IsAdmin
    @Override
    public void addSubject(String subjectName) {
        logger.info("======= start addSubject =======");
        String sql = "INSERT INTO subjects(subject_name) VALUES (?)";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ){
            ps.setString(1,subjectName);
            ps.executeUpdate();
            logger.info("Subject add successful!!");
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end addSubject =======");
    }

    @IsAdmin
    @Override
    public void deleteSubject(int subjectId) {
        logger.info("======= start deleteSubject =======");
        String sql = "DELETE FROM subjects WHERE id = ?";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setInt(1,subjectId);
            ps.executeUpdate();
            logger.info("Subject delete successful!!");
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end deleteSubject =======");
    }

    @IsAdmin
    @Override
    public void updateSubject(Subject subject) {
        logger.info("======= start updateSubject =======");
        String sql = "UPDATE subjects SET subject_name = ? WHERE id = ?";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setString(1,subject.levelName());
            ps.setInt(2,subject.id());
            ps.executeUpdate();
            logger.info("Subject update successful!!");
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end updateSubject =======");
    }

    @IsAdmin
    @Override
    public void getAllSubject() {
        logger.info("======= start getAllSubject =======");
        String sql = "SELECT * FROM subjects";
        try(
                Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
        ){
           try(ResultSet result = stmt.executeQuery(sql)) {
               List<Subject> subjectList = new ArrayList<>();
               while(result.next()) {
                   subjectList.add(new Subject(result.getInt("id"),result.getString("subject_name")));
               }
               logger.info("getAllSubject => " + JsonConverterUtil.convertToJson(subjectList));
           }
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end getAllSubject =======");
    }

    // =============== Level Section ===========================

    @IsAdmin
    @Override
    public void addLevel(String levelName) {
        logger.info("======= start addLevel =======");
        String sql = "INSERT INTO levels(level_name) VALUES (?)";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setString(1,levelName);
            ps.executeUpdate();
            logger.info("Level add successful!!");
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end addLevel =======");
    }

    @IsAdmin
    @Override
    public void deleteLevel(int levelId) {
        logger.info("======= start deleteLevel =======");
        String sql = "DELETE FROM levels WHERE id = ?";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setInt(1,levelId);
            ps.executeUpdate();
            logger.info("Level delete successful!!");
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end deleteLevel =======");
    }

    @IsAdmin
    @Override
    public void updateLevel(Level level) {
        logger.info("======= start updateLevel =======");
        String sql = "UPDATE levels SET level_name = ? WHERE id = ?";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setString(1,level.levelName());
            ps.setInt(2,level.id());
            ps.executeUpdate();
            logger.info("Level update successful!!");
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end updateLevel =======");
    }

    @IsAdmin
    @Override
    public void getAllLevel() {
        logger.info("======= start getAllLevel =======");
        String sql = "SELECT * FROM levels";
        try(
                Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
        ){
            try(ResultSet result = stmt.executeQuery(sql)) {
                List<Level> levelList = new ArrayList<>();
                while(result.next()) {
                    levelList.add(new Level(result.getInt("id"),result.getString("level_name")));
                }
                logger.info("getAllLevel => " + JsonConverterUtil.convertToJson(levelList));
            }
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end getAllLevel =======");
    }

    // =============== Student Section ===========================

    @IsAdmin
    @Override
    public void getAllStudent() {
        logger.info("======= start getAllStudent =======");
        String sql = "SELECT * FROM students";
        try(
                Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
        ){
            try(ResultSet result = stmt.executeQuery(sql)) {
                List<Student> studentList = new ArrayList<>();
                while(result.next()) {
                    studentList.add(new Student(
                            result.getInt("id"),
                            result.getString("email"),
                            result.getString("name"),
                            result.getInt("age"),
                            result.getString("address"),
                            result.getDate("join_date").toLocalDate(),
                            result.getInt("level_id")
                            ));
                }
                logger.info("getAllStudent => " + JsonConverterUtil.convertToJson(studentList));
            }
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end getAllStudent =======");
    }

    @IsAdmin
    @Override
    public void deleteStudent(int studentId) {
        logger.info("======= start deleteStudent =======");
        String sql = "DELETE FROM students WHERE id = ?";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ){
            ps.setInt(1,studentId);
            var result = ps.executeUpdate();
            logger.info("deleteStudent with id: "+studentId +" => " + result);
        }
        catch(SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end deleteStudent =======");
    }

    @IsAdmin
    @Override
    public void deleteAllStudent(int[] studentId) {
        logger.info("======= start deleteAllStudent =======");
        logger.info("======= end deleteAllStudent =======");
    }

    @Override
    public void addStudentToClass(int studentId, int classId) {

    }

    // =============== Teacher Section ===========================

    @IsAdmin
    @Override
    public void getAllTeacher() {
        logger.info("======= start getAllTeacher =======");
        String sql = "SELECT * FROM teachers";
        try(
                Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
        ){
            try(ResultSet result = stmt.executeQuery(sql)) {
                List<Teacher> teacherList = new ArrayList<>();
                while(result.next()) {
                    teacherList.add(new Teacher(
                            result.getInt("id"),
                            result.getString("email"),
                            result.getString("name"),
                            result.getInt("age"),
                            result.getString("address"),
                            result.getDate("join_date").toLocalDate(),
                            result.getInt("subject_id")
                    ));
                }
                logger.info("getAllTeacher => " + JsonConverterUtil.convertToJson(teacherList));
            }
        }
        catch (SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end getAllTeacher =======");
    }

    @IsAdmin
    @Override
    public void deleteTeacher(int teacherId) {
        logger.info("======= start deleteTeacher =======");
        String sql = "DELETE FROM teachers WHERE id = ?";
        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setInt(1,teacherId);
            var result = ps.executeUpdate();
            logger.info("deleteTeacher with id: "+teacherId +" => " + result);
        }
        catch(SQLException e){
            logger.warning(e.getMessage());
        }
        logger.info("======= end deleteTeacher =======");
    }

    @IsAdmin
    @Override
    public void deleteAllTeachers(int[] teacherIds) {
        logger.info("======= start deleteAllTeachers =======");
        logger.info("======= end deleteAllTeachers =======");
    }

    @Override
    public void addTeacherToClass(int teacherId, int classId) {

    }

    // =============== Class Section ===========================

    @Override
    public void getAllClasses() {

    }

    @Override
    public void addClass(String className, int capacity) {

    }

    @Override
    public void deleteClass(int classId) {

    }

    @Override
    public void updateClass(int classId, int capacity) {

    }


}
