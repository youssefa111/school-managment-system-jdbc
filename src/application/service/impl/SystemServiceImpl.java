package application.service.impl;

import application.entity.Level;
import application.entity.Subject;
import application.service.SystemService;
import utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.logging.Logger;

public class SystemServiceImpl implements SystemService {
    Logger logger = Logger.getLogger(SystemServiceImpl.class.getName());

    @Override
    public LinkedHashSet<Level> getSystemLevels() {
        LinkedHashSet<Level> levels = new LinkedHashSet<>();
        String sql = "SELECT * FROM levels";
        try(
                Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
                ){

            while(rs.next()){
                levels.add(new Level(rs.getInt("id"),rs.getString("level_name")));
            }
        }
        catch (SQLException e){
            logger.info(e.getMessage());
        }

        return levels;
    }

    @Override
    public LinkedHashSet<Subject> getSystemSubjects() {
        LinkedHashSet<Subject> subjects = new LinkedHashSet<>();
        String sql = "SELECT * FROM subjects";
        try(
                Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ){
            while(rs.next()){
                subjects.add(new Subject(rs.getInt("id"),rs.getString("subject_name")));
            }
        }
        catch (SQLException e){
            logger.info(e.getMessage());
        }
        return subjects;
    }
}
