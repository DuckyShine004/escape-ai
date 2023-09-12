package nz.ac.auckland.se206.controllers.puzzles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

  // list of panes to change colour based on logic in the current wire
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

  // stores the image views of the current assembly
  private List<ImageView> currentAssemblyImages;

  // is currently active looking to swap
  private int swapping;

  // highlight colour for hover gate
  private String activeHighlight = "1111"; // light grey

  // highlight colour for about to swap gate
  private String swappingHighlight = "3333"; // darker grey

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

  private String onLogicColour = "00ff00"; // green
  private String offLogicColour = "ff0000"; // red

  // This is the number of first column gates
  // x
  // x
  // x
  // x
  // => 4
  private int layoutSize = 4;

  @FXML
  private void initialize() {
    // saves current logic gate positions in grid
    currentAssembly = new ArrayList<>(); // reserve 6 spaces

    // saves the list of image views in grid
    currentAssemblyImages = new ArrayList<>();

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

  /**
   * This Method sets up the logicGate array list
   *
   * <p>Logic Gate list slots :: 0-AND :: 1-NAND :: 2-OR :: 3-NOR
   */
  private void setUpLogicGates() {

    // new arraylist
    logicGates = new ArrayList<>();

    // add gates
    logicGates.add(new LogicGate(LogicGate.Logic.AND)); // AND
    logicGates.add(new LogicGate(LogicGate.Logic.OR)); // OR
    logicGates.add(new LogicGate(LogicGate.Logic.NOR)); // NOR

    currentAssemblyImages.add(imgAnswerGate0);
    currentAssemblyImages.add(imgAnswerGate1);
    currentAssemblyImages.add(imgAnswerGate2);
    currentAssemblyImages.add(imgAnswerGate3);
    currentAssemblyImages.add(imgAnswerGate4);
    currentAssemblyImages.add(imgAnswerGate5);
    currentAssemblyImages.add(imgAnswerGate6);

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
    currentAssembly.add(new LogicGate(LogicGate.Logic.NOR));
    currentAssembly.add(new LogicGate(LogicGate.Logic.NOR));
    currentAssembly.add(new LogicGate(LogicGate.Logic.OR));

    Collections.shuffle(currentAssembly); // randomly shuffles the current Assembly

    // locked end gate
    currentAssembly.add(
        new LogicGate(LogicGate.Logic.OR)); // start as OR, but change to NOR if already solved

    // lays out current assembly
    updateGateLayout();

    // set current logic trail
    updateLogicTrail(true);
  }

  /** This method will layout the current assembly of logic gates */
  private void updateGateLayout() {

    for (int j = 0; j < currentAssemblyImages.size(); j++) {
      // set temp image from the current assembly layout
      Image currentImage = currentAssembly.get(j).getImage();
      // set the imgView to this image in layout
      currentAssemblyImages.get(j).setImage(currentImage);
    }
  }

  /**
   * This method will calculate the logical pathway for the current assembly This is calculations
   * based on a grid of logic gates See Rules/Guidance Doccument to understand layout calculations:
   */
  private void updateLogicTrail(boolean firstTime) {
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

    // if this is the first time after set up, and the puzzle is already solved
    if (firstTime && logicTrail.get(logicTrail.size() - 1) == true) {

      // change last gate to XNOR
      currentAssembly.set(currentAssembly.size() - 1, new LogicGate(LogicGate.Logic.NOR));

      // update visuals of gate layout
      updateGateLayout();

      // call to update once fixed
      updateLogicTrail(false);
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
        output = a && b; // bit wise and
        break;
      case OR:
        output = a || b; // bit wise or
        break;
        // case XOR:
        //   output = ((a || b) && !(a && b)); // (a+b)!(ab)
        //   break;
        // case XNOR:
        //   output = a == b; // equality gate
        //   break;
      case NOR:
        output = !(a || b); // not or gate
        break;
    }

    return output;
  }

  /** This method sets a random number */
  private void setRandomInput() {

    List<Boolean> tempLogic = new ArrayList<>();

    // layout size*2 is number of gates inputs, so 8 inputs required
    for (int i = 0; i < layoutSize; i++) {
      tempLogic.add(true);
      tempLogic.add(false);
    }

    for (int j = 0; j < layoutSize * 2; j++) {
      // add to actual inputs

      // get random value
      Random rand = new Random();
      int randomValue = rand.nextInt(tempLogic.size());

      // add random into actual inputs
      logicTrail.set(j, tempLogic.get(randomValue));

      // remove that value from the auxilliary array
      tempLogic.remove(randomValue);
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
    pAnswerGate0.setStyle("-fx-background-color: #FFFF"); // white collour
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
    updateLogicTrail(false);
  }

  /*
   * This method changes the scene back to the Breaker Room
   */
  @FXML
  private void onBackToBreaker() {
    App.setUi(AppUi.BREAKER);
  }

  /**
   * This method takes in the currently clicked gate, and determins if it should be highlighed or
   * swapped
   *
   * @param current
   */
  private void onClickedGate(int current) {
    //
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
  private void onGate0Clicked(MouseEvent event) {
    // on hover enter
    onClickedGate(0);
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
    onClickedGate(1);
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
    onClickedGate(2);
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
    onClickedGate(3);
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
    onClickedGate(4);
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
    onClickedGate(5);
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
}
