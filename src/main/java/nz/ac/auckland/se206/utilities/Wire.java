package nz.ac.auckland.se206.utilities;

import java.io.FileInputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This utility class creates the wire object to be used in the logic gate puzzle, with a method to
 * change the colour of the wire.
 */
public class Wire {
  private ImageView wire;

  private String onLogicColour = "00ff00"; // green
  private String offLogicColour = "ff0000"; // red

  private Image greenWire;
  private Image redWire;

  /**
   * Constructor method for creating a new wire object.
   *
   * @param wire the image of the wire.
   */
  public Wire(ImageView wire) {
    this.wire = wire;

    try {
      this.greenWire =
          new Image(
              new FileInputStream(
                  "src/main/resources/images/BreakerRoom/LogicGatePuzzle/Wires/greenWire.png"));
      this.redWire =
          new Image(
              new FileInputStream(
                  "src/main/resources/images/BreakerRoom/LogicGatePuzzle/Wires/redWire.png"));
    } catch (Exception e) {
      e.printStackTrace();
    }

    setBackground(false);
  }

  /**
   * This will set the background colour of the wires. The colour can for the wires can either be
   * red or green.
   *
   * @param colour the colour of the wire.
   */
  public void setBackground(Boolean colour) {
    // set background colour
    if (colour) {
      wire.setImage(greenWire);
    } else {
      wire.setImage(redWire);
    }
  }
}
