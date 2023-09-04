package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LogicGatePuzzleController {
  @FXML private Label lblTimer;

  @FXML
  private void onBackToBreaker(ActionEvent event) {
    App.setUi(AppUi.BREAKER);
  }
}
