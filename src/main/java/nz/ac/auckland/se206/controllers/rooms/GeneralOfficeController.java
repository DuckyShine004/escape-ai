package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.utilities.ChatArea;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the control room scene. */
public class GeneralOfficeController {
  @FXML private Label lblTime;

  @FXML private Button btnLeft;
  @FXML private Button btnRight;

  @FXML private Button door;
  @FXML private Rectangle vase;

  @FXML private ChatArea chatArea;

  private SceneManager sceneManager;

  /** Initializes the general office. */
  @FXML
  private void initialize() {
    // add the label to list of labels to be updated.
    Timer.addLabel(lblTime);

    sceneManager = SceneManager.getInstance();
    String chatContent = ChatManager.getChatContent();
    chatArea.setChatContent(chatContent);
  }

  /**
   * On mouse clicked, if the button is pressed, then switch to the left scene.
   *
   * @throws IOException
   */
  @FXML
  private void onLeftButton() throws IOException {
    sceneManager.setChatContent(chatArea.getChatContent());
    App.initializeControlScene();
    App.setUi(AppUi.CONTROL);
  }

  /**
   * On mouse clicked, if the button is pressed, then switch to the right scene.
   *
   * @throws IOException
   */
  @FXML
  private void onRightButton() throws IOException {
    sceneManager.setChatContent(chatArea.getChatContent());
    App.initializeBreakerScene();
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
      GameState.tts.stop(); // mute current tts message
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

  @FXML
  private void onAiClicked() {
    GameState.muted = GameState.muted == false;
  }
}
