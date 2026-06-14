package view;

import dao.ClientDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Client;

import java.io.IOException;
import java.net.URL;

public class ClientListController {

    @FXML private TextField searchField;
    @FXML private TableView<Client> tableClients;
    @FXML private TableColumn<Client, Integer> colId;
    @FXML private TableColumn<Client, String> colPrenom;
    @FXML private TableColumn<Client, String> colNom;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private TableColumn<Client, String> colAdresse;
    @FXML private TableColumn<Client, String> colNumTel;

    private final ClientDAO clientDAO = new ClientDAO();
    private final ObservableList<Client> masterData = FXCollections.observableArrayList();
    private FilteredList<Client> filteredData;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_client"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colNumTel.setCellValueFactory(new PropertyValueFactory<>("num_tel"));

        chargerDonnees();

        filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            appliquerFiltre(newValue);
        });

        SortedList<Client> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableClients.comparatorProperty());
        tableClients.setItems(sortedData);
    }

    // --- Méthodes liées au FXML ---

    @FXML
    private void handleRechercher() {
        System.out.println("Recherche activée");
    }

    @FXML
    private void handleRafraichir() {
        chargerDonnees();
    }

    @FXML
    private void handleAjouter() {
        ouvrirFormulaire(null);
    }

    @FXML
    private void handleModifier() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ouvrirFormulaire(selected);
        } else {
            afficherAlerte("Attention", "Veuillez sélectionner un client à modifier.");
        }
    }

    @FXML
    private void handleSupprimer() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Suppression");
            alert.setContentText("Voulez-vous vraiment supprimer " + selected.getPrenom() + " ?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                clientDAO.deleteClient(selected.getId_client());
                chargerDonnees();
            }
        } else {
            afficherAlerte("Attention", "Veuillez sélectionner un client à supprimer.");
        }
    }

    // --- Méthodes utilitaires ---

    private void ouvrirFormulaire(Client client) {
        try {
            URL fxmlUrl = getClass().getClassLoader().getResource("view/ClientFormView.fxml");

            if (fxmlUrl == null) {
                afficherAlerte("Erreur", "Fichier FXML introuvable : view/ClientFormView.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // --- CORRECTION : Injection du DAO ---
            // On récupère le contrôleur du formulaire chargé par le FXML
            ClientFormController controller = loader.getController();

            // On passe l'instance du DAO et le client à modifier (ou null)
            controller.setClientDAO(this.clientDAO);
            controller.setExistingClient(client);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(client == null ? "Ajouter un client" : "Modifier un client");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Rafraîchir la liste après la fermeture de la fenêtre
            chargerDonnees();
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible d'ouvrir le formulaire : " + e.getMessage());
        }
    }

    private void appliquerFiltre(String searchText) {
        filteredData.setPredicate(client -> {
            if (searchText == null || searchText.isEmpty()) return true;
            String lowerCaseFilter = searchText.toLowerCase();
            return client.getNom().toLowerCase().contains(lowerCaseFilter) ||
                    client.getPrenom().toLowerCase().contains(lowerCaseFilter) ||
                    client.getEmail().toLowerCase().contains(lowerCaseFilter);
        });
    }

    private void chargerDonnees() {
        try {
            masterData.setAll(clientDAO.getAllClients());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}