package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the control room scene. */
public class GeneralOfficeController {
  @FXML private Pane paOffice;

  @FXML private Label lblTime;

  @FXML private Button btnLeft;
  @FXML private Button btnRight;

  @FXML private Button door;
  @FXML private Rectangle vase;

  @FXML private TextArea taChat;
  @FXML private TextField tfChat;

  /** Initializes the general office. */
  @FXML
  private void initialize() {
    // add the label to list of labels to be updated.
    Timer.addLabel(lblTime);

    // Add the text area and text field to the list of chat components
    ChatManager.addChatComponents(taChat, tfChat);
  }

  /**
   * On mouse clicked, if the button is pressed, then switch to the left scene.
   *
   * @throws IOException
   */
  @FXML
  private void onLeftButton() throws IOException {
    App.setUi(AppUi.CONTROL);
  }

  /**
   * On mouse clicked, if the button is pressed, then switch to the right scene.
   *
   * @throws IOException
   */
  @FXML
  private void onRightButton() throws IOException {
    App.setUi(AppUi.BREAKER);
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void onDoorClicked(MouseEvent event) throws IOException {
    System.out.println("door clicked");

    if (!GameState.isRiddleResolved) {
      GameState.currentRoom = AppUi.RIDDLE;
      App.setRoot("puzzles/riddle");
      return;
    }

    if (!GameState.isKeyFound) {
      System.out.println("key found");
    } else {
      App.setUi(AppUi.WINNING);
    }
  }

  /**
   * Handles the click event on the vase.
   *
   * @param event the mouse event
   */
  @FXML
  public void clickVase(MouseEvent event) {
    System.out.println("vase clicked");
    if (GameState.isRiddleResolved && !GameState.isKeyFound) {
      GameState.isKeyFound = true;
    }
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
