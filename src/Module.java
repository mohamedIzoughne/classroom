    public class Module {
        private Long id;
        private String name;
        private Long classId;
        private String description;
    
        // Constructeurs, getters et setters
        public Module(Long id, String name, Long classId, String description) {
            this.id = id;
            this.name = name;
            this.classId = classId;
            this.description = description;
        }
    
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    
        public Long getClassId() { return classId; }
        public void setClassId(Long classId) { this.classId = classId; }
    
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
