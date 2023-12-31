package nz.ac.auckland.se206.controllers.puzzles;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
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
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.utilities.Timer;

/**
 * The controller for the riddle puzzle, which gives all functionality and implementation for the
 * riddle puzzle.
 */
public class RiddlePuzzleController {
  @FXML private Label lblChat;
  @FXML private Label lblEye1;
  @FXML private Label lblEye2;
  @FXML private Circle crcEye1;
  @FXML private Circle crcEye2;
  @FXML private Button btnAnswer1;
  @FXML private Button btnAnswer2;
  @FXML private Button btnAnswer3;

  @FXML private Label lblTime;
  @FXML private Label lblHintCounter;
  @FXML private Pane paChat;
  @FXML private Pane paBack;
  @FXML private Pane paNext;
  @FXML private Pane paBackOverlay;
  @FXML private Pane paNextOverlay;

  @FXML private Polygon pgHint;

  private ChatCompletionRequest chatCompletionRequest;
  private String chat;
  private String currentRiddle;
  private String answer1;
  private String answer2;
  private String answer3;
  private StringProperty chatProperty = new SimpleStringProperty();
  private StringProperty answer1Property = new SimpleStringProperty();
  private StringProperty answer2Property = new SimpleStringProperty();
  private StringProperty answer3Property = new SimpleStringProperty();
  private StringProperty navigateProperty = new SimpleStringProperty();
  private boolean btn1Pressed = false;
  private boolean btn2Pressed = false;
  private boolean btn3Pressed = false;
  private boolean getHint = false;
  private boolean isThinking = false;
  private int number1;
  private int number2;
  private String eyes = "Nasser";

