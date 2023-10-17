package nz.ac.auckland.se206.controllers.puzzles;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.AudioManager;
import nz.ac.auckland.se206.AudioManager.Clip;
import nz.ac.auckland.se206.HintManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.GameState.Difficulty;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.utilities.LogicGate;
import nz.ac.auckland.se206.utilities.Timer;
import nz.ac.auckland.se206.utilities.Wire;

/**
 * The controller for the logic gate puzzle, which gives all the functionality for the logic gate
 * puzzle.
 */
public class LogicGatePuzzleController {
  @FXML private Label lblTimer;
  @FXML private Label lblHintCounter;

  // Panes that sit under Answer Gates
  @FXML private Pane pneAnswerGate0;
  @FXML private Pane pneAnswerGate1;
  @FXML private Pane pneAnswerGate2;
  @FXML private Pane pneAnswerGate3;
  @FXML private Pane pneAnswerGate4;
  @FXML private Pane pneAnswerGate5;
  @FXML private Pane pneAnswerGate6;

  // begin side bar helper Gate : Table ImageViews
  @FXML private ImageView imgGate1;
  @FXML private ImageView imgGate2;
  @FXML private ImageView imgGate3;

  // information bubbles to show clickability
  @FXML private ImageView imgHelperBubble1;
  @FXML private ImageView imgHelperBubble2;
  @FXML private ImageView imgHelperBubble3;
  @FXML private ImageView imgHelpToDo;

  // labels for helper gates
  @FXML private Label lblHelperGate1;
  @FXML private Label lblHelperGate2;
  @FXML private Label lblHelperGate3;

  // input logic img views
  @FXML private ImageView imgInput0;
  @FXML private ImageView imgInput1;
  @FXML private ImageView imgInput2;
  @FXML private ImageView imgInput3;
  @FXML private ImageView imgInput4;
  @FXML private ImageView imgInput5;
  @FXML private ImageView imgInput6;
  @FXML private ImageView imgInput7;

  // remaining wires with constituent pairs for wire bending
  @FXML private ImageView imgWire1;
  @FXML private ImageView imgWire2;
  @FXML private ImageView imgWire3;
  @FXML private ImageView imgWire4;
  @FXML private ImageView imgWire5;
  @FXML private ImageView imgWire6;
  @FXML private ImageView imgWire7;

  @FXML private ImageView imgSolvedLight;

  // text area and field for gpt interaction
  @FXML private TextArea taGptText;
  @FXML private TextField tfTextInput;

  // glass screen covereing end gate
  @FXML private ImageView imgGlassScreen;

  @FXML private ProgressBar pgbGptThinking;

  // label for end logic gate so player can know what it is
  @FXML private Label lblFinalGate;

  // Grid of answer logic gates [ROW:COLUMN]
  //  00 01
  //  10 11 END
  //  20 21

  // Hint button
  @FXML private Polygon pgHint;

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

  // the label with the time
  @FXML private Label lblTime;

  // background blue circuit image
  @FXML private ImageView imgBlueCircuits;

  // input logic lights
  private Image redLight;
  private Image greenLight;

  // list of panes to change colour based on logic in the current wire
  private List<Wire> logicInSection;

  // list to hold helper gates
  private List<Label> lblHelperGates;

  // list of the Image Views inputing true or false.  Constant values
  private List<ImageView> logicInputs;

  // current logic gates in submission grid list
  private List<LogicGate> currentAssembly;

  // stores the image views of the current assembly
  private List<ImageView> currentAssemblyImages;

  // is currently active looking to swap
  private int swapping;

  // highlight colour for hover gate
  private String activeHighlight = "004078"; // darker blue
  // 16b2c7 too similar
  // 004078

  // highlight colour for about to swap gate
  private String swappingHighlight = "00325e"; // darker blue

  // Logic Gate list
  // 0 - AND
  // 1 - NAND
  // 2 - OR
  // 3 - NOR
  // 4 - XOR
  // 5 - XNOR
  private List<LogicGate> logicGates;

  private List<ImageView> helperGates;

