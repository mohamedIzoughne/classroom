package models;

public class Reclamation {
    private String studentName;
    private String group = "pas devise";
    private String date;
    private String excuse;
    private String session;

    public Reclamation() {}
    public Reclamation(String studentName, String group, String date, String excuse, String session) {
        this.studentName = studentName;
        this.group = group;
        this.date = date;
        this.excuse = excuse;
        this.session = session;
    }

    // Getters and Setters
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getExcuse() { return excuse; }
    public void setExcuse(String excuse) { this.excuse = excuse; }
    
    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }

    @Override
    public String toString() {
        return studentName + " - " + session;
    }
}