package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.GameState.Difficulty;

public class OptionsMenuController {
  @FXML private Pane paReturn;
  @FXML private Pane paMainMenu;
  @FXML private Pane paEasyOverlay;
  @FXML private Pane paHardOverlay;
  @FXML private Pane paMediumOverlay;
  @FXML private Pane paReturnOverlay;
  @FXML private Pane paEasyTransparent;
  @FXML private Pane paHardTransparent;
  @FXML private Pane paMediumTransparent;
  @FXML private Pane paTwoMinutesOverlay;
  @FXML private Pane paSixMinutesOverlay;
  @FXML private Pane paFourMinutesOverlay;
  @FXML private Pane paTwoMinutesTransparent;
  @FXML private Pane paSixMinutesTransparent;
  @FXML private Pane paFourMinutesTransparent;

  @FXML private Line lineEasy;
  @FXML private Line lineHard;
  @FXML private Line lineMedium;
  @FXML private Line lineTwoMinutes;
  @FXML private Line lineSixMinutes;
  @FXML private Line lineFourMinutes;

  @FXML private ToggleButton tbtnDeveloperMode;

  @FXML
  public void initialize() {}

  /** When the mouse is hovering over the pane, the overlay appears (easy). */
  @FXML
  private void onEasyPaneEntered() {
    paEasyOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (easy). */
  @FXML
  private void onEasyPaneExited() {
    paEasyOverlay.setVisible(false);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (hard). */
  @FXML
  private void onHardPaneExited() {
    paHardOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (hard). */
  @FXML
  private void onHardPaneEntered() {
    paHardOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (medium). */
  @FXML
  private void onMediumPaneExited() {
    paMediumOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (medium). */
  @FXML
  private void onMediumPaneEntered() {
    paMediumOverlay.setVisible(true);
  }

  /** When the mouse is hovering over the pane, the overlay appears (return). */
  @FXML
  private void onReturnPaneEntered() {
    paReturnOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (return). */
  @FXML
  private void onReturnPaneExited() {
    paReturnOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (two minutes). */
  @FXML
  private void onTwoMinutesPaneEntered() {
    paTwoMinutesOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (two minutes). */
  @FXML
  private void onTwoMinutesPaneExited() {
    paTwoMinutesOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (six minutes). */
  @FXML
  private void onSixMinutesPaneEntered() {
    paSixMinutesOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (six minutes). */
  @FXML
  private void onSixMinutesPaneExited() {
    paSixMinutesOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (four minutes). */
  @FXML
  private void onFourMinutesPaneEntered() {
    paFourMinutesOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (four minutes). */
  @FXML
  private void onFourMinutesPaneExited() {
    paFourMinutesOverlay.setVisible(false);
  }

  /** When easy is clicked, set the difficulty to easy. */
  @FXML
  private void onEasyPaneClicked() {
    setDifficultyEasy();
  }

  /** When hard is clicked, set the difficulty to hard. */
  @FXML
  private void onHardPaneClicked() {
    setDifficultyHard();
  }

  /** When medium is clicked, set the difficulty to medium. */
  @FXML
  private void onMediumPaneClicked() {
    setDifficultyMedium();
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
  private void onDeveloperMode() {
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

    // Set the easy underline visible
    lineEasy.setVisible(true);

    // Set the hard underline invisible
    lineHard.setVisible(false);

    // Set the medium underline invisible
    lineMedium.setVisible(false);
  }

  /** Set the game's difficulty to hard. */
  private void setDifficultyHard() {
    // Set the difficulty to hard
    GameState.gameDifficulty = Difficulty.HARD;

    // Set max hints to zero
    GameState.maxHints = 0;

    // Set the amount of hints to zero
    GameState.hintCounter = 0;

    // Set the easy underline invisible
    lineEasy.setVisible(false);

    // Set the hard underline visible
    lineHard.setVisible(true);

    // Set the medium underline invisible
    lineMedium.setVisible(false);
  }

  /** Set the game's difficulty to medium. */
  private void setDifficultyMedium() {
    // Set the difficulty to medium
    GameState.gameDifficulty = Difficulty.MEDIUM;

    // Set max hints to 5
    GameState.maxHints = 5;

    // Set the amount of hints to 5
    GameState.hintCounter = 5;

    // Set the easy underline invisible
    lineEasy.setVisible(false);

    // Set the hard underline invisible
    lineHard.setVisible(false);

    // Set the medium underline invisible
    lineMedium.setVisible(true);
  }

  private void setTimeTwoMinutes() {
    // Set the time limit to two minutes
    GameState.maxTime = 120;

    // Set the two minutes underline visible
    lineTwoMinutes.setVisible(true);

    // Set the six minutes underline invisible
    lineSixMinutes.setVisible(false);

    // Set the four minutes underline invisible
    lineFourMinutes.setVisible(false);
  }

  private void setTimeSixMinutes() {
    // Set the time limit to two minutes
    GameState.maxTime = 360;

    // Set the two minutes underline invisible
    lineTwoMinutes.setVisible(false);

    // Set the six minutes underline visible
    lineSixMinutes.setVisible(true);

    // Set the four minutes underline invisible
    lineFourMinutes.setVisible(false);
  }

  private void setTimeFourMinutes() {
    // Set the time limit to two minutes
    GameState.maxTime = 240;

    // Set the two minutes underline invisible
    lineTwoMinutes.setVisible(false);

    // Set the six minutes underline invisible
    lineSixMinutes.setVisible(false);

    // Set the four minutes underline visible
    lineFourMinutes.setVisible(true);
  }
}
