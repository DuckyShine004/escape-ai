package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LosingScreenController extends MenuController {
  @FXML private Pane paNo;
  @FXML private Pane paYes;
  @FXML private Pane paPlayAgain;
  @FXML private Pane paExit;
  @FXML private Pane paMenu;
  @FXML private Pane paSelect;

  @FXML private Line lineConfirm;

  @FXML private Label lblConfirm;

  /** When back to main menu is clicked, go back to main menu */
  @Override
  @FXML
  protected void onNavigationPaneClicked() {
    // Disable the exit components
    disableExitComponents();

    // Switch to the options scene
    App.setUi(AppUi.MENU);
  }
}
