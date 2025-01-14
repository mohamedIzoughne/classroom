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
    

    public static void create(String name, int subjectId, int roomId, String day, String hours, boolean status) throws SQLException {
        String sql = "INSERT INTO sessions (name, subject_id, room_id, day, hours, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, subjectId);
            stmt.setInt(3, roomId);
            stmt.setString(4, day);
            stmt.setString(5, hours);
            stmt.setBoolean(6, status);
            stmt.executeUpdate();
        }
    }
    

    public static void update(int id, String name, int subjectId, int roomId, String day, String hours, boolean status) throws SQLException {
        String sql = "UPDATE sessions SET name = ?, subject_id = ?, room_id = ?, day = ?, hours = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, subjectId);
            stmt.setInt(3, roomId);
            stmt.setString(4, day);
            stmt.setString(5, hours);
            stmt.setBoolean(6, status);
            stmt.setInt(7, id);
            stmt.executeUpdate();
        }
    }
    

    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM sessions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    

    public static Session getById(int id) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToSession(rs);
            }
        }
        return null;
    }
    

    public static List<Session> getAll(String className, String name) throws SQLException {
        List<Session> sessions = new ArrayList<>();
        System.out.println(className);
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