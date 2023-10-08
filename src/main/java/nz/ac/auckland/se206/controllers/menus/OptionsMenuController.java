package nz.ac.auckland.se206.controllers.menus;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.GameState.Difficulty;

public class OptionsMenuController {
  @FXML private Pane paReturn;
  @FXML private Pane paMainMenu;
  @FXML private Pane paReturnOverlay;

  @FXML private ToggleButton tbtnDeveloperMode;

  @FXML
  public void initialize() {}

  /** When the mouse is hovering over the arrows, the overlay appears. */
  @FXML
  private void onArrowEntered(Event event) {
    ((Polygon) event.getSource()).setFill(Color.rgb(97, 219, 224));
  }

  /** When the mouse is not hovering over the arrows, the overlay disappears. */
  @FXML
  private void onArrowExited(Event event) {
    ((Polygon) event.getSource()).setFill(Color.BLACK);
  }

  /** M. */
  @FXML
  private void onReturnPaneEntered() {
    paReturnOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (return). */
  @FXML
  private void onReturnPaneExited() {
    paReturnOverlay.setVisible(false);
  }

  /** When return is clicked, go back to the main menu. */
  @FXML
  private void onReturnPaneClicked() {
    App.setUi(AppUi.MENU);
  }

  /** When two minutes is clicked, set the time limit to two minutes. */
  @FXML
  private void onTwoMinutesPaneClicked() {
    setTimeTwoMinutes();
  }

  /** When six minutes is clicked, set the time limit to six minutes. */
  @FXML
  private void onSixMinutesPaneClicked() {
    setTimeSixMinutes();
  }

  /** When four minutes is clicked, set the time limit to four minutes. */
  @FXML
  private void onFourMinutesPaneClicked() {
    setTimeFourMinutes();
  }

  /** When the switch to developer button is pressed, toggle the developer mode. */
  @FXML
  private void onDeveloperModeClicked() {
    // Toggle the developer mode state
    GameState.isDeveloperMode = !GameState.isDeveloperMode;

    // Update the button text based on the new state
    tbtnDeveloperMode.setText(
        GameState.isDeveloperMode ? "Exit Developer Mode" : "Switch to Developer Mode");
  }

  /** Set the game's difficulty to easy. */
  private void setDifficultyEasy() {
    // Set the difficulty to easy
    GameState.gameDifficulty = Difficulty.EASY;

    // Set max hints to infinity
    GameState.maxHints = Integer.MAX_VALUE;

    // Set the amount of hints to infinity
    GameState.hintCounter = Integer.MAX_VALUE;
  }

  /** Set the game's difficulty to hard. */
  private void setDifficultyHard() {
    // Set the difficulty to hard
    GameState.gameDifficulty = Difficulty.HARD;

    // Set max hints to zero
    GameState.maxHints = 0;

    // Set the amount of hints to zero
    GameState.hintCounter = 0;
  }

  /** Set the game's difficulty to medium. */
  private void setDifficultyMedium() {
    // Set the difficulty to medium
    GameState.gameDifficulty = Difficulty.MEDIUM;

    // Set max hints to 5
    GameState.maxHints = 5;

    // Set the amount of hints to 5
    GameState.hintCounter = 5;
  }

  private void setTimeTwoMinutes() {
    // Set the time limit to two minutes
    GameState.maxTime = 120;
  }

  private void setTimeSixMinutes() {
    // Set the time limit to two minutes
    GameState.maxTime = 360;
  }

  private void setTimeFourMinutes() {
    // Set the time limit to two minutes
    GameState.maxTime = 240;
  }
}
