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

  // begin side bar helper Gate : Table ImageViews
  @FXML private ImageView imgGate1;
  @FXML private ImageView imgTable1;

  @FXML private ImageView imgGate2;
  @FXML private ImageView imgTable2;

  @FXML private ImageView imgGate3;
  @FXML private ImageView imgTable3;

  @FXML private ImageView imgGate4;
  @FXML private ImageView imgTable4;

  @FXML private ImageView imgGate5;
  @FXML private ImageView imgTable5;

  @FXML private ImageView imgGate6;
  @FXML private ImageView imgTable6;

  // Logic Gate list
  // 0 - AND
  // 1 - NAND
  // 2 - OR
  // 3 - NOR
  // 4 - XOR
  // 5 - XNOR
  private List<LogicGate> logicGates;

  /**
   * This Method sets up the logicGate array list
   *
   * <p>Logic Gate list slots :: 0-AND :: 1-NAND :: 2-OR :: 3-NOR :: 4-XOR :: 5-XNOR
   */
  private void setUpLogicGates() {

    // new arraylist
    logicGates = new ArrayList<>();

    // add AND GATE
    logicGates.add(new LogicGate(LogicGate.Logic.AND)); // AND
    logicGates.add(new LogicGate(LogicGate.Logic.NAND)); // NAND
    logicGates.add(new LogicGate(LogicGate.Logic.OR)); // OR
    logicGates.add(new LogicGate(LogicGate.Logic.NOR)); // NOR
    logicGates.add(new LogicGate(LogicGate.Logic.XOR)); // XOR
    logicGates.add(new LogicGate(LogicGate.Logic.XNOR)); // XNOR

    // add side bar helper images
    addHelperGateImgs();
  }

  private void addHelperGateImgs() {
    //  loading side bar slot
    imgGate1.setImage(logicGates.get(0).getImage());
    imgTable1.setImage(logicGates.get(0).getTable());

    // loading side bar slot
    imgGate2.setImage(logicGates.get(1).getImage());
    imgTable2.setImage(logicGates.get(1).getTable());

    // loading side bar slot
    imgGate3.setImage(logicGates.get(2).getImage());
    imgTable3.setImage(logicGates.get(2).getTable());

    // loading side bar slot
    imgGate4.setImage(logicGates.get(3).getImage());
    imgTable4.setImage(logicGates.get(3).getTable());

    // loading side bar slot
    imgGate5.setImage(logicGates.get(4).getImage());
    imgTable5.setImage(logicGates.get(4).getTable());

    // loading side bar slot
    imgGate6.setImage(logicGates.get(5).getImage());
    imgTable6.setImage(logicGates.get(5).getTable());
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
