package nz.ac.auckland.se206.controllers.menus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;

public class OptionsMenuController {

  // define comboBox which contains String type selections
  @FXML private ComboBox<String> cmbTimeSelect;

  @FXML private Label lblCurrentSelectedTime;

  @FXML private ToggleButton tbtnDeveloperMode;

  @FXML
  public void initialize() {

    // define 3 drop down selection items
    cmbTimeSelect.getItems().addAll("2 Minutes", "4 Minutes", "6 Minutes");

    // defult 2 minutes
    cmbTimeSelect.setPromptText("2 Minutes");

    // debug label to show time is properly changing
    lblCurrentSelectedTime.setText("Time: " + GameState.maxTime + " Seconds");
  }

  @FXML
  private void onBackToMenu() {
    // change back to main menu
    App.setUi(AppUi.MENU);
  }

  @FXML
  private void onDeveloperMode() {
    // Toggle the developer mode state
    GameState.isDeveloperMode = !GameState.isDeveloperMode;

    // Update the button text based on the new state
    tbtnDeveloperMode.setText(
        GameState.isDeveloperMode ? "Exit Developer Mode" : "Switch to Developer Mode");
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
    String strSelected = cmbTimeSelect.getSelectionModel().getSelectedItem();

    // get the number from the char in position 1
    // int maxTime = strSelected.charAt(0) - 48; // -48 shifts char of '1' to int value 1
    int maxTime = firstNumberFromString(strSelected);

    // set Game State time per round in seconds
    GameState.maxTime = maxTime * 60;

    // chaning debug label
    lblCurrentSelectedTime.setText("Time: " + GameState.maxTime + " Seconds");
  }

  /*
   * This method takes in a string, and returns the entire first number as a int
   */
  private static int firstNumberFromString(String input) {
    StringBuilder number = new StringBuilder();

    // loop through the string
    for (char c : input.toCharArray()) {

      // if the character is a digit
      if (Character.isDigit(c)) {

        // add digit to number
        number.append(c);
      } else {

        // when no more digits
        break;
      }
    }

    // if number is found, return the number, else return null
    return number.length() > 0 ? Integer.parseInt(number.toString()) : null;
  }
}
