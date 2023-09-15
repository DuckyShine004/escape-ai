package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.Instructions;
import nz.ac.auckland.se206.utilities.Printer;

/** Controller class for the terminal scene. */
public class TerminalController {
  @FXML private Pane paBack;
  @FXML private Pane paNext;
  @FXML private Pane paSkip;
  @FXML private Pane paBackOverlay;
  @FXML private Pane paNextOverlay;
  @FXML private Pane paSkipOverlay;

  @FXML private TextArea taTerminal;

  /** Initializes the terminal screen. */
  @FXML
  private void initialize() {
    initializeTerminal();
  }

  /** When the mouse is hovering over the pane, the overlay appears (back). */
  @FXML
  private void onBackPaneEntered() {
    paBackOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (back). */
  @FXML
  private void onBackPaneExited() {
    paBackOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (next). */
  @FXML
  private void onNextPaneEntered() {
    paNextOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (next). */
  @FXML
  private void onNextPaneExited() {
    paNextOverlay.setVisible(false);
  }

  /** When the mouse is hovering over the pane, the overlay appears (skip). */
  @FXML
  private void onSkipPaneEntered() {
    paSkipOverlay.setVisible(true);
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (skip). */
  @FXML
  private void onSkipPaneExited() {
    paSkipOverlay.setVisible(false);
  }

  /** When back is clicked, go back to previous section (control room). */
  @FXML
  private void onBackPaneClicked() {
    App.setUi(AppUi.CONTROL);
  }

  /** When next is clicked, move on to the next thing. */
  @FXML
  private void onNextPaneClicked() {
    // if there is a printing event, exit out of the function call
    if (GameState.isPrinting) {
      return;
    }

    // print the instructions if it has not been printed yet
    if (!Instructions.isInstructionsPrinted) {
      printToTextArea(taTerminal, Instructions.instructions, Instructions.printSpeed);
      Instructions.isInstructionsPrinted = true;

      return;
    }

    // move on to the decryption puzzle
    App.setUi(AppUi.DECRYPTION);
  }

  /** When skip is clicked, skip the current dialogue. */
  @FXML
  private void onSkipPaneClicked() {
    // if there is no printing event going on, exit out of the function call
    if (!GameState.isPrinting) {
      return;
    }

    // stop the printing
    Printer.stop();

    // retrieve the current printed letter position and message
    int letterPosition = Printer.getCurrentLetterPosition();
    String message = Printer.getCurrentMessage();

    // print the remaining message to the text area
    taTerminal.appendText(message.substring(letterPosition, message.length() - 1));
  }

  /** Initializes the bootup message. */
  private void initializeTerminal() {
    printToTextArea(taTerminal, Instructions.bootup, Instructions.printSpeed);
  }

  /**
   * Prints the message to be displayed to a text area.
   *
   * @param textArea the text area.
   * @param message the message to be printed to the text area.
   * @param speed the printing speed.
   */
  private void printToTextArea(TextArea textArea, String message, double speed) {
    // clear the text area before printing to the terminal
    textArea.clear();

    // print the message to the text area
    Printer.printText(textArea, message, speed);
  }
}
