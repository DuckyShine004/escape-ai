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
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.utilities.*;

public class LogicGatePuzzleController {
  @FXML private Label lblTimer;

  // Panes that sit under Answer Gates
  @FXML private Pane pAnswerGate0;
  @FXML private Pane pAnswerGate1;
  @FXML private Pane pAnswerGate2;
  @FXML private Pane pAnswerGate3;
  @FXML private Pane pAnswerGate4;
  @FXML private Pane pAnswerGate5;
  @FXML private Pane pAnswerGate6;

  // begin side bar helper Gate : Table ImageViews
  @FXML private ImageView imgGate1;
  @FXML private ImageView imgGate2;
  @FXML private ImageView imgGate3;
  @FXML private ImageView imgGate4;
  @FXML private ImageView imgGate5;
  @FXML private ImageView imgGate6;

  // input logic img views
  @FXML private Pane pInput0;
  @FXML private Pane pInput1;
  @FXML private Pane pInput2;
  @FXML private Pane pInput3;
  @FXML private Pane pInput4;
  @FXML private Pane pInput5;
  @FXML private Pane pInput6;
  @FXML private Pane pInput7;

  @FXML private Pane pInput8;
  @FXML private Pane pInput9;
  @FXML private Pane pInput10;
  @FXML private Pane pInput11;

  @FXML private Pane pInput12;
  @FXML private Pane pInput13;

  @FXML private Pane pInput14; // end gate

  List<Pane> logicInSection;

  // Grid of answer logic gates [ROW:COLUMN]
  //  00 01
  //  10 11 END
  //  20 21

  // First Row
  @FXML private ImageView imgAnswerGate0;
  @FXML private ImageView imgAnswerGate1;
  @FXML private ImageView imgAnswerGate2;
  @FXML private ImageView imgAnswerGate3;

  // Second Row
  @FXML private ImageView imgAnswerGate4;
  @FXML private ImageView imgAnswerGate5;

  // END gate
  @FXML private ImageView imgAnswerGate6;

  // current logic gates in submission grid list
  private List<LogicGate> currentAssembly;

  // is currently active looking to swap
  private int swapping;

  // highlight colour for hover gate
  private String activeHighlight = "1111"; // light grey

  // highlight colour for about to swap gate
  private String swappingHighlight = "3333";

  // Logic Gate list
  // 0 - AND
  // 1 - NAND
  // 2 - OR
  // 3 - NOR
  // 4 - XOR
  // 5 - XNOR
  private List<LogicGate> logicGates;

  // logic pathway list that stores the current boolean logic in each node entering and exiting
  // store as list
  // first 8 positions are the inital given logic
  // next 4 positions are resulting from first layer
  // next 2 positions are resulting from second layer
  // final value is calulated, and if true, puzzle is solved
  private List<Boolean> logicTrail;

  private String onLogicColour = "00ff00";
  private String offLogicColour = "ff0000";

  // layout size is the number of original gates, should only be 2 or 4 depending on diffucity /
  // time ?
  private int layoutSize = 4; // TODO: have this change based on difficulty or time (2 or 4)

  @FXML
  private void initialize() {
    // saves current logic gate positions in grid
    currentAssembly = new ArrayList<>(); // reserve 6 spaces

    // stores the imgViews for each section of logic between gates
    logicInSection = new ArrayList<>();

    // at initalize, nothing is active looking to swap
    swapping = -1;

    // new logic trail
    // reserve layout * 2 -1 spaces
    // for layout = 4 --> 14
    // for layout = 2 --> 7
    // layoutSize*4-0.5*layoutSize
    // layoutSize(3.5)
    // logicTrail = new ArrayList<>((int) (layoutSize * 3.5)); // just has to work for 2 and 4
    logicTrail = new ArrayList<>();
    for (int i = 0; i < (int) (layoutSize * 3.5 + 1); i++) { // added +1 as debug, proabbly correct
      logicTrail.add(false); // DEBBUG LOOP
    }

    setRandomInput();
    setUpLogicGates();
    setUpLogicTrail();
  }

