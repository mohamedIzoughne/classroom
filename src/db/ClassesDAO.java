package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Classes;

public class ClassesDAO {
    private static Connection connection = DatabaseHelper.connect();
    
    public static void addClass(String name, String filiere, String description) throws SQLException {
        if (name.length() > 50 || filiere.length() > 50 || description.length() > 255) {
            throw new SQLException("Data too long - name and filiere must be <= 50 chars, description <= 255 chars");
        }
        String sql = "INSERT INTO classes (name, filiere, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            System.out.println("Prepared Statement: " + stmt.toString());
            stmt.setString(1, name);
            stmt.setString(2, filiere);
            stmt.setString(3, description);
            System.out.println("Prepared Statement: " + stmt.toString());

            stmt.executeUpdate();
        }
    }
    
    public static void updateClass(int id, String name, String filiere, String description) throws SQLException {
        String sql = "UPDATE classes SET name = ?, filiere = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, filiere);
            stmt.setString(3, description);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }

    public static void updateClassByName(String oldName, String newName, String filiere, String description) throws SQLException {
            String sql = "UPDATE classes SET name = ?, filiere = ?, description = ? WHERE name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, newName);
                stmt.setString(2, filiere);
                stmt.setString(3, description);
                stmt.setString(4, oldName);
                stmt.executeUpdate();
            }
        }
    
    
    public static void removeClass(int id) throws SQLException {
        String sql = "DELETE FROM classes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public static void removeClassByName(String name) throws SQLException {
            String sql = "DELETE FROM classes WHERE name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.executeUpdate();
            }
        }
    
    
    public static void addStudentToClass(int studentId, int classId) throws SQLException {
        String sql = "UPDATE students SET class_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            stmt.setInt(2, studentId);
            stmt.executeUpdate();
        }
    }
    
    public static void removeStudentFromClass(int studentId) throws SQLException {
        String sql = "UPDATE students SET class_id = NULL WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
        }
    }
    
    public static List<Integer> getStudentsInClass(int classId) throws SQLException {
        List<Integer> studentIds = new ArrayList<>();
        String sql = "SELECT id FROM students WHERE class_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                studentIds.add(rs.getInt("id"));
            }
        }
        return studentIds;
    }

    public static List<Classes> getClasses() throws SQLException {
        List<Classes> classes = new ArrayList<Classes>();
        String sql = "SELECT name, filiere, description FROM classes";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Classes classInfo = new Classes(
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("filiere")
                );
                classes.add(classInfo);
            }
        }
        return classes;
    }
}