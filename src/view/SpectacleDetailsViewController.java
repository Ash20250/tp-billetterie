package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Spectacle;

public class SpectacleDetailsViewController {

    @FXML private Label lblTitre;
    @FXML private Label lblDate;
    @FXML private Label lblHeure;
    @FXML private Label lblSalle;
    @FXML private Label lblDuree;
    @FXML private Label lblLangue;

    // Cette méthode est appelée automatiquement par JavaFX quand le FXML est chargé
    @FXML
    public void initialize() {
        System.out.println("DEBUG : Le contrôleur est bien chargé et les Labels sont injectés.");
    }

    public void setSpectacle(Spectacle spectacle) {
        if (spectacle == null) {
            System.out.println("DEBUG : Spectacle reçu est NULL");
            return;
        }

        System.out.println("DEBUG : Mise à jour avec : " + spectacle.getTitre());

        // Sécurité : si lblTitre est null, c'est que l'injection @FXML a échoué
        if (lblTitre == null) {
            System.err.println("ERREUR : Les Labels ne sont pas connectés au FXML !");
            return;
        }

        lblTitre.setText(spectacle.getTitre());
        lblDate.setText("Date : " + (spectacle.getDate_spectacle() != null ? spectacle.getDate_spectacle().toString() : "N/A"));
        lblHeure.setText("Heure : " + (spectacle.getHeure_spectacle() != null ? spectacle.getHeure_spectacle().toString() : "N/A"));
        lblSalle.setText("Salle : " + (spectacle.getSalle() != null ? spectacle.getSalle().getNom_salle() : "N/A"));
        lblDuree.setText("Durée : " + spectacle.getDuree() + " min");
        lblLangue.setText("Langue : " + (spectacle.getLangue() != null ? spectacle.getLangue() : "N/A"));
    }
}