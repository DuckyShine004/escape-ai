package nz.ac.auckland.se206.controllers.puzzles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the chat view. */
public class RiddlePuzzleController {
  @FXML private TextArea taChat;
  @FXML private Button btnAnswer1;
  @FXML private Button btnAnswer2;
  @FXML private Button btnAnswer3;
  @FXML private Button btnGetHint;
  @FXML private Label lblTime;
  @FXML private Pane paBack;
  @FXML private Pane paNext;
  @FXML private Pane paBackOverlay;
  @FXML private Pane paNextOverlay;

  @FXML private ImageView imgEmotion;
  @FXML private Image questioningImage;

  private ChatCompletionRequest chatCompletionRequest;
  private String answer1;
  private String answer2;
  private String answer3;
  private StringProperty answer1Property = new SimpleStringProperty();
  private StringProperty answer2Property = new SimpleStringProperty();
  private StringProperty answer3Property = new SimpleStringProperty();
  private StringProperty navigateProperty = new SimpleStringProperty();
  private boolean btn1Pressed = false;
  private boolean btn2Pressed = false;
  private boolean btn3Pressed = false;
  private boolean getHint = false;

  TextToSpeech tts;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  private void initialize() throws ApiProxyException {

    try {
      questioningImage =
          new Image(new FileInputStream("src/main/resources/images/" + "questionmark" + ".png"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    // bind text properties to their buttons
    btnAnswer1.textProperty().bind(answer1Property);
    btnAnswer2.textProperty().bind(answer2Property);
    btnAnswer3.textProperty().bind(answer3Property);

    // set the text of the buttons
    answer1Property.set("Answer 1");
    answer2Property.set("Answer 2");
    answer3Property.set("Answer 3");

    // disable all buttons and the navigate button
    btnAnswer1.setDisable(true);
    btnAnswer2.setDisable(true);
    btnAnswer3.setDisable(true);
    btnGetHint.setDisable(true);
    paNext.setDisable(true);

    // instantiate the tts
    this.tts = GameState.tts;

    // update the scene to get the timer time
    updateScene();

    // only load the riddle when not in developer mode
    if (!GameState.isDeveloperMode) {
      loadRiddle();
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    taChat.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");

    GameState.isSpeaking = true;
    // doesn't need to check if it is currently speaking because it naturally flows given you cannot
    // click buttons at any time

    // ensure the tts only starts if the player is still in the room
    // as the gpt response may come back after the player has backed out of room
  }

  /**
   * Loads a riddle from the GPT model.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private void loadRiddle() {
    // Generate a random number between 0 and 9
    int randomNumber = (int) (Math.random() * 15);

    // Select a concept from the list of concepts
    String[] concepts = {
      "Ethics",
      "Racial Profiling",
      "Privacy",
      "Bias",
      "Consent",
      "Empathy",
      "Sustainability",
      "Human Rights",
      "Justice",
      "Equality",
      "Emotions",
      "Prejudice",
      "Religion",
      "Purpose",
      "Loyalty"
    };
    String concept = concepts[randomNumber];

    // If this is the first riddle, introduce the puzzle
    if (GameState.riddlesSolved == 0) {
      ChatMessage intro =
          new ChatMessage(
              "assistant",
              "Please help me solve these 3 riddles to update the vocabulary in my programming!");
      appendChatMessage(intro);
    }

    // Generate a loading message
    ChatMessage loading =
        new ChatMessage(
            "assistant", "Generating riddle " + (GameState.riddlesSolved + 1) + " of 3...");
    appendChatMessage(loading);

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
              answer2 = concepts[(randomNumber + 1) % 10];
              answer3 = concepts[(randomNumber + 2) % 10];
            }
            // Update the UI thread
            Platform.runLater(
                () -> {
                  answer1Property.set(answer1);
                  answer2Property.set(answer2);
                  answer3Property.set(answer3);
                  btn1Pressed = false;
                  btn2Pressed = false;
                  btn3Pressed = false;
                  btnAnswer1.setDisable(false);
                  btnAnswer2.setDisable(false);
                  btnAnswer3.setDisable(false);
                  btnGetHint.setDisable(false);
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

  /** set a question mark and thinking indicator */
  private void startThinking() {
    //
    imgEmotion.setVisible(true);
    imgEmotion.setImage(questioningImage);
  }

  /** remove question mark and thinking indicator */
  private void stopThinking() {
    //
    imgEmotion.setVisible(false);
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
        appendChatMessage(riddle);
      } else {
        appendChatMessage(result.getChatMessage());
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
    btnGetHint.setDisable(true);

    // Generate a loading message
    if (getHint) {
      ChatMessage loading = new ChatMessage("assistant", "Searching my database...");
      appendChatMessage(loading);
    } else {
      ChatMessage loading = new ChatMessage("assistant", "Analysing your input...");
      appendChatMessage(loading);
    }

    // Create a new thread to run the GPT model
    Task<Void> buttonClickTask =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            ChatMessage responseMsg;
            // Send the button text as a response to GPT
            System.out.println(buttonText);
            if (getHint) {
              responseMsg = runGpt(new ChatMessage("user", "Define: " + buttonText));
              System.out.println(responseMsg.getContent());
              getHint = false;
            } else {
              responseMsg = runGpt(new ChatMessage("user", "Is it " + buttonText));
              System.out.println(responseMsg.getContent());
            }
            // Update UI based on the response
            Platform.runLater(
                () -> {
                  // If the response is from the assistant and the answer is correct, update the
                  // number of riddles solved
                  if (responseMsg.getRole().equals("assistant")
                      && responseMsg
                          .getContent()
                          .startsWith("Yes! That sounds right with my programming!")) {
                    GameState.riddlesSolved++;
                    if (GameState.riddlesSolved == 1 || GameState.riddlesSolved == 2) {
                      navigateProperty.set("Next Riddle");
                    }
                    // If all riddles are solved, update the navigate button text and thank the
                    // player
                    if (GameState.riddlesSolved == 3) {
                      navigateProperty.set("Exit Puzzle");
                      ChatMessage outro =
                          new ChatMessage(
                              "assistant",
                              "That is three riddles solved! Thank you for helping recalibrate my"
                                  + " drives.");
                      appendChatMessage(outro);
                    }
                    // Set the navigate button to be enabled if the riddle is solved
                    paNext.setDisable(false);
                  } else {
                    // If the answer is incorrect, enable the input buttons again for the other
                    // inputs
                    if (!btn1Pressed) {
                      btnAnswer1.setDisable(false);
                    }
                    if (!btn2Pressed) {
                      btnAnswer2.setDisable(false);
                    }
                    if (!btn3Pressed) {
                      btnAnswer3.setDisable(false);
                    }
                    if (GameState.riddlesSolved != 0) {
                      paNext.setDisable(true);
                    } else {
                      paNext.setDisable(false);
                    }
                    btnGetHint.setDisable(false);
                  }
                });
            return null;
          }
        };

    // Start the thread
    Thread newThread = new Thread(buttonClickTask, "Button Click Thread");
    newThread.start();
  }

  @FXML
  private void onGetHintButton(ActionEvent event) throws ApiProxyException {
    // Generate a loading message
    ChatMessage loading = new ChatMessage("assistant", "Select a word to get a hint for...");
    appendChatMessage(loading);

    getHint = true;
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
    App.setUi(AppUi.OFFICE);
  }

  /** When next is clicked, move on to the next thing. */
  @FXML
  private void onNextPaneClicked() {

    tts.stop();

    if (GameState.riddlesSolved == 1 || GameState.riddlesSolved == 2) {
      // If the riddle is solved, load the next riddle and disable the buttons whilst the riddle is
      // loading
      loadRiddle();
      paNext.setDisable(true);
      btnAnswer1.setDisable(true);
      btnAnswer2.setDisable(true);
      btnAnswer3.setDisable(true);
      btnGetHint.setDisable(true);

    } else if (GameState.riddlesSolved == 3) {
      // If all riddles are solved, navigate back to the office
      App.setUi(AppUi.OFFICE);
      GameState.isRiddleResolved = true;
    }
  }

  /**
   * Update all things related to timing here. Such an example is using animation timer to update
   * the timer text on each frame.
   */
  private void updateScene() {
    AnimationTimer animationTimer =
        new AnimationTimer() {
          // This method is called every frame (~60 times per second)
          @Override
          public void handle(long time) {
            // Update the timer text
            lblTime.setText(Timer.getTime());
          }
        };

    // Start the animation timer
    animationTimer.start();
  }
}
