package NEODatabase;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(new Image("file:login-icon.png"));
        primaryStage.setScene(Login.setupLoginWindow());

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
