package nz.ac.auckland.se206.controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the breaker room. */
public class BreakerRoomController {
  @FXML Label timerText;

  @FXML
  private void initialize() {
    // update the scene on every frame.
    updateScene();
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
