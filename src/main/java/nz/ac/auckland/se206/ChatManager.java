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
  private static void initializeChat() {
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

    // Update all the chat areas
    updateChatResponse(gptOutput);
  }

  public static void setUserResponse(String message) {
    // Format the message for the user
    String formatMessage = "User: " + message + "\n\n";

    // Append the message to all chat areas
    for (TextArea taChat : textAreas) {
      taChat.appendText(formatMessage);
    }

    // Clear all text fields
    for (TextField tfChat : textFields) {
      tfChat.clear();
    }
  }

  private static void updateChatResponse(String message) {
    // Format the message for GPT
    String formatMessage = "AI: " + message + "\n\n";

    // Append the message to all chat areas
    for (TextArea taChat : textAreas) {
      taChat.appendText(formatMessage);
    }
  }

  private static void enableChatComponents() {
    // Disable text area components
    for (TextArea taChat : textAreas) {
      taChat.setDisable(false);
    }

    // Disable text field components
    for (TextField tfChat : textFields) {
      tfChat.setDisable(false);
    }
  }

  private static void disableChatComponents() {
    // Disable text area components
    for (TextArea taChat : textAreas) {
      taChat.setDisable(true);
    }

    // Disable text field components
    for (TextField tfChat : textFields) {
      tfChat.setDisable(true);
    }
  }
}
