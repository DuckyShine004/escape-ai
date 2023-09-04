package nz.ac.auckland.se206.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class LogicGate {

  // logic gate logic
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

  public LogicGate(Logic type) {
    this.type = type;
    loadGateImage(type);
    loadGateTable(type);
  }

  public Logic getType() {
    return this.type;
  }

  public Image getImage() {
    return this.img;
  }

  public Image getTable() {
    return this.tableImg;
  }

  public boolean equals(LogicGate logicGate) {
    return logicGate.getType() == this.type ? true : false;
  }

  private void loadGateImage(Logic logic) {

    try {
      System.out.println("=>  " + logic.toString());
      this.img =
          new Image(
              new FileInputStream(
                  "src/main/resources/images/BreakerRoom/LogicGatePuzzle/Gates/"
                      + logic.toString()
                      + ".jpg"));
    } catch (FileNotFoundException e) {
      System.out.println("Could not find image");
    }
  }

  private void loadGateTable(Logic logic) {
    try {
      System.out.println("Table Loaded for " + logic.toString());
      this.tableImg =
          new Image(
              new FileInputStream(
                  "src/main/resources/images/BreakerRoom/LogicGatePuzzle/Tables/"
                      + logic.toString()
                      + "OUT.jpg"));
    } catch (FileNotFoundException e) {
      System.out.println("Could not find image");
    }
  }
}
