package view;

import dao.BilletDAO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Billet;
import model.Spectacle;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListeParticipantsController {

    @FXML private Label lblTitreSpectacle;
    @FXML private Label lblTotalVentes;
    @FXML private Label lblRecette;

    @FXML private TableView<Billet> tableParticipants;
    @FXML private TableColumn<Billet, String> colNom;
    @FXML private TableColumn<Billet, String> colPrenom;
    @FXML private TableColumn<Billet, String> colPlace;
    @FXML private TableColumn<Billet, String> colDateAchat;
    @FXML private TableColumn<Billet, String> colPrix;

    private final BilletDAO billetDAO = new BilletDAO();

    @FXML
    public void initialize() {
        // Configuration des colonnes
        // Note : On utilise getTitreSpectacle() pour afficher le Nom/Prénom
        // car nous avons stocké la jointure dedans dans le DAO précédent.

        colNom.setCellValueFactory(data -> {
            String full = data.getValue().getTitreSpectacle(); // Contient "Prenom Nom"
            String nom = (full != null && full.contains(" ")) ? full.split(" ")[1] : "N/A";
            return new SimpleStringProperty(nom);
        });

        colPrenom.setCellValueFactory(data -> {
            String full = data.getValue().getTitreSpectacle();
            String prenom = (full != null && full.contains(" ")) ? full.split(" ")[0] : "N/A";
            return new SimpleStringProperty(prenom);
        });

        colPlace.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlace()));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colDateAchat.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDate_achat().format(dtf)));

        colPrix.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("%.2f €", data.getValue().getPrix_paye())));
    }

    /**
     * Méthode appelée par le contrôleur principal pour remplir la vue
     */
    public void chargerParticipants(Spectacle spectacle) {
        if (spectacle == null) return;

        lblTitreSpectacle.setText("Participants pour : " + spectacle.getTitre());

        // Récupération des données via le DAO
        List<Billet> participants = billetDAO.getBilletsBySpectacle(spectacle.getId_spectacle());

        // Calcul des statistiques
        int totalPlaces = participants.size();
        double recetteTotale = participants.stream().mapToDouble(Billet::getPrix_paye).sum();

        // Mise à jour de l'interface
        tableParticipants.setItems(FXCollections.observableArrayList(participants));
        lblTotalVentes.setText("Total places : " + totalPlaces);
        lblRecette.setText(String.format("Recette totale : %.2f €", recetteTotale));
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) tableParticipants.getScene().getWindow();
        stage.close();
    }
}