package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;

public class OptionsMenuController {

  @FXML private ComboBox<String> cbTimeSelect;

  @FXML private Label lblCurrentSelectedTime;

  @FXML
  public void initialize() {
    System.out.println("loading options menu");
    cbTimeSelect.getItems().addAll("2 Minutes", "4 Minutes", "6 Minutes");
    cbTimeSelect.setPromptText("2 Minutes");
  }

  @FXML
  private void onBackToMenu() {
    // change back to main menu
    App.setUi(AppUi.MENU);
  }

  /*
   * This is the on select item in comboBox method
   * This method will change the Game State variable for max Time in round
   * @params takes input event, which is the source and values associated with changing comboBox value
   *
   * This method asumes the time per round will ONLY be 1 character long
   */
  @FXML
  private void onTimeSelect(ActionEvent event) {

    // get the string from the comboBox "2 Minutes"
    String strSelected = cbTimeSelect.getSelectionModel().getSelectedItem();

    // get the number from the char in position 1
    int maxTime = strSelected.charAt(0) - 48; // -48 shifts char of '1' to int value 1

    GameState.maxTime = maxTime;
  }
}
