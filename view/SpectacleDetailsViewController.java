package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.Spectacle;

import java.time.LocalDate;
import java.time.LocalTime;

public class SpectacleDetailsViewController {

    @FXML private Label lblTitre;
    @FXML private Label lblDate;
    @FXML private Label lblHeure;
    @FXML private Label lblSalle;
    @FXML private Label lblDuree;
    @FXML private Label lblLangue;

    private Spectacle spectacle;

    public void setSpectacle(Spectacle spectacle) {
        this.spectacle = spectacle;
        populateFields();
    }

    private void populateFields() {
        if (spectacle == null) return;

        lblTitre.setText(nullSafe(spectacle.getTitre()));

        LocalDate d = spectacle.getDate_spectacle();
        lblDate.setText(d != null ? d.toString() : "");

        LocalTime h = spectacle.getHeure_spectacle();
        lblHeure.setText(h != null ? h.toString() : "");

        lblSalle.setText(
                spectacle.getSalle() != null ? nullSafe(spectacle.getSalle().getNom_salle()) : ""
        );

        Object duree = spectacle.getDuree();
        lblDuree.setText(duree != null ? duree.toString() : "");

        lblLangue.setText(nullSafe(spectacle.getLangue()));
    }

    @FXML
    private void handleOpenClientList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ClientList.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des clients");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Impossible d'ouvrir la liste des clients : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private String nullSafe(String s) {
        return s != null ? s : "";
    }
}