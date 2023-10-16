package nz.ac.auckland.se206.controllers.menus;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.AudioManager;
import nz.ac.auckland.se206.AudioManager.Clip;
import nz.ac.auckland.se206.LeaderboardManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;

/**
 * The controller for the winning screen, which handles offers the user to play again or return to
 * main menu. This class extends {@link MenuController}.
 */
public class WinningScreenController extends MenuController {
  public static StringProperty spTimeBest;
  public static StringProperty spTimeTaken;
  
  @FXML private Pane paNo;
  @FXML private Pane paYes;
  @FXML private Pane paExit;
  @FXML private Pane paPlay;
  @FXML private Pane paNavigation;
  @FXML private Pane paBackground;
  @FXML private Pane paQuitDialogue;

  @FXML private Label lblTimeBest;
  @FXML private Label lblTimeTaken;

  @FXML private TextArea taMessage;

  private StringProperty spMessage;

  /** Initialize the winning screen controller. */
  @FXML
  private void initialize() {
    // Initialize and bind the text area's string property
    spMessage = new SimpleStringProperty(GameState.finalMessage);
    taMessage.textProperty().bind(spMessage);

    // Initialize the time labels
    initializeTimeLabels();
  }

  /** When no is clicked, do not exit the application. */
  @FXML
  private void onNoPaneClicked() {
    // Play the selection sound effect
    AudioManager.loadAudio(Clip.SELECTION);

    // Toggle both the background and quit dialogue panes
    paBackground.setVisible(true);
    paQuitDialogue.setVisible(false);
  }

  /** When exit is clicked, exit the application. */
  @FXML
  private void onExitPaneClicked() {
    // Play the selection sound effect
    AudioManager.loadAudio(Clip.SELECTION);

    // Toggle both the background and quit dialogue panes
    paBackground.setVisible(false);
    paQuitDialogue.setVisible(true);
  }

  @Override
  @FXML
  protected void onNavigationPaneClicked() {
    // Play the selection sound effect
    AudioManager.loadAudio(Clip.SELECTION);

    // Switch to the options scene
    App.setUi(AppUi.MENU);
  }

  private void initializeTimeLabels() {
    // Best time by the player
    spTimeBest = new SimpleStringProperty(LeaderboardManager.timeBestMessage);
    lblTimeBest.textProperty().bind(spTimeBest);

    // Time taken by the player
    spTimeTaken = new SimpleStringProperty(LeaderboardManager.timeTakenMessage);
    lblTimeTaken.textProperty().bind(spTimeTaken);
  }
}
