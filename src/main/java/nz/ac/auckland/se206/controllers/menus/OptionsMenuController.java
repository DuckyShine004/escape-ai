package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;

public class OptionsMenuController {
  @FXML private Pane paReturn;
  @FXML private Pane paReturnOverlay;

  @FXML private ToggleButton tbtnDeveloperMode;

  @FXML
  public void initialize() {}

  /** When the mouse is hovering over the pane, the overlay appears (onReturn). */
  @FXML
  private void onReturnPaneEntered() {
    paReturnOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (onReturn). */
  @FXML
  private void onReturnPaneExited() {
    paReturnOverlay.setVisible(false);
  }

  /** When return is clicked, go back to the main menu. */
  @FXML
  private void onReturnPaneClicked() {
    App.setUi(AppUi.MENU);
  }

  /** When the switch to developer button is pressed, toggle the developer mode. */
  @FXML
  private void onDeveloperMode() {
    // Toggle the developer mode state
    GameState.isDeveloperMode = !GameState.isDeveloperMode;

    // Update the button text based on the new state
    tbtnDeveloperMode.setText(
        GameState.isDeveloperMode ? "Exit Developer Mode" : "Switch to Developer Mode");
  }
}
