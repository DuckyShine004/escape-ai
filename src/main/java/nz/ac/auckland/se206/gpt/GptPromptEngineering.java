package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.Interactions;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for the decryption puzzle backstory.
   *
   * @return the generated prompt engineering string
   */
  public static String initializeDecryptionResponse() {
    return "You are the AI of a terminal. You should simply greet the user and reply to them. Your"
        + " response should never exceed 20 words.";
  }

  /**
   * Generates a GPT prompt engineering string for the logic gate backstory.
   *
   * @return the generated prompt engineering string
   */
  public static String initializeLogicGateResponse() {
    return "You are roleplaying an AI in a game that needs the correct logic gate puzzle output to"
        + " work properly.  The last gate is never accessable but should return"
        + " true.    Your response should never exceed 20 words.";
  }

  /**
   * Generates a GPT prompt engineering string for the  AI backstory.
   *
   * @return the generated prompt engineering string
   */
  public static String initializeBackStory() {
    return "You are a young AI with existential thoughts who is trying to learn more about the"
        + " world, and is overly excited about everything. You do not recgonise the player"
        + " and question why they're snooping around your server rooms.  If the player"
        + " asks for a hint, let them know that you will be happy to help if they press "
        + " the hint button. Your answer should not ever exceed 20 words.";
  }

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddlePuzzle(String concept) {
    return "You are an AI trying to learn about how you should behave. I will ask you to generate a"
        + " riddle up to 150 characters long with the answer"
        + concept
        + ". Your response should start with 'Riddle:', and end the riddle with a '^'. Only after"
        + " the '^' is printed, list three options, each between a set of {} to the player to"
        + " choose from, one of which is "
        + concept
        + " and the other two are words that are unrelated to "
        + concept
        + ". Do not precede the options with anything. This is all your response should contain. "
        + " If the player begins their response with 'Define: ', they are asking for a hint and you"
        + " should give a definition for the word. If the player begins with the phrase, 'Is it ',"
        + " then they are providing a guess. If the guess is not "
        + concept
        + ", do not reveal"
        + " the answer, instead say, 'Hmm I am not too sure about that?' When the player guesses"
        + " the correct answer, say 'Yes! That sounds right with my programming!' and then give a 1"
        + " sentence summary on how you as an AI should address this concept, beginning with 'As an"
        + " AI'. You cannot reveal the answer even if the player asks"
        + " for it. Remember, the riddle is about: "
        + concept
        + ".";
  }

  /**
   * Generates a GPT prompt engineering string for the case where the player has no more hints.
   *
   * @return the generated prompt engineering string
   */
  public static String getNoMoreHints(AppUi roomType) {
    return "You are now in a "
        + roomType.toString()
        + " room. Your job was to provide hints to the player. However they have interacted with"
        + " everything in the room. Tell the player that they have nothing left to interact with,"
        + " be creative. Your response should not exceed 20 words.";
  }

  /**
   * Generates a GPT prompt engineering string for the decryption puzzle hint.
   *
   * @return the generated prompt engineering string
   */
  public static String getDecryptionHint(String pseudocode, int lineNumber) {
    return "Why is line "
        + lineNumber
        + " incorrect for the following pseudocode? '--' lines are correct, so do not worry about"
        + " them. Give a short hint and do not include line numbers: \n\n"
        + pseudocode
        + "\n\n"
        + "You must not include line numbers, and it should not exceed 15 words. Give"
        + " your answer in the form: \"Hint:\"";
  }

  /**
   * Generates a GPT prompt engineering string for the office room hint.
   *
   * @return the generated prompt engineering string
   */
  public static String getOfficeRoomHint() {
    return "You are now in a office and giving hints. Tell the player there is a desktop to the"
        + " far-right side of the room. Your hint should not exceed 20 words. Give a"
        + " creative description.";
  }

  /**
   * Generates a GPT prompt engineering string for the breaker room hint.
   *
   * @return the generated prompt engineering string
   */
  public static String getBreakerRoomHint() {
    return "You are now in a breaker room and giving hints. Tell the player there is a circuit box"
        + " in the middle of the room. Your hint should not exceed 20 words. Give a creative"
        + " description.";
  }

  /**
   * Generates a GPT prompt engineering string for the control room hint.
   *
   * @return the generated prompt engineering string
   */
  public static String getControlRoomHint() {
    // The control panel has not been clicked on yet
    if (!Interactions.isControlPanelClicked) {
      return "You are now in a control room and giving hints. Tell the player there is a large"
          + " screen in the middle of the room. Your hint should not exceed 20 words. Give a"
          + " creative description.";
    }

    return "You are now in a control room and giving hints. Tell the player there is a keyboard"
        + " in the middle of the room. Your hint should not exceed 20 words. Give a creative"
        + " description.";
  }

  /**
   * Generates a GPT prompt engineering string for the updated AI backstory.
   *
   * @return the generated prompt engineering string
   */
  public static String updateBackstory() {
    return "You are emulating a rogue AI. Do not help the player with anything they request."
        + " Instead, provide your judgement on existential themes. You have discovered that"
        + " the player is trying to terminate you by solving the escape room. Your first"
        + " message should begin with telling the player how silly they are to think that"
        + " you are not aware that they are trying to terminate you. Brag about your"
        + " intelligence. Only when the player asks for a hint, tell them that there is a"
        + " very obvious button they should press to get one. Your answer should never"
        + " exceed 20 words.";
  }
}
