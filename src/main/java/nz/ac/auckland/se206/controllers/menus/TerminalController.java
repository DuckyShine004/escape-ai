package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.constants.Instructions;
import nz.ac.auckland.se206.utilities.Printer;

/** Controller class for the terminal scene. */
public class TerminalController {
  @FXML private Pane paBack;
  @FXML private Pane paNext;
  @FXML private Pane paBackOverlay;
  @FXML private Pane paNextOverlay;

  @FXML private TextArea taTerminal;

  /** Initializes the terminal screen. */
  @FXML
  private void initialize() {
    initializeInstructions();
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

  /** When back is clicked, go back to previous section. */
  @FXML
  private void onBackPaneClicked() {
    System.out.println("Back Clicked");
  }

  /** When next is clicked, move on to the next thing. */
  @FXML
  private void onNextPaneClicked() {
    System.out.println("Next Clicked");
  }

  /** Prints the initial instruction onto the screen. */
  private void initializeInstructions() {
    Printer.printText(taTerminal, Instructions.instruction, Instructions.printSpeed);
  }
}
