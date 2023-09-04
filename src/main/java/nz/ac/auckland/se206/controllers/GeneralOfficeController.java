package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the control room scene. */
public class GeneralOfficeController {
  @FXML private Label lblTime;

  @FXML private Button leftButton;
  @FXML private Button rightButton;

  @FXML private Rectangle door;
  @FXML private Rectangle vase;

  /** Initializes the control room. */
  @FXML
  private void initialize() {
    // update the scene
    updateScene();
  }

  /** On mouse clicked, if the button is pressed, then switch to the left scene. */
  @FXML
  private void onLeftButton() {
    App.setUi(AppUi.CONTROL);
  }

  /** On mouse clicked, if the button is pressed, then switch to the right scene. */
  @FXML
  private void onRightButton() {
    App.setUi(AppUi.BREAKER);
  }

  /**
   * Handles the click event on the door.
   *
   * @param event the mouse event
   * @throws IOException if there is an error loading the chat view
   */
  @FXML
  public void clickDoor(MouseEvent event) throws IOException {
    System.out.println("door clicked");

    if (!GameState.isRiddleResolved) {
      App.setRoot("riddle");
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

  /**
   * Update all things related to timing here. Such an example is using animation timer to update
   * the timer text on each frame.
   */
  private void updateScene() {
    AnimationTimer animationTimer =
        new AnimationTimer() {
          @Override
          public void handle(long time) {
            lblTime.setText(Timer.getTime());
          }
        };

    animationTimer.start();
  }
}
