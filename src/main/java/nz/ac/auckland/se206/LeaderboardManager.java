package nz.ac.auckland.se206;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import nz.ac.auckland.se206.utilities.Timer;

public class LeaderboardManager {
  private static String filePath = "";

  private static String timeTaken;
  private static String timeBest;

  private static Scanner scanner;

  public static void initialize() throws IOException {
    scanner = new Scanner(Paths.get(filePath));

    // Set the best time
    String[] tokens = scanner.nextLine().split(" ");
    timeBest = Timer.getTimeTakenMessage(Integer.parseInt(tokens[1]));
  }

  public static String getTimeTaken() {
    return timeTaken;
  }

  public static String getTimeBest() {
    return timeBest;
  }

  public static void update() throws IOException {
    // Initialize fields
    int timeCurrent;
    int timePrevious;

    String[] tokens = scanner.nextLine().split(" ");
    timeBest = Timer.getTimeTakenMessage(Integer.parseInt(tokens[1]));

    timeTaken = Timer.getTimeTakenMessage(Timer.getTimeTaken());
  }
}
