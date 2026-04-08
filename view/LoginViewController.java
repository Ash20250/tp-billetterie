package view;

import dao.ClientDAO;
import model.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginViewController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;

    private final ClientDAO clientDAO = new ClientDAO();

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showInfo("Veuillez remplir tous les champs.");
            return;
        }

        Client user = clientDAO.getLoggedUser(email, password);

        if (user != null) {
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                System.out.println("Connexion Admin réussie !");
                redirectTo("/view/ListeSpectaclesView.fxml", "Gestion Admin");
            } else {
                System.out.println("Connexion Client réussie !");
                redirectTo("/view/ListeSpectaclesView.fxml", "Espace Client");
            }
        } else {
            showInfo("Email ou mot de passe incorrect.");
        }
    }

    // --- NOUVELLE MÉTHODE POUR L'INSCRIPTION ---
    @FXML
    private void goToInscription() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InscriptionView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Billetterie - Inscription");
        } catch (IOException e) {
            showInfo("Erreur de chargement de la page d'inscription.");
            e.printStackTrace();
        }
    }

    private void redirectTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showInfo("Erreur de chargement : " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}