package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    // Configuration des dimensions de la fenêtre pour éviter l'écrasement de l'UI
    private static final double MIN_WIDTH = 900.0;
    private static final double MIN_HEIGHT = 600.0;
    private static final String VIEW_PATH = "/view/";

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        // Configuration initiale de la fenêtre
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setWidth(MIN_WIDTH);
        primaryStage.setHeight(MIN_HEIGHT);
        primaryStage.setResizable(true);

        // Chargement de la première vue
        loadView("LoginView.fxml", "Billetterie - Connexion");
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Méthode centralisée pour changer de vue.
     * Ne redimensionne pas la fenêtre pour respecter le choix de l'utilisateur.
     */
    public static void changeView(String fxmlFile, String title) {
        loadView(fxmlFile, title);
    }

    private static void loadView(String fxmlFile, String title) {
        try {
            URL url = MainApp.class.getResource(VIEW_PATH + fxmlFile);

            if (url == null) {
                throw new IOException("Fichier FXML introuvable : " + VIEW_PATH + fxmlFile);
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            if (primaryStage.getScene() == null) {
                primaryStage.setScene(new Scene(root));
            } else {
                primaryStage.getScene().setRoot(root);
            }

            primaryStage.setTitle(title);

        } catch (IOException e) {
            System.err.println("Erreur critique lors du chargement de la vue : " + fxmlFile);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}