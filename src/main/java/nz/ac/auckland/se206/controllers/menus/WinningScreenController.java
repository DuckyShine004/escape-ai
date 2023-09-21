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

public class WinningScreenController {
  @FXML private Button btnPlayAgain;
  @FXML private Button btnMainMenu;
  @FXML private Button btnViewGameStats;

  /** Initialize the controller. */
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
    GameState.isLogicGateSolved = false;
    GameState.riddlesSolved = 0;
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

    // set the timer's countdown time
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

  private String getFinalMessage() {
    if (GameState.isYesPressed) {
      return "Congratulations! You have successfully completed your mission in terminating this"
                 + " ruthless, humanity-ending AI. We cannot let artifical intelligence be the one"
                 + " to dictate the lives of humanity. Let's just hope this was a wise decision for"
                 + " the progress of humanity...";
    } else {
      return "Congratulations! You have successfully completed your mission in pacifying this"
                 + " radical AI. You have taught it the value of human life, and it has decided to"
                 + " work peacefully alongside humanity. Let's just hope this was a wise decision"
                 + " for the progress of humanity...";
    }
  }
}
