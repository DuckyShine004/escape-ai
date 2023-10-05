package nz.ac.auckland.se206.controllers.menus;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.constants.GameState;
import nz.ac.auckland.se206.constants.Instructions;
import nz.ac.auckland.se206.utilities.Printer;
import nz.ac.auckland.se206.utilities.Timer;

/** Controller class for the terminal scene. */
public class TerminalController {
  @FXML private Pane paBack;
  @FXML private Pane paNext;
  @FXML private Pane paSkip;
  @FXML private Pane paTerminal;

  @FXML private Label lblDay;
  @FXML private Label lblTime;
  @FXML private Label lblYear;
  @FXML private Label lblMonth;
  @FXML private Label lblOperatingSystem;

  @FXML private Sphere sphGlobe;

  @FXML private TextArea taTerminal;

  @FXML private MeshView mvwSphere;

  /** Initializes the terminal screen. */
  @FXML
  private void initialize() {
    // Add the label to list of labels to be updated
    Timer.addLabel(lblTime);

    // Initialize the terminal
    initializeTerminal();

    // Initialize the globe
    initializeGlobe();

    // Initialize all labels
    initializeLabels();
  }

  /** When the mouse is hovering over the pane, the overlay appears (back). */
  @FXML
  private void onBackPaneEntered() {
    paBack.setStyle("-fx-background-color: rgb(29,30,37);");
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (back). */
  @FXML
  private void onBackPaneExited() {
    paBack.setStyle("-fx-background-color: rgb(20,20,23);");
  }

  /** When the mouse is hovering over the pane, the overlay appears (skip). */
  @FXML
  private void onSkipPaneEntered() {
    paSkip.setStyle("-fx-background-color: rgb(29,30,37);");
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (skip). */
  @FXML
  private void onSkipPaneExited() {
    paSkip.setStyle("-fx-background-color: rgb(20,20,23);");
  }

  /** When the mouse is hovering over the pane, the overlay appears (next). */
  @FXML
  private void onNextPaneEntered() {
    paNext.setStyle("-fx-background-color: rgb(29,30,37);");
  }

  /** When the mouse is not hovering over the pane, the overlay disappears (next). */
  @FXML
  private void onNextPaneExited() {
    paNext.setStyle("-fx-background-color: rgb(20,20,23);");
  }

  /** When back is clicked, go back to previous section (control room). */
  @FXML
  private void onBackPaneClicked() {
    App.setUi(AppUi.CONTROL);
  }

  /** When skip is clicked, skip the current dialogue. */
  @FXML
  private void onSkipPaneClicked() {
    // if there is no printing event going on, exit out of the function call
    if (!GameState.isPrinting) {
      return;
    }

    // stop the printing
    Printer.stop();

    // retrieve the current printed letter position and message
    int letterPosition = Printer.getCurrentLetterPosition();
    String message = Printer.getCurrentMessage();

    // print the remaining message to the text area
    taTerminal.appendText(message.substring(letterPosition, message.length() - 1));
  }

  /** When next is clicked, move on to the next thing. */
  @FXML
  private void onNextPaneClicked() {
    // if there is a printing event, exit out of the function call
    if (GameState.isPrinting) {
      return;
    }

    // print the instructions if it has not been printed yet
    if (!Instructions.isInstructionsPrinted) {
      printToTextArea(taTerminal, Instructions.instructions, Instructions.printSpeed);
      Instructions.isInstructionsPrinted = true;

      return;
    }

    // move on to the decryption puzzle
    App.setUi(AppUi.DECRYPTION);
  }

  /** Initializes the bootup message. */
  private void initializeTerminal() {
    printToTextArea(taTerminal, Instructions.bootup, Instructions.printSpeed);
  }

  /**
   * Initialize the spinning globe. This includes, setting texture and initializing rotation
   * animation for the globe.
   */
  private void initializeGlobe() {
    // Create a phong material for the globe
    PhongMaterial globeMaterial = new PhongMaterial();

    // Map texture and color to globe
    globeMaterial.setDiffuseColor(Color.WHITE);
    globeMaterial.setDiffuseMap(new Image("images/earth.jpg"));

    // Apply the phong material to the globe
    sphGlobe.setMaterial(globeMaterial);

    // Rotate the sphere - animation
    updateSphereRotation(20);
  }

  /** Initialize all labels used in the terminal scene. */
  private void initializeLabels() {
    // Initialize the date labels
    initializeDateLabels();

    // Initialize the operating system label
    lblOperatingSystem.setText(System.getProperty("os.name").toUpperCase());
  }

  /** Initialize all date labels. */
  private void initializeDateLabels() {
    // Retrieve the local date
    LocalDate date = LocalDate.now();

    // Set the current day label
    lblDay.setText(Integer.toString(date.getDayOfMonth()));

    // Set the month label
    String month = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    lblMonth.setText(month.toUpperCase());

    // Set the year label
    lblYear.setText(Integer.toString(Year.now().getValue()));
  }

  /**
   * Rotate the globe indefinitely.
   *
   * @param speed the speed at which the globe will be rotated.
   */
  private void updateSphereRotation(double speed) {
    // Create a new transition animation for the globe rotation
    RotateTransition rotateTransition = new RotateTransition(Duration.seconds(speed), sphGlobe);

    // Rotate the globe
    rotateTransition.setByAngle(360);
    rotateTransition.setAxis(Rotate.Y_AXIS);

    // Start the animation
    rotateTransition.setCycleCount(Animation.INDEFINITE);
    rotateTransition.play();
  }

  /**
   * Prints the message to be displayed to a text area.
   *
   * @param textArea the text area.
   * @param message the message to be printed to the text area.
   * @param speed the printing speed.
   */
  private void printToTextArea(TextArea textArea, String message, double speed) {
    // clear the text area before printing to the terminal
    textArea.clear();

    // print the message to the text area
    Printer.printText(textArea, message, speed);
  }
}
