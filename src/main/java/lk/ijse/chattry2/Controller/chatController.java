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

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class chatController {

    public Button btnImage;
    @FXML
    private VBox chatContainer;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public void setNickname(String nickname) {
        try {
            socket = new Socket("localhost", 12345);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            // Send nickname
            out.writeUTF(nickname);
            out.flush();

            // Background listener thread
            new Thread(() -> {
                try {
                    while (true) {
                        String type = in.readUTF();

                        if (type.equals("TEXT")) {
                            String msg = in.readUTF();
                            Platform.runLater(() ->
                                    chatContainer.getChildren().add(new Label(msg))
                            );
                        } else if (type.equals("IMAGE")) {
                            String sender = in.readUTF();
                            int length = in.readInt();
                            byte[] imageData = new byte[length];
                            in.readFully(imageData);

                            InputStream is = new ByteArrayInputStream(imageData);
                            Image image = new Image(is);
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            Platform.runLater(() -> {
                                chatContainer.getChildren().add(new Label(sender + " sent an image:"));
                                chatContainer.getChildren().add(imageView);
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            sendButton.setOnAction(event -> sendMessage());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            try {
                out.writeUTF("TEXT");
                out.writeUTF(message);
                out.flush();
                messageField.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                byte[] imageData = Files.readAllBytes(file.toPath());

                out.writeUTF("IMAGE");
                out.writeInt(imageData.length);
                out.write(imageData);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  void closeConnection() {
        try {
            if (out != null) {
                out.close();
            }

            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
