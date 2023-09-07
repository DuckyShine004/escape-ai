package nz.ac.auckland.se206.controllers.rooms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the control room scene. */
public class ControlRoomController {
  @FXML private Label lblTime;

  @FXML private Button btnLeft;
  @FXML private Button btnRight;
  @FXML private Button btnPlayPuzzle;

  /** Initializes the control room. */
  @FXML
  private void initialize() {
    // add the label to list of labels to be updated.
    Timer.addLabel(lblTime);
  }

  /** On mouse clicked, if the button is pressed, then switch to the left scene. */
  @FXML
  private void onLeftButton() {
    App.setUi(AppUi.BREAKER);
  }

  /** On mouse clicked, if the button is pressed, then switch to the right scene. */
  @FXML
  private void onRightButton() {
    App.setUi(AppUi.OFFICE);
  }

  /** On mouse clicked, if the button is pressed, then switch to the decryption puzzle scene. */
  @FXML
  private void onPlayPuzzleButton() {
    App.setUi(AppUi.DECRYPTION);
  }
}
