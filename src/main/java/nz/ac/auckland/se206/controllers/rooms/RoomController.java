package nz.ac.auckland.se206.controllers.rooms;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.ChatManager;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;

public abstract class RoomController {
  @FXML private Pane paOffice;

  @FXML private Label lblTime;

  @FXML private Button btnHint;
  @FXML private Button btnLeft;
  @FXML private Button btnRight;

  @FXML private TextArea taChat;
  @FXML private TextField tfChat;

  @FXML private Rectangle recOpaque;

  @FXML private ImageView imgAvatar;
  @FXML private ImageView imgAvatarShaddow;
  @FXML private ImageView imgEmotion;

  @FXML
  protected void onAiClicked(MouseEvent event) {
    GameState.muted = GameState.muted == false;
    GameState.tts.stop();
    ChatManager.toggleAiMuted();
    GameState.isChatting = !GameState.isChatting;
    if (GameState.isChatting) {
      taChat.setVisible(true);
      tfChat.setVisible(true);
      recOpaque.setVisible(true);
      
    } else {
      taChat.setVisible(false);
      tfChat.setVisible(false);
      recOpaque.setVisible(false);
    }
  }

  @FXML
  private void onMouseEnterAi(Event event) {
    // enter
    imgAvatarShaddow.setVisible(true);
  }

  @FXML
  private void onMouseExitAi(Event event) {
    // enter
    imgAvatarShaddow.setVisible(false);
  }

  /**
   * Check if there is a keyboard event. If there is a keyboard event, handle the event
   * appropriately.
   *
   * @param keyEvent this event is generated when a key is pressed, released, or typed
   */
  @FXML
  private void onKeyPressed(KeyEvent keyEvent) {
    String userInput = "";

    // get the user input from the chat text field
    if (keyEvent.getCode() == KeyCode.ENTER) {
      userInput = tfChat.getText();
    }

    // trim the user input
    userInput = userInput.trim();

    // check if the user input is empty
    if (userInput == null || userInput.isEmpty()) {
      return;
    }

    // initialize user chat message object
    ChatMessage userMessage;

    // create a new instance of user chat message object
    userMessage = new ChatMessage("user", userInput);

    // append the user's response to the text area
    ChatManager.setUserResponse(userInput);

    // get chatGPT's response and append it to the chatting text area
    ChatManager.getChatResponse(userMessage, false);
  }
}
