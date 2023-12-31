package nz.ac.auckland.se206.utilities;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.AudioManager;
import nz.ac.auckland.se206.AudioManager.Clip;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;

/**
 * This utility class keeps track globally across all scenes the time the player has left in the
 * round of the game.
 */
public class Timer {
  private static Timeline timer;

  private static int minutes;
  private static int seconds;
  private static int time;
  private static int timeTaken;

  private static List<Label> labels;

  /** Initializes the timer. Call this method statically when you want to initialize a timer. */
  public static void initialize() {
    // Initially, time taken is zero
    timeTaken = 0;

    // Create a new arraylist of labels
    labels = new ArrayList<Label>();

    // Create a new timeline
    timer =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  Timer.update();
                }));
  }

  /**
   * Sets the timer's countdown time.
   *
   * @param time the input time. This should be 2, 4, or 6 minutes only (in seconds).
   */
  public static void setTime(int time) {
    Timer.time = time;

    minutes = time / 60;
    seconds = time % 60;

    // Set the timer label for every single label
    for (Label label : labels) {
      label.setText(getTime());
    }
  }

  /**
   * Adds a label to the arraylist, of course, the label should be timer related.
   *
   * @param label the label to be updated by the timer.
   */
  public static void addLabel(Label label) {
    labels.add(label);
  }

  /**
   * Return the string value of minutes left on the timer.
   *
   * @return String
   */
  public static String getMinutes() {
    return (minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes));
  }

  /**
   * Return the string value of seconds left on the timer.
   *
   * @return String
   */
  public static String getSeconds() {
    return (seconds < 10 ? "0" + String.valueOf(seconds) : String.valueOf(seconds));
  }

  /**
   * Return a the time in form of a string. The formatting is as follows: mm:ss. So for example, 1
   * minute and 22 seconds would look like: 01:22.
   *
   * @return String
   */
  public static String getTime() {
    return getMinutes() + ":" + getSeconds();
  }

  /**
   * Returns the integer value of the time left (in seconds).
   *
   * @return the integer time
   */
  public static int getIntegerTime() {
    return time;
  }

  /**
   * Retrieves the time taken for the player to complete the game.
   *
   * @return the total in-game elapsed time.
   */
  public static int getTimeTaken() {
    return timeTaken;
  }

  /**
   * Returns the string value of the time taken.
   *
   * @param time the time taken
   * @return the string value of the time taken
   */
  public static String getTimeTakenMessage(int time) {
    // Initialize fields
    int minutes = time / 60;
    int seconds = time % 60;

    // Get the digits based on the current time taken
    String minuteDigit = (minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes));
    String secondDigit = (seconds < 10 ? "0" + String.valueOf(seconds) : String.valueOf(seconds));

    return minuteDigit + ":" + secondDigit;
  }

  /**
   * Updates the timer and the text associated with it. This method is called inside of the timeline
   * method, which is inside of the initialize method.
   */
  public static void update() {
    time -= 1;
    timeTaken++;

    minutes = time / 60;
    seconds = time % 60;

    for (Label label : labels) {
      label.setText(getTime());
    }

    // Update the time taken for the player

    // Check if the timer has timed out
    if (isTimedOut()) {
      stop();
      AudioManager.loadAudio(Clip.GAME_OVER);
      App.setUi(AppUi.LOSING);
    }

    // Change the persona of the AI at 60 seconds remaining regardless of current persona
    if (getIntegerTime() == 60) {
      GameState.backStoryUpdated = 6;
    }
  }

  /**
   * Return a boolean value based on whether the timer has reached zero or not.
   *
   * @return boolean
   */
  public static boolean isTimedOut() {
    return time == 0;
  }

  /**
   * Reset the time taken by the player on each run to garuantee the leaderboard works perfectly.
   */
  public static void resetTimeTaken() {
    timeTaken = 0;
  }

  /** This method starts the timer by setting the cycle count. */
  public static void play() {
    timer.setCycleCount(time);
    timer.play();
  }

  /** This method is responsible for pausing or stopping the timer. */
  public static void stop() {
    timer.stop();
  }
}
