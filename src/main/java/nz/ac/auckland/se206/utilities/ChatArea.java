package nz.ac.auckland.se206.utilities;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ChatArea extends VBox {
  private TextArea taChat;
  private TextField tfInput;
  private Button btnSend;

  public ChatArea() {
    super();

    taChat = new TextArea();
    tfInput = new TextField();
    btnSend = new Button("Send");

    // Set properties for components
    taChat.setEditable(false);
    taChat.setWrapText(true);

    HBox inputBox = new HBox(tfInput, btnSend);
    HBox.setHgrow(tfInput, Priority.ALWAYS);
    btnSend.setMaxWidth(Double.MAX_VALUE);

    getChildren().addAll(taChat, inputBox);

    setVgrow(taChat, Priority.ALWAYS);

    // Styling
    setSpacing(10);
    setAlignment(Pos.CENTER);

    // Event handlers
    btnSend.setOnAction(e -> sendMessage());
    tfInput.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            sendMessage();
          }
        });
  }

  public void setChatContent(String content) {
    taChat.setText(content);
  }

  public String getChatContent() {
    return taChat.getText();
  }

  public TextArea getTextArea() {
    return taChat;
  }

  public TextField getInputField() {
    return tfInput;
  }

  public Button getSendButton() {
    return btnSend;
  }

  private void sendMessage() {
    String message = tfInput.getText().trim();
    if (!message.isEmpty()) {
      taChat.appendText(message + "\n");
      tfInput.clear();
    }
  }
}
