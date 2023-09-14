package nz.ac.auckland.se206.controllers.puzzles;

import javafx.scene.layout.Pane;

public class Wire {
  private Pane wire;
  private Pane bendWire;

  public Wire(Pane wire) {
    this.wire = wire;
  }

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
