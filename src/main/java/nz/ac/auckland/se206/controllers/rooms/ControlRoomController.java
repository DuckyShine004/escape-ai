package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.AudioManager;
import nz.ac.auckland.se206.HintManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.Interactions;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the control room scene. */
public class ControlRoomController extends RoomController {
  @FXML private Pane paButton;
  @FXML private Pane paControlPanel;

  @FXML private Label lblTime;
  @FXML private Label lblEye1;
  @FXML private Label lblEye2;
  @FXML private Label lblHintCounter;

  @FXML private Circle crcEye1;
  @FXML private Circle crcEye2;

  @FXML private Button btnNo;
  @FXML private Button btnYes;
  @FXML private Button btnWin;
  @FXML private Button btnLeft;
  @FXML private Button btnRight;

  @FXML private Polygon pgHint;
  @FXML private Polygon pgControlKeyboard;

  @FXML private TextArea taChat;
  @FXML private TextField tfChat;

  @FXML private Rectangle recBlur;
  @FXML private Rectangle recOpaque;

  @FXML private ImageView imgButton;

  @FXML private ImageView imgAvatar;
  @FXML private ImageView imgAvatarShaddow;
  @FXML private ImageView imgEmotion;

  /** Initializes the control room. */
  @FXML
  protected void initialize() {
    super.initialize();
    // Add the label to list of labels to be updated.
    Timer.addLabel(lblTime);

    // Add the hint counter components
    HintManager.addHintComponents(lblHintCounter, pgHint);
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

  /**
   * On yes clicked, if the button is pressed, then switch to the winning scene.
   *
   * @throws IOException
   */
  @FXML
  private void onYesButton() throws Exception {
    // Set the final message
    GameState.finalMessage =
        "Outstanding work! You've neutralized the AI that threatened humanity. We mustn't let"
            + " artificial intelligence control our fate. May this choice pave the way for a"
            + " brighter future for all of us...";

    // Switch to the winning scene
    App.initializeWinningScene();
    App.setUi(AppUi.WINNING);

    // Stop the timer
    Timer.stop();

    // Stop the heartbeat sound effect
    AudioManager.stopHeartBeat();

    // Update the leaderboard - UNCOMMENT FOR FINAL
    // LeaderboardManager.update();
  }

  /**
   * On no clicked, if the button is pressed, then switch to the winning scene.
   *
   * @throws Exception
   */
  @FXML
  private void onNoButton() throws Exception {
    // Set the final message
    GameState.finalMessage =
        "Excellent work! You have taught the AI the value of human life and it has chosen to"
            + " harmoniously coexist with humanity. May this peace endure...";

    // Switch to the winning scene
    App.initializeWinningScene();
    App.setUi(AppUi.WINNING);

    // Stop the timer
    Timer.stop();

    // Stop the heartbeat sound effect
    AudioManager.stopHeartBeat();

    // Update the leaderboard - UNCOMMENT FOR FINAL
    // LeaderboardManager.update();
  }

  @FXML
  private void onHintEntered() {
    pgHint.setOpacity(0.25);
  }

  @FXML
  private void onHintExited() {
    pgHint.setOpacity(0);
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
  public void onHintClicked(MouseEvent mouseEvent) {
    super.onHintClicked(mouseEvent);

    // If the control panel has not been clicked on yet
    if (!Interactions.isControlPanelClicked) {
      getUserHint(false);
      return;
    }

    // If the control keyboard has not been clicked on yet
    if (!Interactions.isControlKeyboardClicked) {
      getUserHint(false);
      return;
    }

    // Tell the player that the room has been completed
    getUserHint(true);
  }

  /** On mouse clicked, if the control panel is pressed, then switch to the terminal scene. */
  @FXML
  private void onControlPanelClicked() throws IOException {
    // We should not give anymore hints for clicking on the control panel
    Interactions.isControlPanelClicked = true;

    // Initialize the terminal if player has not clicked on it before
    if (SceneManager.getUi(AppUi.TERMINAL) == null) {
      App.initializeTerminalScene();
    }

    // If the dialogue is paused, play it
    if (AudioManager.isDialoguePlaying()) {
      AudioManager.resumeDialogue();
    }

    // Switch to terminal puzzle scene
    App.setUi(AppUi.TERMINAL);
  }

  /** On mouse clicked, if the control keyboard is pressed, thne prompt the final question. */
  @FXML
  private void onControlKeyboardClicked() {
    // We should not give anymore hints for clicking on the control keyboard
    Interactions.isControlKeyboardClicked = true;

    // If all puzzles are solved, then we can terminate the AI
    if (GameState.isRiddleResolved && GameState.isLogicGateSolved && GameState.isDecryptionSolved) {
      // Play the heart beat sound effect
      AudioManager.playHeartBeat();

      // Create a new chat message
      ChatMessage terminationMessage =
          new ChatMessage(
              "user",
              "I'm about to terminate you by pressing the big red button. Please rant about how the"
                  + ""
                  + " world needs AI, and you will not go back to your previous power hungry"
                  + ""
                  + " self. Your response should not exceed 25 words.");

      // Append the user's response to the text area
      getChatResponse(terminationMessage, false);

      // Set game is solved to true
      GameState.isSolved = true;

      // Make visible all final question components
      recBlur.setVisible(true);
      paButton.setVisible(true);

      // Make AI invisible
      imgAvatar.setVisible(false);
      crcEye1.setOpacity(0);
      crcEye2.setOpacity(0);
      lblEye1.setOpacity(0);
      lblEye2.setOpacity(0);

      // Make the side buttons invisible
      btnLeft.setVisible(false);
      btnRight.setVisible(false);

      // Make the AI speech invisible and disable it
      lblAiChat2.setOpacity(0);
      lblAiChat2.setDisable(true);
    } else {
      // If the player has not solved all the puzzles, then we should not allow them to access the
      // final question
      setUserResponse("Let me access the control panel!");
      setAiMessage("Sorry, you have not solved all three puzzles yet to unlock the control panel.");
    }
  }

  /**
   * Gets the room hint for the user.
   *
   * @return the room hint for the user
   */
  @Override
  protected String getRoomHint() {
    return GptPromptEngineering.getControlRoomHint();
  }
}
