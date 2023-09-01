package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;

public class WinningScreenController {
  @FXML private Button playAgainButton;

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
  private void onPlayAgain() {

    resetGlobalVariables();

    // change scene to Room
    App.setUi(AppUi.ROOM);
  }

  @FXML
  private void onViewGameStats() {}

  @FXML
  private void onMainMenu() {}
}
