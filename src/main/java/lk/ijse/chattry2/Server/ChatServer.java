package lk.ijse.chattry2.Server;
/**
 * --------------------------------------------
 * Author: Vihanga Nimsara(kvn2004)
 * GitHub: https://github.com/kvn2004
 * --------------------------------------------
 * Created: 5/23/2025 9:12 AM
 * Project: chattry2
 * --------------------------------------------
 **/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private static final int PORT = 12345;
    private static final Set<ClientHandler> clients = (new HashSet<>());

    public static void main(String[] args) {
        System.out.println("Server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                System.out.println("Client connected: " + socket.getPort());
                clients.add(handler);
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private String nickname;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                // Get nickname
                nickname = in.readUTF();
                broadcastText("🔵 " + nickname + " has joined the chat!");

                while (true) {
                    String type = in.readUTF();

                    if (type.equals("TEXT")) {
                        String message = in.readUTF();
                        broadcastText("💬 " + nickname + ": " + message);

                    } else if (type.equals("IMAGE")) {
                        int length = in.readInt();
                        byte[] imageBytes = new byte[length];
                        in.readFully(imageBytes);

                        broadcastImage(nickname, imageBytes);
                    }
                }

            } catch (IOException e) {

                System.out.println(nickname + " disconnected.");

            } finally {

                clients.remove(this);
                broadcastText("🔴 " + nickname + " has left the chat.");

                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }


        private void sendText(String message) throws IOException {
            out.writeUTF("TEXT");
            out.writeUTF(message);
            out.flush();
        }

        private void sendImage(String from, byte[] imageData) throws IOException {
            out.writeUTF("IMAGE");
            out.writeUTF(from);
            out.writeInt(imageData.length);
            out.write(imageData);
            out.flush();
        }

        private void broadcastText(String message) {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    try {
                        client.sendText(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void broadcastImage(String from, byte[] imageData) {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    try {
                        client.sendImage(from, imageData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
