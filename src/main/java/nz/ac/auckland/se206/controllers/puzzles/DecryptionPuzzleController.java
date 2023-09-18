package nz.ac.auckland.se206.controllers.puzzles;

import java.lang.reflect.Field;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.Algorithm;
import nz.ac.auckland.se206.constants.Declaration;
import nz.ac.auckland.se206.constants.Description;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.Sequence;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the decryption puzzle scene. */
public class DecryptionPuzzleController {
  @FXML private Pane paBack;
  @FXML private Pane paDecryption;
  @FXML private Pane paBackOverlay;

  @FXML private Label lblTime;
  @FXML private Label lblDigit0;
  @FXML private Label lblDigit1;
  @FXML private Label lblDigit2;
  @FXML private Label lblDigit3;

  @FXML private TextArea taChat;
  @FXML private TextArea taPseudocode;

  @FXML private TextField tfChat;

  private int psuedocodeIndex;

  private String sequence;
  private String algorithm;
  private String description;
  private String declaration;

  private ChatCompletionRequest gptRequest;

  /** Initializes the decryption puzzle. */
  @FXML
  private void initialize() throws Exception {
    // add the label to list of labels to be updated
    Timer.addLabel(lblTime);

    // initialize GPT
    initiailizeChat();

    // initialize the pseudocode puzzle
    initializePseudocode();
  }

  /** When the mouse is hovering over the pane, the overlay appears (back). */
  @FXML
  private void onBackPaneEntered() {
    paBackOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (back). */
  @FXML
  private void onBackPaneExited() {
    paBackOverlay.setVisible(false);
  }

  /** When back is clicked, go back to previous section (control room). */
  @FXML
  private void onBackPaneClicked() {
    App.setUi(AppUi.TERMINAL);
  }

  /**
   * Check if there is a keyboard event. If there is a keyboard event, handle the event
   * appropriately.
   *
   * @param keyEvent this event is generated when a key is pressed, released, or typed
   */
  @FXML
  private void onKeyPressed(KeyEvent keyEvent) {
    String userInput = "";

    // get the user input from the chat text field
    if (keyEvent.getCode() == KeyCode.ENTER) {
      userInput = tfChat.getText();
    }

    // trim the user input
    userInput = userInput.trim();

    // check if the user input is empty
    if (userInput == null || userInput.isEmpty()) {
      return;
    }

    // check whether the user has entered a sequence or not
    if (isUserInputSequence(userInput)) {
      setUserSequence(userInput);
      return;
    }

    // initialize user chat message object
    ChatMessage userMessage;

    // create a new instance of user chat message object
    userMessage = new ChatMessage("user", userInput);

    // append the user's response to the text area
    setUserResponse(userInput);

    // get chatGPT's response and append it to the chatting text area
    getChatResponse(userMessage);
  }

  /**
   * Initialize GPT. Set the tokens and create a new instance of GPT request.
   *
   * <p>Note: I would love to be able to name this method 'initializeGPT'. Unfortunately, we are not
   * allowed to have acronyms as method names as per the naming convention.
   */
  private void initiailizeChat() {
    // initialize the chat message field
    ChatMessage gptMessage;

    // initialize GPT chat message object
    gptMessage = new ChatMessage("assistant", GptPromptEngineering.initializeDecryptionResponse());

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
    getChatResponse(gptMessage);
  }

  /**
   * Initialize pseudocode instances. The method should get a 'random' pseudocode each round.
   *
   * @throws Exception thrown when there is an error initializing pseudocode instances.
   */
  private void initializePseudocode() throws Exception {
    // get a random pseudo code
    psuedocodeIndex = (int) Math.random() * GameState.maxPseudocodes;

    // get the sequence, declaration, desciption, and algorithm of the pseudocode
    intializeSequence();
    initializeDescription();
    initializeDeclaration();
    initializeAlgorithm();

    // append the description, declaration, and algorithm to the text area
    taPseudocode.appendText(description);
    taPseudocode.appendText(declaration);
    taPseudocode.appendText(algorithm);
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
   * Get the string declaration code snippet for the corresponding random pseudocode index.
   *
   * @return the string value of the declaration code snippet.
   * @throws Exception throw when class or field name is not found.
   */
  private void initializeDeclaration() throws Exception {
    // get the field name for the corresponding random pseudocode index
    String fieldName = "declaration" + Integer.toString(psuedocodeIndex);

    // create an object of 'Declaration'
    Declaration declaration = new Declaration();

    // create a runtime reference to the class, 'Declaration'
    Class<?> cls = declaration.getClass();

    // get the field for the corresponding field name
    Field fld = cls.getField(fieldName);

    // retrieve the object value from the field and cast it to string
    this.declaration = (String) fld.get(declaration);
  }

  /**
   * Generate a response from GPT.
   *
   * @param entityMessage the chat message to be sent to GPT.
   */
  private void getChatResponse(ChatMessage entityMessage) {
    // add user input to GPT's user input history
    gptRequest.addMessage(entityMessage);

    // create a concurrent task for handling GPT response
    Task<Void> gptTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // set GPT's response
            setChatResponse();
            return null;
          }
        };

    // create a thread to handle GPT concurrency
    Thread gptThread = new Thread(gptTask);

    // start the thread
    gptThread.start();
  }

