package view;

import dao.BilletDAO;
import dao.ClientDAO;
import dao.SpectacleDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Client;
import model.Spectacle;
import model.Billet;

import java.time.LocalDateTime;
import java.util.List;

public class AchatBilletViewController {

    @FXML private ComboBox<Client> comboClient;
    @FXML private ComboBox<Spectacle> comboSpectacle;
    @FXML private TextField fieldPlace;
    @FXML private ComboBox<String> comboStatut;
    @FXML private TextField fieldPrix;
    @FXML private Button btnAcheter;

    private final ClientDAO clientDAO = new ClientDAO();
    private final SpectacleDAO spectacleDAO = new SpectacleDAO();
    private final BilletDAO billetDAO = new BilletDAO();

    private Spectacle spectaclePreselectionne;

    @FXML
    public void initialize() {
        List<Client> clients = clientDAO.getAllClients();
        List<Spectacle> spectacles = spectacleDAO.getAllSpectacles();

        if (clients.isEmpty() || spectacles.isEmpty()) {
            showInfo("Aucun client ou spectacle disponible. Veuillez vérifier la base de données.");
            btnAcheter.setDisable(true);
            return;
        }

        comboClient.getItems().addAll(clients);
        comboSpectacle.getItems().addAll(spectacles);
        comboStatut.getItems().addAll("Réservé", "Payé", "Annulé");

        if (spectaclePreselectionne != null) {
            comboSpectacle.setValue(spectaclePreselectionne);
        }
    }

    public void setSpectacle(Spectacle spectacle) {
        this.spectaclePreselectionne = spectacle;

        // Si comboSpectacle est déjà initialisé
        if (comboSpectacle != null) {
            comboSpectacle.setValue(spectacle);
        }
    }

    @FXML
    private void acheterBillet() {
        Client client = comboClient.getValue();
        Spectacle spectacle = comboSpectacle.getValue();
        String place = fieldPlace.getText().trim();
        String statut = comboStatut.getValue();
        String prixStr = fieldPrix.getText().trim();

        if (client == null || spectacle == null || place.isEmpty() || statut == null || prixStr.isEmpty()) {
            showInfo("Veuillez remplir tous les champs.");
            return;
        }

        if (!place.matches("[A-Z]\\d{1,2}")) {
            showInfo("Format de place invalide. Exemple attendu : A12");
            return;
        }

        double prix;
        try {
            prix = Double.parseDouble(prixStr);
            if (prix <= 0) {
                showInfo("Le prix doit être supérieur à zéro.");
                return;
            }
        } catch (NumberFormatException e) {
            showInfo("Le prix doit être un nombre valide.");
            return;
        }

        Billet billet = new Billet(
                client.getId_client(),          // ✅ cohérent avec ton modèle Client
                spectacle.getId_spectacle(),    // ✅ cohérent avec ton modèle Spectacle
                LocalDateTime.now(),
                place,
                statut,
                prix
        );

        billetDAO.addBillet(billet);
        showConfirmation(billet, client, spectacle);
        clearForm();
    }

    private void clearForm() {
        fieldPlace.clear();
        fieldPrix.clear();
        comboStatut.setValue(null);
        comboClient.setValue(null);
        if (spectaclePreselectionne == null) {
            comboSpectacle.setValue(null);
        }
    }

    private void showConfirmation(Billet billet, Client client, Spectacle spectacle) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation d'achat");
        alert.setHeaderText("🎟️ Billet acheté avec succès !");
        alert.setContentText(
                "Client : " + client.getPrenom() + " " + client.getNom() + "\n" +
                        "Spectacle : " + spectacle.getTitre() + "\n" +
                        "Salle : " + (spectacle.getSalle() != null ? spectacle.getSalle().getNom_salle() : "Salle inconnue") + "\n" +
                        "Place : " + billet.getPlace() + "\n" +
                        "Statut : " + billet.getStatut() + "\n" +
                        "Prix payé : " + billet.getPrix() + " €\n" +
                        "Date d'achat : " + billet.getDateAchat()
        );
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
}