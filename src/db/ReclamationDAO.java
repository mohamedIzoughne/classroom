package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

import models.Reclamation;

public class ReclamationDAO {
    private static Connection connection = DatabaseHelper.connect();

    // public static boolean insertReclamation(Reclamation reclamation) {
    // String query = "INSERT INTO reclamations (name, excuse, session_id) VALUES
    // (?, ?, ?)";
    // try (PreparedStatement pst = connection.prepareStatement(query)) {
    // pst.setString(1, reclamation.getName());
    // pst.setString(2, reclamation.getExcuse());
    // pst.setInt(3, reclamation.getSessionId());
    // return pst.executeUpdate() > 0;
    // } catch (SQLException e) {
    // e.printStackTrace();
    // return false;
    // }
    // }

    // public static boolean updateReclamation(Reclamation reclamation) {
    // String query = "UPDATE reclamations SET name = ?, excuse = ?, session_id = ?
    // WHERE id = ?";
    // try (PreparedStatement pst = connection.prepareStatement(query)) {
    // pst.setString(1, reclamation.getName());
    // pst.setString(2, reclamation.getExcuse());
    // pst.setInt(3, reclamation.getSessionId());
    // pst.setInt(4, reclamation.getId());
    // return pst.executeUpdate() > 0;
    // } catch (SQLException e) {
    // e.printStackTrace();
    // return false;
    // }
    // }

    public static boolean deleteReclamation(int id) {
        String query = "DELETE FROM reclamations WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Reclamation> getAllReclamations(String studentName, String sessionName) {
        List<Reclamation> reclamations = new ArrayList<>();
        
        String query = "SELECT * FROM reclamations where name LIKE ? and session_name = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, "%" + studentName + "%");
            st.setString(2, sessionName);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                reclamations.add(extractReclamationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reclamations;
    }

    public static List<Reclamation> getReclamationsByClass(int sessionId) {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamations WHERE session_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, sessionId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                reclamations.add(extractReclamationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reclamations;
    }

    public static List<Reclamation> getReclamationsByDay(String dayOfWeek) throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamations WHERE DAYNAME(date) = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, dayOfWeek);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                reclamations.add(extractReclamationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reclamations;
    }

    private static Reclamation extractReclamationFromResultSet(ResultSet rs) throws SQLException {
        Reclamation reclamation = new Reclamation();

        reclamation.setStudentName(rs.getString("name"));
        reclamation.setDate(rs.getString("date"));
        reclamation.setExcuse(rs.getString("excuse"));
        reclamation.setSession(rs.getString("session_name"));

        return reclamation;
    }

    public static List<Reclamation> getReclamationsBySessionAndClass(String sessionName) {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamations WHERE session_name = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, sessionName);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                reclamations.add(extractReclamationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reclamations;
    }

}