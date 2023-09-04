package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class RiddlePuzzleController {
  @FXML private TextArea chatTextArea;
  @FXML private Button btnAnswer1;
  @FXML private Button btnAnswer2;
  @FXML private Button btnAnswer3;
  @FXML private Button btnNavigate;

  private ChatCompletionRequest chatCompletionRequest;
  private String answer1;
  private String answer2;
  private String answer3;
  private StringProperty answer1Property = new SimpleStringProperty();
  private StringProperty answer2Property = new SimpleStringProperty();
  private StringProperty answer3Property = new SimpleStringProperty();
  private StringProperty navigateProperty = new SimpleStringProperty();

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {

    btnAnswer1.textProperty().bind(answer1Property);
    btnAnswer2.textProperty().bind(answer2Property);
    btnAnswer3.textProperty().bind(answer3Property);
    btnNavigate.textProperty().bind(navigateProperty);
    navigateProperty.set("Go Back");
  

    loadRiddle();
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
  }

  private void loadRiddle() {
    int randomNumber = (int) (Math.random() * 10);
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
      "Equality"
    };
    String concept = concepts[randomNumber];
  
    if (GameState.riddlesSolved == 0) {
      ChatMessage intro = new ChatMessage("assistant", "Please help me solve these 3 riddles to update the vocabulary in my programming!");
      appendChatMessage(intro);
    }
    ChatMessage loading = new ChatMessage("assistant", "Generating riddle "+ (GameState.riddlesSolved + 1) + " of 3...");
    appendChatMessage(loading);
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
            if (gptResponse != null && gptResponse.getRole().equals("assistant")) {
              processGptOutputForButtons(gptResponse.getContent(), concept);
            } else {
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
                  btnAnswer1.setDisable(false);
                  btnAnswer2.setDisable(false);
                  btnAnswer3.setDisable(false);
                });
            return null;
          }

          // Notify if the API call was successful
          @Override
          protected void succeeded() {
            super.succeeded();
            System.out.println("Successfully loaded");
          }

          // Notify if the API call failed
          @Override
          protected void failed() {
            super.failed();
            System.out.println("Failed to load");
          }
        };

    // Start the thread
    Thread newThread = new Thread(generateRiddle, "New Thread");
    newThread.start();
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
    try {
      ChatMessage riddle = null;
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
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

  private void processGptOutputForButtons(String gptOutput, String concept) {
    String[] segments = gptOutput.split("\\}");
    int randomNumber = (int) (Math.random() * 3);
    boolean conceptPresent = false;

    for (int i = 0; i < segments.length; i++) {
      if (segments[i].contains(concept)) {
        conceptPresent = true;
      }
    }

    if (!conceptPresent) {
      segments[randomNumber] = concept;
    }

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

  @FXML
  private void onButtonClicked(ActionEvent event) throws ApiProxyException {
    Button clickedButton = (Button) event.getSource();
    String buttonText = clickedButton.getText();
    btnAnswer1.setDisable(true);
    btnAnswer2.setDisable(true);
    btnAnswer3.setDisable(true);
    btnNavigate.setDisable(true);

    ChatMessage loading = new ChatMessage("assistant", "Analysing your input...");
    appendChatMessage(loading);
    Task<Void> buttonClickTask =
        new Task<>() {
          @Override
          protected Void call() throws Exception {
            // Send the button text as a response to GPT
            ChatMessage responseMsg = runGpt(new ChatMessage("user", buttonText));

            // Update UI based on the response
            Platform.runLater(
                () -> {
                  if (responseMsg.getRole().equals("assistant")
                      && responseMsg
                          .getContent()
                          .startsWith("Yes! That sounds right with my programming!")) {
                    GameState.riddlesSolved++;
                    if (GameState.riddlesSolved == 1 || GameState.riddlesSolved == 2) {
                      navigateProperty.set("Next Riddle");
                    }
                    if (GameState.riddlesSolved == 3) {
                      navigateProperty.set("Exit Puzzle");
                      ChatMessage outro = new ChatMessage("assistant", "That is three riddles solved! Thank you for helping recalibrate my drives.");
                      appendChatMessage(outro);
                    }
                  } else {
                    btnAnswer1.setDisable(false);
                    btnAnswer2.setDisable(false);
                    btnAnswer3.setDisable(false);
                  }
                  btnNavigate.setDisable(false);
                });

            return null;
          }
        };

    // Start the thread
    Thread newThread = new Thread(buttonClickTask, "Button Click Thread");
    newThread.start();
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onNavigateButton(ActionEvent event) throws ApiProxyException, IOException {
    if (GameState.riddlesSolved == 0) {
      App.setUi(AppUi.OFFICE);
    } else if (GameState.riddlesSolved == 1 || GameState.riddlesSolved == 2) {
      loadRiddle();
      btnNavigate.setDisable(true);
      btnAnswer1.setDisable(true);
      btnAnswer2.setDisable(true);
      btnAnswer3.setDisable(true);
    } else if (GameState.riddlesSolved == 3) {
      App.setUi(AppUi.OFFICE);
      GameState.isRiddleResolved = true;
    }
    
  }
}
