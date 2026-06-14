package view;

import dao.ClientDAO;
import model.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
// --- CORRECTION ICI ---
import app.MainApp;

public class InscriptionViewController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;

    private final ClientDAO clientDAO = new ClientDAO();

    @FXML
    private void handleInscription() {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (nom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir les champs obligatoires (Nom, Email, MDP).");
            return;
        }

        Client nouveauClient = new Client(0, nom, prenom, email);
        nouveauClient.setRole("CLIENT");

        clientDAO.addClient(nouveauClient);

        showAlert("Succès", "Votre compte a été créé ! Vous pouvez vous connecter.");
        retourLogin();
    }

    @FXML
    private void retourLogin() {
        // --- CORRECTION ICI ---
        // Utilise la méthode correcte, assure-toi que changeView existe dans MainApp
        // ou utilise la même logique que dans LoginViewController
        MainApp.changeView("LoginView.fxml", "Billetterie - Connexion");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}