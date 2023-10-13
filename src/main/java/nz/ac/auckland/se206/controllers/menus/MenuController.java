package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.AudioManager;
import nz.ac.auckland.se206.AudioManager.Clip;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * The abstract controller for menus, which contains all the methods shared across the main menu,
 * winning screen and losing screen.
 */
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
}
