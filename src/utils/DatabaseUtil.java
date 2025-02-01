package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseUtil {

    static Logger logger = Logger.getLogger(DatabaseUtil.class.getName());

    private DatabaseUtil() {}

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            String username = "postgres";
            String password = "postgres";
            String url = "jdbc:postgresql://localhost:5432/school_DB";
            return DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    public static void initDatabase() throws SQLException {
        try(
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {
            for (String sqlStatement : tablesSqlList){
                statement.execute(sqlStatement);
            }
            logger.info("Database initialized Successfully");
        }catch (SQLException e){
            logger.warning(e.getMessage());
        }
    }

    private static final String createStudentTable = """
            CREATE TABLE IF NOT EXISTS students (
            id SERIAL PRIMARY KEY,
            name VARCHAR(100),
            email VARCHAR(100),
            password VARCHAR(100),
            age INT,
            address VARCHAR(255),
            level_id INT,
            class_id INT,
            join_date date
            )
            """;
    private static final String createTeacherTable = """
            CREATE TABLE IF NOT EXISTS teachers (
            id SERIAL PRIMARY KEY,
            name VARCHAR(100),
            email VARCHAR(100),
            password VARCHAR(100),
            age INT,
            address VARCHAR(255),
            subject_id INT,
            join_date date
            )
            """;
    private static final String createAdminTable = """
            CREATE TABLE IF NOT EXISTS admins (
            id SERIAL PRIMARY KEY,
            name VARCHAR(100),
            email VARCHAR(100),
            password VARCHAR(100)
            )
            """;
    private static final String createSubjectTable = """
            CREATE TABLE IF NOT EXISTS subjects (
            id SERIAL PRIMARY KEY,
            subject_name VARCHAR(100)
            )
            """;
    private static final String createClassTable = """
            CREATE TABLE IF NOT EXISTS classes (
            id SERIAL PRIMARY KEY,
            name VARCHAR(10),
            capacity INT
            )
            """;
    private static final String createClassTeacherTable = """
            CREATE TABLE IF NOT EXISTS teacher_classes (
            teacher_id INT,
            class_id INT
            )
            """;
    private static final String createLevelTable = """
            CREATE TABLE IF NOT EXISTS levels (
            id SERIAL PRIMARY KEY,
            level_name VARCHAR(100)
            )
            """;
    private static final String createSubjectStudentTable = """
            CREATE TABLE IF NOT EXISTS subject_student (
            ss_id SERIAL PRIMARY KEY,
            subject_id INT,
            student_id INT
            )
            """;
    private static final String createGradesTable = """
            CREATE TABLE IF NOT EXISTS grades (
            id SERIAL PRIMARY KEY,
            ss_id INT,
            grade NUMERIC(4,1)
            )
            """;
    private static final String[] tablesSqlList = new String[] {
            createAdminTable,
            createClassTable,
            createSubjectTable,
            createLevelTable,
            createStudentTable,
            createTeacherTable,
            createClassTeacherTable,
            createSubjectStudentTable,
            createGradesTable
    };
}
