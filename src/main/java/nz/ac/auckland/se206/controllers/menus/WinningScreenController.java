package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.AudioManager;
import nz.ac.auckland.se206.AudioManager.Clip;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class WinningScreenController extends MenuController {
  @FXML private Pane paNo;
  @FXML private Pane paYes;
  @FXML private Pane paExit;
  @FXML private Pane paPlay;
  @FXML private Pane paNavigation;
  @FXML private Pane paBackground;
  @FXML private Pane paQuitDialogue;

  /** Initialize the winning screen controller. */
  @FXML
  private void initialize() {}

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
}
