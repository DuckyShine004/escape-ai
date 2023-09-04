package nz.ac.auckland.se206.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class LogicGate {

  // logic gate enum type
  public enum Logic {
    AND,
    NAND,
    NOR,
    OR,
    XNOR,
    XOR,
  }

  private Logic type;
  @FXML private Image img;
  @FXML private Image tableImg;

  /** constructor for intializing logic gate */
  public LogicGate(Logic type) {
    this.type = type;
    loadGateImage(type);
    loadGateTable(type);
  }

  // get logic gate type
  public Logic getType() {
    return this.type;
  }

  // get the gate image of the gate
  public Image getImage() {
    return this.img;
  }

  // get the table image of the gate
  public Image getTable() {
    return this.tableImg;
  }

  // equals method, does given LogicGate type equal the current
  public boolean equals(LogicGate logicGate) {
    return logicGate.getType() == this.type ? true : false;
  }

  /** Loading logic gate image method */
  private void loadGateImage(Logic logic) {

    try {

      // try to load gate image using fileInputStream and relative path
      // src/main/resources/images/BreakerRoom/LogicGatePuzzle/Gates/...'AND' + '.jpg'
      this.img =
          new Image(
              new FileInputStream(
                  "src/main/resources/images/BreakerRoom/LogicGatePuzzle/Gates/"
                      + logic.toString()
                      + ".jpg"));
    } catch (FileNotFoundException e) {
      System.out.println("FAILED TO FIND LOGIC GATE IMAGE [gate]");
    }
  }

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
