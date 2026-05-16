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

public class InscriptionViewController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;

    private final ClientDAO clientDAO = new ClientDAO();

    /**
     * Gère l'inscription d'un nouvel utilisateur.
     */
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

        // Création du client (ID 0 car auto-incrémenté en BDD)
        Client nouveauClient = new Client(0, nom, prenom, email);
        nouveauClient.setRole("CLIENT"); // Rôle par défaut

        // Ajout en base de données
        clientDAO.addClient(nouveauClient);

        showAlert("Succès", "Votre compte a été créé ! Vous pouvez vous connecter.");
        retourLogin(); // Redirection automatique après succès
    }

    /**
     * Redirige vers la fenêtre de connexion.
     * Le nom doit être retourLogin pour correspondre au FXML (onAction="#retourLogin").
     */
    @FXML
    private void retourLogin() {
        try {
            // Chargement de la vue Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();

            // Récupération de la fenêtre actuelle (Stage) via n'importe quel composant
            Stage stage = (Stage) txtEmail.getScene().getWindow();

            // Changement de la scène
            stage.setScene(new Scene(root));
            stage.setTitle("Billetterie - Connexion");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur : Le fichier LoginView.fxml est introuvable dans /view/");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}