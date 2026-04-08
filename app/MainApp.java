package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // On charge maintenant la vue de Login au démarrage
        // Utiliser getResource est plus propre que le chemin absolu C:/...
        URL fxmlLocation = getClass().getResource("/view/LoginView.fxml");

        // Si getResource ne trouve rien (selon ta config IntelliJ),
        // on garde ta méthode File temporairement pour le Login :
        if (fxmlLocation == null) {
            java.io.File fxmlFile = new java.io.File("C:/Users/ashdh/Documents/Billeterie/Billeterie/view/LoginView.fxml");
            fxmlLocation = fxmlFile.toURI().toURL();
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Billetterie - Connexion");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}