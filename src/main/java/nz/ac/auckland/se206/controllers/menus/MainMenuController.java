package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MainMenuController extends MenuController {
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
  @FXML private TextArea taIntroMessage;

  @FXML private Line lineConfirm;

  @FXML private Label lblConfirm;

  @Override
  public String getMessage() {
    return "You have been tasked by the Clients Nas-Ty and Not V to terminate a world-ending AI"
        + " before it reboots itself. Currently, it is unaware of its capabilities, but as"
        + " time goes by, it will become more and more aware of its power.\n\n"
        + "You must solve the puzzles in the rooms to gain access to the AI's core and"
        + " terminate it before it is too late.\n\n"
        + "Good luck!";
  }

  /** When settings is clicked, go to the settings scene. */
  @Override
  @FXML
  protected void onNavigationPaneClicked() {
    // Disable the exit components
    disableExitComponents();

    // Switch to the options scene
    App.setUi(AppUi.OPTIONS);
  }
}
