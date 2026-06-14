package view;

import dao.SpectacleDAO;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import model.Salle;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ListeSpectaclesViewController {

    @FXML private TableView<Spectacle> tableSpectacles;
    @FXML private TableColumn<Spectacle, String> colTitre, colDate, colHeure, colSalle, colPlaces;
    @FXML private TextField filterField;
    @FXML private Label lblUtilisateur;
    @FXML private Button btnAjouter, btnModifier, btnSupprimer, btnVoirClient, btnAcheterBillet, btnMesReservations, btnVoirDetails;

    private final SpectacleDAO spectacleDAO = new SpectacleDAO();
    private final ObservableList<Spectacle> masterData = FXCollections.observableArrayList();
    private final DateTimeFormatter frenchDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
    private Client currentUser;
    private boolean isAdmin;

    @FXML
    public void initialize() {
        configurerColonnes();
        tableSpectacles.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateButtonStates(newVal));
        updateButtonStates(null);

        FilteredList<Spectacle> filteredData = new FilteredList<>(masterData, p -> true);
        filterField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(s -> newVal == null || newVal.isEmpty() ||
                    s.getTitre().toLowerCase().contains(newVal.toLowerCase()) ||
                    (s.getSalle() != null && s.getSalle().getNom_salle().toLowerCase().contains(newVal.toLowerCase())));
        });

        SortedList<Spectacle> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableSpectacles.comparatorProperty());
        tableSpectacles.setItems(sortedData);

        chargerDonnees();
    }

    private void updateButtonStates(Spectacle selected) {
        boolean isSelected = (selected != null);
        btnModifier.setDisable(!isSelected);
        btnSupprimer.setDisable(!isSelected);
        btnAcheterBillet.setDisable(!isSelected);
        btnVoirDetails.setDisable(!isSelected);
    }

    private void configurerColonnes() {
        colTitre.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getTitre()));
        colDate.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getDate_spectacle() != null ? d.getValue().getDate_spectacle().format(frenchDateFormatter) : "N/A"));
        colHeure.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getHeure_spectacle() != null ? d.getValue().getHeure_spectacle().toString() : "N/A"));

        colSalle.setCellValueFactory(d -> {
            Salle s = d.getValue().getSalle();
            String nom = (s != null && s.getNom_salle() != null) ? s.getNom_salle() : "Inconnue";
            return new ReadOnlyStringWrapper(nom);
        });

        colPlaces.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().getPlacesDisponibles() > 0 ? String.valueOf(d.getValue().getPlacesDisponibles()) : "Complet"));
        tableSpectacles.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void chargerDonnees() {
        List<Spectacle> liste = spectacleDAO.getAllSpectacles();
        masterData.setAll(liste != null ? liste : FXCollections.observableArrayList());
        tableSpectacles.refresh();
    }

    public void setUtilisateur(Client client) {
        this.currentUser = client;
        lblUtilisateur.setText("Session : " + client.getPrenom() + " " + client.getNom());
        this.isAdmin = "admin".equalsIgnoreCase(client.getRole());

        btnAjouter.setVisible(isAdmin); btnAjouter.setManaged(isAdmin);
        btnModifier.setVisible(isAdmin); btnModifier.setManaged(isAdmin);
        btnSupprimer.setVisible(isAdmin); btnSupprimer.setManaged(isAdmin);
        btnVoirClient.setVisible(isAdmin); btnVoirClient.setManaged(isAdmin);
        btnAcheterBillet.setVisible(!isAdmin); btnAcheterBillet.setManaged(!isAdmin);
        btnMesReservations.setVisible(!isAdmin); btnMesReservations.setManaged(!isAdmin);
        btnVoirDetails.setText(isAdmin ? "Liste Participants" : "Voir Détails");
    }

    @FXML private void handleAjouter() { ouvrirFormulaire(null); }
    @FXML private void handleModifier() { Spectacle s = tableSpectacles.getSelectionModel().getSelectedItem(); if (s != null) ouvrirFormulaire(s); }

    @FXML private void handleSupprimer() {
        Spectacle s = tableSpectacles.getSelectionModel().getSelectedItem();
        if (s == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le spectacle : " + s.getTitre() + " ?");
        Optional<ButtonType> result = alert.showAndWait();

        // CORRECTION : Appel du DAO ici
        if (result.isPresent() && result.get() == ButtonType.OK) {
            spectacleDAO.deleteSpectacle(s.getId_spectacle());
            chargerDonnees();
        }
    }

    @FXML
    private void handleVoirDetails() {
        Spectacle selected = tableSpectacles.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (this.isAdmin) {
            ouvrirListeParticipants(selected);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SpectacleDetailsView.fxml"));
                Parent root = loader.load();
                SpectacleDetailsViewController controller = loader.getController();
                controller.setSpectacle(selected);
                showStage(root, "Détails Spectacle : " + selected.getTitre());
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void ouvrirListeParticipants(Spectacle spectacle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ListeParticipantsView.fxml"));
            Parent root = loader.load();
            ListeParticipantsController controller = loader.getController();
            controller.chargerParticipants(spectacle);
            showStage(root, "Participants : " + spectacle.getTitre());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAcheterBillet() {
        Spectacle selectedSpectacle = tableSpectacles.getSelectionModel().getSelectedItem();
        if (selectedSpectacle == null) { new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un spectacle.").showAndWait(); return; }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AchatBilletView.fxml"));
            Parent root = loader.load();
            AchatBilletViewController controller = loader.getController();
            controller.setData(currentUser, selectedSpectacle);
            showStage(root, "Achat Billet");
            chargerDonnees();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleMesReservations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MesReservationsView.fxml"));
            Parent root = loader.load();
            MesReservationsController controller = loader.getController();
            controller.setUtilisateur(currentUser);
            showStage(root, "Mes Réservations");
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML private void handleGestionClients() { ouvrirFenetre("/view/ClientListView.fxml", "Gestion Clients"); }
    @FXML private void handleLogout() { ((Stage) lblUtilisateur.getScene().getWindow()).close(); }

    private void ouvrirFormulaire(Spectacle s) {
        try {
            URL url = getClass().getResource("/view/SpectacleFormView.fxml");
            if (url == null) throw new IOException("SpectacleFormView.fxml non trouvé");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            // CORRECTION : Transmission de l'objet au contrôleur
            SpectacleFormController controller = loader.getController();
            if (s != null) {
                controller.setSpectacle(s); // Assurez-vous que cette méthode existe dans SpectacleFormController
            }

            showStage(root, (s == null) ? "Ajouter Spectacle" : "Modifier Spectacle");
            chargerDonnees();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void ouvrirFenetre(String fxml, String titre) {
        try {
            URL url = getClass().getResource(fxml);
            if (url == null) throw new IOException("Fichier introuvable : " + fxml);
            FXMLLoader loader = new FXMLLoader(url);
            showStage(loader.load(), titre);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showStage(Parent root, String titre) {
        Stage stage = new Stage();
        stage.setTitle(titre);
        stage.setScene(new Scene(root, 900, 600));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}