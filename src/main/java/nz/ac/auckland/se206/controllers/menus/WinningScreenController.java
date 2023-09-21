package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;

public class WinningScreenController extends MenuController {
  @FXML private Pane paNo;
  @FXML private Pane paYes;
  @FXML private Pane paPlayAgain;
  @FXML private Pane paExit;
  @FXML private Pane paNavigation;
  @FXML private Pane paNoOverlay;
  @FXML private Pane paYesOverlay;
  @FXML private Pane paPlayAgainOverlay;
  @FXML private Pane paExitOverlay;
  @FXML private Pane paNavigationOverlay;
  @FXML private TextArea taMessage;

  @FXML private Line lineConfirm;

  @FXML private Label lblConfirm;

  @Override
  public String getMessage() {
    return GameState.finalMessage;
  }

  @Override
  @FXML
  protected void onNavigationPaneClicked() {
    // Disable the exit components
    disableExitComponents();

    // Switch to the options scene
    App.setUi(AppUi.MENU);
  }
}
