package view;

import dao.BilletDAO;
import dao.ClientDAO;
import dao.SpectacleDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import model.Billet;
import model.Client;
import model.Spectacle;

public class ListeBilletsViewController {

    @FXML private TableView<Billet> tableBillets;
    @FXML private TableColumn<Billet, String> colClient;
    @FXML private TableColumn<Billet, String> colSpectacle;
    @FXML private TableColumn<Billet, String> colPlace;
    @FXML private TableColumn<Billet, String> colStatut;
    @FXML private TableColumn<Billet, String> colPrix;
    @FXML private TableColumn<Billet, String> colDate;

    private final BilletDAO billetDAO = new BilletDAO();
    private final ClientDAO clientDAO = new ClientDAO(); // Utilise le fichier dao/ClientDAO.java
    private final SpectacleDAO spectacleDAO = new SpectacleDAO();
    private final ObservableList<Billet> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Chargement du nom du client via son ID
        colClient.setCellValueFactory(cell -> {
            Client c = clientDAO.getClientById(cell.getValue().getId_client());
            if (c != null) {
                return new SimpleStringProperty(c.getNom() + " " + c.getPrenom());
            }
            return new SimpleStringProperty("Client inconnu");
        });

        // Chargement du titre du spectacle via son ID
        colSpectacle.setCellValueFactory(cell -> {
            Spectacle s = spectacleDAO.getSpectacleById(cell.getValue().getId_spectacle());
            if (s != null) {
                return new SimpleStringProperty(s.getTitre());
            }
            return new SimpleStringProperty("Spectacle inconnu");
        });

        colPlace.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPlace()));
        colStatut.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatut()));
        colPrix.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getPrix_paye()) + " €"));

        colDate.setCellValueFactory(cell -> {
            if (cell.getValue().getDate_achat() != null) {
                return new SimpleStringProperty(cell.getValue().getDate_achat().toString());
            }
            return new SimpleStringProperty("-");
        });

        // Remplissage de la table
        data.setAll(billetDAO.getAllBillets());
        tableBillets.setItems(data);
    }
}