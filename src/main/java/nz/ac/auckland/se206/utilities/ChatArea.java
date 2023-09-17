package nz.ac.auckland.se206.utilities;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.ChatManager;

/**
 * The chat area class is a custom component that contains a text area, a text field, and a send
 * button
 */
public class ChatArea extends VBox {
  private TextArea taChat;
  private TextField tfInput;
  private Button btnSend;

  /** Constructor for the chat area */
  public ChatArea() {
    super();

    taChat = new TextArea();
    tfInput = new TextField();
    btnSend = new Button("Send");

    // Set properties for components
    taChat.setEditable(false);
    taChat.setWrapText(true);

    // Set properties for layout
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

  /** Set the chat content */
  public void setChatContent(String content) {
    taChat.setText(content);
  }

  /**
   * Get the chat content
   *
   * @return chat content
   */
  public String getChatContent() {
    return taChat.getText();
  }

  /**
   * Get the text area
   *
   * @return text area
   */
  public TextArea getTextArea() {
    return taChat;
  }

  /**
   * Get the input field
   *
   * @return input field
   */
  public TextField getInputField() {
    return tfInput;
  }

  /**
   * Get the send button
   *
   * @return send button
   */
  public Button getSendButton() {
    return btnSend;
  }

  /** Append the message to the chat area and clear the input field */
  private void sendMessage() {
    // Get the message from the input field
    String message = tfInput.getText().trim();

    // Append the message to the chat area and clear the input field
    if (!message.isEmpty()) {
      taChat.appendText(message + "\n");
      tfInput.clear();

      // Update the chat content in the chat manager
      String currentChatContent = ChatManager.getChatContent();
      currentChatContent += message + "\n";
      ChatManager.setChatContent(currentChatContent);
    }
  }
}
