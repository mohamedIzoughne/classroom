package db;

import java.sql.*;
import java.util.Map;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import models.Attendance;
import models.Student;

public class AttendanceDAO {
    private static Connection connection = DatabaseHelper.connect();

    public static void saveAttendance(ObservableList<Attendance> attendanceList, String sessionName)
            throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO attendance (student_name, session_name, date, status) VALUES ");
        List<String> values = new ArrayList<>();
        
        for (int i = 0; i < attendanceList.size(); i++) {
            values.add("(?, ?, ?, ?)");
        }
        query.append(String.join(", ", values));

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int paramIndex = 1;
            for (Attendance attendance : attendanceList) {
                stmt.setString(paramIndex++, attendance.getStudentName());
                stmt.setString(paramIndex++, sessionName);
                stmt.setDate(paramIndex++, java.sql.Date.valueOf(java.time.LocalDate.now()));
                stmt.setInt(paramIndex++, attendance.getAttendant().get() ? 1 : 0);
            }
            System.out.println("Executing query: " + stmt.toString());
            stmt.executeUpdate();
        }
    }    public static Map<String, Map<String, Integer>> getLastWeekAttendanceRateByClass(String className)
            throws SQLException {
        Map<String, Map<String, Integer>> results = new HashMap<>();

        String query = "SELECT DAYNAME(a.date) AS day_name, " +
                "COUNT(CASE WHEN a.status = 1 THEN 1 END) AS present_count, " +
                "COUNT(CASE WHEN a.status = 0 THEN 1 END) AS absent_count, " +
                "COUNT(*) AS total_count " +
                "FROM attendance a " +
                "JOIN sessions s ON a.session_name = s.name " +
                "JOIN students st ON a.student_name = st.name " +
                "WHERE a.date BETWEEN DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) + 7 DAY) " +
                "AND DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) + 1 DAY) " +
                "AND st.class_name = ? " +
                "GROUP BY DAYNAME(a.date) " +
                "ORDER BY FIELD(DAYNAME(a.date), 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, className);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Integer> row = new HashMap<>();
                    row.put("present_count", rs.getInt("present_count"));
                    row.put("absent_count", rs.getInt("absent_count"));
                    row.put("total_count", rs.getInt("total_count"));
                    results.put(rs.getString("day_name"), row);
                }
            }
        }

        return results;
    }

    public static Map<String, Map<String, Integer>> getLastWeekAttendanceRateByClass(String className, int weekNumber)
            throws SQLException {
        Map<String, Map<String, Integer>> results = new HashMap<>();

        String query = "SELECT DAYNAME(a.date) AS day_name, " +
                "COUNT(CASE WHEN a.status = 1 THEN 1 END) AS present_count, " +
                "COUNT(CASE WHEN a.status = 0 THEN 1 END) AS absent_count, " +
                "COUNT(*) AS total_count " +
                "FROM attendance a " +
                "JOIN sessions s ON a.session_name = s.name " +
                "JOIN students st ON a.student_name = st.name " +
                "WHERE WEEK(a.date, 1) = ? " +
                "AND st.class_name = ? " +
                "GROUP BY DAYNAME(a.date) " +
                "ORDER BY FIELD(DAYNAME(a.date), 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, weekNumber);
            stmt.setString(2, className);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Integer> row = new HashMap<>();
                    row.put("present_count", rs.getInt("present_count"));
                    row.put("absent_count", rs.getInt("absent_count"));
                    row.put("total_count", rs.getInt("total_count"));
                    results.put(rs.getString("day_name"), row);
                }
            }
        }

        return results;
    }

    public static Map<String, Map<String, Integer>> getLastWeekAttendanceRateByClass(int weekNumber)
            throws SQLException {
        Map<String, Map<String, Integer>> results = new HashMap<>();

        String query = "SELECT DAYNAME(a.date) AS day_name, " +
                "COUNT(CASE WHEN a.status = 1 THEN 1 END) AS present_count, " +
                "COUNT(CASE WHEN a.status = 0 THEN 1 END) AS absent_count, " +
                "COUNT(*) AS total_count " +
                "FROM attendance a " +
                "JOIN sessions s ON a.session_name = s.name " +
                "JOIN students st ON a.student_name = st.name " +
                "WHERE WEEK(a.date) = ? " +
                "GROUP BY DAYNAME(a.date) " +
                "ORDER BY FIELD(DAYNAME(a.date), 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, weekNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Integer> row = new HashMap<>();
                    row.put("present_count", rs.getInt("present_count"));
                    row.put("absent_count", rs.getInt("absent_count"));
                    row.put("total_count", rs.getInt("total_count"));
                    results.put(rs.getString("day_name"), row);
                }
            }
        }

        return results;
    }

    public static Map<String, Map<String, Integer>> getLastWeekAttendanceRateByClass()
            throws SQLException {
        Map<String, Map<String, Integer>> results = new HashMap<>();

        String query = "SELECT DAYNAME(a.date) AS day_name, " +
                "COUNT(CASE WHEN a.status = 1 THEN 1 END) AS present_count, " +
                "COUNT(CASE WHEN a.status = 0 THEN 1 END) AS absent_count, " +
                "COUNT(*) AS total_count " +
                "FROM attendance a " +
                "JOIN sessions s ON a.session_name = s.name " +
                "JOIN students st ON a.student_name = st.name " +
                "WHERE a.date BETWEEN DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) + 7 DAY) " +
                "AND DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) + 1 DAY) " +
                "GROUP BY DAYNAME(a.date) " +
                "ORDER BY FIELD(DAYNAME(a.date), 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Integer> row = new HashMap<>();
                    row.put("present_count", rs.getInt("present_count"));
                    row.put("absent_count", rs.getInt("absent_count"));
                    row.put("total_count", rs.getInt("total_count"));
                    results.put(rs.getString("day_name"), row);
                }
            }
        }

        return results;
    }

    public static Map<String, Integer> getWeeklyAttendanceByStudent(String studentName) throws SQLException {
        Map<String, Integer> results = new LinkedHashMap<>();

        String query = "SELECT " +
                "DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) + 7 - seq.day_index DAY), '%W') AS day_name, "
                +
                "COALESCE( " +
                "    MAX(CASE " +
                "        WHEN a.status = 1 THEN 1 " +
                "        WHEN a.status = 0 THEN 0 " +
                "    END), " +
                "    2 " +
                ") AS attendance_status " +
                "FROM " +
                "    (SELECT 0 AS day_index UNION ALL SELECT 1 UNION ALL SELECT 2 " +
                "     UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) AS seq " +
                "LEFT JOIN attendance a " +
                "    ON a.date = DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) + 7 - seq.day_index DAY) " +
                "    AND a.student_name = ? " +
                "GROUP BY seq.day_index " +
                "ORDER BY seq.day_index";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.put(rs.getString("day_name"), rs.getInt("attendance_status"));
                }
            }
        }

        return results;
    }

    public static List<Attendance> getAttendanceRatesForStudents(List<Student> students, String sessionName)
            throws SQLException {
        if (sessionName == null || sessionName.trim().isEmpty()) {
            return getAttendanceRatesForStudents(students);
        }
        
        List<Attendance> attendances = new ArrayList<>();

        // Build the student names list for IN clause
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < students.size(); i++) {
            placeholders.append(i > 0 ? ",?" : "?");
        }
        String query = "SELECT " +
                "students.name AS student_name, " +
                "COUNT(CASE WHEN a.status = 1 THEN 1 END) AS present_count, " +
                "COUNT(a.session_name) AS total_sessions " +
                "FROM students " +
                "LEFT JOIN attendance a ON students.name = a.student_name " +
                "AND a.session_name = ? " +
                "WHERE students.name IN (" + placeholders.toString() + ") " +
                "GROUP BY students.name";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Set the session name
            int paramIndex = 1;
            stmt.setString(paramIndex, sessionName);

            // Set all student names in the prepared statement
            for (Student student : students) {
                stmt.setString(++paramIndex, student.getFullName());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String studentName = rs.getString("student_name");
                    int presentCount = rs.getInt("present_count");
                    int totalSessions = rs.getInt("total_sessions");

                    double attendanceRate = totalSessions > 0 ? Math.round((double) presentCount / totalSessions * 10000.0) / 100.0 : 100.0;

                    Attendance attendance = new Attendance(studentName, attendanceRate, false);
                    attendances.add(attendance);
                }
            }
        }

        return attendances;

    }
    public static List<Attendance> getAttendanceRatesForStudents(List<Student> students)
            throws SQLException {
        List<Attendance> attendances = new ArrayList<>();

        // Build the student names list for IN clause
            StringBuilder placeholders = new StringBuilder();
            if (students.size() > 0) {
                for (int i = 0; i < students.size(); i++) {
                    placeholders.append(i > 0 ? ",?" : "?");
                }
            } else {
                placeholders.append("''");
            }
            String query = "SELECT " +
                    "students.name AS student_name, " +
                    "COUNT(CASE WHEN a.status = 1 THEN 1 END) AS present_count, " +
                    "COUNT(a.session_name) AS total_sessions " +
                    "FROM students " +
                    "LEFT JOIN attendance a ON students.name = a.student_name " +
                    "WHERE students.name IN (" + placeholders.toString() + ") " +
                    "GROUP BY students.name";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                // Set the session name
                int paramIndex = 0;

                // Set all student names in the prepared statement
                for (Student student : students) {
                    stmt.setString(++paramIndex, student.getFullName());
                }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String studentName = rs.getString("student_name");
                    int presentCount = rs.getInt("present_count");
                    int totalSessions = rs.getInt("total_sessions");

                    double attendanceRate = totalSessions > 0 ? Math.round((double) presentCount / totalSessions * 10000.0) / 100.0 : 100.0;

                    Attendance attendance = new Attendance(studentName, attendanceRate, false);
                    attendances.add(attendance);
                }
            }
        }

        return attendances;

    }
}