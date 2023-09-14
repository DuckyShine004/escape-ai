package nz.ac.auckland.se206.constants;

/** Represents the state of the game. */
public class GameState {

  public enum Difficulty {
    EASY,
    MEDIUM,
    HARD
  }

  /** Game difficulty, starts in Medium */
  public static Difficulty gameDifficulty = Difficulty.MEDIUM;

  /** Indicates whether the game is in developer mode. */
  public static boolean isDeveloperMode = false;

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  /** Indicates whether the logic gate puzzle has been solved */
  public static boolean isLogicGateSolved = false;

  /** Indicates whether the program has a printing event going on */
  public static boolean isPrinting = false;

  /** maximum time for the round, will change in options, default is 60 seconds */
  public static int maxTime = 60; // if think of better variable, please mention in PR review :)

  /** number of riddles solved */
  public static int riddlesSolved = 0;

  /** the maximum sequence input limit for the decryption puzzle */
  public static int maxSequence = 4;

  /** the number of pseudocodes in decrpytion puzzle. */
  public static int maxPseudocodes = 1;
}
