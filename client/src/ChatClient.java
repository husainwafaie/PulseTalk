import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ChatClient extends Application {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private TextArea messages;
    private TextField input;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        socket = new Socket("localhost", 1234);  // same port as server
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        VBox root = new VBox();
        messages = new TextArea();
        messages.setEditable(false);
        input = new TextField();
        input.setPromptText("Type your message...");

        input.setOnAction(event -> sendMessage());

        root.getChildren().addAll(messages, input);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PulseTalk Client");
        primaryStage.show();

        new Thread(() -> receiveMessages()).start();
    }

    private void sendMessage() {
        String message = input.getText();
        writer.println(message);
        input.clear();
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                messages.appendText(message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
