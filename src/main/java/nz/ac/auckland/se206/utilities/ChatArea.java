package nz.ac.auckland.se206.utilities;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.ChatManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * The chat area class is a custom component that contains a text area, a text field, and a send
 * button
 */
public class ChatArea extends VBox {
  private TextArea taChat;
  private TextField tfInput;
  private Button btnSend;
  private ChatCompletionRequest gptRequest;
  private TextToSpeech tts;

  /** Constructor for the chat area */
  public ChatArea() {
    super();

    this.tts = GameState.tts;

    gptRequest = GameState.gptRequest;

    taChat = new TextArea();
    tfInput = new TextField();
    btnSend = new Button("Send");

    // Set properties for components
    taChat.setEditable(false);
    taChat.setWrapText(true);

    // Set properties for layout
    HBox inputBox = new HBox(tfInput, btnSend);
    HBox.setHgrow(tfInput, Priority.ALWAYS);
    btnSend.setMaxWidth(Double.MAX_VALUE);

    getChildren().addAll(taChat, inputBox);

    setVgrow(taChat, Priority.ALWAYS);

    // Styling
    setSpacing(10);
    setAlignment(Pos.CENTER);

    // Event handlers
    btnSend.setOnAction(e -> sendMessage());
    tfInput.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            sendMessage();
          }
        });
  }

  /** Set the chat content */
  public void setChatContent(String content) {
    taChat.setText(content);
  }

  /**
   * Get the chat content
   *
   * @return chat content
   */
  public String getChatContent() {
    return taChat.getText();
  }

  /**
   * Get the text area
   *
   * @return text area
   */
  public TextArea getTextArea() {
    return taChat;
  }

  /**
   * Get the input field
   *
   * @return input field
   */
  public TextField getInputField() {
    return tfInput;
  }

  /**
   * Get the send button
   *
   * @return send button
   */
  public Button getSendButton() {
    return btnSend;
  }

  /** Append the message to the chat area and clear the input field */
  private void sendMessage() {
    // Get the message from the input field
    String message = tfInput.getText().trim();

    // Append the message to the chat area and clear the input field
    if (!message.isEmpty()) {
      ChatMessage inputMessage;
      inputMessage = new ChatMessage("user", message);

      // appent input to text area
      taChat.appendText("User: " + message + "\n\n");

      // Update the chat content in the chat manager
      String currentChatContent = ChatManager.getChatContent();
      currentChatContent += "User: " + message + "\n\n";
      ChatManager.setChatContent(currentChatContent);

      // get the gpt response
      getChatResponse(inputMessage);
      tfInput.clear();
    }
  }

  public void initiailizeChat() {
    // initialize the chat message field
    ChatMessage gptMessage;

    // initialize GPT chat message object
    gptMessage = new ChatMessage("assistant", GptPromptEngineering.initializeBackStory());

    // initialize GPT request object
    gptRequest = new ChatCompletionRequest();

    // set the GPT request object in the game state
    GameState.gptRequest = gptRequest;

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

  private void getChatResponse(ChatMessage gptMessage) {
    // add user input to GPT's user input history
    gptRequest.addMessage(gptMessage);

    // disable input
    tfInput.setDisable(true);

    // clear input
    tfInput.setText("");

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

    // set text field to enabled on failed
    gptTask.setOnFailed(
        event -> {
          tfInput.setDisable(false);
          System.out.println("Failed to get response from GPT");
        });

    // set text field to enabled on succeeded
    gptTask.setOnSucceeded(
        event -> {
          tfInput.setDisable(false);
        });

    // start the thread
    gptThread.start();
    System.out.println("Thread started");
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

    // Update the chat content in the chat manager
    String currentChatContent = ChatManager.getChatContent();
    currentChatContent += "AI: " + gptOutput + "\n\n";
    ChatManager.setChatContent(currentChatContent);

    // add GPT's response to its history
    gptRequest.addMessage(gptMessage);

    // append the result to the text area
    taChat.appendText("AI: " + gptOutput + "\n\n");
    tts.speak(gptOutput, AppUi.OFFICE);
  }
}
