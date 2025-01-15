package models;

public class Session {
    private int id;
    private String name;
    private String subjectName;
    private String roomName;
    private String day;
    private String hours;
    private boolean status;

    public Session() {
    }

    public Session(String name, String subjectName, String roomName, String day, String hours, boolean status) {
        this.name = name;
        this.subjectName = subjectName;
        this.roomName = roomName;
        this.day = day;
        this.hours = hours;
        this.status = status;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectId(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHours() {
        return hours;
    }

    public boolean getStatus() {
        return status;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}