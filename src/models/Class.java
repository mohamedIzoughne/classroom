
package models;

public class Class {
    private int id;
    private String name;
    private Filiere filiere;
    private String description;

    public enum Filiere {
        Science,
        Arts,
        Commerce,
        Engineering
    }

    public Class() {
    }

    public Class(String name, Filiere filiere, String description) {
        this.name = name;
        this.filiere = filiere;
        this.description = description;
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

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
