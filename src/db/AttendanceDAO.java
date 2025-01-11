package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AttendanceDAO {
    private Connection connection;
    
    public AttendanceDAO() {
        this.connection = DatabaseHelper.connect();
    }
    
    public void markAttendance(int studentId, int sessionId, boolean status, int weekNumber) throws SQLException {
        String sql = "INSERT INTO presence (student_id, session_id, status, week_number) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, sessionId);
            stmt.setBoolean(3, status);
            stmt.setInt(4, weekNumber);
            stmt.executeUpdate();
        }
    }
    
    public boolean getAttendanceStatus(int studentId, int sessionId, int weekNumber) throws SQLException {
        String sql = "SELECT status FROM presence WHERE student_id = ? AND session_id = ? AND week_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, sessionId);
            stmt.setInt(3, weekNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("status");
            }
            return false;
        }
    }
    
    public Map<Integer, Integer> getStudentAttendanceStats(int studentId, int weekNumber) throws SQLException {
        Map<Integer, Integer> stats = new HashMap<>();
        String sql = "SELECT session_id, COUNT(*) as attendance_count FROM presence WHERE student_id = ? AND week_number = ? AND status = true GROUP BY session_id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, weekNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                stats.put(rs.getInt("session_id"), rs.getInt("attendance_count"));
            }
        }
        return stats;
    }
    
    public List<Map<String, Object>> getDailyAttendance(String day, int weekNumber) throws SQLException {
        List<Map<String, Object>> attendanceList = new ArrayList<>();
        String sql = "SELECT p.*, s.name as student_name, ses.name as session_name " +
                    "FROM presence p " +
                    "JOIN students s ON p.student_id = s.id " +
                    "JOIN sessions ses ON p.session_id = ses.id " +
                    "WHERE ses.day = ? AND p.week_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, day);
            stmt.setInt(2, weekNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("student_id", rs.getInt("student_id"));
                record.put("student_name", rs.getString("student_name"));
                record.put("session_id", rs.getInt("session_id"));
                record.put("session_name", rs.getString("session_name"));
                record.put("status", rs.getBoolean("status"));
                attendanceList.add(record);
            }
        }
        return attendanceList;
    }
    
    public List<Map<String, Object>> getClassAttendance(int classId, int weekNumber) throws SQLException {
        List<Map<String, Object>> classAttendance = new ArrayList<>();
        String sql = "SELECT p.*, s.name as student_name, ses.name as session_name " +
                    "FROM presence p " +
                    "JOIN students s ON p.student_id = s.id " +
                    "JOIN sessions ses ON p.session_id = ses.id " +
                    "WHERE s.class_id = ? AND p.week_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            stmt.setInt(2, weekNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("student_id", rs.getInt("student_id"));
                record.put("student_name", rs.getString("student_name"));
                record.put("session_id", rs.getInt("session_id"));
                record.put("session_name", rs.getString("session_name"));
                record.put("status", rs.getBoolean("status"));
                attendanceList.add(record);
            }
        }
        return classAttendance;
    }
    
    public void deleteAttendance(int studentId, int sessionId, int weekNumber) throws SQLException {
        String sql = "DELETE FROM presence WHERE student_id = ? AND session_id = ? AND week_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, sessionId);
            stmt.setInt(3, weekNumber);
            stmt.executeUpdate();
        }
    }
    
    public void deleteAllAttendance(int weekNumber) throws SQLException {
        String sql = "DELETE FROM presence WHERE week_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, weekNumber);
            stmt.executeUpdate();
        }
    }
}