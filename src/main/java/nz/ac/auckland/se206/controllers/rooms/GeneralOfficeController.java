package nz.ac.auckland.se206.controllers.rooms;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.HintManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.Interactions;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.utilities.Timer;

/**
 * The controller for the office room, which gives all the functionality for the office room
 * scene. This class extends {@link RoomController}.
 */
public class GeneralOfficeController extends RoomController {

  @FXML private Label lblTime;
  @FXML private Label lblHintCounter;

  @FXML private Button btnLeft;
  @FXML private Button btnRight;

  @FXML private Polygon pgHint;
  @FXML private Polygon pgDesktop;

  @FXML private TextArea taChat;
  @FXML private TextField tfChat;

  @FXML private ImageView imgAvatar;
  @FXML private ImageView imgEmotion;

  /** Initializes the general office. */
  @FXML
  protected void initialize() {
    super.initialize();
    // Add the label to list of labels to be updated
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

  @FXML
  private void onHintEntered() {
    pgHint.setOpacity(0.25);
  }

  @FXML
  private void onHintExited() {
    pgHint.setOpacity(0);
  }

  @FXML
  private void onDesktopEntered() {
    pgDesktop.setOpacity(GameState.overlayCapacity);
  }

  @FXML
  private void onDesktopExited() {
    pgDesktop.setOpacity(0);
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void onDesktopClicked(MouseEvent event) throws IOException {
    // We should not give anymore hints for clicking on the desktop
    Interactions.isDesktopClicked = true;

    if (!GameState.isRiddleResolved) {
      GameState.currentRoom = AppUi.RIDDLE;
      GameState.tts.stop(); // mute current tts message
      App.setRoot("puzzles/riddle");
      return;
    }
  }

  @FXML
  @Override
  public void onHintClicked(MouseEvent mouseEvent) {
    super.onHintClicked(mouseEvent);

    // If the desktop has not been clicked on yet
    if (!Interactions.isDesktopClicked) {
      getUserHint(false);
      return;
    }

    // Tell the player that the room has been completed
    getUserHint(true);
  }

  @Override
  protected String getRoomHint() {
    return GptPromptEngineering.getOfficeRoomHint();
  }
}
