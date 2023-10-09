package nz.ac.auckland.se206;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;

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
      tfChat.setEditable(true);
    }

    // Enable all hint buttons
    HintManager.enableHints();
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
      tfChat.setEditable(false);
    }

    // Disable all hint buttonss
    HintManager.disableHints();
  }

  /** Clear all current text areas. */
  public static void clearTextAreas() {
    // Clear all text areas
    /* for (TextArea taChat : textAreas) {
      taChat.clear();
    } */
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
