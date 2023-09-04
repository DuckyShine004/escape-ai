package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.utilities.*;

public class LogicGatePuzzleController {
  @FXML private Label lblTimer;

  @FXML private ImageView imgGate1;
  @FXML private ImageView imgTable1;

  private List<LogicGate> logicGates;

  private void setUpLogicGates() {
    System.out.println("setting up gates");
    logicGates = new ArrayList<>();
    logicGates.add(new LogicGate(LogicGate.Logic.AND)); // add AND gate to list

    // add side bar helper images
    addHelperGateImgs();
  }

  private void addHelperGateImgs() {
    imgGate1.setImage(logicGates.get(0).getImage());
    imgTable1.setImage(logicGates.get(0).getTable());
  }

  @FXML
  private void initialize() {
    setUpLogicGates();
  }

  /*
   * This method changes the scene back to the Breaker Room
   */
  @FXML
  private void onBackToBreaker() {
    App.setUi(AppUi.BREAKER);
  }
}
