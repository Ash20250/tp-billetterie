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
            showAlert("Champs vides", "Veuillez remplir tous les champs.", Alert.AlertType.WARNING);
            return;
        }

        // Vérification des identifiants via le DAO
        Client user = clientDAO.getLoggedUser(email, password);

        if (user != null) {
            String roleInfo = "admin".equalsIgnoreCase(user.getRole()) ? "Admin" : "Client";
            System.out.println("Connexion " + roleInfo + " réussie : " + user.getEmail());

            // On redirige vers la liste des spectacles (vue commune qui s'adapte au rôle)
            redirectTo("/view/ListeSpectaclesView.fxml", "Billetterie - " + roleInfo, user);
        } else {
            showAlert("Échec de connexion", "Email ou mot de passe incorrect.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void goToInscription() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InscriptionView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Billetterie - Inscription");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page d'inscription.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Charge une nouvelle vue et transmet l'utilisateur au contrôleur cible.
     */
    private void redirectTo(String fxmlPath, String title, Client user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // 1. Charger le parent (déclenche l'initialisation du contrôleur cible)
            Parent root = loader.load();

            // 2. Récupérer le contrôleur de la vue ListeSpectaclesView
            Object controller = loader.getController();

            // 3. Passer l'utilisateur AVANT d'afficher la scène
            if (controller instanceof ListeSpectaclesViewController) {
                ((ListeSpectaclesViewController) controller).setUtilisateur(user);
            }

            // 4. Affichage
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.centerOnScreen(); // Optionnel : centre la fenêtre
            stage.show();

        } catch (IOException e) {
            showAlert("Erreur système", "Erreur de chargement de la vue : " + fxmlPath, Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String titre, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(titre);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}