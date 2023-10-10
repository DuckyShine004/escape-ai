package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LosingScreenController extends MenuController {
  @FXML private Pane paNo;
  @FXML private Pane paYes;
  @FXML private Pane paPlayAgain;
  @FXML private Pane paExit;
  @FXML private Pane paMenu;
  @FXML private Pane paSelect;

  /** Initialize the losing screen controller. */
  @FXML
  private void initialize() {}

  /** When back to main menu is clicked, go back to main menu */
  @Override
  @FXML
  protected void onNavigationPaneClicked() {
    App.setUi(AppUi.MENU);
  }
}
