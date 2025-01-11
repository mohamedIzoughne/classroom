package models;
    public class Module {
        private String module;
        private String salle;
        private String filieres;
    
        // Constructeurs, getters et setters
        public Module(String module, String salle, String filieres) {
            this.module = module;
            this.salle = salle;
            this.filieres = filieres;
        }
    
        public String getModule() { return module; }
        public void setModule(String module) { this.module = module; }
    
        public String getSalle() { return salle; }
        public void setSalle(String salle) { this.salle = salle; }
    
        public String getFilieres() { return filieres; }
        public void setFilieres(String filieres) { this.filieres = filieres; }
    }