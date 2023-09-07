package nz.ac.auckland.se206.controllers.puzzles;

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

  // Grid of answer logic gates [ROW:COLUMN]
  //  00 01
  //  10 11 END
  //  20 21

  // First Row
  @FXML private ImageView imgAnwerGate00;
  @FXML private ImageView imgAnwerGate01;

  // Second Row
  @FXML private ImageView imgAnwerGate10;
  @FXML private ImageView imgAnwerGate11;

  // Third Row
  @FXML private ImageView imgAnwerGate20;
  @FXML private ImageView imgAnwerGate21;

  // END gate
  @FXML private ImageView imgAnwerGateEND;

  // current logic gates in submission grid list
  private List<LogicGate> currentAssembly;

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
    setHelperGateImgs();

    setSubmissionGates();
  }

  /** This method sets up the right side bar logic gate guide */
  private void setHelperGateImgs() {
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

  /**
   * This method is probably just a debug method to quickly add gates to the middle grid for testing
   * purposes
   */
  private void setSubmissionGates() {

    // set up for debug testing purposes
    currentAssembly.add(new LogicGate(LogicGate.Logic.AND));
    currentAssembly.add(new LogicGate(LogicGate.Logic.AND));
    currentAssembly.add(new LogicGate(LogicGate.Logic.OR));
    currentAssembly.add(new LogicGate(LogicGate.Logic.AND));
    currentAssembly.add(new LogicGate(LogicGate.Logic.XNOR));
    currentAssembly.add(new LogicGate(LogicGate.Logic.OR));
    currentAssembly.add(new LogicGate(LogicGate.Logic.OR));

    int i = 0;
    imgAnwerGate00.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGate01.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGate10.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGate11.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGate20.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGate21.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGateEND.setImage(currentAssembly.get(i).getImage());
  }

  @FXML
  private void initialize() {
    // saves current logic gate positions in grid
    currentAssembly = new ArrayList<>(); // reserve 6 spaces

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
