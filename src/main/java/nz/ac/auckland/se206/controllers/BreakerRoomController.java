package nz.ac.auckland.se206.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the breaker room. */
public class BreakerRoomController {
  @FXML Text timerText;

  @FXML
  private void initialize() {
    // update the scene on every frame.
    updateScene();
  }

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
