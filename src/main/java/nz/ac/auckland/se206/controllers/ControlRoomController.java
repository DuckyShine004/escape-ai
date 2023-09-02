package nz.ac.auckland.se206.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the control room scene. */
public class ControlRoomController {
  @FXML private Text timerText;

  @FXML private Button leftButton;
  @FXML private Button rightButton;

  /** Initializes the control room. */
  @FXML
  private void initialize() {
    // update the timer text to match the current timer.
    updateScene();
  }

  /** On mouse clicked, if the button is pressed, then switch to the left scene. */
  @FXML
  private void onLeftButton() {}

  /** On mouse clicked, if the button is pressed, then switch to the right scene. */
  @FXML
  private void onRightButton() {}

  /**
   * Update all things related to timing here. Such an example is using animation timer to update
   * the timer text on each frame.
   */
  private void updateScene() {
    AnimationTimer animationTimer =
        new AnimationTimer() {
          @Override
          public void handle(long time) {
            timerText.setText(Timer.getTime());
          }
        };

    animationTimer.start();
  }
}
