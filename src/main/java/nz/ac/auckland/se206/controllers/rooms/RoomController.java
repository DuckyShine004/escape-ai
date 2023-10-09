package nz.ac.auckland.se206.controllers.rooms;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.HintManager;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.GameState.Difficulty;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public abstract class RoomController {
  @FXML private Pane paRoom;
  @FXML private ImageView imgRoom;

  @FXML private Label lblTime;

  @FXML private Button btnHint;
  @FXML private Button btnLeft;
  @FXML private Button btnRight;

  @FXML private TextArea taChat;
  @FXML private TextField tfChat;
  @FXML private VBox vbChat;
  @FXML private Label lblAiChat;
  @FXML private Label lblPlayerChat;
  @FXML private Label lblOldestChat;
  @FXML protected Label lblAiChat2;

  @FXML private Rectangle recOpaque;
  @FXML private Polygon pgHint;

  @FXML private ImageView imgAvatar;
  @FXML private ImageView imgAvatarShaddow;
  @FXML private ImageView imgEmotion;
  @FXML private Label lblEye1;
  @FXML private Label lblEye2;
  @FXML private Circle crcEye1;
  @FXML private Circle crcEye2;

  private static StringProperty aiChatProperty = new SimpleStringProperty();
  private static StringProperty playerChatProperty = new SimpleStringProperty();
  private static StringProperty oldestChatProperty = new SimpleStringProperty();
  private static ChatCompletionRequest gptRequest;

  protected static boolean isThinking = false;
  protected static String eyes = "Nasser";
  protected static boolean chatBubbleVisible = true;

  /** Initialize the room. */
  @FXML
  protected void initialize() {
    lblAiChat.textProperty().bind(aiChatProperty);
    aiChatProperty.set(GameState.currentAiMessage);
    lblPlayerChat.textProperty().bind(playerChatProperty);
    playerChatProperty.set(GameState.currentPlayerMessage);
    lblOldestChat.textProperty().bind(oldestChatProperty);
    lblAiChat2.textProperty().bind(aiChatProperty);
    initializeChat();

    createNewTimeLine();

    imgRoom.setOnMouseClicked(
        event -> {
          // Check if the event's target is not one of the labels
          if (event.getTarget() != lblAiChat2 && GameState.isChatting == false) {
            lblAiChat2.setVisible(false);
            chatBubbleVisible = false;
          }
        });
  }

  /**
   * Initialize GPT. Set the tokens and create a new instance of GPT request.
   *
   * <p>Note: I would love to be able to name this method 'initializeGPT'. Unfortunately, we are not
   * allowed to have acronyms as method names as per the naming convention.
   */
  public void initializeChat() {
    // initialize the chat message field
    ChatMessage gptMessage;

    // initialize GPT chat message object
    gptMessage = new ChatMessage("assistant", GptPromptEngineering.initializeBackStory());

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
    getChatResponse(gptMessage, false);
  }

  /**
   * Generate a response from GPT.
   *
   * @param entityMessage the chat message to be sent to GPT.
   */
  public void getChatResponse(ChatMessage entityMessage, boolean isHint) {
    // Disable all chat components
    disableChatComponents();

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
          startThinking();
        });

    // If the task succeeds, then enable components
    gptTask.setOnSucceeded(
        event -> {
          enableChatComponents();
          stopThinking();
        });

    // If the task fails
    gptTask.setOnFailed(
        event -> {
          enableChatComponents();
          stopThinking();
          System.out.println("FAILED TO GENERATE RESPONSE");
        });

    // Create a thread to handle GPT concurrency
    Thread gptThread = new Thread(gptTask);

    // Start the thread
    gptThread.start();
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

    // Add the message to GPT's context
    gptRequest.addMessage(gptMessage);

    // Update the AI chat label on the FX application thread
    Platform.runLater(
        () -> {
          setAiMessage(gptOutput);
        });
  }

  /**
   * Update the chat area given the response was from the user.
   *
   * @param message the message to be appended to the text area.
   */
  public void setUserResponse(String message) {
    swapLabelsOrder();
    if (GameState.currentPlayerMessage != "" && GameState.currentAiMessage != "") {
      oldestChatProperty.set(GameState.currentPlayerMessage);
      lblOldestChat.setVisible(true);
    }
    GameState.currentPlayerMessage = message;
    playerChatProperty.set(GameState.currentPlayerMessage);
    tfChat.clear();
    lblPlayerChat.setVisible(true);
    lblOldestChat.getStyleClass().remove("chat-bubble");
    lblOldestChat.getStyleClass().add("chat-bubble1");
  }

  /** Create a new timeline for updating the eye labels. */
  private void createNewTimeLine() {
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.millis(109), // Duration between updates
                new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    // Check the isThinking variable
                    if (isThinking) {
                      int randomNum = (int) (Math.random() * 3);
                      char randomChar = ' ';
                      if (randomNum == 0) {
                        randomChar =
                            (char) (Math.random() * 26 + 'A'); // Random uppercase letter (A-Z)
                      } else if (randomNum == 1) {
                        randomChar =
                            (char) (Math.random() * 10 + '0'); // Random uppercase letter (A-Z)
                      } else if (randomNum == 2) {
                        randomChar =
                            (char) (Math.random() * 26 + 'a'); // Random uppercase letter (A-Z)
                      }

                      eyes = eyes.substring(1) + randomChar;
                      // Update the label's text with the random character
                      lblEye1.setText(eyes.substring(0, 3));
                      lblEye2.setText(eyes.substring(3));
                    }
                  }
                }));

    // Set the cycle count to INDEFINITE to keep the timeline running indefinitely
    timeline.setCycleCount(Timeline.INDEFINITE);

    // Start the timeline
    timeline.play();
  }
  

  @FXML
  protected void onAiClicked(MouseEvent event) {
    GameState.muted = GameState.muted == false;
    GameState.tts.stop();
    GameState.isChatting = !GameState.isChatting;
    if (GameState.isChatting) {
      vbChat.setVisible(true);
      tfChat.setVisible(true);
      recOpaque.setVisible(true);
      lblAiChat2.setVisible(false);
      chatBubbleVisible = false;
      if (GameState.currentAiMessage == "") {
        lblAiChat.setVisible(false);
        lblOldestChat.setVisible(false);
      }
      if (GameState.currentPlayerMessage == "") {
        lblPlayerChat.setVisible(false);
        lblOldestChat.setVisible(false);
      }
    } else {
      vbChat.setVisible(false);
      tfChat.setVisible(false);
      recOpaque.setVisible(false);
      lblAiChat2.setVisible(true);
      chatBubbleVisible = true;
    }
  }

  /** starting thinking and set the thinking components to visible */
  public void startThinking() {
    tfChat.setDisable(true);
    tfChat.setOpacity(0.5);
    lblEye1.setVisible(true);
    lblEye2.setVisible(true);
    crcEye1.setVisible(true);
    crcEye2.setVisible(true);
    isThinking = true;
  }

  /** stop thinking and set the thinking components to not visible */
  public void stopThinking() {
    tfChat.setDisable(false);
    tfChat.setOpacity(1);
    lblEye2.setVisible(false);
    lblEye1.setVisible(false);
    crcEye1.setVisible(false);
    crcEye2.setVisible(false);
    isThinking = false;
  }

  @FXML
  private void onMouseEnterAi(Event event) {
    // enter
    imgAvatarShaddow.setVisible(true);
  }

  @FXML
  private void onMouseExitAi(Event event) {
    // enter
    imgAvatarShaddow.setVisible(false);
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

    // initialize user chat message object
    ChatMessage userMessage;

    // create a new instance of user chat message object
    userMessage = new ChatMessage("user", userInput);

    setUserResponse(userInput);

    getChatResponse(userMessage, false);
  }

  /** Disable chat components. You can include the things you want to disable here. */
  private void disableChatComponents() {
    tfChat.setEditable(false);

    // Disable all hint buttonss
    HintManager.disableHints();
  }

  /** Enable chat components. You can include the things you want to enable here. */
  private void enableChatComponents() {
    tfChat.setEditable(true);

    // Enable all hint buttonss
    HintManager.enableHints();
  }

  /**
   * Sets the most recent AI message
   *
   * @param message
   */
  protected void setAiMessage(String message) {
    swapLabelsOrder();
    if (GameState.currentAiMessage != "" && GameState.currentPlayerMessage != "") {
      oldestChatProperty.set(GameState.currentAiMessage);
      lblOldestChat.setVisible(true);
    }
    GameState.currentAiMessage = message;
    aiChatProperty.set(GameState.currentAiMessage);
    lblAiChat.setVisible(true);
    lblOldestChat.getStyleClass().remove("chat-bubble1");
    lblOldestChat.getStyleClass().add("chat-bubble");
    if (GameState.isChatting == false) {
      lblAiChat2.setVisible(true);
      chatBubbleVisible = true;
    }
  }

  // Method to swap the order of the labels
  private void swapLabelsOrder() {
    // Get the current index of lblAiChat
    int aiChatIndex = vbChat.getChildren().indexOf(lblAiChat);

    // Remove both labels from the VBox
    vbChat.getChildren().removeAll(lblOldestChat, lblAiChat, lblPlayerChat);

    // Swap the order of the labels
    if (aiChatIndex == 1) {
      vbChat.getChildren().addAll(lblOldestChat, lblPlayerChat, lblAiChat);
    } else {
      vbChat.getChildren().addAll(lblOldestChat, lblAiChat, lblPlayerChat);
    }
  }

  @FXML
  public void onHintClicked(MouseEvent mouseEvent) {
    // If the difficulty is hard, ignore user.
    if (GameState.gameDifficulty == Difficulty.HARD) {
      return;
    }

    // If the number of remaining hints is zero
    if (GameState.hintCounter == 0) {
      return;
    }

    // Update the hint counter
    HintManager.updateHintCounter();

    // Append the request for a hint as a message
    setUserResponse("Hint please!");

    // Disable the hints button
    pgHint.setDisable(true);
  }

  /** Generate a GPT hint response. GPT should give a hint for the current room the user is in. */
  public void getUserHint(Boolean isRoomSolved) {
    // Get the hint based on the current room and whether the room is solved
    String hint = (isRoomSolved ? getNoMoreHints() : getRoomHint());

    // Initialize a user hint message compatible for GPT to analyze
    ChatMessage userHintMessage = new ChatMessage("assistant", hint);

    // Get GPT's response
    getChatResponse(userHintMessage, true);
  }

  private static String getNoMoreHints() {
    return GptPromptEngineering.getNoMoreHints(GameState.currentRoom);
  }

  protected abstract String getRoomHint();

  /**
   * Updates chat GPT's persona when called. This should influence the GPT's response to the user.
   */
  public static void updateChatPersona() {
    // initialize the chat message field
    ChatMessage gptMessage;

    // initialize GPT chat message object
    gptMessage = new ChatMessage("assistant", GptPromptEngineering.updateBackstory());

    // get a response from GPT to setup the chat
    // getChatResponse(gptMessage, false);
  }
}
