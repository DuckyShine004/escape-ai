package nz.ac.auckland.se206.utilities;

import javafx.scene.layout.Pane;

public class Wire {
  private Pane wire;

  private String onLogicColour = "00ff00"; // green
  private String offLogicColour = "ff0000"; // red

  public Wire(Pane wire) {
    this.wire = wire;
  }

  /**
   * Takes in colour to set background pane colour to
   *
   * @param colour
   */
  public void setBackground(Boolean colour) {
    // set background colour
    if (colour) {
      wire.setStyle("-fx-background-color: #" + onLogicColour);
    } else {
      wire.setStyle("-fx-background-color: #" + offLogicColour);
    }

  }
}
