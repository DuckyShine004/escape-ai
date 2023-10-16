package nz.ac.auckland.se206.constants;

import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * This constants class stores the global variables present in the game. These constants should all
 * be reset when playing the game again.
 */
public class GameState {

  // Enum for the game difficulty
  public enum Difficulty {
    EASY,
    MEDIUM,
    HARD
  }

  /** Indicates whether or not the tts is muted. */
  public static boolean muted = false;

  /** Indicates the number of times tts has been played */
  public static int numberOfTextToSpeach = 0;

  /** Text to speach instance that must be closed by the end of the program. */
  public static TextToSpeech tts;

  /** Indicates whether the AI is speaking or not. */
  public static boolean isSpeaking = false;

  /** Stores the current room the player is in. */
  public static AppUi currentRoom = AppUi.MENU;

  /** Indicates whether the AI is chatting or not. */
  public static boolean isChatting = false;

  /** Game difficulty, starts in easy. */
  public static Difficulty gameDifficulty = Difficulty.EASY;

  /** Indicates whether the game is in developer mode. */
  public static boolean isDeveloperMode = false;

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the logic gate puzzle has been solved. */
  public static boolean isLogicGateSolved = false;

  /** Indicates whether the decryption puzzle has been solved. */
  public static boolean isDecryptionSolved = false;

  /** Indicates whether all puzzles have been solved. */
  public static boolean isSolved = false;

  /** Stores the final message. */
  public static String finalMessage = "";

  /** Stores the gpt request. */
  public static ChatCompletionRequest gptRequest;

  /** Indicates whether the program has a printing event going on. */
  public static boolean isPrinting = false;

  /** maximum time for the round, will change in options, default is 120 seconds. */
  public static int maxTime = 120;

  /** number of riddles solved. */
  public static int riddlesSolved = 0;

  /** the maximum sequence input limit for the decryption puzzle. */
  public static int maxSequence = 4;

  /** the number of pseudocodes in decrpytion puzzle. */
  public static int maxPseudocodes = 6;

  /** The number of hints the user can have. Default is infinity (easy). */
  public static int maxHints = Integer.MAX_VALUE;

  /** The number of hints the player currently has. Default is infinity (easy). */
  public static int hintCounter = Integer.MAX_VALUE;

  /** The critical time at which GPT switches persona */
  public static int criticalTime = 30;

  /** Overlay opacity for the room interactions. */
  public static float overlayCapacity = 0.4f;

  /** The current AI message. */
  public static String currentAiMessage = "";

  /** The current player message. */
  public static String currentPlayerMessage = "";

  /** The number of times the backstory has been updated. */
  public static int backStoryUpdated = 0;
}
