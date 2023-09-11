package nz.ac.auckland.se206.utilities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

public class Printer {
  private static int letterPosition;

  /**
   * Prints the message onto the text area, letter by letter.
   *
   * @param textArea the passed in text area
   * @param message the message to be printed
   */
  public static void printText(TextArea textArea, String message, double speed) {
    letterPosition = 0;

    Timeline printTextEvent =
        new Timeline(
            new KeyFrame(
                Duration.seconds(speed),
                event -> {
                  // get the current letter of the string to be appended to the text area
                  char currentCharacter = message.charAt(letterPosition);
                  String currentLetter = String.valueOf(currentCharacter);

                  textArea.appendText(currentLetter);

                  letterPosition++;
                }));

    printTextEvent.setCycleCount(message.length());
    printTextEvent.play();
  }
}
