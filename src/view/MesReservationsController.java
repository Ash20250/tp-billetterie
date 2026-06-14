package view;

import dao.BilletDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Billet;
import model.Client;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class MesReservationsController {

    @FXML private TableView<Billet> tableBillets;
    @FXML private TableColumn<Billet, String> colSpectacle;
    @FXML private TableColumn<Billet, String> colPlace;
    @FXML private TableColumn<Billet, String> colDateAchat;
    @FXML private TableColumn<Billet, String> colPrix;
    @FXML private TableColumn<Billet, String> colStatut;

    private final BilletDAO billetDAO = new BilletDAO();
    private Client currentClient;

    public void setUtilisateur(Client client) {
        if (client == null) return;
        this.currentClient = client;

        // Formatage de la date pour un affichage en français (Ex: 15/04/2026 14:19)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Configuration des colonnes
        colSpectacle.setCellValueFactory(d -> {
            String titre = d.getValue().getTitreSpectacle();
            return new SimpleStringProperty(titre != null ? titre : "Spectacle #" + d.getValue().getId_spectacle());
        });

        colPlace.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPlace()));

        colDateAchat.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getDate_achat().format(formatter)));

        colPrix.setCellValueFactory(d ->
                new SimpleStringProperty(String.format("%.2f €", d.getValue().getPrix_paye())));

        colStatut.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatut()));

        chargerBillets();
    }

    private void chargerBillets() {
        List<Billet> billets = billetDAO.getBilletsByClient(currentClient.getId_client());
        tableBillets.setItems(FXCollections.observableArrayList(billets));
    }

    @FXML
    private void handleSupprimer() {
        // 1. Récupérer le billet sélectionné dans la table
        Billet selectedBillet = tableBillets.getSelectionModel().getSelectedItem();

        if (selectedBillet == null) {
            showAlert("Sélection requise", "Veuillez sélectionner un billet à annuler.", Alert.AlertType.WARNING);
            return;
        }

        // 2. Demander confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation d'annulation");
        confirmation.setHeaderText("Annuler la réservation ?");
        confirmation.setContentText("Voulez-vous vraiment supprimer votre billet pour : " + selectedBillet.getTitreSpectacle() + " ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // 3. Appel au DAO pour suppression en base
            if (billetDAO.deleteBillet(selectedBillet.getId_billet())) {
                // 4. Mise à jour de l'interface
                tableBillets.getItems().remove(selectedBillet);
                showAlert("Succès", "La réservation a été annulée.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Impossible de supprimer le billet en base de données.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleClose() {
        if (tableBillets.getScene() != null) {
            ((Stage) tableBillets.getScene().getWindow()).close();
        }
    }

    private void showAlert(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}