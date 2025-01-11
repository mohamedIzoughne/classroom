package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    private static Connection connection = DatabaseHelper.connect();
    
    public static void addSubject(String name, String className, String description) throws SQLException {
        String getClassIdSql = "SELECT id FROM classes WHERE name = ?";
        int classId;
        try (PreparedStatement classStmt = connection.prepareStatement(getClassIdSql)) {
            classStmt.setString(1, className);
            ResultSet rs = classStmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Class not found: " + className);
            }
            classId = rs.getInt("id");
        }

        String sql = "INSERT INTO subjects (name, class_id, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, classId);
            stmt.setString(3, description);
            stmt.executeUpdate();
        }
    }
    
    public static void removeSubject(int id) throws SQLException {
        String sql = "DELETE FROM subjects WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public static void updateSubject(int id, String name, int classId, String description) throws SQLException {
        String sql = "UPDATE subjects SET name = ?, class_id = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, classId);
            stmt.setString(3, description);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }
    
    public static Subject getSubject(int id) throws SQLException {
        String sql = "SELECT * FROM subjects WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Subject(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("class_id"),
                    rs.getString("description")
                );
            }
            return null;
        }
    }
    
    public static List<Subject> getAllSubjects() throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                subjects.add(new Subject(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("class_id"),
                    rs.getString("description")
                ));
            }
        }
        return subjects;
    }
}

class Subject {
    private int id;
    private String name;
    private int classId;
    private String description;
    
    public Subject(int id, String name, int classId, String description) {
        this.id = id;
        this.name = name;
        this.classId = classId;
        this.description = description;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public int getClassId() { return classId; }
    public String getDescription() { return description; }
    
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setClassId(int classId) { this.classId = classId; }
    public void setDescription(String description) { this.description = description; }
}