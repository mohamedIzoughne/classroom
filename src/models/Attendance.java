package models;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Attendance {
    private String studentName;
    private double attendance;
    private BooleanProperty attendant = new SimpleBooleanProperty(false);

    public Attendance(String studentName, double attendance, boolean attendant) {
        this.studentName = studentName;
        this.attendance = attendance;
        this.attendant = new SimpleBooleanProperty(attendant);
    }

    public String getStudentName() {
        return studentName;
    }

    public double getAttendance() {
        return attendance;
    }

    public BooleanProperty getAttendant() {
        return attendant;
    }

}
