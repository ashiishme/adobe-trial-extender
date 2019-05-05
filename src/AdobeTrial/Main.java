package AdobeTrial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Adobe Trial Extender");
        primaryStage.setScene(new Scene(root, 350, 300));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("AdobeTrial/adobe-trial-icon.png"));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);

    }
}