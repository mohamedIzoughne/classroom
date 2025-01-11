package models;
public class Classes {
    private String classe;
    private String description;
    private String filiere;

    public Classes(String classe, String description, String filiere) {
        this.classe = classe;
        this.description = description;
        this.filiere = filiere;
    }

    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }
    
    public String getdescription() { return description; }
    public void setdescription(String description) { this.description = description; }
    
    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }
}