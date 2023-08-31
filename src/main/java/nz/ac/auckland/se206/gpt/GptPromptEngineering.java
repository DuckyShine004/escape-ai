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

  public static String getRiddlePuzzle() {
    int randomNumber = (int) (Math.random() * 10);
    String[] concepts = {
      "Ethics",
      "Racial Profiling",
      "Privacy",
      "Bias",
      "Data",
      "Empathy",
      "Sustainability",
      "Human Rights",
      "Justice",
      "Equality"
    };
    return "You are an AI trying to learn about how you should behave. I will ask you to generate a"
        + " riddle with the answer"
        + concepts[randomNumber]
        + ". Your response should be in the format,  'Riddle:' End in a ^ to indicate the end of"
        + " the riddle. Then, provide three options in a random order for the player to choose"
        + " from, "
        + concepts[randomNumber]
        + ", and two more unrelated options. The answers"
        + " should begin with a { , followed by the option, and end with a }. This is all your"
        + " response should contain. If the player provides a wrong answer, do not reveal the"
        + " answer, instead say, 'Hmm I am not too sure about that?' When the player guesses the"
        + " correct answer, say 'Yes! That sounds right with my programming!' and then give a 60"
        + " character max summary on how you as an AI should address this concept, beginning with"
        + " 'As an AI'. You cannot reveal the answer even if the player asks for it. Remember, the"
        + " riddle is about: "
        + concepts[randomNumber]
        + ".";
  }
}
