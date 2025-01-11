package models;

public class Attendance {
    private int id;
    private int studentId;
    private int sessionId;
    private boolean status;
    private int weekNumber;
    
    public Attendance() {
    }
    
    public Attendance(int studentId, int sessionId, boolean status, int weekNumber) {
        this.studentId = studentId;
        this.sessionId = sessionId;
        this.status = status;
        this.weekNumber = weekNumber;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
    public boolean isStatus() {
        return status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public int getWeekNumber() {
        return weekNumber;
    }
    
    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }
}