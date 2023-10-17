package nz.ac.auckland.se206.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

/**
 * This class represents a logic gate, which is used in the logic gate puzzle. It contains the logic
 * gate type, the image of the gate, and the image of the gate's truth table.
 */
public class LogicGate {

  /** This enum represents the different types of logic gates that can be used in the logic gate. */
  public enum Logic {
    AND,
    OR,
    NOR,
  }

  private Logic type;
  @FXML private Image img;
  @FXML private Image tableImg;

  /** Constructor for intializing logic gate. */
  public LogicGate(Logic type) {
    this.type = type;
    loadGateImage(type);
    loadGateTable(type);
  }

  /**
   * A getter to return the type of the logic gate.
   *
   * @return the type of the logic gate
   */
  public Logic getType() {
    return this.type;
  }

  /**
   * A getter to return the image of the logic gate.
   *
   * @return the image of the logic gate
   */
  public Image getImage() {
    return this.img;
  }

  /**
   * A getter to return the image of the logic gate's truth table.
   *
   * @return the image of the logic gate's truth table
   */
  public Image getTable() {
    return this.tableImg;
  }

  /**
   * A method to check if two logic gates are equal.
   *
   * @param logicGate the logic gate to be compared to
   * @return true if the logic gates are equal, false otherwise
   */
  public boolean equals(LogicGate logicGate) {
    return logicGate.getType() == this.type ? true : false;
  }

  /** This method loads the logic gate image method. */
  private void loadGateImage(Logic logic) {

    try {
      // try to load gate image using fileInputStream and relative path
      // src/main/resources/images/BreakerRoom/LogicGatePuzzle/Gates/...'AND' + '.jpg'
      this.img =
          new Image(
              new FileInputStream(
                  "src/main/resources/images/BreakerRoom/LogicGatePuzzle/Gates/"
                      + logic.toString()
                      + ".png"));
    } catch (FileNotFoundException e) {
      System.out.println("FAILED TO FIND LOGIC GATE IMAGE [gate]");
    }
  }

  /** This method loads the logic gate table image method. */
  private void loadGateTable(Logic logic) {
    try {

      // try to load table image using fileInputStream and relative path
      // src/main/resources/images/BreakerRoom/LogicGatePuzzle/Tables/...'AND'+'OUT.jpg'
      this.tableImg =
          new Image(
              new FileInputStream(
                  "src/main/resources/images/BreakerRoom/LogicGatePuzzle/Tables/"
                      + logic.toString()
                      + "OUT.jpg"));
    } catch (FileNotFoundException e) {
      System.out.println("FAILED TO FIND LOGIC GATE IMAGE [table]");
    }
  }
}
