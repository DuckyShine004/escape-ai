package nz.ac.auckland.se206.utilities;

import javafx.scene.layout.Pane;

/**
 * This utility class creates the wire object to be used in the logic gate puzzle, with a method to
 * change the colour of the wire.
 */
public class Wire {
  private Pane wire;
  private Pane bendWire;

  /**
   * This is the constructor for the wire object with one parameter.
   * @param wire
   */
  public Wire(Pane wire) {
    this.wire = wire;
  }

  /**
   * This is the constructor for the wire object with two parameters.
   * @param wire
   * @param bendWire
   */
  public Wire(Pane wire, Pane bendWire) {
    this.wire = wire;
    this.bendWire = bendWire;
  }

  /**
   * Takes in colour to set background pane colour to
   *
   * @param colour
   */
  public void setBackground(String colour) {
    // set background colour
    wire.setStyle("-fx-background-color: #" + colour);

    // if bend Wire exists
    if (bendWire != null) {
      bendWire.setStyle("-fx-background-color: #" + colour);
    }
  }
}
