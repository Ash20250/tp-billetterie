package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Spectacle;
import model.Client;

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
        updateUI();
    }

    public void setUtilisateur(Client user) {
        // Option A : On ne fait rien ici car le bouton est supprimé
    }

    private void updateUI() {
        if (spectacle == null || lblTitre == null) return;

        lblTitre.setText(spectacle.getTitre());
        lblDate.setText("Date : " + (spectacle.getDate_spectacle() != null ? spectacle.getDate_spectacle() : "N/A"));
        lblHeure.setText("Heure : " + (spectacle.getHeure_spectacle() != null ? spectacle.getHeure_spectacle() : "N/A"));
        lblSalle.setText("Salle : " + (spectacle.getSalle() != null ? spectacle.getSalle().getNom_salle() : "N/A"));
        lblDuree.setText("Durée : " + spectacle.getDuree() + " min");
        lblLangue.setText("Langue : " + spectacle.getLangue());
    }
}