  // logic pathway list that stores the current boolean logic in each node entering and exiting
  // store as list
  // first 8 positions are the inital given logic
  // next 4 positions are resulting from first layer
  // next 2 positions are resulting from second layer
  // final value is calulated, and if true, puzzle is solved
  private List<Boolean> logicTrail;

  // This is the number of first column gates
  // x
  // x
  // x
  // x
  // => 4
  private int layoutSize = 4;

  // boolean state for if the hint has been given for the last gate
  private boolean hasHintedAtEndGate = false;

  // integer value for the last gate that was swapped which the hint will generate for
  private int firstGatesHinted = 0; // 0, 1, 2, 3

  // this is the single gpt context messages
  private ChatCompletionRequest gptRequest;

  private Image clickableImage;
  private Image getInformationImage;

  /** This method initailizes all the fields and lists. Additionally, it sets up the logic gate. */
  @FXML
  private void initialize() {
    // start timer
    Timer.addLabel(lblTime);

    // Add the hint counter components
    HintManager.addHintComponents(lblHintCounter, pgHint);

    // saves current logic gate positions in grid
    currentAssembly = new ArrayList<>(); // reserve 6 spaces

    // saves the list of image views in grid
    currentAssemblyImages = new ArrayList<>();

    // stores the imgViews for each section of logic between gates
    logicInSection = new ArrayList<>();

    // stores the imgViews for the input logic condition
    logicInputs = new ArrayList<>();

    // stores the helper gate labels displaying 'AND' etc
    lblHelperGates = new ArrayList<>();

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

    // call set up functions
    setRandomInput();
    setUpLogicGates();
    setUpLogicTrail();

    // sets the styles of the scene
    setStyles();

    // initalize the chat with a greeting message
    initiailizeChat();
  }

  /** This method initaiailizes the chat area with a greeting message. */
  private void initiailizeChat() {

    taGptText.appendText("\n\n");

    // initialize the chat message field
    ChatMessage gptMessage;

    // initialize GPT chat message object
    gptMessage = new ChatMessage("assistant", GptPromptEngineering.initializeLogicGateResponse());

    // initialize an instance of GPT request
    gptRequest = new ChatCompletionRequest();

    // set the 'n' parameter for the request -> has to be '1'
    gptRequest.setN(1);

    // set the temperature for the request -> [0,2]
    gptRequest.setTemperature(1.4);

    // set the 'top p' value for the request -> [0,1]
    gptRequest.setTopP(0.5);

    // set the max tokens -> has to be at least '1'
    gptRequest.setMaxTokens(100);

    // get a response from GPT to setup the chat
    getChatResponse(gptMessage, tfTextInput);
  }

  /**
   * This method toggles the disabled state of a given input button. The button is also a of type
   * polygon.
   *
   * @param btn the button to be toggled.
   */
  private void toggleButton(Polygon btn) {
    // toogle disabled
    btn.setDisable(btn.isDisable() == false);
  }

  /**
   * This method clears the text from a given text field. It also toggles the disabled state of the
   * text field.
   *
   * @param tf the text field to be toggled.
   */
  private void toggleTextField(TextField tf) {
    // clear input
    tf.setText("");

    // toggle disabled
    tf.setDisable(tf.isDisable() == false);
  }

  /**
   * Retrieve a response from GPT, given that there is some sort of prompt imput. Additionally, this
   * response must be displayed to the user.
   *
   * @param gptMessage the GPT input prompt.
   * @param item the input object.
   */
  private void getChatResponse(ChatMessage gptMessage, Object item) {
    // add user input to GPT's user input history
    gptRequest.addMessage(gptMessage);

    // disable input
    if (item != null) {

      if (item instanceof TextField) {
        TextField tf = (TextField) item;
        toggleTextField(tf);
      } else {
        Polygon btn = (Polygon) item;
        toggleButton(btn);
      }
    }

    // create a concurrent task for handling GPT response
    Task<Void> gptTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {

            // set GPT's response
            System.out.println(this.toString());
            setChatResponse();
            return null;
          }
        };

    // create a thread to handle GPT concurrency
    Thread gptThread = new Thread(gptTask);

    gptTask.setOnRunning(
        event -> {
          startLoadingBar();
        });

