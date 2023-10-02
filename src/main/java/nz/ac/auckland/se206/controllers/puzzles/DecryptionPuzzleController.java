package nz.ac.auckland.se206.controllers.puzzles;

import java.lang.reflect.Field;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.HintManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.Algorithm;
import nz.ac.auckland.se206.constants.Description;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.GameState.Difficulty;
import nz.ac.auckland.se206.constants.Sequence;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the decryption puzzle scene. */
public class DecryptionPuzzleController {
  @FXML private Pane paBack;
  @FXML private Pane paHint;
  @FXML private Pane paDecryption;
  @FXML private Pane paBackOverlay;

  @FXML private Label lblTime;
  @FXML private Label lblHintCounter;

  @FXML private Polygon pgHint;

  @FXML private TextArea taChat;
  @FXML private TextArea taPseudocode;

  private int hintIndex;
  private int psuedocodeIndex;

  private String sequence;
  private String algorithm;
  private String pseudocode;
  private String description;

  private ChatCompletionRequest gptRequest;

  private TextToSpeech tts;

  /** Initializes the decryption puzzle. */
  @FXML
  private void initialize() throws Exception {
    // get the game state instance of tts
    this.tts = GameState.tts;

    // Add the label to list of labels to be updated
    Timer.addLabel(lblTime);

    // Add the hint counter components
    HintManager.addHintComponents(lblHintCounter, pgHint);

    // Initialize the pseudocode and algorithms
    initializePseudocode();
  }

  /** When the mouse is hovering over the pane, the overlay appears (hint). */
  @FXML
  private void onHintPaneEntered() {
    paHint.setStyle("-fx-background-color: rgb(29,30,37);");
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (hint). */
  @FXML
  private void onHintPaneExited() {
    paHint.setStyle("-fx-background-color: rgb(20,20,23);");
  }

  /** When the mouse is hovering over the pane, the overlay appears (back). */
  @FXML
  private void onBackPaneEntered() {
    paBack.setStyle("-fx-background-color: rgb(29,30,37);");
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (back). */
  @FXML
  private void onBackPaneExited() {
    paBack.setStyle("-fx-background-color: rgb(20,20,23);");
  }

  /** When hint is clicked, give the user a hint. */
  @FXML
  private void onHintPaneClicked() {
    // If the difficulty is hard, ignore user.
    if (GameState.gameDifficulty == Difficulty.HARD) {
      return;
    }

    // If the number of remaining hints is zero
    if (GameState.hintCounter == 0) {
      return;
    }

    getUserHint();
  }

  /** When back is clicked, go back to previous section (control room). */
  @FXML
  private void onBackPaneClicked() {
    App.setUi(AppUi.TERMINAL);
  }

  /**
   * Initialize GPT. Set the tokens and create a new instance of GPT request.
   *
   * <p>Note: I would love to be able to name this method 'initializeGPT'. Unfortunately, we are not
   * allowed to have acronyms as method names as per the naming convention.
   */
  private void initializeChat() {
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
  }

  /**
   * Initialize pseudocode instances. The method should get a 'random' pseudocode each round.
   *
   * @throws Exception thrown when there is an error initializing pseudocode instances.
   */
  private void initializePseudocode() throws Exception {
    // Get a random pseudo code
    psuedocodeIndex = (int) (Math.random() * (GameState.maxPseudocodes));

    System.out.println("Current psuedocode: " + psuedocodeIndex);

    // Hint index is initially zero
    hintIndex = 0;

    // Initialize the sequence
    intializeSequence();

    // Initialize the description and algorithm
    initializeDescription();
    initializeAlgorithm();

    // Append the description and algorithm to the text area
    taPseudocode.appendText(description);
    taPseudocode.appendText(algorithm);

    // Get the pseudocode in string form
    pseudocode = description + algorithm;
  }

  /**
   * Get the string sequence for the corresponding random pseudocode index.
   *
   * @return the string value of the sequence.
   * @throws Exception throw when class or field name is not found.
   */
  private void intializeSequence() throws Exception {
    // get the field name for the corresponding random pseudocode index
    String fieldName = "sequence" + Integer.toString(psuedocodeIndex);

    // create an object of 'Sequence'
    Sequence sequence = new Sequence();

    // create a runtime reference to the class, 'Sequence'
    Class<?> cls = sequence.getClass();

    // get the field for the corresponding field name
    Field fld = cls.getField(fieldName);

    // retrieve the object value from the field and cast it to string
    this.sequence = (String) fld.get(sequence);
  }

  /**
   * Get the string algorithm code snippet for the corresponding random pseudocode index.
   *
   * @return the string value of the algorithm code snippet.
   * @throws Exception throw when class or field name is not found.
   */
  private void initializeAlgorithm() throws Exception {
    // get the field name for the corresponding random pseudocode index
    String fieldName = "algorithm" + Integer.toString(psuedocodeIndex);

    // create an object of 'Algorithm'
    Algorithm algorithm = new Algorithm();

    // create a runtime reference to the class, 'Algorithm'
    Class<?> cls = algorithm.getClass();

    // get the field for the corresponding field name
    Field fld = cls.getField(fieldName);

    // retrieve the object value from the field and cast it to string
    this.algorithm = (String) fld.get(algorithm);
  }

  /**
   * Get the string description for the corresponding random pseudocode index.
   *
   * @return the string value of the description.
   * @throws Exception throw when class or field name is not found.
   */
  private void initializeDescription() throws Exception {
    // get the field name for the corresponding random pseudocode index
    String fieldName = "description" + Integer.toString(psuedocodeIndex);

    // create an object of 'Description'
    Description description = new Description();

    // create a runtime reference to the class, 'Description'
    Class<?> cls = description.getClass();

    // get the field for the corresponding field name
    Field fld = cls.getField(fieldName);

    // retrieve the object value from the field and cast it to string
    this.description = (String) fld.get(description);
  }

  /**
   * Generate a response from GPT.
   *
   * @param entityMessage the chat message to be sent to GPT.
   */
  private void getChatResponse(ChatMessage entityMessage, boolean isHint) {
    // add user input to GPT's user input history
    gptRequest.addMessage(entityMessage);

    // create a concurrent task for handling GPT response
    Task<Void> gptTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // Set GPT's response
            setChatResponse();
            return null;
          }
        };

    // If the task is running, disable certain components
    gptTask.setOnRunning(
        event -> {
          disableComponents();
        });

    // If the task succeeds, then enable components
    gptTask.setOnSucceeded(
        event -> {
          enableComponents();
        });

    // If the task fails, then enable components
    gptTask.setOnFailed(
        event -> {
          enableComponents();
        });

    // Create a thread to handle GPT concurrency
    Thread gptThread = new Thread(gptTask);

    // Start the thread
    gptThread.start();
  }

