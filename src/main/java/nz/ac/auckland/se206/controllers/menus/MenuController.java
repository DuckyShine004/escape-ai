package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.HintManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.AudioManager;
import nz.ac.auckland.se206.AudioManager.Clip;
import nz.ac.auckland.se206.SceneManager.AppUi;

public abstract class MenuController {
  @FXML private Pane paNo;
  @FXML private Pane paYes;
  @FXML private Pane paPlay;
  @FXML private Pane paExit;
  @FXML private Pane paSelect;
  @FXML private Pane paConfirm;
  @FXML private Pane paMainMenu;
  @FXML private Pane paNavigation;
  @FXML private Pane paQuitDialogue;
  @FXML private Pane paNoOverlay;
  @FXML private Pane paYesOverlay;

  @FXML private Line lineConfirm;

  @FXML private Label lblConfirm;

  /** Initialize the controller. */
  @FXML
  private void initialize() {}

  /** When the mouse is hovering over the pane, the overlay appears (no). */
  @FXML
  private void onNoPaneEntered() {
    AudioManager.loadAudio(Clip.MAKING_SELECTION);
    paConfirm.setLayoutX(203);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (no). */
  @FXML
  private void onNoPaneExited() {
    paConfirm.setLayoutX(-320);
  }

  /** When the mouse is hovering over the pane, the overlay appears (yes). */
  @FXML
  private void onYesPaneEntered() {
    AudioManager.loadAudio(Clip.MAKING_SELECTION);
    paConfirm.setLayoutX(77);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (yes). */
  @FXML
  private void onYesPaneExited() {
    paConfirm.setLayoutX(-320);
  }

  /** When the mouse is hovering over the pane, the overlay appears (play). */
  @FXML
  private void onPlayPaneEntered() {
    AudioManager.loadAudio(Clip.MAKING_SELECTION);
    paSelect.setLayoutY(247.5);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (play). */
  @FXML
  private void onPlayPaneExited() {
    paSelect.setLayoutY(-30);
  }

  /** When the mouse is hovering over the pane, the overlay appears (exit). */
  @FXML
  private void onExitPaneEntered() {
    AudioManager.loadAudio(Clip.MAKING_SELECTION);
    paSelect.setLayoutY(337.5);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (exit). */
  @FXML
  private void onExitPaneExited() {
    paSelect.setLayoutY(-30);
  }

  /** When the mouse is hovering over the pane, the overlay appears (navigation). */
  @FXML
  private void onNavigationPaneEntered() {
    AudioManager.loadAudio(Clip.MAKING_SELECTION);
    paSelect.setLayoutY(292.5);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (navigation). */
  @FXML
  private void onNavigationPaneExited() {
    paSelect.setLayoutY(-30);
  }

  /** When no is clicked, do not exit the application. */
  @FXML
  private void onNoPaneClicked() {
    // Play the selection sound effect
    AudioManager.loadAudio(Clip.SELECTION);

    // Set the quit dialogue box visible
    paQuitDialogue.setVisible(false);

    // Disable the exit components
    // disableExitComponents();
  }

  /** When yes is clicked, exit the application. */
  @FXML
  private void onYesPaneClicked() {
    System.exit(0);
  }

  /** When play is clicked, start the game. */
  @FXML
  private void onPlayPaneClicked() {
    // Play the selection sound effect
    AudioManager.loadAudio(Clip.SELECTION);

    // Reset the layout of the selection hitbox
    paSelect.setLayoutY(-30);

    // Change scene to backstory
    App.setUi(AppUi.BACKSTORY);
  }

  /** When exit is clicked, exit the application. */
  @FXML
  private void onExitPaneClicked() {
    paQuitDialogue.setVisible(true);
  }

  /** When settings is clicked, switch the scene to options scene. */
  @FXML
  protected abstract void onNavigationPaneClicked();

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

    // change scene to Room
    App.setUi(AppUi.OFFICE);
  }

  /** Enables the exit components of the menu. */
  private void enableExitComponents() {
    // Set the underline visible
    lineConfirm.setVisible(true);

    // Set the confirmation label visible
    lblConfirm.setVisible(true);

    // Set the no pane visible
    paNo.setVisible(true);

    // Set the yes pane visible
    paYes.setVisible(true);

    // Enable the no pane
    paNo.setDisable(false);

    // Enable the yes pane
    paYes.setDisable(false);
  }

  /** Disables the exit components of the menu. */
  protected void disableExitComponents() {
    // Set the underline invisible
    lineConfirm.setVisible(false);

    // Set the confirmation label invisible
    lblConfirm.setVisible(false);

    // Set the no pane invisible
    paNo.setVisible(false);

    // Set the yes pane invisible
    paYes.setVisible(false);

    // Disable the no pane
    paNo.setDisable(true);

    // Disable the yes pane
    paYes.setDisable(true);
  }
}
