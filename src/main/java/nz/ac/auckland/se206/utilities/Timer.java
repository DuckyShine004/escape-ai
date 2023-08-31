package nz.ac.auckland.se206.utilities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Timer {
  private static Timeline timer;

  private static int minutes;
  private static int seconds;
  private static int time;

  public Timer(int time) {
    Timer.time = time;

    initialize();
  }

  private static void initialize() {
    timer =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  Timer.update();
                }));
  }

  public static String getMinutes() {
    return (minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes));
  }

  public static String getSeconds() {
    return (seconds < 10 ? "0" + String.valueOf(seconds) : String.valueOf(seconds));
  }

  public static String getTime() {
    return getMinutes() + ":" + getSeconds();
  }

  public static int getIntegerTime() {
    return time;
  }

  public static void update() {
    time -= 1;
    minutes = time / 60;
    seconds = time % 60;
  }

  public static boolean checkTime() {
    return time == 0;
  }

  public static void play() {
    timer.setCycleCount(time);
    timer.play();
  }

  public static void stop() {
    timer.stop();
  }
}
