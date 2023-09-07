package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the decryption puzzle scene. */
public class DecryptionPuzzleController {
  @FXML private Button btnBack;

  /** Initializes the decryption puzzle. */
  @FXML
  private void initialize() {}

  /** On mouse clicked, if the button is pressed, then switch to the control room scene. */
  @FXML
  private void onBackButton() {
    App.setUi(AppUi.CONTROL);
  }
}
