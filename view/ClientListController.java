package view;

import dao.ClientDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Client;

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

    /**
     * ✅ FIX : onAction='#handleVoirDetails' (Ligne 48)
     */
    @FXML
    private void handleVoirDetails() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Détails du client : " + selected.getNom() + " " + selected.getPrenom());
        }
    }

    @FXML
    private void handleAjouter() {
        System.out.println("Ouverture formulaire ajout...");
    }

    @FXML
    private void handleModifier() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Modification de : " + selected.getNom());
        }
    }

    @FXML
    private void handleSupprimer() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected != null) {
            clientDAO.deleteClient(selected.getId_client());
            chargerDonnees();
        }
    }

    @FXML
    private void handleRechercher() {
        appliquerFiltre(searchField.getText());
    }

    @FXML
    private void handleRafraichir() {
        chargerDonnees();
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
}