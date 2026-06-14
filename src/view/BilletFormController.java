package view;

import dao.BilletDAO;
import dao.ClientDAO;
import dao.SeanceDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Billet;
import model.Client;
import model.Seance;

import java.io.IOException;
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

    private final double prixUnitaire = 25.00;

    @FXML
    public void initialize() {
        comboSeance.getItems().addAll(seanceDAO.getAllSeances());
        comboClient.getItems().addAll(clientDAO.getAllClients());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        spinnerQuantite.setValueFactory(valueFactory);

        spinnerQuantite.valueProperty().addListener((obs, oldVal, newVal) -> updatePrixTotal());
        updatePrixTotal();
    }

    private void updatePrixTotal() {
        int quantite = spinnerQuantite.getValue();
        lblPrixTotal.setText(String.format("%.2f €", quantite * prixUnitaire));
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

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // On charge la vue de connexion (AchatBilletView.fxml d'après vos fichiers)
            Parent root = FXMLLoader.load(getClass().getResource("/view/AchatBilletView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Billetterie - Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}