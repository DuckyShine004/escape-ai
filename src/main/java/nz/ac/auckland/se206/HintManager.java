package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.GameState.Difficulty;

/**
 * This manager class contains methods for managing the hints in the game, including storing hints,
 * the number of hints available to the player, and the changing of the hint counter.
 */
public class HintManager {
  private static List<Node> hintNodes;
  private static List<Label> hintLabels;

  /** This method is responsible for initialising the hint manager. */
  public static void initialize() {
    // Initialize fields
    hintLabels = new ArrayList<Label>();
    hintNodes = new ArrayList<Node>();
  }

  /** Initialize the hints. This should initialize the hint counter label. */
  public static void initializeHintCounter() {
    // Check if the difficulty is easy - set label to infinity
    if (GameState.gameDifficulty == Difficulty.EASY) {
      setHintCounterInfinity();
      return;
    }

    // If the difficulty is hard, disable the hint button
    if (GameState.gameDifficulty == Difficulty.HARD) {
      disableHints();
    }

    // Initialize the hint counter label
    setHintCounter();
  }

  /**
   * Adds hint components to the list of components to be updated. These components should be
   * updated globally.
   *
   * @param label the label to be added to the list of labels.
   * @param node the node to be added to the list of nodes.
   */
  public static void addHintComponents(Label label, Node node) {
    // Add the label to the list of labels
    hintLabels.add(label);

    // Add the node to the list of nodes
    hintNodes.add(node);
  }

  /** Set the hint counter label to infinity (only in easy mode). */
  private static void setHintCounterInfinity() {
    // Loop through the hint counter labels and set to infinity
    for (Label lblHintCounter : hintLabels) {
      lblHintCounter.setText("\u221E/\u221E");
    }
  }

  /** Set the hint counter label. */
  private static void setHintCounter() {
    // Change all the hint counters to match the current hints
    for (Label lblHintCounter : hintLabels) {
      lblHintCounter.setText(GameState.hintCounter + "/" + GameState.maxHints);
    }
  }

  /** Update the hint counter to show the correct value. */
  public static void updateHintCounter() {
    // If the difficulty is easy, do nothing
    if (GameState.gameDifficulty == Difficulty.EASY) {
      return;
    }

    // Decrement the amount of hints
    GameState.hintCounter--;

    // Set the hint counter label
    setHintCounter();

    // If we have no more hints left, disable the hint buttons
    if (GameState.hintCounter == 0) {
      disableHints();
    }
  }

  /** This method is responsible for enabling all hint buttons. */
  public static void enableHints() {
    // Disable all hint buttons
    for (Node hint : hintNodes) {
      hint.setDisable(false);
    }
  }

  /** This method is responsible for disabling all hint buttons. */
  public static void disableHints() {
    // Disable all hint buttons
    for (Node hint : hintNodes) {
      hint.setDisable(true);
    }
  }
}
