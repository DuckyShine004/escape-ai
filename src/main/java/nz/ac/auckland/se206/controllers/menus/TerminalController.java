package nz.ac.auckland.se206.controllers.menus;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
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
  @FXML private Pane paWarning;
  @FXML private Pane paTerminal;
  @FXML private Pane paLoadingBar;

  @FXML private Label lblDay;
  @FXML private Label lblTime;
  @FXML private Label lblYear;
  @FXML private Label lblMonth;
  @FXML private Label lblLoading;
  @FXML private Label lblProgress;
  @FXML private Label lblOperatingSystem;

  @FXML private Sphere sphGlobe;

  @FXML private TextArea taTerminal;

  @FXML private MeshView mvwSphere;

  private Timeline loadingTime;

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

    // Complete the loading bar
    completeLoadingBar();
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
      // Update the loading label
      lblLoading.setText("LOADING:");

      // Make the warning pane visible
      paWarning.setVisible(true);

      // Print the next instruction
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
   * Initialize the loading bar. The loading bar is just for visual effects, GPT is not used in the
   * instructions scene.
   */
  private void initializeLoadingBar() {
    // Reset the progress label to zero percent
    lblProgress.setText("0%");

    // Reset the loading bar
    paLoadingBar.setPrefWidth(0);

    // Create a timeline for the loading bar
    loadingTime =
        new Timeline(
            new KeyFrame(
                Duration.seconds(Instructions.printSpeed),
                event -> {
                  // Update the loading bar
                  updateLoadingBar();
                }));

    // Start the animation for the loading bar
    loadingTime.setCycleCount(Animation.INDEFINITE);
    loadingTime.play();
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
   * Update the loading bar. This is used to increase the size of the loading bar, and also to keep
   * track of how much more of the message remains.
   */
  private void updateLoadingBar() {
    // Get the current message length
    double messageLength = Printer.getCurrentMessage().length();

    // Get the current percentage of letters printed for the current message
    double percentage = ((double) Printer.getCurrentLetterPosition() / messageLength);

    // Update the width of the loading bar
    double width = 285 * percentage;
    paLoadingBar.setPrefWidth(width);

    // Check if we are at 100% of the width
    if (width == 285) {
      completeLoadingBar();
      return;
    }

    // Update the progress label
    lblProgress.setText((int) (percentage * 100) + "%");
  }

  /**
   * Prints the message to be displayed to a text area.
   *
   * @param textArea the text area.
   * @param message the message to be printed to the text area.
   * @param speed the printing speed.
   */
  private void printToTextArea(TextArea textArea, String message, double speed) {
    // Clear the text area before printing to the terminal
    textArea.clear();

    // Print the message to the text area
    Printer.printText(textArea, message, speed);

    // Initialize the loading bar
    initializeLoadingBar();
  }

  /** Complete the loading bar. This should be called when the hint is generated by GPT. */
  private void completeLoadingBar() {
    // Stop the loading bar timeline
    loadingTime.stop();

    // Update the progress label to 100%
    lblProgress.setText("100%");

    // Complete the loading bar
    paLoadingBar.setPrefWidth(285);
  }
}
