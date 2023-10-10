package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.AudioManager;
import nz.ac.auckland.se206.AudioManager.Clip;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LosingScreenController extends MenuController {
  @FXML private Pane paNo;
  @FXML private Pane paYes;
  @FXML private Pane paExit;
  @FXML private Pane paFail;
  @FXML private Pane paSelect;
  @FXML private Pane paConfirm;
  @FXML private Pane paPlayAgain;
  @FXML private Pane paQuitDialogue;

  /** Initialize the losing screen controller. */
  @FXML
  private void initialize() {}

  /** When no is clicked, do not exit the application. */
  @FXML
  private void onNoPaneClicked() {
    // Play the selection sound effect
    AudioManager.loadAudio(Clip.SELECTION);

    // Toggle both the fail and quit dialogue panes
    paFail.setVisible(true);
    paQuitDialogue.setVisible(false);
  }

  /** When exit is clicked, exit the application. */
  @FXML
  private void onExitPaneClicked() {
    // Play the selection sound effect
    AudioManager.loadAudio(Clip.SELECTION);

    // Toggle both the fail and quit dialogue panes
    paFail.setVisible(false);
    paQuitDialogue.setVisible(true);
  }

  /** When back to main menu is clicked, go back to main menu */
  @Override
  @FXML
  protected void onNavigationPaneClicked() {
    // Play the selection sound effect
    AudioManager.loadAudio(Clip.SELECTION);

    // Change the scene to main menu
    App.setUi(AppUi.MENU);
  }
}
