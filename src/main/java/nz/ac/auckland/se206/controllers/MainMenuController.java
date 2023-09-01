package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;

public class MainMenuController {
  @FXML private Button startButton;

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
  }

  @FXML
  private void onStartButton() {

    resetGlobalVariables();

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
