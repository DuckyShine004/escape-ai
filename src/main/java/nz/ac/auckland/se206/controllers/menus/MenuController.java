package nz.ac.auckland.se206.controllers.menus;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ChatManager;
import nz.ac.auckland.se206.HintManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.utilities.Timer;

public abstract class MenuController {
  @FXML private Pane paNo;
  @FXML private Pane paYes;
  @FXML private Pane paPlay;
  @FXML private Pane paExit;
  @FXML private Pane paNavigation;
  @FXML private Pane paNoOverlay;
  @FXML private Pane paYesOverlay;
  @FXML private Pane paPlayOverlay;
  @FXML private Pane paExitOverlay;
  @FXML private Pane paNavigationOverlay;
  @FXML private TextArea taMessage;

  @FXML private Line lineConfirm;

  @FXML private Label lblConfirm;

  private StringProperty messageProperty = new SimpleStringProperty("");

  /** Initialize the controller. */
  @FXML
  private void initialize() {
    taMessage.textProperty().bind(messageProperty);
    messageProperty.set(getMessage());
  }

  // Define a getter for the StringProperty
  public StringProperty mssageProperty() {
    return messageProperty;
  }

  // Define a setter for the StringProperty
  protected void setMessage(String message) {
    messageProperty.set(message);
  }

  public abstract String getMessage();

  /** When the mouse is hovering over the pane, the overlay appears (no). */
  @FXML
  private void onNoPaneEntered() {
    paNoOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (no). */
  @FXML
  private void onNoPaneExited() {
    paNoOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (yes). */
  @FXML
  private void onYesPaneEntered() {
    paYesOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (yes). */
  @FXML
  private void onYesPaneExited() {
    paYesOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (play). */
  @FXML
  private void onPlayPaneEntered() {
    paPlayOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (play). */
  @FXML
  private void onPlayPaneExited() {
    paPlayOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (exit). */
  @FXML
  private void onExitPaneEntered() {
    paExitOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (exit). */
  @FXML
  private void onExitPaneExited() {
    paExitOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (navigation). */
  @FXML
  private void onNavigationPaneEntered() {
    paNavigationOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (navigation). */
  @FXML
  private void onNavigationPaneExited() {
    paNavigationOverlay.setVisible(false);
  }

  /** When no is clicked, do not exit the application. */
  @FXML
  private void onNoPaneClicked() {
    disableExitComponents();
  }

  /** When yes is clicked, exit the application. */
  @FXML
  private void onYesPaneClicked() {
    System.exit(0);
  }

  /** When play is clicked, start the game. */
  @FXML
  private void onPlayPaneClicked() {
    // Disable the exit components
    disableExitComponents();

    // Start the game
    startGame();
  }

  /** When exit is clicked, exit the application. */
  @FXML
  private void onExitPaneClicked() {
    enableExitComponents();
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

    // Clear the chat manager
    ChatManager.reset();

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

    // Remove visibility of the text area
    taMessage.setVisible(false);

    // TODO: Stop the timer
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

    // Enable visibility of the text area
    taMessage.setVisible(true);
  }
}