  public static void logicGateRestart() {
    // TODO: somehow have the level reset
    System.out.println("RESTARTING");
  }

  /** TODO: change name, currently this is dead code */
  public void NONSTATIClogicGateRestart() {
    currentAssembly.clear();
    logicInSection.clear();

    swapping = -1;

    logicTrail.clear();
    for (int i = 0; i < (int) (layoutSize * 3.5 + 1); i++) { // added +1 as debug, proabbly correct
      logicTrail.add(false); // DEBBUG LOOP
    }

    setRandomInput();
    setUpLogicGates();
    setUpLogicTrail();
  }

  /**
   * This Method sets up the logicGate array list
   *
   * <p>Logic Gate list slots :: 0-AND :: 1-NAND :: 2-OR :: 3-NOR :: 4-XOR :: 5-XNOR
   */
  private void setUpLogicGates() {

    // new arraylist
    logicGates = new ArrayList<>();

    // add gates
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

    // lays out current assembly
    updateGateLayout();

    // set current logic trail
    updateLogicTrail();
  }

  /** This method will layout the current assembly of logic gates */
  private void updateGateLayout() {
    // can change this to a list containing imgViews for each gate in later refactor
    int i = 0;
    imgAnswerGate0.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnswerGate1.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnswerGate2.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnswerGate3.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnswerGate4.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnswerGate5.setImage(currentAssembly.get(i).getImage());
    i++;
    imgAnswerGate6.setImage(currentAssembly.get(i).getImage());
  }

  /** This method will calculate the logical pathway for the current assembly */
  private void updateLogicTrail() {
    // for now this is of unvarying size

    // for each circuit in assebmly
    for (int i = 0; i < currentAssembly.size(); i++) {
      // 0 --> 6
      //
      // 0:0  2:1  4:2  6:3  8:4 10:5 12:6
      boolean a = logicTrail.get(i * 2);
      boolean b = logicTrail.get(i * 2 + 1);

      // get result
      boolean result = compare(a, b, currentAssembly.get(i).getType());

      // 0 --> 8
      // 1 --> 9
      // 2 --> 10
      logicTrail.set((i) * 2 + (8 - i), result);
    }

    updateDisplayLogicTrail();
  }

  /**
   * This method will take in current gate's inputs, as well as its type, will return the result of
   * this boolean oporation
   *
   * @param a
   * @param b
   * @param logic
   */
  private boolean compare(boolean a, boolean b, LogicGate.Logic logic) {

    boolean output = false;

    switch (logic) {
      case AND:
        output = a && b;
        break;
      case NAND:
        output = !(a && b);
        break;
      case OR:
        output = a || b;
        break;
      case NOR:
        output = !(a || b);
        break;
      case XOR:
        output = ((a || b) && !(a && b));
        break;
      case XNOR:
        output = a == b;
        break;
    }

    return output;
  }

  /** This method sets a random number */
  private void setRandomInput() {

    // layout size*2 is number of gates inputs, so 8 inputs required
    for (int i = 0; i < layoutSize * 2; i++) {

      // Random Input
      if (Math.random() < 0.5) {
        logicTrail.set(i, true);
      } else {
        logicTrail.set(i, false);
      }
    }
  }

  /**
   * This method will set up each imgView showing the player what the current logic is in each wire
   */
  private void setUpLogicTrail() {
    // adding panes to array of panes

    // first column
    logicInSection.add(this.pInput0);
    logicInSection.add(this.pInput1);
    logicInSection.add(this.pInput2);
    logicInSection.add(this.pInput3);
    logicInSection.add(this.pInput4);
    logicInSection.add(this.pInput5);
    logicInSection.add(this.pInput6);
    logicInSection.add(this.pInput7);

    // second column
    logicInSection.add(this.pInput8);
    logicInSection.add(this.pInput9);
    logicInSection.add(this.pInput10);
    logicInSection.add(this.pInput11);

    // third column
    logicInSection.add(this.pInput12);
    logicInSection.add(this.pInput13);

    // fourth column
    logicInSection.add(this.pInput14);

    updateDisplayLogicTrail();
  }

