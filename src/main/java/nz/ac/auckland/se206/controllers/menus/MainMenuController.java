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
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.utilities.Timer;

public class MainMenuController {
  @FXML private Pane paNo;
  @FXML private Pane paYes;
  @FXML private Pane paPlay;
  @FXML private Pane paExit;
  @FXML private Pane paSettings;
  @FXML private Pane paNoOverlay;
  @FXML private Pane paYesOverlay;
  @FXML private Pane paPlayOverlay;
  @FXML private Pane paExitOverlay;
  @FXML private Pane paSettingsOverlay;
  @FXML private TextArea taIntroMessage;

  @FXML private Line lineConfirm;

  @FXML private Label lblConfirm;

  private String introMessage =
      "You have been tasked by the Clients Nas-Ty and Not V to terminate a world-ending AI before"
          + " it reboots itself. Currently, it is unaware of its capabilities, but as time goes by,"
          + " it will become more and more aware of its power.\n\n"
          + "You must solve the puzzles in the rooms to gain access to the AI's core and terminate"
          + " it before it is too late.\n\n"
          + "Good luck!";
  private StringProperty introMessageProperty = new SimpleStringProperty(introMessage);

  /** Initialize the main scene. */
  @FXML
  private void initialize() {
    taIntroMessage.textProperty().bind(introMessageProperty);
    introMessageProperty.set(introMessage);
  }

  // Define a getter for the StringProperty
  public StringProperty introMessageProperty() {
    return introMessageProperty;
  }

  // Define a setter for the StringProperty
  public void setFinalMessage(String introMessage) {
    introMessageProperty.set(introMessage);
  }

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

  /** When the mouse is hovering over the pane, the overlay appears (settings). */
  @FXML
  private void onSettingsPaneEntered() {
    paSettingsOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (settings). */
  @FXML
  private void onSettingsPaneExited() {
    paSettingsOverlay.setVisible(false);
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
  private void onSettingsPaneClicked() {
    // Disable the exit components
    disableExitComponents();

    // Switch to the options scene
    App.setUi(AppUi.OPTIONS);
  }

  /** This method will reset all the global fields in GameState. */
  private void resetGlobalVariables() {
    GameState.isRiddleResolved = false;
    GameState.isLogicGateSolved = false;
    GameState.isDecryptionSolved = false;
    GameState.riddlesSolved = 0;
    GameState.isSolved = false;
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
  }

  /** Disables the exit components of the menu. */
  private void disableExitComponents() {
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
