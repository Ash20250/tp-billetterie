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
import app.MainApp;

import java.io.IOException;
import java.net.URL;

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

        Client user = clientDAO.getLoggedUser(email, password);

        if (user != null) {
            try {
                // Chargement de la vue suivante
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ListeSpectaclesView.fxml"));
                Parent root = loader.load();

                ListeSpectaclesViewController controller = loader.getController();
                controller.setUtilisateur(user);

                // Création de la scène
                Scene scene = new Scene(root);

                // Chargement sécurisé du CSS
                URL cssUrl = getClass().getResource("/style.css");
                if (cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                } else {
                    System.err.println("Attention : Fichier style.css introuvable à la racine.");
                }

                // Application de la scène
                MainApp.getPrimaryStage().setScene(scene);
                MainApp.getPrimaryStage().setTitle("Billetterie - " + user.getRole());

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de charger l'interface.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Échec de connexion", "Email ou mot de passe incorrect.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void goToInscription() {
        MainApp.changeView("InscriptionView.fxml", "Billetterie - Inscription");
    }

    private void showAlert(String titre, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(titre);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}