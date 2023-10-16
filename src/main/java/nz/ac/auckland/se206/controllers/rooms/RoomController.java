package nz.ac.auckland.se206.controllers.rooms;

import java.io.FileInputStream;
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
import javafx.scene.image.Image;
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
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.GameState.Difficulty;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/**
 * The abstract controller for a generic room, which implements all the methods shared between the
 * breaker room, control room, and office room.
 */
public abstract class RoomController {
  private static StringProperty aiChatProperty = new SimpleStringProperty();
  private static StringProperty playerChatProperty = new SimpleStringProperty();
  private static StringProperty oldestChatProperty = new SimpleStringProperty();
  private static ChatCompletionRequest gptRequest;

  protected static boolean isThinking = false;
  protected static String eyes = "Nasser";
  protected static boolean chatBubbleVisible = true;

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

  @FXML private Button btnToggleTtsMute;

  @FXML private Image mutedImage;
  @FXML private Image talkingImage;

  /** This method initialiszes the room controller using a default method. */
  @FXML
  protected void initialize() {
    lblAiChat.textProperty().bind(aiChatProperty);
    aiChatProperty.set(GameState.currentAiMessage);
    lblPlayerChat.textProperty().bind(playerChatProperty);
    playerChatProperty.set(GameState.currentPlayerMessage);
    lblOldestChat.textProperty().bind(oldestChatProperty);
    lblAiChat2.textProperty().bind(aiChatProperty);

    try {
      // load glass image
      this.mutedImage = new Image(new FileInputStream("src/main/resources/images/mutedavatar.png"));
      this.talkingImage =
          new Image(new FileInputStream("src/main/resources/images/avataroutline.png"));

    } catch (Exception e) {
      e.printStackTrace();
    }

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
   * This method toggles the tts mute state.
   *
   * @param event
   */
  @FXML
  private void onToggleTtsMute(Event event) {
    // toggle mute
    GameState.muted = !GameState.muted;

    // mute current message
    GameState.tts.stop();

    updateAvatarImage();

    System.out.println("Muted: " + GameState.muted);
  }

  /** This method will update the image of the avatar based on whether the AI is muted or not. */
  private void updateAvatarImage() {
    // update avatar image
    if (GameState.muted) {
      imgAvatar.setImage(this.mutedImage);
    } else {
      imgAvatar.setImage(this.talkingImage);
    }
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
    gptMessage = new ChatMessage("assistant", GptPromptEngineering.getResponse());

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
          GameState.backStoryUpdated++;
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

  /**
   * When the AI icon is clicked, this method is called. This method toggles the chat components.
   *
   * @param event the mouse event
   */
  @FXML
  protected void onAiClicked(MouseEvent event) {

    // Toggle the chatting state of the game
    GameState.isChatting = !GameState.isChatting;

    Boolean isVisible = btnToggleTtsMute.isVisible();
    this.btnToggleTtsMute.setVisible(!isVisible);

    // If the game state is chatting, then set the chat components to visible
    if (GameState.isChatting) {
      setChatComponentsVisible();
    } else {
      setChatComponentsNotVisible();
    }
  }

  /** When the game state is chatting, set visibility of chat components to true. */
  private void setChatComponentsVisible() {
    // If the chat bubble is not visible, then set it to visible
    vbChat.setVisible(true);
    tfChat.setVisible(true);
    recOpaque.setVisible(true);
    lblAiChat2.setVisible(false);
    chatBubbleVisible = false;

    // If the AI chat is empty, then set the oldest chat to not visible
    if (GameState.currentAiMessage == "") {
      lblAiChat.setVisible(false);
      lblOldestChat.setVisible(false);
    }

    // If the player chat is empty, then set the oldest chat to not visible
    if (GameState.currentPlayerMessage == "") {
      lblPlayerChat.setVisible(false);
      lblOldestChat.setVisible(false);
    }
  }

  /** When the game state is not chatting, set visibility of chat components to false. */
  private void setChatComponentsNotVisible() {
    vbChat.setVisible(false);
    tfChat.setVisible(false);
    recOpaque.setVisible(false);
    lblAiChat2.setVisible(true);
    chatBubbleVisible = true;
  }

  /** starting thinking and set the thinking components to visible */
  public void startThinking() {
    // Set the chat components to visible
    tfChat.setDisable(true);
    tfChat.setOpacity(0.5);

    // Set the eye components to visible
    lblEye1.setVisible(true);
    lblEye2.setVisible(true);
    crcEye1.setVisible(true);
    crcEye2.setVisible(true);
    isThinking = true;
  }

  /** stop thinking and set the thinking components to not visible */
  public void stopThinking() {
    // Set the chat components to not visible
    tfChat.setDisable(false);
    tfChat.setOpacity(1);

    // Set the eye components to not visible
    lblEye2.setVisible(false);
    lblEye1.setVisible(false);
    crcEye1.setVisible(false);
    crcEye2.setVisible(false);
    isThinking = false;
  }

  /**
   * When the mouse enters the AI icon, the shadow is visible.
   *
   * @param event
   */
  @FXML
  private void onMouseEnterAi(Event event) {
    // Set AI shadow visible
    imgAvatarShaddow.setVisible(true);
  }

  /**
   * When the mouse exits the AI icon, the shadow is not visible.
   *
   * @param event
   */
  @FXML
  private void onMouseExitAi(Event event) {
    // set AI shadow not visible
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
    if (GameState.backStoryUpdated % 3 == 0 && GameState.backStoryUpdated != 0) {
      userMessage =
          new ChatMessage(
              "user", GptPromptEngineering.getResponse() + "The player says: " + userInput);
    } else {
      userMessage = new ChatMessage("user", userInput);
    }
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

    GameState.tts.speak(message, AppUi.OFFICE);
  }

  /** Swap the order of the labels in the chat area. */
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

  /**
   * This method handles the click event on the hint button.
   *
   * @param mouseEvent the mouse event
   */
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
    ChatMessage userHintMessage =
        new ChatMessage("assistant", GptPromptEngineering.addGetHint(hint));

    // Get GPT's response
    getChatResponse(userHintMessage, true);
  }

  /**
   * Generates a GPT prompt engineering string for the case where the player has no more hints.
   *
   * @return the generated prompt engineering string
   */
  private static String getNoMoreHints() {
    return GptPromptEngineering.getNoMoreHints(GameState.currentRoom);
  }

  /** An abstract method that returns the room hint for the current room. */
  protected abstract String getRoomHint();
}
