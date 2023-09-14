package nz.ac.auckland.se206.controllers.menus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.GameState.Difficulty;

public class OptionsMenuController {

  // define comboBox which contains String type selections
  @FXML private ComboBox<String> cmbTimeSelect;
  @FXML private ComboBox<String> cmbDifficultySelect;

  @FXML private Label lblCurrentSelectedTime;

  @FXML private ToggleButton tbtnDeveloperMode;

  @FXML
  public void initialize() {

    // sets up all the combo boxes
    setUpComboBoxes();
  }

  /** This method calls the set up methods for each combo box */
  private void setUpComboBoxes() {
    // set up time combo box
    setUpTimeBox();

    // set up difficulty combo box
    setUpDifficulty();
  }

  /** This method sets up the comboBox for the time limit */
  private void setUpTimeBox() {
    // define 3 drop down selection items
    cmbTimeSelect.getItems().addAll("2 Minutes", "4 Minutes", "6 Minutes");

    // defult 2 minutes
    cmbTimeSelect.setPromptText("2 Minutes");

    // debug label to show time is properly changing
    lblCurrentSelectedTime.setText("Time: " + GameState.maxTime + " Seconds");
  }

  /** This method sets up the comboBox for the game difficulty */
  private void setUpDifficulty() {
    // define 3 drop down selection items
    cmbDifficultySelect
        .getItems()
        .addAll(
            Difficulty.EASY.toString(), Difficulty.MEDIUM.toString(), Difficulty.HARD.toString());

    // defult 2 minutes
    cmbDifficultySelect.setPromptText(GameState.gameDifficulty.toString());
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

  @FXML
  private void onDifficultySelect(ActionEvent event) {
    // get the string from the comboBox "2 Minutes"
    String strSelected = cmbDifficultySelect.getSelectionModel().getSelectedItem();

    // changes difficulty based on difficulty selected
    switch (strSelected) {
      case "EASY":
        GameState.gameDifficulty = Difficulty.EASY;
        break;
      case "MEDIUM":
        GameState.gameDifficulty = Difficulty.MEDIUM;
        break;
      case "HARD":
        GameState.gameDifficulty = Difficulty.HARD;
        break;
    }
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