  private TextToSpeech tts;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  private void initialize() throws ApiProxyException {
    // Add the label to list of labels to be updated
    Timer.addLabel(lblTime);

    // Add the hint counter components
    HintManager.addHintComponents(lblHintCounter, pgHint);

    lblChat.textProperty().bind(chatProperty);

    // bind text properties to their buttons
    btnAnswer1.textProperty().bind(answer1Property);
    btnAnswer2.textProperty().bind(answer2Property);
    btnAnswer3.textProperty().bind(answer3Property);

    chatProperty.set("Chat");

    // set the text of the buttons
    answer1Property.set("Answer 1");
    answer2Property.set("Answer 2");
    answer3Property.set("Answer 3");

    // disable all buttons and the navigate button
    btnAnswer1.setDisable(true);
    btnAnswer2.setDisable(true);
    btnAnswer3.setDisable(true);
    pgHint.setDisable(true);
    paNext.setDisable(false);

    if (GameState.riddlesSolved == 1 || GameState.riddlesSolved == 2) {
      paNext.setDisable(true);
      loadRiddle();
    }

    // instantiate the tts
    this.tts = GameState.tts;

    // If this is the first riddle, introduce the puzzle
    if (GameState.riddlesSolved == 0) {
      appendChatMessage(
          "I need help solving 3 riddles to update the vocabulary in my programming!"
              + "\n\n"
              + "I will give you a riddle about a concept, and you will have to guess the concept."
              + "\n\n"
              + "If you are ready, press the 'Next Riddle' button to begin!");
    }

    HintManager.initializeHintCounter();

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.millis(100), // Duration between updates
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    // Check the isThinking variable
                    if (isThinking) {
                      // Get a random character
                      char randomChar = getRandomCharacter();

                      // Update the eyes
                      updateEyes(randomChar);
                    }
                  }
                }));

    // Set the cycle count to INDEFINITE to keep the timeline running indefinitely
    timeline.setCycleCount(Timeline.INDEFINITE);

    // Start the timeline
    timeline.play();
  }

  /**
   * Generates a random character that is a number, uppercase, or lowercase letter.
   *
   * @return the random character
   */
  private char getRandomCharacter() {
    int randomNum = (int) (Math.random() * 3);
    if (randomNum == 0) {
      return (char) (Math.random() * 10 + '0'); // Random number (0-9)
    } else if (randomNum == 1) {
      return (char) (Math.random() * 10 + 'A'); // Random uppercase letter (A-Z)
    } else if (randomNum == 2) {
      return (char) (Math.random() * 26 + 'a'); // Random lowercase letter (a-z)
    } else {
      return ' ';
    }
  }

  /**
   * Updates the eyes with a random character.
   *
   * @param randomChar the random character to update the eyes with
   */
  private void updateEyes(char randomChar) {
    eyes = eyes.substring(1) + randomChar;
    // Update the label's text with the random character
    lblEye1.setText(eyes.substring(0, 3));
    lblEye2.setText(eyes.substring(3));
  }

  /**
   * Set chat property of label to new chat.
   *
   * @param msg the chat message to append.
   */
  private void appendChatMessage(String chat) {
    chatProperty.set(chat);

    GameState.isSpeaking = true;
    // doesn't need to check if it is currently speaking because it naturally flows given you cannot
    // click buttons at any time

    // ensure the tts only starts if the player is still in the room
    // as the gpt response may come back after the player has backed out of room
  }

  /**
   * Generates a random number between 0 and 20, excluding the two numbers used in the previous
   * riddle.
   *
   * @return the random number.
   */
  private int getRandomNumber() {
    int randomNumber = (int) (Math.random() * 20);
    while (number1 == randomNumber || number2 == randomNumber) {
      randomNumber = (int) (Math.random() * 20);
    }
    return randomNumber;
  }

  /**
   * Loads a riddle from the GPT model.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy.
   */
  private void loadRiddle() {
    // Select a concept from the list of concepts
    String[] concepts = {
      "Ethics",
      "Privacy",
      "Bias",
      "Consent",
      "Empathy",
      "Human Rights",
      "Justice",
      "Equality",
      "Emotions",
      "Prejudice",
      "Religion",
      "Purpose",
      "Loyalty",
      "Integrity",
      "Inclusion",
      "Diversity",
      "Stewardship",
      "Sustainability",
      "Animal Welfare",
      "Racial Profiling"
    };

    // Generate a random number
    int randomNumber = getRandomNumber();

    // Set the number to be used in the next riddle
    String concept = concepts[randomNumber];

    // Generate a loading message
    appendChatMessage("Generating riddle " + (GameState.riddlesSolved + 1) + " of 3...");

    // Create a new thread to run the GPT model
    Task<Void> generateRiddle =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            System.out.println("Task.call method: " + Thread.currentThread().getName());
            // Create a new chat completion request
            chatCompletionRequest =
                new ChatCompletionRequest()
                    .setN(1)
                    .setTemperature(0.2)
                    .setTopP(0.5)
                    .setMaxTokens(100);
            ChatMessage gptResponse =
                runGpt(new ChatMessage("user", GptPromptEngineering.getRiddlePuzzle(concept)));
            System.out.println(gptResponse.getContent());
            // If the response is from the assistant, process the output
            if (gptResponse != null && gptResponse.getRole().equals("assistant")) {
              processGptOutputForButtons(gptResponse.getContent(), concept);
            } else {
              // If GPT does not provide options in the correct format, generate them manually
              answer1 = concept;
              answer2 = concepts[(randomNumber + 1) % 20];
              answer3 = concepts[(randomNumber + 2) % 20];
            }
            // Update the UI thread
            Platform.runLater(
                () -> {
                  appendChatMessage(chat);
                  answer1Property.set(answer1);
                  answer2Property.set(answer2);
                  answer3Property.set(answer3);
                  btn1Pressed = false;
                  btn2Pressed = false;
                  btn3Pressed = false;
                  btnAnswer1.setDisable(false);
                  btnAnswer2.setDisable(false);
                  btnAnswer3.setDisable(false);
                  pgHint.setDisable(false);
                });
            return null;
          }

          /*
           * Notify if the API call succeeded
           */
          @Override
          protected void succeeded() {
            super.succeeded();
            System.out.println("Successfully loaded");
          }

          /*
           * Notify if the API call failed
           */
          @Override
          protected void failed() {
            super.failed();
            System.out.println("Failed to load");
            loadRiddle();
          }
        };

    generateRiddle.setOnRunning(
        event -> {
          startThinking();
        });

    generateRiddle.setOnSucceeded(
        event -> {
          stopThinking();
        });

    generateRiddle.setOnFailed(
        event -> {
          stopThinking();
        });

    // Start the thread
    Thread newThread = new Thread(generateRiddle, "New Thread");
    newThread.start();
  }

  /** starting thinking and set the thinking components to visible. */
  private void startThinking() {
    lblEye1.setVisible(true);
    lblEye2.setVisible(true);
    crcEye1.setVisible(true);
    crcEye2.setVisible(true);
    isThinking = true;
  }

  /** stop thinking and set the thinking components to not visible. */
  private void stopThinking() {
    lblEye2.setVisible(false);
    lblEye1.setVisible(false);
    crcEye1.setVisible(false);
    crcEye2.setVisible(false);
    isThinking = false;
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    // Send the message to the GPT model
    try {
      ChatMessage riddle = null;
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      // Format the ridddle correctly
      if (result.getChatMessage().getRole().equals("assistant")
          && result.getChatMessage().getContent().startsWith("Riddle:")) {
        if (result.getChatMessage().getContent().indexOf('^') != 1) {
          riddle =
              new ChatMessage(
                  "assistant",
                  result
                      .getChatMessage()
                      .getContent()
                      .substring(
                          result.getChatMessage().getContent().indexOf(':') + 1,
                          result.getChatMessage().getContent().indexOf('^')));
        }
        currentRiddle = riddle.getContent();
        chat = currentRiddle;
      } else {
        chat = result.getChatMessage().getContent();
      }
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Processes the output from the GPT model to generate the buttons.
   *
   * @param gptOutput the output from the GPT model
   * @param concept the concept to be used in the riddle
   */
  private void processGptOutputForButtons(String gptOutput, String concept) {
    String[] segments = gptOutput.split("\\}");
    int randomNumber = (int) (Math.random() * 3);
    boolean conceptPresent = false;

    // Check if the concept is already present in the options
    for (int i = 0; i < segments.length; i++) {
      if (segments[i].contains(concept)) {
        conceptPresent = true;
      }
    }

    // If the concept is not present, add it to the options
    if (!conceptPresent) {
      segments[randomNumber] = concept;
    }

    // Change the answer options to the options generated by GPT
    if (segments.length >= 2) {
      answer1 =
          segments[(0 + randomNumber) % 3].substring(
              segments[(0 + randomNumber) % 3].lastIndexOf("{") + 1);
      answer2 =
          segments[(1 + randomNumber) % 3].substring(
              segments[(1 + randomNumber) % 3].lastIndexOf("{") + 1);
      answer3 =
          segments[(2 + randomNumber) % 3].substring(
              segments[(2 + randomNumber) % 3].lastIndexOf("{") + 1);
    } else {
      // If GPT does not provide options in the correct format, generate them manually
      answer1 = concept;
      answer2 = "Death";
      answer3 = "Culture";
    }
  }

  /*
   * Handle input buttion option clicks
   *
   * @param event the action event triggered by the button click
   */
  @FXML
  private void onButtonClicked(ActionEvent event) throws ApiProxyException {
    Button clickedButton = (Button) event.getSource();
    // Get the text from the button
    String buttonText = clickedButton.getText();

    if (!getHint) {
      // Set the button pressed to true
      if (buttonText.equals(answer1)) {
        btn1Pressed = true;
      } else if (buttonText.equals(answer2)) {
        btn2Pressed = true;
      } else if (buttonText.equals(answer3)) {
        btn3Pressed = true;
      }
    }

    // Disable all buttons and the navigate button when a button has been clicked
    btnAnswer1.setDisable(true);
    btnAnswer2.setDisable(true);
    btnAnswer3.setDisable(true);
    paNext.setDisable(true);
    pgHint.setDisable(true);

    // Generate a loading message
    if (getHint) {
      appendChatMessage("Searching my database...");
    } else {
      appendChatMessage("Analysing your input...");
    }

    // Create a new thread to run the GPT model
    Task<Void> buttonClickTask =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            ChatMessage responseMsg;
            // Send the button text as a response to GPT
            System.out.println(buttonText);
            startThinking();
            if (getHint) {
              responseMsg = runGpt(new ChatMessage("user", "Define: " + buttonText));
              System.out.println(responseMsg.getContent());
              chat = responseMsg.getContent();
              getHint = false;
            } else {
              responseMsg = runGpt(new ChatMessage("user", "Is it " + buttonText));
              System.out.println(responseMsg.getContent());
              chat = responseMsg.getContent();
            }
            // Update UI based on the response
            Platform.runLater(
                () -> {
                  stopThinking();
                  // If the response is from the assistant and the answer is correct, update the
                  // number of riddles solved
                  if (responseMsg.getRole().equals("assistant")
                      && responseMsg
                          .getContent()
                          .startsWith("Yes! That sounds right with my programming!")) {
                    AudioManager.loadAudio(Clip.RIDDLE_SOLVED);
                    GameState.riddlesSolved++;
                    if (GameState.riddlesSolved == 1 || GameState.riddlesSolved == 2) {
                      navigateProperty.set("Next Riddle");
                    }
                    // If all riddles are solved, update the navigate button text and thank the
                    // player
                    if (GameState.riddlesSolved == 3) {
                      navigateProperty.set("Exit Puzzle");
                      chat +=
                          "\n\n"
                              + "That is three riddles solved! Thank you for helping recalibrate my"
                              + " drives.";
                    }
                    // Set the navigate button to be enabled if the riddle is solved
                    paNext.setDisable(false);
                  } else {
                    chat += "\n\n" + "Remember," + currentRiddle;
                    // If the answer is incorrect, enable the input buttons again for the other
                    // inputs
                    if (!btn1Pressed) {
                      AudioManager.loadAudio(Clip.WRONG);
                      btnAnswer1.setDisable(false);
                    }
                    if (!btn2Pressed) {
                      AudioManager.loadAudio(Clip.WRONG);
                      btnAnswer2.setDisable(false);
                    }
                    if (!btn3Pressed) {
                      AudioManager.loadAudio(Clip.WRONG);
                      btnAnswer3.setDisable(false);
                    }
                    if (GameState.riddlesSolved != 3) {
                      paNext.setDisable(true);
                    } else {
                      paNext.setDisable(false);
                    }
                    pgHint.setDisable(false);
                  }
                  appendChatMessage(chat);
                });
            return null;
          }
        };

    // Start the thread
    Thread newThread = new Thread(buttonClickTask, "Button Click Thread");
    newThread.start();
  }

  @FXML
  private void onHintClicked(MouseEvent event) throws ApiProxyException {
    // If the difficulty is hard, ignore user.
    if (GameState.gameDifficulty == Difficulty.HARD) {
      return;
    }

    // If the number of remaining hints is zero
    if (GameState.hintCounter == 0) {
      return;
    }

    // If the hint button is already pressed
    if (getHint == true) {
      return;
    }

    // If there currently is no riddle
    if (currentRiddle == null) {
      return;
    }

    // Update the hint counter
    HintManager.updateHintCounter();

    // Generate a loading message
    appendChatMessage("Select a word to get a hint for...");

    getHint = true;
  }

  /**
   * When the mouse is hovering over the hint button, the hint button is highlighted.
   *
   * @param mouseEvent the mouse event
   */
  @FXML
  private void onHintEntered() {
    if (currentRiddle != null || GameState.gameDifficulty == Difficulty.HARD) {
      pgHint.setOpacity(0.25);
      pgHint.setCursor(Cursor.HAND);
    } else {
      pgHint.setCursor(Cursor.DEFAULT);
    }
  }

  /**
   * When the mouse is not hovering over the hint button, the hint button is not highlighted.
   *
   * @param mouseEvent the mouse event
   */
  @FXML
  private void onHintExited() {
    if (currentRiddle != null || GameState.gameDifficulty == Difficulty.HARD) {
      pgHint.setOpacity(0);
      pgHint.setCursor(Cursor.DEFAULT);
    }
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

  /** When the mouse is hovering over the pane, the overlay appears (next). */
  @FXML
  private void onNextPaneEntered() {
    paNextOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (next). */
  @FXML
  private void onNextPaneExited() {
    paNextOverlay.setVisible(false);
  }

  /** When back is clicked, go back to previous section (control room). */
  @FXML
  private void onBackPaneClicked() {
    if (GameState.riddlesSolved == 3) {
      GameState.isRiddleResolved = true;
    }
    App.setUi(AppUi.OFFICE);
  }

  /** When next is clicked, move on to the next thing. */
  @FXML
  private void onNextPaneClicked() {

    tts.stop();

    if (GameState.riddlesSolved == 0
        || GameState.riddlesSolved == 1
        || GameState.riddlesSolved == 2) {
      // If the riddle is solved, load the next riddle and disable the buttons whilst the riddle is
      // loading
      loadRiddle();
      paNext.setDisable(true);
      btnAnswer1.setDisable(true);
      btnAnswer2.setDisable(true);
      btnAnswer3.setDisable(true);
      pgHint.setDisable(true);

    } else if (GameState.riddlesSolved == 3) {
      // If all riddles are solved, navigate back to the office
      App.setUi(AppUi.OFFICE);
      GameState.isRiddleResolved = true;
    }
  }
}