  /** Generate a GPT response. GPT should give a hint for the current pseudocode. */
  private void getUserHint() {
    // Initialize a new instance of gpt
    initializeChat();

    // Update the hint counter
    HintManager.updateHintCounter();

    // Get the incorrect line number for the following pseudocode and hint index
    int lineNumber = Integer.valueOf(sequence.charAt(hintIndex));

    // Get the hint prompt for GPT to analyze and generate a response
    String hint = GptPromptEngineering.getDecryptionHint(pseudocode, lineNumber);

    // Initialize a user hint message compatible for GPT to analyze
    ChatMessage userHintMessage = new ChatMessage("assistant", hint);

    // Get GPT's response
    getChatResponse(userHintMessage, true);

    // Update the hint index
    hintIndex = (hintIndex + 1) % GameState.maxSequence;
  }

  /**
   * Set the chat response from GPT. This includes printing the response to the text area.
   *
   * @throws Exception thrown when we fail to retrieve a response from GPT.
   */
  private void setChatResponse() throws Exception {
    // Get GPT's response
    ChatCompletionResult gptResult = gptRequest.execute();

    // Get GPT's choice
    Choice gptChoice = gptResult.getChoices().iterator().next();

    // Get GPT's chat message
    ChatMessage gptMessage = gptChoice.getChatMessage();

    // Get the content of gpt's message in the form of a string
    String gptOutput = gptMessage.getContent();

    // Append the result to the text area
    taChat.appendText("ai> " + gptOutput + "\n\n");

    // Make text-to-speech read GPT's output
    tts.speak(gptOutput, AppUi.DECRYPTION);
  }

  /** Enable components when a task is finished. */
  private void enableComponents() {
    // Enable the hint pane
    paHint.setDisable(false);
  }

  /** disable components when a task is running. */
  private void disableComponents() {
    // Disable the hint pane
    paHint.setDisable(true);
  }
}
