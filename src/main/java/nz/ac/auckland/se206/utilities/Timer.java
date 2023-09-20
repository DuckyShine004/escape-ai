package nz.ac.auckland.se206.utilities;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** The timer class keeps track of the time left for the player. */
public class Timer {
  private static Timeline timer;

  private static int minutes;
  private static int seconds;
  private static int time;

  private static List<Label> labels;

  /**
   * Initializes the timer. Call this method statically when you want to initialize a timer.
   *
   * @param time the input time. This should be 2, 4, or 6 minutes only (in seconds).
   */
  public static void initialize() {
    labels = new ArrayList<Label>();

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
   * @return int
   */
  public static int getIntegerTime() {
    return time;
  }

  /**
   * Updates the timer and the text associated with it. This method is called inside of the timeline
   * method, which is inside of the initialize method.
   */
  public static void update() {
    time -= 1;

    minutes = time / 60;
    seconds = time % 60;

    for (Label label : labels) {
      label.setText(getTime());
    }

    if (checkTime()) {
      stop();
      App.setUi(AppUi.LOSING);
    }

    // if (time == 30) {
    //   SceneManager.getInstance().initGptThread();
    // }
  }

  /**
   * Return a boolean value based on whether the timer has reached zero or not.
   *
   * @return boolean
   */
  public static boolean checkTime() {
    return time == 0;
  }

  /** Start the timer. */
  public static void play() {
    timer.setCycleCount(time);
    timer.play();
  }

  /** Pause or stop the timer. */
  public static void stop() {
    timer.stop();
  }
}
