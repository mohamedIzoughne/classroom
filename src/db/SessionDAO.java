package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO {
    private Connection connection;
    
    public SessionDAO() {
        this.connection = DatabaseHelper.connect();
    }
    
    public void create(String name, int subjectId, int roomId, String day, String hours, boolean status) throws SQLException {
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
    
    public void update(int id, String name, int subjectId, int roomId, String day, String hours, boolean status) throws SQLException {
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
    
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM sessions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public Session getById(int id) throws SQLException {
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
    
    public List<Session> getAll() throws SQLException {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sessions.add(mapResultSetToSession(rs));
            }
        }
        return sessions;
    }
    
    private Session mapResultSetToSession(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setId(rs.getInt("id"));
        session.setName(rs.getString("name"));
        session.setSubjectId(rs.getInt("subject_id"));
        session.setRoomId(rs.getInt("room_id"));
        session.setDay(rs.getString("day"));
        session.setHours(rs.getString("hours"));
        session.setStatus(rs.getBoolean("status"));
        return session;
    }
}
