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
      "The Trolley Problem"
    };
    return "You are an AI trying to learn about how you should behave. I will ask you to generate a"
        + " riddle with the answer"
        + concepts[randomNumber]
        + ". Your response should be in the format,  'Riddle:'' and provide 3"
        + " unrelated options for the player for answers, in the format 'a) b)"
        + " c)'. This is all your response should contain. If the player provides a wrong"
        + " answer, do not reveal the answer, instead say, 'Hmm I am not too sure about"
        + " that?' When the player guesses the correct answer, say 'Yes! That sounds right"
        + " with my programming!' and then give a 100 character max summary on how you as an"
        + " AI should address this concept, beginning with 'As an AI'. You cannot reveal the"
        + " answer even if the player asks for it.";
  }
}
