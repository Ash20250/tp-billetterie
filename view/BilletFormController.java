package src.main.ressources.view;

import dao.BilletDAO;
import dao.ClientDAO;
import dao.SeanceDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Billet;
import model.Client;
import model.Seance;

import java.time.LocalDate;

public class BilletFormController {

    @FXML private ComboBox<Seance> comboSeance;
    @FXML private ComboBox<Client> comboClient;
    @FXML private Spinner<Integer> spinnerQuantite;
    @FXML private Label lblPrixTotal;
    @FXML private Button btnValider;

    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final BilletDAO billetDAO = new BilletDAO();

    private final double prixUnitaire = 25.00; // exemple

    @FXML
    public void initialize() {
        comboSeance.getItems().addAll(seanceDAO.getAllSeances());
        comboClient.getItems().addAll(clientDAO.getAllClients());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        spinnerQuantite.setValueFactory(valueFactory);

        spinnerQuantite.valueProperty().addListener((obs, oldVal, newVal) -> updatePrixTotal());
    }

    private void updatePrixTotal() {
        int quantite = spinnerQuantite.getValue();
        lblPrixTotal.setText((quantite * prixUnitaire) + " €");
    }

    @FXML
    private void validerAchat() {
        Seance seance = comboSeance.getValue();
        Client client = comboClient.getValue();
        int quantite = spinnerQuantite.getValue();

        if (seance != null && client != null) {
            for (int i = 0; i < quantite; i++) {
                Billet billet = new Billet(0, seance, client, prixUnitaire, LocalDate.now());
                billetDAO.addBillet(billet);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Achat validé pour " + quantite + " billet(s).");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une séance et un client.");
            alert.showAndWait();
        }
    }
}