  /** This Method decides what colour trail each logic input should be */
  private void updateDisplayLogicTrail() {

    for (int i = 0; i < logicInSection.size(); i++) {

      String colour = "";

      if (logicTrail.get(i) == true) {

        // set colour to Green
        colour = onLogicColour;
      } else {

        // set colour to Red
        colour = offLogicColour;
      }

      // set background of pane to colour
      logicInSection.get(i).setStyle("-fx-background-color: #" + colour);
    }

    if (logicTrail.get(logicTrail.size() - 1) == true) {
      // the puzzle has been solved
      System.out.println("Logic Gate Puzzle Solved");
      GameState.isLogicGateSolved = true;
    }
  }

  /**
   * This method will set currently clicked background to active colour, and will reset non active
   * gates to clear colour
   */
  private void updateActiveBackgrounds(int active) {
    // switch statement to change all gates based on int active

    // clears all
    pAnswerGate0.setStyle("-fx-background-color: #FFFF");
    pAnswerGate1.setStyle("-fx-background-color: #FFFF");
    pAnswerGate2.setStyle("-fx-background-color: #FFFF");
    pAnswerGate3.setStyle("-fx-background-color: #FFFF");
    pAnswerGate4.setStyle("-fx-background-color: #FFFF");
    pAnswerGate5.setStyle("-fx-background-color: #FFFF");
    pAnswerGate6.setStyle("-fx-background-color: #FFFF");

    // sets active gate to highlight
    switch (active) {
      case 0:
        pAnswerGate0.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 1:
        pAnswerGate1.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 2:
        pAnswerGate2.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 3:
        pAnswerGate3.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 4:
        pAnswerGate4.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 5:
        pAnswerGate5.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 6:
        pAnswerGate6.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case -1:
        break;
    }
  }

  /** This method will swap the gates a and b */
  private void swapGates(int a, int b) {
    // swap gates
    LogicGate temp = currentAssembly.get(a);
    currentAssembly.set(a, currentAssembly.get(b));
    currentAssembly.set(b, temp);

    // no current swapping gates
    this.swapping = -1;

    // refresh layout of gates
    updateGateLayout();

    // clear backgrounds
    updateActiveBackgrounds(this.swapping);

    // update logic trail
    updateLogicTrail();
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
    int current = 0;
    if (this.swapping == -1 | this.swapping == current) {

      // set active swaping gate
      this.swapping = current;

      // sets active backgrounds, i.e. clicked gates, and clears non active
      updateActiveBackgrounds(current);
    } else {

      // if there is an active gate that isn't itself, swap
      swapGates(this.swapping, 0);
    }
  }

