package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // CORRECTION : On utilise getResource avec le nom d'un fichier qui EXISTE
        // Si ton fichier s'appelle InscriptionView.fxml :
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InscriptionView.fxml"));

        // Si tu crées un fichier LoginView.fxml plus tard, tu changeras juste le nom ici

        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setTitle("Billetterie - Bienvenue");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}