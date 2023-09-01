package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/** Controller class for the control room scene. */
public class GeneralOfficeController {
  @FXML private Text timerText;

  @FXML private Button leftButton;
  @FXML private Button rightButton;

  /** Initializes the control room. */
  @FXML
  private void initialize() {}

  /** On mouse clicked, if the button is pressed, then switch to the left scene. */
  @FXML
  private void onLeftButton() {}

  /** On mouse clicked, if the button is pressed, then switch to the right scene. */
  @FXML
  private void onRightButton() {}
}
