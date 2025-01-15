import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import db.TeacherDAO;

public class LoginController {
    @FXML
    private TextField emailInput;
    
    @FXML
    private TextField passwordInput;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button emailIconButton;
    
    @FXML
    private Button passwordIconButton;
    
    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
    }
    
    private void handleLogin() {
        String email = emailInput.getText();
        String password = passwordInput.getText();
        
        if (validateInput(email, password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
                Parent root = loader.load();
                
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Could not load main page.");
            }
        }
    }
    
    private boolean validateInput(String email, String password) {
        // if (email.isEmpty() || password.isEmpty()) {
        //     showAlert("Error", "Please fill in all fields.");
        //     return false;
        // }
        
        // if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        //     showAlert("Error", "Please enter a valid email address.");
        //     return false;
        // }
        
        // if (password.length() < 6 || !password.matches(".*[A-Z].*")) {
        //     showAlert("Error", "Password must be at least 6 characters and contain 1 capital letter.");
        //     return false;
        // }
        
        String hashed = TeacherDAO.hashPassword("1234ABizourne@%_");
        System.out.println("Hashed password:");
        System.out.println(hashed);
        
        TeacherDAO teacherDAO = new TeacherDAO();
        if (!teacherDAO.login(email, password)) {
            showAlert("Error", "Invalid email or password.");
            return false;
        }
        
        return true;
    }    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}