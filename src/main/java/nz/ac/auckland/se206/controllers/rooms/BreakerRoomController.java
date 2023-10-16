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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.HintManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.Interactions;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.utilities.Timer;

/**
 * The controller for the breaker room, which gives all the functionality for the breaker room
 * scene. This class extends {@link RoomController}.
 */
public class BreakerRoomController extends RoomController {

  @FXML private Pane paCircuitBox;

  @FXML private Button btnHint;

  @FXML private Label lblTime;
  @FXML private Label lblHintCounter;

  @FXML private Polygon pgHint;

  @FXML private TextArea taChat;
  @FXML private TextField tfChat;

  @FXML private Rectangle recOpaque;

  @FXML private ImageView imgAvatar;
  @FXML private ImageView imgAvatarShaddow;
  @FXML private ImageView imgEmotion;

  /** This method initializes the breaker room. */
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
    App.setUi(AppUi.OFFICE);
  }

  /**
   * On mouse clicked, if the button is pressed, then switch to the right scene.
   *
   * @throws IOException
   */
  @FXML
  private void onRightButton() throws IOException {
    App.setUi(AppUi.CONTROL);
  }

  /**
   * When the cursor is hovered over the hint button, the hint button is highlighted.
   */
  @FXML
  private void onHintEntered() {
    pgHint.setOpacity(0.25);
  }

  /**
   * When the cursor is not hovered over the hint button, the hint button is not highlighted.
   */
  @FXML
  private void onHintExited() {
    pgHint.setOpacity(0);
  }

  /**
   * When the cursor is hovered over the circuit box, the circuit box is highlighted.
   */
  @FXML
  private void onCircuitBoxEntered() {
    paCircuitBox.setOpacity(GameState.overlayCapacity);
  }

  /**
   * When the cursor is not hovered over the circuit box, the circuit box is not highlighted.
   */
  @FXML
  private void onCircuitBoxExited() {
    paCircuitBox.setOpacity(0);
  }

  /** This method changes the scene to the logic puzzle. */
  @FXML
  private void onCircuitBoxClicked() {
    // We should not give anymore hints for clicking on the circuit box
    Interactions.isCircuitBoxClicked = true;

    // Switch to the logic gate puzzle
    App.setUi(AppUi.LOGIC_PUZZLE);
  }

  /**
   * This method handles the click event on the hint button.
   *
   * @param mouseEvent the mouse event
   */
  @FXML
  @Override
  public void onHintClicked(MouseEvent mouseEvent) {
    super.onHintClicked(mouseEvent);

    // If the circuit box has not been clicked on yet
    if (!Interactions.isCircuitBoxClicked) {
      getUserHint(false);
      return;
    }

    // Tell the player that the room has been completed
    getUserHint(true);
  }

  /** This method gets the hint for the breaker room. */
  @Override
  protected String getRoomHint() {
    return GptPromptEngineering.getBreakerRoomHint();
  }
}
