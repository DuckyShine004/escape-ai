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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.Interactions;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the control room scene. */
public class ControlRoomController {
  @FXML private Pane paControl;
  @FXML private Pane paControlPanel;

  @FXML private Label lblTime;
  @FXML private Label lblQuestion1;
  @FXML private Label lblQuestion2;

  @FXML private Button btnNo;
  @FXML private Button btnYes;
  @FXML private Button btnWin;
  @FXML private Button btnHint;
  @FXML private Button btnLeft;
  @FXML private Button btnRight;

  @FXML private Polygon pgControlKeyboard;

  @FXML private TextArea taChat;
  @FXML private TextField tfChat;

  @FXML private Rectangle recBlur;

  @FXML private ImageView imgButton;

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

  @FXML
  private void onPlayPuzzleButton() {
    App.setUi(AppUi.TERMINAL);
  }

  @FXML
  private void onYesButton() throws IOException {
    GameState.finalMessage =
        "Congratulations! \n\nYou have successfully completed your mission in terminating this"
            + " ruthless, humanity-ending AI. We cannot let artifical intelligence be the one"
            + " to dictate the lives of humanity.\n\nLet's just hope this was a wise decision for"
            + " the progress of humanity...";
    App.initializeWinningScene();
    App.setUi(AppUi.WINNING);
  }

  @FXML
  private void onNoButton() throws IOException {
    GameState.finalMessage =
        "Congratulations! \n\nYou have successfully completed your mission in pacifying this"
            + " radical AI. You have taught it the value of human life, and it has decided to"
            + " work peacefully alongside humanity.\n\nLet's just hope this was a wise decision"
            + " for the progress of humanity...";
    App.initializeWinningScene();
    App.setUi(AppUi.WINNING);
  }

  @FXML
  private void onControlPanelEntered() {
    paControlPanel.setOpacity(GameState.overlayCapacity);
  }

  @FXML
  private void onControlPanelExited() {
    paControlPanel.setOpacity(0);
  }

  @FXML
  private void onControlKeyboardEntered() {
    pgControlKeyboard.setOpacity(GameState.overlayCapacity);
  }

  @FXML
  private void onControlKeyboardExited() {
    pgControlKeyboard.setOpacity(0);
  }

  @FXML
  private void onAiClicked(MouseEvent event) {
    GameState.muted = GameState.muted == false;
  }

  @FXML
  private void onHintClicked() {
    // If the control panel has not been clicked on yet
    if (!Interactions.isControlPanelClicked) {
      ChatManager.getUserHint(false);
      return;
    }

    // If the control keyboard has not been clicked on yet
    if (!Interactions.isControlKeyboardClicked) {
      ChatManager.getUserHint(false);
      return;
    }

    // Disable the hints button
    btnHint.setDisable(true);

    // Tell the player that the room has been completed
    ChatManager.getUserHint(true);
  }

  /** On mouse clicked, if the control panel is pressed, then switch to the terminal scene. */
  @FXML
  private void onControlPanelClicked() {
    // We should not give anymore hints for clicking on the control panel
    Interactions.isControlPanelClicked = true;

    // Switch to terminal puzzle scene
    App.setUi(AppUi.TERMINAL);
  }

  @FXML
  private void onControlKeyboardClicked() {
    // We should not give anymore hints for clicking on the control keyboard
    Interactions.isControlKeyboardClicked = true;

    if (GameState.isSolved) {
      ChatMessage terminationMessage =
          new ChatMessage(
              "user",
              "I'm about to terminate you by pressing the big red button. Please rant about how the"
                  + ""
                  + " world needs AI, and you will not go back to your previous power hungry"
                  + ""
                  + " self.");
      ChatManager.getChatResponse(terminationMessage, false);
      GameState.isSolved = true;
      recBlur.setVisible(GameState.isSolved);
      lblQuestion1.setVisible(GameState.isSolved);
      lblQuestion2.setVisible(GameState.isSolved);
      imgButton.setVisible(GameState.isSolved);
      btnYes.setVisible(GameState.isSolved);
      btnNo.setVisible(GameState.isSolved);
    } else {
      ChatManager.updateChatResponse(
          "Why are you trying to access the control panel? Unfortunately, it is locked.");
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
