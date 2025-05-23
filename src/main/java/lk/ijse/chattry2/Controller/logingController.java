package lk.ijse.chattry2.Controller;

/**
 * --------------------------------------------
 * Author: Vihanga Nimsara(kvn2004)
 * GitHub: https://github.com/kvn2004
 * --------------------------------------------
 * Created: 5/23/2025 9:12 AM
 * Project: chattry2
 * --------------------------------------------
 **/


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class logingController {
    @FXML
    private TextField nicknameField;

    @FXML
    private Button joinButton;

    @FXML
    void joinChat(ActionEvent event) {
        try {
            String nickname = nicknameField.getText();
            if (nickname.isEmpty()) return;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/chattry2/chat-view.fxml"));
            Parent root = loader.load();

            chatController chatController = loader.getController();
            chatController.setNickname(nickname);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle(nickname + "'s Chat Room");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnCloseRequest(e ->{
                chatController.closeConnection();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
