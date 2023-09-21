package nz.ac.auckland.se206.controllers.menus;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.utilities.Timer;

public class LosingScreenController {
  @FXML private Button btnPlayAgain;

  @FXML
  private void initialize() {}

  /*
   * This method will reset all the global fields in GameState
   *
   */
  private void resetGlobalVariables() {
    GameState.isRiddleResolved = false;
    GameState.isLogicGateSolved = false;
    GameState.isDecryptionSolved = false;
    GameState.riddlesSolved = 0;
    GameState.isSolved = false;
  }

  /*
   * This method will restart the game
   */
  @FXML
  private void onPlayAgain() {

    resetGlobalVariables();

    // try to reset the levels
    try {
      SceneManager.onResetLevel();
    } catch (IOException e) {
      // on error print stack trace
      e.printStackTrace();
    }

    // retrieve the max time from gamestate and initialize the timer
    Timer.setTime(GameState.maxTime);

    // start the timer
    Timer.play();

    // Clear the chat manager
    ChatManager.reset();

    // change scene to Office
    App.setUi(AppUi.OFFICE);
  }

  /*
   * This method will show the game stats
   */
  @FXML
  private void onViewGameStats() {}

  /*
   * This method will set the scene to the main menu
   */
  @FXML
  private void onBackToMenu() {
    App.setUi(AppUi.MENU);
  }
}
