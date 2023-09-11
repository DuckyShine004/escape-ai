package nz.ac.auckland.se206.utilities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.util.Duration;

public class Printer {
  private static int letterPosition;

  public static void printText(TextArea textArea, String message) {
    letterPosition = 0;

    Timeline printTextEvent =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.025),
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
