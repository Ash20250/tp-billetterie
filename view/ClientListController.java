package view;

import dao.ClientDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.Client;

import java.util.List;

public class ClientListController {

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button refreshButton;

    @FXML private TableView<Client> tableClients;
    @FXML private TableColumn<Client, String> colId;
    @FXML private TableColumn<Client, String> colPrenom;
    @FXML private TableColumn<Client, String> colNom;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private TableColumn<Client, String> colAdresse;
    @FXML private TableColumn<Client, String> colNumTel;

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button detailsButton;

    private final ClientDAO clientDAO = new ClientDAO();
    private ObservableList<Client> clients;

    @FXML
    public void initialize() {
        // Liaison des colonnes avec les propriétés du modèle Client
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId_client())));
        colPrenom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        colAdresse.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdresse()));
        colNumTel.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumTel()));

        // Charger les clients au démarrage
        loadClients();

        // Actions des boutons
        searchButton.setOnAction(e -> searchClients());
        refreshButton.setOnAction(e -> loadClients());
        addButton.setOnAction(e -> addClient());
        editButton.setOnAction(e -> editClient());
        deleteButton.setOnAction(e -> deleteClient());
        detailsButton.setOnAction(e -> showClientDetails());
    }

    private void loadClients() {
        List<Client> allClients = clientDAO.getAllClients();
        clients = FXCollections.observableArrayList(allClients);
        tableClients.setItems(clients);
    }

    private void searchClients() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadClients();
            return;
        }
        List<Client> results = clientDAO.searchClientsByName(keyword);
        clients = FXCollections.observableArrayList(results);
        tableClients.setItems(clients);
    }

    private void addClient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ClientFormView.fxml"));
            Parent root = loader.load();

            ClientFormController controller = loader.getController();
            controller.setClientDAO(clientDAO);
            controller.setOnSaved(this::loadClients); // ✅ rafraîchit la liste après ajout

            Stage stage = new Stage();
            stage.setTitle("Ajouter un client");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erreur ouverture formulaire client : " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    private void editClient() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un client à modifier.").showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ClientFormView.fxml"));
            Parent root = loader.load();

            ClientFormController controller = loader.getController();
            controller.setClientDAO(clientDAO);
            controller.setExistingClient(selected); // ✅ pré-remplit le formulaire
            controller.setOnSaved(this::loadClients); // ✅ rafraîchit après modification

            Stage stage = new Stage();
            stage.setTitle("Modifier un client");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erreur ouverture formulaire client : " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    private void deleteClient() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected != null) {
            clientDAO.deleteClient(selected.getId_client());
            loadClients();
        }
    }

    private void showClientDetails() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Profil du client :\n" +
                            "Nom : " + selected.getNom() + "\n" +
                            "Prénom : " + selected.getPrenom() + "\n" +
                            "Email : " + selected.getEmail() + "\n" +
                            "Adresse : " + selected.getAdresse() + "\n" +
                            "Téléphone : " + selected.getNumTel()
            );
            alert.setHeaderText("Détails du client");
            alert.showAndWait();
        }
    }
}