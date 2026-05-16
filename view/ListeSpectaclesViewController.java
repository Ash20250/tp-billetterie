package view;

import dao.SpectacleDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Spectacle;
import model.Client;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ListeSpectaclesViewController {

    @FXML private TableView<Spectacle> tableSpectacles;
    @FXML private TableColumn<Spectacle, String> colTitre;
    @FXML private TableColumn<Spectacle, String> colDate;
    @FXML private TableColumn<Spectacle, String> colHeure;
    @FXML private TableColumn<Spectacle, String> colSalle;
    @FXML private TableColumn<Spectacle, String> colPlaces;

    @FXML private TextField filterField;
    @FXML private Label lblUtilisateur;

    @FXML private Button btnAjouter, btnModifier, btnSupprimer, btnVoirClient;
    @FXML private Button btnAcheterBillet, btnMesReservations, btnVoirDetails;

    private final SpectacleDAO spectacleDAO = new SpectacleDAO();
    private Client utilisateurConnecte;
    private final ObservableList<Spectacle> masterData = FXCollections.observableArrayList();
    private final DateTimeFormatter frenchDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerDonnees();

        // Logique de filtrage
        FilteredList<Spectacle> filteredData = new FilteredList<>(masterData, p -> true);
        if (filterField != null) {
            filterField.textProperty().addListener((obs, oldVal, newVal) -> {
                filteredData.setPredicate(spectacle -> {
                    if (newVal == null || newVal.isEmpty()) return true;
                    String filter = newVal.toLowerCase();
                    if (spectacle.getTitre().toLowerCase().contains(filter)) return true;
                    if (spectacle.getSalle() != null && spectacle.getSalle().getNom_salle().toLowerCase().contains(filter)) return true;
                    return false;
                });
            });
        }

        SortedList<Spectacle> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableSpectacles.comparatorProperty());
        tableSpectacles.setItems(sortedData);
    }

    private void configurerColonnes() {
        colTitre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitre()));
        colDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDate_spectacle() != null ? d.getValue().getDate_spectacle().format(frenchDateFormatter) : "N/A"));
        colHeure.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getHeure_spectacle() != null ? d.getValue().getHeure_spectacle().toString() : "N/A"));
        colSalle.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSalle() != null ? d.getValue().getSalle().getNom_salle() : "Inconnue"));

        colPlaces.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPlacesDisponibles() > 0 ? String.valueOf(d.getValue().getPlacesDisponibles()) : "Complet"));
        colPlaces.setCellFactory(column -> new TableCell<Spectacle, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else {
                    setText(item);
                    setStyle("Complet".equals(item) ? "-fx-text-fill: #e74c3c; -fx-font-weight: bold;" : "-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                }
            }
        });
    }

    @FXML
    public void chargerDonnees() {
        List<Spectacle> liste = spectacleDAO.getAllSpectacles();
        if (liste != null) masterData.setAll(liste);
    }

    public void setUtilisateur(Client client) {
        this.utilisateurConnecte = client;
        if (client != null) {
            lblUtilisateur.setText("Session : " + client.getPrenom() + " " + client.getNom());
            boolean isAdmin = "admin".equalsIgnoreCase(client.getRole());

            btnAjouter.setVisible(isAdmin); btnAjouter.setManaged(isAdmin);
            btnModifier.setVisible(isAdmin); btnModifier.setManaged(isAdmin);
            btnSupprimer.setVisible(isAdmin); btnSupprimer.setManaged(isAdmin);
            btnVoirClient.setVisible(isAdmin); btnVoirClient.setManaged(isAdmin);

            btnAcheterBillet.setVisible(!isAdmin); btnAcheterBillet.setManaged(!isAdmin);
            btnMesReservations.setVisible(!isAdmin); btnMesReservations.setManaged(!isAdmin);
            btnVoirDetails.setText(isAdmin ? "Liste Participants" : "Voir Détails");
        }
    }

    @FXML private void handleGestionClients() {
        ouvrirFenetre("/view/ClientListView.fxml", "Gestion des Clients");
    }

    @FXML private void handleAjouter() { ouvrirFormulaire(null); }

    @FXML private void handleModifier() {
        Spectacle s = tableSpectacles.getSelectionModel().getSelectedItem();
        if (s != null) ouvrirFormulaire(s);
    }

    private void ouvrirFormulaire(Spectacle s) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SpectacleFormView.fxml"));
            Parent root = loader.load();
            ((SpectacleFormController)loader.getController()).setSpectacle(s);
            showStage(root, s == null ? "Ajouter" : "Modifier");
            chargerDonnees();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML private void handleAcheterBillet() {
        Spectacle s = tableSpectacles.getSelectionModel().getSelectedItem();
        if (s != null && s.getPlacesDisponibles() > 0) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AchatBilletView.fxml"));
                Parent root = loader.load();
                ((AchatBilletViewController)loader.getController()).setData(s, utilisateurConnecte);
                showStage(root, "Achat : " + s.getTitre());
                chargerDonnees();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    @FXML private void handleMesReservations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MesReservationsView.fxml"));
            Parent root = loader.load();
            ((MesReservationsController)loader.getController()).setUtilisateur(utilisateurConnecte);
            showStage(root, "Mes Billets");
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML private void handleVoirDetails() {
        Spectacle s = tableSpectacles.getSelectionModel().getSelectedItem();
        if (s == null) return;
        boolean isAdmin = "admin".equalsIgnoreCase(utilisateurConnecte.getRole());
        try {
            String fxml = isAdmin ? "/view/ListeParticipantsView.fxml" : "/view/SpectacleDetailsView.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            if (isAdmin) ((ListeParticipantsController)loader.getController()).chargerParticipants(s);
            else ((SpectacleDetailsViewController)loader.getController()).setSpectacle(s);
            showStage(root, "Détails");
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML private void handleSupprimer() {
        Spectacle s = tableSpectacles.getSelectionModel().getSelectedItem();
        if (s != null) {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer ?", ButtonType.OK, ButtonType.CANCEL);
            if (a.showAndWait().get() == ButtonType.OK) {
                if (spectacleDAO.deleteSpectacle(s.getId_spectacle())) chargerDonnees();
            }
        }
    }

    @FXML private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            ((Stage) tableSpectacles.getScene().getWindow()).setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void ouvrirFenetre(String path, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            showStage(root, title);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showStage(Parent root, String title) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private void showAlert(String t, String m, Alert.AlertType type) {
        new Alert(type, m).showAndWait();
    }
}