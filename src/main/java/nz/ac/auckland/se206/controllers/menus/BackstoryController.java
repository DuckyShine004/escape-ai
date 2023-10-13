package nz.ac.auckland.se206.controllers.menus;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.AudioManager;
import nz.ac.auckland.se206.AudioManager.Clip;
import nz.ac.auckland.se206.HintManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.Interactions;
import nz.ac.auckland.se206.utilities.Timer;

/**
 * The controller for the backstory screen, which gives the user the backstory for the game before
 * the game begins.
 */
public class BackstoryController {
  @FXML private Pane paSelect;
  @FXML private Pane paContinue;

  /** Initialize the backstory controller. */
  @FXML
  private void initialize() {}

  /** When the cursor is hovered over this pane, set the select icon visible. */
  @FXML
  private void onContinueEntered() {
    AudioManager.loadAudio(Clip.MAKING_SELECTION);
    paSelect.setVisible(true);
  }

  /** When the cursor is not hovered over this pane, set the select icon invisible. */
  @FXML
  private void onContinueExited() {
    paSelect.setVisible(false);
  }

  /** When the continue is clicked, start the game. */
  @FXML
  private void onContinueClicked() {
    AudioManager.loadAudio(Clip.SELECTION);
    startGame();
  }

  /** Starts the game. */
  private void startGame() {
    resetGlobalVariables();

    try {
      SceneManager.onResetLevel();
    } catch (IOException e) {
      // on error print stack trace
      e.printStackTrace();
    }

    // Set the timer's countdown time
    Timer.setTime(GameState.maxTime);

    // start the timer
    if (!GameState.isDeveloperMode) {
      Timer.play();
    }

    // Initialize the hint counter components
    HintManager.initializeHintCounter();

    // Change scene to office
    App.setUi(AppUi.OFFICE);
  }

  /** This method will reset all the global fields in GameState. */
  private void resetGlobalVariables() {
    // Reset riddle puzzle solved
    GameState.isRiddleResolved = false;

    // Reset logic gate puzzle solved
    GameState.isLogicGateSolved = false;

    // Reset decryption puzzle solved
    GameState.isDecryptionSolved = false;

    // Reset global game solved
    GameState.isSolved = false;

    // Reset amount of riddles solved
    GameState.riddlesSolved = 0;

    // Reset the amount of hints user has
    GameState.hintCounter = GameState.maxHints;

    // Reset all interactions
    Interactions.reset();

    // Reset the chatting status
    GameState.isChatting = false;

    // Reset the TTS
    GameState.isSpeaking = false;

    // Reset the AI message
    GameState.currentAiMessage = "";

    // Reset the player message
    GameState.currentPlayerMessage = "";

    // Reset the number of times the backstory has been updated
    GameState.backStoryUpdated = 0;
  }
}
