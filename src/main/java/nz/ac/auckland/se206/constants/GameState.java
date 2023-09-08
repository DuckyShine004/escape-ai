package nz.ac.auckland.se206.constants;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  /** maximum time for the round, will change in options, default is 60 seconds */
  public static int maxTime = 60; // if think of better variable, please mention in PR review :)

  /** number of riddles solved */
  public static int riddlesSolved = 0;

  /** the maximum sequence input limit for the decryption puzzle */
  public static int maxSequence = 10;
}
