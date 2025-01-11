package db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class StudentDAO {
    private static Connection connection = DatabaseHelper.connect();

    public static void addStudent(String name, String email, String phoneNumber, String gender, Date dateOfBirth, Integer classId) {
        if (connection != null) {
            String query = "INSERT INTO students (name, email, phoneNumber, gender, date_of_birth, class_id) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, phoneNumber);
                statement.setString(4, gender);
                statement.setDate(5, dateOfBirth);
                if (classId != null) {
                    statement.setInt(6, classId);
                } else {
                    statement.setNull(6, java.sql.Types.INTEGER);
                }
                statement.executeUpdate();
                System.out.println("Student added successfully.");
            } catch (SQLException e) {
                System.out.println("Error adding student: " + e.getMessage());
            }
        }
    }

    public static void deleteStudent(int studentId) {
        if (connection != null) {
            String query = "DELETE FROM students WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, studentId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Student deleted successfully.");
                } else {
                    System.out.println("No student found with ID: " + studentId);
                }
            } catch (SQLException e) {
                System.out.println("Error deleting student: " + e.getMessage());
            }
        }
    }

    public static void updateStudent(int studentId, String name, String email, String phoneNumber, String gender, Date dateOfBirth, Integer classId) {
        if (connection != null) {
            String query = "UPDATE students SET name = ?, email = ?, phoneNumber = ?, gender = ?, date_of_birth = ?, class_id = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, phoneNumber);
                statement.setString(4, gender);
                statement.setDate(5, dateOfBirth);
                if (classId != null) {
                    statement.setInt(6, classId);
                } else {
                    statement.setNull(6, java.sql.Types.INTEGER);
                }
                statement.setInt(7, studentId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Student updated successfully.");
                } else {
                    System.out.println("No student found with ID: " + studentId);
                }
            } catch (SQLException e) {
                System.out.println("Error updating student: " + e.getMessage());
            }
        }
    }
}