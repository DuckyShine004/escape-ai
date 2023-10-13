package nz.ac.auckland.se206;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Scanner;
import nz.ac.auckland.se206.controllers.menus.WinningScreenController;
import nz.ac.auckland.se206.utilities.Timer;

/**
 * This manager class contains methods for storing and updating the leaderboard seen at the end of
 * the game.
 */
public class LeaderboardManager {
  private static int timeBest;
  private static int timeTaken;

  private static String filePath;
  public static String timeBestMessage;
  public static String timeTakenMessage;

  private static File fileLocation;

  public static void initialize() throws Exception {
    // Initialize the file path
    initializeFilePath();

    // Initialize the scanner
    Scanner scanner = new Scanner(Paths.get(filePath));

    // Set the best time
    String[] tokens = scanner.nextLine().split(" ");
    timeBest = Integer.parseInt(tokens[0]);

    // Set the time labels
    timeTakenMessage = "--:--";
    timeBestMessage = (timeBest <= 360) ? Timer.getTimeTakenMessage(timeBest) : "--:--";

    // Close the scanner
    scanner.close();
  }

  private static void initializeFilePath() throws Exception {
    // Get the file location and file path
    fileLocation = new File(App.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    filePath = fileLocation.getParentFile().getParentFile().getPath();

    // // Add the next paths
    filePath = filePath.replace("\\", "/");
    filePath += "/src/main/resources/text/leaderboard.txt";
  }

  public static void update() throws Exception {
    // Get the current time
    timeTaken = Timer.getTimeTaken();

    // Check if the current time or the previous time is better
    timeBest = Math.min(timeTaken, timeBest);

    // Update the time messages
    timeBestMessage = (timeBest <= 360) ? Timer.getTimeTakenMessage(timeBest) : "--:--";
    timeTakenMessage = Timer.getTimeTakenMessage(timeTaken);

    // Set the time taken for string property
    WinningScreenController.spTimeTaken.setValue(timeTakenMessage);

    // Initialize the file writer and write to the file
    FileWriter fileWriter = new FileWriter(filePath, false);
    fileWriter.write(Integer.toString(timeBest));

    // Flush and close the file writer
    fileWriter.flush();
    fileWriter.close();
  }
}
