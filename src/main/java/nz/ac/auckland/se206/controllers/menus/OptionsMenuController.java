package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
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

  @FXML private Line lineEasy;
  @FXML private Line lineHard;
  @FXML private Line lineMedium;

  @FXML private ToggleButton tbtnDeveloperMode;

  @FXML
  public void initialize() {}

  /** When the mouse is hovering over the pane, the overlay appears (easy). */
  @FXML
  private void onEasyPaneEntered(MouseEvent mouseEvent) {
    paEasyOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (easy). */
  @FXML
  private void onEasyPaneExited(MouseEvent mouseEvent) {
    paEasyOverlay.setVisible(false);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (hard). */
  @FXML
  private void onHardPaneExited(MouseEvent mouseEvent) {
    paHardOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (hard). */
  @FXML
  private void onHardPaneEntered(MouseEvent mouseEvent) {
    paHardOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (medium). */
  @FXML
  private void onMediumPaneExited(MouseEvent mouseEvent) {
    paMediumOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (medium). */
  @FXML
  private void onMediumPaneEntered(MouseEvent mouseEvent) {
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

    // Set the easy underline invisible
    lineEasy.setVisible(false);

    // Set the hard underline visible
    lineHard.setVisible(true);

    // Set the medium underline invisible
    lineMedium.setVisible(true);
  }

  /** Set the game's difficulty to medium. */
  private void setDifficultyMedium() {
    // Set the difficulty to medium
    GameState.gameDifficulty = Difficulty.MEDIUM;

    // Set the easy underline invisible
    lineEasy.setVisible(false);

    // Set the hard underline invisible
    lineHard.setVisible(false);

    // Set the medium underline invisible
    lineMedium.setVisible(true);
  }
}