  @FXML
  private void onGate0Enter(MouseEvent event) {
    //
    if (this.swapping != 0) {
      pAnswerGate0.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  @FXML
  private void onGate0Exit(MouseEvent event) {
    //
    if (this.swapping != 0) {
      pAnswerGate0.setStyle("-fx-background-color: #FFFF");
    }
  }

  @FXML
  private void onGate1Clicked(MouseEvent event) {
    //
    int current = 1;
    if (this.swapping == -1 | this.swapping == current) {

      // set active swaping gate
      this.swapping = current;

      // sets active backgrounds, i.e. clicked gates, and clears non active
      updateActiveBackgrounds(current);
    } else {

      // if there is an active gate that isn't itself, swap
      swapGates(this.swapping, current);
    }
  }

  @FXML
  private void onGate1Enter(MouseEvent event) {
    //
    if (this.swapping != 1) {
      pAnswerGate1.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  @FXML
  private void onGate1Exit(MouseEvent event) {
    //
    if (this.swapping != 1) {
      pAnswerGate1.setStyle("-fx-background-color: #FFFF");
    }
  }

  @FXML
  private void onGate2Clicked(MouseEvent event) {
    //
    int current = 2;
    if (this.swapping == -1 | this.swapping == current) {

      // set active swaping gate
      this.swapping = current;

      // sets active backgrounds, i.e. clicked gates, and clears non active
      updateActiveBackgrounds(current);
    } else {

      // if there is an active gate that isn't itself, swap
      swapGates(this.swapping, current);
    }
  }

  @FXML
  private void onGate2Enter(MouseEvent event) {
    //
    if (this.swapping != 2) {
      pAnswerGate2.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  @FXML
  private void onGate2Exit(MouseEvent event) {
    //
    if (this.swapping != 2) {
      pAnswerGate2.setStyle("-fx-background-color: #FFFF");
    }
  }

  @FXML
  private void onGate3Clicked(MouseEvent event) {
    //
    int current = 3;
    if (this.swapping == -1 | this.swapping == current) {

      // set active swaping gate
      this.swapping = current;

      // sets active backgrounds, i.e. clicked gates, and clears non active
      updateActiveBackgrounds(current);
    } else {

      // if there is an active gate that isn't itself, swap
      swapGates(this.swapping, current);
    }
  }

  @FXML
  private void onGate3Enter(MouseEvent event) {
    //
    if (this.swapping != 3) {
      pAnswerGate3.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  @FXML
  private void onGate3Exit(MouseEvent event) {
    //
    if (this.swapping != 3) {
      pAnswerGate3.setStyle("-fx-background-color: #FFFF");
    }
  }

  @FXML
  private void onGate4Clicked(MouseEvent event) {
    //
    int current = 4;
    if (this.swapping == -1 | this.swapping == current) {

      // set active swaping gate
      this.swapping = current;

      // sets active backgrounds, i.e. clicked gates, and clears non active
      updateActiveBackgrounds(current);
    } else {

      // if there is an active gate that isn't itself, swap
      swapGates(this.swapping, current);
    }
  }

  @FXML
  private void onGate4Enter(MouseEvent event) {
    //
    if (this.swapping != 4) {
      pAnswerGate4.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  @FXML
  private void onGate4Exit(MouseEvent event) {
    //
    if (this.swapping != 4) {
      pAnswerGate4.setStyle("-fx-background-color: #FFFF");
    }
  }

  @FXML
  private void onGate5Clicked(MouseEvent event) {
    //
    int current = 5;
    if (this.swapping == -1 | this.swapping == current) {

      // set active swaping gate
      this.swapping = current;

      // sets active backgrounds, i.e. clicked gates, and clears non active
      updateActiveBackgrounds(current);
    } else {

      // if there is an active gate that isn't itself, swap
      swapGates(this.swapping, current);
    }
  }

  @FXML
  private void onGate5Enter(MouseEvent event) {
    //
    if (this.swapping != 5) {
      pAnswerGate5.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  @FXML
  private void onGate5Exit(MouseEvent event) {
    //
    if (this.swapping != 5) {
      pAnswerGate5.setStyle("-fx-background-color: #FFFF");
    }
  }

  @FXML
  private void onGate6Clicked(MouseEvent event) {
    //
    int current = 6;
    if (this.swapping == -1 | this.swapping == current) {

      // set active swaping gate
      this.swapping = current;

      // sets active backgrounds, i.e. clicked gates, and clears non active
      updateActiveBackgrounds(current);
    } else {

      // if there is an active gate that isn't itself, swap
      swapGates(this.swapping, current);
    }
  }

  @FXML
  private void onGate6Enter(MouseEvent event) {
    //
    if (this.swapping != 6) {
      pAnswerGate6.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  @FXML
  private void onGate6Exit(MouseEvent event) {
    //
    if (this.swapping != 6) {
      pAnswerGate6.setStyle("-fx-background-color: #FFFF");
    }
  }
}
