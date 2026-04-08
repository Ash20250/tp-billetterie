package view;

import dao.SpectacleDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.Spectacle;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class ListeSpectaclesViewController {

    @FXML private TableView<Spectacle> tableSpectacles;
    @FXML private TableColumn<Spectacle, String> colNom;
    @FXML private TableColumn<Spectacle, String> colDate;
    @FXML private TableColumn<Spectacle, String> colHeure;
    @FXML private TableColumn<Spectacle, String> colSalle;

    @FXML private Button btnPrev;
    @FXML private Button btnNext;
    @FXML private Button btnDetails;
    @FXML private Button btnAcheter;

    private final SpectacleDAO spectacleDAO = new SpectacleDAO();
    private ObservableList<Spectacle> spectacles;
    private int currentIndex = 0;
    private final int pageSize = 10;

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitre()));

        colDate.setCellValueFactory(data -> {
            LocalDate d = data.getValue().getDate_spectacle();
            return new SimpleStringProperty(d != null ? d.toString() : "");
        });

        colHeure.setCellValueFactory(data -> {
            LocalTime h = data.getValue().getHeure_spectacle();
            return new SimpleStringProperty(h != null ? h.toString() : "");
        });

        colSalle.setCellValueFactory(data -> {
            return new SimpleStringProperty(
                    data.getValue().getSalle() != null ? data.getValue().getSalle().getNom_salle() : ""
            );
        });

        List<Spectacle> allSpectacles = spectacleDAO.getAllSpectacles();
        spectacles = FXCollections.observableArrayList(allSpectacles);
        updateTable();
    }

    private void updateTable() {
        int fromIndex = Math.max(0, currentIndex);
        int toIndex = Math.min(fromIndex + pageSize, spectacles.size());
        tableSpectacles.setItems(FXCollections.observableArrayList(spectacles.subList(fromIndex, toIndex)));
    }

    @FXML
    private void goToPreviousPage() {
        if (currentIndex >= pageSize) {
            currentIndex -= pageSize;
            updateTable();
        }
    }

    @FXML
    private void goToNextPage() {
        if (currentIndex + pageSize < spectacles.size()) {
            currentIndex += pageSize;
            updateTable();
        }
    }

    @FXML
    private void showDetails() {
        Spectacle selected = tableSpectacles.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SpectacleDetailsView.fxml"));
                Parent root = loader.load();

                SpectacleDetailsViewController controller = loader.getController();
                controller.setSpectacle(selected);

                Stage stage = new Stage();
                stage.setTitle("Détails du Spectacle");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur d'affichage des détails : " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un spectacle.");
            alert.showAndWait();
        }
    }

    @FXML
    private void acheterBillet() {
        Spectacle selected = tableSpectacles.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AchatBilletView.fxml"));
                Parent root = loader.load();

                AchatBilletViewController controller = loader.getController();
                controller.setSpectacle(selected);

                Stage stage = new Stage();
                stage.setTitle("Achat de billet");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ouverture de l'achat : " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un spectacle.");
            alert.showAndWait();
        }
    }
}