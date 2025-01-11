import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DaysSchedule {
    private final StringProperty monday;
    private final StringProperty tuesday;
    private final StringProperty wednesday;
    private final StringProperty thursday;
    private final StringProperty friday;
    private final StringProperty saturday;
    private final StringProperty sunday;

    public DaysSchedule(String monday, String tuesday, String wednesday, 
                      String thursday, String friday, 
                      String saturday, String sunday) {
        this.monday = new SimpleStringProperty(monday);
        this.tuesday = new SimpleStringProperty(tuesday);
        this.wednesday = new SimpleStringProperty(wednesday);
        this.thursday = new SimpleStringProperty(thursday);
        this.friday = new SimpleStringProperty(friday);
        this.saturday = new SimpleStringProperty(saturday);
        this.sunday = new SimpleStringProperty(sunday);
    }

    public StringProperty mondayProperty() { return monday; }
    public StringProperty tuesdayProperty() { return tuesday; }
    public StringProperty wednesdayProperty() { return wednesday; }
    public StringProperty thursdayProperty() { return thursday; }
    public StringProperty fridayProperty() { return friday; }
    public StringProperty saturdayProperty() { return saturday; }
    public StringProperty sundayProperty() { return sunday; }
}
