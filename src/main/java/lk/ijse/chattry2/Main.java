package lk.ijse.chattry2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.chattry2.Server.ChatServer;

/**
 * --------------------------------------------
 * Author: Vihanga Nimsara(kvn2004)
 * GitHub: https://github.com/kvn2004
 * --------------------------------------------
 * Created: 5/23/2025 9:12 AM
 * Project: chattry2
 * --------------------------------------------
 **/



public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        new Thread(() -> {
            try {
                System.out.println("Starting Chat Server...");
                ChatServer.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/chattry2/login-view.fxml"));
        primaryStage.setTitle("Group Chat - Login");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
