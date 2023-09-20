package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** The chat manager class helps sync all chat messages across all scenes */
public class ChatManager {
  private static List<TextArea> textAreas;
  private static List<TextField> textFields;

  private static ChatCompletionRequest gptRequest;

  public static void initialize() {
    textAreas = new ArrayList<TextArea>();
    textFields = new ArrayList<TextField>();

    initializeChat();
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
          disableChatComponents();
        });

    // If the task succeeds, then enable components
    gptTask.setOnSucceeded(
        event -> {
          enableChatComponents();
        });

    // If the task fails
    gptTask.setOnFailed(
        event -> {
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
  private static void updateChatResponse(String message) {
    // Format the message for GPT
    String formatMessage = "AI: " + message + "\n\n";

    // Append the message to all chat areas
    for (TextArea taChat : textAreas) {
      taChat.appendText(formatMessage);
    }
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
  }

  /** Disable chat components. You can include the things you want to disable here. */
  private static void disableChatComponents() {
    // Disable text field components
    for (TextField tfChat : textFields) {
      tfChat.setDisable(true);
    }
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
}
