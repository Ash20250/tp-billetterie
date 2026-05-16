package view;

import dao.SalleDAO;
import dao.SpectacleDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.Salle;
import model.Spectacle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class SpectacleFormController {

    @FXML private TextField txtTitre;
    @FXML private DatePicker dateSpectacle;
    @FXML private TextField txtHeure;
    @FXML private Spinner<Integer> spinnerDuree;
    @FXML private Spinner<Integer> spinnerAgeMinimum;
    @FXML private ComboBox<Salle> comboSalle;
    @FXML private TextField txtLangue;

    private final SpectacleDAO spectacleDAO = new SpectacleDAO();
    private final SalleDAO salleDAO = new SalleDAO();
    private Spectacle spectacle;

    @FXML
    public void initialize() {
        // Charger les salles
        ObservableList<Salle> salles = FXCollections.observableArrayList(salleDAO.getAllSalles());
        comboSalle.setItems(salles);

        // Configurer les spinners (Min, Max, Valeur par défaut)
        spinnerDuree.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(30, 300, 90));
        spinnerAgeMinimum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 18, 0));
    }

    public void setSpectacle(Spectacle spectacle) {
        this.spectacle = spectacle;
        if (spectacle != null) {
            txtTitre.setText(spectacle.getTitre());
            dateSpectacle.setValue(spectacle.getDate_spectacle());

            if (spectacle.getHeure_spectacle() != null) {
                txtHeure.setText(spectacle.getHeure_spectacle().toString());
            } else {
                txtHeure.setText("20:00:00");
            }

            spinnerDuree.getValueFactory().setValue(spectacle.getDuree());
            txtLangue.setText(spectacle.getLangue());
            spinnerAgeMinimum.getValueFactory().setValue(spectacle.getAge_minimum());
            comboSalle.setValue(spectacle.getSalle());
        }
    }

    @FXML
    private void saveSpectacle() {
        try {
            String titre = txtTitre.getText();
            LocalDate date = dateSpectacle.getValue();

            // 1. Validation de l'heure
            LocalTime heure;
            try {
                heure = LocalTime.parse(txtHeure.getText());
            } catch (DateTimeParseException | NullPointerException e) {
                throw new Exception("Format d'heure invalide. Utilisez HH:mm ou HH:mm:ss (ex: 20:30)");
            }

            int duree = spinnerDuree.getValue();
            String langue = txtLangue.getText();
            int ageMin = spinnerAgeMinimum.getValue();
            Salle salle = comboSalle.getValue();

            // 2. Validation des champs obligatoires
            if (salle == null || date == null || titre.isEmpty()) {
                throw new Exception("Veuillez remplir les champs obligatoires (Titre, Date, Salle).");
            }

            // 3. VÉRIFICATION DE LA DISPONIBILITÉ (LA POP-UP)
            // On récupère l'ID actuel pour ne pas s'auto-bloquer lors d'une modification
            int currentId = (spectacle != null) ? spectacle.getId_spectacle() : -1;

            if (spectacleDAO.isSalleOccupee(salle.getId_salle(), date, heure, currentId)) {
                throw new Exception("Conflit d'horaire : La salle '" + salle.getNom_salle() +
                        "' est déjà réservée le " + date + " à " + heure + ".");
            }

            // 4. Action : Ajout ou Modification
            if (spectacle == null) {
                Spectacle newSpectacle = new Spectacle(0, titre, date, heure, salle, duree, langue, ageMin);
                if (spectacleDAO.addSpectacle(newSpectacle)) {
                    showSuccessAndClose();
                } else {
                    throw new Exception("Erreur lors de l'ajout en base de données.");
                }
            } else {
                spectacle.setTitre(titre);
                spectacle.setDate_spectacle(date);
                spectacle.setHeure_spectacle(heure);
                spectacle.setDuree(duree);
                spectacle.setLangue(langue);
                spectacle.setAge_minimum(ageMin);
                spectacle.setSalle(salle);

                spectacleDAO.updateSpectacle(spectacle);
                showSuccessAndClose();
            }

        } catch (Exception e) {
            // C'est ici que la pop-up d'erreur s'affiche (pour l'heure ou la salle prise)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void showSuccessAndClose() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Spectacle enregistré avec succès !");
        alert.showAndWait();
        handleAnnuler();
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) txtTitre.getScene().getWindow();
        stage.close();
    }
}