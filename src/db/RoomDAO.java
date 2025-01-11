package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Room;

public class RoomDAO {
    private static final String INSERT_ROOM = "INSERT INTO rooms (name) VALUES (?)";
    private static final String DELETE_ROOM = "DELETE FROM rooms WHERE id = ?";
    private static final String UPDATE_ROOM = "UPDATE rooms SET name = ? WHERE id = ?";
    private static final String SELECT_ALL_ROOMS = "SELECT * FROM rooms";
    
    private Connection connection;
    
    public RoomDAO() throws SQLException {
        this.connection = DatabaseHelper.connect();
    }
    
    public void addRoom(String name) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_ROOM)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }
    
    public void removeRoom(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_ROOM)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public void updateRoom(int id, String name) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_ROOM)) {
            stmt.setString(1, name);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }
    
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_ROOMS)) {
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setName(rs.getString("name"));
                rooms.add(room);
            }
        }
        return rooms;
    }
    
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}