package nz.ac.auckland.se206.controllers.puzzles;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.utilities.*;

public class LogicGatePuzzleController {
  @FXML private Label lblTimer;

  // Panes that sit under Answer Gates
  @FXML private Pane pAnswerGate1;

  // begin side bar helper Gate : Table ImageViews
  @FXML private ImageView imgGate1;
  @FXML private ImageView imgGate2;
  @FXML private ImageView imgGate3;
  @FXML private ImageView imgGate4;
  @FXML private ImageView imgGate5;
  @FXML private ImageView imgGate6;

  // Grid of answer logic gates [ROW:COLUMN]
  //  00 01
  //  10 11 END
  //  20 21

  // First Row
  @FXML private ImageView imgAnwerGate0;
  @FXML private ImageView imgAnwerGate1;
  @FXML private ImageView imgAnwerGate2;
  @FXML private ImageView imgAnwerGate3;

  // Second Row
  @FXML private ImageView imgAnwerGate4;
  @FXML private ImageView imgAnwerGate5;

  // END gate
  @FXML private ImageView imgAnwerGate6;

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

    // loading side bar slot
    imgGate2.setImage(logicGates.get(1).getImage());

    // loading side bar slot
    imgGate3.setImage(logicGates.get(2).getImage());

    // loading side bar slot
    imgGate4.setImage(logicGates.get(3).getImage());

    // loading side bar slot
    imgGate5.setImage(logicGates.get(4).getImage());

    // loading side bar slot
    imgGate6.setImage(logicGates.get(5).getImage());
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
    imgAnwerGate0.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGate1.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGate2.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGate3.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGate4.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGate5.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnwerGate6.setImage(currentAssembly.get(i).getImage());
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

  @FXML
  private void onGate0Clicked(MouseEvent event) {
    //
    System.out.println("clicked: " + event.getSource().toString());
  }

  @FXML
  private void onGate0Enter(MouseEvent event) {
    //
    pAnswerGate1.setStyle("-fx-background-color: #1111");
  }

  @FXML
  private void onGate0Exit(MouseEvent event) {
    //
    pAnswerGate1.setStyle("-fx-background-color: #FFFF");
  }

  @FXML
  private void onGate1Clicked(MouseEvent event) {
    //
    System.out.println("clicked: " + event.getSource().toString());
  }

  @FXML
  private void onGate1Enter(MouseEvent event) {
    //
    System.out.println("Enter: " + event.getSource().toString());
  }

  @FXML
  private void onGate1Exit(MouseEvent event) {
    //
    System.out.println("Exit: " + event.getSource().toString());
  }

  @FXML
  private void onGate2Clicked(MouseEvent event) {
    //
    System.out.println("clicked: " + event.getSource().toString());
  }

  @FXML
  private void onGate2Enter(MouseEvent event) {
    //
    System.out.println("Enter: " + event.getSource().toString());
  }

  @FXML
  private void onGate2Exit(MouseEvent event) {
    //
    System.out.println("Exit: " + event.getSource().toString());
  }

  @FXML
  private void onGate3Clicked(MouseEvent event) {
    //
    System.out.println("clicked: " + event.getSource().toString());
  }

  @FXML
  private void onGate3Enter(MouseEvent event) {
    //
    System.out.println("Enter: " + event.getSource().toString());
  }

  @FXML
  private void onGate3Exit(MouseEvent event) {
    //
    System.out.println("Exit: " + event.getSource().toString());
  }

  @FXML
  private void onGate4Clicked(MouseEvent event) {
    //
    System.out.println("clicked: " + event.getSource().toString());
  }

  @FXML
  private void onGate4Enter(MouseEvent event) {
    //
    System.out.println("Enter: " + event.getSource().toString());
  }

  @FXML
  private void onGate4Exit(MouseEvent event) {
    //
    System.out.println("Exit: " + event.getSource().toString());
  }

  @FXML
  private void onGate5Clicked(MouseEvent event) {
    //
    System.out.println("clicked: " + event.getSource().toString());
  }

  @FXML
  private void onGate5Enter(MouseEvent event) {
    //
    System.out.println("Enter: " + event.getSource().toString());
  }

  @FXML
  private void onGate5Exit(MouseEvent event) {
    //
    System.out.println("Exit: " + event.getSource().toString());
  }

  @FXML
  private void onGate6Clicked(MouseEvent event) {
    //
    System.out.println("clicked: " + event.getSource().toString());
  }

  @FXML
  private void onGate6Enter(MouseEvent event) {
    //
    System.out.println("Enter: " + event.getSource().toString());
  }

  @FXML
  private void onGate6Exit(MouseEvent event) {
    //
    System.out.println("Exit: " + event.getSource().toString());
  }
}
