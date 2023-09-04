package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.utilities.Timer;

public class MainMenuController {
  @FXML private Button btnStart;

  @FXML
  private void initialize() {}

  /*
   * This method will reset all the global fields in GameState
   *
   */
  private void resetGlobalVariables() {
    //
    GameState.isKeyFound = false;
    GameState.isRiddleResolved = false;
    GameState.riddlesSolved = 0;
  }

  @FXML
  private void onStartButton() {

    resetGlobalVariables();

    // retrieve the max time from gamestate and initialize the timer
    Timer.initialize(GameState.maxTime);

    // start the timer
    Timer.play();

    // change scene to Room
    App.setUi(AppUi.OFFICE);
  }

  @FXML
  private void onSelectDifficulty() {}

  @FXML
  private void onOptions() {

    // change scene to Options
    App.setUi(AppUi.OPTIONS);
  }
}
