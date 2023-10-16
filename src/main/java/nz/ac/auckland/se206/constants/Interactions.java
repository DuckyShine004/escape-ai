package nz.ac.auckland.se206.constants;

/**
 * This constants class contains the boolean values of whether or not the player has interacted with
 * specific things in the game, which can also be reset by calling the reset method.
 */
public class Interactions {
  /** Indicates whether the desktop in the office room has been clicked or not. */
  public static boolean isDesktopClicked = false;

  /** Indicates whether the circuit box in the breaker room has been clicked or not. */
  public static boolean isCircuitBoxClicked = false;

  /** Indicates whether the control panel in the control room has been clicked or not. */
  public static boolean isControlPanelClicked = false;

  /** Indicates whether the control keyboard in the control room has been clicked or not. */
  public static boolean isControlKeyboardClicked = false;

  /** Reset all variables to be resetted in this class to their initial value. */
  public static void reset() {
    // Reset desktop clicked
    isDesktopClicked = false;

    // Reset circuit breaker clicked
    isCircuitBoxClicked = false;

    // Reset control panel clicked
    isControlPanelClicked = false;

    // reset control keybooard clicked
    isControlKeyboardClicked = false;
  }
}
