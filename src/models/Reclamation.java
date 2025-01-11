package models;

public class Reclamation {
    private int id;
    private String name;
    private String date;
    private String excuse;
    private int sessionId;
    
    public Reclamation() {
    }
    
    public Reclamation(String name, String excuse, int sessionId) {
        this.name = name;
        this.excuse = excuse;
        this.sessionId = sessionId;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getExcuse() {
        return excuse;
    }
    
    public void setExcuse(String excuse) {
        this.excuse = excuse;
    }
    
    public int getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}