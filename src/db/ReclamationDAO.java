package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Reclamation;

public class ReclamationDAO {
    private Connection connection;
    
    public ReclamationDAO() {
        this.connection = DatabaseHelper.connect();
    }
    
    public boolean insertReclamation(Reclamation reclamation) {
        String query = "INSERT INTO reclamations (name, excuse, session_id) VALUES (?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, reclamation.getName());
            pst.setString(2, reclamation.getExcuse());
            pst.setInt(3, reclamation.getSessionId());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateReclamation(Reclamation reclamation) {
        String query = "UPDATE reclamations SET name = ?, excuse = ?, session_id = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, reclamation.getName());
            pst.setString(2, reclamation.getExcuse());
            pst.setInt(3, reclamation.getSessionId());
            pst.setInt(4, reclamation.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteReclamation(int id) {
        String query = "DELETE FROM reclamations WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Reclamation> getAllReclamations() {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamations";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                reclamations.add(extractReclamationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reclamations;
    }
    
    public List<Reclamation> getReclamationsByClass(int sessionId) {
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
    
    public List<Reclamation> getReclamationsByDay(String dayOfWeek) {
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
    
    private Reclamation extractReclamationFromResultSet(ResultSet rs) throws SQLException {
        Reclamation reclamation = new Reclamation();
        reclamation.setId(rs.getInt("id"));
        reclamation.setName(rs.getString("name"));
        reclamation.setDate(rs.getTimestamp("date"));
        reclamation.setExcuse(rs.getString("excuse"));
        reclamation.setSessionId(rs.getInt("session_id"));
        return reclamation;
    }
}
