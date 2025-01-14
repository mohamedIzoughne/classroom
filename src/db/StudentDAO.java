package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.Student;

public class StudentDAO {
    private static Connection connection = DatabaseHelper.connect();

    public static void addStudent(String name, String email, String phoneNumber, String gender, String dateOfBirth,
            String className) {
        if (connection != null) {
            String query = "INSERT INTO students (name, email, phoneNumber, gender, date_of_birth, class_name) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, phoneNumber);
                statement.setString(4, gender);
                statement.setString(5, dateOfBirth);

                if (className != null) {
                    statement.setString(6, className);
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

    public static void updateStudent(int studentId, String name, String email, String phoneNumber, String gender,
            Date dateOfBirth, Integer classId) {
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

    public static List<Student> getStudentsByClassName(String className) throws SQLException {
        List<Student> students = new ArrayList<>();
        if (connection != null) {
            String query = "SELECT * from students where class_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, className);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Student student = new Student();
                    student.setFullName(resultSet.getString("name"));
                    student.setEmail(resultSet.getString("email"));
                    student.setPhoneNumber(resultSet.getString("phoneNumber"));
                    student.setGender(resultSet.getString("gender"));
                    student.setDateOfBirth(resultSet.getString("date_of_birth"));
                    // student.set(resultSet.getInt("class_name"));
                    students.add(student);
                }
            } catch (SQLException e) {
                System.out.println("Error getting students by class name: " + e.getMessage());
            }
        }
        return students;
    }

    public static int[] getStudentCountsByGender() {
        int[] counts = new int[] { 0, 0 };
        if (connection != null) {
            String query = "SELECT gender, COUNT(*) as count FROM students GROUP BY gender";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String gender = resultSet.getString("gender");
                    int count = resultSet.getInt("count");
                    if ("Male".equalsIgnoreCase(gender)) {
                        counts[0] = count;
                    } else if ("Female".equalsIgnoreCase(gender)) {
                        counts[1] = count;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error getting number of students by gender: " + e.getMessage());
            }
        }
        return counts;
    }

    public static int[] getStudentCountsByGender(String className) {
        int[] counts = new int[] { 0, 0 };
        if (connection != null) {
            String query = "SELECT gender, COUNT(*) as count FROM students where class_name = ? GROUP BY gender";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, className);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String gender = resultSet.getString("gender");
                    int count = resultSet.getInt("count");
                    if ("Male".equalsIgnoreCase(gender)) {
                        counts[0] = count;
                    } else if ("Female".equalsIgnoreCase(gender)) {
                        counts[1] = count;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error getting number of students by gender: " + e.getMessage());
            }
        }
        return counts;
    }

    public static String getNumberOfStudents() {
        String count = "0";
        if (connection != null) {
            String query = "SELECT COUNT(name) as count FROM students";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getString("count");
                }
            } catch (SQLException e) {
                System.out.println("Error getting number of students: " + e.getMessage());
            }
        }
        return count;
    }

    public static ArrayList<Student> getStudentByName(String name, String className) {
        ArrayList<Student> students = new ArrayList<>();
        if (connection != null) {
            String query = "SELECT * FROM students WHERE name LIKE ? AND class_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, "%" + name + "%");
                statement.setString(2, className);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Student student = new Student();
                    student.setFullName(resultSet.getString("name"));
                    student.setGender(resultSet.getString("gender"));
                    student.setDateOfBirth(resultSet.getString("date_of_birth"));
                    student.setEmail(resultSet.getString("email"));
                    student.setPhoneNumber(resultSet.getString("phoneNumber"));
                    students.add(student);
                }
            } catch (SQLException e) {
                System.out.println("Error getting student details: " + e.getMessage());
            }
        }
        return students;
    }
}