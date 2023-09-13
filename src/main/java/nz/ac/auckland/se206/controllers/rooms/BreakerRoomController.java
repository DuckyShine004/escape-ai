package nz.ac.auckland.se206.controllers.rooms;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the breaker room. */
public class BreakerRoomController {
  @FXML private Label lblTime;

  /** Initialize the breaker room. */
  @FXML
  private void initialize() {
    Timer.addLabel(lblTime);
  }

  /** On mouse clicked, if the button is pressed, then switch to the left scene. */
  @FXML
  private void onLeftButton() {
    App.setUi(AppUi.OFFICE);
  }

  /** On mouse clicked, if the button is pressed, then switch to the right scene. */
  @FXML
  private void onRightButton() {
    App.setUi(AppUi.CONTROL);
  }

  /** Change the scene to the logic puzzle */
  @FXML
  private void onOpenLogicGatePuzzle() {
    App.setUi(AppUi.LOGIC_PUZZLE);
  }
}
