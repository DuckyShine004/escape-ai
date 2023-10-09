package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.AudioManager;
import nz.ac.auckland.se206.AudioManager.Clip;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MainMenuController extends MenuController {
  @FXML private Pane paNo;
  @FXML private Pane paYes;
  @FXML private Pane paPlay;
  @FXML private Pane paExit;
  @FXML private Pane paMainMenu;
  @FXML private Pane paNavigation;
  @FXML private Pane paNoOverlay;
  @FXML private Pane paYesOverlay;
  @FXML private Pane paPlayOverlay;
  @FXML private Pane paExitOverlay;
  @FXML private Pane paNavigationOverlay;
  @FXML private TextArea taIntroMessage;

  @FXML private Line lineConfirm;

  @FXML private Label lblConfirm;

  /** When settings is clicked, go to the settings scene. */
  @Override
  @FXML
  protected void onNavigationPaneClicked() {
    // Play the selection sound effect
    AudioManager.loadAudio(Clip.SELECTION);

    // Disable the exit components
    // disableExitComponents();

    // Switch to the options scene
    App.setUi(AppUi.OPTIONS);
  }
}
