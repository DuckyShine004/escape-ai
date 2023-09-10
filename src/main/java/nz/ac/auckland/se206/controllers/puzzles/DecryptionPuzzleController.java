package nz.ac.auckland.se206.controllers.puzzles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;

/** Controller class for the decryption puzzle scene. */
public class DecryptionPuzzleController {
  @FXML private Label lblSequence;

  @FXML private Button btn1;
  @FXML private Button btn2;
  @FXML private Button btn3;
  @FXML private Button btn4;
  @FXML private Button btn5;
  @FXML private Button btn6;
  @FXML private Button btn7;
  @FXML private Button btn8;
  @FXML private Button btn9;

  @FXML private Button btnBack;
  @FXML private Button btnClear;
  @FXML private Button btnEnter;
  @FXML private Button btnBackSpace;

  /** Initializes the decryption puzzle. */
  @FXML
  private void initialize() {}

  /** On mouse clicked, if the button is pressed, then switch to the control room scene. */
  @FXML
  private void onBackButton() {
    App.setUi(AppUi.CONTROL);
  }

  /** On mouse clicked, if the button is pressed, clear the player's current sequence input. */
  @FXML
  private void onClearButton() {
    lblSequence.setText("");
  }

  /** On mouse clicked, if the button is pressed, remove the last digit from the sequence. */
  @FXML
  private void onBackSpaceButton() {
    int length = lblSequence.getText().length();

    // check if the input is already zero
    if (length == 0) {
      return;
    }

    lblSequence.setText(lblSequence.getText().substring(0, length - 1));
  }

  /** On mouse clicked, if a number button is pressed, add the number to the sequence. */
  @FXML
  private void onNumberButton(ActionEvent event) {
    // check if the player's input is too long
    if (lblSequence.getText().length() > GameState.maxSequence) {
      return;
    }

    lblSequence.setText(lblSequence.getText() + getButtonIndex(event));
  }

  /**
   * On mouse clicked, return the index of the button pressed.
   *
   * @param event the event representing the type of action.
   * @return the button index of the button pressed.
   */
  public String getButtonIndex(ActionEvent event) {
    String buttonIndex = ((Control) event.getSource()).getId();

    return buttonIndex.substring(buttonIndex.length() - 1);
  }
}
