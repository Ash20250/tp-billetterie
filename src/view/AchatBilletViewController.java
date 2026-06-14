package view;

import dao.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AchatBilletViewController {

    // Labels au lieu des ComboBox
    @FXML private Label lblClient;
    @FXML private Label lblSpectacle;

    @FXML private TextField fieldPlace;
    @FXML private ComboBox<String> comboStatut;
    @FXML private Label lblPrixTotal;
    @FXML private Button btnAcheter;
    @FXML private GridPane gridSalle;

    private final BilletDAO billetDAO = new BilletDAO();

    // Stockage des données injectées
    private Client clientActuel;
    private Spectacle spectacleActuel;

    private final List<String> placesSelectionnees = new ArrayList<>();
    private final double PRIX_UNITAIRE = 45.00;

    @FXML
    public void initialize() {
        comboStatut.getItems().setAll("Payé", "Réservé");
        comboStatut.setValue("Payé");
    }

    /**
     * Reçoit les données depuis la vue précédente
     */
    public void setData(Client client, Spectacle spectacle) {
        this.clientActuel = client;
        this.spectacleActuel = spectacle;

        if (client != null) {
            lblClient.setText(client.getPrenom() + " " + client.getNom());
        }

        if (spectacle != null) {
            lblSpectacle.setText(spectacle.getTitre());
            dessinerSalle(spectacle.getId_spectacle());
        }
    }

    private void dessinerSalle(int idSpectacle) {
        gridSalle.getChildren().clear();
        placesSelectionnees.clear();

        char[] rangees = {'A', 'B', 'C', 'D', 'E', 'F'};

        for (int i = 0; i < rangees.length; i++) {
            for (int j = 0; j < 10; j++) {
                String nomPlace = rangees[i] + String.valueOf(j + 1);
                Button btn = new Button(nomPlace);

                // Taille agrandie à 45x35 pour afficher le "10" sans couper le texte
                btn.setPrefSize(45, 35);

                // Vérifier l'état de la place
                if (billetDAO.isPlaceOccupee(idSpectacle, nomPlace)) {
                    btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                    btn.setDisable(true);
                } else {
                    btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");

                    btn.setOnAction(e -> {
                        if (placesSelectionnees.contains(nomPlace)) {
                            placesSelectionnees.remove(nomPlace);
                            btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                        } else {
                            placesSelectionnees.add(nomPlace);
                            btn.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: black;");
                        }
                        fieldPlace.setText(String.join(", ", placesSelectionnees));
                        lblPrixTotal.setText(String.format("%.2f €", placesSelectionnees.size() * PRIX_UNITAIRE));
                    });
                }
                gridSalle.add(btn, j, i);
            }
        }
    }

    @FXML
    private void acheterBillet() {
        // Utilisation directe des objets stockés
        if (clientActuel == null || placesSelectionnees.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Sélectionnez des places !").show();
            return;
        }

        try {
            for (String p : placesSelectionnees) {
                Billet b = new Billet(clientActuel.getId_client(),
                        spectacleActuel.getId_spectacle(),
                        LocalDateTime.now(), p, comboStatut.getValue(), PRIX_UNITAIRE);
                billetDAO.addBillet(b);
            }
            Stage stage = (Stage) btnAcheter.getScene().getWindow();
            stage.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}