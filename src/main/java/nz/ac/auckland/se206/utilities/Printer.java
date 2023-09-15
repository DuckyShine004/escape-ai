package nz.ac.auckland.se206.utilities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.util.Duration;
import nz.ac.auckland.se206.constants.GameState;

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

                  // append the current letter to the text area
                  textArea.appendText(currentLetter);

                  letterPosition++;
                }));

    GameState.isPrinting = true;

    // printing event will finish once the full message has been displayed
    printTextEvent.setCycleCount(message.length());
    printTextEvent.play();

    // let the program know the printing event is finished
    printTextEvent.setOnFinished(
        e -> {
          GameState.isPrinting = false;
        });
  }

  /**
   * Method is called to retrieve the last printed position of the letter when the printing event is
   * CANCELLED.
   *
   * @return the letter position of the current message.
   */
  public static int getLetterPosition() {
    return letterPosition;
  }
}
