package nz.ac.auckland.se206;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import nz.ac.auckland.se206.utilities.Timer;

public class LeaderboardManager {
  private static int timeBest;
  private static int timeTaken;

  public static String timeBestMessage;
  public static String timeTakenMessage;

  public static void initialize() throws Exception {
    // Initialize the scanner
    Scanner scanner = new Scanner(Paths.get(getFilePath()));

    // Set the best time
    String[] tokens = scanner.nextLine().split(" ");
    timeBest = Integer.parseInt(tokens[0]);

    // Set the best time label
    timeBestMessage = (timeBest <= 360) ? Timer.getTimeTakenMessage(timeBest) : "--:--";

    update();

    // Close the scanner
    scanner.close();
  }

  private static String getFilePath() {
    String filePath = App.class.getResource("/text/leaderboard.txt").toString();

    return filePath.substring(6, filePath.length());
  }

  public static void update() throws Exception {
    // Get the current time
    timeTaken = Timer.getTimeTaken();

    System.out.println(timeTaken);

    // Check if the current time or the previous time is better
    timeBest = Math.min(timeTaken, timeBest);

    // Update the time messages
    timeBestMessage = (timeBest <= 360) ? Timer.getTimeTakenMessage(timeBest) : "--:--";
    timeTakenMessage = Timer.getTimeTakenMessage(timeTaken);

    System.out.println(timeTakenMessage);

    // Initialize the scanner and write to the file
    try (FileWriter fileWriter = new FileWriter(getFilePath())) {
      fileWriter.write(timeBest);
      System.out.println("Data has been written to the file.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
