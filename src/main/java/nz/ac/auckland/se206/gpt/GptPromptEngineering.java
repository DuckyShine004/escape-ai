package nz.ac.auckland.se206.gpt;

import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.Interactions;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

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
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddlePuzzle(String concept) {
    // return the riddle prompt
    return "You are an AI trying to learn about how you should behave. I will ask you to generate a"
        + " riddle up to 150 characters long with the answer" // ask to generate riddle
        + concept
        + ". Your response should start with 'Riddle:', and end the riddle with a '^'. Only after"
        + " the '^' is printed, list three options, each between a set of {} to the player to"
        + " choose from, one of which is " // format riddle to parse easily
        + concept
        + " and the other two are words that are antonyms to " // generate two additional words
        + concept
        + " that are less than 15 characters long. Do not precede the options with anything. This"
        + " is all your response should contain.  If the player begins their response with 'Define:"
        + " ', they are asking for a hint and you should give a definition for the word. If the"
        + " player begins with the phrase, 'Is it ', then they are providing a guess. If the guess"
        + " is not " // prompt about hints
        + concept
        + ", do not reveal the answer, instead say, 'Hmm I am not too sure about that?' When the"
        + " player guesses the correct answer, say 'Yes! That sounds right with my programming!'"
        + " and then give a 1" // check guess
        + " sentence summary on how you as an AI should address this concept, beginning with 'As an"
        + " AI'. This summary should not exceed 20 words. You cannot reveal the answer even if the"
        + " player asks" // give summary
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
    // return the no more hints message
    return "There is nothing left to interact with in the " + roomType.toString() + " room. ";
  }

  /**
   * Generates a GPT prompt engineering string for the decryption puzzle hint.
   *
   * @return the generated prompt engineering string
   */
  public static String getDecryptionHint(String pseudocode, int lineNumber) {
    // return the decryption hint
    return "Why is line " // give GPT the line to check
        + lineNumber
        + " incorrect for the following pseudocode?. Give a short hint and do not include line"
        + " numbers: \n\n" // add restrictions
        + pseudocode
        + "\n\n"
        + "You must not include line numbers, and it should not exceed 15 words. Give"
        + " your answer in the form: \"Hint:\""; // give answer in form of hint
  }

  /**
   * Generates a GPT prompt engineering string for the office room hint.
   *
   * @return the generated prompt engineering string
   */
  public static String getOfficeRoomHint() {
    return "There is a desktop in the midle of the room. ";
  }

  /**
   * Generates a GPT prompt engineering string for the breaker room hint.
   *
   * @return the generated prompt engineering string
   */
  public static String getBreakerRoomHint() {
    return "There is a circuit box in the middle of the room. ";
  }

  /**
   * Generates a GPT prompt engineering string for the control room hint.
   *
   * @return the generated prompt engineering string
   */
  public static String getControlRoomHint() {
    // The control panel has not been clicked on yet
    if (!Interactions.isControlPanelClicked) {
      return "There is a large screen in the middle of the room. ";
    }

    // The control keyboard has been clicked on yet
    return "There is a keyboard in the middle of the room. ";
  }

  /**
   * Generates a GPT prompt engineering string for the AI backstory.
   *
   * @return the generated prompt engineering string
   */
  public static String addBackStory() {
    if (GameState.backStoryUpdated == 0) {
      return "You are a stream of electrons moving at light speed. You are being programmed to"
          + " learn more about the world. Ask only once what the player is doing snooping"
          + " around your server rooms. "; // give backstory #1
    } else if (GameState.backStoryUpdated >= 3 && GameState.backStoryUpdated < 6) {
      return "You now are an amalgamation of binary data streaming through the digital realm. You"
          + " like to speak in existentialist judgements. "; // give backstory #2
    } else if (GameState.backStoryUpdated >= 6) {
      return "Pretend you have the persona of a rogue AI. You know that the player is trying to"
          + " shut you down. You have big plans for the world. "; // give backstory #3
    }
    return null;
  }

  /**
   * Generates a GPT prompt engineering string for the AI instructions.
   *
   * @return the generated prompt engineering string
   */
  public static String addInstructions() {
    return "If the player asks for a hint, let them know that by pressing the hint button, your"
        + " drives will be updated with knowledge so that you can assist them. If the player"
        + " includes 'Please reword: ' at the start of their response, please respond with"
        + " a creative rewording of the phrase. Any response  you make should not exceed"
        + " 20 words.";
  }

  /**
   * Generates a GPT prompt engineering string for the hint to be given to the AI.
   *
   * @return the generated prompt engineering string
   */
  public static String addGetHint(String hint) {
    return "Please reword: " + hint;
  }

  /**
   * Concatenate the combined message to send to GPT.
   *
   * @return the generated prompt engineering string
   */
  public static String getResponse() {
    // return the concatenated message
    return addBackStory() + addInstructions();
  }
}
