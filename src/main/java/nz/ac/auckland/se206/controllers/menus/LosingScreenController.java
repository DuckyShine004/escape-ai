package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LosingScreenController extends MenuController {
  @FXML private Pane paNo;
  @FXML private Pane paYes;
  @FXML private Pane paPlayAgain;
  @FXML private Pane paExit;
  @FXML private Pane paMainMenu;
  @FXML private Pane paNoOverlay;
  @FXML private Pane paYesOverlay;
  @FXML private Pane paPlayAgainOverlay;
  @FXML private Pane paExitOverlay;
  @FXML private Pane paMainMenuOverlay;
  @FXML private TextArea taMessage;

  @FXML private Line lineConfirm;

  @FXML private Label lblConfirm;

  @Override
  public String getMessage() {
    return "Mission failed!\n\n"
               + "The AI has rebooted to its previous, rogue state. It is unknown what it will do"
               + " next. Will it help human progress, or ultimately end? Only time will tell..";
  }

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