  /**
   * @param userInput the user's input from the text field.
   * @return the user's sequence.
   */
  private String getUserSequence(String userInput) {
    int position;

    // Stop at the first digit
    for (position = 0; position < userInput.length(); position++) {
      if (Character.isDigit(userInput.charAt(position))) {
        break;
      }
    }

    // Get the next four characters from the user's input
    String userSequence = userInput.substring(position, position + 4);

    return userSequence;
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
    taChat.appendText("ai> " + gptOutput + "\n\n");
  }

  /**
   * Set the user's response. The user's response will be appended to the chat.
   *
   * @param userInput the user input from the text field.
   */
  private void setUserResponse(String userInput) {
    // append the text to the chat text area
    taChat.appendText("user> " + userInput + "\n\n");

    // clear the text field user input
    tfChat.clear();
  }

  private void setUserSequence(String userInput) {
    String userSequence = getUserSequence(userInput);

    // Check if the user sequence is correct or not
    if (userSequence.equals(sequence)) {
      handleCorrectSequence(userSequence);
    } else {
      handleIncorrectSequence(userSequence);
    }

    // Update the user's sequence on the red boxes
    updateUserSequence(userSequence);

    // Set the user's sequence
    setUserResponse(userSequence);
  }

  private void handleCorrectSequence(String userSequence) {
    System.out.println("SEQUENCE IS CORRECT");
  }

  private void handleIncorrectSequence(String userSequence) {
    System.out.println("SEQUENCE IS INCORRECT");
  }

  private void updateUserSequence(String userSequence) {}

  /**
   * Check if the user's input has contains a four digit number. If it does, then handle it in
   * another function call.
   *
   * @param userInput the user's input from the text field.
   * @return a boolean based on whether the user's input is a sequence.
   */
  private Boolean isUserInputSequence(String userInput) {
    // Check if the sequence is contained within the user input
    if (!userInput.contains(sequence)) {
      return false;
    }

    // Check if there are only four digits in the user's input
    if (!isUserSequenceFourDigits(userInput)) {
      return false;
    }

    // Check if the digits are contiguous in the user's input
    if (!isUserSequenceContiguous(userInput)) {
      return false;
    }

    return true;
  }

  /**
   * Check whether the user's input contains four digits or not.
   *
   * @param userInput
   * @return a boolean value based on whether the user's input contains four digits.
   */
  private Boolean isUserSequenceFourDigits(String userInput) {
    int count = 0;

    // Go through the user's input and check if there are only four digits
    for (int i = 0; i < userInput.length(); i++) {
      if (Character.isDigit(userInput.charAt(i))) {
        count++;
      }
    }

    return (count == 4 ? true : false);
  }

  /**
   * Check whether there exists four contiguous digits in the user's input.
   *
   * @param userInput the user's input from the text field.
   * @return a boolean value based on whether there are four contiguous digits.
   */
  private Boolean isUserSequenceContiguous(String userInput) {
    int position;

    // Stop at the first digit
    for (position = 0; position < userInput.length(); position++) {
      if (Character.isDigit(userInput.charAt(position))) {
        break;
      }
    }

    // Get the next four characters from the user's input
    String userSequence = userInput.substring(position, position + 4);

    return isUserSequenceNumeric(userSequence);
  }

  /**
   * Check if the user's sequence is numeric.
   *
   * @param userSequence the user's sequence.
   * @return a boolean value whether the user's sequence is a valid four digit number.
   */
  private Boolean isUserSequenceNumeric(String userSequence) {
    return userSequence.matches("-?\\d+(\\.\\d+)?");
  }
}
