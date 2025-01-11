package models;
    public class Subject {
        private String module;
        private String classe;

        // Constructeurs, getters et setters
        public Subject(String module, String classe) {
            this.module = module;
            this.classe = classe;
        }
    
        public String getModule() { return module; }
        public void setModule(String module) { this.module = module; }
    
        public String getClasse() { return classe; }
        public void setClasse(String classe) { this.classe = classe; }
    }