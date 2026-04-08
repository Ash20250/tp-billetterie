package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Hyperlink;  // <-- important !
import model.Client;

import java.awt.Desktop;
import java.net.URI;

public class ClientDetailsController {

    @FXML private Label lblNom;
    @FXML private Label lblPrenom;
    @FXML private Hyperlink lblEmail;   // <-- mail cliquable
    @FXML private Label lblTelephone;
    @FXML private Label lblAge;
    @FXML private Label lblAdresse;

    private Client client;

    public void setClient(Client client) {
        this.client = client;
        afficherDetails();
    }

    private void afficherDetails() {
        if (client != null) {
            lblNom.setText(client.getNom());
            lblPrenom.setText(client.getPrenom());
            lblEmail.setText(client.getEmail());
            lblTelephone.setText(client.getNumTel());
            lblAge.setText(String.valueOf(client.getAge()));
            lblAdresse.setText(client.getAdresse());
        }
    }

    @FXML
    private void onEmailClick() {
        try {
            Desktop.getDesktop().mail(new URI("mailto:" + client.getEmail()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
