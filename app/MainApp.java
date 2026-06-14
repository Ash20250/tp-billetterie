package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Chargement du FXML (assure-toi que le fichier existe bien dans /view/)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // Intégration du CSS sécurisée
        // Puisque "ressources" est ton "Resources Root", on accède au fichier via /style.css
        URL cssUrl = getClass().getResource("/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("ERREUR : Fichier style.css introuvable dans le classpath.");
        }

        stage.setTitle("Billetterie - Connexion");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Méthode utilitaire pour changer de vue facilement dans l'application
    public static void changeView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/" + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // On réapplique le CSS à la nouvelle scène
            URL cssUrl = MainApp.class.getResource("/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}