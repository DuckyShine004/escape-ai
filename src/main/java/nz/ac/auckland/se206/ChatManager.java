package nz.ac.auckland.se206;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** The chat manager class helps sync all chat messages across all scenes. */
public class ChatManager {
  private static List<TextArea> textAreas;
  private static List<TextField> textFields;

  private static List<ImageView> imgAiFigures;
  private static Image AiFigure;
  private static Image mutedAiFigure;

  private static List<ImageView> imgEmotions;
  private static Image questioningImage;

  private static ChatCompletionRequest gptRequest;

  /** Initialize the chat manager. */
  public static void initialize() {
    // Initialize fields
    textAreas = new ArrayList<TextArea>();
    textFields = new ArrayList<TextField>();

    imgAiFigures = new ArrayList<ImageView>();
    imgEmotions = new ArrayList<ImageView>();

    initializeImages();
  }

  /** loads ImageView and Images into class */
  private static void initializeImages() {
    //
    try {
      AiFigure =
          new Image(new FileInputStream("src/main/resources/images/" + "avataroutline" + ".png"));
      mutedAiFigure =
          new Image(new FileInputStream("src/main/resources/images/" + "mutedavatar" + ".png"));
      questioningImage =
          new Image(new FileInputStream("src/main/resources/images/" + "questionmark" + ".png"));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** add instances of ImageView into list */
  public static void addAiInstance(ImageView AiFigure, ImageView AiEmotion) {
    imgAiFigures.add(AiFigure);
    imgEmotions.add(AiEmotion);
  }

  /** toggles image of scene AIs */
  public static void toggleAiMuted() {
    //

    for (ImageView imageView : imgAiFigures) {
      if (GameState.muted) {

        // change to muted figure
        imageView.setImage(mutedAiFigure);
      } else {
        // change to un-muted figure
        imageView.setImage(AiFigure);
      }
    }
  }

  /**
   * Initialize GPT. Set the tokens and create a new instance of GPT request.
   *
   * <p>Note: I would love to be able to name this method 'initializeGPT'. Unfortunately, we are not
   * allowed to have acronyms as method names as per the naming convention.
   */
  public static void initializeChat() {
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
   * Adds chat components to be updated globally when something occurs and chat window needs to be
   * updated.
   *
   * @param textArea the text area to be added to list of text areas.
   * @param textField the text field to be added to list of text fields.
   */
  public static void addChatComponents(TextArea textArea, TextField textField) {
    // Add the text area to the list of text areas
    textAreas.add(textArea);

    // Add the text field to the list of text fields
    textFields.add(textField);
  }

  /**
   * Generate a response from GPT.
   *
   * @param entityMessage the chat message to be sent to GPT.
   */
  public static void getChatResponse(ChatMessage entityMessage, boolean isHint) {
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

          // Remove the previous message if it was a hint
          if (isHint) {
            removePreviousMessage();
          }
        });

    // If the task fails
    gptTask.setOnFailed(
        event -> {
          enableChatComponents();
          stopThinking();
          System.out.println("FAILED TO GENERATE RESPONSE");

          // Remove the previous message if it was a hint
          if (isHint) {
            removePreviousMessage();
          }
        });

    // Create a thread to handle GPT concurrency
    Thread gptThread = new Thread(gptTask);

    // Start the thread
    gptThread.start();
  }

  /** Generate a GPT hint response. GPT should give a hint for the current room the user is in. */
  public static void getUserHint(Boolean isRoomSolved) {
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

  /**
   * Using the Java reflection API, we can get the hint prompt for GPT. The prompt should be related
   * to the current user's room.
   *
   * @return the return value from the invoked method.
   */
  private static String getRoomHint() {
    // Initialize method field
    Method roomHintMethod = null;

    // Initialize a room hint field
    String roomHint = "";

    // Get the method name for the current room
    String roomMethodName = getRoomMethodName();

    // Get the method given the name of the method and handle the exception (if any)
    try {
      roomHintMethod = ChatManager.class.getDeclaredMethod(roomMethodName);
    } catch (Exception exception) {
      exception.printStackTrace();
    }

    // Get the hint, this should be a prompt
    try {
      roomHint = (String) roomHintMethod.invoke(null);
    } catch (Exception exception) {
      exception.printStackTrace();
    }

    return roomHint;
  }

  /**
   * Return a string value for the method name according to the room the user is in. This is useful
   * for determining which prompt to give to GPT.
   *
   * @return the method name according to the current room.
   */
  private static String getRoomMethodName() {
    // Initialize the prefix of the method name
    String prefix = "get";

    // Initialize the suffix of the method name
    String suffix = "RoomHint";

    // Get the string for the current room
    String roomName = GameState.currentRoom.toString();

    // Format the room name correctly
    roomName = roomName.substring(0, 1) + roomName.substring(1).toLowerCase();

    return prefix + roomName + suffix;
  }

  /**
   * Get the GPT prompt for the office room. This method should also handle valid and invalid cases.
   *
   * @return a GPT prompt for the office room.
   */
  protected static String getOfficeRoomHint() {
    return GptPromptEngineering.getOfficeRoomHint();
  }

  /**
   * Get the GPT prompt for the breaker room. This method should also handle valid and invalid
   * cases.
   *
   * @return a GPT prompt for the breaker room.
   */
  protected static String getBreakerRoomHint() {
    return GptPromptEngineering.getBreakerRoomHint();
  }

  /**
   * Get the GPT prompt for the control room. This method should also handle valid and invalid
   * cases.
   *
   * @return a GPT prompt for the control room.
   */
  protected static String getControlRoomHint() {
    return GptPromptEngineering.getControlRoomHint();
  }

  /**
   * Set the chat response from GPT. This includes printing the response to the text area.
   *
   * @throws Exception thrown when we fail to retrieve a response from GPT.
   */
  private static void setChatResponse() throws Exception {
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

    // Update all the chat areas
    updateChatResponse(gptOutput);
  }

  /**
   * Update the chat area given the response was from the user.
   *
   * @param message the message to be appended to the text area.
   */
  public static void setUserResponse(String message) {
    // Format the message for the user
    String formatMessage = "User: " + message + "\n\n";

    // Append the message to all chat areas
    for (TextArea taChat : textAreas) {
      taChat.appendText(formatMessage);
    }

    // Clear all text fields
    clearTextFields();
  }

  /**
   * Updates chat GPT's persona when called. This should influence the GPT's response to the user.
   */
  public static void updateChatPersona() {
    // initialize the chat message field
    ChatMessage gptMessage;

    // initialize GPT chat message object
    gptMessage = new ChatMessage("assistant", GptPromptEngineering.updateBackstory());

    // get a response from GPT to setup the chat
    getChatResponse(gptMessage, false);
  }

  /**
   * Update the chat area given the response was from chat GPT.
   *
   * @param message the message to be appended to the text area.
   */
  public static void updateChatResponse(String message) {
    // Format the message for GPT
    String formatMessage = "AI: " + message + "\n\n";

    // Append the message to all chat areas
    for (TextArea taChat : textAreas) {
      taChat.appendText(formatMessage);
    }

    // Make text-to-speech voiceover the current message
    GameState.tts.speak(message, AppUi.OFFICE);
  }

  /**
   * Enable chat components. This should re-enable chat components if its previous state was
   * disabled.
   */
  private static void enableChatComponents() {
    // Enable text field components
    for (TextField tfChat : textFields) {
      tfChat.setDisable(false);
    }

    // Enable all hint buttons
    HintManager.enableHintButtons();
  }

  /** set a question mark and thinking indicator */
  private static void startThinking() {
    //
    for (ImageView imageView : imgEmotions) {
      imageView.setImage(questioningImage);
      imageView.setVisible(true);
    }

    for (TextField tfChat : textFields) {
      tfChat.setText("Hmmm. Let me think about that");
    }
  }

  /** remove question mark and thinking indicator */
  private static void stopThinking() {
    //
    for (ImageView imageView : imgEmotions) {
      imageView.setVisible(false);
    }

    for (TextField tfChat : textFields) {
      tfChat.setText("");
    }
  }

  /** Disable chat components. You can include the things you want to disable here. */
  private static void disableChatComponents() {
    // Disable text field components
    for (TextField tfChat : textFields) {
      tfChat.setDisable(true);
    }

    // Disable all hint buttonss
    HintManager.disableHintButtons();
  }

  /** Clear all current text areas. */
  public static void clearTextAreas() {
    // Clear all text areas
    for (TextArea taChat : textAreas) {
      taChat.clear();
    }
  }

  /** Clear all current text fields. */
  public static void clearTextFields() {
    // Clear all text fields
    for (TextField tfChat : textFields) {
      tfChat.clear();
    }
  }

  /** Clear the current chat history */
  public static void clearChatHistory() {

    if (gptRequest == null) {
      return;
    }

    // Get the current GPT messages
    List<ChatMessage> gptMessages = gptRequest.getMessages();

    // Remove last message sent
    gptMessages.clear();
  }

  /** Reset the chat manager on reset. */
  public static void reset() {
    // Clear the text areas
    clearTextAreas();

    // Clear the text fields
    clearTextFields();

    // Clear all the chat history
    clearChatHistory();

    // Initialize the initial message again
    ChatManager.initializeChat();
  }

  /**
   * Remove the previous message in GPT's history. This is to ensure that the users won't be able to
   * abuse the hint system.
   */
  private static void removePreviousMessage() {
    // Get the current GPT messages
    List<ChatMessage> gptMessages = gptRequest.getMessages();

    // Remove last message sent
    gptMessages.remove(gptMessages.size() - 1);
  }
}