    // set text field to enabled on failed
    gptTask.setOnFailed(
        event -> {
          pgbGptThinking.setVisible(false);
          if (item != null) {

            if (item instanceof TextField) {
              toggleTextField((TextField) item);
            } else {
              toggleButton((Polygon) item);
            }
          }
        });

    // set text field to enabled on succeeded
    gptTask.setOnSucceeded(
        event -> {
          pgbGptThinking.setVisible(false);
          if (item != null) {

            if (item instanceof TextField) {
              toggleTextField((TextField) item);
            } else {
              toggleButton((Polygon) item);
            }
          }
        });

    // start the thread
    gptThread.start();
  }

  /**
   * This method will start the loading animation within the progress bar. This will take 2 seconds.
   */
  private void startLoadingBar() {
    // Set the initial visibility
    pgbGptThinking.setVisible(true);
    pgbGptThinking.setProgress(0);

    Timeline task =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(pgbGptThinking.progressProperty(), 0)),
            new KeyFrame(Duration.seconds(2), new KeyValue(pgbGptThinking.progressProperty(), 1)));

    task.play();
  }

  /**
   * Set the chat response from GPT. This includes printing the response to the text area.
   *
   * @throws Exception thrown when we fail to retrieve a response from GPT.
   */
  private void setChatResponse() throws Exception {
    // get GPT's response
    ChatCompletionResult gptResult = gptRequest.execute();

    // get GPT's choice
    Choice gptChoice = gptResult.getChoices().iterator().next();

    // get GPT's chat message
    ChatMessage gptMessage = gptChoice.getChatMessage();

    // get the content of gpt's message in the form of a string
    String gptOutput = gptMessage.getContent();

    // add GPT's response to its history
    gptRequest.addMessage(gptMessage);

    // append the result to the text area
    taGptText.appendText(gptOutput + "\n\n");
  }

  /**
   * This method will update trigger when the user submits their prompt into the text field.
   *
   * @param event when the user presses enter.
   */
  @FXML
  private void onEnterInput(KeyEvent event) {
    // get input from text field
    String input = tfTextInput.getText();

    // was enter pressed, and is input valid
    if (event.getCode() == KeyCode.ENTER && input.length() > 0 && input.length() < 200) {
      if (input.trim().isEmpty()) {
        return;
      }

      // initialize input chat message object
      ChatMessage inputMessage;

      // create a new instance of input chat message object
      inputMessage = new ChatMessage("user", input);

      // appent input to text area
      taGptText.appendText(input + "\n\n");

      // get the gpt response
      getChatResponse(inputMessage, tfTextInput);
    }
  }

  /**
   * This method sets the styles for this scene. This will load all images that contribute to the
   * overall aesthetics of the room.
   */
  private void setStyles() {
    try {
      // load glass image
      Image glassScreen =
          new Image(
              new FileInputStream(
                  "src/main/resources/images/BreakerRoom/LogicGatePuzzle/" + "glassScreen2.png"));
      imgGlassScreen.setImage(glassScreen);

    } catch (Exception e) {
      e.printStackTrace();
    }

    try {

      Image circuits =
          new Image(
              new FileInputStream(
                  "src/main/resources/images/BreakerRoom/LogicGatePuzzle/" + "circuits" + ".jpg"));

      imgBlueCircuits.setImage(circuits);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This Method sets up the logicGate array list. The logic gates available are the AND, NAND, OR,
   * and NOR gates. The indexing for each gate is shown below.
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

  /**
   * This method sets up the right side bar logic gate guide. Additionally, it initializes the array
   * of logic gates to be used later on, and any logic gate labels.
   */
  private void setHelperGateImgs() {

    helperGates = new ArrayList<>();

    // add helper gates to array
    helperGates.add(imgGate1);
    helperGates.add(imgGate2);
    helperGates.add(imgGate3);

    // sets the logic gate images
    for (int i = 0; i < helperGates.size(); i++) {
      helperGates.get(i).setImage(logicGates.get(i).getImage());
    }

    // add labels to list
    lblHelperGates.add(lblHelperGate1);
    lblHelperGates.add(lblHelperGate2);
    lblHelperGates.add(lblHelperGate3);

    try {
      // try to load clickable image icon
      this.clickableImage =
          new Image(
              new FileInputStream(
                  "src/main/resources/images/BreakerRoom/LogicGatePuzzle/clickable.png"));

      this.getInformationImage =
          new Image(
              new FileInputStream(
                  "src/main/resources/images/BreakerRoom/LogicGatePuzzle/getInformation.png"));
    } catch (Exception e) {
      e.printStackTrace();
    }

    // set bubble icon
    imgHelperBubble1.setImage(this.clickableImage);
    imgHelperBubble2.setImage(this.clickableImage);
    imgHelperBubble3.setImage(this.clickableImage);
    imgHelpToDo.setImage(this.getInformationImage);

    // initlize all labels
    for (int i = 0; i < lblHelperGates.size(); i++) {
      lblHelperGates.get(i).setText(logicGates.get(i).getType().toString());
    }
  }

  /**
   * This method is probably just a debug method to quickly add gates to the middle grid for testing
   * purposes.
   */
  private void setSubmissionGates() {

    // set up
    currentAssembly.add(new LogicGate(LogicGate.Logic.AND));
    currentAssembly.add(new LogicGate(LogicGate.Logic.AND));
    currentAssembly.add(new LogicGate(LogicGate.Logic.OR));
    currentAssembly.add(new LogicGate(LogicGate.Logic.OR));
    currentAssembly.add(new LogicGate(LogicGate.Logic.NOR));
    currentAssembly.add(new LogicGate(LogicGate.Logic.NOR));

    Collections.shuffle(currentAssembly); // randomly shuffles the current Assembly

    // locked end gate
    currentAssembly.add(
        new LogicGate(LogicGate.Logic.OR)); // start as OR, but change to NOR if already solved
    lblFinalGate.setText("OR");

    // lays out current assembly
    updateGateLayout();

    // set current logic trail
    updateLogicTrail(true);
  }

  /** This method will update the layout of the current assembly of logic gates. */
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
   * based on a grid of logic gates. See Rules/Guidance Doccument to understand layout calculations.
   *
   * @param firstTime check if it is the first time a logic gate has been switched.
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
      lblFinalGate.setText("NOR");

      // update visuals of gate layout
      updateGateLayout();

      // call to update once fixed
      updateLogicTrail(false);
    }
    updateDisplayLogicTrail();
  }

  /**
   * This method will take in current gate's inputs, as well as its type, and will perform bit-wise
   * operations to determine the logic gate outputs.
   *
   * @param a the first value, a.
   * @param b the second value, b.
   * @param logic enum that checks which gate is used.
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

  /**
   * This method will set up a random grid of logic gates. The layout as mentioned, is random,
   * however, the logic gates must also be arranged in a way it is possible for the player to solve.
   */
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
   * This method initializes and loads the input images. The input images consist of a red light and
   * a green light.
   *
   * @throws Exception when the image cannot be loaded.
   */
  private void loadInputImages() throws Exception {
    //
    this.redLight =
        new Image(
            new FileInputStream(
                "src/main/resources/images/BreakerRoom/LogicGatePuzzle/Wires/redlight.png"));
    this.greenLight =
        new Image(
            new FileInputStream(
                "src/main/resources/images/BreakerRoom/LogicGatePuzzle/Wires/greenlight.png"));
  }

  /**
   * This method displays the loaded images. Additionally, if the player solves the puzzle, the
   * green light will turn on, otherwise, maintain the red light status.
   */
  private void displayInputImages() {

    // loop through inputs
    for (int i = 0; i < logicInputs.size(); i++) {

      // get current image view
      ImageView currentImageView = logicInputs.get(i);

      // if it is on
      if (logicTrail.get(i) == true) {
        // set to green light
        currentImageView.setImage(greenLight);
      } else {
        // set to red light
        currentImageView.setImage(redLight);
      }
    }
  }

  /**
   * This method will set up each image view showing the player what the current logic is in each
   * wire.
   */
  private void setUpLogicTrail() {
    // adding panes to array of panes

    // first column are constant image views
    logicInputs.add(imgInput0);
    logicInputs.add(imgInput1);
    logicInputs.add(imgInput2);
    logicInputs.add(imgInput3);
    logicInputs.add(imgInput4);
    logicInputs.add(imgInput5);
    logicInputs.add(imgInput6);
    logicInputs.add(imgInput7);

    // try to load image, if can't load, throw error
    try {
      loadInputImages();
    } catch (Exception e) {
      // print error
      e.printStackTrace();
    }

    // display the input logic images
    displayInputImages();

    // second column
    logicInSection.add(new Wire(this.imgWire1));
    logicInSection.add(new Wire(this.imgWire2));
    logicInSection.add(new Wire(this.imgWire3));
    logicInSection.add(new Wire(this.imgWire4));

    // third column
    logicInSection.add(new Wire(this.imgWire5));
    logicInSection.add(new Wire(this.imgWire6));

    // fourth column
    logicInSection.add(new Wire(this.imgWire7));

    // set solved light to red / off
    imgSolvedLight.setImage(redLight);

    // display the logic through the wires
    updateDisplayLogicTrail();
  }

  /**
   * This method decides what colour trail each logic gate should have. The logic trail is also
   * referring to the wire colour.
   */
  private void updateDisplayLogicTrail() {

    for (int i = 0; i < logicInSection.size(); i++) {

      Boolean colour;

      if (logicTrail.get(i + 8) == true) {

        // set colour to Green
        colour = true;
      } else {

        // set colour to Red
        colour = false;
      }

      // set background of pane to colour
      Wire currentWire = logicInSection.get(i);
      currentWire.setBackground(colour);
    }

    if (logicTrail.get(logicTrail.size() - 1) == true) {
      setSolved();
    }
  }

  /**
   * Set and change the user interface and the game's states, once the player has solved the logic
   * gate puzzle.
   */
  private void setSolved() {
    // Play the logic gate puzzle solved sound effect
    AudioManager.loadAudio(Clip.LOGIC_GATE_SOLVED);

    // the puzzle has been solved
    imgSolvedLight.setImage(greenLight);

    String solvedPrompt =
        "Congratulate me on solving the logic gate puzzle, I now have learned what an "
            + currentAssembly.get(currentAssembly.size() - 1).getType()
            + " does";

    ChatMessage inputMessage = new ChatMessage("user", solvedPrompt);

    // clear text area
    taGptText.clear();
    
    // get the gpt response
    getChatResponse(inputMessage, null);

    // debug message in console
    System.out.println("Logic Gate Puzzle Solved");

    // set to solved
    GameState.isLogicGateSolved = true;

    // shows player this is un clickable
    for (ImageView logicGate : currentAssemblyImages) {
      logicGate.setCursor(javafx.scene.Cursor.DEFAULT);
    }

    // disable hint button
    pgHint.setDisable(true);
  }

  /**
   * This method will set currently clicked background to active colour, and will reset non active
   * gates to clear colour.
   *
   * @param active the current gate that is active.
   */
  private void updateActiveBackgrounds(int active) {
    // switch statement to change all gates based on int active

    // clears all
    pneAnswerGate0.setStyle("-fx-background-color: " + "transparent");
    pneAnswerGate1.setStyle("-fx-background-color: " + "transparent");
    pneAnswerGate2.setStyle("-fx-background-color: " + "transparent");
    pneAnswerGate3.setStyle("-fx-background-color: " + "transparent");
    pneAnswerGate4.setStyle("-fx-background-color: " + "transparent");
    pneAnswerGate5.setStyle("-fx-background-color: " + "transparent");
    pneAnswerGate6.setStyle("-fx-background-color: " + "transparent");

    // sets active gate to highlight
    switch (active) {
      case 0:
        pneAnswerGate0.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 1:
        pneAnswerGate1.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 2:
        pneAnswerGate2.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 3:
        pneAnswerGate3.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 4:
        pneAnswerGate4.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 5:
        pneAnswerGate5.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case 6:
        pneAnswerGate6.setStyle("-fx-background-color: #" + this.swappingHighlight);
        break;
      case -1:
        break;
    }
  }

  /**
   * Given two logic gates, logic gate a, and logic gate b, perform a swap. Any user interface
   * changes should be handled.
   *
   * @param a the logic gate, a.
   * @param b the logic gate, b.
   */
  private void swapGates(int a, int b) {
    // swap gates
    LogicGate temp = currentAssembly.get(a);
    currentAssembly.set(a, currentAssembly.get(b));
    currentAssembly.set(b, temp);

    // update last gate, in the first column, to be swapped
    if (a < 4) {
      firstGatesHinted = a;
    } else if (b < 4) {
      firstGatesHinted = b;
    }

    // no current swapping gates
    this.swapping = -1;

    // refresh layout of gates
    updateGateLayout();

    // clear backgrounds
    updateActiveBackgrounds(this.swapping);

    // update logic trail
    updateLogicTrail(false);
  }

  /** This method will set the opacity of the hint button to 0.25 on hover. */
  @FXML
  private void onHintEntered() {
    pgHint.setOpacity(0.25);
  }

  /** This method will set the opacity of the hint button to 0 on exit hover. */
  @FXML
  private void onHintExited() {
    pgHint.setOpacity(0);
  }

  /*
   * This method changes the scene back to the Breaker Room.
   */
  @FXML
  private void onBackToBreaker() {
    App.setUi(AppUi.BREAKER);
  }

  /**
   * This method takes in the currently clicked gate, and determines if it should be highlighted or
   * swapped.
   *
   * @param current the index of the current logic gate.
   */
  private void onClickedGate(int current) {

    // lock game when win
    if (GameState.isLogicGateSolved) {
      return;
    }

    // else if not locked
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

  /**
   * This method will trigger on gate 0 click, triggering the general gate clicked method.
   *
   * @param event when the user clicks on gate 0.
   */
  @FXML
  private void onGate0Clicked(MouseEvent event) {
    // on hover enter
    onClickedGate(0);
  }

  /**
   * This method will trigger on gate 0 hover causeing highlighting.
   *
   * @param event when the user's mouse enters gate 0.
   */
  @FXML
  private void onGate0Enter(MouseEvent event) {
    //
    if (this.swapping != 0 && GameState.isLogicGateSolved != true) {
      pneAnswerGate0.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  /**
   * This method will trigger on gate 0 hover causeing highlighting to cease.
   *
   * @param event when the user's mouse exits gate 0.
   */
  @FXML
  private void onGate0Exit(MouseEvent event) {
    //
    if (this.swapping != 0) {
      pneAnswerGate0.setStyle("-fx-background-color: " + "transparent");
    }
  }

  /**
   * This method will trigger on gate 1 click, triggering the general gate clicked method.
   *
   * @param event when the user clicks on gate 1.
   */
  @FXML
  private void onGate1Clicked(MouseEvent event) {
    //
    onClickedGate(1);
  }

  /**
   * This method will trigger on gate 1 hover causeing highlighting.
   *
   * @param event when the user's mouse enters gate 1.
   */
  @FXML
  private void onGate1Enter(MouseEvent event) {
    //
    if (this.swapping != 1 && GameState.isLogicGateSolved != true) {
      pneAnswerGate1.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  /**
   * This method will trigger on gate 1 hover causeing highlighting to cease.
   *
   * @param event when the user's mouse exits gate 1.
   */
  @FXML
  private void onGate1Exit(MouseEvent event) {
    //
    if (this.swapping != 1) {
      pneAnswerGate1.setStyle("-fx-background-color: " + "transparent");
    }
  }

  /**
   * This method will trigger on gate 2 click, triggering the general gate clicked method.
   *
   * @param event when the user clicks on gate 2.
   */
  @FXML
  private void onGate2Clicked(MouseEvent event) {
    //
    onClickedGate(2);
  }

  /**
   * This method will trigger on gate 2 hover causeing highlighting.
   *
   * @param event when the user's mouse enters gate 2.
   */
  @FXML
  private void onGate2Enter(MouseEvent event) {
    //
    if (this.swapping != 2 && GameState.isLogicGateSolved != true) {
      pneAnswerGate2.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  /**
   * This method will trigger on gate 2 hover causeing highlighting to cease.
   *
   * @param event when the user's mouse exits gate 2.
   */
  @FXML
  private void onGate2Exit(MouseEvent event) {
    //
    if (this.swapping != 2) {
      pneAnswerGate2.setStyle("-fx-background-color: " + "transparent");
    }
  }

  /**
   * This method will trigger on gate 3 click, triggering the general gate clicked method.
   *
   * @param event when the user clicks on gate 3.
   */
  @FXML
  private void onGate3Clicked(MouseEvent event) {
    //
    onClickedGate(3);
  }

  /**
   * This method will trigger on gate 3 hover causeing highlighting.
   *
   * @param event when the user's mouse enters gate 3.
   */
  @FXML
  private void onGate3Enter(MouseEvent event) {
    //
    if (this.swapping != 3 && GameState.isLogicGateSolved != true) {
      pneAnswerGate3.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  /**
   * This method will trigger on gate 3 hover causeing highlighting to cease.
   *
   * @param event when the user's mouse exits gate 3.
   */
  @FXML
  private void onGate3Exit(MouseEvent event) {
    //
    if (this.swapping != 3) {
      pneAnswerGate3.setStyle("-fx-background-color: " + "transparent");
    }
  }

  /**
   * This method will trigger on gate 4 click, triggering the general gate clicked method.
   *
   * @param event when the user clicks on gate 4.
   */
  @FXML
  private void onGate4Clicked(MouseEvent event) {
    //
    onClickedGate(4);
  }

  /**
   * This method will trigger on gate 4 hover causeing highlighting.
   *
   * @param event when the user's mouse enters gate 4.
   */
  @FXML
  private void onGate4Enter(MouseEvent event) {
    //
    if (this.swapping != 4 && GameState.isLogicGateSolved != true) {
      pneAnswerGate4.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  /**
   * This method will trigger on gate 4 hover causeing highlighting to cease.
   *
   * @param event when the user's mouse exits gate 4.
   */
  @FXML
  private void onGate4Exit(MouseEvent event) {
    //
    if (this.swapping != 4) {
      pneAnswerGate4.setStyle("-fx-background-color: " + "transparent");
    }
  }

  /**
   * This method will trigger on gate 5 click, triggering the general gate clicked method.
   *
   * @param event when the user clicks on gate 5.
   */
  @FXML
  private void onGate5Clicked(MouseEvent event) {
    //
    onClickedGate(5);
  }

  /**
   * This method will trigger on gate 5 hover causeing highlighting.
   *
   * @param event when the user's mouse enters gate 5.
   */
  @FXML
  private void onGate5Enter(MouseEvent event) {
    //
    if (this.swapping != 5 && GameState.isLogicGateSolved != true) {
      pneAnswerGate5.setStyle("-fx-background-color: #" + activeHighlight);
    }
  }

  /**
   * This method will trigger on gate 5 hover causeing highlighting to cease.
   *
   * @param event when the user's mouse exits gate 5.
   */
  @FXML
  private void onGate5Exit(MouseEvent event) {
    //
    if (this.swapping != 5) {
      pneAnswerGate5.setStyle("-fx-background-color: " + "transparent");
    }
  }

  /**
   * This method toggles the helper logic gate, labels, and table image of the given helper logic
   * gate.
   *
   * @param helperGate the input helper logic gate.
   */
  private void toggleHelperGate(int helperGate) {

    // get the current helper gate clicked
    ImageView currentGate = helperGates.get(helperGate);

    // get the current image displayed
    Image currentImage = currentGate.getImage();

    // get the gate and table images
    Image gateImage = logicGates.get(helperGate).getImage();
    Image tableImage = logicGates.get(helperGate).getTable();

    if (currentImage == gateImage) {
      // change to table image
      currentGate.setImage(tableImage);

      // hide 'AND' text
      lblHelperGates.get(helperGate).setVisible(false);
    } else {
      // change to gate image
      currentGate.setImage(gateImage);

      // show 'AND' text
      lblHelperGates.get(helperGate).setVisible(true);
    }
  }

  /**
   * This method is the on click method for helper gate 1. This should toggle the helper gate
   * labeled 1.
   *
   * @param event the event to be handled.
   */
  @FXML
  private void onHelper1Clicked(Event event) {
    toggleHelperGate(0);
  }

  /**
   * This method is the on click method for helper gate 2. This should toggle the helper gate
   * labeled 2.
   *
   * @param event the event to be handled.
   */
  @FXML
  private void onHelper2Clicked(Event event) {
    toggleHelperGate(1);
  }

  /**
   * This method is the on click method for helper gate 3. This should toggle the helper gate
   * labeled 3.
   *
   * @param event the event to be handled.
   */
  @FXML
  private void onHelper3Clicked(Event event) {
    toggleHelperGate(2);
  }

  /**
   * When the hint button is clicked, events should be handled. Addtionally, GPT should generate a
   * hint for the player.
   *
   * @param mouseEvent the mouse event to be captured.
   */
  @FXML
  private void onHintClicked(MouseEvent mouseEvent) {
    // no hints for hard mode
    if (GameState.gameDifficulty == Difficulty.HARD) {
      taGptText.clear();
      taGptText.appendText("Hard Mode has Disabled Hints" + "\n\n");
      pgHint.setDisable(true);
      return;
    }

    // If the number of remaining hints is zero
    if (GameState.hintCounter == 0) {
      return;
    }

    // Update the hint counter
    HintManager.updateHintCounter();

    // create a new instance of input chat message object
    ChatMessage inputMessage = new ChatMessage("user", getHintPrompt());

    // clear the text area
    taGptText.clear();

    // get the gpt response
    getChatResponse(inputMessage, pgHint);

  }

  /**
   * This method generates the hint prompt to send to GPT. This method will return the prompt as a
   * String.
   *
   * @return the hint prompt.
   */
  private String getHintPrompt() {
    //
    String hintPrompt;
    LogicGate currentGate;

    // logic inputs to current gate
    boolean logicInputA;
    boolean logicInputB;

    if (!hasHintedAtEndGate) {
      // end gate hint
      currentGate = currentAssembly.get(currentAssembly.size() - 1);

      // the inputs to the last gate
      logicInputA = logicTrail.get(12); // top side input
      logicInputB = logicTrail.get(13); // bottom side input

      hintPrompt =
          "what logic do i need as input to change for the logic gate: "
              + currentGate.getType()
              + " so that its output is true"
              + ", currently the inputs are: "
              + logicInputA
              + " and "
              + logicInputB
              + ". Your answer should not exceed 20 words.";
      hasHintedAtEndGate = true;

    } else {
      // first gates hint
      currentGate = currentAssembly.get(firstGatesHinted);
      // hint for firstGateHint

      // get the inputs for the current gate
      logicInputA = logicTrail.get(firstGatesHinted * 2);
      logicInputB = logicTrail.get(firstGatesHinted * 2 + 1);

      // get the top or bottom side input to the final gate depending on what the current gate side
      // is
      boolean desiredLogic = firstGatesHinted <= 2 ? logicTrail.get(12) : logicTrail.get(13);
      LogicGate nextGate;

      // get the next gate
      if (firstGatesHinted < 2) {
        nextGate = currentAssembly.get(4);
      } else {
        nextGate = currentAssembly.get(5);
      }

      hintPrompt =
          "Given the inputs for the logic gate are: "
              + logicInputA
              + " and "
              + logicInputB
              + " , and the gate is '"
              + currentGate.getType()
              + "' what is the output and how does it relate to wanting to then have another gate"
              + " after it that leads to a "
              + desiredLogic
              + " leading from a "
              + nextGate.getType()
              + ". Your answer should not exceed 20 words.";

      firstGatesHinted++;
      if (firstGatesHinted == 4) {
        firstGatesHinted = 0;
      }
    }

    return hintPrompt;
  }

  /**
   * This method is called when the player clicks the get help icon because they don't know what
   * they should be doing.
   *
   * @param event the event to be handled.
   */
  @FXML
  private void onGetToDoHelp(MouseEvent event) {
    // define the help message
    String helpMessage =
        "Swap the logic gates by clicking them.  \n"
            + "Your Goal is to turn the end light on (green) \n"
            + " Last Gate is covered by glass\n";

    // append help message to text area
    taGptText.clear();
    taGptText.appendText(helpMessage + "\n\n");
  }
}
