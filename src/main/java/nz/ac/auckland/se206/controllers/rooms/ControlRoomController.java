package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
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
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the control room scene. */
public class ControlRoomController {
  @FXML private Pane paControl;

  @FXML private Label lblTime;

  @FXML private Button btnLeft;
  @FXML private Button btnRight;
  @FXML private Button btnPlayPuzzle;
  @FXML private Button btnWin;

  @FXML private TextArea taChat;
  @FXML private TextField tfChat;
  @FXML private Rectangle recBlur;
  @FXML private Label lblQuestion1;
  @FXML private Label lblQuestion2;
  @FXML private ImageView imgButton;
  @FXML private Button btnYes;
  @FXML private Button btnNo;

  /** Initializes the control room. */
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
    App.setUi(AppUi.BREAKER);
  }

  /**
   * On mouse clicked, if the button is pressed, then switch to the right scene.
   *
   * @throws IOException
   */
  @FXML
  private void onRightButton() throws IOException {
    App.setUi(AppUi.OFFICE);
  }

  /** On mouse clicked, if the button is pressed, then switch to the terminal scene. */
  @FXML
  private void onPlayPuzzleButton() {
    App.setUi(AppUi.TERMINAL);
  }

  @FXML 
  private void onYesButton() {
    App.setUi(AppUi.WINNING);
  }

  @FXML
  private void onNoButton() {
    App.setUi(AppUi.WINNING);
  }

  @FXML
  private void onAiClicked(MouseEvent event) {
    GameState.muted = GameState.muted == false;
  }

  @FXML
  private void onCheckWinButton() {
    if (GameState.isDecryptionSolved && GameState.isRiddleResolved && GameState.isLogicGateSolved) {
      ChatMessage terminationMessage = new ChatMessage("user", "I'm about to terminate you by pressing the big red button. Please rant about how the world needs AI, and you will not go back to your previous power hungry self.");
      ChatManager.getChatResponse(terminationMessage, false);
      GameState.isSolved = true;
      recBlur.setVisible(GameState.isSolved);
      lblQuestion1.setVisible(GameState.isSolved);
      lblQuestion2.setVisible(GameState.isSolved);
      imgButton.setVisible(GameState.isSolved);
      btnYes.setVisible(GameState.isSolved);
      btnNo.setVisible(GameState.isSolved);
    } else {
      ChatManager.updateChatResponse("Why are you trying to access the control panel? Unfortunately, it is locked." );
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
