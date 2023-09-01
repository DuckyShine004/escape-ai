package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

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
}
