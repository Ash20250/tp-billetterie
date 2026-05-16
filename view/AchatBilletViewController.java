package view;

import dao.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AchatBilletViewController {

    @FXML private ComboBox<Client> comboClient;
    @FXML private ComboBox<Spectacle> comboSpectacle;
    @FXML private TextField fieldPlace;
    @FXML private ComboBox<String> comboStatut;
    @FXML private Label lblPrixTotal;
    @FXML private Button btnAcheter;
    @FXML private GridPane gridSalle;

    private final ClientDAO clientDAO = new ClientDAO();
    private final SpectacleDAO spectacleDAO = new SpectacleDAO();
    private final BilletDAO billetDAO = new BilletDAO();

    private final List<String> placesSelectionnees = new ArrayList<>();
    private final double PRIX_UNITAIRE = 45.00;

    @FXML
    public void initialize() {
        setupComboBoxes();
        comboClient.getItems().setAll(clientDAO.getAllClients());
        comboSpectacle.getItems().setAll(spectacleDAO.getAllSpectacles());
        comboStatut.getItems().setAll("Payé", "Réservé");
        comboStatut.setValue("Payé");

        comboSpectacle.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) dessinerSalle(newV.getId_spectacle());
        });
    }

    public void setData(Spectacle s, Client c) {
        if (s != null) comboSpectacle.setValue(s);
        if (c != null) {
            comboClient.setValue(c);
            comboClient.setDisable(true);
        }
    }

    private void dessinerSalle(int idSpectacle) {
        gridSalle.getChildren().clear();
        placesSelectionnees.clear();
        fieldPlace.clear();

        Spectacle s = comboSpectacle.getValue();
        if (s == null || s.getSalle() == null) return;

        int maxPlaces = s.getSalle().getCapacite();
        int compteur = 0;

        // On fixe 6 rangées (A à F) et 10 colonnes maximum
        char[] rangees = {'A', 'B', 'C', 'D', 'E', 'F'};

        for (int i = 0; i < rangees.length; i++) {
            for (int j = 1; j <= 10; j++) {
                if (compteur >= maxPlaces) break; // Arrêt simple

                String nomPlace = rangees[i] + String.valueOf(j);
                Button btn = new Button(nomPlace);
                btn.setPrefSize(45, 35);

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
                compteur++;
            }
        }
    }

    @FXML
    private void acheterBillet() {
        if (comboClient.getValue() == null || placesSelectionnees.isEmpty()) return;

        try {
            int idSpec = comboSpectacle.getValue().getId_spectacle();
            int idCli = comboClient.getValue().getId_client();

            for (String p : placesSelectionnees) {
                Billet b = new Billet(idCli, idSpec, LocalDateTime.now(), p, comboStatut.getValue(), PRIX_UNITAIRE);
                billetDAO.addBillet(b);
            }
            ((Stage) btnAcheter.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupComboBoxes() {
        comboClient.setConverter(new StringConverter<Client>() {
            @Override public String toString(Client c) { return c == null ? "" : c.getNom() + " " + c.getPrenom(); }
            @Override public Client fromString(String s) { return null; }
        });
        comboSpectacle.setConverter(new StringConverter<Spectacle>() {
            @Override public String toString(Spectacle s) { return s == null ? "" : s.getTitre(); }
            @Override public Spectacle fromString(String s) { return null; }
        });
    }
}