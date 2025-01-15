package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Session;

public class SessionDAO {

    private static Connection connection;
    


    static {
        connection = DatabaseHelper.connect();
    }
    

    public static void create(String name, String subjectName, String roomName, String day, String hours, boolean status) throws SQLException {
        String sql = "INSERT INTO sessions (name, subject_name, room_name, day_name, hours, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, subjectName);
            stmt.setString(3, roomName);
            stmt.setString(4, day);
            stmt.setString(5, hours);
            stmt.setBoolean(6, status);
            stmt.executeUpdate();
        }
    }
    

    public static void update(String oldName, String newName, String subjectName, String roomName, String day, String hours, boolean status) throws SQLException {
        String sql = "UPDATE sessions SET name = ?, subject_name = ?, room_name = ?, day_name = ?, hours = ?, status = ? WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, subjectName);
            stmt.setString(3, roomName);
            stmt.setString(4, day);
            stmt.setString(5, hours);
            stmt.setBoolean(6, status);
            stmt.setString(7, oldName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public static void delete(String name) throws SQLException {
        String sql = "DELETE FROM sessions WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }
    



    public static List<Session> getAll(String className, String name) throws SQLException {
        List<Session> sessions = new ArrayList<>();
        System.out.println(className);
        String sql = "SELECT * FROM sessions where session_name = ? in (select subject_name from subjects where class_name = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(1, className);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sessions.add(mapResultSetToSession(rs));
            }
        }
        return sessions;
    }

    public static List<Session> getAllByDay(String className, String dayName) throws SQLException {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions where day_name = ? and subject_name in (select subject_name from subjects where class_name = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dayName);
            stmt.setString(2, className);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sessions.add(mapResultSetToSession(rs));
            }
        }
        return sessions;
    }
    public static List<Session> getAllByDay(String dayName) throws SQLException {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions where day_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dayName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sessions.add(mapResultSetToSession(rs));
            }
        }
        return sessions;
    }

    public static List<Session> getAll(String className) throws SQLException {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions where subject_name in (select subject_name from subjects where class_name = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, className);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sessions.add(mapResultSetToSession(rs));
            }
        }
        return sessions;
    }

    public static int getSessionsCount() throws SQLException {
            String sql = "SELECT COUNT(*) FROM sessions";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        }
    
    

    private static Session mapResultSetToSession(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setName(rs.getString("name"));
        session.setSubjectId(rs.getString("subject_name"));
        session.setRoomName(rs.getString("room_name"));
        session.setDay(rs.getString("day_name"));
        session.setHours(rs.getString("hours"));
        session.setStatus(rs.getBoolean("status"));
        return session;
    }

}