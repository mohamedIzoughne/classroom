package db;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import models.Teacher;

public class TeacherDAO {
    private static Connection connection = DatabaseHelper.connect();

    public static boolean login(String email, String password) {
        String hashedPassword = hashPassword(password);
        String sql = "SELECT * FROM teachers WHERE email = ? AND password = ?";
        return true;
        // try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        //     stmt.setString(1, email);
        //     stmt.setString(2, hashedPassword);

        //     ResultSet rs = stmt.executeQuery();
        //     return rs.next();
        // } catch (SQLException e) {
        //     e.printStackTrace();
        //     return true;
        // }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}