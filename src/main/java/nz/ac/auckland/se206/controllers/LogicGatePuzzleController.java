package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LogicGatePuzzleController {
  @FXML private Label lblTimer;

  /*
   * This method changes the scene back to the Breaker Room
   */
  @FXML
  private void onBackToBreaker() {
    App.setUi(AppUi.BREAKER);
  }
}
