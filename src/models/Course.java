package models;
import javafx.beans.property.*;

public class Course {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty subject = new SimpleStringProperty();
    private final StringProperty salle = new SimpleStringProperty();
    private final StringProperty day = new SimpleStringProperty();
    private final StringProperty time = new SimpleStringProperty();
    private final IntegerProperty hours = new SimpleIntegerProperty();
    private final BooleanProperty status = new SimpleBooleanProperty();
    private final StringProperty classe = new SimpleStringProperty();
    private final StringProperty groupe = new SimpleStringProperty();

    public Course(String name, String subject, String salle, String day, String time, int hours, boolean status, String classe, String groupe) {
        this.name.set(name);
        this.subject.set(subject);
        this.salle.set(salle);
        this.day.set(day);
        this.time.set(time);
        this.hours.set(hours);
        this.status.set(status);
        this.classe.set(classe);
        this.groupe.set(groupe);
    }

    // Getters et setters
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public String getSubject() { return subject.get(); }
    public void setSubject(String subject) { this.subject.set(subject); }
    public StringProperty subjectProperty() { return subject; }

    public String getSalle() { return salle.get(); }
    public void setSalle(String salle) { this.salle.set(salle); }
    public StringProperty salleProperty() { return salle; }

    public String getDay() { return day.get(); }
    public void setDay(String day) { this.day.set(day); }
    public StringProperty dayProperty() { return day; }

    public String getTime() { return time.get(); }
    public void setTime(String time) { this.time.set(time); }
    public StringProperty timeProperty() { return time; }

    public Integer getHours() { return hours.get(); }
    public void setHours(int hours) { this.hours.set(hours); }
    public IntegerProperty hoursProperty() { return hours; }

    public boolean getStatus() { return status.get(); }
    public void setStatus(boolean status) { this.status.set(status); }
    public BooleanProperty statusProperty() { return status; }

    public String getClasse() { return classe.get(); }
    public void setClasse(String classe) { this.classe.set(classe); }
    public StringProperty classeProperty() { return classe; }

    public String getGroupe() { return groupe.get(); }
    public void setGroupe(String groupe) { this.groupe.set(groupe); }
    public StringProperty groupeProperty() { return groupe; }
}