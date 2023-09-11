package nz.ac.auckland.se206.controllers.menus;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.constants.Instructions;
import nz.ac.auckland.se206.utilities.Printer;

/** Controller class for the terminal scene. */
public class TerminalController {
  @FXML private TextArea taTerminal;

  /** Initializes the decryption puzzle. */
  @FXML
  private void initialize() {
    initializeInstructions();
  }

  private void initializeInstructions() {
    Printer.printText(taTerminal, Instructions.instruction, Instructions.printSpeed);
  }
}
