package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.GameState.Difficulty;

/** The HintManager class will help keep the hint counter the same */
public class HintManager {
  private static List<Label> hintLabels;
  private static List<Polygon> hintPolygons;

  public static void initialize() {
    // Initialize fields
    hintLabels = new ArrayList<Label>();
    hintPolygons = new ArrayList<Polygon>();
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
      disableHintButtons();
    }

    // Initialize the hint counter label
    setHintCounter();
  }

  /**
   * Adds hint components to the list of components to be updated. These components should be
   * updated globally.
   *
   * @param label the label to be added to the list of labels.
   * @param polygon the polygon to be added to the list of polygons.
   */
  public static void addHintComponents(Label label, Polygon polygon) {
    // Add the label to the list of labels
    hintLabels.add(label);

    // Add the button to the list of buttons
    hintPolygons.add(polygon);
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
      disableHintButtons();
    }
  }

  private static void disableHintButtons() {
    // Disable all hint buttons
    for (Polygon pgHint : hintPolygons) {
      pgHint.setDisable(true);
    }
  }
}
