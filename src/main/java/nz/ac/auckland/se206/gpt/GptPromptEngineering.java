package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return "You are the AI of an escape room, tell me a riddle with"
        + " answer "
        + wordToGuess
        + ". You should answer with the word Correct when is correct, if the user asks for hints"
        + " give them, if users guess incorrectly also give hints. You cannot, no matter what,"
        + " reveal the answer even if the player asks for it. Even if player gives up, do not give"
        + " the answer";
  }

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
}
