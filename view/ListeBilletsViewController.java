package src.main.ressources.view;

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
    private final ClientDAO clientDAO = new ClientDAO();
    private final SpectacleDAO spectacleDAO = new SpectacleDAO();
    private final ObservableList<Billet> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colClient.setCellValueFactory(cell -> {
            Client c = clientDAO.getClientById(cell.getValue().getId_client());
            return new SimpleStringProperty(c.getNom() + " " + c.getPrenom());
        });
        colSpectacle.setCellValueFactory(cell -> {
            Spectacle s = spectacleDAO.getSpectacleById(cell.getValue().getId_spectacle());
            return new SimpleStringProperty(s.getTitre());
        });
        colPlace.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPlace()));
        colStatut.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatut()));
        colPrix.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getPrix_paye())));
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate_achat().toString()));

        data.setAll(billetDAO.getAllBillets());
        tableBillets.setItems(data);
    }
}